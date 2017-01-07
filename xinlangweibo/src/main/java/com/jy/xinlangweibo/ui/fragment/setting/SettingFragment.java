package com.jy.xinlangweibo.ui.fragment.setting;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.jy.xinlangweibo.AppSetting;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Preference pTheme;


    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addPreferencesFromResource(R.xml.ui_basic_settings_item);

        // 主题
        pTheme = (Preference) findPreference("pTheme");
        pTheme.setOnPreferenceClickListener(this);
        pTheme.setSummary(getResources().getStringArray(R.array.mdColorNames)[AppSetting.getThemeColor()]);

        // 缓存
        pTheme = (Preference) findPreference("pClearCache");
        pTheme.setOnPreferenceClickListener(this);
        initTitle();
    }

    private void initTitle() {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.title_settings);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "pTheme":

                MDColorsDialogFragment.launch(getActivity());
                break;

            case "pClearCache":

                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().clearDiskCache();
                ToastUtils.show(getActivity(),"已经清除",Toast.LENGTH_SHORT);
                break;
        }
        return true;
    }
}
