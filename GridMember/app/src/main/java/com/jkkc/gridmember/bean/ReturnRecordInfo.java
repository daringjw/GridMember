package com.jkkc.gridmember.bean;

import java.util.List;

/**
 * Created by Guan on 2018/1/31.
 */

public class ReturnRecordInfo {


    /**
     * msg : success
     * data : [{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"},{"address":"吉林省长春市二道区经开一区16栋4单元307","name":"徐浩","jibing":null,"returnVisitDate":"1/31/18 11:29 AM","gridMemberId":2,"phone":"13943187200","filePath":"null","imgPath":"/files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png","gridMemberName":"陈双飞"}]
     * code : 200
     */

    private String msg;
    private String code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * address : 吉林省长春市二道区经开一区16栋4单元307
         * name : 徐浩
         * jibing : null
         * returnVisitDate : 1/31/18 11:29 AM
         * gridMemberId : 2
         * phone : 13943187200
         * filePath : null
         * imgPath : /files/gridMemberApp/7e13e148ae69793aa353ce9c7e0d4ca7.png
         * gridMemberName : 陈双飞
         */

        private String address;
        private String name;
        private Object jibing;
        private String returnVisitDate;
        private int gridMemberId;
        private String phone;
        private String filePath;
        private String imgPath;
        private String gridMemberName;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getJibing() {
            return jibing;
        }

        public void setJibing(Object jibing) {
            this.jibing = jibing;
        }

        public String getReturnVisitDate() {
            return returnVisitDate;
        }

        public void setReturnVisitDate(String returnVisitDate) {
            this.returnVisitDate = returnVisitDate;
        }

        public int getGridMemberId() {
            return gridMemberId;
        }

        public void setGridMemberId(int gridMemberId) {
            this.gridMemberId = gridMemberId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getGridMemberName() {
            return gridMemberName;
        }

        public void setGridMemberName(String gridMemberName) {
            this.gridMemberName = gridMemberName;
        }
    }
}
