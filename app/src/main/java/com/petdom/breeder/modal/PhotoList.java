package com.petdom.breeder.modal;

import java.util.ArrayList;

/**
 * Created by diwakar.mishra on 9/30/2015.
 */
public class PhotoList {

    private ArrayList<Photo> photos;

    public PhotoList() {
        photos = new ArrayList<>();
    }
    void add(Photo photo){
        if ( !photos.contains(photo)){
            photos.add(photo);
        }
    }
    public void remove(Photo photo){
        photos.remove(photo);
    }
    public Photo get(int index){
        return photos.get(index);
    }
    public int size(){
        return photos.size();
    }
}
