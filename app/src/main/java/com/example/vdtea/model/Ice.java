package com.example.vdtea.model;

public class Ice {
    private String level;
    private long topping_price;

    public Ice() {
    }

    public Ice(String level, long topping_price) {
        this.level = level;
        this.topping_price = topping_price;
    }

    public String getLevel() {
        return level + " Đá";
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getTopping_price() {
        return topping_price;
    }

    public void setTopping_price(long topping_price) {
        this.topping_price = topping_price;
    }
}
