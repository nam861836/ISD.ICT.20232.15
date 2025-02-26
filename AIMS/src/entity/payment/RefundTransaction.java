//package entity.payment;
//
//import entity.db.AIMSDB;
//
//import java.sql.*;
//import java.util.Date;
//
//public class RefundTransaction {
//    private String errorCode;
//    private String transactionNo;
//    private String transactionContent;
//    private int amount;
//    private Integer orderID;
//    private Date createdAt;
//    private String txnRef;
//
//    public void setErrorCode(String errorCode) {
//        this.errorCode = errorCode;
//    }
//
//    public String getTransactionNo() {
//        return transactionNo;
//    }
//
//    public void setTransactionNo(String transactionNo) {
//        this.transactionNo = transactionNo;
//    }
//
//    public void setTransactionContent(String transactionContent) {
//        this.transactionContent = transactionContent;
//    }
//
//    public Integer getOrderID() {
//        return orderID;
//    }
//
//    public void setOrderID(Integer orderID) {
//        this.orderID = orderID;
//    }
//
//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Date createdAt) {
//        this.createdAt = createdAt;
//    }
//
//
//
//    public void setAmount(int amount) {
//        this.amount = amount;
//    }
//    public int getAmount(){
//        return amount;
//    }
//
//    public String getTxnRef() {
//        return txnRef;
//    }
//
//    public void setTxnRef(String txnRef) {
//        this.txnRef = txnRef;
//    }
//
//
//
//    public RefundTransaction(){}
//    public RefundTransaction(String errorCode, String transactionNo, String transactionContent,
//                              int amount, Date createdAt, String txnRef) {
//        super();
//        this.errorCode = errorCode;
//
//
//        this.txnRef = txnRef;
//        this.transactionNo = transactionNo;
//        this.transactionContent = transactionContent;
//        this.amount = amount;
//        this.createdAt = createdAt;
//    }
//
//
//    /**
//     * @return String
//     */
//    public String getErrorCode() {
//        return errorCode;
//    }
//
//    public String getTransactionContent() {
//        return transactionContent;
//    }
//
//    public void save(int orderId) {
//        this.orderID = orderId;
//        try {
//            Statement stm = AIMSDB.getConnection().createStatement();
//
//            String query = "INSERT INTO RefundTransaction ( orderID, createdAt, content, txnRef, amount, transactionNo) " +
//                    "VALUES (?, ?, ?, ?, ?, ?)";
//
//            try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(query)) {
//                preparedStatement.setInt(1, orderID);
//                preparedStatement.setDate(2, new java.sql.Date(createdAt.getTime()));
//                preparedStatement.setString(3, transactionContent);
//                preparedStatement.setString(4, txnRef);
//
//                preparedStatement.setInt(6, amount);
//                preparedStatement.setString(7, transactionNo);
//
//                preparedStatement.executeUpdate();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
