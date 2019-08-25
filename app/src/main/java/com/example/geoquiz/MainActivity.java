package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mPreButton;
    private Button mNextButton;

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

    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWER = "answer";

    //正确答题数目
    private double mCorrectAnswer = 0;

    //回答问题总数
    private double answerLength = 0;

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
        }

        mQuestionText = findViewById(R.id.question_text_view);

        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mPreButton = findViewById(R.id.pre_button);
        mNextButton = findViewById(R.id.next_button);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkQuestion(true);
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkQuestion(false);
            }
        });
        mPreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex>0){
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestions.length;
                }else if (mCurrentIndex == 0){
                    mCurrentIndex = mQuestions.length-1;
                }
                updateQuestion();
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                updateQuestion();
                answerLength++;
                showScore();
            }
        });

        //显示当前界面
        updateQuestion();
    }

    /**
     * 保存数据以应对屏幕旋转
     * @param outState
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

        if (userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
            mCorrectAnswer++;

        }else {
            messageResId = R.string.incorrect_toast;
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
