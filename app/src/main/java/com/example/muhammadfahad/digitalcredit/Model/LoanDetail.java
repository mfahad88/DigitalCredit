package com.example.muhammadfahad.digitalcredit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanDetail {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("customerId")
@Expose
private Integer customerId;
@SerializedName("amt")
@Expose
private Integer amt;
@SerializedName("loanCreatedDate")
@Expose
private String loanCreatedDate;
@SerializedName("loanDueDate")
@Expose
private String loanDueDate;
@SerializedName("amtPaid")
@Expose
private Integer amtPaid;
@SerializedName("loanStatus")
@Expose
private String loanStatus;
@SerializedName("lastPaidDate")
@Expose
private String lastPaidDate;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Integer getCustomerId() {
return customerId;
}

public void setCustomerId(Integer customerId) {
this.customerId = customerId;
}

public Integer getAmt() {
return amt;
}

public void setAmt(Integer amt) {
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

public Integer getAmtPaid() {
return amtPaid;
}

public void setAmtPaid(Integer amtPaid) {
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

    @Override
    public String toString() {
        return "LoanDetail{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", amt=" + amt +
                ", loanCreatedDate='" + loanCreatedDate + '\'' +
                ", loanDueDate='" + loanDueDate + '\'' +
                ", amtPaid=" + amtPaid +
                ", loanStatus='" + loanStatus + '\'' +
                ", lastPaidDate='" + lastPaidDate + '\'' +
                '}';
    }
}
