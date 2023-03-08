package pro.javadev.bean;

public class ObjectCreationException extends RuntimeException {

    public ObjectCreationException(String message) {
        super(message);
    }

    public ObjectCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectCreationException(Throwable cause) {
        super(cause);
    }

}
