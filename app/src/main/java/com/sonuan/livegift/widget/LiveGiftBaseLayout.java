package com.sonuan.livegift.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * @author: wusongyuan
 * @date: 2016.08.10
 * @desc:
 */
public abstract class LiveGiftBaseLayout {

    // 屏幕的宽度
    public static final int SCREEN_WIDTH = 720;
    // 屏幕的高度, 到时要减掉状态栏标题栏
    public static final int SCREEN_HEIGHT = 1280;
    // 屏幕高度的一半
    public static final int SCREEN_HEIGHT_HALF = SCREEN_HEIGHT / 2;
    private Animator.AnimatorListener mAnimatorListener;
    private AnimatorSet mAnimatorSet;

    private Context mContext;

    private ViewGroup mParentView;

    public LiveGiftBaseLayout(Context context, ViewGroup viewGroup) {
        setupAnimators();
        mContext = viewGroup.getContext();
        mParentView = viewGroup;
    }

    public Context getContext() {
        return mContext;
    }

    private View mContentView;

    protected void setContentView(@LayoutRes int res) {
        mContentView = LayoutInflater.from(mContext).inflate(res, mParentView, false);
    }

    protected View findViewById(@IdRes int id) {
        if (mContentView == null) {
            return null;
        }
        return mContentView.findViewById(id);
    }

    public void setParentView(ViewGroup viewGroup) {
        if (mContentView == null) {
            return;
        }
        if (mContentView.getParent() != null) {
            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
        }
        viewGroup.addView(mContentView);
    }

    public boolean isRunning() {
        return mAnimatorSet != null && mAnimatorSet.isRunning();
    }

    private void setupDefaultParams(Context context) {
    }

    protected void setupAnimators() {
        final ValueAnimator enterAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        enterAnimator.setDuration(800);
        enterAnimator.setInterpolator(new DecelerateInterpolator());
        enterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float vaule = (float) animation.getAnimatedValue();
                enterAnimator(vaule);

            }
        });

        final ValueAnimator pauseAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        pauseAnimator.setDuration(700);
        pauseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float vaule = (float) animation.getAnimatedValue();
                pauseAnimator(vaule);

            }
        });
        ValueAnimator exitAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        exitAnimator.setDuration(800);
        exitAnimator.setInterpolator(new LinearInterpolator());
        exitAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float vaule = (float) animation.getAnimatedValue();
                exitAnimator(vaule);
            }
        });
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playSequentially(enterAnimator, pauseAnimator, exitAnimator);
        if (mAnimatorListener != null) {
            mAnimatorSet.addListener(mAnimatorListener);
        }
    }

    public void start() {
        if (mAnimatorSet == null) {
            setupAnimators();
        }
        if (mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
        }
        mAnimatorSet.start();
    }

    protected abstract void enterAnimator(float progress);

    protected abstract void pauseAnimator(float progress);

    protected abstract void exitAnimator(float progress);

}
