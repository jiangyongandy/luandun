package com.jy.xinlangweibo.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.jiang.library.ui.adapter.recyleviewadapter.ARecycleViewItemView;
import com.jiang.library.ui.adapter.recyleviewadapter.BasicRecycleViewAdapter;
import com.jiang.library.ui.adapter.recyleviewadapter.IITemView;
import com.jiang.library.ui.adapter.recyleviewadapter.IItemViewCreator;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.db.StatusBeanDB;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.utils.ACache;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIANG on 2016/10/14.
 */
public class SearchDialogFragment extends DialogFragment {

    public static SearchDialogFragment getSearchDialogFragment() {
        return new SearchDialogFragment();
    }

    private static final String SEARCHHISTORY = "searchHistory";
    @BindView(R.id.sv_home)
    SearchView svHome;
    @BindView(R.id.rv_history_suggesst)
    RecyclerView rvHistorySuggesst;
    private BasicRecycleViewAdapter adapter;
    private List<String> strings = new ArrayList<String>();
    private ACache mCache;
    private Timer timer ;
    private QueryTimeTask timerTask ;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

/*    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //dialog.setTitle(R.string.warning);
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
//		setCancelable(false);
//		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.alertDialog);
//		builder.setTitle(R.string.warning);
//		builder.setMessage(this.getArguments().getString("msg"));
//		builder.setPositiveButton(R.string.btn_ok, null);
        return dialog;
    }*/

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
        mCache.put(SEARCHHISTORY,(ArrayList)strings,24*60*60);
    }

    private void getCache() {
        //		得到缓存
        mCache = ACache.get(this.getActivity());
        strings = (List<String>) mCache.getAsObject(SEARCHHISTORY);
    }


    private void initViewAndEvents() {
        adapter = new BasicRecycleViewAdapter<String>(new IItemViewCreator<String>() {
            @Override
            public View newContentView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
                return layoutInflater.inflate(R.layout.item_search_suggest, parent, false);
            }

            @Override
            public IITemView<String> newItemView(View convertView, int viewType) {
                return new SeachSuggestItem(getActivity(),convertView);
            }
        }, strings);
        rvHistorySuggesst.setAdapter(adapter);
        rvHistorySuggesst.setLayoutManager(new LinearLayoutManager(getActivity()));
        svHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
//                500毫秒内没有内容变化则执行定时任务
                if(timer != null) {
                    timer.cancel();
                    timerTask = null;
                }
                startTimerTak();
                timerTask.setNewText(newText);
                return true;
            }

            private void startTimerTak() {
                timer = new Timer();
                timerTask = new QueryTimeTask();
                timer.schedule(timerTask,500);
            }
        });
    }

    public  class QueryTimeTask extends TimerTask {
        private String newText;

        @Override
        public void run() {
            BaseActivity activity = (BaseActivity) getActivity();
            StatusBeanDB.getInstance(getActivity()).queryFuzzyStatusBeanList(Long.parseLong(activity.getAccessAccessToken().getUid()),newText);
        }

        public void setNewText(String newText) {
            this.newText = newText;
        }
    }

    public static class SeachSuggestItem extends ARecycleViewItemView<String> {
//        R.layout.item_search_suggest
        @BindView(R.id.tv_item_search_suggest)
        TextView tvItemSearchSuggest;

        public SeachSuggestItem(Context context, View itemView) {
            super(context, itemView);
        }

        @Override
        public void onBindData(View convertView, String model, int position) {
            tvItemSearchSuggest.setText(model);
        }
    }
}
