package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.jkkc.gridmember.LoginActivity;
import com.jkkc.gridmember.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


//        通过注册消息监听来接收消息。

        EMClient.getInstance().chatManager().addMessageListener(msgListener);




    }


    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Log.d(TAG,messages.get(0).getBody().toString());


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
                Log.d(TAG,"环信账号退出");

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

        },1000);



    }


}
