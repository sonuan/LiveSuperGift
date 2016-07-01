package com.sonuan.livegift.gift.base;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.transforms.Transform;

public class GiftBaseDrawable extends GifDrawable implements Animatable {
    private GiftBaseRenderer mLoadingRender;
    private Transform mTransform;

    private final Drawable.Callback mCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable d) {
            invalidateSelf();
        }

        @Override
        public void scheduleDrawable(Drawable d, Runnable what, long when) {
            scheduleSelf(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable d, Runnable what) {
            unscheduleSelf(what);
        }
    };

    public GiftBaseDrawable(Resources res, int id, GiftBaseRenderer loadingRender) throws IOException {
        super(res, id);
        this.mLoadingRender = loadingRender;
        this.mLoadingRender.setCallback(mCallback);
        mLoadingRender.setWidth(getIntrinsicWidth());
        mLoadingRender.setHeight(getIntrinsicHeight());
        mTransform = new Transform() {
            @Override
            public void onBoundsChange(Rect bounds) {
                mLoadingRender.onBoundsChange(bounds);
            }

            @Override
            public void onDraw(Canvas canvas, Paint paint, Bitmap buffer) {
                mLoadingRender.onDraw(canvas, paint, buffer);
            }
        };
        setTransform(mTransform);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mLoadingRender.draw(canvas, getBounds());
    }

    @Override
    public void setAlpha(int alpha) {
        super.setAlpha(alpha);
        mLoadingRender.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        super.setColorFilter(cf);
        mLoadingRender.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void start() {
        super.start();
        mLoadingRender.start();
    }

    @Override
    public void stop() {
        super.stop();
        mLoadingRender.stop();
    }

    @Override
    public boolean isRunning() {
        return super.isRunning() || mLoadingRender.isRunning();
    }
}
