package entity.order.entities;

import java.util.Date;

public class DetailResponse {

    private String amount;

    private String orderInfo;

    private String bankCode;

    private String payDate;

    private String transactionId;

    private String transactionType;

    private String transactionStatus;

    private String promotionCode;

    private String promotionAmount;

    public DetailResponse() {

    }

    public DetailResponse(String amount, String orderInfo, String bankCode, String payDate, String transactionId, String transactionType, String transactionStatus, String promotionCode, String promotionAmount) {
        this.amount = amount;
        this.orderInfo = orderInfo;
        this.bankCode = bankCode;
        this.payDate = payDate;
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.promotionCode = promotionCode;
        this.promotionAmount = promotionAmount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getAmount() {
        return amount;
    }
    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
    public String getOrderInfo() {
        return orderInfo;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setPayDate(String payDate) {
        this.payDate = payDate;

    }
    public String getPayDate() {
        System.out.println("payDate: " + payDate);
        return payDate;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    public String getTransactionType() {
        return transactionType;
    }
    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
    public String getTransactionStatus() {
        return transactionStatus;
    }
    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }
    public String getPromotionCode() {
        return promotionCode;
    }
    public void setPromotionAmount(String promotionAmount) {
        this.promotionAmount = promotionAmount;
    }
    public String getPromotionAmount() {
        return promotionAmount;
    }
}
