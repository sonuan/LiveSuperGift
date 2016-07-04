package com.sonuan.livegift.activity;

import android.animation.FloatEvaluator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.Gravity;
import android.view.View;

import java.math.BigDecimal;

/**********************
 * @author: wusongyuan
 * @date: 2016-07-01
 * @desc: 直播-高级礼物-轨迹动画
 **********************/
public class LiveGiftPathAnimUtils {


    private static final String TAG = LiveGiftPathAnimUtils.class.getSimpleName();
    public static final int OFFSET_Y = 200;

    // 屏幕的宽度
    public static final int SCREEN_WIDTH = 720;
    // 屏幕的高度, 到时要减掉状态栏标题栏
    public static final int SCREEN_HEIGHT = 1280;
    // 屏幕高度的一半
    public static final int SCREEN_HEIGHT_HALF = SCREEN_HEIGHT / 2;

    public @interface PATH {
        String XP = "POSITIVE_X";//正X 0'
        String YP = "POSITIVE_Y";//正Y 90'
        String XN = "NEGATIVE_X";//负X 180'
        String YN = "NEGATIVE_Y";//负Y 270'
        String XP_YP = "POSITIVE_X-POSITIVE_Y"; // 0 ~ 90
        String XP_YN = "POSITIVE_X-NEGATIVE_Y"; // 90 ~ 180
        String XN_YP = "NEGATIVE_X-POSITIVE_Y"; // 180 ~ 270
        String XN_YN = "NEGATIVE_X-NEGATIVE_Y"; // 270 ~ 360
    }

    public static ValueAnimator transAnim(final View view, @PATH String
            path, final int angle, final ValueAnimator.AnimatorUpdateListener listener) {
        return transAnim(view, Gravity.CENTER_VERTICAL, 0, -1, path, angle, listener);
    }

    public static ValueAnimator transAnim(final View view, int gravity, int offsetY, @PATH String
            path, final int angle, final ValueAnimator.AnimatorUpdateListener listener) {
        return transAnim(view, gravity, offsetY, -1, path, angle, listener);
    }

    /**
     * @param view
     * @param gravity
     * @param offsetY
     * @param startX
     * @param path 路径方向
     * @param angle    0 ~ 90, 注意:这边的角度是0~90
     * @param listener
     * @return
     */
    public static ValueAnimator transAnim(final View view, int gravity, int offsetY, final int startX, @PATH String
            path, final int angle, final ValueAnimator.AnimatorUpdateListener listener) {
        if (angle < 0 || 90 < angle) {
            // angle without 0 ~ 90.
            return null;
        }
        int finallyAngle = 0;
        int finallyStartX = startX;
        if (path == PATH.XP) { // x轴正方向, 与angle无关
            finallyAngle = 0;
            if (finallyStartX == -1) {
                finallyStartX = 0;
            }
        } else if (path == PATH.YP) { // y轴正方向, 与angle无关
            finallyAngle = 90;
        } else if (path == PATH.XN) { // x轴负方向, 与angle无关
            finallyAngle = 180;
            if (finallyStartX == -1) {
                finallyStartX = SCREEN_WIDTH;
            }
        } else if (path == PATH.YN) { // y轴负方向, 与angle无关
            finallyAngle = 270;
        } else if (path == PATH.XP_YP) { // x轴正方向, y轴正方向
            finallyAngle = angle;
            if (finallyStartX == -1) {
                finallyStartX = 0;
            }
        } else if (path == PATH.XP_YN) { // x轴正方向, y轴负方向
            finallyAngle = 180 - angle;
            if (finallyStartX == -1) {
                finallyStartX = SCREEN_WIDTH;
            }
        } else if (path == PATH.XN_YP) { // x轴负方向, y轴正方向
            finallyAngle = 180 + angle;
            if (finallyStartX == -1) {
                finallyStartX = SCREEN_WIDTH;
            }
        } else if (path == PATH.XN_YN) { // x轴负方向, y轴负方向
            finallyAngle = 360 - angle;
            if (finallyStartX == -1) {
                finallyStartX = 0;
            }
        }
        return transAnim(view, gravity, offsetY, finallyStartX, finallyAngle, listener);
    }

    /**
     * @param view
     * @param gravity y轴上的位置 Gravity.TOP,Gravity.CENTER_VERTICAL,Gravity.BOTTOM
     * @param offsetY y轴的偏移量
     * @param startX  x轴的起点坐标
     * @param angle   轨迹与x轴的角度 0~360
     * @return
     */
    public static ValueAnimator transAnim(final View view, int gravity, int offsetY, final int startX, final int
            angle, final ValueAnimator.AnimatorUpdateListener listener) {
        int startY = 0; // 确定Y轴上的位置
        // 根据offsetY偏移量计算出y轴开始坐标
        if (gravity == Gravity.TOP) {
            startY = 0 + offsetY;
        } else if (gravity == Gravity.CENTER_VERTICAL) {
            startY = SCREEN_HEIGHT_HALF - view.getHeight() / 2 + offsetY;
        } else if (gravity == Gravity.BOTTOM) {
            startY = SCREEN_HEIGHT - view.getHeight() + offsetY;
        } else {
            // gravity is not one of Gravity.TOP,Gravity.CENTER_VERTICAL,Gravity.BOTTOM
            return null;
        }

        int startx = 0;
        int endX = 0;

        // 因为必须移出到屏幕以外,利用角度, 得出x轴最终坐标
        if (360 - angle <= 90 || angle <= 90) { // x轴正方向
            endX = SCREEN_WIDTH + view.getWidth();
            startx = startX - view.getWidth(); // 需要减掉该view的宽
        } else { // x轴负方向
            endX = 0 - view.getWidth();
            startx = startX;
        }
        final int finallyStartX = startx;
        final int finallyEndX = endX; // x轴最终坐标
        final int originalY = startY; // y轴原始坐标, 根据gravity和offsetY计算得出
        final float tanAngle = (float) Math.tan(angle / 180.0f * Math.PI);
        final BigDecimal bigDecimal = new BigDecimal(tanAngle);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(5000);
        valueAnimator.setInterpolator(new MyInterploator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private FloatEvaluator mEvaluator = new FloatEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (listener != null) {
                    listener.onAnimationUpdate(animation);
                }
                float value = (float) animation.getAnimatedValue();
                float x = finallyStartX;
                float y = 0;
                if (angle == 270) { // y轴 负方向
                    y = mEvaluator.evaluate(value, originalY, 0 - view.getHeight());
                } else if (angle == 90) { // y轴 正方向
                    y = mEvaluator.evaluate(value, originalY, SCREEN_HEIGHT + view.getHeight());
                } else {
                    x = mEvaluator.evaluate(value, finallyStartX, finallyEndX);
                    y = originalY + ((x - finallyStartX) * bigDecimal.floatValue());
                }
                view.setX(x);
                view.setY(y);
                //                Log.i(TAG, "onAnimationUpdate: x" + x + " y:" + y);
            }
        });
        return valueAnimator;
    }

    public static class MyInterploator implements TimeInterpolator {

        private float startTime = 0.5f;
        private float endTime;
        private float stayTime = 0.2f;

        public MyInterploator() {
            endTime = startTime + stayTime;
        }

        @Override
        public float getInterpolation(float input) {
            if (startTime < input && input <= endTime) {
                return startTime;
            } else if (input > endTime) {
                // 基数
                float mul = (1.0f - startTime) / (1.0f - endTime);
                return (input - 1.0f) * mul + 1.0f;
            }
            return input;
        }
    }

}
