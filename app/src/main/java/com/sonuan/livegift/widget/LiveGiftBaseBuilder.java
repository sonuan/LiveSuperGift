package com.sonuan.livegift.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;


import java.math.BigDecimal;



/**
 * @author: wusongyuan
 * @date: 2016.08.10
 * @desc:
 */
public abstract class LiveGiftBaseBuilder {

    protected static final String TAG = LiveGiftBaseBuilder.class.getSimpleName();
    // 屏幕的宽度
    private int mParentWidth = 0;
    // 屏幕的高度, 到时要减掉状态栏标题栏
    private int mParentHeight = 0;
    // 屏幕高度的一半
    private int mParentHeightHalf;

    protected OnGiftAnimatorListener mGiftAnimatorListener;

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

    private static final int[] DEFAUL_DURATION = {800, 700, 800};// 法拉利的时间
    private int mEnterDuration = DEFAUL_DURATION[0];
    private int mPauseDuration = DEFAUL_DURATION[1];
    private int mExitDuration = DEFAUL_DURATION[2];

    private int mRepeatCount = 0;
    private int mCurrentIteration;
    @RepeatMode
    private int mRepeatMode = RepeatMode.RESTART;

    private int mStartX = 0;
    private int mPathAngle = 10;
    private int mGravity = Gravity.BOTTOM;
    private int mOffsetY = -300;
    private int mPauseOffsetX = 0;
    private int mPauseOffsetY = 0;

    private BigDecimal mTanBigDecimal;
    static FloatEvaluator mEvaluator = new FloatEvaluator();

    private View mMoveView;

    private boolean mIsPathAngle = true;

    private boolean mIsAnimationCanceled = false;

    public @interface RepeatMode {
        int RESTART = 1;
        int REVERSE = 2;
        int MIRROR = 3; // 镜像
        int TOGGLE = 4; // 镜像切换
    }

    public LiveGiftBaseBuilder(ViewGroup viewGroup) {
        this(viewGroup, null);
    }

    public LiveGiftBaseBuilder(ViewGroup viewGroup, int[] animTimes) {
        if (viewGroup == null) {
            return;
        }
        mContext = viewGroup.getContext();
        mParentView = viewGroup;

        if (animTimes != null) {
            if (animTimes.length >= 3) {
                mEnterDuration = animTimes[0];
                mPauseDuration = animTimes[1];
                mExitDuration = animTimes[2];
            }
        }
        // TODO: 16/8/13
        mParentWidth = 720;
        mParentHeight = 1280;
        mParentHeightHalf = mParentHeight / 2;
        setupAnimators();
        onCreate();
    }


    protected Context getContext() {
        return mContext;
    }


    protected void setContentView(@LayoutRes int res) {
        mContentView = LayoutInflater.from(mContext).inflate(res, mParentView, false);
        if (mContentView.getParent() != null) {
            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
        }
        if (mParentView != null && mContentView != null) {
            mParentView.addView(mContentView);
        }
    }

    private void calculateAngleTan(float enterStartX, float enterStartY, float pauseStartX, float pauseStartY) {
        float tanAngle = 0;
        if (mIsPathAngle) {
            tanAngle = (float) Math.tan(mPathAngle / 180.0f * Math.PI);
            Log.i(TAG, "calculateAngleTan() angle tanAngle:" + tanAngle);
        } else {
            tanAngle = (pauseStartY - enterStartY) / (pauseStartX - enterStartX);
            Log.i(TAG, "calculateAngleTan() xy tanAngle:" + tanAngle);
        }
        mTanBigDecimal = new BigDecimal(tanAngle);
    }

    private float getAngleTan() {

        return mTanBigDecimal.floatValue();
    }

    private void calculateEnterStartY(View moveView, int gravity, int offsetY) {
        int startY = 0; // 确定Y轴上的位置
        // 根据offsetY偏移量计算出y轴开始坐标
        if (gravity == Gravity.TOP) {
            startY = 0 + offsetY;
        } else if (gravity == Gravity.CENTER_VERTICAL) {
            startY = mParentHeightHalf - moveView.getMeasuredHeight() / 2 + offsetY;
        } else if (gravity == Gravity.BOTTOM) {
            startY = mParentHeight - moveView.getMeasuredHeight() + offsetY;
        } else {
            // mGravity is not one of Gravity.TOP,Gravity.CENTER_VERTICAL,Gravity.BOTTOM
            return;
        }
        mEnterStartY = startY; // y轴原始坐标, 根据gravity和offsetY计算得出
    }

    private void calculateEnterStartXAndExitEndX(View moveView, float angle, float startX) {
        int endX = 0;
        // 因为必须移出到屏幕以外,利用角度, 得出x轴最终坐标
        if (360 - angle <= 90 || angle <= 90) { // x轴正方向
            endX = mParentWidth;
            mEnterStartX = startX - moveView.getMeasuredWidth(); // 需要减掉该view的宽
        } else { // x轴负方向
            endX = 0 - moveView.getMeasuredWidth();
            mEnterStartX = startX;
        }
        mExitEndX = endX; // x轴最终坐标
    }

    private void calculateExitEndY(View moveView, float angle) {
        float endY = 0;
        if (angle == 270) { // y轴 负方向
            endY = 0 - moveView.getMeasuredHeight();
        } else if (angle == 90) { // y轴 正方向
            endY = mParentHeight + moveView.getMeasuredHeight();
        }
        if (mExitEndY <= 0) {
            mExitEndY = endY;
        }
    }

    private void calculatePauseXY(float pauseOffsetX, float pauseOffsetY) {
        if (mIsPathAngle) {
            mPauseStartX = (mExitEndX + mEnterStartX) / 2.0f;// 暂停x轴坐标
            //mPauseStartY = mEnterStartY + ((mPauseStartX - mEnterStartX) *  getTanAngle());
            mPauseStartY = calculateY(mEnterStartX, mPauseStartX, mEnterStartY, getAngleTan());
        }
        mPauseEndX = mPauseStartX + pauseOffsetX;
        mPauseEndY = mPauseStartY + pauseOffsetY;
    }

    private float calculateY(float startX, float endX, float startY, float tan) {
        return startY + ((endX - startX) * tan);
    }


    protected View findViewById(@IdRes int id) {
        if (mContentView == null) {
            Log.i(TAG, "findViewById: mContentView == null");
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
        enterAnimator.setDuration(mEnterDuration);
        enterAnimator.setInterpolator(new DecelerateInterpolator());
        enterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float vaule = (float) animation.getAnimatedValue();
                enterAnimator(vaule);

            }
        });
        final ValueAnimator pauseAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        pauseAnimator.setDuration(mPauseDuration);
        pauseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float vaule = (float) animation.getAnimatedValue();
                pauseAnimator(vaule);

            }
        });
        ValueAnimator exitAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        exitAnimator.setDuration(mExitDuration);
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
                mIsAnimationCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG, "onAnimationEnd() RepeatMode:" + mRepeatMode + " CurrentIteration:" + mCurrentIteration
                        + " IsMirror:" + mIsMirror);
                if (mCurrentIteration < mRepeatCount && !mIsAnimationCanceled) {
                    if (mRepeatMode == RepeatMode.RESTART) {
                        start(true);
                    } else if (mRepeatMode == RepeatMode.MIRROR) {
                        toggleMirror();
                        start(true);
                    } else if (mRepeatMode == RepeatMode.TOGGLE) {
                        toggleMirror();
                        start(true);
                    }
                    the().onAnimationRepeat(mCurrentIteration, mRepeatMode, mIsMirror);
                    mCurrentIteration++;
                } else {
                    reset();
                    the().onAnimationEnd();
                    if (mGiftAnimatorListener != null) {
                        mGiftAnimatorListener.onAnimatorEnd();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                the().onAnimationCancel();
                mIsAnimationCanceled = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private LiveGiftBaseBuilder the() {
        return LiveGiftBaseBuilder.this;
    }

    protected void onAnimationStart() {
        mContentView.setVisibility(View.VISIBLE);
    }

    protected void onAnimationEnd() {
        mContentView.setVisibility(View.INVISIBLE);
    }

    protected void onAnimationCancel() {

    }

    protected void onAnimationRepeat(int currentIteration, @RepeatMode int repeatMode, boolean isMirror) {

    }

    private boolean mIsMirror = false;

    private void toggleMirror() {
        mIsMirror = !mIsMirror;
        mStartX = mParentWidth - mStartX;
        if (mPathAngle > 180) {
            mPathAngle = 360 - (mPathAngle - 180);
        } else {
            mPathAngle = 180 - mPathAngle;
        }
        mPauseStartX = mParentWidth - mPauseStartX - mMoveView.getWidth();
        mPauseOffsetX = 0 - mPauseOffsetX;
        Log.i(TAG, "toggleMirror() mPathAngle:" + mPathAngle + " mStartX:" + mStartX + " mPauseStartX:" + mPauseStartX
                + " mPauseOffsetX:" + mPauseOffsetX);
        build();
    }

    protected void build() {
        calculateEnterStartY(mMoveView, mGravity, mOffsetY);
        calculateEnterStartXAndExitEndX(mMoveView, mPathAngle, mStartX);
        calculateAngleTan(mEnterStartX, mEnterStartY, mPauseStartX, mPauseStartY);
        calculateExitEndY(mMoveView, mPathAngle);
        calculatePauseXY(mPauseOffsetX, mPauseOffsetY);
    }

    private void start(boolean isRepeat) {
        if (!isRepeat) {
            if (mGiftAnimatorListener != null) {
                mGiftAnimatorListener.onAnimatorStart();
            }
            the().onAnimationStart();
            Log.i(TAG, "start ");
        } else {
            Log.i(TAG, "restart ");
        }
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
        mIsAnimationCanceled = false;
    }

    private void reset() {
        mCurrentIteration = 0;
        if (mIsMirror) {
            toggleMirror(); // 恢复原有的位置
        }
    }

    public void start() {
        start(false);
    }

    protected void setXY(float progress, View animView, float startX, float endX, float startY, float endY) {
        if (animView == null) {
            Log.i(TAG, "setXY: animView == null");
            return;
        }
        float x = startX;
        float y = startY;
        if (mPathAngle == 270) { // y轴 负方向
            y = mEvaluator.evaluate(progress, startY, endY);
        } else if (mPathAngle == 90) { // y轴 正方向
            y = mEvaluator.evaluate(progress, startY, endY);
        } else {
            x = mEvaluator.evaluate(progress, startX, endX);
            if (startY != endY) {
                if (mIsPathAngle) {
                    y = calculateY(startX, x, startY, getAngleTan());
                } else {
                    y = mEvaluator.evaluate(progress, startY, endY);
                }
            }
        }
        Log.i(TAG, "x:" + x + " y:" + y);
        animView.setX(x);
        animView.setY(y);
    }

    protected void setXY(float progress, float startX, float endX, float startY, float endY) {
        setXY(progress, mMoveView, startX, endX, startY, endY);
    }

    protected abstract void onCreate();

    //protected abstract void initViews();
    //
    //protected abstract void setupChildAnimators();
    //
    //protected abstract void initDatas();

    protected void enterAnimator(float progress) {
        setXY(progress, mMoveView, mEnterStartX, mPauseStartX, mEnterStartY, mPauseStartY);
    }

    protected void pauseAnimator(float progress) {
        if (mPauseStartX == mPauseEndX && mPauseStartY == mPauseEndY) {
            return;
        }
        setXY(progress, mMoveView, mPauseStartX, mPauseEndX, mPauseStartY, mPauseEndY);
    }

    protected void exitAnimator(float progress) {
        setXY(progress, mMoveView, mPauseEndX, mExitEndX, mPauseEndY, mExitEndY);
    }

    public void cancel() {
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
        }
    }

    public interface OnGiftAnimatorListener {
        void onAnimatorStart();

        void onAnimatorEnd();
    }

    public void release() {
        onDestroy();
    }

    protected void onDestroy() {
        if (mAnimatorSet != null) {
            if (mAnimatorSet.isRunning()) mAnimatorSet.cancel();
            mAnimatorSet = null;
        }

        if (mContentView.getParent() != null) {
            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
        }
    }


    //public OnGiftAnimatorListener getGiftAnimatorListener() {
    //    return mGiftAnimatorListener;
    //}

    public void setGiftAnimatorListener(OnGiftAnimatorListener giftAnimatorListener) {
        mGiftAnimatorListener = giftAnimatorListener;
    }


    public int getRepeatCount() {
        return mRepeatCount;
    }

    protected LiveGiftBaseBuilder setRepeatCount(int repeatCount) {
        mRepeatCount = repeatCount;
        return this;
    }

    public int getRepeatMode() {
        return mRepeatMode;
    }

    protected LiveGiftBaseBuilder setRepeatMode(@RepeatMode int repeatMode) {
        mRepeatMode = repeatMode;
        return this;
    }

    public int getStartX() {
        return mStartX;
    }

    protected LiveGiftBaseBuilder setStartX(int startX) {
        mStartX = startX;
        return this;
    }

    public int getPathAngle() {
        return mPathAngle;
    }

    protected LiveGiftBaseBuilder setPathAngle(int pathAngle) {
        mPathAngle = pathAngle;
        return this;

    }

    public int getOffsetY() {
        return mOffsetY;
    }

    public int getGravity() {
        return mGravity;
    }

    protected LiveGiftBaseBuilder setGravity(int gravity, int offsetY) {
        mGravity = gravity;
        mOffsetY = offsetY;
        return this;

    }

    public int getPauseOffsetX() {
        return mPauseOffsetX;
    }

    protected LiveGiftBaseBuilder setPauseOffsetX(int pauseOffsetX) {
        mPauseOffsetX = pauseOffsetX;
        return this;

    }

    public int getPauseOffsetY() {
        return mPauseOffsetY;
    }

    protected LiveGiftBaseBuilder setPauseOffsetY(int pauseOffsetY) {
        mPauseOffsetY = pauseOffsetY;
        return this;

    }

    public View getMoveView() {
        return mMoveView;
    }

    protected LiveGiftBaseBuilder setMoveView(View moveView) {
        mMoveView = moveView;
        Log.i(TAG, "setMoveView mMoveView :" + (mMoveView == null));
        return this;
    }

    public float getPauseStartX() {
        return mPauseStartX;
    }

    protected LiveGiftBaseBuilder setPauseStartXY(float pauseStartX, float pauseStartY) {
        mPauseStartX = pauseStartX;
        mPauseStartY = pauseStartY;
        mIsPathAngle = false;
        return the();
    }

    public float getPauseStartY() {
        return mPauseStartY;
    }

    public float getExitEndX() {
        return mExitEndX;
    }

    public LiveGiftBaseBuilder setExitEndY(float exitEndY) {
        mExitEndY = exitEndY;
        return the();
    }

    public float getExitEndY() {
        return mExitEndY;
    }

}
