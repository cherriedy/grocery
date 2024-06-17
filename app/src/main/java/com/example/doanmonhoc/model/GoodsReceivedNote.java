package com.example.doanmonhoc.model;

import java.io.Serializable;
import java.util.Date;

public class GoodsReceivedNote implements Serializable {
    private Integer id;
    private String grnKey;
    private String Staffid;
    private Date createdAt;
    private Date updatedAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
