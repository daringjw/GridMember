package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.PositionBean;
import com.jkkc.gridmember.event.PositionEvent;

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


        mTvBack = (TextView) findViewById(R.id.tvBack);
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(PositionEvent event) {

        mPositionBean = event.getPositionBean();

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

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        //        mCurrentMarker = BitmapDescriptorFactory
        //                .fromResource(R.drawable.icon_geo);
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_location_3);

        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING,
                true, mCurrentMarker);

        mBaiduMap.setMyLocationConfiguration(config);

        //导航
        mTvGuide = (TextView) findViewById(R.id.tvGuide);
        mTvGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new SweetAlertDialog(BaiduMapActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("导航?")
                        .setContentText("进入百度地图进行导航")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                //确定，进入百度地图进行导航
                                Intent intent = getPackageManager().getLaunchIntentForPackage("com.baidu.BaiduMap");
                                if (intent != null) {
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(BaiduMapActivity.this, "你还没有安装百度地图", Toast.LENGTH_SHORT).show();
                                }


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
