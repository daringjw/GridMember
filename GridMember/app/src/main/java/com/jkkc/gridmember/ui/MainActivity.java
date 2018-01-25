package com.jkkc.gridmember.ui;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.CallerBean;
import com.jkkc.gridmember.bean.PositionBean;
import com.jkkc.gridmember.event.PositionEvent;
import com.jkkc.gridmember.manager.CallerInfoManager;
import com.jkkc.gridmember.manager.PositionManager;
import com.jkkc.gridmember.ui.fragment.ForHelpFragment;
import com.jkkc.gridmember.ui.fragment.MapGuideFragment;
import com.jkkc.gridmember.ui.fragment.PersonalCenterFragment;
import com.jkkc.gridmember.ui.fragment.ReturnVisitFragment;
import com.jkkc.gridmember.utils.PrefUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private NotificationManager notificationManager;

    // 第二步：对Notification的一些属性进行设置比如：内容，图标，标题，相应notification的动作进行处理等等；
    public void showNotification() {

        Notification.Builder builder1 = new Notification.Builder(MainActivity.this);
        builder1.setSmallIcon(R.mipmap.ic_launcher_round); //设置图标
        builder1.setTicker("显示第二个通知");
        builder1.setContentTitle("老人向你求助"); //设置标题
        builder1.setContentText("点击查看详细内容"); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_VIBRATE); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intent = new Intent(MainActivity.this, TimingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
        builder1.setContentIntent(pendingIntent);
        Notification notification1 = builder1.build();
        notificationManager.notify(124, notification1); // 通过通知管理器发送通知


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

            startActivity(new Intent(getApplicationContext(), TimingActivity.class));

            // notification
            showNotification();


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       /* RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        FunGameHitBlockHeader funGameHitBlockHeader = new FunGameHitBlockHeader(this);
        refreshLayout.setRefreshHeader(funGameHitBlockHeader);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(3000*//*,false*//*);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(3000*//*,false*//*);//传入false表示加载失败
            }
        });*/

        // 第一步：通过getSystemService（）方法得到NotificationManager对象；
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


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

        String caller_info = PrefUtils.getString(getApplicationContext(), "caller_info", null);
        if (!TextUtils.isEmpty(caller_info)) {
            Gson gson = new Gson();
            mCallerBean = gson.fromJson(caller_info, CallerBean.class);
            Log.d(TAG, "getHomeKeyPlace=" + mCallerBean.getHomeKeyPlace());

        }

        //Fragment+ViewPager+FragmentViewPager组合的使用
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                this);
        viewPager.setAdapter(adapter);

        //TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

//        tabLayout.addTab(tabLayout.newTab().setText("回访").setIcon(R.mipmap.ic_launcher));
//        tabLayout.addTab(tabLayout.newTab().setText("导航").setIcon(R.mipmap.ic_launcher));
//        tabLayout.addTab(tabLayout.newTab().setText("求助").setIcon(R.mipmap.ic_launcher));
//        tabLayout.addTab(tabLayout.newTab().setText("个人中心").setIcon(R.mipmap.ic_launcher));

        tabLayout.getTabAt(0).setIcon(R.drawable.button_location_selector);
        tabLayout.getTabAt(1).setIcon(R.drawable.button_location_selector);
        tabLayout.getTabAt(2).setIcon(R.drawable.button_location_selector);
        tabLayout.getTabAt(3).setIcon(R.drawable.button_location_selector);



    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter {


        private String[] titles = new String[]{"回访", "导航", "求助", "个人中心"};
        private SparseArray<Fragment> fragmentMap;

        private Context context;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;

            if (fragmentMap == null) {
                fragmentMap = new SparseArray();
                fragmentMap.put(0, new ReturnVisitFragment());
                fragmentMap.put(1, new MapGuideFragment());
                fragmentMap.put(2, new ForHelpFragment());
                fragmentMap.put(3, new PersonalCenterFragment());
            }


        }

        @Override
        public Fragment getItem(int position) {
            return fragmentMap.get(position);
        }

        @Override
        public int getCount() {
            return fragmentMap.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }


    }

}
