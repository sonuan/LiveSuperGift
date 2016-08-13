package com.sonuan.livegift.widget;

import android.animation.TimeInterpolator;

/**
 * @author: wusongyuan
 * @date: 2016.08.14
 * @desc:
 */
public class PeriodInterpolator implements TimeInterpolator {
    float average;
    float[] times;
    float[] progress;
    float[] multiples;
    float mDuration;
    //float time1 = 0;
    //float time2 = 0;
    //float time3 = 0;
    //float time4 = 0;

    PeriodInterpolator(long duration, float... times) {
        if (times == null || times.length == 0) {
            return;
        }
        average = 1.0f / (times.length - 1);
        this.times = times;
        this.mDuration = duration * 1.0f;
        //time1 = 400f / duration; // 0.08
        //time2 = 1000f / duration; // 0.2
        //time3 = 4000f / duration;
        //time4 = 5000f / duration;
        progress = new float[times.length];
        multiples = new float[times.length];
        for (int i = 0; i < times.length; i++) {
            progress[i] = times[i] / mDuration;
            //Log.i(TAG, "progress: " + progress[i]);
            if (i < times.length - 1) {
                float a = ((times[i + 1] - times[i]) / mDuration);
                multiples[i] = average / a;
                //Log.i(TAG, "a:" + a + "multiples: " + multiples[i]);
            }
        }
    }


    @Override
    public float getInterpolation(float input) {
        input = getInput(input);
        //Log.i("float", "getInterpolation() input:" + input);
        return input;
    }

    private float getInput(float input) {
        int index = 0;
        for (int i = 1; i < progress.length; i++) {
            if (input <= progress[i]) {
                index = i - 1;
                break;
            }
        }
        //Log.i(TAG, "getInput: index:" + index + " progress:" + progress[index] + " multiples:" + multiples[index]);
        input = index * average + (input - progress[index]) * multiples[index];
        return input;
    }
}
