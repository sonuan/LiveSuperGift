package com.sonuan.livegift.activity;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.view.View;

/**********************
 * @author: wusongyuan
 * @date: 2016-07-01
 * @desc:
 **********************/
public class GiftAnimUtils {


    public static final int OFFSET_Y = 200;

    public static ValueAnimator transAnim(final View view, final int startX, final int startY, final int endX, final
    int endY) {
        return transAnim(view, startX, startY, endX, endY, OFFSET_Y, null);
    }

    public static ValueAnimator transAnim(final View view, final int startX, final int startY, final int endX, final int
            endY, int offsetY, final ValueAnimator.AnimatorUpdateListener listener) {
        final float finallyStartY = startY + offsetY;
        final float finallyEndY = endY + offsetY;
        view.setX(startX);
        view.setY(finallyStartY);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private FloatEvaluator mEvaluator = new FloatEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float x = mEvaluator.evaluate(value, startX, endX);
                float y = mEvaluator.evaluate(value, finallyStartY, finallyEndY);
                view.setX(x);
                view.setY(y);
                if (listener != null) {
                    listener.onAnimationUpdate(animation);
                }
            }
        });
        return valueAnimator;
    }

}
