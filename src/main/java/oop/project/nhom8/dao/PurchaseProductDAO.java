package oop.project.nhom4.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oop.project.nhom4.model.PurchaseProduct;

public class PurchaseProductDAO {
    private final Connection connection;
    private final String table = "purchase_product"; // table name

    public PurchaseProductDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public boolean add(PurchaseProduct pp) {
        String sql = "INSERT INTO " + table +
                " (purchase_id, product_id, quantity, unit_price, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, pp.getPurchaseId());
            stmt.setLong(2, pp.getProductId());
            stmt.setInt(3, pp.getQuantity());
            stmt.setDouble(4, pp.getUnitPrice());
            stmt.setTimestamp(5, pp.getCreatedAt());
            stmt.setTimestamp(6, pp.getUpdatedAt());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ALL
    public List<PurchaseProduct> getAll() {
        List<PurchaseProduct> list = new ArrayList<>();
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
    public PurchaseProduct getById(long id) {
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

    // READ BY PURCHASE ID
    public List<PurchaseProduct> getByPurchaseId(long purchaseId) {
        List<PurchaseProduct> list = new ArrayList<>();
        String sql = "SELECT * FROM " + table + " WHERE purchase_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, purchaseId);
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
    public boolean update(PurchaseProduct pp) {
        String sql = "UPDATE " + table +
                " SET purchase_id = ?, product_id = ?, quantity = ?, unit_price = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, pp.getPurchaseId());
            stmt.setLong(2, pp.getProductId());
            stmt.setInt(3, pp.getQuantity());
            stmt.setDouble(4, pp.getUnitPrice());
            stmt.setTimestamp(5, pp.getUpdatedAt());
            stmt.setLong(6, pp.getId());
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

    // DELETE BY PURCHASE ID (useful for cascading deletions)
    public boolean deleteByPurchaseId(long purchaseId) {
        String sql = "DELETE FROM " + table + " WHERE purchase_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, purchaseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper: map ResultSet → PurchaseProduct object
    private PurchaseProduct map(ResultSet rs) throws SQLException {
        PurchaseProduct pp = new PurchaseProduct();
        pp.setId(rs.getLong("id"));
        pp.setPurchaseId(rs.getLong("purchase_id"));
        pp.setProductId(rs.getLong("product_id"));
        pp.setQuantity(rs.getInt("quantity"));
        pp.setUnitPrice(rs.getDouble("unit_price"));
        pp.setCreatedAt(rs.getTimestamp("created_at"));
        pp.setUpdatedAt(rs.getTimestamp("updated_at"));
        return pp;
    }
}
