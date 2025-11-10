package oop.project.nhom4.controller;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import oop.project.nhom4.dao.PurchaseDAO;
import oop.project.nhom4.database.DatabaseConnectionManager;
import oop.project.nhom4.model.Purchase;
import oop.project.nhom4.view.CustomerPurchaseFrame;

public class PurchaseController {
    private final PurchaseDAO purchaseDAO;
    private final CustomerPurchaseFrame view;

    public PurchaseController(CustomerPurchaseFrame view) {
        DatabaseConnectionManager myConnect = new DatabaseConnectionManager();
        Connection connection = myConnect.getConnection();
        this.purchaseDAO = new PurchaseDAO(connection);
        this.view = view;
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

    public void loadDataToView(String customerId) {
        List<Purchase> purchases = purchaseDAO.getByCustomerId(customerId);
        view.loadPurchases(purchases);
    }
}
