package com.jkkc.gridmember.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkkc.gridmember.R;

/**
 * Created by Guan on 2018/1/26.
 * <p>
 * 回访记录详情
 */

public class ReturnVisitRecordActivity extends AppCompatActivity {

    private Button mBtnTakePic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_return_visit_record);

        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        String oldName = getIntent().getStringExtra("oldName");
        TextView tvOldName = (TextView) findViewById(R.id.tvOldName);
        tvOldName.setText(oldName);

        mBtnTakePic = (Button) findViewById(R.id.btnTakePic);
        mBtnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });




    }
}
