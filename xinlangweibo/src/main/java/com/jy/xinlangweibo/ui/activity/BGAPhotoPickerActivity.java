package com.jy.xinlangweibo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiang.library.ui.adapter.recyleviewadapter.ARecycleViewItemView;
import com.jiang.library.ui.adapter.recyleviewadapter.BasicRecycleViewAdapter;
import com.jiang.library.ui.adapter.recyleviewadapter.IITemView;
import com.jiang.library.ui.adapter.recyleviewadapter.IItemViewCreator;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.bean.BGAImageFolderModel;
import com.jy.xinlangweibo.ui.activity.base.FragmentToolbarActivity;
import com.jy.xinlangweibo.ui.activity.base.ToolbarActivity;
import com.jy.xinlangweibo.ui.fragment.ImageBrowserFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.ToastUtils;
import com.jy.xinlangweibo.utils.Utils;
import com.jy.xinlangweibo.widget.ninephoto.BGAAsyncTask;
import com.jy.xinlangweibo.widget.ninephoto.BGAImageCaptureManager;
import com.jy.xinlangweibo.widget.ninephoto.BGAImageView;
import com.jy.xinlangweibo.widget.ninephoto.BGALoadPhotoTask;
import com.jy.xinlangweibo.widget.ninephoto.BGARVOnScrollListener;
import com.jy.xinlangweibo.widget.ninephoto.BGASpaceItemDecoration;
import com.jy.xinlangweibo.widget.pw.BGAPhotoFolderPw;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/24 下午2:55
 * 描述:图片选择界面
 */
public class BGAPhotoPickerActivity extends ToolbarActivity  implements AdapterView.OnItemClickListener,View.OnClickListener, BGAAsyncTask.Callback<ArrayList<BGAImageFolderModel>> {
    private static final String EXTRA_IMAGE_DIR = "EXTRA_IMAGE_DIR";
    private static final String EXTRA_SELECTED_IMAGES = "EXTRA_SELECTED_IMAGES";
    private static final String EXTRA_MAX_CHOOSE_COUNT = "EXTRA_MAX_CHOOSE_COUNT";
    private static final String EXTRA_PAUSE_ON_SCROLL = "EXTRA_PAUSE_ON_SCROLL";

    /**
     * 拍照的请求码
     */
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    /**
     * 预览照片的请求码
     */
    private static final int REQUEST_CODE_PREVIEW = 2;

    @BindView(R.id.rv_photo_picker_content)
    RecyclerView rvPhotoPickerContent;

    private TextView mTitleTv;
    private ImageView mArrowIv;
    private TextView mSubmitTv;

    private BGAImageFolderModel mCurrentImageFolderModel;

    /**
     * 是否可以拍照
     */
    private boolean mTakePhotoEnabled;
    /**
     * 最多选择多少张图片，默认等于1，为单选
     */
    private int mMaxChooseCount = 1;
    /**
     * 右上角按钮文本
     */
    private String mTopRightBtnText;
    /**
     * 图片目录数据集合
     */
    private ArrayList<BGAImageFolderModel> mImageFolderModels;

    private BGAPhotoPickerAdapter mPicAdapter;

    private BGAImageCaptureManager mImageCaptureManager;

    private BGAPhotoFolderPw mPhotoFolderPw;
    /**
     * 上一次显示图片目录的时间戳，防止短时间内重复点击图片目录菜单时界面错乱
     */
    private long mLastShowPhotoFolderTime;
    private BGALoadPhotoTask mLoadPhotoTask;
    private AppCompatDialog mLoadingDialog;

    /**
     * @param context        应用程序上下文
     * @param imageDir       拍照后图片保存的目录。如果传null表示没有拍照功能，如果不为null则具有拍照功能，
     * @param maxChooseCount 图片选择张数的最大值
     * @param selectedImages 当前已选中的图片路径集合，可以传null
     * @param pauseOnScroll  滚动列表时是否暂停加载图片
     * @return
     */
    public static Intent newIntent(Context context, File imageDir, int maxChooseCount, ArrayList<String> selectedImages, boolean pauseOnScroll) {
        Intent intent = new Intent(context, BGAPhotoPickerActivity.class);
        intent.putExtra(EXTRA_IMAGE_DIR, imageDir);
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, maxChooseCount);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        intent.putExtra(EXTRA_PAUSE_ON_SCROLL, pauseOnScroll);
        return intent;
    }

    /**
     * 获取已选择的图片集合
     *
     * @param intent
     * @return
     */
    public static ArrayList<String> getSelectedImages(Intent intent) {
        return intent.getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bga_pp_activity_photo_picker);
        ButterKnife.bind(this);
        mPicAdapter = new BGAPhotoPickerAdapter(new IItemViewCreator<String>() {
            @Override
            public View newContentView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
                return layoutInflater.inflate(R.layout.bga_pp_item_photo_picker, parent,false);
            }

            @Override
            public IITemView<String> newItemView(View convertView, int viewType) {
                return  mPicAdapter.new PhotoPickerItem(BGAPhotoPickerActivity.this,convertView);
            }
        }, null);
        mPicAdapter.setOnItemClickListener(this);
        if (getIntent().getBooleanExtra(EXTRA_PAUSE_ON_SCROLL, false)) {
            rvPhotoPickerContent.addOnScrollListener(new BGARVOnScrollListener(this));
        }

        processLogic(savedInstanceState);
    }

    protected void processLogic(Bundle savedInstanceState) {
        // 获取拍照图片保存目录
        File imageDir = (File) getIntent().getSerializableExtra(EXTRA_IMAGE_DIR);
        if (imageDir != null) {
            mTakePhotoEnabled = true;
            mImageCaptureManager = new BGAImageCaptureManager(this, imageDir);
        }
        // 获取图片选择的最大张数
        mMaxChooseCount = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        if (mMaxChooseCount < 1) {
            mMaxChooseCount = 1;
        }

        // 获取右上角按钮文本
        mTopRightBtnText = getString(R.string.bga_pp_confirm);

        GridLayoutManager layoutManager = new GridLayoutManager(this, BGASpaceItemDecoration.SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
        rvPhotoPickerContent.setLayoutManager(layoutManager);
        rvPhotoPickerContent.addItemDecoration(new BGASpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.bga_pp_size_photo_divider)));

        ArrayList<String> selectedImages = getIntent().getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
        if (selectedImages != null && selectedImages.size() > mMaxChooseCount) {
            String selectedPhoto = selectedImages.get(0);
            selectedImages.clear();
            selectedImages.add(selectedPhoto);
        }

        rvPhotoPickerContent.setAdapter(mPicAdapter);
        mPicAdapter.setSelectedImages(selectedImages);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLoadPhotoTask = new BGALoadPhotoTask(this, this, mTakePhotoEnabled).perform();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bga_pp_menu_photo_picker, menu);
        MenuItem menuItem = menu.findItem(R.id.item_photo_picker_title);
        View actionView = menuItem.getActionView();

        mTitleTv = (TextView) actionView.findViewById(R.id.tv_photo_picker_title);
        mArrowIv = (ImageView) actionView.findViewById(R.id.iv_photo_picker_arrow);
        mSubmitTv = (TextView) actionView.findViewById(R.id.tv_photo_picker_submit);

        mTitleTv.setOnClickListener(this);
        mArrowIv.setOnClickListener(this);
        mSubmitTv.setOnClickListener(this);

        mTitleTv.setText(R.string.bga_pp_all_image);
        if (mCurrentImageFolderModel != null) {
            mTitleTv.setText(mCurrentImageFolderModel.name);
        }

        renderTopRightBtn();

        return true;
    }

    @Override
    public void onClick(View v) {
        if ((v.getId() == R.id.tv_photo_picker_title || v.getId() == R.id.iv_photo_picker_arrow) && mImageFolderModels != null && mImageFolderModels.size() > 0 && System.currentTimeMillis() - mLastShowPhotoFolderTime > BGAPhotoFolderPw.ANIM_DURATION) {
            showPhotoFolderPw();
            mLastShowPhotoFolderTime = System.currentTimeMillis();
        } else if (v.getId() == R.id.tv_photo_picker_submit) {
            returnSelectedImages(mPicAdapter.getSelectedImages());
        }
    }

    /**
     * 返回已选中的图片集合
     *
     * @param selectedImages
     */
    private void returnSelectedImages(ArrayList<String> selectedImages) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showPhotoFolderPw() {
        if (mPhotoFolderPw == null) {
            mPhotoFolderPw = new BGAPhotoFolderPw(this, mToolbar, new BGAPhotoFolderPw.Delegate() {
                @Override
                public void onSelectedFolder(int position) {
                    reloadPhotos(position);
                }

                @Override
                public void executeDismissAnim() {
                    ViewCompat.animate(mArrowIv).setDuration(BGAPhotoFolderPw.ANIM_DURATION).rotation(0).start();
                }
            });
        }
        mPhotoFolderPw.setData(mImageFolderModels);
        mPhotoFolderPw.show();

        ViewCompat.animate(mArrowIv).setDuration(BGAPhotoFolderPw.ANIM_DURATION).rotation(-180).start();
    }

    /**
     * 显示只能选择 mMaxChooseCount 张图的提示
     */
    private void toastMaxCountTip() {
        ToastUtils.show(this, getString(R.string.bga_pp_toast_photo_picker_max, mMaxChooseCount), Toast.LENGTH_SHORT);
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        try {
            startActivityForResult(mImageCaptureManager.getTakePictureIntent(), REQUEST_CODE_TAKE_PHOTO);
        } catch (Exception e) {
            ToastUtils.show(this, getString(R.string.bga_pp_photo_not_support), Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                ArrayList<String> photos = new ArrayList<>();
                photos.add(mImageCaptureManager.getCurrentPhotoPath());
                startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, 1, photos, photos, 0, true), REQUEST_CODE_PREVIEW);
            } else if (requestCode == REQUEST_CODE_PREVIEW) {
                if (BGAPhotoPickerPreviewActivity.getIsFromTakePhoto(data)) {
                    // 从拍照预览界面返回，刷新图库
                    mImageCaptureManager.refreshGallery();
                }

                returnSelectedImages(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == REQUEST_CODE_PREVIEW) {
            if (BGAPhotoPickerPreviewActivity.getIsFromTakePhoto(data)) {
                // 从拍照预览界面返回，删除之前拍的照片
                mImageCaptureManager.deletePhotoFile();
            } else {
                mPicAdapter.setSelectedImages(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
                renderTopRightBtn();
            }
        }
    }

    /**
     * 渲染右上角按钮
     */
    private void renderTopRightBtn() {
        if (mPicAdapter.getSelectedCount() == 0) {
            mSubmitTv.setEnabled(false);
            mSubmitTv.setText(mTopRightBtnText);
        } else {
            mSubmitTv.setEnabled(true);
            mSubmitTv.setText(mTopRightBtnText + "(" + mPicAdapter.getSelectedCount() + "/" + mMaxChooseCount + ")");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mTakePhotoEnabled) {
            mImageCaptureManager.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mTakePhotoEnabled) {
            mImageCaptureManager.onRestoreInstanceState(savedInstanceState);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 处理点击选择按钮事件
     *
     * @param position 当前点击的item的索引位置
     */
    private void handleClickSelectFlagIv(int position) {
        String currentImage = mPicAdapter.getItem(position);
        if (mMaxChooseCount == 1) {
            // 单选

            if (mPicAdapter.getSelectedCount() > 0) {
                String selectedImage = mPicAdapter.getSelectedImages().remove(0);
                if (TextUtils.equals(selectedImage, currentImage)) {
                    mPicAdapter.notifyItemChanged(position);
                } else {
                    int preSelectedImagePosition = mPicAdapter.getDatas().indexOf(selectedImage);
                    mPicAdapter.notifyItemChanged(preSelectedImagePosition);
                    mPicAdapter.getSelectedImages().add(currentImage);
                    mPicAdapter.notifyItemChanged(position);
                }
            } else {
                mPicAdapter.getSelectedImages().add(currentImage);
                mPicAdapter.notifyItemChanged(position);
            }
            renderTopRightBtn();
        } else {
            // 多选

            if (!mPicAdapter.getSelectedImages().contains(currentImage) && mPicAdapter.getSelectedCount() == mMaxChooseCount) {
                toastMaxCountTip();
            } else {
                if (mPicAdapter.getSelectedImages().contains(currentImage)) {
                    mPicAdapter.getSelectedImages().remove(currentImage);
                } else {
                    mPicAdapter.getSelectedImages().add(currentImage);
                }
                mPicAdapter.notifyItemChanged(position);

                renderTopRightBtn();
            }
        }
    }

    /**
     * 处理点击预览按钮事件
     *
     * @param position 当前点击的item的索引位置
     */
    private void handleClickPreviewIv(int position) {
        if (mMaxChooseCount == 1) {
            // 单选

            if (mCurrentImageFolderModel.isTakePhotoEnabled() && position == 0) {
                takePhoto();
            } else {
                changeToPreview(position);
            }
        } else {
            // 多选

            if (mCurrentImageFolderModel.isTakePhotoEnabled() && position == 0) {
                if (mPicAdapter.getSelectedCount() == mMaxChooseCount) {
                    toastMaxCountTip();
                } else {
                    takePhoto();
                }
            } else {
                changeToPreview(position);
            }
        }
    }

    /**
     * 跳转到图片选择预览界面
     *
     * @param position 当前点击的item的索引位置
     */
    private void changeToPreview(int position) {
        int currentPosition = position;
        if (mCurrentImageFolderModel.isTakePhotoEnabled()) {
            currentPosition--;
        }
//        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mMaxChooseCount, mPicAdapter.getSelectedImages(), (ArrayList<String>) mPicAdapter.getDatas(), currentPosition, false), REQUEST_CODE_PREVIEW);

        //					前往图片浏览器
        Intent intent = new Intent(this, FragmentToolbarActivity.class);
        intent.putExtra("Pic_urls", (ArrayList)mPicAdapter.getDatas());
        intent.putExtra("Position", position);
        FragmentToolbarActivity.launch(this, ImageBrowserFragment.class,intent,false);
    }

    private void reloadPhotos(int position) {
        if (position < mImageFolderModels.size()) {
            mCurrentImageFolderModel = mImageFolderModels.get(position);
            if (mTitleTv != null) {
                mTitleTv.setText(mCurrentImageFolderModel.name);
            }

            mPicAdapter.setImageFolderModel(mCurrentImageFolderModel);
        }
    }

    @Override
    public void onPostExecute(ArrayList<BGAImageFolderModel> imageFolderModels) {
        mLoadPhotoTask = null;
        mImageFolderModels = imageFolderModels;
        reloadPhotos(mPhotoFolderPw == null ? 0 : mPhotoFolderPw.getCurrentPosition());
    }

    @Override
    public void onTaskCancelled() {
        mLoadPhotoTask = null;
    }

    private void cancelLoadPhotoTask() {
        if (mLoadPhotoTask != null) {
            mLoadPhotoTask.cancelTask();
            mLoadPhotoTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        cancelLoadPhotoTask();

        mTitleTv = null;
        mArrowIv = null;
        mSubmitTv = null;
        rvPhotoPickerContent = null;
        mCurrentImageFolderModel = null;
        mTopRightBtnText = null;
        mImageFolderModels = null;
        mPicAdapter = null;
        mImageCaptureManager = null;
        mPhotoFolderPw = null;

        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        handleClickPreviewIv(position);
    }

    public  class BGAPhotoPickerAdapter extends BasicRecycleViewAdapter<String> {

        private ArrayList<String> mSelectedImages = new ArrayList<>();
        private boolean mTakePhotoEnabled;

        public boolean ismTakePhotoEnabled() {
            return mTakePhotoEnabled;
        }

        public BGAPhotoPickerAdapter(IItemViewCreator<String> itemViewCreator, ArrayList<String> datas) {
            super(itemViewCreator, datas);
        }

        public void setSelectedImages(ArrayList<String> selectedImages) {
            if (selectedImages != null) {
                mSelectedImages = selectedImages;
            }
            notifyDataSetChanged();
        }

        public ArrayList<String> getSelectedImages() {
            return mSelectedImages;
        }

        public int getSelectedCount() {
            return mSelectedImages.size();
        }

        public void setImageFolderModel(BGAImageFolderModel imageFolderModel) {
            mTakePhotoEnabled = imageFolderModel.isTakePhotoEnabled();
            setDatas(imageFolderModel.getImages());
        }

        public class PhotoPickerItem extends ARecycleViewItemView<String> implements View.OnClickListener {
            @BindView(R.id.iv_item_photo_picker_photo)
            BGAImageView ivItemPhotoPickerPhoto;
            @BindView(R.id.tv_item_photo_picker_tip)
            TextView tvItemPhotoPickerTip;
            @BindView(R.id.iv_item_photo_picker_flag)
            ImageView ivItemPhotoPickerFlag;
            private int mImageWidth;
            private int mImageHeight;


            public PhotoPickerItem(Context context, View itemView) {
                super(context, itemView);
                mImageWidth = Utils.getDisplayWidthPixels(BGAPhotoPickerActivity.this) / 6;
                mImageHeight = mImageWidth;
            }

            @Override
            public void onBindData(View convertView, String model, int position) {
                if (mTakePhotoEnabled && position == 0) {
                    tvItemPhotoPickerTip.setVisibility(View.VISIBLE);
                    ivItemPhotoPickerPhoto.setScaleType(ImageView.ScaleType.CENTER);
                    ivItemPhotoPickerPhoto.setImageResource(R.drawable.bga_pp_ic_gallery_camera);

                    ivItemPhotoPickerFlag.setVisibility(View.VISIBLE);
                    ivItemPhotoPickerPhoto.setColorFilter(null);
                } else {
                    tvItemPhotoPickerTip.setVisibility(View.INVISIBLE);
                    ivItemPhotoPickerPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    CustomImageLoader.displayImage(BGAPhotoPickerActivity.this, ivItemPhotoPickerPhoto, model, R.drawable.bga_pp_ic_holder_dark, R.drawable.bga_pp_ic_holder_dark, mImageWidth, mImageHeight);

                    ivItemPhotoPickerFlag.setVisibility(View.VISIBLE);

                    if (mSelectedImages.contains(model)) {
                        ivItemPhotoPickerFlag.setImageResource(R.drawable.bga_pp_ic_cb_checked);
                        ivItemPhotoPickerPhoto.setColorFilter(convertView.getResources().getColor(R.color.bga_pp_photo_selected_mask));
                    } else {
                        ivItemPhotoPickerFlag.setImageResource(R.drawable.bga_pp_ic_cb_normal);
                        ivItemPhotoPickerPhoto.setColorFilter(null);
                    }
                }
                ivItemPhotoPickerFlag.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.iv_item_photo_picker_flag) {
                    handleClickSelectFlagIv(this.getAdapterPosition());
                }
            }

        }
    }
}