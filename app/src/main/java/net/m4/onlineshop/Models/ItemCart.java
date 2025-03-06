package net.m4.onlineshop.Models;

public class ItemCart {

    //region Fields
    private int id;
    private double quantityKg;
    private double price;
    //endregion

    //region Constructs
    public ItemCart(int id, double quantityKg, double price) {
        this.id = id;
        this.quantityKg = quantityKg;
        this.price = price;
    }
    //endregion

    //region Getters & Setters
    public int getId() {
        return id;
    }

    public void setQuantityKg(double quantityKg) {
        this.quantityKg = quantityKg;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQuantityKg() {
        return quantityKg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double totalPrice) {
        this.price = totalPrice;
    }
    //endregion
}