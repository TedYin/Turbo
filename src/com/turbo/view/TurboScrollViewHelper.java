package com.turbo.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.tedyin.turbo.R;
import com.turbo.view.TurboScrollLayout.OnViewChangeListener;

import java.util.Map;

/**
 * ScrollLayout 配置类
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboScrollViewHelper implements OnViewChangeListener, OnClickListener {

	private Context context;
	private TurboScrollLayout scrollLayout; // 滑动布局
	private LinearLayout dotLayout; // 底部原点布局
	private FrameLayout parentLayout; // 父Layout，承载scrollLayout和dotLayout
	private int[] viewBgRes;
	private ImageView[] mImageViews;
	private int mCurSel = 0; // 当前选中的View
	private int viewCount = 0; // View的数量

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param viewBgRes
	 */
	public TurboScrollViewHelper(Context context, int[] viewBgRes) {
		this.context = context;
		this.viewBgRes = viewBgRes;
		this.viewCount = viewBgRes.length;
		initLayout();
	}

	/**
	 * 初始化Layout布局
	 */
	private void initLayout() {
		// init Scroll Layout
		scrollLayout = new TurboScrollLayout(context);
		scrollLayout.addOnViewChangeListener(this);
		scrollLayout.setMinimumHeight(LayoutParams.MATCH_PARENT);
		scrollLayout.setMinimumWidth(LayoutParams.MATCH_PARENT);

		// init Dot layout
		dotLayout = new LinearLayout(context);
		LinearLayout.LayoutParams dotLP = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		dotLayout.setOrientation(LinearLayout.HORIZONTAL);
		dotLayout.setLayoutParams(dotLP);

		// init parent layout
		parentLayout = new FrameLayout(context);
		// RelativeLayout.LayoutParams parentLP = new
		// RelativeLayout.LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		FrameLayout.LayoutParams parentLP = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		parentLayout.setLayoutParams(parentLP);
	}

	@Override
	public void onViewChange(int destViewPosition) {
		setCurPoint(destViewPosition);
	}

	public void onClick(View v) {
		int pos = (Integer) (v.getTag());
		setCurPoint(pos);
		scrollLayout.snapToScreen(pos);
	}

	/**
	 * 初始化ScrollLayout部分
	 * 
	 * @param viewBgRes
	 */
	private void initScrollLayout(int[] viewBgRes) {
		for (int i = 0; i < viewBgRes.length; i++) {
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			FrameLayout layout = new FrameLayout(context);
			layout.setBackgroundResource(viewBgRes[i]);
			layout.setLayoutParams(lp);
			scrollLayout.addView(layout);
		}
	}

	/**
	 * 自定义最后一个View
	 * 
	 * @param lastLayoutId
	 *            最后一个View的Id
	 * @param btnMap
	 *            Map<按钮ID，按钮监听器>
	 */
	private void initScrollLayout(int lastLayoutId,
			Map<Integer, OnClickListener> btnMap) {
		initScrollLayout(viewBgRes);
		LayoutInflater inflater = LayoutInflater.from(context);
		View lastLayout = inflater.inflate(lastLayoutId, null);
		for (int btnId : btnMap.keySet()) {
			lastLayout.findViewById(btnId)
					.setOnClickListener(btnMap.get(btnId));
		}
		scrollLayout.addView(lastLayout);
	}

	/**
	 * 初始化Dot部分
	 * 
	 * @param viewCount
	 */
	private void initDotLayout(int viewCount) {
		mImageViews = new ImageView[viewCount];
		LinearLayout.LayoutParams imageLp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int margin = px2dip(40);
		int padding = px2dip(15);
		imageLp.setMargins(margin, margin, margin, margin);
		imageLp.gravity = Gravity.CENTER_VERTICAL;
		for (int i = 0; i < viewCount; i++) {
			ImageView image = new ImageView(context);
			image.setBackgroundResource(R.drawable.turbo_scroll_view_dot_bg);
			image.setPadding(padding, padding, padding, padding);
			image.setClickable(true);
			image.setTag(i);
			image.setOnClickListener(this);
			mImageViews[i] = image;
			dotLayout.addView(image, imageLp);
		}
		mImageViews[0].setEnabled(false);
	}

	/**
	 * 设置当前Dot的位置
	 * 
	 * @param index
	 */
	private void setCurPoint(int index) {
		if (index < 0 || index > viewCount - 1 || mCurSel == index) {
			return;
		}
		mImageViews[mCurSel].setEnabled(true);
		mImageViews[index].setEnabled(false);
		mCurSel = index;
		if (index == viewCount - 1) {
			dotLayout.setVisibility(View.GONE);
		} else {
			dotLayout.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	private int px2dip(float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 创建滑动布局
	 * 
	 * @return
	 */
	public View createView() {
		initScrollLayout(viewBgRes);
		initDotLayout(viewBgRes.length);
		// 添加ScrollLayout
		FrameLayout.LayoutParams scrollParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		parentLayout.addView(scrollLayout, scrollParams);
		// 添加DotLayout
		FrameLayout.LayoutParams dotParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		dotParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		dotParams.setMargins(0, 0, 0, px2dip(30));
		parentLayout.addView(dotLayout, dotParams);
		return parentLayout;
	}

	/**
	 * 创建滑动布局
	 * 
	 * @param lastLayoutId
	 * @param btnMap
	 *            Map<按钮ID，按钮监听器>
	 * @return
	 */
	public View createView(int lastLayoutId,
			Map<Integer, OnClickListener> btnMap) {
		// 添加最后一个自定义View
		viewCount += 1;
		initScrollLayout(lastLayoutId, btnMap);
		initDotLayout(viewBgRes.length + 1);
		// 添加ScrollLayout
		FrameLayout.LayoutParams scrollParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		parentLayout.addView(scrollLayout, scrollParams);
		// 添加DotLayout
		FrameLayout.LayoutParams dotParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		dotParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		dotParams.setMargins(0, 0, 0, px2dip(30));
		parentLayout.addView(dotLayout, dotParams);
		return parentLayout;
	}
}
