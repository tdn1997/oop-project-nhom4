package oop.project.nhom4;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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