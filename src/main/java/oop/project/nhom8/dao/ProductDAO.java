package oop.project.nhom8.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oop.project.nhom8.model.Product;

public class ProductDAO {
    private final Connection connection;
    private final String table = "product"; // your table name

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public boolean add(Product product) {
        String sql = "INSERT INTO " + table +
                " (name, price, description, stock_quantity, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getDescription());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setTimestamp(5, product.getCreatedAt());
            stmt.setTimestamp(6, product.getUpdatedAt());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ALL
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
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
    public Product getById(long id) {
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

    // UPDATE
    public boolean update(Product product) {
        String sql = "UPDATE " + table +
                " SET name = ?, price = ?, description = ?, stock_quantity = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getDescription());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setTimestamp(5, product.getUpdatedAt());
            stmt.setLong(6, product.getId());
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

    // Map ResultSet → Product object
    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setPrice(rs.getDouble("price"));
        p.setDescription(rs.getString("description"));
        p.setStockQuantity(rs.getInt("stock_quantity"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }
}
