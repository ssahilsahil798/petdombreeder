package com.petdom.breeder.modal;

import java.util.ArrayList;

/**
 * Created by diwakar.mishra on 9/22/2015.
 */
public class DataController {
    private static DataController instance = new DataController();
    private BreederList breeders = new BreederList();
    private User user;
    private DogBreedList dogBreedList;
    private PetTypeList petTypeList;
    private ColorList colorList;
    private PatternList patternList;
    private PhotoList photoList;

    private DataController() {
        photoList = AppPreferences.getInstance().load();
    }

    public static DataController getInstance() {
        return instance;
    }

    public PhotoList getPhotoList() {
        return photoList;
    }

    public BreederList getBreeders() {
        return breeders;
    }

    public User createUser(String name, String password) {
        return user = new User(name, password);
    }

    public User getUser() {
        return user;
    }

    public void setBreeders(BreederList breeders) {
        this.breeders = breeders;
    }

    public void setDogBreedsList(DogBreedList list) {
        this.dogBreedList = list;
    }

    public DogBreedList getDogBreedsList() {
        return dogBreedList;
    }

    public void setPetTypeList(PetTypeList petTypeList) {
        this.petTypeList = petTypeList;
    }

    public PetTypeList getPetTypeList() {
        return petTypeList;
    }

    public ColorList getColorList() {
        return colorList;
    }

    public void setColorList(ColorList colorList) {
        this.colorList = colorList;
    }

    public PatternList getPatternList() {
        return patternList;
    }

    public void setPatternList(PatternList patternList) {
        this.patternList = patternList;
    }

    public void savePhotos(ArrayList<Photo> photos) {
        for (int i = 0; i < photos.size(); i++) {
            photoList.add(photos.get(i));
        }
        commitPhotos();
    }

    public void commitPhotos() {
        AppPreferences.getInstance().savePics(photoList);
    }

    public void deletePhoto(Photo photo) {
        photoList.remove(photo);
        commitPhotos();
    }

}
