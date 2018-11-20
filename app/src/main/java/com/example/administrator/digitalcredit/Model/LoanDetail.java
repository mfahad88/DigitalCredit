package com.example.administrator.digitalcredit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanDetail {

@SerializedName("id")
@Expose
private int id;
@SerializedName("customerId")
@Expose
private int customerId;
@SerializedName("amt")
@Expose
private float amt;
@SerializedName("loanCreatedDate")
@Expose
private String loanCreatedDate;
@SerializedName("loanDueDate")
@Expose
private String loanDueDate;
@SerializedName("amtPaid")
@Expose
private float amtPaid;
@SerializedName("loanStatus")
@Expose
private String loanStatus;
@SerializedName("lastPaidDate")
@Expose
private String lastPaidDate;

@SerializedName("remainingAmt")
@Expose
private float remainingAmt;

@SerializedName("loanFees")
@Expose
private int loanFees;

@SerializedName("partialPayment")
@Expose
private boolean partialPayment;
@SerializedName("fk_user_distributerId")
@Expose
private int fk_user_distributerId;
@SerializedName("orderId")
@Expose
private int orderId;
@SerializedName("order_status")
@Expose
private String order_status;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public float getAmt() {
        return amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }

    public String getLoanCreatedDate() {
        return loanCreatedDate;
    }

    public void setLoanCreatedDate(String loanCreatedDate) {
        this.loanCreatedDate = loanCreatedDate;
    }

    public String getLoanDueDate() {
        return loanDueDate;
    }

    public void setLoanDueDate(String loanDueDate) {
        this.loanDueDate = loanDueDate;
    }

    public float getAmtPaid() {
        return amtPaid;
    }

    public void setAmtPaid(float amtPaid) {
        this.amtPaid = amtPaid;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLastPaidDate() {
        return lastPaidDate;
    }

    public void setLastPaidDate(String lastPaidDate) {
        this.lastPaidDate = lastPaidDate;
    }

    public float getRemainingAmt() {
        return remainingAmt;
    }

    public void setRemainingAmt(float remainingAmt) {
        this.remainingAmt = remainingAmt;
    }

    public int getLoanFees() {
        return loanFees;
    }

    public void setLoanFees(int loanFees) {
        this.loanFees = loanFees;
    }

    public boolean isPartialPayment() {
        return partialPayment;
    }

    public void setPartialPayment(boolean partialPayment) {
        this.partialPayment = partialPayment;
    }

    public int getFk_user_distributerId() {
        return fk_user_distributerId;
    }

    public void setFk_user_distributerId(int fk_user_distributerId) {
        this.fk_user_distributerId = fk_user_distributerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
