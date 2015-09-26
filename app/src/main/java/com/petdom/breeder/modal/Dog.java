package com.petdom.breeder.modal;

/**
 * Created by diwakar.mishra on 9/22/2015.
 */
public class Dog {
    private String breed;

    private String breed_type;

    private boolean vaccination_certificate;

    private boolean health_history;

    private float weight;

    private String listing_type;

    private double lng;

    private boolean birth_certificate;

    private float height;

    private float price;

    private String pattern;

    private String color;

    private String description;

    private String microchip_number;

    private String name;

    private String dob;

    private int age;

    private String gender;

    private double lat;

    private Breeder breeder;

    public Dog(String name,Breeder breeder) {
        this.breeder=breeder;
        this.name = name;
    }

    public Dog(Breeder breeder) {
        this.breeder = breeder;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public boolean isVaccination_certificate() {
        return vaccination_certificate;
    }

    public void setVaccination_certificate(boolean vaccination_certificate) {
        this.vaccination_certificate = vaccination_certificate;
    }

    public boolean isHealth_history() {
        return health_history;
    }

    public void setHealth_history(boolean health_history) {
        this.health_history = health_history;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getListing_type() {
        return listing_type;
    }

    public void setListing_type(String listing_type) {
        this.listing_type = listing_type;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isBirth_certificate() {
        return birth_certificate;
    }

    public void setBirth_certificate(boolean birth_certificate) {
        this.birth_certificate = birth_certificate;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMicrochip_number() {
        return microchip_number;
    }

    public void setMicrochip_number(String microchip_number) {
        this.microchip_number = microchip_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Breeder getBreeder() {
        return breeder;
    }

    public void setBreeder(Breeder breeder) {
        this.breeder = breeder;
    }

    public String getBreedType() {
        return breed_type;
    }

    public void setBreedType(String breed_type) {
        this.breed_type = breed_type;
    }
}
