package com.hack.blackhawk.raktadaanam.models;

import java.util.Date;


public class People {

    public People(String name, long mobileNumber, String bloodGroup, Date dob, String gender, String password, Date lastDonationDate) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.bloodGroup = bloodGroup;
        this.dob = dob;
        this.gender = gender;
        this.password = password;
        this.lastDonationDate = lastDonationDate;
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
        return mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
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
        return lastDonationDate;
    }

    public void setLastDonationDate(Date lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }

    int id;
    String name;
    long mobileNumber;
    String bloodGroup;
    Date dob;
    String gender;
    String password;
    Date lastDonationDate;
}
