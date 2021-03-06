package com.petdom.breeder.modal;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class DogBreedList {

    private DogBreed[] objects;

    public DogBreed[] getBreedTypes() {
        return objects;
    }

    public int size() {
        return objects == null ? 0 : objects.length;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public DogBreed get(int index) {
        if (size() > 0) {
            return objects[index];
        }
        return null;
    }

    public String[] getAllName() {
        if (!isEmpty()) {
            String[] arr = new String[size()];
            for (int i = 0; i < objects.length; i++) {
                arr[i] = objects[i].getName();
            }
            return arr;
        }
        return null;
    }
}
