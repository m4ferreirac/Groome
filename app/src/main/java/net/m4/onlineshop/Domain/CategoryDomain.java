package net.m4.onlineshop.Domain;

import androidx.annotation.NonNull;

public class CategoryDomain {

    //region Fields
    private int Id;
    private String ImagePath;
    private String Name;
    //endregion

    //region Constructors
    public CategoryDomain() {
    }
    //endregion

    //region Methods
    @NonNull
    @Override
    public String toString() {
        return Name;
    }
    //endregion

    //region Getters & Setters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    //endregion
}
