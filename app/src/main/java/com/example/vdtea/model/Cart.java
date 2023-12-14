package com.example.vdtea.model;

public class Cart {
    private String drinks_ice, drinks_size, drinks_sugar, drinks_id;

    long quantity, drinks_totalprice;

    public String getDrinks_ice() {
        return drinks_ice;
    }

    public Cart() {
    }

    public Cart(String drinks_ice, String drinks_size, String drinks_sugar, String drinks_id, long quantity, long drinks_totalprice) {
        this.drinks_ice = drinks_ice;
        this.drinks_size = drinks_size;
        this.drinks_sugar = drinks_sugar;
        this.drinks_id = drinks_id;
        this.quantity = quantity;
        this.drinks_totalprice = drinks_totalprice;
    }

    public void setDrinks_ice(String drinks_ice) {
        this.drinks_ice = drinks_ice;
    }

    public String getDrinks_size() {
        return "Size "+ drinks_size;
    }

    public void setDrinks_size(String drinks_size) {
        this.drinks_size = drinks_size;
    }

    public String getDrinks_sugar() {
        return drinks_sugar;
    }

    public void setDrinks_sugar(String drinks_sugar) {
        this.drinks_sugar = drinks_sugar;
    }

    public String getDrinks_id() {
        return drinks_id;
    }

    public void setDrinks_id(String drinks_id) {
        this.drinks_id = drinks_id;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getDrinks_totalprice() {
        return drinks_totalprice;
    }

    public void setDrinks_totalprice(long drinks_totalprice) {
        this.drinks_totalprice = drinks_totalprice;
    }
}
