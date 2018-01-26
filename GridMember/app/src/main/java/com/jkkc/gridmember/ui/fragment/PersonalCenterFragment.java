package com.jkkc.gridmember.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jkkc.gridmember.LoginActivity;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.LoginInfo;
import com.jkkc.gridmember.utils.PrefUtils;
import com.tencent.bugly.Bugly;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Guan on 2018/1/19.
 */

public class PersonalCenterFragment extends Fragment {


    private TextView mTvUserName;
    private TextView mTvVersion;
    private TextView mTvReturnVisitCount;
    private Button mBtnLogout;
    private TextView mTvAddress;
    private TextView mTvPhone;
    private RelativeLayout mRlVersion;
    private SweetAlertDialog mLogoutDialog;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_personal_center, null);

        mTvUserName = view.findViewById(R.id.tvUserName);
        mTvVersion = view.findViewById(R.id.tvVersion);
        mTvReturnVisitCount = view.findViewById(R.id.tvReturnVisitCount);
        mBtnLogout = view.findViewById(R.id.btnLogout);

        mTvAddress = view.findViewById(R.id.tvAddress);
        mTvPhone = view.findViewById(R.id.tvPhone);

        mRlVersion = view.findViewById(R.id.rlVersion);


        if (AppUtils.isAppDebug()) {
            mTvVersion.setText("测试版本 " + AppUtils.getAppVersionName() +
                    AppUtils.getAppVersionCode());
        } else {
            mTvVersion.setText("正式版本 " + AppUtils.getAppVersionName() +
                    AppUtils.getAppVersionCode());
        }

        mRlVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(),"检测升级",Toast.LENGTH_SHORT).show();

                if (AppUtils.isAppDebug()) {
                    //内测版本
                    Bugly.init(getActivity(), "8711747843", false);

                } else {
                    //正式版本
                    Bugly.init(getActivity(), "001e1b77fe", false);

                }

            }
        });


        String user_info = PrefUtils.getString(getActivity(), "user_info", null);
        if (!TextUtils.isEmpty(user_info)) {
            Gson gson = new Gson();
            LoginInfo loginInfo = gson.fromJson(user_info, LoginInfo.class);
            mTvUserName.setText(loginInfo.getData().getName());
            mTvAddress.setText(loginInfo.getData().getAddress());
            mTvPhone.setText(loginInfo.getData().getPhone());

        }


        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLogoutDialog = new SweetAlertDialog(getActivity(),
                        SweetAlertDialog.PROGRESS_TYPE);
                mLogoutDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                mLogoutDialog.setTitleText("用户正在退出");
                mLogoutDialog.setCancelable(false);
                mLogoutDialog.show();

                //退出
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "用户退出", Toast.LENGTH_SHORT).show();
                                mLogoutDialog.cancel();
                            }
                        });

                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("logout", true);
                        startActivity(intent);
                        getActivity().finish();

                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub


                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub

                    }
                });

            }
        });


        return view;

    }


}
