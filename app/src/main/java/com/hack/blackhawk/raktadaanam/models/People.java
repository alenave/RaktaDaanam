package com.hack.blackhawk.raktadaanam.models;

import java.io.Serializable;
import java.util.Date;


public class People implements Serializable{
    public People() {
    }

    public People(String name, long mobile_number, String blood_group, String dob, String gender, String password) {
        this.name = name;
        this.mobile_number = mobile_number;
        this.blood_group = blood_group;
        this.dob = dob;
        this.gender = gender;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMobileNumber() {
        return mobile_number;
    }

    public void setMobileNumber(long mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getBloodGroup() {
        return blood_group;
    }

    public void setBloodGroup(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastDonationDate() {
        return last_donation_date;
    }

    public void setLastDonationDate(Date last_donation_date) {
        this.last_donation_date = last_donation_date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    int id;
    String name;
    long mobile_number;
    String blood_group;
    String dob;
    String gender;
    String password;
    Date last_donation_date;
    Location location;
}
