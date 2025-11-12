package oop.project.nhom8;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import oop.project.nhom8.view.LoginFrame;

public class App {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}