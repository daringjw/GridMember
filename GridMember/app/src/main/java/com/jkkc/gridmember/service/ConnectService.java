package com.jkkc.gridmember.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.util.NetUtils;
import com.jkkc.gridmember.LoginActivity;
import com.jkkc.gridmember.ui.MainActivity;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

/**
 * Created by Guan on 2018/2/1.
 */

public class ConnectService extends Service{

    public final static String TAG=ConnectService.class.getSimpleName();




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG,"ConnectService");





    }





    @Override
    public void onDestroy() {
        super.onDestroy();



    }
}
