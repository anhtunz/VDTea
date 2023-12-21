package com.example.vdtea.model;

import java.util.List;

public class Booking {
    private String delivery_address;
    private List<Menu> menu;

    private String order_date;
    private String phone_numer;

    private long status;
    private long total_amount;

    private String user_name;
    private String payment;

    public Booking() {
    }

    public Booking(String delivery_address, List<Menu> menu, String order_date, String phone_numer, long status, long total_amount, String user_name, String payment) {
        this.delivery_address = delivery_address;
        this.menu = menu;
        this.order_date = order_date;
        this.phone_numer = phone_numer;
        this.status = status;
        this.total_amount = total_amount;
        this.user_name = user_name;
        this.payment = payment;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getPhone_numer() {
        return phone_numer;
    }

    public void setPhone_numer(String phone_numer) {
        this.phone_numer = phone_numer;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(long total_amount) {
        this.total_amount = total_amount;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public static class Menu{
        private String menu_detail;

        public Menu() {
        }

        public Menu(String menu_detail) {
            this.menu_detail = menu_detail;
        }

        public String getMenu_detail() {
            return menu_detail;
        }

        public void setMenu_detail(String menu_detail) {
            this.menu_detail = menu_detail;
        }
    }
}
