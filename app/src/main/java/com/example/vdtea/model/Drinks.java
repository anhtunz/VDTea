package com.example.vdtea.model;
public class Drinks {
    private String drinks_name, decription, drinks_image,drinks_id;
    private long price, sale, sold_count;

    private double rating;
    public Drinks(){}

    public Drinks(String drinks_name, String decription, String drinks_image, String drinks_id, long price, long sale, long sold_count, double rating) {
        this.drinks_name = drinks_name;
        this.decription = decription;
        this.drinks_image = drinks_image;
        this.drinks_id = drinks_id;
        this.price = price;
        this.sale = sale;
        this.sold_count = sold_count;
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDrinks_id() {
        return drinks_id;
    }

    public void setDrinks_id(String drinks_id) {
        this.drinks_id = drinks_id;
    }

    public String getDrinks_name() {
        return drinks_name;
    }

    public void setDrinks_name(String drinks_name) {
        this.drinks_name = drinks_name;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getDrinks_image() {
        return drinks_image;
    }

    public void setDrinks_image(String drinks_image) {
        this.drinks_image = drinks_image;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }


    public long getSale() {
        return sale;
    }

    public void setSale(long sale) {
        this.sale = sale;
    }

    public long getSold_count() {
        return sold_count;
    }

    public void setSold_count(long sold_count) {
        this.sold_count = sold_count;
    }
}
