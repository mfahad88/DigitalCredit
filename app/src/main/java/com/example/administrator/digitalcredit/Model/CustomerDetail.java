package com.example.administrator.digitalcredit.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerDetail {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_mobile_no")
    @Expose
    private String userMobileNo;
    @SerializedName("user_cnic")
    @Expose
    private String userCnic;
    @SerializedName("user_channel_id")
    @Expose
    private String userChannelId;
    @SerializedName("income")
    @Expose
    private Integer income;
    @SerializedName("user_status")
    @Expose
    private String userStatus;
    @SerializedName("available_Balance")
    @Expose
    private Double availableBalance;
    @SerializedName("base_scrore")
    @Expose
    private Integer baseScrore;
    @SerializedName("behavior_score")
    @Expose
    private Integer behaviorScore;
    @SerializedName("base_score_flag")
    @Expose
    private Integer baseScoreFlag;
    @SerializedName("behavior_score_flag")
    @Expose
    private Integer behaviorScoreFlag;
    @SerializedName("assigned_amount_Limit")
    @Expose
    private Double assignedAmountLimit;
    @SerializedName("consumed_Limit")
    @Expose
    private Integer consumedLimit;
    @SerializedName("available_Amount_Limit")
    @Expose
    private Double availableAmountLimit;
    @SerializedName("dbr_value")
    @Expose
    private Integer dbrValue;
    @SerializedName("dbr_value_flag")
    @Expose
    private Integer dbrValueFlag;
    @SerializedName("user_type")
    @Expose
    private Integer userType;
    @SerializedName("availLoan")
    @Expose
    private Double availLoan;
    @SerializedName("user_name")
    @Expose
    private String userName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    public String getUserCnic() {
        return userCnic;
    }

    public void setUserCnic(String userCnic) {
        this.userCnic = userCnic;
    }

    public String getUserChannelId() {
        return userChannelId;
    }

    public void setUserChannelId(String userChannelId) {
        this.userChannelId = userChannelId;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Integer getBaseScrore() {
        return baseScrore;
    }

    public void setBaseScrore(Integer baseScrore) {
        this.baseScrore = baseScrore;
    }

    public Integer getBehaviorScore() {
        return behaviorScore;
    }

    public void setBehaviorScore(Integer behaviorScore) {
        this.behaviorScore = behaviorScore;
    }

    public Integer getBaseScoreFlag() {
        return baseScoreFlag;
    }

    public void setBaseScoreFlag(Integer baseScoreFlag) {
        this.baseScoreFlag = baseScoreFlag;
    }

    public Integer getBehaviorScoreFlag() {
        return behaviorScoreFlag;
    }

    public void setBehaviorScoreFlag(Integer behaviorScoreFlag) {
        this.behaviorScoreFlag = behaviorScoreFlag;
    }

    public Double getAssignedAmountLimit() {
        return assignedAmountLimit;
    }

    public void setAssignedAmountLimit(Double assignedAmountLimit) {
        this.assignedAmountLimit = assignedAmountLimit;
    }

    public Integer getConsumedLimit() {
        return consumedLimit;
    }

    public void setConsumedLimit(Integer consumedLimit) {
        this.consumedLimit = consumedLimit;
    }

    public Double getAvailableAmountLimit() {
        return availableAmountLimit;
    }

    public void setAvailableAmountLimit(Double availableAmountLimit) {
        this.availableAmountLimit = availableAmountLimit;
    }

    public Integer getDbrValue() {
        return dbrValue;
    }

    public void setDbrValue(Integer dbrValue) {
        this.dbrValue = dbrValue;
    }

    public Integer getDbrValueFlag() {
        return dbrValueFlag;
    }

    public void setDbrValueFlag(Integer dbrValueFlag) {
        this.dbrValueFlag = dbrValueFlag;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Double getAvailLoan() {
        return availLoan;
    }

    public void setAvailLoan(Double availLoan) {
        this.availLoan = availLoan;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "CustomerDetail{" +
                "userId=" + userId +
                ", userMobileNo='" + userMobileNo + '\'' +
                ", userCnic='" + userCnic + '\'' +
                ", userChannelId='" + userChannelId + '\'' +
                ", income=" + income +
                ", userStatus='" + userStatus + '\'' +
                ", availableBalance=" + availableBalance +
                ", baseScrore=" + baseScrore +
                ", behaviorScore=" + behaviorScore +
                ", baseScoreFlag=" + baseScoreFlag +
                ", behaviorScoreFlag=" + behaviorScoreFlag +
                ", assignedAmountLimit=" + assignedAmountLimit +
                ", consumedLimit=" + consumedLimit +
                ", availableAmountLimit=" + availableAmountLimit +
                ", dbrValue=" + dbrValue +
                ", dbrValueFlag=" + dbrValueFlag +
                ", userType=" + userType +
                ", availLoan=" + availLoan +
                ", userName='" + userName + '\'' +
                '}';
    }
}
