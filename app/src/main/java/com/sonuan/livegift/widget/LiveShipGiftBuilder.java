package com.sonuan.livegift.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.sonuan.livegift.R;

/**
 * @author: wusongyuan
 * @date: 2016.08.13
 * @desc:
 */
public class LiveShipGiftBuilder extends LiveGiftBaseBuilder {

    private View mIvWavaAbove;
    private View mIvWavaBelow;

    public LiveShipGiftBuilder(ViewGroup viewGroup) {
        super(viewGroup, new int[]{1500, 2000, 1500});
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.item_live_gift_ship);
        initViews();
        initAnims();
    }

    private void initViews() {
        mIvWavaAbove = findViewById(R.id.ic_live_gift_ship_wave_above);
        mIvWavaBelow = findViewById(R.id.ic_live_gift_ship_wave_below);
    }

    private AnimatorSet mWaveAnimatorSet;

    private void initAnims() {
        ObjectAnimator aboveMoveAnimator = ObjectAnimator.ofFloat(mIvWavaAbove, "TranslationY", 0, -20).setDuration(
                1200);
        aboveMoveAnimator.setRepeatCount(Animation.INFINITE);
        aboveMoveAnimator.setRepeatMode(Animation.REVERSE);
        ObjectAnimator belowMoveAnimator = ObjectAnimator.ofFloat(mIvWavaBelow, "TranslationY", 0, 10).setDuration(
                1200);
        belowMoveAnimator.setRepeatCount(Animation.INFINITE);
        belowMoveAnimator.setRepeatMode(Animation.REVERSE);
        final int aboveAlphaDuration = 5000;
        final float one = 400f / 5000.0f;
        final float two = 600f / 5000.0f;
        final float three = 3000f / 5000.0f;
        final float four = 1000f / 5000.0f;
        ObjectAnimator aboveAlphaAnimator = ObjectAnimator.ofFloat(mIvWavaAbove, "alpha", 0, 0.8f, 0.6f, 0.8f, 0)
                .setDuration(aboveAlphaDuration);
        //aboveAlphaAnimator.setRepeatCount(Animation.INFINITE);
        //aboveAlphaAnimator.setRepeatMode(Animation.REVERSE);
        aboveAlphaAnimator.setEvaluator(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                Log.i("float",
                        "evaluate() fraction:" + fraction + " startValue:" + startValue + " endValue:" + endValue);
                float startFloat = startValue.floatValue();
                return startFloat + fraction * (endValue.floatValue() - startFloat);
            }
        });

        aboveAlphaAnimator.setInterpolator(new TimeInterpolator() {
                                               float time1 = 400f / 5000.0f;
                                               float time2 = 1000f / 5000.0f;
                                               float time3 = 4000f / 5000.0f;
                                               float time4 = 5000f / 5000.0f;

                                               @Override
                                               public float getInterpolation(float input) {
                                                   //if (input <= time1) {
                                                   //    input = one;
                                                   //} else if (input > time1 && input <= time2) {
                                                   //    input = two;
                                                   //} else if (input > time2 && input <= time3) {
                                                   //    input = three;
                                                   //} else if (input > time3 && input < time4) {
                                                   //    input = four;
                                                   //}
                                                   if (input <= time1) {
                                                       input = input * 3.125f;
                                                   } else if (input > time1 && input <= time2) {
                                                       input = 0.25f + (input - time1) * 2.08f;
                                                   } else if (input > time2 && input <= time3) {
                                                       input = 0.5f + (input - time2) * 0.417f;
                                                   } else if (input > time3 && input <= time4) {
                                                       input = 0.75f + (input - time3) * 1.25f;
                                                   }
                                                   Log.i("float", "getInterpolation() input:" + input);
                                                   return input;
                                               }
                                           }

        );
        ObjectAnimator belowAlphaAnimator = ObjectAnimator.ofFloat(mIvWavaBelow, "alpha", 0, 0.6f, 0.4f, 0.6f, 0)
                .setDuration(5000);
        belowAlphaAnimator.setRepeatCount(Animation.INFINITE);
        belowAlphaAnimator.setRepeatMode(Animation.REVERSE);

        mWaveAnimatorSet = new

                AnimatorSet();

        mWaveAnimatorSet.playTogether(aboveMoveAnimator, belowMoveAnimator, aboveAlphaAnimator, belowAlphaAnimator);
    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        if (mWaveAnimatorSet != null) {
            mWaveAnimatorSet.start();
        }
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        cancelAnim();
    }

    private void cancelAnim() {
        if (mWaveAnimatorSet != null) {
            mWaveAnimatorSet.cancel();
        }
    }
}
