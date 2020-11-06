package com.bac.commonlib.http.error;

/**
 * Created by Wjz on 2017/5/20.
 */

public class ShowToastError extends RuntimeException {
    public ShowToastError() {
    }

    public ShowToastError(String message) {
        super(message);
    }

    public ShowToastError(String message, Throwable cause) {
        super(message, cause);
    }

    public ShowToastError(Throwable cause) {
        super(cause);
    }
}
