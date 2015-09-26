package com.petdom.breeder.events;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class Event {

    public static final int STATUS_ERROR=1;
    public static final int STATUS_SUCCESS=2;

    public static final int EVENT_NONE = -1;
    public static final int EVENT_DOWNLOAD_BREEDERS=1;
    public static final int EVENT_DOWNLOAD_DOG_BREEDS =2;
    public static final int EVENT_DOWNLOAD_PET_TYPES=3;
    public static final int EVENT_DOWNLOAD_DOG_COLORS=4;
    public static final int EVENT_DOWNLOAD_DOG_COLOR_PATTERNS=5;
    public static final int EVENT_CREATE_NEW_BREEDER=6;
    public static final int EVENT_CREATE_NEW_DOG = 7;
    public static final int EVENT_DOWNLOAD_DOGS = 8;

    private Object data;
    private int type;
    private int status;
    private Error error;
    public Event(int type,int status) {
        this.type = type;
        this.status=status;
    }

    public int getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public boolean isSuccess(){
        return  status==STATUS_SUCCESS;
    }

    public boolean hasError(){
        return  status==STATUS_ERROR;
    }
}
