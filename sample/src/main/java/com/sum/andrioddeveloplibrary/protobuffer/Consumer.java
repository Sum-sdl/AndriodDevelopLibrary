package com.sum.andrioddeveloplibrary.protobuffer;

import java.io.Serializable;

/**
 * 客户
 */
public class Consumer implements Serializable {

    //ui操作状态
    public boolean isSelected;//选中状态
    public boolean isChooseEnable = true;//是否可以选中
    public boolean isSingleChoose = false;//是否是单选


    private Long id;

    private Long cid;

    private String name;

    private String shortName;

    private String contactName;

    private String contactTel;

    private String address;

    private Double addrLat;

    private Double addrLng;

    private Byte state;

    private Double debtAmount = 0.00;

    private int distance;//距离


    //本地查询条件
    //输入的关键字
    private String queryText;
    

    private Integer isSignin;
    private String signinTime;
    private Long[] routeId;//线路ID
    private Integer[] seq; //拜访序列

    private String createTime;
    private String updateTime;
    private String submitTime;

    private String showRemark = "";//备注1
    private String printRemark = "";//备注2
    private String consumerCode;//客户编码
    private String licence;//客户编号

    private Integer settleMethod = 1;//1现结，2挂账

    private String foodLicenseNumber; // 食品许可证

    private String maxDebtAmount;   // 客户最大欠款额度

    private String openWeChat;//0：未开通，1：已开通店管家

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public Long[] getRouteId() {
        return routeId;
    }

    public void setRouteId(Long[] routeId) {
        this.routeId = routeId;
    }

    public Integer[] getSeq() {
        return seq;
    }

    public void setSeq(Integer[] seq) {
        this.seq = seq;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getAddrLat() {
        return addrLat;
    }

    public void setAddrLat(Double addrLat) {
        this.addrLat = addrLat;
    }

    public Double getAddrLng() {
        return addrLng;
    }

    public void setAddrLng(Double addrLng) {
        this.addrLng = addrLng;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public Double getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(Double debtAmount) {
        this.debtAmount = debtAmount;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Integer getIsSignin() {
        if (isSignin == null) {
            isSignin = 0;
        }
        return isSignin;
    }

    public void setIsSignin(Integer isSignin) {
        this.isSignin = isSignin;
    }

    public String getSigninTime() {
        return signinTime;
    }

    public void setSigninTime(String signinTime) {
        this.signinTime = signinTime;
    }

    public String getPrintRemark() {
        return printRemark;
    }

    public void setPrintRemark(String printRemark) {
        this.printRemark = printRemark;
    }

    public String getShowRemark() {
        return showRemark;
    }

    public void setShowRemark(String showRemark) {
        this.showRemark = showRemark;
    }

    public Integer getSettleMethod() {
        return settleMethod;
    }

    public void setSettleMethod(Integer settleMethod) {
        this.settleMethod = settleMethod;
    }

    public String getFoodLicenseNumber() {
        return foodLicenseNumber;
    }

    public void setFoodLicenseNumber(String foodLicenseNumber) {
        this.foodLicenseNumber = foodLicenseNumber;
    }

    public String getMaxDebtAmount() {
        return maxDebtAmount;
    }

    public void setMaxDebtAmount(String maxDebtAmount) {
        this.maxDebtAmount = maxDebtAmount;
    }

    public boolean isMonthlySettle() {
        if (Integer.valueOf(2).equals(settleMethod)) {
            return true;
        } else {
            return false;
        }
    }

    public String getOpenWeChat() {
        return openWeChat;
    }

    public void setOpenWeChat(String openWeChat) {
        this.openWeChat = openWeChat;
    }

}