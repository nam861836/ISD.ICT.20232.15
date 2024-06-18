package common.exception.vnPayException;

import common.exception.*;

import java.util.HashMap;
import java.util.Map;

public class TransactionExceptionHolder {
    private static final TransactionExceptionHolder instance = new TransactionExceptionHolder();

    private Map<String, PaymentException> exceptions = new HashMap<>();

    private TransactionExceptionHolder() {
        // Khởi tạo các exception và đưa vào Mape

        exceptions.put("91", new TransactionNotDoneException());
        exceptions.put("02", new TransactionFailedException());
        exceptions.put("03", new TransactionReverseException());
        exceptions.put("94", new ProcessingException());
        exceptions.put("09", new RejectedTransactionException());
        exceptions.put("95", new SendToBankException());
        exceptions.put("97", new AnonymousTransactionException());
        exceptions.put("99", new PaymentException("Có lỗi khác xảy ra, vui lòng thử lại sau!"));
    }

    public static TransactionExceptionHolder getInstance() {
        return instance;
    }

    public PaymentException getException(String code) {
        return exceptions.get(code);
    }
}
