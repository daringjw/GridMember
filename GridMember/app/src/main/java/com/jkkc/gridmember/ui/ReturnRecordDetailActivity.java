package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.ReturnRecordInfo;
import com.jkkc.gridmember.utils.PrefUtils;

import java.util.List;

/**
 * Created by Guan on 2018/1/31.
 */

public class ReturnRecordDetailActivity extends AppCompatActivity {

    private static final String TAG = ReturnRecordDetailActivity.class.getSimpleName();
    private Intent mIntent;
    private List<ReturnRecordInfo.DataBean> mReturnDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_return_record_detail);

        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        mIntent = getIntent();
        int detail_position = mIntent.getIntExtra("detail_position", 0);
        Log.d(TAG, detail_position + "");

        String result = PrefUtils.getString(getApplicationContext(), "return_record_list", null);
        if (!TextUtils.isEmpty(result)) {
            Gson gson1 = new Gson();
            ReturnRecordInfo returnRecordInfo = gson1.fromJson(result, ReturnRecordInfo.class);
            mReturnDatas = returnRecordInfo.getData();
        }

        /**
         "address": "吉林省长春市二道区经开一区16栋4单元307",
         "filePath": "null",
         "gridMemberId": 2,
         "gridMemberName": "陈双飞",
         "imgPath": "/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png",
         "name": "徐浩",
         "phone": "13943187200",
         "returnVisitDate": "2018-01-31 11:29:54"
         */
        ReturnRecordInfo.DataBean dataBean = mReturnDatas.get(detail_position);
        TextView tvOldAddress = (TextView) findViewById(R.id.tvOldAddress);
        TextView tvOldName = (TextView) findViewById(R.id.tvOldName);
        TextView tvOldPhoneNumber = (TextView) findViewById(R.id.tvOldPhoneNumber);
        TextView tvReturnDateTime = (TextView) findViewById(R.id.tvReturnDateTime);
        tvOldAddress.setText(dataBean.getAddress());
        tvOldName.setText(dataBean.getName());
        tvOldPhoneNumber.setText(dataBean.getPhone());
        tvReturnDateTime.setText(dataBean.getReturnVisitDate());




    }
}
