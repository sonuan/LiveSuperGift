package com.sonuan.livegift.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.sonuan.livegift.R;
import com.sonuan.livegift.widget.LiveFerrariBuilder;

public class FerrariActivity2 extends AppCompatActivity {

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, FerrariActivity2.class);
        context.startActivity(intent);
    }

    private FrameLayout mLiveGiftBaseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ferrari2);
        mLiveGiftBaseLayout = (FrameLayout) findViewById(R.id.fl_live_gift);


        final LiveFerrariBuilder liveGiftBaseBuilder = new LiveFerrariBuilder(mLiveGiftBaseLayout);
        mLiveGiftBaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveGiftBaseBuilder.start();
            }
        });
        liveGiftBaseBuilder.setAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                liveGiftBaseBuilder.toggle();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        liveGiftBaseBuilder.start();
                    }
                }, 100);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
