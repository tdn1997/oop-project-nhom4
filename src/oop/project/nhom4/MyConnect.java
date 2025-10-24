package oop.project.nhom4;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MyConnect {
 
        private final String className = "com.mysql.cj.jdbc.Driver";
        private final String url = "jdbc:mysql://localhost:3306/oop_project_nhom4_db";
        private final String user ="root";
        private final String pass ="Baohien14082004@";
        private final String table = "khach_hang";
        private Connection connection;
        
        // Kết nối đến cơ sở dữ liệu
        public void connect() {
            try {
                Class.forName(className);
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Connect success!");
            } catch (ClassNotFoundException e) {
                        
            } catch (SQLException e) {
                System.out.println("Error connection!");
            }
        }
        
        // Hiển thị dữ liệu 
        public void showData(ResultSet rs) {
            try {
    		while (rs.next()) {
                    System.out.printf("%-10s %-20s %-5.2f \n", rs.getString(1),
                    rs.getString(2), rs.getDouble(3), rs.getString(4));
                }
            } catch (SQLException e) {
            }
    	}
        
        // Lấy dữ liệu và trả về Resultset với lệnh select
        public ResultSet getData() {
            ResultSet rs = null;
            String sqlCommand = "select * from " + table;
            Statement st;
            try {
                st = connection.createStatement();
                rs = st.executeQuery(sqlCommand);
            } catch (SQLException e) {
                System.out.println("select error \n" + e.toString());
            }
            return rs;
    	}
        
        // Xóa bởi id
        public void deleteId(String id) {
            String sqlCommand = "delete from " + table + " where id = ?";
            PreparedStatement pst = null;
            try {
    		pst = connection.prepareStatement(sqlCommand);
    		pst.setString(1, id);
            if (pst.executeUpdate() > 0) {
    		System.out.println("delete success");
            } else {
    		System.out.println("delete error \n");
            }
            } catch (SQLException e) {
    		System.out.println("delete error \n" + e.toString());
            }
    	}
        
        // Thêm khách hàng vào cơ sở dữ liệu
        public void insert(Khachhang s) {
            String sqlCommand = "insert into " + table + " value(?, ?, ?, ?)";
            PreparedStatement pst = null;
            try {
    		pst = connection.prepareStatement(sqlCommand);
    		pst.setString(1, s.getId());
    		pst.setString(2, s.getName());
    		pst.setDouble(3, s.getPhone());
    		pst.setString(4, s.getDateOfBirth());
            if (pst.executeUpdate() > 0) {
    		System.out.println("insert success");
            } else {
    		System.out.println("insert error \n");
            }
            } catch (SQLException e) {
    		System.out.println("insert error \n" + e.toString());
            }
    	}
        
        // Update thông tin khách hàng
        public void updateId(String id, Khachhang s) {
            String sqlCommand = "update " + table
    				+ " set name = ?, phone = ? " + " where id = ?";
            PreparedStatement pst = null;
            try {
    		pst = connection.prepareStatement(sqlCommand);
    		pst.setString(1, s.getName());
    		pst.setDouble(2, s.getPhone());
    		pst.setString(3, s.getId());
    		pst.executeUpdate();
            if (pst.executeUpdate() > 0) {
    		System.out.println("update success");
            } else {
    		System.out.println("update error \n");
            }
            } catch (SQLException e) {
    		System.out.println("update error \n" + e.toString());
            }
    	}
        
        // Thêm phương thức Tìm kiếm khách hàng
        public ResultSet search(String columnName, String query) {
            ResultSet rs = null;
            
            // Tìm kiếm dữ liệu trong một cột cụ thể với điều kiện LIKE
            String sqlCommand = "select * from " + table + " where " + columnName + " LIKE ?";
            PreparedStatement pst = null;
            try {
                pst = connection.prepareStatement(sqlCommand);
                pst.setString(1, "%" + query + "%");
                rs = pst.executeQuery();
            } catch (SQLException e) {
                System.out.println("Search error \n" + e.toString());
            }
            return rs;
        }
        
        // Phương thức tạo bảng thống kê
        public Map<Integer, Integer> getBirthYearCounts() {
            Map<Integer, Integer> yearCounts = new HashMap<>();
            String sql = "SELECT `Date of birth` FROM " + table;

            try (Statement st = connection.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String dob = rs.getString("Date of birth");
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
        
    public static void main(String[] args) {
        MyConnect myConnect = new MyConnect();
        myConnect.connect();
    }
}