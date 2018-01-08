package com.jkkc.gridmember.manager;

import com.jkkc.gridmember.bean.LoginInfo;

/**
 * Created by Guan on 2018/1/8.
 */

public class UserInfoManager {

    private static UserInfoManager mInstance;

    private LoginInfo mLoginInfo;

    public LoginInfo getLoginInfo() {

        if (mLoginInfo == null) {

            mLoginInfo = new LoginInfo();
        }

        return mLoginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {

        mLoginInfo = loginInfo;

    }


    public static UserInfoManager getInstance() {
        if (mInstance == null) {
            synchronized (UserInfoManager.class) {
                if (mInstance == null) {
                    mInstance = new UserInfoManager();
                }
            }
        }

        return mInstance;
    }


}
