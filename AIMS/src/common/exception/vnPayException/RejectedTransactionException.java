package common.exception.vnPayException;

import common.exception.PaymentException;

public class RejectedTransactionException extends PaymentException {
    public RejectedTransactionException() {
        super("ERROR: GD Hoàn trả bị từ chối");
    }
}
