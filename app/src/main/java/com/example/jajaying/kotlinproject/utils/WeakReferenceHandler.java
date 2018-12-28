package com.example.jajaying.kotlinproject.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by jajaying on 2018/8/28.
 */

public class WeakReferenceHandler  extends Handler {
    WeakReference<Activity> mWeakReference;
    public WeakReferenceHandler(Activity activity)
    {
        mWeakReference=new WeakReference<Activity>(activity);
    }
    @Override
    public void handleMessage(Message msg)
    {
        final Activity activity=mWeakReference.get();
        if(activity!=null)
        {
            if (msg.what == 1)
            {

            }
        }
    }
}
