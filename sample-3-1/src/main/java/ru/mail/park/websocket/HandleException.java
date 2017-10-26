package ru.mail.park.websocket;


/**
 * Created by Solovyev on 06/04/16.
 */
public class HandleException extends Exception {
    public HandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleException(String message) {
        super(message);
    }
}
