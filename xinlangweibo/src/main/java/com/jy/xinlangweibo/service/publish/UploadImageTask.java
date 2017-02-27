package com.jy.xinlangweibo.service.publish;

import android.content.Context;
import android.os.AsyncTask;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.bean.PublishBean;
import com.jy.xinlangweibo.models.net.StatusesInteraction;
import com.jy.xinlangweibo.models.net.impl.StatusesInteractionImpl;
import com.jy.xinlangweibo.models.net.sinaapi.StatusInteraction;

import java.util.ArrayList;

/**
 * Created by JIANG on 2016/9/22.
 */
public class UploadImageTask extends AsyncTask{

    private final PublishBean publishBean;
    private final Context context;

    public UploadImageTask(PublishBean publishBean, Context context) {
        this.context = context;
        this.publishBean = publishBean;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        ArrayList<String> pids = new ArrayList<String>();
        for(int i = 0;i<publishBean.getPics().length;i++) {
            pids.add(0,StatusInteraction.getInstance(context).uploadImageForPicId(BaseApplication.getInstance().getAccessAccessToken().getToken(),publishBean.getPics()[i]));
        }
        // 图片都上传完了，开始发布微博
        String picIdStr = "";
        for (String picId : pids) {
            picIdStr = picIdStr + picId + ",";
        }
        picIdStr = picIdStr.substring(0, picIdStr.length() - 1);
        StatusesInteraction api = new StatusesInteractionImpl(BaseApplication.getInstance(), BaseApplication.getInstance().getAccessAccessToken());
        api.updateTextAImage(publishBean.getTextContent(),null,picIdStr,null,null,null);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }
}
