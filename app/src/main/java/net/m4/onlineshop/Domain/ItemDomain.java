package net.m4.onlineshop.Domain;

import java.io.Serializable;

public class ItemDomain implements Serializable {

    //region Fields
    private int Id;
    private int CategoryId;
    private String Title;
    private String ImagePath;
    private String Description;
    private double Price;
    private double Star;
    //endregion

    //region Constructors
    public ItemDomain() {
    }
    //endregion

    //region Getters & Setters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getStar() {
        return Star;
    }

    public void setStar(double star) {
        Star = star;
    }
    //endregion
}
