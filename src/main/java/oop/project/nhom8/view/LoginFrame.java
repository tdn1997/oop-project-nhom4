package oop.project.nhom8.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import oop.project.nhom8.controller.CustomerController;
import oop.project.nhom8.dao.CustomerDAO;
import oop.project.nhom8.database.DatabaseConnectionManager;

public class LoginFrame extends JFrame {

    private final Color COLOR_BG = new Color(25, 25, 112);
    private final Color COLOR_PRIMARY = new Color(0, 0, 128);
    private final Color COLOR_SECONDARY = new Color(0, 120, 215);
    private final Color COLOR_TEXT_LIGHT = Color.WHITE;
    private final Font FONT_MAIN = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 14);

    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnLogin;
    private JButton btnCancel;

    public LoginFrame() {
        setTitle("Quản Lý Khách Hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1124, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        // Main content panel
        JPanel content = new JPanel(new BorderLayout());
        setContentPane(content);

        // Left branding panel
        JPanel panelLeft = new JPanel();
        panelLeft.setBackground(COLOR_BG);
        panelLeft.setPreferredSize(new Dimension(1124 * 40 / 100, 0));
        panelLeft.setLayout(new GridBagLayout());
        JLabel lblBrand = new JLabel("Nhóm 8");
        lblBrand.setForeground(COLOR_TEXT_LIGHT);
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 48));
        panelLeft.add(lblBrand);
        content.add(panelLeft, BorderLayout.WEST);

        // Right login panel
        JPanel panelRight = new JPanel();
        panelRight.setBackground(Color.WHITE);
        panelRight.setLayout(new GridBagLayout());
        content.add(panelRight, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Đăng nhập");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelRight.add(lblTitle, gbc);

        JLabel lblUsername = new JLabel("Tài khoản:");
        lblUsername.setFont(FONT_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelRight.add(lblUsername, gbc);

        tfUsername = new JTextField();
        tfUsername.setText("admin"); // TODO

        styleTextField(tfUsername);
        gbc.gridx = 1;
        gbc.gridy = 1;
        tfUsername.setColumns(15);
        panelRight.add(tfUsername, gbc);

        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(FONT_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelRight.add(lblPassword, gbc);

        pfPassword = new JPasswordField();
        pfPassword.setText("1234"); // TODO

        styleTextField(pfPassword);
        gbc.gridx = 1;
        gbc.gridy = 2;
        pfPassword.setColumns(15);
        panelRight.add(pfPassword, gbc);

        // btnCancel = new JButton("Đóng");
        // styleActionButton(btnCancel);
        // gbc.gridx = 0;
        // gbc.gridy = 3;
        // gbc.gridwidth = 1;
        // panelRight.add(btnCancel, gbc);

        btnLogin = new JButton("Đăng nhập");
        styleActionButton(btnLogin);
        btnLogin.setBackground(COLOR_PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelRight.add(btnLogin, gbc);

        // Button actions
        btnLogin.addActionListener(e -> loginAction());
        // btnCancel.addActionListener(e -> {
        // tfUsername.setText("");
        // pfPassword.setText("");
        // });
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(FONT_LABEL);
        Border padding = new EmptyBorder(5, 8, 5, 8);
        Border line = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        textField.setBorder(BorderFactory.createCompoundBorder(line, padding));
    }

    private void styleActionButton(JButton btn) {
        btn.setFont(FONT_MAIN);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 12, 12, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
    }

    private void loginAction() {
        String username = tfUsername.getText();
        String password = new String(pfPassword.getPassword());

        // Ví dụ kiểm tra cứng: username = "admin", password = "1234"
        if ("admin".equals(username) && "1234".equals(password)) {
            // Đăng nhập thành công: mở IndexFrame và đóng LoginFrame
            SwingUtilities.invokeLater(() -> {

                // 1. Khởi tạo lớp kết nối
                DatabaseConnectionManager myConnect = new DatabaseConnectionManager();
                Connection connection = myConnect.getConnection();

                if (connection == null) {
                    System.err.println("Không thể kết nối đến CSDL. Ứng dụng thoát.");
                    return;
                }

                // 2. Khởi tạo DAO (truyền kết nối vào)
                CustomerDAO khachhangDAO = new CustomerDAO(connection);

                // 3. Khởi tạo View
                IndexFrame index = new IndexFrame();

                // 4. Khởi tạo Controller (truyền View và DAO vào)
                CustomerController controller = new CustomerController(index, khachhangDAO);

                // 5. Kết nối View với Controller
                index.setController(controller);

                // 6. Controller hiển thị dữ liệu ban đầu
                controller.loadDataToView();

                // 7. Hiển thị giao diện
                index.setVisible(true);
            });
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Sai tài khoản hoặc mật khẩu",
                    "Đăng nhập thất bại",
                    JOptionPane.ERROR_MESSAGE);
            // Có thể xóa mật khẩu và username
            pfPassword.setText("");
        }
    }
}
