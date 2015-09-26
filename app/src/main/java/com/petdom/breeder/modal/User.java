package com.petdom.breeder.modal;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class User {
    private String name;
    private String password;

    private String apiKey;
    protected User(String name, String password) {
        this(name,password,"");
    }
    protected User(String name, String password, String apiKey) {
        this.name = name;
        this.password = password;
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
