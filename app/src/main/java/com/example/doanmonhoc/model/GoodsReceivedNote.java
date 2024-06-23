package com.example.doanmonhoc.model;

import java.io.Serializable;
import java.sql.Timestamp;


public class GoodsReceivedNote implements Serializable {
    private Integer id;
    private String grnKey;
    private String Staffid;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrnKey() {
        return grnKey;
    }

    public void setGrnKey(String grnKey) {
        this.grnKey = grnKey;
    }

    public String getStaffid() {
        return Staffid;
    }

    public void setStaffid(String staffid) {
        Staffid = staffid;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
