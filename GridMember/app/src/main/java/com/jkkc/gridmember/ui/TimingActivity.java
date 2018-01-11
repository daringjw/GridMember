package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.CallerBean;
import com.jkkc.gridmember.utils.PrefUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * Created by Guan on 2018/1/9.
 */

public class TimingActivity extends AppCompatActivity {

    private static final String TAG = TimingActivity.class.getSimpleName();


    private ImageView mIvHelp;
    private ImageView mIvStartoff;
    private LinearLayout mLlStartoff;
    private LinearLayout mLlHelp;
    private ImageView mIvHelpGuide;
    private ImageView mIvStartoffGuide;
    private ImageView mIvBack;


    private int minute = 30;//这是分钟
    private int second = 0;//这是分钟后面的秒数。这里是以30分钟为例的，所以，minute是30，second是0
    private TextView timeView;
    private Timer timer;
    private TimerTask timerTask;
    //这是接收回来处理的消息
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (minute == 0) {
                if (second == 0) {
                    timeView.setText("Time out !");
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (timerTask != null) {
                        timerTask = null;
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        timeView.setText("0" + minute + ":" + second);
                    } else {
                        timeView.setText("0" + minute + ":0" + second);
                    }
                }
            } else {
                if (second == 0) {
                    second = 59;
                    minute--;
                    if (minute >= 10) {
                        timeView.setText(minute + ":" + second);
                    } else {
                        timeView.setText("0" + minute + ":" + second);
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        if (minute >= 10) {
                            timeView.setText(minute + ":" + second);
                        } else {
                            timeView.setText("0" + minute + ":" + second);
                        }
                    } else {
                        if (minute >= 10) {
                            timeView.setText(minute + ":0" + second);
                        } else {
                            timeView.setText("0" + minute + ":0" + second);
                        }
                    }
                }
            }
        }

    };
    private TextView mTvOldManName1;
    private TextView mTvOldManName;
    private TextView mTvGender;
    private TextView mTvAge;
    private TextView mTvBloodType;
    private TextView mTvKeyPosition;
    private TextView mTvAddress;
    private TextView mTvMedicalHistory;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timing);
        ButterKnife.bind(this);

        mIvHelp = (ImageView) findViewById(R.id.ivHelp);
        mIvStartoff = (ImageView) findViewById(R.id.ivStartoff);
        mLlHelp = (LinearLayout) findViewById(R.id.llHelp);
        mLlStartoff = (LinearLayout) findViewById(R.id.llStartoff);
        mIvHelpGuide = (ImageView) findViewById(R.id.ivHelpGuide);
        mIvStartoffGuide = (ImageView) findViewById(R.id.ivStartoffGuide);
        mIvBack = (ImageView) findViewById(R.id.ivBack);
        timeView = (TextView) findViewById(R.id.timeView);

        mTvOldManName = (TextView) findViewById(R.id.tvOldManName);
        mTvGender = (TextView) findViewById(R.id.tvGender);
        mTvAge = (TextView) findViewById(R.id.tvAge);
        mTvBloodType = (TextView) findViewById(R.id.tvBloodType);
        mTvKeyPosition = (TextView) findViewById(R.id.tvKeyPosition);
        mTvAddress = (TextView) findViewById(R.id.tvAddress);
        mTvMedicalHistory = (TextView) findViewById(R.id.tvMedicalHistory);



        timeView.setText("00:" + minute + ":" + second);

        timerTask = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);


        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        mIvStartoffGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), BaiduMapActivity.class));

            }
        });


        mIvHelpGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), BaiduMapActivity.class));
            }
        });


        mIvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIvStartoff.setImageResource(R.mipmap.startoff_nor);
                mIvHelp.setImageResource(R.mipmap.help_pre);
                mLlHelp.setVisibility(View.VISIBLE);
                mLlStartoff.setVisibility(View.GONE);

            }
        });


        mIvStartoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIvStartoff.setImageResource(R.mipmap.startoff_pre);
                mIvHelp.setImageResource(R.mipmap.help_nor);
                mLlHelp.setVisibility(View.GONE);
                mLlStartoff.setVisibility(View.VISIBLE);


            }
        });


        String caller_info = PrefUtils.getString(getApplicationContext(), "caller_info", null);
        if (!TextUtils.isEmpty(caller_info)) {
            Gson gson = new Gson();
            mCallerBean = gson.fromJson(caller_info, CallerBean.class);
            Log.d(TAG, "getImgPath=" + mCallerBean.getImgPath());

            mTvOldManName.setText(mCallerBean.getName());
            if (mCallerBean.getSex()==0){
                mTvGender.setText("男");
            }else if (mCallerBean.getSex()==1){
                mTvGender.setText("女");
            }

            mTvAge.setText(mCallerBean.getAge()+"");
            mTvBloodType.setText(mCallerBean.getBloodType());
            mTvKeyPosition.setText(mCallerBean.getHomeKeyPlace());
            mTvAddress.setText("住址："+mCallerBean.getAddress());
            mTvMedicalHistory.setText("病史："+mCallerBean.getJibing());



        }


    }

    private CallerBean mCallerBean;

}
