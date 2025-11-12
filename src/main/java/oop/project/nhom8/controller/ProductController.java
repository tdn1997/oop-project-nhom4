package oop.project.nhom8.controller;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import oop.project.nhom8.dao.ProductDAO;
import oop.project.nhom8.database.DatabaseConnectionManager;
import oop.project.nhom8.model.Product;

public class ProductController {
    private final ProductDAO productDAO;

    public ProductController() {
        DatabaseConnectionManager myConnect = new DatabaseConnectionManager();
        Connection connection = myConnect.getConnection();
        this.productDAO = new ProductDAO(connection);
    }

    public boolean create(String name, double price, String description, int stockQuantity) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Product product = new Product(0, name, price, description, stockQuantity, now, now);
        return productDAO.add(product);
    }

    public List<Product> getAll() {
        return productDAO.getAll();
    }

    public Product getById(long id) {
        return productDAO.getById(id);
    }

    public boolean update(long id, String name, double price, String description, int stockQuantity) {
        Product product = productDAO.getById(id);
        if (product == null)
            return false;

        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setStockQuantity(stockQuantity);
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return productDAO.update(product);
    }

    public boolean delete(long id) {
        return productDAO.delete(id);
    }
}
