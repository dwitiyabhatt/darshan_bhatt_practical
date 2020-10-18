package com.darshan.darshanshop.models.pojo;

public class BidItem {
    private String id;
    private String Price;

    public BidItem(String id, String price) {
        this.id = id;
        Price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
