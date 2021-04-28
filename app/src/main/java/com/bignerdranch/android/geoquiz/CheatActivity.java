package com.bignerdranch.android.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String ANSWER_IS_TRUE = "answer_is_true";
    private final String Tag = "CheatActivity";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String IS_CHEATED = "cheated";

    private boolean mAnswerIsTrue;
    private boolean mIsAnswerShown = false;
    private Button mShowAnswer;
    private TextView mAnswerTextView;
    private TextView mApiLevelTextView;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        Log.d(Tag, "onCreate");

        mShowAnswer = findViewById(R.id.showAnswerButton);
        mAnswerTextView = findViewById(R.id.answerTextView);
        mApiLevelTextView = findViewById(R.id.apiLevel);
//        Log.i("API", Build.VERSION.CODENAME);
        mApiLevelTextView.setText("API level " + Build.VERSION.SDK_INT);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsAnswerShown = true;
                showAnswer(mAnswerIsTrue, mAnswerTextView);
                setAnswerShownResult(mIsAnswerShown);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    showAnimator(mShowAnswer);
                }else {
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            }

        });

        if(savedInstanceState!=null){
            mIsAnswerShown = savedInstanceState.getBoolean(IS_CHEATED, false);
            mAnswerIsTrue = savedInstanceState.getBoolean(ANSWER_IS_TRUE, false);
            showAnswer(mAnswerIsTrue, mAnswerTextView);
            setAnswerShownResult(mIsAnswerShown);
        }else {
            mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        }

    }

    private void showAnimator(View v) {
        int cx = v.getWidth() / 2;
        int cy = v.getHeight() / 2;
        float radius = v.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(
                v,
                cx,
                cy,
                radius,
                0
        );
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }
    private static void showAnswer(boolean answerIsTrue, TextView answerTextView) {
        if(answerIsTrue){
            answerTextView.setText(R.string.true_button);
        }else {
            answerTextView.setText(R.string.false_button);
        }
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_CHEATED, mIsAnswerShown);
        outState.putBoolean(ANSWER_IS_TRUE, mAnswerIsTrue);
    }
}