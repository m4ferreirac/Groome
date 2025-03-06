package net.m4.onlineshop.Models;

import android.annotation.SuppressLint;

import net.m4.onlineshop.Enums.OrderStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Order {

    //region Fields
    private String orderId;
    private String userId;
    private ArrayList<ItemCart> items;
    private double totalPrice;
    private String orderDate;
    private OrderStatus status;
    //endregion

    //region Constructors
    public Order(String orderId, String userId, ArrayList<ItemCart> items, double totalPrice, String orderDate, OrderStatus status) {
        setOrderId(orderId);
        setUserId(userId);
        setItems(items);
        setTotalPrice(totalPrice);
        setOrderDate(orderDate);
        setStatus(status);
    }

    public Order(String orderId, String status, String userId) {
        setOrderId(orderId);
        setStatus(OrderStatus.valueOf(status));
        setUserId(userId);
        setOrderDate(getCurrentDate());
    }
    //endregion

    //region Methods
    public static String getCurrentDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
    //endregion

    //region Getters & setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public ArrayList<ItemCart> getItems() { return items; }
    public void setItems(ArrayList<ItemCart> items) { this.items = items; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    //endregion
}