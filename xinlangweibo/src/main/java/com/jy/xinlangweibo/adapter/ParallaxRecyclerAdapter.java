package com.jy.xinlangweibo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.constant.CustomConstant;
import com.jy.xinlangweibo.utils.Utils;
import com.jy.xinlangweibo.widget.PulltorefreshRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public abstract class ParallaxRecyclerAdapter<T> extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private float mScrollMultiplier = 0.5f;

	public static class VIEW_TYPES {
		public static final int NORMAL = 1;
		public static final int HEADER = 2;
		public static final int FIRST_VIEW = 3;
	}

	public abstract void onBindViewHolderImpl(
			RecyclerView.ViewHolder viewHolder,
			ParallaxRecyclerAdapter<T> adapter, int i);

	public abstract RecyclerView.ViewHolder onCreateViewHolderImpl(
			ViewGroup viewGroup, ParallaxRecyclerAdapter<T> adapter, int i);

	public abstract int getItemCountImpl(ParallaxRecyclerAdapter<T> adapter);

	public interface OnClickEvent {
		/**
		 * Event triggered when you click on a item of the adapter
		 * 
		 * @param v
		 *            view
		 * @param position
		 *            position on the array
		 */
		void onClick(View v, int position);
	}

	public interface OnParallaxScroll {
		/**
		 * Event triggered when the parallax is being scrolled.
		 * 
		 * @param percentage
		 * @param offset
		 * @param parallax
		 */
		void onParallaxScroll(float percentage, float offset, View parallax);
	}

	private List<T> mData;
	private CustomRelativeWrapper mHeader;
	private OnClickEvent mOnClickEvent;
	private OnParallaxScroll mParallaxScroll;
	private PulltorefreshRecyclerView mRecyclerView;
	private boolean mShouldClipView = true;
	private Context context;

	/**
	 * Translates the adapter in Y
	 * 
	 * @param of
	 *            offset in px
	 */
	public void translateHeader(float of) {
		float ofCalculated = of * mScrollMultiplier;
		// System.out.println("ofCalculated:"+ofCalculated);
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
		// && of < mHeader.getHeight()) {
		// mHeader.setTranslationY(ofCalculated);
		// } else if (of < mHeader.getHeight()) {
		// TranslateAnimation anim = new TranslateAnimation(0, 0,
		// ofCalculated, ofCalculated);
		// anim.setFillAfter(true);
		// anim.setDuration(0);
		// mHeader.startAnimation(anim);
		// }
		// 这里用位移动画的位移距离 of * mScrollMultiplier 加上ClipY的缩放距离 of *
		// mScrollMultiplier
		// 组成了整个parraxhead 动画效果
		mHeader.setClipY(Math.round(ofCalculated));
		if (mParallaxScroll != null) {
			// 这里通过位置0的复用holder是否还存在来判断滑动百分比
			final RecyclerView.ViewHolder holder = mRecyclerView
					.findViewHolderForAdapterPosition(0);
			float left;
			if (holder != null) {
				left = Math
						.min(1,
								((ofCalculated) / ((mHeader.getHeight()-Utils.dip2px(CustomConstant.getContext(), 48)) * mScrollMultiplier)));
			} else {
				left = 1;
			}
			mParallaxScroll.onParallaxScroll(left, of, mHeader);
		}
	}

	/**
	 * Set the view as header.
	 * 
	 * @param header
	 *            The inflated header
	 * @param view
	 *            The RecyclerView to set scroll listeners
	 */
	public void setParallaxHeader(View header, final RecyclerView view) {
		mRecyclerView = (PulltorefreshRecyclerView) view;
		context = header.getContext();
		mHeader = new CustomRelativeWrapper(header.getContext(),
				mShouldClipView);
		mHeader.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		mHeader.addView(
				header,
				new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue
								.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
										270, context.getResources()
												.getDisplayMetrics())));
		view.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (mHeader != null) {
					if (mHeader != null) {
						translateHeader(mRecyclerView.getLayoutManager()
								.getChildAt(0) == mHeader ? mRecyclerView
								.computeVerticalScrollOffset() : mHeader
								.getHeight());
					}

				}
			}
		});
	}

	// 这里对onbind特殊处理 主要是在recyleview 有headview时与dataset下标对应
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
		if (i != 0 && mHeader != null) {
			onBindViewHolderImpl(viewHolder, this, i - 1);
		} else if (i != 0) {
			onBindViewHolderImpl(viewHolder, this, i);
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup,
			final int i) {
		if (i == VIEW_TYPES.HEADER && mHeader != null)
			return new ViewHolder(mHeader);
		if (i == VIEW_TYPES.FIRST_VIEW && mHeader != null
				&& mRecyclerView != null) {
			final RecyclerView.ViewHolder holder = mRecyclerView
					.findViewHolderForAdapterPosition(0);
			if (holder != null) {
				// translateHeader(-holder.itemView.getTop());
			}
		}
		final RecyclerView.ViewHolder holder = onCreateViewHolderImpl(
				viewGroup, this, i);
		if (mOnClickEvent != null) {
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnClickEvent.onClick(v, holder.getAdapterPosition()
							- (mHeader == null ? 0 : 1));
				}
			});
		}
		return holder;
	}

	/**
	 * @return true if there is a header on this adapter, false otherwise
	 */
	public boolean hasHeader() {
		return mHeader != null;
	}

	public void setOnClickEvent(OnClickEvent onClickEvent) {
		mOnClickEvent = onClickEvent;
	}

	public boolean isShouldClipView() {
		return mShouldClipView;
	}

	/**
	 * Defines if we will clip the layout or not. MUST BE CALLED BEFORE
	 * {@link #setParallaxHeader(android.view.View, android.support.v7.widget.RecyclerView)}
	 * 
	 * @param shouldClickView
	 */
	public void setShouldClipView(boolean shouldClickView) {
		mShouldClipView = shouldClickView;
	}

	public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
		mParallaxScroll = parallaxScroll;
		mParallaxScroll.onParallaxScroll(0, 0, mHeader);
	}

	 protected ImageLoader imageLoader;

	public ParallaxRecyclerAdapter(List<T> data) {
		mData = data;
		imageLoader = ImageLoader.getInstance();
	}

	public List<T> getData() {
		return mData;
	}

	public void setData(List<T> data) {
		mData = data;
		notifyDataSetChanged();
	}

	public void addItem(T item, int position) {
		mData.add(position, item);
		notifyItemInserted(position + (mHeader == null ? 0 : 1));
	}

	public void removeItem(T item) {
		int position = mData.indexOf(item);
		if (position < 0)
			return;
		mData.remove(item);
		notifyItemRemoved(position + (mHeader == null ? 0 : 1));
	}

	public int getItemCount() {
		return getItemCountImpl(this) + (mHeader == null ? 0 : 1);
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 1)
			return VIEW_TYPES.FIRST_VIEW;
		return position == 0 ? VIEW_TYPES.HEADER : VIEW_TYPES.NORMAL;
	}

	private  class ViewHolder extends RecyclerView.ViewHolder {
		private View head;
		private View headBackground;

		public ViewHolder(View itemView) {
			super(itemView);
			head = itemView.findViewById(R.id.head);
			headBackground = itemView.findViewById(R.id.headBackground);
			if (mRecyclerView.getMheadView() == null) {
				mRecyclerView.setMheadView(head);
			}
			if (mRecyclerView.getMheadBackground() == null) {
				mRecyclerView.setMheadBackground(headBackground);
			}
		}
	}

	static class CustomRelativeWrapper extends RelativeLayout {

		private int mOffset;
		private boolean mShouldClip;

		public CustomRelativeWrapper(Context context, boolean shouldClick) {
			super(context);
			mShouldClip = shouldClick;
		}

		// 实现缩放的代码
		// @Override
		// protected void dispatchDraw(Canvas canvas) {
		// // 这里对画布进行了处理 具体处理效果还有待分析。。。。
		// // 正常的RECT应该为Rect(getLeft(), getTop(), getRight(),
		// // getBottom() + mOffset)) offset为滚动偏移量
		// //
		// if (mShouldClip) {
		// canvas.clipRect(new Rect(getLeft(), getTop(), getRight(),
		// getBottom() + mOffset));
		// }
		// super.dispatchDraw(canvas);
		// }

		public void setClipY(int offset) {
			mOffset = offset;
			invalidate();
		}
	}

	/**
	 * Set parallax scroll multiplier.
	 * 
	 * @param mul
	 *            The multiplier
	 */
	public void setScrollMultiplier(float mul) {
		this.mScrollMultiplier = mul;
	}

	/**
	 * Get the current parallax scroll multiplier.
	 * 
	 */
	public float getScrollMultiplier() {
		return this.mScrollMultiplier;
	}

}
