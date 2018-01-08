package com.jkkc.gridmember;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.jkkc.gridmember.bean.LoginInfo;
import com.jkkc.gridmember.common.Config;
import com.jkkc.gridmember.manager.UserInfoManager;
import com.jkkc.gridmember.ui.HomeActivity;
import com.jkkc.gridmember.utils.MD5;
import com.jkkc.gridmember.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.etUserName)
    EditText mEtUserName;
    @BindView(R.id.etPwd)
    EditText mEtPwd;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());





    }


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {

            Log.d(TAG,"已连接上");
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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



        OkGo.<String>post(Config.GRIDMAN_URL+Config.LOGIN_URL)
                .tag(this)
                .params("account","18518030828")
                .params("pwd", MD5.md5Encode("12345678"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String result = response.body().toString();
                        Log.d(TAG,result);

                        Gson gson = new Gson();
                        LoginInfo loginInfo = gson.fromJson(result, LoginInfo.class);
                        Log.d(TAG,loginInfo.getCode());

                        //把loginInfo存进内存
                        UserInfoManager.getInstance().setLoginInfo(loginInfo);

                        LoginInfo.DataBean data = loginInfo.getData();
                        PrefUtils.setString(getApplicationContext(),"data",result+"");
                        PrefUtils.setString(getApplicationContext(),"Address",data.getAddress());
                        PrefUtils.setString(getApplicationContext(),"Name",data.getName());
                        PrefUtils.setString(getApplicationContext(),"Phone",data.getPhone());
                        PrefUtils.setString(getApplicationContext(),"Sex",data.getSex());
                        PrefUtils.setString(getApplicationContext(),"Token",data.getToken());
                        PrefUtils.setString(getApplicationContext(),"Age",data.getAge()+"");
                        PrefUtils.setString(getApplicationContext(),"Id",data.getId()+"");





                        if (loginInfo.getCode().equals("200")){

//                            Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_SHORT).show();

                            //登陆环信
                            EMClient.getInstance().login("18518030828", "12345678", new EMCallBack() {//回调
                                @Override
                                public void onSuccess() {
                                    EMClient.getInstance().groupManager().loadAllGroups();
                                    EMClient.getInstance().chatManager().loadAllConversations();
                                    Log.d(TAG, "登录聊天服务器成功！");

                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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
                                            Toast.makeText(getApplicationContext(),"环信服务器出错",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });


                                }
                            });






                        }else {

                            Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);



                    }
                })
        ;



    }
}
