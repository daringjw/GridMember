package com.jkkc.gridmember.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jkkc.gridmember.R;
import com.jkkc.gridmember.utils.PrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Guan on 2018/1/5.
 */

public class PersonalInfoActivity extends AppCompatActivity {

    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvAge)
    TextView mTvAge;
    @BindView(R.id.tvData)
    TextView mTvData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personalinfo);
        ButterKnife.bind(this);

        String name = PrefUtils.getString(getApplicationContext(), "Name", null);
        String age = PrefUtils.getString(getApplicationContext(), "Age", null);
        String data = PrefUtils.getString(getApplicationContext(), "data", null);

        mTvName.setText(name);
        mTvAge.setText(age);
        mTvData.setText(data);


    }


}
