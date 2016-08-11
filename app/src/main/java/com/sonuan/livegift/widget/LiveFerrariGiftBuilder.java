package com.sonuan.livegift.widget;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sonuan.livegift.R;


/**
 * @author: wusongyuan
 * @date: 2016.08.10
 * @desc:
 */
public class LiveFerrariGiftBuilder extends LiveGiftBaseBuilder {

    private ObjectAnimator mWheelRotaionAnimator1;
    private ObjectAnimator mWheelRotaionAnimator2;
    private ImageView mCarWheel1;
    private ImageView mCarWheel2;

    private View mAnimView;

    private static int[] duration = {800, 700, 800};

    public LiveFerrariGiftBuilder(ViewGroup viewGroup) {
        super(viewGroup, duration);
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.item_live_gift_car);
    }

    protected void initViews() {
        mAnimView = findViewById(R.id.carFerrari);
        if (mAnimView.getMeasuredHeight() == 0 || mAnimView.getMeasuredWidth() == 0) {
            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            mAnimView.measure(width, height);
        }
        mCarWheel1 = (ImageView) findViewById(R.id.wheel1);
        mCarWheel2 = (ImageView) findViewById(R.id.wheel2);

        updateWheelParams(mAnimView, mCarWheel1, 95 / 540f, 95 / 237f, 324 / 540f, 125 / 237f);
        updateWheelParams(mAnimView, mCarWheel2, 78 / 540f, 78 / 237f, 479 / 540f, 74 / 237f);
    }

    protected void setupChildAnimators() {
        mWheelRotaionAnimator1 = ObjectAnimator.ofFloat(mCarWheel1, "rotation", mCarWheel1.getRotation() - 360);
        mWheelRotaionAnimator1.setInterpolator(null);
        mWheelRotaionAnimator1.setDuration(500);
        mWheelRotaionAnimator1.setRepeatCount(-1);

        mWheelRotaionAnimator2 = ObjectAnimator.ofFloat(mCarWheel2, "rotation", mCarWheel2.getRotation() - 360);
        mWheelRotaionAnimator2.setInterpolator(null);
        mWheelRotaionAnimator2.setDuration(500);
        mWheelRotaionAnimator2.setRepeatCount(-1);
    }


    protected void initDatas() {
        this.setAnimView(mAnimView).setStartX(720).setPathAngle(170).setGravity(Gravity.TOP).setOffsetY(100).build();
    }


    private void updateWheelParams(View parentView, View wheel, float rW, float rH, float rMarginLeft,
            float rMarginTop) {
        int width = parentView.getMeasuredWidth();
        int height = parentView.getMeasuredHeight();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) wheel.getLayoutParams();
        params.width = Math.round(width * rW);
        params.height = Math.round(height * rH);
        params.leftMargin = Math.round(width * rMarginLeft);
        params.topMargin = Math.round(height * rMarginTop);
        wheel.setLayoutParams(params);

    }

    @Override
    public void start() {
        super.start();
        if (mWheelRotaionAnimator1 != null) {
            mWheelRotaionAnimator1.start();
        }
        if (mWheelRotaionAnimator2 != null) {
            mWheelRotaionAnimator2.start();
        }

    }

    @Override
    protected void onCancel() {
        Log.i(TAG, "onCancel()");
        if (mWheelRotaionAnimator1 != null && mWheelRotaionAnimator1.isStarted()) {
            mWheelRotaionAnimator1.cancel();
        }
        if (mWheelRotaionAnimator2 != null && mWheelRotaionAnimator2.isStarted()) {
            mWheelRotaionAnimator2.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onCancel();
    }

    @Override
    protected void onAnimationRepeat(int currentIteration, @RepeatMode int repeatMode, boolean isMirror) {
        super.onAnimationRepeat(currentIteration, repeatMode, isMirror);

    }
}
