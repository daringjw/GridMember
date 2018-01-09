package com.jkkc.gridmember.event;

import com.jkkc.gridmember.bean.PositionBean;

/**
 * Created by Guan on 2018/1/9.
 */

public class PositionEvent {

    private PositionBean mPositionBean;

    public void setPositionBean(PositionBean positionBean){

        this.mPositionBean = positionBean;
    }

    public PositionBean getPositionBean(){

        return  mPositionBean;
    }


}
