package com.example.doanmonhoc.model;

public class ChangePasswordRequest {
    private String matkhaucu;
    private String matkhaumoi;

    public ChangePasswordRequest(String matkhaucu, String matkhaumoi) {
        this.matkhaucu = matkhaucu;
        this.matkhaumoi = matkhaumoi;
    }

    public String getMatkhaucu() {
        return matkhaucu;
    }

    public void setMatkhaucu(String matkhaucu) {
        this.matkhaucu = matkhaucu;
    }

    public String getMatkhaumoi() {
        return matkhaumoi;
    }

    public void setMatkhaumoi(String matkhaumoi) {
        this.matkhaumoi = matkhaumoi;
    }
}
