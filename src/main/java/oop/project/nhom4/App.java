package oop.project.nhom4;

import java.sql.Connection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import oop.project.nhom4.controller.CustomerController;
import oop.project.nhom4.dao.CustomerDAO;
import oop.project.nhom4.database.DatabaseConnectionManager;
import oop.project.nhom4.view.Index;
import oop.project.nhom4.view.Login;

public class App {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}