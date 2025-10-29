package oop.project.nhom4;

import java.sql.Connection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import oop.project.nhom4.controller.ConsumerController;
import oop.project.nhom4.dao.ConsumerDAO;
import oop.project.nhom4.database.DatabaseConnectionManager;
import oop.project.nhom4.view.Index;

public class App {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // 1. Khởi tạo lớp kết nối
            DatabaseConnectionManager myConnect = new DatabaseConnectionManager();
            Connection connection = myConnect.getConnection();

            if (connection == null) {
                System.err.println("Không thể kết nối đến CSDL. Ứng dụng thoát.");
                return;
            }

            // 2. Khởi tạo DAO (truyền kết nối vào)
            ConsumerDAO khachhangDAO = new ConsumerDAO(connection);

            // 3. Khởi tạo View
            Index myFrame = new Index();

            // 4. Khởi tạo Controller (truyền View và DAO vào)
            ConsumerController controller = new ConsumerController(myFrame, khachhangDAO);

            // 5. Kết nối View với Controller
            myFrame.setController(controller);
            
            // 6. Controller hiển thị dữ liệu ban đầu
            controller.loadDataToView();
            
            // 7. Hiển thị giao diện
            myFrame.setVisible(true);
        });
    }
}