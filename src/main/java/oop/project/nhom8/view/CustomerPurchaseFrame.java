package oop.project.nhom4.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import oop.project.nhom4.model.Purchase;

public class CustomerPurchaseFrame extends JFrame {
    private JTable purchaseTable;
    private DefaultTableModel model;
    private String customerId;
    private JLabel lblCustomerName;

    public CustomerPurchaseFrame(String customerId, String customerName) {
        this.customerId = customerId;
        setTitle("Danh sách đơn hàng của " + customerName);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents(customerName);

    }

    private void initComponents(String customerName) {
        lblCustomerName = new JLabel("Khách hàng: " + customerName);
        lblCustomerName.setFont(new Font("Segoe UI", Font.BOLD, 16));

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] {
                "ID", "Ngày mua", "Tổng tiền", "Trạng thái", "Phương thức thanh toán"
        });

        purchaseTable = new JTable(model);
        purchaseTable.setRowHeight(28);
        purchaseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(purchaseTable);

        purchaseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = purchaseTable.rowAtPoint(e.getPoint());
                if (row != -1 && e.getClickCount() == 2) {
                    long purchaseId = (long) purchaseTable.getValueAt(row, 0);
                    openPurchaseProductsFrame(purchaseId);
                }
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblCustomerName, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadPurchases(List<Purchase> purchases) {
        model.setRowCount(0);
        for (Purchase p : purchases) {
            model.addRow(new Object[] {
                    p.getId(),
                    p.getPurchaseDate(),
                    p.getTotalAmount(),
                    p.getPurchaseStatus(),
                    p.getPaymentMethod()
            });
        }

    }

    // TODO
    private void openPurchaseProductsFrame(long purchaseId) {
        // SwingUtilities.invokeLater(() -> new
        // PurchaseProduct(purchaseId).setVisible(true));
    }

}
