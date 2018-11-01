package com.example.administrator.digitalcredit.Model;

public class MobileBean {
    private int user_id;
    private int cat_id;
    private int rec_id;
    private String name;
    private String value;

    public MobileBean(int user_id, int cat_id, int rec_id, String name, String value) {
        this.user_id = user_id;
        this.cat_id = cat_id;
        this.rec_id = rec_id;
        this.name = name;
        this.value = value;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getRec_id() {
        return rec_id;
    }

    public void setRec_id(int rec_id) {
        this.rec_id = rec_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MobileBean{" +
                "user_id=" + user_id +
                ", cat_id=" + cat_id +
                ", rec_id=" + rec_id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
