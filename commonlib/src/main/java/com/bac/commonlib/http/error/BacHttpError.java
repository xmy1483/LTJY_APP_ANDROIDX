package com.bac.commonlib.http.error;

/**
 * Created by Wjz on 2017/5/19.
 */

public class BacHttpError extends RuntimeException {

    public BacHttpError() {
        super();
    }

    public BacHttpError(String message) {
        super(message);
    }

    public BacHttpError(String message, Throwable cause) {
        super(message, cause);
    }
}
