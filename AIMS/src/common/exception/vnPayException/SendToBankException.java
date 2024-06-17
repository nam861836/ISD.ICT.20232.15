package common.exception.vnPayException;

import common.exception.PaymentException;



public class SendToBankException extends PaymentException {
    public SendToBankException() {
        super("ERROR: VNPAY đã gửi yêu cầu hoàn tiền sang Ngân hàng (GD hoàn tiền)");
    }
}
