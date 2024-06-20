package com.example.doanmonhoc.model;

import java.io.Serializable;

public class DetailedGoodsReceivedNote implements Serializable {
    private Integer id;
    private Integer GoodReceivedNoteid;
    private Integer productid;
    private Integer quantity;
    private Integer price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodReceivedNoteid() {
        return GoodReceivedNoteid;
    }

    public void setGoodReceivedNoteid(Integer goodReceivedNoteid) {
        GoodReceivedNoteid = goodReceivedNoteid;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
