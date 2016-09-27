package com.jy.xinlangweibo.models.service.publish;

import android.os.AsyncTask;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.bean.PublishBean;
import com.jy.xinlangweibo.models.StatusesInteraction;
import com.jy.xinlangweibo.models.impl.StatusesInteractionImpl;
import com.jy.xinlangweibo.models.retrofitservice.StatusInteraction;

import java.util.ArrayList;

/**
 * Created by JIANG on 2016/9/22.
 */
public class UploadImageTask extends AsyncTask{

    private final PublishBean publishBean;

    public UploadImageTask(PublishBean publishBean) {
        this.publishBean = publishBean;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        ArrayList<String> pids = new ArrayList<>();
        for(int i = 0;i<publishBean.getPics().length;i++) {
            pids.add(0,StatusInteraction.getInstance().uploadImageForPicId(BaseApplication.getInstance().getAccessAccessToken().getToken(),publishBean.getPics()[i]));
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
