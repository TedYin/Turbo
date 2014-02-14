package com.turbo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * 带有清空功能的EditText
 * @author Ted
 * @date 2013-4-13
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class ClearableEditText extends EditText implements
		OnFocusChangeListener, TextWatcher {

	private Drawable xD;


	public ClearableEditText(Context context) {
		super(context);
		init();
	}

	public ClearableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		xD = getCompoundDrawables()[2];//获取TextView中的隐藏图片：0--3分别代表：坐上右下的图片
		if (xD == null) {
			xD = getResources().getDrawable(android.R.drawable.btn_minus);
		}
		xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
		setClearIconVisible(false);
		super.setOnFocusChangeListener(this);
		addTextChangedListener(this);
	}

	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener f) {
		this.f = f;
	}

	private OnFocusChangeListener f;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean tappedX = event.getX() > (getWidth()
						- getPaddingRight() - xD.getIntrinsicWidth());
				if (tappedX) {
					setText("");

					event.setAction(MotionEvent.ACTION_CANCEL);

				}
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
		if (f != null) {
			f.onFocusChange(v, hasFocus);
		}
	}

	protected void setClearIconVisible(boolean visible) {
		Drawable x = visible ? xD : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		setClearIconVisible(s.length() > 0);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {
	}

}
