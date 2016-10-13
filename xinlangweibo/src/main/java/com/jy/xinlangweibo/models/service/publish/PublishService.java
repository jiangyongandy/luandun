package com.jy.xinlangweibo.models.service.publish;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.bean.PublishBean;

/**
 * Created by JIANG on 2016/9/21.
 */
public class PublishService extends Service implements  InterfacePublisher{

    public static void  startPublish(Context context, PublishBean bean) {
        Intent intent = new Intent(context,PublishService.class);
        intent.putExtra("publishbean",bean);
        context.startService(intent);
    }

    public static void stopPublish() {
        Intent intent = new Intent(BaseApplication.getInstance(), PublishService.class);
        BaseApplication.getInstance().stopService(intent);
    }

    private PublishService publisher;
    private PublishBinder binder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(publisher == null)
            publisher = this;
        if(binder == null)
            binder = new PublishBinder();
        if(intent != null){
            PublishBean publishbean = (PublishBean) intent.getSerializableExtra("publishbean");
            if(publishbean != null)
                publish(publishbean);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void publish(PublishBean data) {
        new UploadImageTask(data,getApplicationContext()).execute();
    }

    public class PublishBinder extends Binder {

        public InterfacePublisher getPublisher() {
            return publisher;
        }

    }
}
