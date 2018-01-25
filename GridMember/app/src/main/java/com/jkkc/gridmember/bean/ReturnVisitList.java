package com.jkkc.gridmember.bean;

/**
 * Created by Guan on 2018/1/25.
 */

public class ReturnVisitList {


    /**
     * msg : success
     * code : 200
     * data : [{"address":"16栋4单元307","name":"徐浩","jibing":null,"phone":"13943187200"},{"address":"2区8栋3单元507","name":"贾淑华","jibing":null,"phone":"13159773017"},{"address":"3栋4单元102","name":"成云昌","jibing":null,"phone":"18584356873"},{"address":"3栋4单元102","name":"陈坤","jibing":null,"phone":"18584356585"},{"address":"201栋6单元314","name":"付志国","jibing":null,"phone":"15526671992"},{"address":"175栋7单元602","name":"李琴","jibing":null,"phone":"13844060185"},{"address":"175栋7单元602","name":"杨武","jibing":null,"phone":"13596038649"},{"address":"六区10栋2单元504","name":"张桂霞","jibing":null,"phone":"13596448920"},{"address":"199栋6单元417","name":"赵桂兰","jibing":null,"phone":"15517100625"}]
     */

    private String msg;
    private int code;
    private String data;



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
