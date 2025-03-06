package net.m4.onlineshop.Models;

import net.m4.onlineshop.Domain.ItemDomain;

import java.util.HashMap;
import java.util.Map;

public class User {

    //region Fields
    private String username;
    private String userId;
    private String email;
    private Map<String, ItemDomain> wishlist;
    //endregion

    //region Constructors
    public User() {}

    public User(String userId, String email, String username) {
        setUserId(userId);
        setEmail(email);
        setUsername(username);
        this.wishlist = new HashMap<>();
    }

    public User(String userId, String email, Map<String, ItemDomain> wishlist) {
        setUserId(userId);
        setEmail(email);
        setWishlist(wishlist);
    }
    //endregion

    //region Getters & Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, ItemDomain> getWishlist() {
        return wishlist;
    }

    public void setWishlist(Map<String, ItemDomain> wishlist) {
        this.wishlist = wishlist;
    }
    //endregion
}


