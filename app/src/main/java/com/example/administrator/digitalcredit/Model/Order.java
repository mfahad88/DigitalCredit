package com.example.administrator.digitalcredit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {


private Integer userId;

private Double totalAmt;

private Double discountAmt;

private Integer fkPromoId;

private Integer fkDistributerId;

private Integer fkLoanId;

private String orderType;

private String orderStatus;

public Integer getUserId() {
return userId;
}

public void setUserId(Integer userId) {
this.userId = userId;
}

public Double getTotalAmt() {
return totalAmt;
}

public void setTotalAmt(Double totalAmt) {
this.totalAmt = totalAmt;
}

public Double getDiscountAmt() {
return discountAmt;
}

public void setDiscountAmt(Double discountAmt) {
this.discountAmt = discountAmt;
}

public Integer getFkPromoId() {
return fkPromoId;
}

public void setFkPromoId(Integer fkPromoId) {
this.fkPromoId = fkPromoId;
}

public Integer getFkDistributerId() {
return fkDistributerId;
}

public void setFkDistributerId(Integer fkDistributerId) {
this.fkDistributerId = fkDistributerId;
}

public Integer getFkLoanId() {
return fkLoanId;
}

public void setFkLoanId(Integer fkLoanId) {
this.fkLoanId = fkLoanId;
}

public String getOrderType() {
return orderType;
}

public void setOrderType(String orderType) {
this.orderType = orderType;
}

public String getOrderStatus() {
return orderStatus;
}

public void setOrderStatus(String orderStatus) {
this.orderStatus = orderStatus;
}

}