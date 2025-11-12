package oop.project.nhom8.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oop.project.nhom8.model.Customer;

public class CustomerDAO {

    private final Connection connection;
    private final String table = "customer";

    // Nhận kết nối từ bên ngoài
    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    private List<Customer> mapResultSetToKhachhangList(ResultSet rs) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        while (rs.next()) {
            String id = rs.getString("id");
            String name = rs.getString("name");
            String phone = rs.getString("phone");
            String dob = rs.getString("date_of_birth");
            Integer purchaseCount = rs.getInt("purchase_count");
            customers.add(new Customer(id, name, phone, dob, purchaseCount));
        }
        return customers;
    }

    public List<Customer> getAll() {
        String sqlCommand = "select c.*, count(p.id) as purchase_count from customer c\n" + //
                "left join purchase p on c.id = p.customer_id\n" + //
                "group by c.id";
        try (Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sqlCommand)) {
            return mapResultSetToKhachhangList(rs);
        } catch (SQLException e) {
            System.err.println("select error: " + e.toString());
            return new ArrayList<>();
        }
    }

    public void deleteId(String id) {
        String sqlCommand = "DELETE FROM " + table + " WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sqlCommand)) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("delete error: " + e.toString());
        }
    }

    public void insert(Customer s) {
        String sqlCommand = "INSERT INTO " + table + " (id, name, phone, `date_of_birth`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sqlCommand)) {
            pst.setString(1, s.getId());
            pst.setString(2, s.getName());
            pst.setString(3, s.getPhone());
            pst.setString(4, s.getDateOfBirth());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("insert error: " + e.toString());
        }
    }

    // Phương thức update khách hàng
    public void updateId(Customer s) {
        String sqlCommand = "UPDATE " + table + " SET name = ?, phone = ?, `date_of_birth` = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sqlCommand)) {
            pst.setString(1, s.getName());
            pst.setString(2, s.getPhone());
            pst.setString(3, s.getDateOfBirth());
            pst.setString(4, s.getId()); // ID là tham số cuối
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("update error: " + e.toString());
        }
    }

    public List<Customer> search(String columnName, String query) {
        String safeColumnName = columnName;
        if (columnName.equals("date_of_birth")) {
            safeColumnName = "`date_of_birth`";
        }

        String sqlCommand = "SELECT * FROM " + table + " WHERE " + safeColumnName + " LIKE ?";
        try (PreparedStatement pst = connection.prepareStatement(sqlCommand)) {
            pst.setString(1, "%" + query + "%");
            try (ResultSet rs = pst.executeQuery()) {
                return mapResultSetToKhachhangList(rs);
            }
        } catch (SQLException e) {
            System.err.println("Search error: " + e.toString());
            return new ArrayList<>();
        }
    }

    public Map<Integer, Integer> getBirthYearCounts() {
        Map<Integer, Integer> yearCounts = new HashMap<>();
        String sql = "SELECT `date_of_birth` FROM " + table;

        try (Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String dob = rs.getString("date_of_birth");
                if (dob != null && dob.length() >= 4) {
                    String yearString = dob.substring(dob.length() - 4);
                    int year = Integer.parseInt(yearString);
                    yearCounts.put(year, yearCounts.getOrDefault(year, 0) + 1);
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
        return yearCounts;
    }

    public boolean isIdExists(String id) {
        String sql = "SELECT 1 FROM " + table + " WHERE id = ? LIMIT 1";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
