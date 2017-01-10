package com.jy.xinlangweibo.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.sinaapi.StatusInteraction;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.StatusBean;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.EmoticonsUtils;
import com.jy.xinlangweibo.utils.TitleBuilder;
import com.jy.xinlangweibo.utils.ToastUtils;
import com.jy.xinlangweibo.utils.WeiboStringUtils;
import com.jy.xinlangweibo.widget.emotionkeyboard.EmoticonsEditText;
import com.jy.xinlangweibo.widget.emotionkeyboard.EmoticonsEditText.OnTextChangedInterface;
import com.jy.xinlangweibo.widget.emotionkeyboard.EmoticonsKeyBoardBar;
import com.jy.xinlangweibo.widget.ninephoto.BGASortableNinePhotoLayout;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

import static com.jy.xinlangweibo.ui.activity.MainActivity.RESULT_UPDATE;

public class WriteStatusActivity extends BaseActivity implements
        OnClickListener, OnTextChangedInterface, BGASortableNinePhotoLayout.Delegate {

    private static final String SHARE_PREFERENCE_NAME = "com.jy.xinlangweibo";
    private static final String SHARE_PREFERENCE_TAG = "soft_input_height";
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;

    private static final int MAX_PHOTO_COUNT = 9;

    private PublishType publishType;
    private ImageView iv_card_status;
    private TextView tv_card_status;

    public enum PublishType {

        // 新微博
        status,
        // 新建评论
        commentCreate,
        // 回复评论
        commentReply,
        // 转发微博
        statusRepost

    }

    @BindView(R.id.snpl_moment_add_photos)
    BGASortableNinePhotoLayout snplMomentAddPhotos;
    @BindView(R.id.et_content)
    EmoticonsEditText et_content;
    @BindView(R.id.rg_card_status)
    RelativeLayout rg_card_status;
    private StatusBean oldStatus;
    private SharedPreferences sp;
    private InputMethodManager imm;
    private ImageView iv_emotions;
    private ImageView iv_add;
    private View iv_picture;
    private View iv_at;
    private View iv_topic;
    private EmoticonsKeyBoardBar EmoticonsKeyBoardBar;
    private TitleBuilder titleBuilder;
    private View mContentView;
    private Handler handler = new Handler();

    public static void intentToUpdate(Activity from ,int requestCode) {
        Intent intent = new Intent(from, WriteStatusActivity.class);
        intent.putExtra("publishType", PublishType.status);
        from.startActivityForResult(intent,requestCode);
    }

    public static void intentToRepost(Activity from , StatusBean status, int requestCode) {
        Intent intent = new Intent(from, WriteStatusActivity.class);
        intent.putExtra("status",status);
        intent.putExtra("publishType", PublishType.statusRepost);
        from.startActivityForResult(intent,requestCode);
    }

    public static void intentToComment(Activity from ,StatusBean status,int requestCode) {
        Intent intent = new Intent(from, WriteStatusActivity.class);
        intent.putExtra("status",status);
        intent.putExtra("publishType", PublishType.commentCreate);
        from.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writestatus);
        ButterKnife.bind(this);

        titleBuilder = new TitleBuilder(this);
        iv_card_status = (ImageView) findViewById(R.id.iv_card_status);
        tv_card_status = (TextView) findViewById(R.id.tv_card_status);
        publishType = (PublishType) getIntent().getSerializableExtra("publishType");
        if(publishType == null) {
            showLog("获取参数错误");
            showToast("获取参数错误");
            finish();
        }
//        showLog("获取publishType==="+publishType.name()+"  "+publishType);
        switch (publishType) {
            case status:
                titleBuilder.setTitle("发表微博");
                et_content.setHint("分享有趣的事情......");
                break;
            case statusRepost:
                titleBuilder.setTitle("转发");
                et_content.setHint("输入内容......");
                rg_card_status.setVisibility(View.GONE);
                // 获得原微博内容
                oldStatus = (StatusBean) getIntent().getSerializableExtra("status");
                break;
            case commentCreate:
                titleBuilder.setTitle("评论");
                et_content.setHint("输入内容......");
                rg_card_status.setVisibility(View.GONE);
                // 获得原微博内容
                oldStatus = (StatusBean) getIntent().getSerializableExtra("status");
                break;
            case commentReply:
        }


        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        sp = this.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        //oncreate 方法中（在这个方法中界面还未显示） 获取软键盘高度？
        int softInputHeight = sp.getInt(SHARE_PREFERENCE_TAG, 400);
        if (softInputHeight == 400) {
            System.out.println("默认值 键盘高度未获取到");
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    getSupportSoftInputHeight();
                }
            }, 200);
        }
        initView();
    }

    private void initView() {
        initTitle();

        initWeiboContent();

        initBottombar();

        initEmotions();
    }

    private void initEmotions() {
        EmoticonsKeyBoardBar = (EmoticonsKeyBoardBar) findViewById(R.id.EmoticonsKeyBoardBar);
        mContentView = findViewById(R.id.content_view);
        // 设置与表情面板匹配的EmoticonsEditText
        EmoticonsKeyBoardBar.setEt_chat(et_content);
        // 从数据库中取出表情属性集（包括路径，对应的字符等）并把该List设置给EmoticonsKeyBoardBar
        EmoticonsKeyBoardBar.setBuilder(EmoticonsUtils.getBuilder(this));
    }

    private void initBottombar() {
        iv_picture = findViewById(R.id.iv_picture);
        iv_at = findViewById(R.id.iv_at);
        iv_topic = findViewById(R.id.iv_topic);
        iv_emotions = (ImageView) findViewById(R.id.iv_emotions);
        iv_add = (ImageView) findViewById(R.id.iv_add);

        iv_picture.setOnClickListener(this);
        iv_at.setOnClickListener(this);
        iv_topic.setOnClickListener(this);
        iv_emotions.setOnClickListener(this);
        iv_add.setOnClickListener(this);
    }

    private void initWeiboContent() {
        snplMomentAddPhotos.setDelegate(this);
        snplMomentAddPhotos.init(this);
        et_content.setOnTextChangedInterface(this);
        et_content.setOnClickListener(this);

        if (oldStatus != null) {
            rg_card_status.setVisibility(View.VISIBLE);
            snplMomentAddPhotos.setVisibility(View.GONE);
            if (TextUtils.isEmpty(oldStatus.thumbnail_pic)) {
                iv_card_status.setVisibility(View.GONE);
            }else {
                CustomImageLoader.displayImage(this,
                        iv_card_status,
                        oldStatus.thumbnail_pic,
                        R.drawable.timeline_image_loading,
                        R.drawable.timeline_image_failure,0,0);
            }
            tv_card_status.setText(WeiboStringUtils.getOnlyImageSpan(this, oldStatus.text,tv_card_status));
        }
    }

    private void initTitle() {
        titleBuilder
        .setLeftimg(R.drawable.icon_navbarback_gra2ora_sel)
        .setrightButton("发送", R.color.bg_agray,
                R.color.bg_gray_pressed).setRightOnclickListener(this)
        .setLeftOnclickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                WriteStatusActivity.this.finish();
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (EmoticonsKeyBoardBar != null && EmoticonsKeyBoardBar.isShown()) {
                    iv_emotions.setImageResource(R.drawable.btn_insert_emotion);
                    EmoticonsKeyBoardBar.setVisibility(View.GONE);
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.iv_picture:

            break;
        case R.id.iv_at:

            break;
        case R.id.iv_topic:

            break;
        case R.id.iv_emotions:
            if (EmoticonsKeyBoardBar.getVisibility() == View.VISIBLE) {
                // 显示表情面板时点击,将按钮图片设为笑脸按钮,同时隐藏面板
                iv_emotions.setImageResource(R.drawable.btn_insert_emotion);
//				EmoticonsKeyBoardBar.setVisibility(View.GONE);
//				getWindow().setSoftInputMode(
//						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//				imm.showSoftInput(et_content, InputMethodManager.SHOW_IMPLICIT);

                lockContentHeight();   //固定内容布局的位置  通过固定布局高度实现（键盘的RESIZE模式只对 内容布局不是固定高度有效？）
                hideEmotionLayout(true);
                unlockContentHeightDelayed();

            } else {
                // 未显示表情面板时点击,将按钮图片设为键盘,同时显示面板
                iv_emotions.setImageResource(R.drawable.btn_insert_keyboard);
//				getWindow().setSoftInputMode(
//						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//				imm.hideSoftInputFromWindow(et_content.getWindowToken(),
//						InputMethodManager.HIDE_NOT_ALWAYS); // 强制隐藏键盘
//				EmoticonsKeyBoardBar.setVisibility(View.VISIBLE);

                if (isSoftInputShown()) {
                    lockContentHeight();
                    showEmotionLayout();
                    unlockContentHeightDelayed();
                } else {
                    showEmotionLayout();
                }

            }
            break;
        case R.id.iv_add:

            break;
        case R.id.et_content:
            if (EmoticonsKeyBoardBar.getVisibility() == View.VISIBLE) {
                EmoticonsKeyBoardBar.setVisibility(View.GONE);
            }
            break;
        case R.id.nav_right_text:
            if (!TextUtils.isEmpty(et_content.getText().toString())) {
                if(snplMomentAddPhotos.getData().size() == 1) {
                    StatusInteraction.getInstance(this).uploadFile(
                            BaseApplication.getInstance().getAccessAccessToken().getToken(),
                            et_content.getText().toString(),
                            snplMomentAddPhotos.getData().get(0));
                }
                else if(snplMomentAddPhotos.getData().size() > 1){
//                       多图暂时只上传第一张图
                    StatusInteraction.getInstance(this).uploadFile(
                            BaseApplication.getInstance().getAccessAccessToken().getToken(),
                            et_content.getText().toString(),
                            snplMomentAddPhotos.getData().get(0));
                }
                else if(snplMomentAddPhotos.getData().size() == 0){
                    switch (publishType) {
                    case status:
                    StatusInteraction.getInstance(this).update(
                    BaseApplication.getInstance().getAccessAccessToken().getToken(),
                    et_content.getText().toString());
                    finishForResult(MainActivity.RESULT_UPDATE);
                    break;
                    case statusRepost:
                    StatusInteraction.getInstance(this).statusesRepost(
                    BaseApplication.getInstance().getAccessAccessToken().getToken(),
                    oldStatus.id,
                    et_content.getText().toString(),
                    new Observer<StatusBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(StatusBean status) {
                            showLog("转发成功======="+status);
                        }
                    });
                    finishForResult(MainActivity.RESULT_REPOST);
                    break;
                    case commentCreate:
                    StatusInteraction.getInstance(this).commentsCreate(
                            BaseApplication.getInstance().getAccessAccessToken().getToken(),
                            oldStatus.id,
                            et_content.getText().toString(),
                            new Observer<StatusBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(StatusBean status) {
                                    showLog("评论成功======="+status);
                                }
                            });
                    finishForResult(MainActivity.RESULT_COMMENT);
                    break;
                    case commentReply:
                        finishForResult(MainActivity.RESULT_COMMENT);
                    }
                    break;
                }
                finishForResult(RESULT_UPDATE);

            } else {
                ToastUtils.show(this, "内容不能为空", Toast.LENGTH_SHORT);
            }
            break;

        }
    }

    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    private void showSoftInput() {
        et_content.requestFocus();
        et_content.post(new Runnable() {
            @Override
            public void run() {
                imm.showSoftInput(et_content, 0);
            }
        });
    }

    private void hideSoftInput() {
        imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
    }


    private void showEmotionLayout() {
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = sp.getInt(SHARE_PREFERENCE_TAG, 400);
        }
        hideSoftInput();
        EmoticonsKeyBoardBar.getLayoutParams().height = softInputHeight;
        EmoticonsKeyBoardBar.setVisibility(View.VISIBLE);
    }

    private void hideEmotionLayout(boolean showSoftInput) {
        if (EmoticonsKeyBoardBar.isShown()) {
            EmoticonsKeyBoardBar.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }


    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }

    private void unlockContentHeightDelayed() {
        mContentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L); //延迟200  留给键盘隐藏的时间  防止表情键盘挤到界面上面去
    }

    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = this.getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }
        if (softInputHeight < 0) {
            Log.w("EmotionInputDetector", "Warning: value of softInputHeight is below zero!");
        }
        if (softInputHeight > 0) {
            sp.edit().putInt(SHARE_PREFERENCE_TAG, softInputHeight).apply();
        }
        return softInputHeight;
    }

    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        this.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }


    @Override
    public void onTextChanged(final CharSequence argo) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (!TextUtils.isEmpty(argo)) {
                    et_content.setTextColor(getResources().getColor(
                            R.color.black));
                    titleBuilder.setrightButton("发送",
                            R.drawable.backgro_tabbarcenter_gra2ora_sel,
                            R.color.white);
                } else {
                    et_content.setTextColor(getResources().getColor(
                            R.color.txt_light_gray));
                    titleBuilder.setrightButton("发送",
                            R.color.bg_agray,
                            R.color.bg_gray_pressed);
                }
            }
        });
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(View view, int position, String model, ArrayList<String> models) {
        snplMomentAddPhotos.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, MAX_PHOTO_COUNT, models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    private void choicePhotoWrapper() {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

        startActivityForResult(BGAPhotoPickerActivity.newIntent(this,takePhotoDir,MAX_PHOTO_COUNT, snplMomentAddPhotos.getData(), true), REQUEST_CODE_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            snplMomentAddPhotos.setData(BGAPhotoPickerActivity.getSelectedImages(data));
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            snplMomentAddPhotos.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
        }
    }
}
