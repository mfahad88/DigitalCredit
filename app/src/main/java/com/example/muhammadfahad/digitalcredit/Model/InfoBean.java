package com.example.muhammadfahad.digitalcredit.Model;

public class InfoBean {

    private String name;
    private String cnic;
    private String mobile;
    private int userId;
    private static InfoBean bean;

    public InfoBean() {
    }

    public static InfoBean getInstance() {
        if(bean==null){
            bean=new InfoBean();
        }
        return bean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static InfoBean getBean() {
        return bean;
    }

    public static void setBean(InfoBean bean) {
        InfoBean.bean = bean;
    }
}
