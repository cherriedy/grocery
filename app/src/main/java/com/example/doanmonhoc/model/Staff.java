package com.example.doanmonhoc.model;

public class Staff {
    private String name;
    private String email;
    private int photo;

    public Staff(String name, String email, int photo) {
        this.name = name;
        this.email = email;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getPhoto() {
        return photo;
    }
}
