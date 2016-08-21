package com.jy.xinlangweibo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.utils.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;
import java.util.List;

public class StaggeredHomeAdapter extends
		RecyclerView.Adapter<StaggeredHomeAdapter.MyViewHolder> {

	private ArrayList<Status> mDatas;
	private LayoutInflater mInflater;
	private ImageLoader imageLoader;
	private List<Integer> mHeights;
	private Context context;

	public interface OnItemClickLitener {
		void onItemClick(View view, int position);

		void onItemLongClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public StaggeredHomeAdapter(Context context, ArrayList<Status> datas,List<Integer> mHeights) {
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
		imageLoader = ImageLoader.getInstance();
		this.mHeights = mHeights;
		this.context = context;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(
				R.layout.item_staggered_home, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		if(mHeights.size() != 0) {
//			LayoutParams lp = holder.ll_status.getLayoutParams();
//			lp.height = mHeights.get(position);
//			
//			holder.ll_status.setLayoutParams(lp);
			holder.tv_content.setText(StringUtils.getKeyText(context, mDatas.get(position).text, holder.tv_content));
			imageLoader.displayImage(mDatas.get(position).user.avatar_hd,
					holder.iv_head, ImageLoadeOptions.getIvHeadOption());
			// 如果设置了回调，则设置点击事件
			if (mOnItemClickLitener != null) {
				holder.itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int pos = holder.getLayoutPosition();
						mOnItemClickLitener.onItemClick(holder.itemView, pos);
					}
				});
				
				holder.itemView.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						int pos = holder.getLayoutPosition();
						mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
						removeData(pos);
						return false;
					}
				});
			}
		}
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	// public void addData(int position)
	// {
	// mDatas.add(position, "Insert One");
	// mHeights.add( (int) (100 + Math.random() * 300));
	// notifyItemInserted(position);
	// }

	public void removeData(int position) {
		mDatas.remove(position);
		notifyItemRemoved(position);
	}

	class MyViewHolder extends ViewHolder {

		TextView tv_content;
		ImageView iv_head;
		LinearLayout ll_status;

		public MyViewHolder(View view) {
			super(view);
			tv_content = (TextView) view.findViewById(R.id.tv_content);
			iv_head = (ImageView) view.findViewById(R.id.iv_head);
			ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
		}
	}
}