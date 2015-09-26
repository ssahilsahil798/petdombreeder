package com.petdom.breeder.modal;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class BreederList {
    private Breeder[] objects;

    public Breeder[] getBreeders() {
        return objects;
    }

    public int size() {
        return objects == null ? 0 : objects.length;
    }

    public boolean isEmpty() {
        return size()==0;
    }

    public Breeder get(int index) {
        if (size()>0){
            return objects[index];
        }
        return null;
    }
}
