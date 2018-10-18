package com.example.muhammadfahad.digitalcredit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppVersion {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("appVersion")
@Expose
private Double appVersion;
@SerializedName("appUrl")
@Expose
private String appUrl;
@SerializedName("appDesc")
@Expose
private String appDesc;
@SerializedName("releaseDate")
@Expose
private String releaseDate;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Double getAppVersion() {
return appVersion;
}

public void setAppVersion(Double appVersion) {
this.appVersion = appVersion;
}

public String getAppUrl() {
return appUrl;
}

public void setAppUrl(String appUrl) {
this.appUrl = appUrl;
}

public String getAppDesc() {
return appDesc;
}

public void setAppDesc(String appDesc) {
this.appDesc = appDesc;
}

public String getReleaseDate() {
return releaseDate;
}

public void setReleaseDate(String releaseDate) {
this.releaseDate = releaseDate;
}

}
