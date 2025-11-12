package oop.project.nhom8.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import oop.project.nhom8.controller.PurchaseController;
import oop.project.nhom8.model.Customer;

public class IndexFrame extends JFrame {

    // Định nghĩa màu sắc và font chữ
    private final Color COLOR_BG = new Color(25, 25, 112);
    private final Color COLOR_PRIMARY = new Color(0, 0, 128);
    private final Color COLOR_SECONDARY = new Color(0, 120, 215);
    private final Color COLOR_TEXT_LIGHT = Color.WHITE;
    private final Font FONT_MAIN = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 14);

    private boolean isUpdate = false;
    private JTable table;
    private JTextField tfId, tfName, tfPhone, tfDateOfBirth;
    private JTextField tfSearch;
    private JComboBox<String> cbSearchType;
    private JComboBox<String> cbSortOptions;
    private TableRowSorter<DefaultTableModel> sorter;

    // Các nút bấm
    private JButton btnOk, btnCancel;
    private JButton btnAdd, btnUpdate, btnDelete;
    private JButton btnSearch, btnReset;
    private JButton btnStats, btnExport, btnProduct;
    private JLabel sidebarTitleLabel; // Để thay đổi tiêu đề

    public IndexFrame() {
        setTitle("Quản Lý Khách Hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1124, 650));
        setLocationRelativeTo(null);

        JPanel sidebarPanel = createSidebarPanel();
        JPanel contentPanel = createContentPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, contentPanel);
        splitPane.setDividerLocation(350);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(5);

        add(splitPane);
    }

    // Phương thức gắn controller
    public void setController(ActionListener controller) {
        // Gắn controller cho tất cả các nút
        btnAdd.addActionListener(controller);
        btnUpdate.addActionListener(controller);
        btnDelete.addActionListener(controller);
        btnOk.addActionListener(controller);
        btnCancel.addActionListener(controller);
        btnSearch.addActionListener(controller);
        btnReset.addActionListener(controller);
        btnStats.addActionListener(controller);
        btnExport.addActionListener(controller);
        btnProduct.addActionListener(controller);
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel(new BorderLayout(10, 20));
        sidebar.setBackground(COLOR_BG);
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        sidebarTitleLabel = new JLabel("Thông Tin Khách Hàng", SwingConstants.CENTER);
        sidebarTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sidebarTitleLabel.setForeground(COLOR_TEXT_LIGHT);
        sidebar.add(sidebarTitleLabel, BorderLayout.NORTH);

        // Panel chứa các ô nhập liệu
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        String[] labels = { "ID Khách Hàng:", "Họ và Tên:", "Số Điện Thoại:", "Ngày Sinh (dd/mm/yyyy):" };
        tfId = new JTextField();
        tfName = new JTextField();
        tfPhone = new JTextField();
        tfDateOfBirth = new JTextField();

        JTextField[] textFields = { tfId, tfName, tfPhone, tfDateOfBirth };
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(FONT_LABEL);
            label.setForeground(COLOR_TEXT_LIGHT);
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            inputPanel.add(label, gbc);
            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.weightx = 1;
            styleTextField(textFields[i]);
            inputPanel.add(textFields[i], gbc);
        }

        // Panel cho nút OK và Cancel
        JPanel confirmPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        confirmPanel.setOpaque(false);
        btnOk = createButton("OK");
        btnCancel = createButton("Hủy");

        styleSideButton(btnOk);
        styleSideButton(btnCancel);
        confirmPanel.add(btnOk);
        confirmPanel.add(btnCancel);
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        inputPanel.add(confirmPanel, gbc);
        sidebar.add(inputPanel, BorderLayout.CENTER);

        // Panel chứa các nút
        JPanel actionButtonPanel = new JPanel(new GridLayout(1, 3, 10, 5));
        actionButtonPanel.setOpaque(false);

        btnAdd = createButton("Thêm");
        btnUpdate = createButton("Sửa");
        btnDelete = createButton("Xóa");

        styleSideButton(btnAdd);
        styleSideButton(btnUpdate);
        styleSideButton(btnDelete);

        actionButtonPanel.add(btnAdd);
        actionButtonPanel.add(btnUpdate);
        actionButtonPanel.add(btnDelete);

        sidebar.add(actionButtonPanel, BorderLayout.SOUTH);
        setDisplayInput(false, false);
        return sidebar;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Danh Sách Khách Hàng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(COLOR_PRIMARY);
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setOpaque(false);
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.add(createSearchPanel());
        controlsPanel.add(createSortAndStatsPanel());

        JPanel tableWrapper = new JPanel(new BorderLayout(10, 10));
        tableWrapper.setOpaque(false);
        tableWrapper.add(controlsPanel, BorderLayout.NORTH);
        tableWrapper.add(createTablePanel(), BorderLayout.CENTER);

        contentPanel.add(tableWrapper, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(""));

        Font searchFont = new Font("Segoe UI", Font.PLAIN, 15);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbSearch = new JLabel("Tìm theo:");
        lbSearch.setFont(searchFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(lbSearch, gbc);

        cbSearchType = new JComboBox<>(new String[] { "ID", "Họ tên", "Ngày sinh" });
        cbSearchType.setFont(searchFont);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(cbSearchType, gbc);

        tfSearch = new JTextField();
        tfSearch.setFont(searchFont);
        tfSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        panel.add(tfSearch, gbc);

        btnSearch = new JButton("Tìm");

        btnSearch.setFont(searchFont);
        btnSearch.setPreferredSize(new Dimension(100, 35));
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(btnSearch, gbc);

        btnReset = new JButton("Làm mới");
        btnReset.setFont(searchFont);
        btnReset.setPreferredSize(new Dimension(100, 35));
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(btnReset, gbc);

        styleActionButton(btnSearch);
        styleActionButton(btnReset);

        return panel;
    }

    private JPanel createSortAndStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(createSortPanel(), BorderLayout.WEST);

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsPanel.setOpaque(false);

        btnProduct = createButton("Sản phẩm");
        styleActionButton(btnProduct);

        btnStats = createButton("Thống kê");
        styleActionButton(btnStats);

        btnExport = createButton("Xuất dữ liệu");
        styleActionButton(btnExport);

        statsPanel.add(btnProduct);
        statsPanel.add(btnStats);
        statsPanel.add(btnExport);
        panel.add(statsPanel, BorderLayout.EAST);

        return panel;
    }

    private CustomerPurchaseFrame customerPurchase;

    public void updateTable(List<Customer> customers) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(new String[] { "ID", "Họ tên", "Số điện thoại (+84)", "Ngày sinh", "Số đơn hàng" });

        for (Customer c : customers) {
            model.addRow(new Object[] {
                    c.getId(),
                    c.getName(),
                    c.getPhone(),
                    c.getDateOfBirth(),
                    c.getPurchaseCount()
            });
        }
        table.setModel(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        Collator viCollator = Collator.getInstance(new Locale("vi", "VN"));

        DefaultTableCellRenderer clickableRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);

                int purchaseCount = (int) table.getValueAt(row, 4);

                if (purchaseCount > 0) {
                    lbl.setForeground(new Color(0, 102, 204));
                    // bold + underline
                    Font fnt = lbl.getFont().deriveFont(Font.BOLD);
                    Map<TextAttribute, Object> attrs = new HashMap<>(fnt.getAttributes());
                    attrs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    lbl.setFont(fnt.deriveFont(attrs));
                } else {
                    // disabled look
                    lbl.setForeground(Color.GRAY);
                    lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN));
                }

                Map<TextAttribute, Object> attributes = new java.util.HashMap<>(lbl.getFont().getAttributes());
                lbl.setFont(lbl.getFont().deriveFont(attributes));

                return lbl;
            }
        };
        table.getColumnModel().getColumn(4).setCellRenderer(clickableRenderer);

        sorter.setComparator(1, new Comparator<String>() {
            @Override
            public int compare(String name1, String name2) {
                String firstName1 = getFirstName(name1);
                String firstName2 = getFirstName(name2);
                return viCollator.compare(firstName1, firstName2);
            }
        });

        if (cbSortOptions != null) {
            cbSortOptions.setSelectedIndex(0);
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col == 4 && row != -1) {
                    int purchaseCount = (int) table.getValueAt(row, 4);

                    if (purchaseCount == 0) {
                        return; // disabled
                    }

                    if (customerPurchase != null) {
                        customerPurchase.setVisible(false);
                    }

                    String customerId = (String) table.getValueAt(row, 0);
                    String customerName = (String) table.getValueAt(row, 1);

                    customerPurchase = new CustomerPurchaseFrame(customerId, customerName);
                    PurchaseController controller = new PurchaseController(customerPurchase);
                    controller.loadDataToView(customerId);

                    customerPurchase.setVisible(true);
                }
            }
        });
    }

    public void showAddMode() {
        clear();
        isUpdate = false;
        setDisplayInput(true, false);
        sidebarTitleLabel.setText("Thêm Khách Hàng Mới");
    }

    public void showUpdateMode() {
        if (table.getSelectedRow() >= 0) {
            isUpdate = true;
            setDisplayInput(true, true); // (true, true) sẽ fill dữ liệu
            sidebarTitleLabel.setText("Cập Nhật Thông Tin");
        } else {
            showMessage("Vui lòng chọn một khách hàng để cập nhật.");
        }
    }

    public void cancelAction() {
        clear();
        isUpdate = false;
        setDisplayInput(false, false);
        sidebarTitleLabel.setText("Thông Tin Khách Hàng");
    }

    public Customer getCustomerFromInput() {
        String id = tfId.getText().trim().toUpperCase();
        String name = tfName.getText().trim();
        String dateOfBirth = tfDateOfBirth.getText().trim();
        String phone = tfPhone.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            showMessage("ID và Tên không được để trống!");
            return null;
        }

        if (phone.isEmpty()) {
            showMessage("Số điện thoại không được để trống!");
            return null;
        }

        return new Customer(id, name, phone, dateOfBirth, 0);
    }

    public String getSelectedCustomerId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return null;
        }
        return table.getValueAt(row, 0).toString();
    }

    public String[] getSearchInfo() {
        String columnName = "";
        switch (cbSearchType.getSelectedItem().toString()) {
            case "ID":
                columnName = "id";
                break;
            case "Họ tên":
                columnName = "name";
                break;
            case "Ngày sinh":
                columnName = "date_of_birth";
                break;
        }
        String query = tfSearch.getText().trim();
        return new String[] { columnName, query };
    }

    public void clearSearchField() {
        tfSearch.setText("");
    }

    public TableModel getTableModel() {
        return table.getModel();
    }

    public boolean isUpdateMode() {
        return this.isUpdate;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public boolean showConfirmDialog(String title, String message) {
        int choice = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    private JScrollPane createTablePanel() {
        table = new JTable();
        table.getTableHeader().setFont(FONT_MAIN);
        // Sửa lại màu tiêu đề bảng cho đẹp hơn
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(COLOR_PRIMARY);
        table.setRowHeight(25);
        table.setFont(FONT_LABEL);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(COLOR_SECONDARY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return scrollPane;
    }

    private JPanel createSortPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setOpaque(false);
        panel.add(new JLabel("Sắp xếp theo:"));
        String[] sortOptions = { "Mặc định", "Mới nhất", "Theo tên (A-Z)" };
        cbSortOptions = new JComboBox<>(sortOptions);
        panel.add(cbSortOptions);

        cbSortOptions.addActionListener(e -> {
            if (sorter == null) {
                return;
            }
            String selection = (String) cbSortOptions.getSelectedItem();
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();

            int columnIndex;
            switch (selection) {
                case "Mới nhất":
                    columnIndex = 0; // Cột 'ID' ở vị trí 0
                    sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.DESCENDING));
                    break;

                case "Theo tên (A-Z)":
                    columnIndex = 1; // Cột 'Name' ở vị trí 1
                    sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING));
                    break;

                default:
                    sorter.setSortKeys(null);
                    return;
            }
            sorter.setSortKeys(sortKeys);
            sorter.sort();
        });
        return panel;
    }

    private void setDisplayInput(boolean display, boolean isUpdateMode) {
        tfId.setEnabled(display && !isUpdateMode);
        tfName.setEnabled(display);
        tfPhone.setEnabled(display);
        tfDateOfBirth.setEnabled(display);
        btnOk.setEnabled(display);
        btnCancel.setEnabled(display);

        if (isUpdateMode && table.getSelectedRow() >= 0) {
            int row = table.getSelectedRow();
            tfId.setText(table.getValueAt(row, 0).toString());
            tfName.setText(table.getValueAt(row, 1).toString());
            tfPhone.setText(table.getValueAt(row, 2).toString());
            tfDateOfBirth.setText(table.getValueAt(row, 3).toString());
        }
    }

    private void clear() {
        tfId.setText("");
        tfName.setText("");
        tfPhone.setText("");
        tfDateOfBirth.setText("");
        table.clearSelection();
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        // btn.addActionListener((ActionListener) this);
        return btn;
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
        btn.setForeground(COLOR_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSideButton(JButton btn) {
        btn.setFont(FONT_MAIN);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 12, 12, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
    }

    private String getFirstName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullName.trim().split("\\s+");
        return parts[parts.length - 1];
    }
}
