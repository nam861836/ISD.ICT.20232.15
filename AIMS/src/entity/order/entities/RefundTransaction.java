package entity.order.entities;

import utils.Utils;

public class RefundTransaction {


       private String trantype; // 02 03
       private String amount;
       private String order_id;
       private String transactionNo;
       private String trans_date;
       private String user;

    public RefundTransaction(String trantype, String amount, String order_id, String transactionNo, String trans_date, String user) {
        this.trantype = trantype;
        this.amount = amount;
        this.order_id = order_id;
        this.transactionNo = transactionNo;
        this.trans_date = trans_date;
        this.user = user;
    }


    public String getTrantype() {
        return trantype;
    }

    public void setTrantype(String trantype) {
        this.trantype = trantype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getTrans_date() {
        return trans_date;
    }

    public void setTrans_date(String trans_date) {
        this.trans_date = trans_date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
