package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jkkc.gridmember.R;

/**
 * Created by Guan on 2018/1/9.
 */

public class TimingActivity extends AppCompatActivity{

    private ImageView mIvHelp;
    private ImageView mIvStartoff;
    private LinearLayout mLlStartoff;
    private LinearLayout mLlHelp;
    private ImageView mIvHelpGuide;
    private ImageView mIvStartoffGuide;
    private ImageView mIvBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timing);

        mIvHelp = (ImageView) findViewById(R.id.ivHelp);
        mIvStartoff = (ImageView) findViewById(R.id.ivStartoff);
        mLlHelp = (LinearLayout) findViewById(R.id.llHelp);
        mLlStartoff = (LinearLayout) findViewById(R.id.llStartoff);
        mIvHelpGuide = (ImageView) findViewById(R.id.ivHelpGuide);
        mIvStartoffGuide = (ImageView) findViewById(R.id.ivStartoffGuide);
        mIvBack = (ImageView) findViewById(R.id.ivBack);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        mIvStartoffGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),BaiduMapActivity.class));

            }
        });


        mIvHelpGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),BaiduMapActivity.class));
            }
        });


        mIvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIvStartoff.setImageResource(R.mipmap.startoff_nor);
                mIvHelp.setImageResource(R.mipmap.help_pre);
                mLlHelp.setVisibility(View.VISIBLE);
                mLlStartoff.setVisibility(View.GONE);

            }
        });



        mIvStartoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIvStartoff.setImageResource(R.mipmap.startoff_pre);
                mIvHelp.setImageResource(R.mipmap.help_nor);
                mLlHelp.setVisibility(View.GONE);
                mLlStartoff.setVisibility(View.VISIBLE);


            }
        });



    }
}
