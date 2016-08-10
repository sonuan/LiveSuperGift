package com.sonuan.livegift.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * @author: wusongyuan
 * @date: 2016.08.10
 * @desc:
 */
public abstract class LiveGiftBaseLayout {

    // 屏幕的宽度
    public int mParentWidth = 0;
    // 屏幕的高度, 到时要减掉状态栏标题栏
    public int mParentHeight = 0;
    // 屏幕高度的一半
    public int mParentHeightHalf;
    private Animator.AnimatorListener mAnimatorListener;
    private AnimatorSet mAnimatorSet;

    private Context mContext;

    private ViewGroup mParentView;

    public LiveGiftBaseLayout(ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        setupAnimators();
        mContext = viewGroup.getContext();
        mParentView = viewGroup;

        ViewTreeObserver vto = mParentView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mParentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mParentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mParentWidth = mParentView.getWidth();
                mParentHeight = mParentView.getHeight();
                mParentHeightHalf = mParentHeight / 2;
                Log.d("LiveGiftBaseLayout", "mParentWidth:" + mParentWidth);
                Log.d("LiveGiftBaseLayout", "mParentHeight:" + mParentHeight);
            }
        });
        mParentWidth = mParentView.getMeasuredWidth();
        mParentHeight = mParentView.getMeasuredHeight();
        mParentHeightHalf = mParentHeight / 2;
        Log.d("LiveGiftBaseLayout", "mParentWidth:" + mParentWidth);
        Log.d("LiveGiftBaseLayout", "mParentHeight:" + mParentHeight);

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
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mAnimatorListener != null) {
                    mAnimatorListener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mAnimatorListener != null) {
                    mAnimatorListener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (mAnimatorListener != null) {
                    mAnimatorListener.onAnimationCancel(animation);
                }
                onCancel();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (mAnimatorListener != null) {
                    mAnimatorListener.onAnimationRepeat(animation);
                }
            }
        });

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

    protected abstract void initViews();

    protected abstract void initDatas();

    protected void onCancel() {

    }

    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        mAnimatorListener = animatorListener;
    }
}
