package subsystem;

import common.exception.PaymentException;
import entity.order.Order;
import entity.order.entities.DetailResponse;
import entity.order.entities.RefundTransaction;
import entity.payment.PaymentTransaction;
import entity.order.entities.RefundResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * SOLID: Khó mở rộng khi cần thêm phương thức thanh toán ko cần lấy URL
 * @author ntvu
 */
public interface VnPayInterface {


    String generatePayUrl(int amount, String contents)
            throws  IOException;

    public DetailResponse getDetailTransaction(Order order) throws IOException;
    RefundResponse refund(RefundTransaction refundTransaction) throws PaymentException, IOException;
    PaymentTransaction
    makePaymentTransaction(Map<String, String> response) throws ParseException;
}
