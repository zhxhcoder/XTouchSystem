package com.zhxh.xtouchsystem.touch;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.zhxh.xtouchsystem.Const;


public class CFrameLayout extends FrameLayout {
    public CFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(Const.TAG, "[省长]-dispatchTouchEvent-任务<" + Util.actionToString(event.getAction()) + "> : 需要分派");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean bo = false;
        Log.d(Const.TAG, "[省长]-onInterceptTouchEvent-任务<" + Util.actionToString(event.getAction()) + "> : 拦截吗？" + bo);
        return bo;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean bo = false;
        Log.d(Const.TAG, "[省长]-onTouchEvent-任务<" + Util.actionToString(event.getAction()) + "> : 市长是个废物，下次再也不找你了，我自己来尝试一下。能解决？" + bo);
        return bo;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
