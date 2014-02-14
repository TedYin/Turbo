package com.turbo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 横向滑动控件
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboScrollLayout extends ViewGroup {

	private static final String TAG = "TurboScrollLayout";
	private VelocityTracker mVelocityTracker; // 用于判断滑动手势速度
	private static final int SNAP_VELOCITY = 600; // 滑动速度临界值
	private Scroller mScroller; // 滑动控制器
	private int mCurScreen = 0; // 当前屏幕(默认第一屏的位置为0)
	private float mLastMotionX; // 前一个View的X坐标
	private OnViewChangeListener listener; // 滑动监听器

	/**
	 * ScrollLayout滑动监听
	 * 
	 * @author Ted
	 */
	public interface OnViewChangeListener {
		/**
		 * 切换到目标View
		 * 
		 * @param destViewPosition
		 */
		public void onViewChange(int destViewPosition);
	}

	public TurboScrollLayout(Context context) {
		super(context);
		init(context);
	}

	public TurboScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TurboScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mScroller = new Scroller(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			int childLeft = 0;
			for (int i = 0; i < getChildCount(); i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft + childWidth,
							childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurScreen * width, 0);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float x = event.getX();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			}
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;

		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);
			if (IsCanMove(deltaX)) {
				if (mVelocityTracker != null) {
					mVelocityTracker.addMovement(event);
				}
				mLastMotionX = x;
				scrollBy(deltaX, 0);
			}

			break;
		case MotionEvent.ACTION_UP:
			int velocityX = 0;
			if (mVelocityTracker != null) {
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				velocityX = (int) mVelocityTracker.getXVelocity();
			}
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				// Fling enough to move left
				Log.e(TAG, "snap left");
				snapToScreen(mCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1) {
				// Fling enough to move right
				Log.e(TAG, "snap right");
				snapToScreen(mCurScreen + 1);
			} else {
				int screenWidth = getWidth();
				int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
				snapToScreen(destScreen);
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;
		}
		return true;
	}
	
	/**
	 * 是否可以继续滚动
	 * @param deltaX
	 * @return
	 */
	private boolean IsCanMove(int deltaX) {
		if (getScrollX() <= 0 && deltaX < 0) 
			return false;
		if (getScrollX() >= (getChildCount() - 1) * getWidth() && deltaX > 0) 
			return false;
		return true;
	}
	
	/**
	 * 滑动到指定的Screen
	 * 
	 * @param whichScreen
	 */
	public void snapToScreen(int whichScreen) {
		// get the valid layout page
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * getWidth())) {
			final int delta = whichScreen * getWidth() - getScrollX();
			mScroller.startScroll(getScrollX(), 0, delta, 0,
					Math.abs(delta) * 2);

			mCurScreen = whichScreen;
			invalidate(); // Redraw the layout
			if (listener != null) {
				listener.onViewChange(mCurScreen);
			}
		}
	}
	
	/**
	 * 添加监听器
	 * @param listener
	 */
	public void addOnViewChangeListener(OnViewChangeListener listener) {
		this.listener = listener;
	}
}