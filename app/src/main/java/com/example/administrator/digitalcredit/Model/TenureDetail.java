package com.example.administrator.digitalcredit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TenureDetail {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("tenure_time")
@Expose
private Integer tenureTime;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Integer getTenureTime() {
return tenureTime;
}

public void setTenureTime(Integer tenureTime) {
this.tenureTime = tenureTime;
}

}