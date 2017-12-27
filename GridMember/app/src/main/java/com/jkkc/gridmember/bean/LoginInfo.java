package com.jkkc.gridmember.bean;

/**
 * Created by Guan on 2017/12/27.
 */

public class LoginInfo {


    /**
     * code : 200
     * data : {"id":2,"phone":"18518030828","address":"爱是当你话健康","sex":"男","token":"9904ded5-6cb5-4690-9c93-8cc69ac37b18","name":"陈双飞","age":25}
     * msg : 登录成功！
     */

    private String code;
    private DataBean data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * id : 2
         * phone : 18518030828
         * address : 爱是当你话健康
         * sex : 男
         * token : 9904ded5-6cb5-4690-9c93-8cc69ac37b18
         * name : 陈双飞
         * age : 25
         */

        private int id;
        private String phone;
        private String address;
        private String sex;
        private String token;
        private String name;
        private int age;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
