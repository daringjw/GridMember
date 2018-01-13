package com.jkkc.gridmember.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.jkkc.gridmember.LoginActivity;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.CallerBean;
import com.jkkc.gridmember.bean.PositionBean;
import com.jkkc.gridmember.common.Config;
import com.jkkc.gridmember.event.PositionEvent;
import com.jkkc.gridmember.manager.CallerInfoManager;
import com.jkkc.gridmember.manager.PositionManager;
import com.jkkc.gridmember.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

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
    @BindView(R.id.btnMap)
    Button mBtnMap;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private SweetAlertDialog mPDialog;
    private PositionBean mPositionBean;
    private PositionEvent mPositionEvent;
    private String mMsg;
    private CallerBean mCallerBean;
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
//原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息

            //将位置信息存进 内存中
            if (mPositionBean == null) {

                mPositionBean = new PositionBean();

            }
//            mPositionBean.latitude = latitude;
//            mPositionBean.longtitude = longitude;
            mPositionBean.mBDLocation = location;
            PositionManager.getInstance().setPositionBean(mPositionBean);
//            Log.d(TAG,"++++++++++++++");
//            Log.d(TAG, "latitude=" + latitude + "longitude=" + longitude);
            if (mPositionEvent == null) {

                mPositionEvent = new PositionEvent();

            }
            mPositionEvent.setPositionBean(mPositionBean);
            EventBus.getDefault().post(mPositionEvent);

            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明


            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息

           /* Log.d(TAG, "addr=" + addr);
            Log.d(TAG, "country=" + country);
            Log.d(TAG, "province=" + province);
            Log.d(TAG, "city=" + city);
            Log.d(TAG, "district=" + district);
            Log.d(TAG, "street=" + street);*/


        }
    }


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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        timeView = (TextView) findViewById(R.id.tvTimeView);

        timeView.setText(minute + ":" + second);

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


        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS}, 1);
            }


        }


        //开始定位
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认gcj02
//gcj02：国测局坐标；
//bd09ll：百度经纬度坐标；
//bd09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(5000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.start();
//mLocationClient为第二步初始化过的LocationClient对象
//调用LocationClient的start()方法，便可发起定位请求


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


        mBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), BaiduMapActivity.class));

            }
        });


        String caller_info = PrefUtils.getString(getApplicationContext(), "caller_info", null);
        if (!TextUtils.isEmpty(caller_info)) {
            Gson gson = new Gson();
            mCallerBean = gson.fromJson(caller_info, CallerBean.class);
            Log.d(TAG, "getHomeKeyPlace=" + mCallerBean.getHomeKeyPlace());

        }

        Button btnTimingActivity = (Button) findViewById(R.id.btnTimingActivity);
        btnTimingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), TimingActivity.class));


            }

        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),HomeActivity1.class));

            }
        });


    }


    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            //收到消息
            mMsg = messages.get(0).getBody().toString();
            mMsg = mMsg.replace("txt", "");
            mMsg = mMsg.substring(2, mMsg.length() - 1);

            Log.d(TAG, mMsg);

            Gson gson = new Gson();
            CallerBean callerBean = gson.fromJson(mMsg, CallerBean.class);

            //数据存在内存
            CallerInfoManager.getInstance().setCallerBean(callerBean);
            CallerBean callerBean1 = CallerInfoManager.getInstance().getCallerBean();
            Log.d(TAG, callerBean1.getSosId() + "");
            //求助者 数据存sp
            PrefUtils.setString(getApplicationContext(), "caller_info", mMsg);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    mTvReceive.setText(mMsg);

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

       /* @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
        }*/

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };


    //    记得在不需要的时候移除listener，如在activity的onDestroy()时
    @Override
    protected void onDestroy() {

        EMClient.getInstance().chatManager().removeMessageListener(msgListener);

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
        minute = -1;
        second = -1;

        if (mPDialog!=null){
            mPDialog.dismiss();
        }

        super.onDestroy();
    }

    @OnClick(R.id.btnLogout)
    public void onViewClicked() {

        if (mPDialog == null) {
            mPDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        }
        mPDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mPDialog.setTitleText("用户正在退出...");
        mPDialog.setCancelable(true);
        mPDialog.show();


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

                mPDialog.dismiss();
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

                Log.d(TAG, "出动成功" + mPositionBean.mBDLocation.getLatitude());
                Log.d(TAG, "出动成功" + mPositionBean.mBDLocation.getLongitude());

                if (mCallerBean != null) {
                    Log.d(TAG, mCallerBean.getSosId() + "");
                }


                //出动
                OkGo.<String>post(Config.GRIDMAN_URL + Config.STARTOFF_URL)
                        .tag(this)
                        .params("token", PrefUtils.getString(getApplicationContext(), "Token", null))
                        .params("operatorName", PrefUtils.getString(getApplicationContext(), "Name", null))
                        .params("latBD", mPositionBean.mBDLocation.getLatitude())
                        .params("lngBD", mPositionBean.mBDLocation.getLongitude())
                        .params("handleFlag", 1)
                        .params("sosId", mCallerBean == null ? 110 : mCallerBean.getSosId())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String result = response.body().toString();
                                Log.d(TAG, result);
                                Toast.makeText(getApplicationContext(), "出动成功",
                                        Toast.LENGTH_SHORT).show();


                            }

                        });


                break;
            case R.id.btnRefuseStartOff:

                Log.d(TAG, "拒绝出动成功" + mPositionBean.mBDLocation.getLatitude());
                Log.d(TAG, "拒绝出动成功" + mPositionBean.mBDLocation.getLongitude());

                if (mCallerBean != null) {
                    Log.d(TAG, mCallerBean.getSosId() + "");
                }

                //拒绝出动
                OkGo.<String>post(Config.GRIDMAN_URL + Config.REFUSESTARTOFF_URL)
                        .tag(this)
                        .params("token", PrefUtils.getString(getApplicationContext(), "Token", null))
                        .params("sosId", mCallerBean == null ? 110 : mCallerBean.getSosId())
                        .params("operatorName", PrefUtils.getString(getApplicationContext(), "Name", null))
                        .params("operatorDesc", "operatorDesc")
                        .params("handleFlag", 2)
                        .params("latBD", mPositionBean.mBDLocation.getLatitude())
                        .params("lngBD", mPositionBean.mBDLocation.getLongitude())
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

                Log.d(TAG, "到达成功" + mPositionBean.mBDLocation.getLatitude());
                Log.d(TAG, "到达成功" + mPositionBean.mBDLocation.getLongitude());

                if (mCallerBean != null) {
                    Log.d(TAG, mCallerBean.getSosId() + "");
                }

                //到达
                OkGo.<String>post(Config.GRIDMAN_URL + Config.ARRIVE_URL)
                        .tag(this)
                        .params("token", PrefUtils.getString(getApplicationContext(), "Token", null))
                        .params("sosId", mCallerBean == null ? 110 : mCallerBean.getSosId())
                        .params("operatorName", PrefUtils.getString(getApplicationContext(), "Name", null))
                        .params("operatorDesc", "operatorDesc")
                        .params("handleFlag", 3)
                        .params("latBD", mPositionBean.mBDLocation.getLatitude())
                        .params("lngBD", mPositionBean.mBDLocation.getLongitude())
                        .execute(new StringCallback() {

                            @Override
                            public void onSuccess(Response<String> response) {

                                String result = response.body().toString();
                                Log.d(TAG, result);
                                Toast.makeText(getApplicationContext(), "到达成功",
                                        Toast.LENGTH_SHORT).show();


                            }

                        });

                break;

        }
    }
}
