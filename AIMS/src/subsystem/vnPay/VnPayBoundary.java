package subsystem.vnPay;

import com.google.gson.Gson;
import entity.order.Order;
import entity.order.entities.DetailResponse;
import entity.order.entities.RefundResponse;
import entity.payment.PaymentTransaction;
import entity.order.entities.RefundTransaction;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class VnPayBoundary {

    public String generatePayUrl(int amount, String content) throws IOException {
        PayRequestVNPay payRequestVNPay = new PayRequestVNPay(amount, content);
        return payRequestVNPay.generatePayUrl();
    }

    public RefundResponse refund(RefundTransaction refundTransaction) throws IOException {
        RefundVNPay refundVNPay = new RefundVNPay(refundTransaction);
        return refundVNPay.refund();
    }

    public DetailResponse getDetailTransaction(Order order) throws IOException {
        PayResponseVNPay payResponseVNPay = new PayResponseVNPay(order);
        return payResponseVNPay.getDetailTransaction();
    }

    public PaymentTransaction makePaymentTransaction(Map<String, String> response) throws ParseException{
        Trans trans = new Trans(response);
        return trans.makePaymentTransaction();
    }

}
