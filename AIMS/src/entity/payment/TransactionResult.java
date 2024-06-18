package entity.payment;

public class TransactionResult {
    private String result;
    private  String message;
    public TransactionResult(){}
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
