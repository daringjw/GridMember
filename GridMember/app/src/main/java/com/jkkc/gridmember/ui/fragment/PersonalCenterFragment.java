package com.jkkc.gridmember.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jkkc.gridmember.LoginActivity;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.utils.PrefUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Guan on 2018/1/19.
 */

public class PersonalCenterFragment extends Fragment {

    private Button mBtnLogout;

    private SweetAlertDialog mPDialog;
    private TextView mTvUserLoginInfo;
    private String mUser_info;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_personal_center, null);

        mTvUserLoginInfo = view.findViewById(R.id.tvUserLoginInfo);
        mUser_info = PrefUtils.getString(getActivity(), "user_info", null);
        if (!TextUtils.isEmpty(mUser_info)) {

            mTvUserLoginInfo.setText(mUser_info);

        }


        mBtnLogout = view.findViewById(R.id.btnLogout);
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mPDialog == null) {
                    mPDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                }
                mPDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                mPDialog.setTitleText("用户正在退出...");
                mPDialog.setCancelable(true);
                mPDialog.show();


                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Log.d("PersonalCenterFragment", "环信账号退出");

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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mPDialog.dismiss();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();


                    }

                }, 1000);
            }
        });

        return view;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPDialog != null) {
            mPDialog.dismiss();
        }
    }
}
