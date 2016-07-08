package com.sonuan.livegift.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**********************
 * @author: wusongyuan
 * @date: 2016-07-07
 * @desc:
 **********************/
public class MyImageView extends ImageView {
    public static final String TAG = MyImageView.class.getSimpleName();

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure: ");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout: ");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: ");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "onFinishInflate: ");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow: ");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "onDetachedFromWindow: ");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.i(TAG, "draw: ");
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.i(TAG, "dispatchDraw: ");
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        Log.i(TAG, "onDrawForeground: ");
    }

    @Override
    public void invalidate() {
        super.invalidate();
        Log.i(TAG, "invalidate: ");
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
        Log.i(TAG, "invalidate: ");
    }

    @Override
    public void invalidate(Rect dirty) {
        super.invalidate(dirty);
        Log.i(TAG, "invalidate: ");
    }

    @Override
    public void invalidateDrawable(Drawable dr) {
        super.invalidateDrawable(dr);
        Log.i(TAG, "invalidateDrawable: ");
    }

    @Override
    public void invalidateOutline() {
        super.invalidateOutline();
        Log.i(TAG, "invalidateOutline: ");
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        Log.i(TAG, "requestLayout: ");
    }
}
