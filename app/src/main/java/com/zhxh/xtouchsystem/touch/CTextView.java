package com.zhxh.xtouchsystem.touch;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class CTextView extends TextView {
    public CTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("zhxh", "【农民】-dispatchTouchEvent-任务<" + Util.actionToString(event.getAction()) + "> : 需要分派，我下面没人了，怎么办？自己干吧");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean bo = true;
        Log.d("zhxh", "【农民】-onTouchEvent-任务<" + Util.actionToString(event.getAction()) + "> : 自己动手，埋头苦干。能解决？" + bo);
        return bo;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


}