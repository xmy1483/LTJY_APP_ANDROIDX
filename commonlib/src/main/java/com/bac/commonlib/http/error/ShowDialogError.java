package com.bac.commonlib.http.error;

/**
 * Created by Wjz on 2017/5/20.
 */

public class ShowDialogError extends RuntimeException {
    public ShowDialogError() {

    }

    public ShowDialogError(String message) {
        super(message);
    }

    public ShowDialogError(String message, Throwable cause) {
        super(message, cause);
    }

    public ShowDialogError(Throwable cause) {
        super(cause);
    }

}
