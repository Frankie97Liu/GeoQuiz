package com.example.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import static com.example.geoquiz.MainActivity.EXTRA_ANSWER_IS_TRUE;
import static com.example.geoquiz.MainActivity.EXTRA_CHEAT_CHANCE;

public class CheatActivity extends AppCompatActivity {

    //private static final String EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true";

    //为extra增加常量
    private static final String EXTRA_ANSWER_SHOWN = "com.example.geoquiz.answer_shown";



    //增加key
    private static final String KEY_ANSWER_IS_TRUE = "answer_is_true";
    private static final String KEY_ANSWER_INDEX = "answer_index";

    //答案是不是true
    private boolean mAnswerIsTrue;

    private TextView mAnswerText;

    private Button mShowAnswerButton;

    private TextView mVersionText;

    //记录作弊次数
    private static int mCheatChance;

    /*
    public static Intent newIntent(Context packageContext,boolean answerIsTrue){
        Intent intent = new Intent(packageContext,CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return intent;
    }
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        //检查存储的Bundle数据
        if (savedInstanceState!=null){
            mAnswerIsTrue = savedInstanceState.getBoolean(KEY_ANSWER_IS_TRUE);
            //判断是否看答案
            if (mAnswerIsTrue){
                setAnswerShowResult(mAnswerIsTrue);
            }
        }

        //获取extra,
        mCheatChance = getIntent().getIntExtra(EXTRA_CHEAT_CHANCE,3);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);


        mAnswerText = findViewById(R.id.answer_text_view);
        mShowAnswerButton = findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheatChance--;
                if (mAnswerIsTrue){
                    mAnswerText.setText(R.string.true_button);
                }else{
                    mAnswerText.setText(R.string.false_button);
                }
                setAnswerShowResult(true);

                //检查Android设备版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }else{
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        mVersionText = findViewById(R.id.version_text);
        //获取设备编译版本并显示
        CharSequence cs = "API Level"+ Build.VERSION.SDK_INT;
        mVersionText.setText(cs);
    }
    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    private void setAnswerShowResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        data.putExtra(EXTRA_CHEAT_CHANCE,mCheatChance);
        setResult(RESULT_OK,data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存Answer信息
        outState.putBoolean(KEY_ANSWER_IS_TRUE,mAnswerIsTrue);

    }
}
