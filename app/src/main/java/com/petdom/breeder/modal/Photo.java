package com.petdom.breeder.modal;

import java.io.Serializable;

/**
 * Created by diwakar.mishra on 9/27/2015.
 */
public class Photo{
    public static final int STATUS_QUEUED = 0;
    public static final int STATUS_UPLOADED = 1;
    public static final int STATUS_FAILED = 2;
    public static final int STATUS_NOT_FOUND = 3;

    public static final String KEY_BIRTH_CERTIFICATE="birth_certificate";
    public static final String KEY_VACCINATION_CERTIFICATE="vaccination_certificate";
    public static final String KEY_HEALTH_HISTORY="health_history";

    private int id;
    private String localPath;
    private String key;
    private int status;

    public Photo(int id,String key, String localPath) {
        this.id = id;


        this.localPath = localPath;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLocalPath() {
        return localPath;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Photo)) {
            return false;
        }
        Photo p = (Photo) o;
        return p.id == this.id;
    }

    @Override
    public int hashCode() {
        return (id + localPath).hashCode();
    }

    public int getId() {
        return id;
    }

}
