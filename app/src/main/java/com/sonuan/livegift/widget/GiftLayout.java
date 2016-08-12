package com.sonuan.livegift.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Mianamiana on 16/7/4.
 * 为高级礼物做的layout
 * function:1.镜像
 *          2.todo 按比例添加childView
 */
public class GiftLayout extends FrameLayout {

    private boolean mIsMirror = false;


    @interface type{
        int wheel =1;
        int light =2;
    }

    public GiftLayout(Context context) {
        this(context, null);
    }

    public GiftLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    public void draw(Canvas canvas) {
        //简单的镜像开启方法
        if (mIsMirror) {
            canvas.save();
            canvas.scale(-1, 1, getWidth() / 2, getHeight() / 2);
            super.draw(canvas);
            canvas.restore();
        } else {
            super.draw(canvas);
        }
    }


    public boolean isMirror() {
        return mIsMirror;
    }


    /**
     * 是否开启镜像
     * @param isMirror
     */
    public void setIsMirror(boolean isMirror) {
        mIsMirror = isMirror;
        ViewCompat.postInvalidateOnAnimation(this);
    }

}
