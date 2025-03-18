package com.huanjing.iotapp.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanjing.iotapp.R;


public class TopBar extends RelativeLayout {

	private TextView mRight, mCenter;
	private ImageView im_back;

	private Context context;
	private ITopBarClickListener topBarClickListener;
    private ImageView im_right;
	public TopBar(Context context) {
		this(context, null, 0);
	}

	public TopBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TopBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(attrs);
		setListener();
	}

	private void init(AttributeSet attrs) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.top_bar, this, true);

		im_back = (ImageView) findViewById(R.id.im_back);
		mRight = (TextView) findViewById(R.id.btn_right);
		mCenter = (TextView) findViewById(R.id.top_title);
		im_right=(ImageView)findViewById(R.id.im_right);
		TypedArray a = context
				.obtainStyledAttributes(attrs, R.styleable.topBar);


		String centerString = a.getString(R.styleable.topBar_centerText);
		String rightString = a.getString(R.styleable.topBar_rightText);
		Drawable rightDrawable = a
				.getDrawable(R.styleable.topBar_rightDrawable);
		boolean hideLeft = a.getBoolean(R.styleable.topBar_hideLeft, false);
		a.recycle();


		mCenter.setText(centerString);
		mRight.setText(rightString);
		if (rightDrawable != null) {
			mRight.setCompoundDrawablesWithIntrinsicBounds(null, null,
					rightDrawable, null);
		} else {
			mRight.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
					null);
		}
	}

	public ImageView getIm_back() {
		return im_back;
	}

	private void setListener() {
		im_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (topBarClickListener != null) {
					topBarClickListener.leftClick();
				}
			}
		});

		mRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (topBarClickListener != null) {
					topBarClickListener.rightClick();
				}
			}
		});
		im_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (topBarClickListener != null) {
					topBarClickListener.rightClick();
				}
			}
		});
	}

	public void hideLeftDrawable() {

	}

	/**
	 * 设置左边安钮的图�?
	 * 
	 * @param isRight
	 *            图片是否在右�?
	 */
	public void setLeftDrawable(boolean isRight, Drawable drawable) {

	}

	public ImageView getmLeft() {
		return im_back;
	}

	public TextView getmRight() {
		return mRight;
	}

	public void hideRightDrawable() {
		mRight.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
	}

	public void setRightText(String param, Drawable leftDrawable) {
		mRight.setText(param);
		mRight.setCompoundDrawablesWithIntrinsicBounds(null, null,
				leftDrawable, null);
	}

	public void setRightText(String param) {
		mRight.setText(param);
	}

	public void setRightTextColor(int id) {
		mRight.setTextColor(id);
	}

	public void setCenterText(String param) {
		mCenter.setText(param);
	}
	public void setRightIm(int rid) {
		im_right.setBackgroundResource(rid);
	}
	public void setTopBarClickListener(ITopBarClickListener topBarClickListener) {
		this.topBarClickListener = topBarClickListener;
	}

	/**
	 * TopBar点击回调
	 */
	public interface ITopBarClickListener {
		/**
		 * TopBar左边点击事件
		 */
		void leftClick();

		/**
		 * TopBar右边点击事件
		 */
		void rightClick();
	}
}
