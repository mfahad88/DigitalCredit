
package com.example.administrator.digitalcredit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("total_amt")
    @Expose
    private Float totalAmt;
    @SerializedName("discount_amt")
    @Expose
    private Integer discountAmt;
    @SerializedName("fk_promo_id")
    @Expose
    private Integer fkPromoId;
    @SerializedName("fk_distributer_id")
    @Expose
    private Integer fkDistributerId;
    @SerializedName("fk_loan_id")
    @Expose
    private Integer fkLoanId;
    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("modified_on")
    @Expose
    private Object modifiedOn;
    @SerializedName("modified_by")
    @Expose
    private Integer modifiedBy;
    @SerializedName("totalItem")
    @Expose
    private Integer totalItem;
    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cnic")
    @Expose
    private String cnic;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Float getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Float totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Integer getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(Integer discountAmt) {
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Object modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Integer getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Integer totalItem) {
        this.totalItem = totalItem;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
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

    @Override
    public String toString() {
        return "Order{" +
                "userId=" + userId +
                ", orderId=" + orderId +
                ", totalAmt=" + totalAmt +
                ", discountAmt=" + discountAmt +
                ", fkPromoId=" + fkPromoId +
                ", fkDistributerId=" + fkDistributerId +
                ", fkLoanId=" + fkLoanId +
                ", orderType='" + orderType + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", modifiedOn=" + modifiedOn +
                ", modifiedBy=" + modifiedBy +
                ", totalItem=" + totalItem +
                ", mobile_no='" + mobile_no + '\'' +
                ", name='" + name + '\'' +
                ", cnic='" + cnic + '\'' +
                '}';
    }
}
