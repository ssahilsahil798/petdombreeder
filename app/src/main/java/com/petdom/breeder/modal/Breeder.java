package com.petdom.breeder.modal;

import java.util.ArrayList;

/**
 * Created by diwakar.mishra on 9/21/2015.
 */
public class Breeder {
    private int id;
    private String phone;

    private String owner_name;

    private String website;

    private boolean grooming_centre;

    private boolean boarding;

    private double lng;

    private int since;

    private boolean accessories;

    private boolean pedigreed_pup;

    private boolean vaccination;

    private boolean surgery;

    private String city;

    private String country;

    private String pet_types;

    private int pincode;

    private boolean training;

    private String scale_type;

    private boolean dentistry;

    private String address;

    private String email;

    private String name;

    private String locality;

    private String breed_types;

    private double lat;

    private String resource_uri;

    private int years_in_operation;

    private String petdom_contact;

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isGrooming_centre() {
        return grooming_centre;
    }

    public void setGrooming_centre(boolean grooming_centre) {
        this.grooming_centre = grooming_centre;
    }

    public boolean isBoarding() {
        return boarding;
    }

    public void setBoarding(boolean boarding) {
        this.boarding = boarding;
    }

    public double getLongitude() {
        return lng;
    }

    public void setLongitude(double lng) {
        this.lng = lng;
    }

    public int getSince() {
        return since;
    }

    public void setSince(int since) {
        this.since = since;
    }

    public boolean isAccessories() {
        return accessories;
    }

    public void setAccessories(boolean accessories) {
        this.accessories = accessories;
    }

    public boolean isPedigreed_pup() {
        return pedigreed_pup;
    }

    public void setPedigreed_pup(boolean pedigreed_pup) {
        this.pedigreed_pup = pedigreed_pup;
    }

    public boolean isVaccination() {
        return vaccination;
    }

    public void setVaccination(boolean vaccination) {
        this.vaccination = vaccination;
    }

    public boolean isSurgery() {
        return surgery;
    }

    public void setSurgery(boolean surgery) {
        this.surgery = surgery;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPet_types() {
        return pet_types;
    }

    public void setPet_types(String pet_types) {
        this.pet_types = pet_types;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public boolean isTraining() {
        return training;
    }

    public void setTraining(boolean training) {
        this.training = training;
    }

    public String getScale_type() {
        return scale_type;
    }

    public void setScale_type(String scale_type) {
        this.scale_type = scale_type;
    }

    public boolean isDentistry() {
        return dentistry;
    }

    public void setDentistry(boolean dentistry) {
        this.dentistry = dentistry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getBreed_types() {
        return breed_types;
    }

    public void setBreed_types(String breed_types) {
        this.breed_types = breed_types;
    }

    public double getLatitude() {
        return lat;
    }

    public void setLatitude(double lat) {
        this.lat = lat;
    }

    public String getResource_uri() {
        return resource_uri;
    }

    public void setResource_uri(String resource_uri) {
        this.resource_uri = resource_uri;
    }

    public int getYears_in_operation() {
        return years_in_operation;
    }

    public void setYears_in_operation(int years_in_operation) {
        this.years_in_operation = years_in_operation;
    }

    private ArrayList<Dog> dogs;

    public Breeder() {
    }

    public Breeder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(ArrayList<Dog> dogs) {
        if (dogs!=null){
            for (int i=0;i<dogs.size();i++){
                dogs.get(i).setBreeder(this);
            }
        }
        this.dogs = dogs;
    }

    public int getId() {
        return id;
    }

    public String getOwnerName() {
        return owner_name;
    }

    public String getPetdom_contact() {
        return petdom_contact;
    }

    public void setPetdom_contact(String petdom_contact) {
        this.petdom_contact = petdom_contact;
    }
}
