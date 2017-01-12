package com.jy.xinlangweibo.ui.fragment.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.cache.LocaleHistory;
import com.jy.xinlangweibo.models.net.sinaapi.BaseObserver;
import com.jy.xinlangweibo.models.net.sinaapi.StatusInteraction;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.UserBean;
import com.jy.xinlangweibo.models.net.videoapi.RetrofitHelper;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.GankHttpResponse;
import com.jy.xinlangweibo.models.net.videoapi.videobean.GankItemBean;
import com.jy.xinlangweibo.ui.activity.AboutActivity;
import com.jy.xinlangweibo.ui.activity.LoginActivity;
import com.jy.xinlangweibo.ui.activity.MainActivity;
import com.jy.xinlangweibo.ui.activity.UserShowActivity;
import com.jy.xinlangweibo.ui.activity.WriteStatusActivity;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.activity.base.FragmentToolbarActivity;
import com.jy.xinlangweibo.ui.adapter.VideoHistoryAdapter;
import com.jy.xinlangweibo.ui.fragment.base.BaseSupportFragment;
import com.jy.xinlangweibo.ui.fragment.setting.SettingFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.RxBus;
import com.jy.xinlangweibo.widget.CircleImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by JIANG on 2016/12/17.
 */

public class PersonalFragment extends BaseSupportFragment {

    @BindView(R.id.bangumi_bg)
    ImageView bangumiBg;
    @BindView(R.id.bangumi_pic)
    CircleImageView bangumiPic;
    @BindView(R.id.tv_screen_name)
    TextView tvScreenName;
    @BindView(R.id.tv_created_at)
    TextView tvCreatedAt;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.ll_video_history)
    LinearLayout llVideoHistory;
    @BindView(R.id.hlvSimpleList)
    RecyclerView hlvSimpleList;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.rl_out1)
    RelativeLayout rlOut;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.feed_back)
    RelativeLayout feedBack;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((MainActivity) activity).getToolbar().setVisibility(View.VISIBLE);
            ((MainActivity) activity).getNavTitle().setText("个人中心");
            List<ChildListBean> playHistory = LocaleHistory.getInstance().getVideoPlayHistory();
            updateVideoHistoryUI(playHistory);
        }
    }

    @Override
    protected int CreateView() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initViewAndEvent(View rootView) {
        super.initViewAndEvent(rootView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hlvSimpleList.setLayoutManager(layoutManager);
        StatusInteraction.getInstance(getActivity()).userShow(((BaseActivity) getActivity()).getAccessAccessToken().getToken(),
                new BaseObserver<UserBean>() {
                    @Override
                    public void onNext(UserBean userBean) {
                        super.onNext(userBean);
                        updateUI(userBean);
                    }
                });
        RetrofitHelper.getGankApis().getGirlList(0, 0).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GankHttpResponse<List<GankItemBean>>>() {
                    @Override
                    public void call(GankHttpResponse<List<GankItemBean>> listGankHttpResponse) {
                        CustomImageLoader.displayImage(getActivity(),
                                bangumiBg,
                                listGankHttpResponse.getResults().get(BaseApplication.getInstance().random.nextInt(50)).getUrl(),
                                R.drawable.timeline_image_loading,
                                R.drawable.timeline_image_failure,
                                0, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.showLog(throwable.getMessage(), "personfragment");
                    }
                });

        RxBus.getInstance().toObserverable(MainActivity.OuathResultEvnet.class).subscribe(new Action1<MainActivity.OuathResultEvnet>() {
            @Override
            public void call(MainActivity.OuathResultEvnet ouathResult) {
                StatusInteraction.getInstance(getActivity()).userShow(((BaseActivity) getActivity()).getAccessAccessToken().getToken(),
                        new BaseObserver<UserBean>() {
                            @Override
                            public void onNext(UserBean userBean) {
                                super.onNext(userBean);
                                updateUI(userBean);
                            }
                        });
            }
        });
    }

    private void updateVideoHistoryUI(List<ChildListBean> playHistory) {
        if (playHistory == null || playHistory.size() == 0) {
            hlvSimpleList.setVisibility(View.GONE);
        } else {
            hlvSimpleList.setVisibility(View.VISIBLE);
        }
        hlvSimpleList.setAdapter(new VideoHistoryAdapter(playHistory));
    }

    private void updateUI(final UserBean model) {
        CustomImageLoader.displayImage(getActivity(),
                bangumiPic,
                model.getAvatar_large(),
                R.drawable.avatar_default,
                R.drawable.avatar_default,
                0, 0);
        tvScreenName.setText(model.getScreen_name());
        tvDescription.setText(model.getDescription());
        tvCreatedAt.setText("注册时间：" + DateUtils.formatDate(model.getCreated_at()));
        bangumiPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserShowActivity.launch(getActivity(), model.screen_name);
            }
        });

    }

    @OnClick({R.id.rl_setting, R.id.iv_delete, R.id.bangumi_pic, R.id.rl_about, R.id.feed_back, R.id.rl_out1})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_setting:

                FragmentToolbarActivity.launch(getActivity(), SettingFragment.class);
                break;
            case R.id.iv_delete:

                LocaleHistory.getInstance().clearVideoPlayHistory();
                hlvSimpleList.setVisibility(View.GONE);
                break;
            case R.id.bangumi_pic:

                if (!activity.getAccessAccessToken().isSessionValid()) {
                    activity.startActivityForResult(LoginActivity.class, MainActivity.OAUTH_REQUEST);
                }
                break;
            case R.id.rl_about:

                intent2Activity(AboutActivity.class);
                break;
            case R.id.feed_back:

                if(!activity.getAccessAccessToken().isSessionValid()) {
                    showToast("需要登录微博哟，试试重新登陆吧0.0");
                    return;
                }
                WriteStatusActivity.intentToFeedBack(getActivity());
                break;
            case R.id.rl_out1:

                MobclickAgent.onKillProcess(activity);
                getActivity().finish();
                System.exit(0);
                break;
        }

    }

}
