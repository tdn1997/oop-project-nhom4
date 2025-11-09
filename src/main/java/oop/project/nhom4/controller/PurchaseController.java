package oop.project.nhom4.controller;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import oop.project.nhom4.dao.PurchaseDAO;
import oop.project.nhom4.model.Purchase;

public class PurchaseController {
    private final PurchaseDAO purchaseDAO;

    public PurchaseController(Connection connection) {
        this.purchaseDAO = new PurchaseDAO(connection);
    }

    public boolean create(String customerId, Timestamp purchaseDate, double totalAmount,
            String purchaseStatus, String paymentMethod) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Purchase purchase = new Purchase(0, customerId, purchaseDate, totalAmount,
                purchaseStatus, paymentMethod, now, now);
        return purchaseDAO.add(purchase);
    }

    public List<Purchase> getAll() {
        return purchaseDAO.getAll();
    }

    public Purchase getById(long id) {
        return purchaseDAO.getById(id);
    }

    public List<Purchase> getByCustomerId(String customerId) {
        return purchaseDAO.getByCustomerId(customerId);
    }

    public boolean update(long id, String customerId, Timestamp purchaseDate, double totalAmount,
            String purchaseStatus, String paymentMethod) {
        Purchase purchase = purchaseDAO.getById(id);
        if (purchase == null)
            return false;

        purchase.setCustomerId(customerId);
        purchase.setPurchaseDate(purchaseDate);
        purchase.setTotalAmount(totalAmount);
        purchase.setPurchaseStatus(purchaseStatus);
        purchase.setPaymentMethod(paymentMethod);
        purchase.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return purchaseDAO.update(purchase);
    }

    public boolean delete(long id) {
        return purchaseDAO.delete(id);
    }
}
