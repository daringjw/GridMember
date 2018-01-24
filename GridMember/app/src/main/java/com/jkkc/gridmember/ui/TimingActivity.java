package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.CallerBean;
import com.jkkc.gridmember.common.Config;
import com.jkkc.gridmember.ui.dialog.RefuseDialog;
import com.jkkc.gridmember.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


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
    private TextView mTvOldManName;
    private TextView mTvGender;
    private TextView mTvAge;
    private TextView mTvBloodType;
    private TextView mTvKeyPosition;
    private TextView mTvAddress;
    private TextView mTvMedicalHistory;
    private ImageView mIvScene;
    private LinearLayout mLlTheScene;
    private ImageView mIvFamilyScene;
    private ImageView mIvScene120;
    private ImageView mIvSceneEnd;
    private ImageView mBtnHelpStartoff;
    private ImageView mBtnHelpRefuseStartoff;
    private ImageView mIvArrive;
    private ImageView mIvStartoffFamily;
    private ImageView mIvCall120;
    private ImageView mIvCallOldMan;


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

        mIvScene = (ImageView) findViewById(R.id.ivScene);
        mLlTheScene = (LinearLayout) findViewById(R.id.llTheScene);
        mIvFamilyScene = (ImageView) findViewById(R.id.ivFamilyScene);
        mIvScene120 = (ImageView) findViewById(R.id.ivScene120);
        mIvSceneEnd = (ImageView) findViewById(R.id.ivSceneEnd);

        mIvStartoffFamily = (ImageView) findViewById(R.id.ivStartoffFamily);

        mIvCall120 = (ImageView) findViewById(R.id.ivCall120);
        mIvCallOldMan = (ImageView) findViewById(R.id.ivCallOldMan);


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


       /* mIvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIvStartoff.setImageResource(R.mipmap.startoff_nor);
                mIvHelp.setImageResource(R.mipmap.help_pre);
                mIvScene.setImageResource(R.mipmap.the_scene_nor);
                mLlHelp.setVisibility(View.VISIBLE);
                mLlStartoff.setVisibility(View.GONE);
                mLlTheScene.setVisibility(View.GONE);


            }
        });*/


       /* mIvStartoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIvStartoff.setImageResource(R.mipmap.startoff_pre);
                mIvHelp.setImageResource(R.mipmap.help_nor);
                mIvScene.setImageResource(R.mipmap.the_scene_nor);
                mLlHelp.setVisibility(View.GONE);
                mLlStartoff.setVisibility(View.VISIBLE);
                mLlTheScene.setVisibility(View.GONE);

            }
        });*/


       /* mIvScene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIvStartoff.setImageResource(R.mipmap.startoff_nor);
                mIvHelp.setImageResource(R.mipmap.help_nor);
                mIvScene.setImageResource(R.mipmap.the_scene_pre);
                mLlHelp.setVisibility(View.GONE);
                mLlStartoff.setVisibility(View.GONE);
                mLlTheScene.setVisibility(View.VISIBLE);

            }
        });*/


        String caller_info = PrefUtils.getString(getApplicationContext(), "caller_info", null);
        if (!TextUtils.isEmpty(caller_info)) {
            Gson gson = new Gson();
            mCallerBean = gson.fromJson(caller_info, CallerBean.class);
            Log.d(TAG, "getImgPath=" + mCallerBean.getImgPath());

            mTvOldManName.setText(mCallerBean.getName());
            if (mCallerBean.getSex() == 0) {
                mTvGender.setText("男");
            } else if (mCallerBean.getSex() == 1) {
                mTvGender.setText("女");
            }

            mTvAge.setText(mCallerBean.getAge() + "");
            mTvBloodType.setText(mCallerBean.getBloodType());
            mTvKeyPosition.setText(mCallerBean.getHomeKeyPlace());
            mTvAddress.setText("住址：" + mCallerBean.getAddress());
            mTvMedicalHistory.setText("病史：" + mCallerBean.getJibing());

            mIvFamilyScene.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mCallerBean != null) {

                        new SweetAlertDialog(TimingActivity.this)
                                .setTitleText("拨号?")
                                .setContentText(mCallerBean.linkPhone)
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        //调用Android系统API打电话
                                        Uri uri = Uri.parse("tel:" + mCallerBean.linkPhone);
                                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                                        startActivity(intent);

                                    }
                                })
                                .setCancelText("取消")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();

                                    }
                                })
                                .show();


                    }


                }
            });

            mIvCall120.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new SweetAlertDialog(TimingActivity.this)
                            .setTitleText("拨号?")
                            .setContentText("120")
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    //调用Android系统API打电话
                                    Uri uri = Uri.parse("tel:120");
                                    Intent intent = new Intent(Intent.ACTION_CALL, uri);

                                    startActivity(intent);

                                }
                            })
                            .setCancelText("取消")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();

                                }
                            })
                            .show();


                }
            });

            mIvScene120.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    new SweetAlertDialog(TimingActivity.this)
                            .setTitleText("拨号?")
                            .setContentText("120")
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    //调用Android系统API打电话
                                    Uri uri = Uri.parse("tel:120");
                                    Intent intent = new Intent(Intent.ACTION_CALL, uri);

                                    startActivity(intent);

                                }
                            })
                            .setCancelText("取消")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();

                                }
                            })
                            .show();

                }
            });

            mIvCallOldMan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mCallerBean != null) {
                        new SweetAlertDialog(TimingActivity.this)
                                .setTitleText("拨号?")
                                .setContentText(mCallerBean.linkPhone)
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        //调用Android系统API打电话
                                        Uri uri = Uri.parse("tel:" + mCallerBean.linkPhone);
                                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                                        startActivity(intent);

                                    }
                                })
                                .setCancelText("取消")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();

                                    }
                                })
                                .show();
                    }

                }
            });


            mIvStartoffFamily.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (mCallerBean != null) {
                        new SweetAlertDialog(TimingActivity.this)
                                .setTitleText("拨号?")
                                .setContentText(mCallerBean.linkPhone)
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        //调用Android系统API打电话
                                        Uri uri = Uri.parse("tel:" + mCallerBean.linkPhone);
                                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                                        startActivity(intent);

                                    }
                                })
                                .setCancelText("取消")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();

                                    }
                                })
                                .show();
                    }


                }
            });


        }


        mBtnHelpStartoff = (ImageView) findViewById(R.id.btnHelpStartoff);


        mBtnHelpStartoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //出动
                OkGo.<String>post(Config.GRIDMAN_URL + Config.STARTOFF_URL)
                        .tag(this)
                        .params("token", PrefUtils.getString(getApplicationContext(), "Token", null))
                        .params("operatorName", PrefUtils.getString(getApplicationContext(), "Name", null))
                        //22.5581041512,113.8975656619
                        .params("latBD", 22.5581041512)
                        .params("lngBD", 113.8975656619)
                        .params("handleFlag", 1)
                        .params("sosId", mCallerBean == null ? 110 : mCallerBean.getSosId())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String result = response.body().toString();
                                Log.d(TAG, result);

                                Toast.makeText(getApplicationContext(), "出动成功",
                                        Toast.LENGTH_SHORT).show();

                                mIvStartoff.setImageResource(R.mipmap.startoff_pre);
                                mIvHelp.setImageResource(R.mipmap.help_nor);
                                mIvScene.setImageResource(R.mipmap.the_scene_nor);
                                mLlHelp.setVisibility(View.GONE);
                                mLlStartoff.setVisibility(View.VISIBLE);
                                mLlTheScene.setVisibility(View.GONE);


                            }

                        });

            }
        });

        mBtnHelpRefuseStartoff = (ImageView) findViewById(R.id.btnHelpRefuseStartoff);
        mBtnHelpRefuseStartoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //拒绝出动
                OkGo.<String>post(Config.GRIDMAN_URL + Config.REFUSESTARTOFF_URL)
                        .tag(this)
                        .params("token", PrefUtils.getString(getApplicationContext(), "Token", null))
                        .params("sosId", mCallerBean == null ? 110 : mCallerBean.getSosId())
                        .params("operatorName", PrefUtils.getString(getApplicationContext(), "Name", null))
                        .params("operatorDesc", "operatorDesc")
                        .params("handleFlag", 2)
                        //22.5581041512,113.8975656619
                        .params("latBD", 22.5581041512)
                        .params("lngBD", 113.8975656619)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String result = response.body().toString();
                                Log.d(TAG, result);

                                RefuseDialog refuseDialog = new RefuseDialog(TimingActivity.this);
                                refuseDialog.show();
                                refuseDialog.setCanceledOnTouchOutside(true);





//                                startActivity(new Intent(getApplicationContext(), RefuseActivity.class));
//                                finish();


                            }

                        });
            }
        });


        mIvArrive = (ImageView) findViewById(R.id.ivArrive);
        mIvArrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //到达
                OkGo.<String>post(Config.GRIDMAN_URL + Config.ARRIVE_URL)
                        .tag(this)
                        .params("token", PrefUtils.getString(getApplicationContext(), "Token", null))
                        .params("sosId", mCallerBean == null ? 110 : mCallerBean.getSosId())
                        .params("operatorName", PrefUtils.getString(getApplicationContext(), "Name", null))
                        .params("operatorDesc", "operatorDesc")
                        .params("handleFlag", 3)
                        //22.5581041512,113.8975656619
                        .params("latBD", 22.5581041512)
                        .params("lngBD", 113.8975656619)
                        .execute(new StringCallback() {

                            @Override
                            public void onSuccess(Response<String> response) {

                                String result = response.body().toString();
                                Log.d(TAG, result);
                                Toast.makeText(getApplicationContext(), "到达成功",
                                        Toast.LENGTH_SHORT).show();

                                mIvStartoff.setImageResource(R.mipmap.startoff_nor);
                                mIvHelp.setImageResource(R.mipmap.help_nor);
                                mIvScene.setImageResource(R.mipmap.the_scene_pre);
                                mLlHelp.setVisibility(View.GONE);
                                mLlStartoff.setVisibility(View.GONE);
                                mLlTheScene.setVisibility(View.VISIBLE);

                                mIvSceneEnd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        finish();

                                    }
                                });


                            }

                        });

            }
        });


    }

    private CallerBean mCallerBean;

}
