package com.sonuan.livegift.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.math.BigDecimal;

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
    private View mContentView;

    protected float mEnterStartX = 0;
    protected float mEnterStartY = 0;

    protected float mPauseStartX = 0f;
    protected float mPauseStartY = 0f;
    protected float mPauseEndX = 0f;
    protected float mPauseEndY = 0f;

    protected float mExitEndX = 0;
    protected float mExitEndY = 0;

    public LiveGiftBaseLayout(ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
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
                onCreate();
                initViews();
                initAnims();
                initDatas();
                Log.d("LiveGiftBaseLayout", "mParentWidth:" + mParentWidth);
                Log.d("LiveGiftBaseLayout", "mParentHeight:" + mParentHeight);
            }
        });
    }

    protected abstract void initAnims();

    protected abstract void initDatas();

    protected abstract void initViews();

    public Context getContext() {
        return mContext;
    }

    protected void setContentView(@LayoutRes int res) {
        mContentView = LayoutInflater.from(mContext).inflate(res, mParentView, false);
        if (mContentView == null || mParentView == null) {
            return;
        }
        if (mContentView.getParent() != null) {
            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
        }
        mParentView.addView(mContentView);
    }

    private int mStartX = 0;
    private int mPathAngle = 10;
    private int gravity = Gravity.BOTTOM;
    private int offsetY = -300;

    private BigDecimal mTanBigDecimal;
    static FloatEvaluator mEvaluator = new FloatEvaluator();

    protected void prepare(View animView) {

        final float tanAngle = (float) Math.tan(mPathAngle / 180.0f * Math.PI);
        mTanBigDecimal = new BigDecimal(tanAngle);

        int endX = 0;

        int startY = 0; // 确定Y轴上的位置
        // 根据offsetY偏移量计算出y轴开始坐标
        if (gravity == Gravity.TOP) {
            startY = 0 + offsetY;
        } else if (gravity == Gravity.CENTER_VERTICAL) {
            startY = mParentHeightHalf - animView.getMeasuredHeight() / 2 + offsetY;
        } else if (gravity == Gravity.BOTTOM) {
            startY = mParentHeight - animView.getMeasuredHeight() + offsetY;
        } else {
            // gravity is not one of Gravity.TOP,Gravity.CENTER_VERTICAL,Gravity.BOTTOM
            return;
        }
        mEnterStartY = startY; // y轴原始坐标, 根据gravity和offsetY计算得出

        // 因为必须移出到屏幕以外,利用角度, 得出x轴最终坐标
        if (360 - mPathAngle <= 90 || mPathAngle <= 90) { // x轴正方向
            endX = mParentWidth;
            mEnterStartX = mStartX - animView.getMeasuredWidth(); // 需要减掉该view的宽
        } else { // x轴负方向
            endX = 0 - animView.getMeasuredWidth();
            mEnterStartX = mStartX;
        }
        mExitEndX = endX; // x轴最终坐标


        int endY = 0;
        if (mPathAngle == 270) { // y轴 负方向
            endY = 0 - animView.getMeasuredHeight();
        } else if (mPathAngle == 90) { // y轴 正方向
            endY = mParentHeight + animView.getMeasuredHeight();
        }
        mExitEndY = endY;

        mPauseStartX = (mExitEndX + mEnterStartX) / 2.0f;// 暂停x轴坐标
        mPauseStartY = mEnterStartY + ((mPauseStartX - mEnterStartX) * mTanBigDecimal.floatValue());

        mPauseEndX = mPauseStartX;
        mPauseEndY = mPauseStartY;

    }

    protected void setXY(float progress,View animView, float startX, float endX, float startY, float endY) {
        float x = startX;
        float y = startY;
        if (mPathAngle == 270) { // y轴 负方向
            y = mEvaluator.evaluate(progress, startY, endY);
        } else if (mPathAngle == 90) { // y轴 正方向
            y = mEvaluator.evaluate(progress, startY, endY);
        } else {
            x = mEvaluator.evaluate(progress, startX, endX);
            y = startY + ((x - startX) * mTanBigDecimal.floatValue());
        }
        animView.setX(x);
        animView.setY(y);
    }

    protected View findViewById(@IdRes int id) {
        if (mContentView == null) {
            return null;
        }
        return mContentView.findViewById(id);
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

    protected void onCreate() {
        setupAnimators();
    }
    protected void onCancel() {

    }

    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        mAnimatorListener = animatorListener;
    }



}
