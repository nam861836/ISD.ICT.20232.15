package common.exception;

public class QueryException extends RuntimeException{

        public QueryException() {
        }

        public QueryException(String message) {
            super(message);
        }
}
