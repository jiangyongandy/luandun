package com.jy.xinlangweibo.widget.pw;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiang.library.ui.adapter.recyleviewadapter.ARecycleViewItemView;
import com.jiang.library.ui.adapter.recyleviewadapter.BasicRecycleViewAdapter;
import com.jiang.library.ui.adapter.recyleviewadapter.IITemView;
import com.jiang.library.ui.adapter.recyleviewadapter.IItemViewCreator;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.bean.BGAImageFolderModel;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.Utils;
import com.jy.xinlangweibo.widget.ninephoto.BGAImageView;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/24 下午6:19
 * 描述:选择图片目录的PopupWindow
 */
public class BGAPhotoFolderPw extends BGABasePopupWindow implements AdapterView.OnItemClickListener {
    public static final int ANIM_DURATION = 300;
    private LinearLayout mRootLl;
    private RecyclerView mContentRv;
    private BasicRecycleViewAdapter mFolderAdapter;
    private Delegate mDelegate;
    private int mCurrentPosition;

    public BGAPhotoFolderPw(Activity activity, View anchorView, Delegate delegate) {
        super(activity, R.layout.bga_pp_pw_photo_folder, anchorView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mDelegate = delegate;
    }

    @Override
    protected void initView() {
        mRootLl = getViewById(R.id.ll_photo_folder_root);
        mContentRv = getViewById(R.id.rv_photo_folder_content);
    }

    @Override
    protected void setListener() {
        mRootLl.setOnClickListener(this);
        mFolderAdapter = new BasicRecycleViewAdapter<BGAImageFolderModel>(new IItemViewCreator<BGAImageFolderModel>() {
            @Override
            public View newContentView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
                return layoutInflater.inflate(R.layout.bga_pp_item_photo_folder, parent,false);
            }

            @Override
            public IITemView<BGAImageFolderModel> newItemView(View convertView, int viewType) {
                return new FolderPwItem(convertView.getContext(),convertView);
            }
        }, null);
        mFolderAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void processLogic() {
        setAnimationStyle(android.R.style.Animation);
        setBackgroundDrawable(new ColorDrawable(0x90000000));

        mContentRv.setLayoutManager(new LinearLayoutManager(mActivity));
        mContentRv.setAdapter(mFolderAdapter);
    }

    /**
     * 设置目录数据集合
     *
     * @param datas
     */
    public void setData(ArrayList<BGAImageFolderModel> datas) {
        mFolderAdapter.setDatas(datas);
    }

    @Override
    public void show() {
        showAsDropDown(mAnchorView);
        ViewCompat.animate(mContentRv).translationY(-mWindowRootView.getHeight()).setDuration(0).start();
        ViewCompat.animate(mContentRv).translationY(0).setDuration(ANIM_DURATION).start();
        ViewCompat.animate(mRootLl).alpha(0).setDuration(0).start();
        ViewCompat.animate(mRootLl).alpha(1).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_photo_folder_root) {
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        ViewCompat.animate(mContentRv).translationY(-mWindowRootView.getHeight()).setDuration(ANIM_DURATION).start();
        ViewCompat.animate(mRootLl).alpha(1).setDuration(0).start();
        ViewCompat.animate(mRootLl).alpha(0).setDuration(ANIM_DURATION).start();

        if (mDelegate != null) {
            mDelegate.executeDismissAnim();
        }

        mContentRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                BGAPhotoFolderPw.super.dismiss();
            }
        }, ANIM_DURATION);
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mDelegate != null && mCurrentPosition != position) {
            mDelegate.onSelectedFolder(position);
        }
        mCurrentPosition = position;
        dismiss();
    }

    public class FolderPwItem extends ARecycleViewItemView<BGAImageFolderModel> {
        @BindView(R.id.iv_item_photo_folder_photo)
        BGAImageView  ivItemPhotoFolderPhoto;
        @BindView(R.id.tv_item_photo_folder_name)
        TextView tvItemPhotoFolderName;
        @BindView(R.id.tv_item_photo_folder_count)
        TextView tvItemPhotoFolderCount;
        private int mImageWidth;
        private int mImageHeight;

        public FolderPwItem(Context context, View itemView) {
            super(context, itemView);
            mImageWidth = Utils.getDisplayWidthPixels(mActivity) / 10;
            mImageHeight = mImageWidth;
        }

        @Override
        public void onBindData(View convertView, BGAImageFolderModel model, int position) {
            tvItemPhotoFolderName.setText(model.name);
            tvItemPhotoFolderCount.setText(String.valueOf(model.getCount()));
            CustomImageLoader.displayImage(mActivity, ivItemPhotoFolderPhoto, model.coverPath, R.drawable.bga_pp_ic_holder_light, R.drawable.bga_pp_ic_holder_light, mImageWidth, mImageHeight);
        }
    }

    public interface Delegate {
        void onSelectedFolder(int position);

        void executeDismissAnim();
    }
}