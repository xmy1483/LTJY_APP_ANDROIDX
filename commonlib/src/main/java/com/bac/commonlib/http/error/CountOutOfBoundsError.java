package com.bac.commonlib.http.error;

/**
 * Created by Wjz on 2017/5/20.
 */

public class CountOutOfBoundsError extends RuntimeException {
    public CountOutOfBoundsError() {
    }

    public CountOutOfBoundsError(String message) {
        super(message);
    }

    public CountOutOfBoundsError(String message, Throwable cause) {
        super(message, cause);
    }

    public CountOutOfBoundsError(Throwable cause) {
        super(cause);
    }
}
