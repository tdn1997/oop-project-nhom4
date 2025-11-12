package oop.project.nhom8.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oop.project.nhom8.model.Purchase;

public class PurchaseDAO {
    private final Connection connection;
    private final String table = "purchase"; // table name

    public PurchaseDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public boolean add(Purchase purchase) {
        String sql = "INSERT INTO " + table + " (customer_id, purchase_date, total_amount, " +
                "purchase_status, payment_method, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, purchase.getCustomerId());
            stmt.setTimestamp(2, purchase.getPurchaseDate());
            stmt.setDouble(3, purchase.getTotalAmount());
            stmt.setString(4, purchase.getPurchaseStatus());
            stmt.setString(5, purchase.getPaymentMethod());
            stmt.setTimestamp(6, purchase.getCreatedAt());
            stmt.setTimestamp(7, purchase.getUpdatedAt());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ALL
    public List<Purchase> getAll() {
        List<Purchase> list = new ArrayList<>();
        String sql = "SELECT * FROM " + table;
        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ BY ID
    public Purchase getById(long id) {
        String sql = "SELECT * FROM " + table + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ BY CUSTOMER ID
    public List<Purchase> getByCustomerId(String customerId) {
        List<Purchase> list = new ArrayList<>();
        String sql = "SELECT * FROM " + table + " WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public boolean update(Purchase purchase) {
        String sql = "UPDATE " + table + " SET customer_id = ?, purchase_date = ?, total_amount = ?, " +
                "purchase_status = ?, payment_method = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, purchase.getCustomerId());
            stmt.setTimestamp(2, purchase.getPurchaseDate());
            stmt.setDouble(3, purchase.getTotalAmount());
            stmt.setString(4, purchase.getPurchaseStatus());
            stmt.setString(5, purchase.getPaymentMethod());
            stmt.setTimestamp(6, purchase.getUpdatedAt());
            stmt.setLong(7, purchase.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean delete(long id) {
        String sql = "DELETE FROM " + table + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Map ResultSet → Purchase object
    private Purchase map(ResultSet rs) throws SQLException {
        Purchase p = new Purchase();
        p.setId(rs.getLong("id"));
        p.setCustomerId(rs.getString("customer_id"));
        p.setPurchaseDate(rs.getTimestamp("purchase_date"));
        p.setTotalAmount(rs.getDouble("total_amount"));
        p.setPurchaseStatus(rs.getString("purchase_status"));
        p.setPaymentMethod(rs.getString("payment_method"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }
}
