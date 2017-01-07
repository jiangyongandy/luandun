package com.jy.xinlangweibo.widget.ninephoto;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.jiang.library.ui.adapter.recyleviewadapter.ARecycleViewItemView;
import com.jiang.library.ui.adapter.recyleviewadapter.BasicRecycleViewAdapter;
import com.jiang.library.ui.adapter.recyleviewadapter.IITemView;
import com.jiang.library.ui.adapter.recyleviewadapter.IItemViewCreator;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/8 下午4:51
 * 描述:
 */
public class BGASortableNinePhotoLayout extends RecyclerView implements AdapterView.OnItemClickListener {
    public static final int MAX_ITEM_COUNT = 9;
    private static final int MAX_SPAN_COUNT = 3;

    private PhotoAdapter<String> mPhotoAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private Delegate mDelegate;
    private GridLayoutManager mGridLayoutManager;
    private boolean mIsPlusSwitchOpened = true;
    private boolean mIsSortable = true;
    private Activity mActivity;

    public PhotoAdapter<String> getmPhotoAdapter() {
        return mPhotoAdapter;
    }

    public BGASortableNinePhotoLayout(Context context) {
        this(context, null);
    }

    public BGASortableNinePhotoLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BGASortableNinePhotoLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOverScrollMode(OVER_SCROLL_NEVER);

        initAttrs(context, attrs);

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback());
        mItemTouchHelper.attachToRecyclerView(this);

        mGridLayoutManager = new GridLayoutManager(context, MAX_SPAN_COUNT);
        setLayoutManager(mGridLayoutManager);
        addItemDecoration(new BGASpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.bga_pp_size_photo_divider)));

        mPhotoAdapter = new PhotoAdapter<String>(new IItemViewCreator<String>() {
            @Override
            public View newContentView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
                return layoutInflater.inflate(R.layout.bga_pp_item_nine_photo, parent,false);
            }

            @Override
            public IITemView<String> newItemView(View convertView, int viewType) {
                return mPhotoAdapter.new SortableNinePhotoItem<String>(convertView.getContext(),convertView);
            }
        }, null);
        mPhotoAdapter.setOnItemClickListener(this);
        setAdapter(mPhotoAdapter);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGASortableNinePhotoLayout);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BGASortableNinePhotoLayout_bga_snpl_isPlusSwitchOpened) {
            mIsPlusSwitchOpened = typedArray.getBoolean(attr, mIsPlusSwitchOpened);
        } else if (attr == R.styleable.BGASortableNinePhotoLayout_bga_snpl_isSortable) {
            mIsSortable = typedArray.getBoolean(attr, mIsSortable);
        }
    }

    public void init(Activity activity) {
        mActivity = activity;
    }

    /**
     * 设置是否可拖拽排序
     *
     * @param isSortable
     */
    public void setIsSortable(boolean isSortable) {
        mIsSortable = isSortable;
    }

    /**
     * 设置图片路径数据集合
     *
     * @param photos
     */
    public void setData(ArrayList<String> photos) {
        if (mActivity == null) {
            throw new RuntimeException("请先调用init方法进行初始化");
        }

        mPhotoAdapter.setDatas(photos);
        updateHeight();
    }

    private void updateHeight() {
        if (mPhotoAdapter.getItemCount() > 0 && mPhotoAdapter.getItemCount() < MAX_SPAN_COUNT) {
            mGridLayoutManager.setSpanCount(mPhotoAdapter.getItemCount());
        } else {
            mGridLayoutManager.setSpanCount(MAX_SPAN_COUNT);
        }
        int itemWidth = Utils.getDisplayWidthPixels(getContext()) / (MAX_SPAN_COUNT );
        int width = itemWidth * mGridLayoutManager.getSpanCount();
        int height = 0;
        if (mPhotoAdapter.getItemCount() != 0) {
            int rowCount = (mPhotoAdapter.getItemCount() - 1) / mGridLayoutManager.getSpanCount() + 1;
            height = itemWidth * rowCount;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }

    /**
     * 获取图片路劲数据集合
     *
     * @return
     */
    public ArrayList<String> getData() {
        return (ArrayList<String>) mPhotoAdapter.getDatas();
    }

    /**
     * 删除指定索引位置的图片
     *
     * @param position
     */
    public void removeItem(int position) {
        mPhotoAdapter.removeItem(position);
        updateHeight();
    }

    /**
     * 获取图片总数
     *
     * @return
     */
    public int getItemCount() {
        return mPhotoAdapter.getRealItemCount();
    }


    public void setIsPlusSwitchOpened(boolean isPlusSwitchOpened) {
        mIsPlusSwitchOpened = isPlusSwitchOpened;
        updateHeight();
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mPhotoAdapter.isPlusItem(position)) {
            if (mDelegate != null) {
                mDelegate.onClickAddNinePhotoItem(this, view, position, (ArrayList<String>) mPhotoAdapter.getDatas());
            }
        } else {
            if (mDelegate != null && ViewCompat.getScaleX(view) <= 1.0f) {
                mDelegate.onClickNinePhotoItem(this, view, position, mPhotoAdapter.getItem(position), (ArrayList<String>) mPhotoAdapter.getDatas());
            }
        }
    }

    public class PhotoAdapter<Y extends String> extends BasicRecycleViewAdapter<Y> {

        public PhotoAdapter(IItemViewCreator<Y> itemViewCreator, ArrayList<Y> datas) {
            super(itemViewCreator, datas);
        }

        public boolean isPlusItem(int position) {
            return mIsPlusSwitchOpened && super.getItemCount() < MAX_ITEM_COUNT && position == getItemCount() - 1;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            if (mIsPlusSwitchOpened && super.getItemCount() < MAX_ITEM_COUNT) {
                return super.getItemCount() + 1;
            }

            return super.getItemCount();
        }

        @Override
        public Y getItem(int position) {
            if (isPlusItem(position)) {
                return null;
            }

            return super.getItem(position);
        }

        public class SortableNinePhotoItem<T extends String> extends ARecycleViewItemView<T> {
            @BindView(R.id.iv_item_nine_photo_photo)
            BGAImageView ivItemNinePhotoPhoto;
            @BindView(R.id.iv_item_nine_photo_flag)
            ImageView ivItemNinePhotoFlag;
            private int mImageWidth;
            private int mImageHeight;

            public SortableNinePhotoItem(Context context, View itemView) {
                super(context, itemView);
                mImageWidth = Utils.getDisplayWidthPixels(BGASortableNinePhotoLayout.this.getContext()) / 6;
                mImageHeight = mImageWidth;
            }

            @Override
            public void onBindData(View convertView, final T model, final int position) {
                if (isPlusItem(position)) {
                    ivItemNinePhotoFlag.setVisibility(View.GONE);
                    ivItemNinePhotoPhoto.setImageResource(R.drawable.bga_pp_ic_plus);
                }else {
                    ivItemNinePhotoFlag.setVisibility(View.VISIBLE);
                    CustomImageLoader.displayImage(mActivity, ivItemNinePhotoPhoto, model, R.drawable.bga_pp_ic_holder_light, R.drawable.bga_pp_ic_holder_light, mImageWidth, mImageHeight);
                }
                ivItemNinePhotoFlag.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDelegate != null) {
                            mDelegate.onClickDeleteNinePhotoItem( v, SortableNinePhotoItem.this.getAdapterPosition(),model, (ArrayList<String>) mPhotoAdapter.getDatas());
                        }
                    }
                });
            }
        }
    }


    private class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

        @Override
        public boolean isLongPressDragEnabled() {
            return mIsSortable && mPhotoAdapter.getDatas().size() > 1;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            if (mPhotoAdapter.isPlusItem(viewHolder.getAdapterPosition())) {
                return ItemTouchHelper.ACTION_STATE_IDLE;
            }

            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
            int swipeFlags = dragFlags;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder source, ViewHolder target) {
            if (source.getItemViewType() != target.getItemViewType() || mPhotoAdapter.isPlusItem(target.getAdapterPosition())) {
                return false;
            }
            mPhotoAdapter.moveItem(source.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                ViewCompat.setScaleX(viewHolder.itemView, 1.2f);
                ViewCompat.setScaleY(viewHolder.itemView, 1.2f);
                ((PhotoAdapter.SortableNinePhotoItem ) viewHolder).ivItemNinePhotoPhoto.setColorFilter(getResources().getColor(R.color.bga_pp_photo_selected_mask));
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
            ViewCompat.setScaleX(viewHolder.itemView, 1.0f);
            ViewCompat.setScaleY(viewHolder.itemView, 1.0f);
            ((PhotoAdapter.SortableNinePhotoItem ) viewHolder).ivItemNinePhotoPhoto.setColorFilter(null);
            super.clearView(recyclerView, viewHolder);
        }
    }

    public interface Delegate {
        void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models);

        void onClickDeleteNinePhotoItem(View view, int position, String model, ArrayList<String> models);

        void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models);
    }
}