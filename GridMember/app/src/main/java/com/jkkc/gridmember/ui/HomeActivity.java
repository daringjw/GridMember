package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.jkkc.gridmember.LoginActivity;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.common.Config;
import com.jkkc.gridmember.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Guan on 2017/12/27.
 */

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @BindView(R.id.tvReceive)
    TextView mTvReceive;
    @BindView(R.id.btnLogout)
    Button mBtnLogout;
    @BindView(R.id.btnMsgPush)
    Button mBtnMsgPush;
    @BindView(R.id.btnStartOff)
    Button mBtnStartOff;
    @BindView(R.id.btnRefuseStartOff)
    Button mBtnRefuseStartOff;
    @BindView(R.id.btnArrive)
    Button mBtnArrive;
    @BindView(R.id.btnPersonalInfo)
    Button mBtnPersonalInfo;
    @BindView(R.id.btnPic)
    Button mBtnPic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

//        通过注册消息监听来接收消息。

        EMClient.getInstance().chatManager().addMessageListener(msgListener);


        mBtnPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));

            }
        });

        mBtnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), SweetActivity.class));

            }
        });


    }


    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            //收到消息
            Log.d(TAG, messages.get(0).getBody().toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mTvReceive.setText(messages.get(0).getBody().toString());

                    mBtnMsgPush.setText("你有新消息");

                }
            });


        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };


    //    记得在不需要的时候移除listener，如在activity的onDestroy()时
    @Override
    protected void onDestroy() {

        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        super.onDestroy();
    }

    @OnClick(R.id.btnLogout)
    public void onViewClicked() {

        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.d(TAG, "环信账号退出");

            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();


            }

        }, 1000);


    }


    @OnClick({R.id.btnMsgPush, R.id.btnStartOff, R.id.btnRefuseStartOff, R.id.btnArrive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnMsgPush:

                mBtnMsgPush.setText("消息推送");
//                OkGo.<String>post(Config.GRIDMAN_URL+Config.PUSHSOSMSG_URL)
//                        .tag(this)
//                        .params()


                break;

            case R.id.btnStartOff:
                //出动
                OkGo.<String>post(Config.GRIDMAN_URL + Config.STARTOFF_URL)
                        .tag(this)
                        .params("token", PrefUtils.getString(getApplicationContext(), "Token", null))
                        .params("operatorName", PrefUtils.getString(getApplicationContext(), "Name", null))
                        .params("latBD", 39.875365)
                        .params("lngBD", 116.107056)
                        .params("handleFlag", 1)
                        .params("sosId", 1)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String result = response.body().toString();
                                Log.d(TAG, result);
                                Toast.makeText(getApplicationContext(), "出动成功", Toast.LENGTH_SHORT).show();


                            }

                        });


                break;
            case R.id.btnRefuseStartOff:

                //拒绝出动
                OkGo.<String>post(Config.GRIDMAN_URL + Config.REFUSESTARTOFF_URL)
                        .tag(this)
                        .params("token", PrefUtils.getString(getApplicationContext(), "Token", null))
                        .params("sosId", 1)
                        .params("operatorName", PrefUtils.getString(getApplicationContext(), "Name", null))
                        .params("operatorDesc", "operatorDesc")
                        .params("handleFlag", 2)
                        .params("latBD", 39.875365)
                        .params("lngBD", 116.107056)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String result = response.body().toString();
                                Log.d(TAG, result);
                                Toast.makeText(getApplicationContext(), "拒绝出动成功", Toast.LENGTH_SHORT).show();


                            }

                        });


                break;
            case R.id.btnArrive:

                //到达
                OkGo.<String>post(Config.GRIDMAN_URL + Config.ARRIVE_URL)
                        .tag(this)
                        .params("token", PrefUtils.getString(getApplicationContext(), "Token", null))
                        .params("sosId", 1)
                        .params("operatorName", PrefUtils.getString(getApplicationContext(), "Name", null))
                        .params("operatorDesc", "operatorDesc")
                        .params("handleFlag", 3)
                        .params("latBD", 39.875365)
                        .params("lngBD", 116.107056)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String result = response.body().toString();
                                Log.d(TAG, result);
                                Toast.makeText(getApplicationContext(), "到达成功", Toast.LENGTH_SHORT).show();

                            }

                        });

                break;

        }
    }
}
