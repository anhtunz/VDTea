package com.example.vdtea.model;

public class Size {
    private String size_name;
    private long size_price;

    public Size() {
    }

    public Size(String size_name, long size_price) {
        this.size_name = size_name;
        this.size_price = size_price;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public long getSize_price() {
        return size_price;
    }

    public void setSize_price(long size_price) {
        this.size_price = size_price;
    }
}
