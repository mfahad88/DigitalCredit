package com.example.administrator.digitalcredit.Model;

public class SessionBean {
    private static SessionBean bean;
    private String key;
    private String value;

    private SessionBean() {

    }

    public static SessionBean getInstance() {
        if(bean==null){
            bean=new SessionBean();
        }
        return bean;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SessionBean{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
