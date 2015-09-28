package com.petdom.breeder.modal;

/**
 * Created by diwakar.mishra on 9/26/2015.
 */
public class Price {
    private String currency="INR";

    private float amount;

    public Price(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }
}
