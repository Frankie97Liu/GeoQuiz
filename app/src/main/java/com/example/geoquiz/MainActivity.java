package com.example.geoquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mPreButton;
    private Button mNextButton;

    private Button mCheatButton;

    private Question[] mQuestions = new Question[]{
            new Question(R.string.question_australia,true),
            new Question(R.string.question_oceans,true),
            new Question(R.string.question_mideast,false),
            new Question(R.string.question_africa,false),
            new Question(R.string.question_americas,true),
            new Question(R.string.question_asia,true)

    };

    private int mCurrentIndex = 0;

    private TextView mQuestionText;

    //定义key值
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWER = "answer";
    private static final String Key_ANSWER_INDEX1 = "index1";
    private static final String Third_BUG = "third bug";

    //requestCode
    private static final int REQUEST_CODE_CHEAT = 0;

    //正确答题数目
    private double mCorrectAnswer = 0;

    //回答问题总数
    private double answerLength = 0;

    private boolean[] mIsCheater = new boolean[mQuestions.length];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //检查存储的bundle数据
        if (savedInstanceState!=null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);

            int[] answerList = savedInstanceState.getIntArray(KEY_ANSWER);
            for (int i = 0;i<mQuestions.length; i++){
                mQuestions[i].setIsAnswered(answerList[i]);
            }

            mIsCheater = savedInstanceState.getBooleanArray(Third_BUG);//防止旋转作弊漏洞
        }
        for (int i = 0;i<mQuestions.length;i++){
            mIsCheater[i]=false;
        }

        mQuestionText = findViewById(R.id.question_text_view);

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(this);
        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(this);
        mPreButton = findViewById(R.id.pre_button);
        mPreButton.setOnClickListener(this);
        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(this);
        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(this);

        //显示当前界面
        updateQuestion();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.true_button:
                checkQuestion(true);
                break;
            case R.id.false_button:
                checkQuestion(false);
                break;
            case R.id.pre_button:
                if (mCurrentIndex>0){
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestions.length;
                }else if (mCurrentIndex == 0){
                    mCurrentIndex = mQuestions.length-1;
                }
                updateQuestion();
                break;
            case R.id.next_button:
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                updateQuestion();
                answerLength++;
                showScore();
                break;
            case R.id.cheat_button:
                //start CheatActivity
                boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();//传递extra
                Intent intent = CheatActivity.newIntent(MainActivity.this,answerIsTrue);
                //startActivity(intent);
                //从子Activity返回结果
                startActivityForResult(intent,REQUEST_CODE_CHEAT);
                break;
            default:

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE_CHEAT:
                if (resultCode == RESULT_OK){
                    mIsCheater[mCurrentIndex] = CheatActivity.wasAnswerShown(data);
                }
                break;
                default:
        }
    }

    /**
     * 保存数据以应对屏幕旋转
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存题目序号
        outState.putInt(KEY_INDEX,mCurrentIndex);

        //创建已经答题的列表
        int[] answerList = new int[mQuestions.length];
        for (int i = 0; i<mQuestions.length; i++){
            answerList[i] = mQuestions[i].getIsAnswered();
        }
        //保存已经答过题的列表
        outState.putIntArray(KEY_ANSWER,answerList);

        //保存是否已经作弊的信息
        outState.putBooleanArray(Third_BUG,mIsCheater);
    }

    /**
     * 使用updateQuestion封装公共代码
     */
    private void updateQuestion(){
        int question = mQuestions[mCurrentIndex].getTextResId();
        mQuestionText.setText(question);
        //判断代码的可见性
        ButtonEnabled();
    }

    /**
     * 使用checkQuestion检查答案的正确性
     */
    private void checkQuestion(boolean userPressedTrue){
        boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        //显示已答过该题
        mQuestions[mCurrentIndex].setIsAnswered(1);

        if (mIsCheater[mCurrentIndex]){
            messageResId = R.string.judgement_toast;
            //自动跳转下一题
            mCurrentIndex = (mCurrentIndex + 1)%mQuestions.length;
            updateQuestion();
        }else {
            if (userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
                mCorrectAnswer++;

            }else {
                messageResId = R.string.incorrect_toast;
            }
        }

        ButtonEnabled();

        Toast mToast = Toast.makeText(getApplicationContext(),messageResId,Toast.LENGTH_SHORT);
        //设置Toast的重力值
        mToast.setGravity(Gravity.TOP,0,200);
        mToast.show();
    }

    /**
     * 显示按钮的可见性
     */
    private void ButtonEnabled(){
        if (mQuestions[mCurrentIndex].getIsAnswered()>0){
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }else{
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }

    /**
     * 显示最后得分
     */
    private void showScore(){
        if (answerLength == mQuestions.length){
            double i = mCorrectAnswer / mQuestions.length;
            double score = i * 100;
            Toast.makeText(this, String.valueOf(score), Toast.LENGTH_SHORT).show();
        }
    }
}
