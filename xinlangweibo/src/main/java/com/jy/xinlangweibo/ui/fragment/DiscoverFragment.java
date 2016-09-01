package com.jy.xinlangweibo.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.api.SimpleRequestlistener;
import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.interaction.StatusesInteraction;
import com.jy.xinlangweibo.interaction.impl.StatusesInteractionImpl;
import com.jy.xinlangweibo.ui.activity.MainActivity;
import com.jy.xinlangweibo.ui.adapter.StaggeredHomeAdapter;
import com.jy.xinlangweibo.ui.adapter.StaggeredHomeAdapter.OnItemClickLitener;
import com.jy.xinlangweibo.ui.fragment.base.BaseCacheFragment;
import com.jy.xinlangweibo.utils.InternetConnectUtils;
import com.jy.xinlangweibo.widget.AdvertizeView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.openapi.models.GeoList;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import java.util.ArrayList;

public class DiscoverFragment extends BaseCacheFragment {
    private View view;
    private SwipeRefreshLayout mSwip;
    private ArrayList<Status> mDatas = new ArrayList<Status>();
    private StaggeredHomeAdapter mStaggeredHomeAdapter;
    private int curPage;
    private ArrayList<Integer> mHeights = new ArrayList<Integer>();
    private RecyclerView mRecyclerView;
    private int[] images = {R.drawable.desert, R.drawable.lighthouse,
            R.drawable.penguins, R.drawable.tulips};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.fragment_discover, null);

        init();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            ((MainActivity)activity).getToolbar().setVisibility(View.GONE);
        }
    }

    private void init() {

        // initData();

        mSwip = (SwipeRefreshLayout) view.findViewById(R.id.swiprefresh);
        mSwip.setProgressBackgroundColorSchemeColor(getResources().getColor(
                R.color.orange));
        mSwip.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadData(1);
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview);
        mStaggeredHomeAdapter = new StaggeredHomeAdapter(getActivity(), mDatas,
                mHeights);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mStaggeredHomeAdapter);
        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        initEvent();

        AdvertizeView ad = (AdvertizeView) view.findViewById(R.id.ad);
        ArrayList<View> views = new ArrayList<View>();
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(images[i]);
            views.add(imageView);
        }
        ad.setDataSource(views);

        loadData(1);
    }

    private void loadData(final int page) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                String ip = InternetConnectUtils.GetNetIp();
                if (!TextUtils.isEmpty(ip)) {
                    Oauth2AccessToken readAccessToken = AccessTokenKeeper
                            .readAccessToken(activity);
                    final StatusesInteraction api = new StatusesInteractionImpl(activity, readAccessToken);
                    api.ip2Geo(ip, new SimpleRequestlistener(activity, null) {
                        @Override
                        public void onComplete(String arg0) {
                            super.onComplete(arg0);
                            GeoList geos = GeoList.parse(arg0);
                            System.out.println("" + geos.Geos.get(0).latitude
                                    + "   ," + geos.Geos.get(0).longitude);
                            api.nearby_timeline(page,
                                    geos.Geos.get(0).latitude,
                                    geos.Geos.get(0).longitude,
                                    new SimpleRequestlistener(activity, null) {
                                        @Override
                                        public void onComplete(final String arg0) {
                                            super.onComplete(arg0);

                                            activity.runOnUiThread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    // TODO Auto-generated
                                                    // method stub

                                                    addData(page, arg0);
                                                    mSwip.setRefreshing(false);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onWeiboException(
                                                WeiboException arg0) {
                                            super.onWeiboException(arg0);
                                            activity.runOnUiThread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    mSwip.setRefreshing(false);
                                                }
                                            });
                                        }
                                    });
                        }

                        @Override
                        public void onWeiboException(WeiboException arg0) {
                            super.onWeiboException(arg0);
                            activity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    mSwip.setRefreshing(false);
                                }
                            });
                        }
                    });
                } else {
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mSwip.setRefreshing(false);
                        }
                    });
                    // ToastUtils.show(activity, "连接异常请重新加载",
                    // Toast.LENGTH_SHORT);
                }
            }

            private void addData(final int page, String response) {
                if (page == 1) {
                    mDatas.clear();
                    mHeights.clear();
                    curPage = 1;
                }
                curPage = page;
                StatusList list = StatusList.parse(response);
                if (list != null && list.statusList != null) {

                    for (int i = 0; i < list.statusList.size(); i++) {
                        mHeights.add((int) (100 + Math.random() * 300));
                    }
                    for (Status sta : list.statusList) {
                        mDatas.add(sta);
                    }

                    mStaggeredHomeAdapter.notifyDataSetChanged();
                }
                // adapter.notifyDataSetChanged();
                // ListView refreshableView = lv.getRefreshableView();
                // if (curPage < list.total_number) {
                // addFootView(refreshableView);
                // } else {
                // removeFootView(refreshableView);
                // }
            }
        }).start();
    }

    private void initEvent() {
        mStaggeredHomeAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(activity, position + " click",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(activity, position + " long click",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // protected void initData() {
    // mDatas = new ArrayList<String>();
    // for (int i = 'A'; i < 'z'; i++) {
    // mDatas.add("" + (char) i);
    // }
    // }


    public SwipeRefreshLayout getmSwip() {
        return mSwip;
    }
}
