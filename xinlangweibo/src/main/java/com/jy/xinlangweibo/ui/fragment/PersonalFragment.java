package com.jy.xinlangweibo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.sinaapi.BaseObserver;
import com.jy.xinlangweibo.models.net.sinaapi.StatusInteraction;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.UserBean;
import com.jy.xinlangweibo.models.net.videoapi.RetrofitHelper;
import com.jy.xinlangweibo.models.net.videoapi.videobean.GankHttpResponse;
import com.jy.xinlangweibo.models.net.videoapi.videobean.GankItemBean;
import com.jy.xinlangweibo.ui.activity.MainActivity;
import com.jy.xinlangweibo.ui.activity.UserShowActivity;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.activity.base.FragmentToolbarActivity;
import com.jy.xinlangweibo.ui.fragment.base.BaseFragment;
import com.jy.xinlangweibo.ui.fragment.setting.SettingFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by JIANG on 2016/12/17.
 */

public class PersonalFragment extends BaseFragment {
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            ((MainActivity)activity).getToolbar().setVisibility(View.VISIBLE);
            ((MainActivity) activity).getNavTitle().setText("个人中心");
        }
    }

    @Override
    protected int CreateView() {
        return R.layout.fragment_personal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initViewAndEvent(View rootView) {
        super.initViewAndEvent(rootView);
        StatusInteraction.getInstance(getActivity()).userShow(((BaseActivity) getActivity()).getAccessAccessToken().getToken(),
                new BaseObserver<UserBean>() {
                    @Override
                    public void onNext(UserBean userBean) {
                        super.onNext(userBean);
                        updateUI(userBean);
                    }
                });
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
        RetrofitHelper.getGankApis().getGirlList(0,0).subscribeOn(Schedulers.io())
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
                        Logger.showLog(throwable.getMessage(),"personfragment");
                    }
                });
    }

    @OnClick(R.id.rl_setting)
    public void onClick() {
        FragmentToolbarActivity.launch(getActivity(), SettingFragment.class);
    }

}
