package com.jy.xinlangweibo.ui.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiang.library.ui.adapter.recyleviewadapter.ARecycleViewItemView;
import com.jiang.library.ui.adapter.recyleviewadapter.BasicRecycleViewAdapter;
import com.jiang.library.ui.adapter.recyleviewadapter.IITemView;
import com.jiang.library.ui.adapter.recyleviewadapter.IItemViewCreator;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.db.StatusBeanDB;
import com.jy.xinlangweibo.models.db.bean.LocateRecordBeanSet;
import com.jy.xinlangweibo.models.net.sinaapi.BaseObserver;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.utils.ACache;
import com.jy.xinlangweibo.utils.WeiboStringUtils;
import com.jy.xinlangweibo.widget.NestListView.NestFullListView;
import com.jy.xinlangweibo.widget.NestListView.NestFullListViewAdapter;
import com.jy.xinlangweibo.widget.NestListView.NestFullViewHolder;
import com.jy.xinlangweibo.widget.emotionkeyboard.EmoticonsEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JIANG on 2016/10/14.
 */
public class SearchDialogFragment extends DialogFragment {

    private static final String SEARCHHISTORYKEY = "searchHistoryKey";
    private static final String SEARCHHISTORY = "searchHistory";

    @BindView(R.id.rv_history_suggesst)
    RecyclerView rvHistorySuggesst;
    @BindView(R.id.et_search)
    EmoticonsEditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    private BasicRecycleViewAdapter adapter;
    private List<String> strings = new ArrayList<String>();
    private ACache mCache;
    private Timer timer;
    private QueryTimeTask timerTask;
    private String queryKey = "";
    private List<LocateRecordBeanSet<String>> locateRecordBeenList = new ArrayList<>();

    public static SearchDialogFragment getSearchDialogFragment() {
        return new SearchDialogFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View v = inflater.inflate(R.layout.dialogfragment_search, container, false);
        ButterKnife.bind(this, v);
        getCache();
        initViewAndEvents();
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
//        保存搜索历史24h
        mCache.put(SEARCHHISTORY, (ArrayList) strings, 24 * 60 * 60);
        mCache.put(SEARCHHISTORYKEY, queryKey, 24 * 60 * 60);
    }

    private void getCache() {
        //		得到缓存
        mCache = ACache.get(this.getActivity());
        List<String> cachelist = (List<String>) mCache.getAsObject(SEARCHHISTORY);
        String cacheQueryKey = mCache.getAsString(SEARCHHISTORYKEY);
        if (cachelist != null)
            strings = cachelist;
        if (cacheQueryKey != null)
            queryKey = cacheQueryKey;

    }


    private void initViewAndEvents() {
        adapter = new BasicRecycleViewAdapter<LocateRecordBeanSet<String>>(new IItemViewCreator<LocateRecordBeanSet<String>>() {
            @Override
            public View newContentView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
                return layoutInflater.inflate(R.layout.item_search_suggest, parent, false);
            }

            @Override
            public IITemView<LocateRecordBeanSet<String>> newItemView(View convertView, int viewType) {
                return new SeachSuggestItem(getActivity(), convertView);
            }
        }, locateRecordBeenList, getActivity());
        rvHistorySuggesst.setAdapter(adapter);
        rvHistorySuggesst.setLayoutManager(new LinearLayoutManager(getActivity()));
        etSearch.setOnTextChangedInterface(new EmoticonsEditText.OnTextChangedInterface() {
            @Override
            public void onTextChanged(CharSequence newText) {
                if (((String) newText).isEmpty())
                    return;
//                500毫秒内没有内容变化则执行定时任务
                if (timer != null) {
                    timer.cancel();
                    timerTask = null;
                }
                startTimerTak((String) newText);
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                    startTimerTak(etSearch.getText().toString());
                return true;
            }
        });
        //  软键盘显示搜索
        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    private void startTimerTak(String newText) {
        timer = new Timer();
        timerTask = new QueryTimeTask();
        timerTask.setNewText(newText);
        timer.schedule(timerTask, 500);
    }

    @OnClick({R.id.iv_search, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                startTimerTak(etSearch.getText().toString());
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    public class QueryTimeTask extends TimerTask {
        private String newText;

        @Override
        public void run() {
            BaseActivity activity = (BaseActivity) getActivity();
            if(activity.getAccessAccessToken().getUid().isEmpty()) {
                return;
            }
            StatusBeanDB.getInstance(getActivity()).queryFuzzyStatusBeanList2(Long.parseLong(activity.getAccessAccessToken().getUid()),
                    newText,
                    new BaseObserver<List<String>>() {
                        @Override
                        public void onNext(List<String> models) {
                            super.onNext(models);
                            updateListView(models);
                        }
                    }
            );
            queryKey = newText;
        }

        public void setNewText(String newText) {
            this.newText = newText;
        }
    }

    private void updateListView(List<String> models) {
        locateRecordBeenList.clear();
        LocateRecordBeanSet recordBean = new LocateRecordBeanSet();
        recordBean.recordType = "本地微博记录";
        recordBean.recordList = models;
        locateRecordBeenList.add(recordBean);
        adapter.notifyDataSetChanged();

        strings.clear();
        strings.addAll(models);
    }

    public class SeachSuggestItem extends ARecycleViewItemView<LocateRecordBeanSet<String>> {
        public static final int MAXITEMNUM = 4;
        //        R.layout.item_search_suggest
        @BindView(R.id.tv_item_search_type)
        TextView tvItemSearchType;
        @BindView(R.id.nfl_recordList)
        NestFullListView nflRecordList;

        public SeachSuggestItem(Context context, View itemView) {
            super(context, itemView);
        }

        @Override
        public void onBindData(View convertView, final LocateRecordBeanSet<String> model, int position) {
            tvItemSearchType.setText(model.recordType);
            List<String> subList = model.recordList;
            if (model.recordList.size() > MAXITEMNUM) {
                subList = model.recordList.subList(0, MAXITEMNUM);
                nflRecordList.addFooterView(getActivity().getLayoutInflater().inflate(R.layout.nestfulllistview_footer, nflRecordList, false));
            } else {
                nflRecordList.removeFooterView();
            }
            nflRecordList.setAdapter(new NestFullListViewAdapter<String>(R.layout.item_search_suggest_record, subList) {
                @Override
                public void onBind(int pos, String o, NestFullViewHolder holder) {
                    TextView tvSearch = holder.getView(R.id.tv_item_search_suggest);
                    tvSearch.setText(WeiboStringUtils.getSearchMatchedText(context, queryKey, model.recordList.get(pos), tvSearch));
                }
            });
            nflRecordList.updateUI();
        }
    }
}
