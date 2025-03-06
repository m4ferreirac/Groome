package net.m4.onlineshop.Enums;

public enum OrderStatus {
    PENDING("Em processo"),
    SHIPPING("Em envio"),
    COMPLETED("Concluído");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
