package oop.project.nhom4.model;

import java.sql.Timestamp;

public class Purchase {
    private long id;
    private String customerId;
    private Timestamp purchaseDate;
    private double totalAmount;
    private String purchaseStatus;
    private String paymentMethod;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Purchase() {
    }

    public Purchase(long id, String customerId, Timestamp purchaseDate, double totalAmount,
                    String purchaseStatus, String paymentMethod, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.purchaseDate = purchaseDate;
        this.totalAmount = totalAmount;
        this.purchaseStatus = purchaseStatus;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // getters & setters
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }
    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPurchaseStatus() {
        return purchaseStatus;
    }
    public void setPurchaseStatus(String purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
