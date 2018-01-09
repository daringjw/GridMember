package com.jkkc.gridmember.manager;

import com.jkkc.gridmember.bean.CallerBean;

/**
 * Created by Guan on 2018/1/9.
 */

public class CallerInfoManager {


    private static CallerInfoManager mInstance;

    private CallerBean mCallerBean;

    public CallerBean getCallerBean() {

        if (mCallerBean == null) {

            mCallerBean = new CallerBean();
        }

        return mCallerBean;
    }

    public void setCallerBean(CallerBean callerBean) {

        mCallerBean = callerBean;

    }


    public static CallerInfoManager getInstance() {
        if (mInstance == null) {
            synchronized (CallerInfoManager.class) {
                if (mInstance == null) {
                    mInstance = new CallerInfoManager();
                }
            }
        }

        return mInstance;
    }

}
