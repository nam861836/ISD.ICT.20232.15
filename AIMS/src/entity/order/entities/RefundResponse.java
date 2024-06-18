package entity.order.entities;

import java.util.Date;

public class RefundResponse {
    private String errorCode;
    private String transactionNo;
    private String transactionContent;
    private int amount;
    private Integer orderID;
    private Date createdAt;
    private String txnRef;



    public RefundResponse() {

    }


    public RefundResponse(String errorCode,  String transactionNo, String transactionContent, int amount, Integer orderID, Date createdAt, String txnRef) {
        this.errorCode = errorCode;
        this.transactionNo = transactionNo;
        this.transactionContent = transactionContent;
        this.amount = amount;
        this.orderID = orderID;
        this.createdAt = createdAt;
        this.txnRef = txnRef;

    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getTransactionContent() {
        return transactionContent;
    }

    public void setTransactionContent(String transactionContent) {
        this.transactionContent = transactionContent;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getTxnRef() {
        return txnRef;
    }

    public void setTxnRef(String txnRef) {
        this.txnRef = txnRef;
    }
}
