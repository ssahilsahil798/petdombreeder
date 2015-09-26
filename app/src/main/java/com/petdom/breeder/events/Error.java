package com.petdom.breeder.events;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class Error {
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_INTERNET_CONNECTION = 1;
    public static final int TYPE_INVALID_RESPONSE_CODE = 2;

    private int type;
    private Exception exception;

    public Error(int type, Exception ex) {
        this.type = type;
        this.exception = ex;
    }

    public Exception getException() {
        return exception;
    }
    public boolean hasInternetError(){
        return type==TYPE_INTERNET_CONNECTION;
    }
    public boolean hasUnknownError(){
        return type==TYPE_UNKNOWN;
    }
    public boolean hasInvalidResponseError(){
        return type==TYPE_INVALID_RESPONSE_CODE;
    }
}
