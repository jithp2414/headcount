package com.futureproducts.headcount.model;

public class resturantsmodel {
    String resturantname, address, covid, geohash, logo, original, headcount;
    double lat, lng;

    public resturantsmodel() {
    }

    public resturantsmodel(String resturantname, String address, String covid, String geohash, String logo, String original, String headcount, double lat, double lng) {
        this.resturantname = resturantname;
        this.address = address;
        this.covid = covid;
        this.geohash = geohash;
        this.logo = logo;
        this.original = original;
        this.headcount = headcount;
        this.lat = lat;
        this.lng = lng;
    }

    public String getResturantname() {
        return resturantname;
    }

    public void setResturantname(String resturantname) {
        this.resturantname = resturantname;
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

    public String getHeadcount() {
        return headcount;
    }

    public void setHeadcount(String headcount) {
        this.headcount = headcount;
    }
}
