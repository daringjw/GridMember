package com.jkkc.gridmember.manager;

import com.jkkc.gridmember.bean.PositionBean;

/**
 * Created by Guan on 2018/1/8.
 */

public class PositionManager {

    private static PositionManager mInstance;

    private PositionBean mPositionBean;

    public PositionBean getPositionBean() {

        if (mPositionBean == null) {

            mPositionBean = new PositionBean();
        }

        return mPositionBean;
    }

    public void setPositionBean(PositionBean positionBean) {

        mPositionBean = positionBean;

    }


    public static PositionManager getInstance() {
        if (mInstance == null) {
            synchronized (PositionManager.class) {
                if (mInstance == null) {
                    mInstance = new PositionManager();
                }
            }
        }

        return mInstance;
    }


}
