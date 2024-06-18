package common.exception;

;


public class ProductNotAvailableException extends AimsException {

    private static final long serialVersionUID = 1091337136123906298L;

    public ProductNotAvailableException() {

    }

    public ProductNotAvailableException(String message) {
        super(message);
    }

}