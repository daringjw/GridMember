package com.jkkc.gridmember.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jkkc.gridmember.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Guan on 2018/1/22.
 */

public class RefuseActivity extends AppCompatActivity {

    @BindView(R.id.etReason)
    EditText mEtReason;
    @BindView(R.id.btnConfirm)
    Button mBtnConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_refuse);
        ButterKnife.bind(this);


        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

    }


}
