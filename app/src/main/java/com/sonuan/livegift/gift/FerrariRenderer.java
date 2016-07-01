package com.sonuan.livegift.gift;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.sonuan.livegift.gift.base.GiftBaseRenderer;


/**********************
 * @author: wusongyuan
 * @date: 2016-06-28
 * @desc:
 **********************/
public class FerrariRenderer extends GiftBaseRenderer {

    private static final String TAG = FerrariRenderer.class.getSimpleName();
    private RectF mDstRectF = new RectF();
    private Rect mSrcRect = new Rect();

    private Bitmap mBitmap;
    private float mProgress;

    public FerrariRenderer(Context context) {
        super(context);
        mProgress = 0;
    }

    public void onBoundsChange(Rect bounds) {
        Log.i(TAG, "onBoundsChange: " + bounds.left + " " + bounds.right + " " + bounds.top + " " + bounds.bottom);
    }

    @Override
    public void draw(Canvas canvas, Rect bounds) {

    }

    @Override
    public void onDraw(Canvas canvas, Paint paint, Bitmap buffer) {
        mBitmap = buffer;
        mSrcRect.set(0, 0, buffer.getWidth(), buffer.getHeight());
        float startX = ((getWidth() + mBitmap.getWidth()) * (1 - mProgress)) - mBitmap.getWidth();
        float startY = (getHeight() * mProgress);
        if (mProgress < 0.5) {
            canvas.scale(mProgress * 2, mProgress * 2, startX + mBitmap.getWidth(), startY);
        }
        Log.i(TAG, "onDraw: " + startX + " " + startY);
        //        mDstRect.set(startX, startY, startX + mBitmap.getWidth(), startY + mBitmap.getHeight());
        mDstRectF.set(startX, startY, startX + mBitmap.getWidth(), startY + mBitmap.getHeight());
        canvas.drawBitmap(buffer, mSrcRect, mDstRectF, paint);
    }

    @Override
    public void computeRender(float renderProgress) {
        mProgress = renderProgress;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public void reset() {
        mProgress = 0;
    }


}
