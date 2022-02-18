package com.futureproducts.headcount.model;

import com.google.firebase.firestore.PropertyName;

public class Restaurant {

    @PropertyName("ID")
    private String ID;
    @PropertyName("address")
    private String address;
    @PropertyName("covid")
    private String covid;
    @PropertyName("geohash")
    private String geohash;
    @PropertyName("lat")
    private double lat;
    @PropertyName("lng")
    private double lng;
    @PropertyName("logo")
    private String logo;
    @PropertyName("original")
    private String original;
    @PropertyName("resturantname")
    private String resturantname;
    @PropertyName("distanceAwayInMeters")
    private double distanceAwayInMeters;
    @PropertyName("headcount")
    private String headcount;


    public Restaurant() {
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getDistanceAwayInMeters() {
        return distanceAwayInMeters;
    }


    public void setDistanceAwayInMeters(double distanceAwayInMeters) {
        this.distanceAwayInMeters = distanceAwayInMeters;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCovid() {
        return covid;
    }

    public void setCovid(String covid) {
        this.covid = covid;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getResturantname() {
        return resturantname;
    }

    public void setResturantname(String resturantname) {
        this.resturantname = resturantname;
    }

    public String getHeadcount() {
        return headcount;
    }

    public void setHeadcount(String headcount) {
        this.headcount = headcount;
    }
}
