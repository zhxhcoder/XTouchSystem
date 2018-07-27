package com.zhxh.xtouchsystem.touch.conflict;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

import java.util.LinkedList;
import java.util.Queue;

public class HorizontalListView extends AdapterView<ListAdapter> {

    private final static int MAX_SLIDE_Y = 6;

    private View childSelected;
    private float xInit;
    private float yInit;

    public boolean mAlwaysOverrideTouch = true;
    protected ListAdapter mAdapter;
    private int mLeftViewIndex = -1;
    private int mRightViewIndex = 0;
    protected int mCurrentX;
    protected int mNextX;
    private int mMaxX = Integer.MAX_VALUE;
    private int mDisplayOffset = 0;
    protected Scroller mScroller;
    private GestureDetector mGesture;
    private Queue<View> mRemovedViewQueue = new LinkedList<View>();
    private OnItemSelectedListener mOnItemSelected;
    private OnItemClickListener mOnItemClicked;
    private OnTouchListener mOnItemOutUpListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnTouchListener mOnItemMoveListener; // It is an hacked touchLister,
    // in fact it is
    // OnMoveListener
    private OnItemClickListener mOnItemReceiver;
    private boolean mDataChanged = false;
    private boolean isMove = false;

    protected OnHoriScroll mHoriScroll;

    protected int mSpace = 25;

    protected void Log(Object msg) {
    }

    public interface OnHoriScroll {
        void onScorll(boolean onStart, boolean onEnd);
    }

    public void setOnHoriScroll(OnHoriScroll mHoriScroll) {
        this.mHoriScroll = mHoriScroll;
    }

    public void setSpace(int mSpace) {
        this.mSpace = mSpace;
    }

    public HorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    private synchronized void initView() {
        mLeftViewIndex = -1;
        mRightViewIndex = 0;
        mDisplayOffset = 0;
        mCurrentX = 0;
        mNextX = 0;
        mMaxX = Integer.MAX_VALUE;
        mScroller = new Scroller(getContext());
        mGesture = new GestureDetector(getContext(), mOnGesture);
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mOnItemSelected = listener;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClicked = listener;
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public void setOnItemMoveListener(OnTouchListener listener) {
        mOnItemMoveListener = listener;
    }

    public void setOnItemUpOutListener(OnTouchListener listener) {
        this.mOnItemOutUpListener = listener;
    }

    public void setOnItemReceiverListener(OnItemClickListener listener) {
        this.mOnItemReceiver = listener;
    }

    private DataSetObserver mDataObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            synchronized (HorizontalListView.this) {
                mDataChanged = true;
            }
            invalidate();
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            reset();
            invalidate();
            requestLayout();
        }

    };

    @Override
    public ListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mDataObserver);
        reset();
    }

    private synchronized void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    private void addAndMeasureChild(final View child, int viewPos) {
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = generateDefaultLayoutParams();
        }

        addViewInLayout(child, viewPos, params, true);

        child.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected synchronized void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mAdapter == null) {
            return;
        }

        if (mDataChanged) {
            int oldCurrentX = mCurrentX;
            initView();
            removeAllViewsInLayout();
            mNextX = oldCurrentX;
            mDataChanged = false;
        }

        if (mScroller.computeScrollOffset()) {
            int scrollx = mScroller.getCurrX();
            mNextX = scrollx;
        }

        if (mNextX < 0) {
            mNextX = 0;
            mScroller.forceFinished(true);
        }
        if (mNextX > mMaxX) {
            mNextX = mMaxX;
            mScroller.forceFinished(true);
        }

        int dx = mCurrentX - mNextX;

        removeNonVisibleItems(dx);
        fillList(dx);
        positionItems(dx);

        mCurrentX = mNextX;

        if (null != mHoriScroll) {
            mHoriScroll.onScorll(mCurrentX == 0, mCurrentX == mMaxX);
        }

        if (!mScroller.isFinished()) {
            post(new Runnable() {
                @Override
                public void run() {
                    requestLayout();
                }
            });
        }
    }

    private void fillList(final int dx) {
        int edge = 0;
        View child = getChildAt(getChildCount() - 1);
        if (child != null) {
            edge = child.getRight();
        }

        fillListRight(edge, dx);

        edge = 0;
        child = getChildAt(0);
        if (child != null) {
            edge = child.getLeft();
        }

        fillListLeft(edge, dx);
    }

    private void fillListRight(int rightEdge, final int dx) {
        while (rightEdge + dx < getWidth() && mRightViewIndex < mAdapter.getCount()) {
            View child = mAdapter.getView(mRightViewIndex, mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, -1);
            rightEdge += child.getMeasuredWidth() + UIUtils.pxToDip(this.getContext(), mSpace);
            ;

            if (mRightViewIndex == mAdapter.getCount() - 1) {
                if (mCurrentX + rightEdge < getWidth()) {
                    mMaxX = 0;
                } else {
                    mMaxX = mCurrentX + rightEdge - getWidth();
                }
            }

            mRightViewIndex++;
        }
    }

    private void fillListLeft(int leftEdge, final int dx) {
        while (leftEdge + dx > 0 && mLeftViewIndex >= 0) {
            View child = mAdapter.getView(mLeftViewIndex, mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, 0);
            leftEdge -= child.getMeasuredWidth();
            mLeftViewIndex--;
            mDisplayOffset -= child.getMeasuredWidth();
        }
    }

    private void removeNonVisibleItems(final int dx) {
        View child = getChildAt(0);
        while (child != null && child.getRight() + dx <= 0) {
            mDisplayOffset += child.getMeasuredWidth();
            mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            mLeftViewIndex++;
            child = getChildAt(0);

        }

        child = getChildAt(getChildCount() - 1);
        while (child != null && child.getLeft() + dx >= getWidth()) {
            mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            mRightViewIndex--;
            child = getChildAt(getChildCount() - 1);
        }
    }

    private void positionItems(final int dx) {
        if (getChildCount() > 0) {
            mDisplayOffset += dx;
            int left = mDisplayOffset;

            for (int i = 0; i < getChildCount(); i++) {
                int top = 0;
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                top = (getHeight() - child.getMeasuredHeight()) / 2;

                int len = UIUtils.pxToDip(this.getContext(), mSpace);

                child.layout(left, top, left + childWidth, child.getMeasuredHeight() + top);
                child.offsetLeftAndRight(len);

                left += childWidth + len;
            }
        }
    }

    public synchronized void scrollTo(int x) {
        mScroller.startScroll(mNextX, 0, x - mNextX, 0);
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean handled = false;

        if (mOnItemReceiver != null)
            handled = onUpReceive(ev);

        if (mOnItemMoveListener != null && !handled)
            handled = onMove(ev);

        if (!handled)
            handled = mGesture.onTouchEvent(ev);

        return handled;

    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        synchronized (HorizontalListView.this) {
            mScroller.fling(mNextX, 0, (int) -velocityX, 0, 0, mMaxX, 0, 0);
        }
        requestLayout();

        return true;
    }

    public boolean onDown(MotionEvent e) {
        isMove = false;
        mScroller.forceFinished(true);
        return true;
    }

    private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return HorizontalListView.this.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return HorizontalListView.this.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (distanceY < MAX_SLIDE_Y) {
                synchronized (HorizontalListView.this) {
                    mNextX += (int) distanceX;
                }
                requestLayout();
                requestDisallowInterceptTouchEvent(true);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Rect viewRect = new Rect();
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int left = child.getLeft();
                int right = child.getRight();
                int top = child.getTop();
                int bottom = child.getBottom();
                viewRect.set(left, top, right, bottom);
                if (viewRect.contains((int) e.getX(), (int) e.getY())) {

                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    if (mOnItemSelected != null) {
                        mOnItemSelected.onItemSelected(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    break;
                }
            }

        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Rect viewRect = new Rect();
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int left = child.getLeft();
                int right = child.getRight();
                int top = child.getTop();
                int bottom = child.getBottom();
                viewRect.set(left, top, right, bottom);
                if (viewRect.contains((int) e.getX(), (int) e.getY())) {
                    if (mOnItemClicked != null) {
                        mOnItemClicked.onItemClick(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    if (mOnItemSelected != null) {
                        mOnItemSelected.onItemSelected(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    break;
                }

            }
            return true;
        }

    };

    public boolean onMove(MotionEvent event) {

        float xNow = event.getX();
        float yNow = event.getY();

        Rect viewRect = new Rect();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float xInit1 = event.getX();
            float yInit1 = event.getY();

            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int left = child.getLeft();
                int right = child.getRight();
                int top = child.getTop();
                int bottom = child.getBottom();
                viewRect.set(left, top, right, bottom);
                if (viewRect.contains((int) xInit1, (int) yInit1)) {

                    if (mOnItemSelected != null) {
                        mOnItemSelected.onItemSelected(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }

                    this.xInit = xInit1;
                    this.yInit = yInit1;

                    childSelected = child;
                }
            }
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            if (!isMove) {
                if (Math.abs(yNow - yInit) > 0 && Math.abs(xNow - xInit) < 2) {
                    if (mOnItemMoveListener != null) {
                        mOnItemMoveListener.onTouch(childSelected, event);
                    }

                    isMove = true;
                    return true;

                }

            } else {
                mOnItemMoveListener.onTouch(childSelected, event);
                return true;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP && isMove) {
            int left = this.getLeft();
            int right = this.getRight();
            int top = this.getTop();
            int bottom = this.getBottom();
            Rect rect = new Rect(left, top, right, bottom);

            if (mOnItemMoveListener != null) {
                mOnItemMoveListener.onTouch(childSelected, event);
            }

            if (!rect.contains((int) xNow, (int) yNow)) {

                if (mOnItemOutUpListener != null) {
                    mOnItemOutUpListener.onTouch(this.childSelected, event);
                }

            }

            isMove = false;
            return false;
        }

        return false;
    }

    /**
     * @param e
     * @return
     */
    public boolean onUpReceive(MotionEvent e) {

        if (e.getAction() == MotionEvent.ACTION_UP) {
            Rect viewRect = new Rect();
            int x = (int) e.getX();
            int y = (int) e.getY();

            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int left = child.getLeft();
                int right = child.getRight();
                int top = child.getTop() + this.getTop();
                int bottom = child.getBottom() + this.getBottom();
                viewRect.set(left, top, right, bottom);
                if (viewRect.contains(x, y)) {
                    if (mOnItemSelected != null) {
                        mOnItemSelected.onItemSelected(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    if (mOnItemReceiver != null) {
                        mOnItemReceiver.onItemClick(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    return true;
                }

            }

            int left = this.getLeft();
            int right = this.getRight();
            int top = this.getTop();
            int bottom = this.getBottom();
            Rect rect = new Rect(left, top, right, bottom);

            if (rect.contains(x, y)) {

                if (this.getChildCount() > 0) {
                    int maxX = this.getChildAt(this.getChildCount() - 1).getRight();
                    int minX = this.getChildAt(0).getRight();

                    if (x < minX) {
                        mOnItemReceiver.onItemClick(HorizontalListView.this, null, 0, 0);
                    } else {
                        if (x > maxX) {
                            mOnItemReceiver.onItemClick(HorizontalListView.this, null, this.getChildCount() - 1, 0);
                        }

                    }

                    return true;

                } else {

                    if (mOnItemReceiver != null) {
                        mOnItemReceiver.onItemClick(HorizontalListView.this, null, 0, 0);
                    }
                    return true;
                }

            }

        }
        return false;
    }

    @Override
    public void setSelection(int position) {
        int childNum = getChildCount();
        for (int i = 0; i < childNum; i++) {
            View child = getChildAt(i);
            if (i + mLeftViewIndex + 1 != position) {
                child.setSelected(false);
            } else {
                child.setSelected(true);
            }
        }
    }

}