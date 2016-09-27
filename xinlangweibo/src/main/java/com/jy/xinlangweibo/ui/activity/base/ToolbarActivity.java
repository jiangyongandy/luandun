package com.jy.xinlangweibo.ui.activity.base;

import android.support.v7.widget.ViewStubCompat;
import android.widget.RelativeLayout;

import com.jy.xinlangweibo.R;

/**
 * Created by JIANG on 2016/9/17.
 */
public class ToolbarActivity extends BaseActivity {


    private ViewStubCompat viewStub;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.bga_pp_toolbar_viewstub);
        viewStub = (ViewStubCompat) findViewById(R.id.viewStub);
        if(layoutResID != R.layout.bga_pp_activity_photo_picker_preview) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewStub.getLayoutParams();
            lp.addRule(RelativeLayout.BELOW, R.id.toolbar);
        }
        viewStub.setLayoutResource(layoutResID);
        viewStub.inflate();
    }
}
