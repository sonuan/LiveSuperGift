package com.sonuan.livegift.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.sonuan.livegift.R;

/**
 * @author: wusongyuan
 * @date: 2016.08.13
 * @desc:
 */
public class LiveShipGiftBuilder extends LiveGiftBaseBuilder {

    private View mIvWavaAbove;
    private View mIvWavaBelow;
    private View mIvLight;
    private View mIvShip;
    private LinearInterpolator mInterpolator;


    public LiveShipGiftBuilder(ViewGroup viewGroup) {
        super(viewGroup, 5000, 0, 0);
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
        mIvLight = findViewById(R.id.ic_live_gift_ship_light);
        mIvShip = findViewById(R.id.ic_live_gift_ship);
    }

    //aboveAlphaAnimator.setEvaluator(new TypeEvaluator<Float>() {
    //    @Override
    //    public Float evaluate(float fraction, Float startValue, Float endValue) {
    //        Log.i("float",
    //                "evaluate() fraction:" + fraction + " startValue:" + startValue + " endValue:" + endValue);
    //        float startFloat = startValue.floatValue();
    //        return startFloat + fraction * (endValue.floatValue() - startFloat);
    //    }
    //});
    private AnimatorSet mWaveAnimatorSet;
    private AnimatorSet mLightAnimatorSet;
    private AnimatorSet mShipAnimatorSet;

    private void initAnims() {
        mInterpolator = new LinearInterpolator();
        // 波浪上下移动动画
        ObjectAnimator aboveMoveAnimator = ObjectAnimator.ofFloat(mIvWavaAbove, "TranslationY", 0, -20).setDuration(
                1200);
        aboveMoveAnimator.setRepeatCount(Animation.INFINITE);
        aboveMoveAnimator.setRepeatMode(Animation.REVERSE);
        aboveMoveAnimator.setInterpolator(mInterpolator);
        ObjectAnimator belowMoveAnimator = ObjectAnimator.ofFloat(mIvWavaBelow, "TranslationY", 0, 10).setDuration(
                1200);
        belowMoveAnimator.setInterpolator(mInterpolator);
        belowMoveAnimator.setRepeatCount(Animation.INFINITE);
        belowMoveAnimator.setRepeatMode(Animation.REVERSE);
        // 波浪透明变化动画
        long duration = 5000;
        PeriodInterpolator waveInterpolator = new PeriodInterpolator(duration, 0, 400f, 1000f, 4000f, 5000f);
        ObjectAnimator aboveAlphaAnimator = ObjectAnimator.ofFloat(mIvWavaAbove, "alpha", 0, 0.8f, 0.6f, 0.8f, 0)
                .setDuration(duration);
        aboveAlphaAnimator.setInterpolator(waveInterpolator);
        ObjectAnimator belowAlphaAnimator = ObjectAnimator.ofFloat(mIvWavaBelow, "alpha", 0, 0.6f, 0.4f, 0.6f, 0)
                .setDuration(duration);
        belowAlphaAnimator.setInterpolator(waveInterpolator);

        mWaveAnimatorSet = new AnimatorSet();
        mWaveAnimatorSet.playTogether(aboveMoveAnimator, belowMoveAnimator, aboveAlphaAnimator, belowAlphaAnimator);
        // 闪光动画
        long lightDuration = 5000;
        ObjectAnimator lightMoveXAnimator = ObjectAnimator.ofFloat(mIvLight, "x", 60, 0, 10, 10, 10).setDuration(
                lightDuration);
        ObjectAnimator lightMoveYAnimator = ObjectAnimator.ofFloat(mIvLight, "y", 500, 440, 440, 400, 400).setDuration(
                lightDuration);
        ObjectAnimator lightAlphaAnimator = ObjectAnimator.ofFloat(mIvLight, "alpha", 0, 1f, 1f, 1f, 0).setDuration(
                duration);
        PeriodInterpolator lightInterpolator = new PeriodInterpolator(lightDuration, 0, 1700f, 2400f, 4000f, 5000f);

        mLightAnimatorSet = new AnimatorSet();
        mLightAnimatorSet.setDuration(lightDuration);
        mLightAnimatorSet.setInterpolator(lightInterpolator);
        mLightAnimatorSet.playTogether(lightMoveXAnimator, lightMoveYAnimator, lightAlphaAnimator);

        // 轮船动画
        long shipDuration = 4500;
        ObjectAnimator shipMoveXAnimator = ObjectAnimator.ofFloat(mIvShip, "x", -500, -100, -8, 30, 68, 100, 140, 640)
                .setDuration(shipDuration);
        ObjectAnimator shipMoveYAnimator = ObjectAnimator.ofFloat(mIvShip, "y", 370, 470, 510, 520, 510, 544, 546, 650)
                .setDuration(shipDuration);
        PeriodInterpolator shipInterpolator = new PeriodInterpolator(shipDuration, 0f, 800f, 1600f, 2000f, 2500f, 3300f,
                4100f, 4500f);
        mShipAnimatorSet = new AnimatorSet();
        mShipAnimatorSet.setDuration(shipDuration);
        mShipAnimatorSet.setInterpolator(shipInterpolator);
        mShipAnimatorSet.playTogether(shipMoveXAnimator, shipMoveYAnimator);
    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        startAnim(mWaveAnimatorSet, mShipAnimatorSet, mLightAnimatorSet);
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        cancelAnim();
    }

    private void cancelAnim() {
        cancelAnim(mWaveAnimatorSet, mShipAnimatorSet, mLightAnimatorSet);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAnim();
    }
}
