package com.jkkc.gridmember.bean;

/**
 * Created by Guan on 2018/1/25.
 */

public class ReturnVisitListData {


    /**
     * address : 16栋4单元307
     * jibing : null
     * name : 徐浩
     * phone : 13943187200
     */

    private String address;
    private Object jibing;
    private String name;
    private String phone;
    public String day;
    public String oldId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getJibing() {
        return jibing;
    }

    public void setJibing(Object jibing) {
        this.jibing = jibing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
