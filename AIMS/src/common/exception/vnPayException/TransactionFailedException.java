package common.exception.vnPayException;

import common.exception.PaymentException;

public class TransactionFailedException extends PaymentException {

    public TransactionFailedException() {
        super("ERROR: Giao dịch thất bại!");
    }

}
