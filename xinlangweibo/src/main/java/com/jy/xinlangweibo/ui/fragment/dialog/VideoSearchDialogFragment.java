package com.jy.xinlangweibo.ui.fragment.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiang.library.ui.widget.LineLoadRecycleView;
import com.jiang.library.ui.widget.LoadMoreRecycleView;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.cache.LocaleHistory;
import com.jy.xinlangweibo.models.db.bean.LocateRecordBeanSet;
import com.jy.xinlangweibo.models.net.videoapi.RetrofitHelper;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoListBean;
import com.jy.xinlangweibo.ui.adapter.SearchTagAdapter;
import com.jy.xinlangweibo.ui.adapter.VideoSearchSection;
import com.jy.xinlangweibo.ui.adapter.section.SectionedRecyclerViewAdapter;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.widget.emotionkeyboard.EmoticonsEditText;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by JIANG on 2016/12/26.
 */

public class VideoSearchDialogFragment extends DialogFragment {

    @BindView(R.id.et_search)
    EmoticonsEditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.rl_content)
    RelativeLayout llContent;
    @BindView(R.id.tags_layout)
    TagFlowLayout tagsLayout;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;

    private View contentView;
    private View loadingView;
    private ViewGroup parentView;
    private int viewIndex;
    private ViewGroup.LayoutParams params;
    private View currentView;

    private List<String> searchHistoryStrings = new ArrayList<String>();
    private List<LocateRecordBeanSet<String>> locateRecordBeenList = new ArrayList<>();

    private int pNum = 2;
    private LoadMoreRecycleView rvCommon;
    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;
    private VideoSearchSection searchSection;
    private SearchTagAdapter searchTagAdapter;

    public static VideoSearchDialogFragment getVideoSearchDialogFragment() {
        return new VideoSearchDialogFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View v = inflater.inflate(R.layout.dialogfragment_video_search, container, false);
        ButterKnife.bind(this, v);
        getCache();
        initViewAndEvents();
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
//        保存搜索历史24h
        LocaleHistory.getInstance().updateVideoSearchHistory(searchHistoryStrings);
    }

    private void getCache() {
        //		得到缓存
        List<String> cachelist = (List<String>) LocaleHistory.getInstance().getVideoSearchHistory();
        if (cachelist != null)
            searchHistoryStrings = cachelist;
    }


    private void initViewAndEvents() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                    showSearchResult(null);
                return true;
            }
        });

        contentView = llContent;
        searchTagAdapter = new SearchTagAdapter(searchHistoryStrings);
        tagsLayout.setAdapter(searchTagAdapter);
        tagsLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                showSearchResult(searchTagAdapter.getItem(position));

                return true;
            }
        });
    }

    @OnClick({R.id.iv_search, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                showSearchResult(null);
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    private void loadData(final String kewWord, final String pNum) {
        RetrofitHelper.getVideoApi().getVideoListByKeyWord(kewWord, pNum).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VideoListBean>() {
                    @Override
                    public void call(VideoListBean models) {
                        //showlayout recycleview init recycleview
                        if (!kewWord.isEmpty() && !searchHistoryStrings.contains(kewWord)) {
                            searchHistoryStrings.add(kewWord);
                        }
                        restoreLayout();
                        if (pNum.equals("1")) {
                            searchSection = new VideoSearchSection(getActivity(), models.ret);
                            mSectionedRecyclerViewAdapter.addSection(searchSection);
                            mSectionedRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        showErrorMessage("搜索好像出现了意外哦！");
                    }
                });
    }

    private void loadMore() {
        RetrofitHelper.getVideoApi().getVideoListByKeyWord(etSearch.getText().toString(), "" + pNum++).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VideoListBean>() {
                    @Override
                    public void call(VideoListBean models) {
                        if (models.ret.totalRecords == 0 || models.ret.totalPnum < pNum) {
                            new Toast(getActivity()).show();
                            Toast.makeText(getActivity(), "没有更多结果了哦！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        searchSection.addData(models.ret.list);
                        mSectionedRecyclerViewAdapter.notifyItemRangeInserted(mSectionedRecyclerViewAdapter.getItemCount()
                                - models.ret.list.size(), models.ret.list.size());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.show(throwable.getMessage(), Log.WARN, "" + this);
                    }
                });
    }

    protected void showSearchResult(String keyWord) {
        View layout = LayoutInflater.from(contentView.getContext()).inflate(R.layout.comm_recycleview, null);
        rvCommon = (LoadMoreRecycleView) layout.findViewById(R.id.rv_common);
        loadingView = rvCommon;
        mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        rvCommon.setAdapter(mSectionedRecyclerViewAdapter);
        rvCommon.setmListViewListener(new LineLoadRecycleView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        showLayout(layout);
        loadData(keyWord == null?etSearch.getText().toString():keyWord, "" + 1);
    }

    protected void restoreLayout() {
        showLayout(loadingView);
    }


    protected void showLoading() {
        String msg = "";
        View layout = LayoutInflater.from(contentView.getContext()).inflate(R.layout.loading, null);
        if (!msg.isEmpty()) {
            TextView textView = (TextView) layout.findViewById(R.id.loading_msg);
            textView.setText(msg);
        }
        showLayout(layout);
    }

    protected void showErrorMessage(String msg) {
        View layout = LayoutInflater.from(contentView.getContext()).inflate(R.layout.message, null);
        if (!msg.isEmpty()) {
            TextView textView = (TextView) layout.findViewById(R.id.message_info);
            textView.setText(msg);
        }
        showLayout(layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchResult(null);
            }
        });
    }

    private void showLayout(View layout) {
        if (parentView == null) {
            params = contentView.getLayoutParams();
            if (contentView.getParent() != null) {
                parentView = (ViewGroup) contentView.getParent();
            } else {
                parentView = (ViewGroup) contentView.getRootView().findViewById(android.R.id.content);
            }
            int count = parentView.getChildCount();
            for (int index = 0; index < count; index++) {
                if (contentView == parentView.getChildAt(index)) {
                    viewIndex = index;
                    break;
                }
            }
            currentView = contentView;
        }
        this.currentView = layout;
        // 如果已经是那个view，那就不需要再进行替换操作了
        if (parentView.getChildAt(viewIndex) != layout) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
            parentView.removeViewAt(viewIndex);
            parentView.addView(layout, viewIndex, params);
        }
    }

    @OnClick(R.id.iv_delete)
    public void onClick() {
        boolean remove = LocaleHistory.getInstance().clearVideoSearchHistory();
        if(!remove) {
            Logger.showLog("failure------------","searchHistoryDelete");
        }
        searchHistoryStrings.clear();
        searchTagAdapter.notifyDataChanged();
    }
}
