package com.example.muhammadfahad.digitalcredit.Model;


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
    private Integer availableBalance;
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
    private Integer assignedAmountLimit;
    @SerializedName("consumed_Limit")
    @Expose
    private Integer consumedLimit;
    @SerializedName("available_Amount_Limit")
    @Expose
    private Integer availableAmountLimit;
    @SerializedName("dbr_value")
    @Expose
    private Integer dbrValue;
    @SerializedName("dbr_value_flag")
    @Expose
    private Integer dbrValueFlag;

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

    public Integer getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Integer availableBalance) {
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

    public Integer getAssignedAmountLimit() {
        return assignedAmountLimit;
    }

    public void setAssignedAmountLimit(Integer assignedAmountLimit) {
        this.assignedAmountLimit = assignedAmountLimit;
    }

    public Integer getConsumedLimit() {
        return consumedLimit;
    }

    public void setConsumedLimit(Integer consumedLimit) {
        this.consumedLimit = consumedLimit;
    }

    public Integer getAvailableAmountLimit() {
        return availableAmountLimit;
    }

    public void setAvailableAmountLimit(Integer availableAmountLimit) {
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

}
