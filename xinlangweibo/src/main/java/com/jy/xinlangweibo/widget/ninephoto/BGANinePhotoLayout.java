package com.jy.xinlangweibo.widget.ninephoto;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jiang.library.ui.adapter.listviewadapter.ListViewDataAdapter;
import com.jiang.library.ui.adapter.listviewadapter.ViewHolderBase;
import com.jiang.library.ui.adapter.listviewadapter.ViewHolderCreator;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/8 下午2:41
 * 描述:
 */
public class BGANinePhotoLayout extends FrameLayout implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener, View.OnLongClickListener {
    private ListViewDataAdapter<String> mPhotoAdapter;
    private ImageView mPhotoIv;
    private BGAHeightWrapGridView mPhotoGv;
    private int mCurrentClickItemPosition;
    private Activity mActivity;

    public BGANinePhotoLayout(Context context) {
        this(context, null);
    }

    public BGANinePhotoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BGANinePhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPhotoIv = new ImageView(context);
        mPhotoIv.setClickable(true);
        mPhotoIv.setOnClickListener(this);
        mPhotoIv.setOnLongClickListener(this);

        mPhotoGv = new BGAHeightWrapGridView(context);
        int spacing = context.getResources().getDimensionPixelSize(R.dimen.bga_pp_size_photo_divider);
        mPhotoGv.setHorizontalSpacing(spacing);
        mPhotoGv.setVerticalSpacing(spacing);
        mPhotoGv.setNumColumns(3);
        mPhotoGv.setOnItemClickListener(this);
        mPhotoGv.setOnItemLongClickListener(this);
        mPhotoAdapter = new ListViewDataAdapter<String>(new ViewHolderCreator<String>() {
            @Override
            public ViewHolderBase<String> createViewHolder(int position) {
                return new NinePhotoItemViewHolder();
            }
        });
        mPhotoGv.setAdapter(mPhotoAdapter);

        addView(mPhotoIv, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mPhotoGv);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        mCurrentClickItemPosition = position;
        if (mDelegate != null) {
            mDelegate.onClickNinePhotoItem(this, view, mCurrentClickItemPosition, mPhotoAdapter.getItem(mCurrentClickItemPosition), mPhotoAdapter.getData());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        return false;
    }

    @Override
    public void onClick(View view) {
        mCurrentClickItemPosition = 0;
        if (mDelegate != null) {
            mDelegate.onClickNinePhotoItem(this, view, mCurrentClickItemPosition, mPhotoAdapter.getItem(mCurrentClickItemPosition), mPhotoAdapter.getData());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public void setDelegate(Delegate delegate) {
        this.mDelegate = delegate;
    }

    private Delegate mDelegate;

    public interface Delegate {
        void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models);

        boolean onLongClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models);
    }

    public void init(Activity activity) {
        mActivity = activity;
    }

    public void setData(ArrayList<String> photos) {
        if (mActivity == null) {
            throw new RuntimeException("请先调用init方法进行初始化");
        }

        int itemWidth = Utils.getDisplayWidthPixels(getContext()) / 4;
        if (photos.size() == 0) {
            setVisibility(GONE);
        } else if (photos.size() == 1) {
            setVisibility(VISIBLE);
            mPhotoGv.setVisibility(GONE);
            mPhotoAdapter.setData(photos);
            mPhotoIv.setVisibility(VISIBLE);

            mPhotoIv.setMaxWidth(itemWidth * 3);
            mPhotoIv.setMaxHeight(itemWidth * 3);

            CustomImageLoader.displayImage(mActivity, mPhotoIv, photos.get(0), R.drawable.timeline_image_loading,R.drawable.timeline_image_failure, itemWidth * 2, itemWidth * 2);
        } else {
            setVisibility(VISIBLE);
            mPhotoIv.setVisibility(GONE);
            mPhotoGv.setVisibility(VISIBLE);

            ViewGroup.LayoutParams layoutParams = mPhotoGv.getLayoutParams();
            if (photos.size() == 2) {
                mPhotoGv.setNumColumns(2);
                layoutParams.width = itemWidth * 4-Utils.dip2px(getContext(),10*2);
                layoutParams.height = layoutParams.width/2;
            } else if (photos.size() == 4) {
                mPhotoGv.setNumColumns(2);
                layoutParams.width = itemWidth * 4-Utils.dip2px(getContext(),10*2);
                layoutParams.height = layoutParams.width;
            } else {
                mPhotoGv.setNumColumns(3);
                layoutParams.width = itemWidth * 4-Utils.dip2px(getContext(),10*2);
                layoutParams.height = layoutParams.width;
            }
            mPhotoGv.setLayoutParams(layoutParams);
            mPhotoAdapter.setData(photos);
        }
    }


    public ArrayList<String> getData() {
        return (ArrayList<String>) mPhotoAdapter.getData();
    }

    public int getItemCount() {
        return mPhotoAdapter.getCount();
    }

    public String getCurrentClickItem() {
        return (String) mPhotoAdapter.getItem(mCurrentClickItemPosition);
    }

    public int getCurrentClickItemPosition() {
        return mCurrentClickItemPosition;
    }

    public class NinePhotoItemViewHolder extends ViewHolderBase<String> {

        @BindView(R.id.iv_item_nine_photo_photo)
        BGAImageView  ivItemNinePhotoPhoto;
        @BindView(R.id.iv_item_nine_photo_flag)
        ImageView ivItemNinePhotoFlag;
        private int mImageWidth;
        private int mImageHeight;

        @Override
        public View createView(LayoutInflater layoutInflater) {
            View rootView = layoutInflater.inflate(R.layout.bga_pp_item_nine_photo, null);
            ButterKnife.bind(this, rootView);
            mImageWidth = Utils.getDisplayWidthPixels(getContext()) / 6;
            mImageHeight = mImageWidth;
            return rootView;
        }

        @Override
        public void showData(int position, String model) {
            CustomImageLoader.displayImage(mActivity, ivItemNinePhotoPhoto, model, R.drawable.timeline_image_loading,R.drawable.timeline_image_failure, mImageWidth, mImageHeight);
        }
    }

}