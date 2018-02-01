package com.jkkc.gridmember.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jkkc.gridmember.R;
import com.jkkc.gridmember.ui.fragment.PageFragment;

import java.util.ArrayList;

/**
 * Created by Guan on 2018/2/1.
 */

public class PicDetailActivity extends AppCompatActivity {

    private static final String TAG = PicDetailActivity.class.getSimpleName();
    private ArrayList<String> mImgList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pic_detail);

        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        mImgList = intent.getStringArrayListExtra("imgList");
        if (mImgList != null) {
            Log.d(TAG, mImgList.get(0).toString());
            Log.d(TAG, mImgList.size() + "张");

            //Fragment+ViewPager+FragmentViewPager组合的使用
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
            MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                    this);
            viewPager.setAdapter(adapter);

            //TabLayout
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);

        }


    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public final int COUNT = mImgList.size();
        private String[] titles = new String[]{"Tab1", "Tab2", "Tab3", "Tab4", "Tab5"};
        private Context context;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;


        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "图片" + (position + 1);

        }
    }


}
