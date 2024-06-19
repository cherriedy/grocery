package com.example.doanmonhoc.model;

public class LoginResponse {
    private String message;
    private long id;
    private long Roleid;
    private String staffName;

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return id;
    }

    public long getRoleid() {
        return Roleid;
    }
}
