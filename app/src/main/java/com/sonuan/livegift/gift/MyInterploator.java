package com.sonuan.livegift.gift;

import android.animation.TimeInterpolator;

/**********************
 * @author: wusongyuan
 * @date: 2016-06-30
 * @desc:
 **********************/
public class MyInterploator implements TimeInterpolator {

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
            //            float a = 1.0f - 1.0f / mul;
            //            return (input - a) * mul;
            return (input - 1.0f) * mul + 1.0f;
        }
        return input;
    }
}
