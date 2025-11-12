package oop.project.nhom8.controller;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import oop.project.nhom8.dao.PurchaseProductDAO;
import oop.project.nhom8.model.PurchaseProduct;

public class PurchaseProductController {
    private final PurchaseProductDAO purchaseProductDAO;

    public PurchaseProductController(Connection connection) {
        this.purchaseProductDAO = new PurchaseProductDAO(connection);
    }

    public boolean create(long purchaseId, long productId, int quantity, double unitPrice) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        PurchaseProduct pp = new PurchaseProduct(0, purchaseId, productId, quantity, unitPrice, now, now);
        return purchaseProductDAO.add(pp);
    }

    public List<PurchaseProduct> getAll() {
        return purchaseProductDAO.getAll();
    }

    public PurchaseProduct getById(long id) {
        return purchaseProductDAO.getById(id);
    }

    public List<PurchaseProduct> getByPurchaseId(long purchaseId) {
        return purchaseProductDAO.getByPurchaseId(purchaseId);
    }

    public boolean update(long id, long purchaseId, long productId, int quantity, double unitPrice) {
        PurchaseProduct pp = purchaseProductDAO.getById(id);
        if (pp == null)
            return false;

        pp.setPurchaseId(purchaseId);
        pp.setProductId(productId);
        pp.setQuantity(quantity);
        pp.setUnitPrice(unitPrice);
        pp.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return purchaseProductDAO.update(pp);
    }

    public boolean delete(long id) {
        return purchaseProductDAO.delete(id);
    }

    public boolean deleteByPurchaseId(long purchaseId) {
        return purchaseProductDAO.deleteByPurchaseId(purchaseId);
    }
}
