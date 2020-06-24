package net.peihuan.blogapi.exception;

public class RequestException extends RuntimeException {
    public RequestException(String message) {
        super(message);
    }

    public RequestException() {
    }
}
