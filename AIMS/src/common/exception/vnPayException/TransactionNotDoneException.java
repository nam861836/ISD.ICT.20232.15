package common.exception.vnPayException;

import common.exception.PaymentException;



public class TransactionNotDoneException extends PaymentException {
    public TransactionNotDoneException() {
        super("ERROR: Giao dịch chưa hoàn tất!");
    }
}
