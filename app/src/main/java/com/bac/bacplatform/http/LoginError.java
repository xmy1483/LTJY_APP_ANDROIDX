package com.bac.bacplatform.http;

/**
 * Created by guke on 2018/2/2.
 */

class LoginError extends RuntimeException {
    public LoginError() {
    }
    public LoginError(String methodname) {
super(methodname);
    }
}
