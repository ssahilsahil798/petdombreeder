package com.petdom.breeder.modal;

/**
 * Created by diwakar.mishra on 9/22/2015.
 */
public class DataController {
    private static DataController instance = new DataController();
    private BreederList breeders=new BreederList();
    private User user;
    private DogBreedList dogBreedList;
    private PetTypeList petTypeList;
    private ColorList colorList;
    private PatternList patternList;
    private DataController() {


    }

    public static DataController getInstance() {
        return instance;
    }

    public BreederList getBreeders() {
        return breeders;
    }
    public User createUser(String name,String password){
        return user = new User(name,password);
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
}
