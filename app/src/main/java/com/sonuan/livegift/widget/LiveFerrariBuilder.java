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
public class LiveFerrariBuilder extends LiveGiftBaseBuilder {

    private ObjectAnimator mWheelRotaionAnimator1;
    private ObjectAnimator mWheelRotaionAnimator2;
    private ImageView mCarWheel1;
    private ImageView mCarWheel2;

    private View view;

    private int mStartX = 0;
    private int mPathAngle = 10;
    private int gravity = Gravity.BOTTOM;
    private int offsetY = -300;

    private static int[] duration = {800, 700, 800};

    public LiveFerrariBuilder(ViewGroup viewGroup) {
        super(viewGroup, duration);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        setContentView(R.layout.item_live_gift_car);
    }

    protected void initViews() {
        view = findViewById(R.id.carFerrari);
        if (view.getMeasuredHeight() == 0 || view.getMeasuredWidth() == 0) {
            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(width, height);
        }
        mCarWheel1 = (ImageView) findViewById(R.id.wheel1);
        mCarWheel2 = (ImageView) findViewById(R.id.wheel2);

        updateWheelParams(view, mCarWheel1, 95 / 540f, 95 / 237f, 324 / 540f, 125 / 237f);
        updateWheelParams(view, mCarWheel2, 78 / 540f, 78 / 237f, 479 / 540f, 74 / 237f);
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


    private int[] mParam;
    int[] param1 = {720, 170, Gravity.TOP, 87, 0, 0};
    int[] param2 = {0, 10, Gravity.TOP, 87, 0, 0};

    protected void initDatas() {
        mParam = param1;
        prepare(view, mParam);
    }

    public void toggle() {
        if (mParam == param1) {
            mParam = param2;
        } else {
            mParam = param1;
        }
        prepare(view, mParam);
    }


    private void updateWheelParams(View parentView, View wheel, float rW, float rH, float rMarginLeft,
            float rMarginTop) {
        int width = parentView.getMeasuredWidth();
        int height = parentView.getMeasuredHeight();

        Log.d("LiveFerrariBuilder", "width:" + width + "  height:" + height);
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
        mWheelRotaionAnimator1.start();
        mWheelRotaionAnimator2.start();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        Log.d("LiveFerrariBuilder", "cancel");
        if (mWheelRotaionAnimator1 != null && mWheelRotaionAnimator1.isStarted()) {
            mWheelRotaionAnimator1.cancel();
        }
        if (mWheelRotaionAnimator2 != null && mWheelRotaionAnimator2.isStarted()) {
            mWheelRotaionAnimator2.cancel();
        }
    }
}
