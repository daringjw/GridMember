package com.jkkc.gridmember.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.jkkc.gridmember.R;
import com.tencent.bugly.Bugly;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Guan on 2018/1/12.
 */

public class HomeActivity1 extends AppCompatActivity {

    @BindView(R.id.tvVersionCode)
    TextView mTvVersionCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home1);
        ButterKnife.bind(this);

        String isDebugStr = AppUtils.isAppDebug() ? "内测版本" : "正式版本";

        mTvVersionCode.setText("当前版本：" + isDebugStr +
                AppUtils.getAppVersionName() + "." +
                AppUtils.getAppVersionCode());


        if (AppUtils.isAppDebug()) {

            //内测版本
            Bugly.init(getApplicationContext(), "8711747843", false);

        } else {
            //正式版本
            Bugly.init(getApplicationContext(),"001e1b77fe",false);


        }


    }


}

