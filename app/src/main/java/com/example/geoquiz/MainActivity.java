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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionText = findViewById(R.id.question_text_view);
        //获取当前信息问题的序号
        //int question = mQuestions[mCurrentIndex].getTextResId();
        //mQuestionText.setText(question);
        updateQuestion();


        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkQuestion(true);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkQuestion(false);
            }
        });

        mPreButton = findViewById(R.id.pre_button);
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

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                //int question = mQuestions[mCurrentIndex].getTextResId();
                //mQuestionText.setText(question);
                updateQuestion();
            }
        });
    }

    /**
     * 使用updateQuestion封装公共代码
     */
    private void updateQuestion(){
        int question = mQuestions[mCurrentIndex].getTextResId();
        mQuestionText.setText(question);
    }

    /**
     * 使用checkQuestion检查答案的正确性
     */
    private void checkQuestion(boolean userPressedTrue){
        boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;

        }else {
            messageResId = R.string.incorrect_toast;
        }

        Toast mToast = Toast.makeText(getApplicationContext(),messageResId,Toast.LENGTH_SHORT);
        //设置Toast的重力值
        mToast.setGravity(Gravity.TOP,0,200);
        mToast.show();
    }
}
