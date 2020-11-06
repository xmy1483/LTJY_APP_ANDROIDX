package com.bac.commonlib.http.error;

/**
 * Created by Wjz on 2017/5/20.
 */

public class AuthenticateError extends RuntimeException {
    public AuthenticateError() {
    }

    public AuthenticateError(String message) {
        super(message);
    }

    public AuthenticateError(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticateError(Throwable cause) {
        super(cause);
    }


}
