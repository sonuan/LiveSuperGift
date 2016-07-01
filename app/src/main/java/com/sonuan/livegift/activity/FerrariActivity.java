package com.sonuan.livegift.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sonuan.livegift.R;
import com.sonuan.livegift.gift.FerrariRenderer;
import com.sonuan.livegift.gift.base.GiftBaseDrawable;

import java.io.IOException;


public class FerrariActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = FerrariActivity.class.getSimpleName();
    private GiftBaseDrawable mGiftBaseDrawable;

    private ImageView mIVGift;
    private ImageView mImageView;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, FerrariActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ferrari);
        mIVGift = (ImageView) findViewById(R.id.iv_gift);
        mIVGift.setOnClickListener(this);
        mImageView = (ImageView) findViewById(R.id.iv_gif);
        mImageView.setOnClickListener(this);
        findViewById(R.id.btn_start).setOnClickListener(this);
        FerrariRenderer ferrariRenderer = new FerrariRenderer(this);
        ferrariRenderer.setDuration(3000);
        ferrariRenderer.addRenderListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mGiftBaseDrawable.stop();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        try {
            mGiftBaseDrawable = new GiftBaseDrawable(getResources(), R.drawable.gif_car_run, ferrariRenderer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_gift:
                Toast.makeText(FerrariActivity.this, "gift", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_gif:
                if (true) {
                    return;
                }
                mImageView.setImageDrawable(mGiftBaseDrawable);
                mGiftBaseDrawable.start();
                break;
            case R.id.btn_start:
                GiftAnimUtils.transAnim(mIVGift, 700, 300, 300, 300).start();
                break;
        }
    }

    @Override
    protected void onStop() {
        mGiftBaseDrawable.stop();
        super.onStop();
    }
}
