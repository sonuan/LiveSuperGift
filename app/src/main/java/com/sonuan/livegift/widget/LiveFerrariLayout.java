package com.sonuan.livegift.widget;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.sonuan.livegift.R;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author: wusongyuan
 * @date: 2016.08.10
 * @desc:
 */
public class LiveFerrariLayout extends LiveGiftBaseLayout {

    private BigDecimal mTanBigDecimal;
    private ObjectAnimator mWheelRotaionAnimator1;
    private ObjectAnimator mWheelRotaionAnimator2;
    private ImageView mCarWheel1;
    private ImageView mCarWheel2;

    private View view;
    private int mViewWidth;
    private int mViewHeight;

    private int mStartX = 720;

    private int mStartY = 87;

    private float mPauseStartX = 0f;
    private float mPauseStartY = 0f;
    private float mPauseEndX = 0f;
    private float mPauseEndY = 0f;

    private float mEnterStartX = 0;
    private float mEnterStartY = 0;
    private float mEnterEndX = 0;
    private float mEnterEndY = 0;

    private float mExitStartX = 0;
    private float mExitStartY = 0;
    private float mExitEndX = 0;
    private float mExitEndY = 0;

    public LiveFerrariLayout(Context context, ViewGroup viewGroup) {
        super(context, viewGroup);
        setContentView(R.layout.item_live_gift_car);
        initView();
        initData();
    }

    private void initView() {
        view = findViewById(R.id.carFerrari);

        if (view.getMeasuredHeight() == 0 || view.getMeasuredWidth() == 0) {
            mViewWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            mViewHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(mViewWidth, mViewHeight);
        }
        mCarWheel1 = (ImageView) findViewById(R.id.wheel1);
        mCarWheel2 = (ImageView) findViewById(R.id.wheel2);
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                updateWheelParams(view, mCarWheel1, 95 / 540f, 95 / 237f, 324 / 540f, 125 / 237f);
                updateWheelParams(view, mCarWheel2, 78 / 540f, 78 / 237f, 479 / 540f, 74 / 237f);
            }
        });
        mWheelRotaionAnimator1 = ObjectAnimator.ofFloat(mCarWheel1, "rotation", mCarWheel1.getRotation() - 360);
        mWheelRotaionAnimator1.setInterpolator(null);
        mWheelRotaionAnimator1.setDuration(500);
        mWheelRotaionAnimator1.setRepeatCount(-1);

        mWheelRotaionAnimator2 = ObjectAnimator.ofFloat(mCarWheel2, "rotation", mCarWheel2.getRotation() - 360);
        mWheelRotaionAnimator2.setInterpolator(null);
        mWheelRotaionAnimator2.setDuration(500);
        mWheelRotaionAnimator2.setRepeatCount(-1);
    }


    private void updateWheelParams(View parentView, View wheel, float rW, float rH, float rMarginLeft,
            float rMarginTop) {
        int width = parentView.getWidth();
        int height = parentView.getHeight();

        Log.d("LiveFerrariLayout", "width:" + width);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) wheel.getLayoutParams();
        params.width = Math.round(width * rW);
        params.height = Math.round(height * rH);
        params.leftMargin = Math.round(width * rMarginLeft);
        params.topMargin = Math.round(height * rMarginTop);
        wheel.setLayoutParams(params);

    }


    private void initData() {
        int endX = 0;
        // 因为必须移出到屏幕以外,利用角度, 得出x轴最终坐标
        if (360 - angle <= 90 || angle <= 90) { // x轴正方向
            endX = SCREEN_WIDTH;
            mEnterStartX = mStartX - view.getMeasuredWidth(); // 需要减掉该view的宽
        } else { // x轴负方向
            endX = 0 - view.getMeasuredWidth();
            mEnterStartX = mStartX;
        }
        mExitEndX = endX; // x轴最终坐标


        int endY = 0;
        if (angle == 270) { // y轴 负方向
            endY = 0 - view.getMeasuredHeight();
        } else if (angle == 90) { // y轴 正方向
            endY = SCREEN_HEIGHT + view.getMeasuredHeight();
        }
        mExitEndY = endY;
        mEnterStartY = mStartY; // y轴原始坐标, 根据gravity和offsetY计算得出

        mEnterEndX = (mExitEndX + mEnterStartX) / 2.0f;// 暂停x轴坐标
        mPauseStartX = mEnterEndX;

        final float tanAngle = (float) Math.tan(angle / 180.0f * Math.PI);
        mTanBigDecimal = new BigDecimal(tanAngle);
        mEnterEndY = mEnterStartY + ((mPauseStartX - mEnterStartX) * mTanBigDecimal.floatValue());
        mPauseStartY = mEnterEndY;

        mPauseEndX = mPauseStartX;
        mPauseEndY = mPauseStartY;

        mExitStartX = mPauseEndX;
        mExitStartY = mPauseEndY;

    }

    private ArrayList<Animator> mWheelAnimatorList = new ArrayList<>();


    private void startAnimation4Wheel(View... wheelList) {
        int length = wheelList.length;
        for (int i = 0; i < length; i++) {
            View wheel = wheelList[i];
            ObjectAnimator animator = ObjectAnimator.ofFloat(wheel, "rotation", wheel.getRotation() - 360);
            animator.setInterpolator(null);
            animator.setDuration(500);
            animator.setRepeatCount(-1);
            animator.start();
            mWheelAnimatorList.add(animator);
        }

    }

    private int angle = 170;
    static FloatEvaluator mEvaluator = new FloatEvaluator();


    @Override
    protected void enterAnimator(float progress) {
        setXY(progress, mEnterStartX, mEnterEndX, mEnterStartY, mEnterEndY);
    }

    @Override
    protected void pauseAnimator(float progress) {
        if (mPauseStartX == mPauseEndX && mPauseStartY == mPauseEndY) {
            return;
        }
        setXY(progress, mPauseStartX, mPauseEndX, mPauseStartY, mPauseEndY);
    }

    @Override
    protected void exitAnimator(float progress) {
        setXY(progress, mExitStartX, mExitEndX, mExitStartY, mExitEndY);
    }

    private void setXY(float progress, float startX, float endX, float startY, float endY) {
        float x = startX;
        float y = startY;
        if (angle == 270) { // y轴 负方向
            y = mEvaluator.evaluate(progress, startY, endY);
        } else if (angle == 90) { // y轴 正方向
            y = mEvaluator.evaluate(progress, startY, endY);
        } else {
            x = mEvaluator.evaluate(progress, startX, endX);
            y = startY + ((x - startX) * mTanBigDecimal.floatValue());
        }
        view.setX(x);
        view.setY(y);
    }


    @Override
    public void start() {
        super.start();

        if (mWheelRotaionAnimator1.isStarted()) {
            mWheelRotaionAnimator1.cancel();
        }
        if (mWheelRotaionAnimator2.isStarted()) {
            mWheelRotaionAnimator2.cancel();
        }
        mWheelRotaionAnimator1.start();
        mWheelRotaionAnimator2.start();

    }


}
