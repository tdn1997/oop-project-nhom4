package oop.project.nhom8.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import oop.project.nhom8.controller.ProductController;
import oop.project.nhom8.model.Product;

public class ProductFrame extends JFrame {
    private final ProductController productController;
    private JTable table;
    private DefaultTableModel model;

    private JPanel formPanel; // 🔹 Keep reference for show/hide
    private JTextField tfName, tfPrice, tfStock, tfDescription;
    private JButton btnAdd, btnEdit, btnDelete, btnSave, btnCancel; // 🔹 Add Cancel button
    private long selectedId = -1;
    private boolean isEditing = false; // 🔹 Track state

    public ProductFrame() {
        this.productController = new ProductController();
        initUI();
        loadProducts();
    }

    private void initUI() {
        setTitle("Sản phẩm");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ======== Top Form Panel ========
        formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        tfName = new JTextField();
        tfPrice = new JTextField();
        tfStock = new JTextField();
        tfDescription = new JTextField();

        formPanel.add(new JLabel("Tên:"));
        formPanel.add(tfName);
        formPanel.add(new JLabel("Giá:"));
        formPanel.add(tfPrice);
        formPanel.add(new JLabel("Số lượng tồn:"));
        formPanel.add(tfStock);
        formPanel.add(new JLabel("Mô tả:"));
        formPanel.add(tfDescription);

        add(formPanel, BorderLayout.NORTH);
        formPanel.setVisible(false); // 🔹 Hide initially

        // ======== Table ========
        model = new DefaultTableModel(new Object[] { "ID", "Tên", "Giá (vnđ)", "Số lượng tồn", "Mô tả" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(true); // ✅ show "16,000,000" instead of 16000000
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(0);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Number) {
                    setText(nf.format(((Number) value).doubleValue()));
                } else {
                    setText(value == null ? "" : value.toString());
                }
            }
        };
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        // Apply to all columns by default
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ======== Button Panel ========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);

        btnSave.setVisible(false); // 🔹 Hide Save/Cancel until editing
        btnCancel.setVisible(false);

        // ======== Event Listeners ========
        btnAdd.addActionListener(e -> {
            clearForm();
            toggleEditMode(true);
            isEditing = false; // adding new
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa.");
                return;
            }
            fillFormFromRow(row);
            toggleEditMode(true);
            isEditing = true; // editing existing
        });

        btnDelete.addActionListener(e -> {
            try {

                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa.");
                    return;
                }
                long id = (long) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Xóa sản phẩm đã chọn?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION && productController.delete(id)) {
                    loadProducts();
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                }
            } catch (Exception err) {
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại!");
            }
        });

        btnSave.addActionListener(e -> saveProduct());
        btnCancel.addActionListener(e -> toggleEditMode(false));
    }

    // ======== Utility Methods ========
    private void fillFormFromRow(int row) {
        selectedId = (long) model.getValueAt(row, 0);
        tfName.setText((String) model.getValueAt(row, 1));
        tfPrice.setText(model.getValueAt(row, 2).toString());
        tfStock.setText(model.getValueAt(row, 3).toString());
        tfDescription.setText((String) model.getValueAt(row, 4));
    }

    private void saveProduct() {
        try {
            String name = tfName.getText().trim();
            double price = Double.parseDouble(tfPrice.getText().trim());
            int stock = Integer.parseInt(tfStock.getText().trim());
            String description = tfDescription.getText().trim();

            boolean success;
            if (!isEditing) {
                success = productController.create(name, price, description, stock);
            } else {
                success = productController.update(selectedId, name, price, description, stock);
            }

            if (success) {
                loadProducts();
                clearForm();
                toggleEditMode(false);
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Lưu thất bại!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số nhập vào không hợp lệ.");
        }
    }

    private void loadProducts() {
        model.setRowCount(0);
        List<Product> products = productController.getAll();
        for (Product p : products) {
            model.addRow(new Object[] {
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getStockQuantity(),
                    p.getDescription()
            });
        }
    }

    private void clearForm() {
        selectedId = -1;
        tfName.setText("");
        tfPrice.setText("");
        tfStock.setText("");
        tfDescription.setText("");
        table.clearSelection();
    }

    private void toggleEditMode(boolean showForm) {
        formPanel.setVisible(showForm);
        btnSave.setVisible(showForm);
        btnCancel.setVisible(showForm);

        btnAdd.setEnabled(!showForm);
        btnEdit.setEnabled(!showForm);
        btnDelete.setEnabled(!showForm);
        table.setEnabled(!showForm);
    }
}
