package com.jy.xinlangweibo.ui.activity.base;

import android.view.Menu;
import android.view.MenuItem;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.share.ShareBottomDialog;

/**
 * Created by JIANG on 2017/1/10.
 */

public class BaseShareActivity extends BaseActivity {

    protected ShareBottomDialog dialog = new ShareBottomDialog();

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_share:
                share();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void share()
    {
        dialog.show(getSupportFragmentManager());
    }

}
