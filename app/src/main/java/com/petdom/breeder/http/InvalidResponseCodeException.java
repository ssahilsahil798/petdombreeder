package com.petdom.breeder.http;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class InvalidResponseCodeException extends  Exception {

    private int code;

    public InvalidResponseCodeException(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
