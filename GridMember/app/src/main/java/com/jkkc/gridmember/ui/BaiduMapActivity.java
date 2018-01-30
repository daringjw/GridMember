package com.jkkc.gridmember.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.google.gson.Gson;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.CallerBean;
import com.jkkc.gridmember.bean.PositionBean;
import com.jkkc.gridmember.event.PositionEvent;
import com.jkkc.gridmember.utils.PrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Guan on 2018/1/8.
 */

public class BaiduMapActivity extends AppCompatActivity {

    private static final String TAG = BaiduMapActivity.class.getSimpleName();
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private PositionBean mPositionBean;
    private TextView mTvBack;
    private TextView mTvGuide;
    private ImageView mIvCurrentPosition;
    private BitmapDescriptor mCurrentMarker;
    private boolean mIconLocationPre;
    private MyLocationConfiguration mConfig;
    private BDLocation mBdLocation;
    private String mCaller_info;
    private MyLocationData mLocData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);//订阅

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        //获取地图控件引用
        mMapView = (TextureMapView) findViewById(R.id.mTexturemap);
        mBaiduMap = mMapView.getMap();

        //显示定位
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

//         隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        //放大地图
        MapStatus.Builder builder = new MapStatus.Builder(mBaiduMap.getMapStatus());
        builder.zoom(20.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


        mTvBack = (TextView) findViewById(R.id.tvBack);
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        //        mCurrentMarker = BitmapDescriptorFactory
        //                .fromResource(R.drawable.icon_geo);
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_location_3);

        mConfig = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING,
                true, mCurrentMarker);

        mIvCurrentPosition = (ImageView) findViewById(R.id.ivCurrentPosition);

        mIconLocationPre = true;

        mIvCurrentPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mIconLocationPre) {
                    mIvCurrentPosition.setImageResource(R.mipmap.icon_location_normal);
                    mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                            MyLocationConfiguration.LocationMode.NORMAL,
                            true, mCurrentMarker));
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.overlook(0);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                            .newMapStatus(builder.build()));

                    mIconLocationPre = false;
                } else {

                    mIvCurrentPosition.setImageResource(R.mipmap.icon_location_pressed);
                    mConfig = new MyLocationConfiguration(
                            MyLocationConfiguration.LocationMode.NORMAL,
                            true, mCurrentMarker);

                    mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                            MyLocationConfiguration.LocationMode.FOLLOWING,
                            true, mCurrentMarker));
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.overlook(0);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                            .newMapStatus(builder.build()));

                    mIconLocationPre = true;
                }


            }
        });


        mBaiduMap.setMyLocationConfiguration(mConfig);


        mCaller_info = PrefUtils.getString(getApplicationContext(), "caller_info", null);
        if (!TextUtils.isEmpty(mCaller_info)) {
            Gson gson = new Gson();
            CallerBean callerBean = gson.fromJson(mCaller_info, CallerBean.class);
            Log.d(TAG, callerBean.getLat() + "longitude=" + callerBean.getLng());
            double latitude = Double.parseDouble(callerBean.getLat());
            double longitude = Double.parseDouble(callerBean.getLng());
            mBdLocation = new BDLocation();
            //22.5857814798,113.8761278926
            mBdLocation.setLatitude(latitude);
            mBdLocation.setLongitude(longitude);

            // 构造定位数据
            mLocData = new MyLocationData.Builder()
                    .accuracy(mBdLocation.getRadius())
                    .latitude(mBdLocation.getLatitude())
                    .longitude(mBdLocation.getLongitude())
                    .build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(mLocData);

        } else {

            if (mBdLocation == null) {
                mBdLocation = new BDLocation();
                //22.5857814798,113.8761278926
                mBdLocation.setLatitude(22.5857814798);
                mBdLocation.setLongitude(113.8761278926);
            }

            // 构造定位数据
            mLocData = new MyLocationData.Builder()
                    .accuracy(mBdLocation.getRadius())
                    .latitude(mBdLocation.getLatitude())
                    .longitude(mBdLocation.getLongitude())
                    .build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(mLocData);

        }


    }



    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(PositionEvent event) {


        mPositionBean = event.getPositionBean();

        Log.d(TAG, "latitude=" + mPositionBean.mBDLocation.getLatitude());
        Log.d(TAG, "longitude=" + mPositionBean.mBDLocation.getLongitude());

        //导航
        mTvGuide = (TextView) findViewById(R.id.tvGuide);
        mTvGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new SweetAlertDialog(BaiduMapActivity.this)
                        .setTitleText("导航?")
                        .setContentText("进入百度地图进行导航")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {


                                //确定，进入百度地图进行导航
                                // 天安门坐标
                                double mLat1 = mPositionBean.mBDLocation.getLatitude();
                                double mLon1 = mPositionBean.mBDLocation.getLongitude();

                                // 百度大厦坐标

                                if (mBdLocation == null) {
                                    mBdLocation = new BDLocation();
                                    //22.5857814798,113.8761278926
                                    mBdLocation.setLatitude(22.5857814798);
                                    mBdLocation.setLongitude(113.8761278926);
                                }

                                double mLat2 = mBdLocation.getLatitude();
                                double mLon2 = mBdLocation.getLongitude();
                                LatLng pt_start = new LatLng(mLat1, mLon1);
                                LatLng pt_end = new LatLng(mLat2, mLon2);

                                // 构建 route搜索参数以及策略，起终点也可以用name构造
                                RouteParaOption para = new RouteParaOption()
                                        .startPoint(pt_start)
                                        .endPoint(pt_end)
                                        .busStrategyType(RouteParaOption.EBusStrategyType.bus_recommend_way);
                                try {
                                    BaiduMapRoutePlan.openBaiduMapTransitRoute(para, BaiduMapActivity.this);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //结束调启功能时调用finish方法以释放相关资源
                                BaiduMapRoutePlan.finish(BaiduMapActivity.this);

                                sDialog.cancel();

                                /*Intent intent = getPackageManager()
                                        .getLaunchIntentForPackage("com.baidu.BaiduMap");
                                if (intent != null) {
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(BaiduMapActivity.this, "你还没有安装百度地图", Toast.LENGTH_SHORT).show();
                                }*/


                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.cancel();


                            }
                        })


                        .show();

            }
        });


  /*      mPositionBean = event.getPositionBean();

        Log.d(TAG, "latitude=" + mPositionBean.mBDLocation.getLatitude());
        Log.d(TAG, "longitude=" + mPositionBean.mBDLocation.getLongitude());

        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(mPositionBean.mBDLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                //                .direction(100)
                .latitude(mPositionBean.mBDLocation.getLatitude())
                .longitude(mPositionBean.mBDLocation.getLongitude()).build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        */


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        EventBus.getDefault().unregister(this);//解除订阅

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


}
