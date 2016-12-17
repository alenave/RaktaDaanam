package com.hack.blackhawk.raktadaanam.models;


public class Location {
    public Location(int id, float lat, float lng, People people) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.people = people;
    }

    public int getId() {
        return id;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    int id;
    float lat;
    float lng;
    People people;

}
