package cn.joy.widget.shadow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;

/**
 * User: JiYu
 * Date: 2016-08-26
 * Time: 15-25
 */

public class ShadowGridView extends GridView{

	private int mSelectedPosition = 0;

	public ShadowGridView(Context context) {
		super(context);
		init();
	}

	public ShadowGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ShadowGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		setClipChildren(false);
		setClipToPadding(false);
		setChildrenDrawingOrderEnabled(true);
	}

	@Override
	public void bringChildToFront(View child) {
		//		super.bringChildToFront(child);
		mSelectedPosition = indexOfChild(child);
		invalidate();
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if(mSelectedPosition != AbsListView.INVALID_POSITION){
			if(i == mSelectedPosition){
				return --childCount;
			}
			if(i == childCount - 1){
				return mSelectedPosition;
			}
		}
		return super.getChildDrawingOrder(childCount, i);
	}
}
