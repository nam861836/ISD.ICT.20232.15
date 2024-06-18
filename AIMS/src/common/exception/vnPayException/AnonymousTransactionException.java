package common.exception.vnPayException;

import common.exception.PaymentException;



public class AnonymousTransactionException extends PaymentException {
    public AnonymousTransactionException() {
        super("ERROR: Giao dịch bị nghi ngờ gian lận");
    }
}
