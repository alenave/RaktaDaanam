package com.adatech.blackhawk.raktadaanam.models;


public class Location {
    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        //this.people = people;
    }

    public int getId() {
        return id;
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

//    public People getPeople() {
//        return people;
//    }
//
//    public void setPeople(People people) {
//        this.people = people;
//    }

    int id;
    double lat;
    double lng;
//    People people;

}
