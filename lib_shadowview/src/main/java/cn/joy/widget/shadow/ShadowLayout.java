package cn.joy.widget.shadow;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import cn.joy.widget.R;

/**
 * User: JiYu
 * Date: 2016-08-26
 * Time: 13-50
 */

public class ShadowLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {

	/**
	 * 状态改变类型 Focus：焦点状态，Select：选中状态
	 */
	enum ChangeType {
		Focus, Select
	}

	//  默认选择效果的放大比例
	private static final float SCALE_CONTENT_DEFAULT = 1.1F;
	//  子view,有且仅有一个
	private View mChildView;

	//  缩放比计算flag
	private boolean calculated = false;
	//  内容缩放比以及阴影缩放比
	private float scx, scy, ssx, ssy;
	//  阴影宽度
	private int mShadowSize;
	//  缩放比
	private float mScale;

	private ValueAnimator mAnim;

	private ChangeType mChangeType;

	public ShadowLayout(Context context) {
		super(context);
		init(context, null);
	}

	public ShadowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
		mShadowSize = ta.getDimensionPixelSize(R.styleable.ShadowLayout_sl_shadowWidth, getResources().getDimensionPixelOffset(R.dimen.shadow_width));
		mScale = ta.getFloat(R.styleable.ShadowLayout_sl_scale, SCALE_CONTENT_DEFAULT);
		int mShadowResourceId = ta.getResourceId(R.styleable.ShadowLayout_sl_shadowDrawable, R.drawable.draw_selector_item_shadow);
		mChangeType = ta.getInteger(R.styleable.ShadowLayout_sl_changeType, 0) == 0 ? ChangeType.Focus : ChangeType.Select;
		ta.recycle();
		setBackgroundResource(mShadowResourceId);

		if (mChangeType == ChangeType.Focus) {
			setFocusable(true);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (getScaleX() != 1f || getScaleY() != 1f || calculated)
			return;
		//  获取真实的未放大的宽高
		final int w = MeasureSpec.getSize(widthMeasureSpec);
		final int h = MeasureSpec.getSize(heightMeasureSpec);
		if (w == 0 || h == 0)
			return;
		//  放大后阴影效果下的宽高
		final float sw = w * mScale + mShadowSize;
		final float sh = h * mScale + mShadowSize;
		//  放大后内容的宽高
		final float scw = w * mScale;
		final float sch = h * mScale;
		//  获取正确的缩放比
		ssx = sw / w;
		ssy = sh / h;
		scx = scw / sw;
		scy = sch / sh;
		calculated = true;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (getChildCount() > 1) {
			throw new IllegalArgumentException("child view can only be one!!!");
		}
		if (getChildCount() == 1) {
			mChildView = getChildAt(0);
		}
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if (mChangeType == ChangeType.Focus)
			onStateChanged(gainFocus);
	}

	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		if (mChangeType == ChangeType.Select)
			onStateChanged(selected);
	}

	/**
	 * 焦点变化或select状态变化时调用
	 * @param changed 是否已变化
	 */
	private void onStateChanged(boolean changed) {
		float fraction, end;
		if (changed) {
			//  将自己置于顶层
			bringToFront();
			fraction = 0f;
			end = 1f;
		} else {
			fraction = 1f;
			end = 0f;
		}
		if (mAnim != null && mAnim.isRunning()) {
			mAnim.cancel();
			fraction = mAnim.getAnimatedFraction();
		}
		mAnim = ValueAnimator.ofFloat(fraction, end);
		mAnim.addUpdateListener(this);
		mAnim.start();
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		final float v = Float.parseFloat(animation.getAnimatedValue().toString());
		if(getTag() != null && getTag().equals("xx")){
			Log.d("anim", v + "");
		}
		final float ssx0 = getScale(1f, calculated ? ssx : mScale, v);
		final float ssy0 = getScale(1f, calculated ? ssy : mScale, v);
		setScaleX(ssx0);
		setScaleY(ssy0);
		if (mChildView != null) {
			final float scx0 = getScale(1f, calculated ? scx : mScale, v);
			final float scy0 = getScale(1f, calculated ? scy : mScale, v);
			mChildView.setScaleX(scx0);
			mChildView.setScaleY(scy0);
		}
	}

	private float getScale(float start, float end, float fraction) {
		return (end - start) * fraction + start;
	}
}
