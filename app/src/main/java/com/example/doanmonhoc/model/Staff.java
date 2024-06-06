package com.example.doanmonhoc.model;

import java.util.Date;

public class Staff {
    private long id;
    private String staffKey;
    private String staffName;
    private Date staffDob;
    private Byte staffGender;
    private String staffPhone;
    private String staffEmail;
    private String address;
    private String staffImage;


    // Constructors
    public Staff() {
    }

    public Staff(long id, String staffKey, String staffName, Date staffDob, Byte staffGender, String staffPhone, String staffEmail, String address, String staffImage) {
        this.id = id;
        this.staffKey = staffKey;
        this.staffName = staffName;
        this.staffDob = staffDob;
        this.staffGender = staffGender;
        this.staffPhone = staffPhone;
        this.staffEmail = staffEmail;
        this.address = address;
        this.staffImage = staffImage;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStaffKey() {
        return staffKey;
    }

    public void setStaffKey(String staffKey) {
        this.staffKey = staffKey;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Date getStaffDob() {
        return staffDob;
    }

    public void setStaffDob(Date staffDob) {
        this.staffDob = staffDob;
    }

    public Byte getStaffGender() {
        return staffGender;
    }

    public void setStaffGender(Byte staffGender) {
        this.staffGender = staffGender;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStaffImage() {
        return staffImage;
    }

    public void setStaffImage(String staffImage) {
        this.staffImage = staffImage;
    }
}
