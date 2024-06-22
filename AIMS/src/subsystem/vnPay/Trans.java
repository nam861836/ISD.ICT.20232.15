package subsystem.vnPay;

import entity.payment.PaymentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Trans {
    Map<String, String> response;
    public Trans(Map<String, String> response){
        this.response= response;
    }
    public PaymentTransaction makePaymentTransaction() throws ParseException {
        if (response == null) {
            return null;
        }

        // Create Payment transaction
        String errorCode = response.get("vnp_TransactionStatus");
        String cardType = response.get("vnp_CardType");

        String txnRef = response.get("vnp_TxnRef");
        String transactionId = response.get("vnp_TransactionNo");
        String transactionContent = response.get("vnp_OrderInfo");
        int amount = Integer.parseInt((String) response.get("vnp_Amount")) / 100;
        String createdAt = response.get("vnp_PayDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = dateFormat.parse(createdAt);
        PaymentTransaction trans = new
                PaymentTransaction(errorCode, transactionId, transactionContent, amount, date, cardType, txnRef);
        return trans;
    }
}
