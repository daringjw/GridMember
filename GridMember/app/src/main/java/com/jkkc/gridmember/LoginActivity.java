package com.jkkc.gridmember;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.jkkc.gridmember.bean.LoginInfo;
import com.jkkc.gridmember.common.Config;
import com.jkkc.gridmember.manager.UserInfoManager;
import com.jkkc.gridmember.ui.MainActivity;
import com.jkkc.gridmember.utils.MD5;
import com.jkkc.gridmember.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.bugly.Bugly;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.etUserName)
    EditText mEtUserName;
    @BindView(R.id.etPwd)
    EditText mEtPwd;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;
    private SweetAlertDialog mPDialog;
    private MyConnectionListener mMyConnectionListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

        //开始定位
        if (AppUtils.isAppDebug()) {

            //内测版本
            Bugly.init(getApplicationContext(), "8711747843", false);

        } else {
            //正式版本
            Bugly.init(getApplicationContext(), "001e1b77fe", false);


        }


    }

//    When it succeeded in obtaining permission

    @PermissionSuccess(requestCode = 100)
    public void doSomething() {

//        Toast.makeText(this, "已经授权", Toast.LENGTH_LONG).show();
        //开始定位
        if (AppUtils.isAppDebug()) {

            //内测版本
            Bugly.init(getApplicationContext(), "8711747843", false);

        } else {
            //正式版本
            Bugly.init(getApplicationContext(), "001e1b77fe", false);


        }


    }
//    When it failed in obtaining permission

   /* @PermissionFail(requestCode = 100)
    public void doFailSomething(){

        Toast.makeText(this, "Contact permission is not granted", Toast.LENGTH_SHORT).show();


    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        //6.0权限
        PermissionGen.with(LoginActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.GET_ACCOUNTS

                )
                .request();


        //注册一个监听连接状态的listener
        mMyConnectionListener = new MyConnectionListener();
        EMClient.getInstance().addConnectionListener(mMyConnectionListener);


    }


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {

            Log.d(TAG, "已连接上");
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除


                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录


                    } else {
                        if (NetUtils.hasNetwork(LoginActivity.this)) {
                            //连接不到聊天服务器

                        } else {
                            //当前网络不可用，请检查网络设置


                        }
                    }
                }
            });
        }
    }

    @OnClick(R.id.btnLogin)
    public void onViewClicked(View view) {

        if (mPDialog == null) {
            mPDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        }
        mPDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mPDialog.setTitleText("用户正在登录...");
        mPDialog.setCancelable(true);
        mPDialog.show();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        },1500);*/


        OkGo.<String>post(Config.GRIDMAN_URL + Config.LOGIN_URL)
                .tag(this)
                //
                .params("account", "18518030828")
                .params("pwd", MD5.md5Encode("12345678"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String result = response.body().toString();
                        Log.d(TAG, result);
                        PrefUtils.setString(getApplicationContext(), "user_info", result);


                        Gson gson = new Gson();
                        LoginInfo loginInfo = gson.fromJson(result, LoginInfo.class);
                        Log.d(TAG, loginInfo.getCode());

                        //把loginInfo存进内存
                        UserInfoManager.getInstance().setLoginInfo(loginInfo);

                        LoginInfo.DataBean data = loginInfo.getData();
                        PrefUtils.setString(getApplicationContext(), "data", result + "");


                        if (loginInfo.getCode().equals("200")) {

//                            Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_SHORT).show();
                            PrefUtils.setString(getApplicationContext(), "Address", data.getAddress());
                            PrefUtils.setString(getApplicationContext(), "Name", data.getName());
                            PrefUtils.setString(getApplicationContext(), "Phone", data.getPhone());
                            PrefUtils.setString(getApplicationContext(), "Sex", data.getSex());
                            PrefUtils.setString(getApplicationContext(), "Token", data.getToken());
                            PrefUtils.setString(getApplicationContext(), "Age", data.getAge() + "");
                            PrefUtils.setString(getApplicationContext(), "Id", data.getId() + "");

                            //登陆环信
                            EMClient.getInstance().login("18518030828", "12345678", new EMCallBack() {//回调
                                @Override
                                public void onSuccess() {
                                    EMClient.getInstance().groupManager().loadAllGroups();
                                    EMClient.getInstance().chatManager().loadAllConversations();
                                    Log.d(TAG, "登录聊天服务器成功！");
                                    mPDialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();


                                }

                                @Override
                                public void onProgress(int progress, String status) {

                                }

                                @Override
                                public void onError(int code, String message) {
                                    Log.d(TAG, "登录聊天服务器失败！");
                                    Log.d(TAG, message);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "登录聊天服务器失败！",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });


                                }
                            });


                        } else {

                            Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                })
        ;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPDialog != null) {
            mPDialog.dismiss();
        }

        if (mMyConnectionListener != null) {
            EMClient.getInstance().removeConnectionListener(mMyConnectionListener);
            Log.d(TAG, "removeConnectionListener");

        }


    }


}
