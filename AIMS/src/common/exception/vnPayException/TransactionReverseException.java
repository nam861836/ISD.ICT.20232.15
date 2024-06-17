package common.exception.vnPayException;

import common.exception.PaymentException;



public class TransactionReverseException extends PaymentException {

    public TransactionReverseException() {
        super("ERROR: Giao dịch đảo (Khách hàng đã bị trừ tiền tại Ngân hàng nhưng GD chưa thành công ở VNPAY)!");
    }

}
