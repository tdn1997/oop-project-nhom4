package oop.project.nhom4;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.util.Map;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import javax.swing.table.TableModel;


public class Nhom4 extends JFrame implements ActionListener {

    // Định nghĩa màu sắc và font chữ ---
    private final Color COLOR_PRIMARY = new Color(0,0,128);
    private final Color COLOR_SECONDARY = new Color(0, 120, 215);
    private final Color COLOR_TEXT_LIGHT = Color.WHITE;
    private final Font FONT_MAIN = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 14);

    private boolean isUpdate = false;
    private JTable table;
    private JTextField tfId, tfName, tfPhone, tfDateOfBirth;
    private JTextField tfSearch;
    private JComboBox<String> cbSearchType;
    private JButton btnOk;
    private JButton btnCancel;
    MyConnect myConnect = new MyConnect();
    
    private JComboBox<String> cbSortOptions; // ComboBox cho sắp xếp
    private TableRowSorter<DefaultTableModel> sorter; // Đối tượng xử lý sắp xếp

    public Nhom4() {
        setTitle("Quản Lý Khách Hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1124, 650)); // Đặt kích thước tối thiểu
        setLocationRelativeTo(null);

        // Tạo sidebar bên trái và panel nội dung bên phải
        JPanel sidebarPanel = createSidebarPanel();
        JPanel contentPanel = createContentPanel();

        // Tạo JSplitPane để chia hai panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, contentPanel);
        splitPane.setDividerLocation(350); // Độ rộng ban đầu của sidebar
        splitPane.setResizeWeight(0.1); // Giúp sidebar ít bị thay đổi kích thước hơn khi resize cửa sổ
        splitPane.setEnabled(false); // Vô hiệu hóa việc kéo divider
        splitPane.setDividerSize(5);

        // Thêm JSplitPane vào Frame
        add(splitPane);

        // Kết nối CSDL và tải dữ liệu
        myConnect.connect();
        loadData(); // Tải dữ liệu ban đầu

        setVisible(true);
    }
    
    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel(new BorderLayout(10, 20));
        sidebar.setBackground(COLOR_PRIMARY);
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tiêu đề cho sidebar
        JLabel titleLabel = new JLabel("Thông Tin Khách Hàng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(COLOR_TEXT_LIGHT);
        sidebar.add(titleLabel, BorderLayout.NORTH);

        // Panel chứa các ô nhập liệu
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nhãn và ô nhập liệu
        String[] labels = {"ID Khách Hàng:", "Họ và Tên:", "Số Điện Thoại:", "Ngày Sinh (dd-mm-yyyy):"};
        tfId = new JTextField();
        tfName = new JTextField();
        tfPhone = new JTextField();
        tfDateOfBirth = new JTextField();
        JTextField[] textFields = {tfId, tfName, tfPhone, tfDateOfBirth};
        
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
        btnOk = createButton("Ok");
        btnCancel = createButton("Cancel");
        styleDialogButton(btnOk);
        styleDialogButton(btnCancel);
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
        
        JButton btnAdd = createButton("Add");
        JButton btnUpdate = createButton("Update");
        JButton btnDelete = createButton("Delete");
        
        styleActionButton(btnAdd);
        styleActionButton(btnUpdate);
        styleActionButton(btnDelete);
        
        actionButtonPanel.add(btnAdd);
        actionButtonPanel.add(btnUpdate);
        actionButtonPanel.add(btnDelete);
        
        sidebar.add(actionButtonPanel, BorderLayout.SOUTH);

        // Ban đầu các ô nhập liệu bị vô hiệu hóa
        setDisplayInput(false, false);
        return sidebar;
    }
    
    // Phương thức tạo panel nội dung chính bên phải.
    // Chứa tiêu đề, thanh tìm kiếm và bảng dữ liệu.
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tiêu đề chính ---
        JLabel titleLabel = new JLabel("Danh Sách Khách Hàng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(COLOR_PRIMARY);
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Tạo một panel wrapper cho các thanh điều khiển (tìm kiếm, sắp xếp)
        JPanel controlsPanel = new JPanel();
        controlsPanel.setOpaque(false);
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS)); 
        controlsPanel.add(createSearchPanel());
        
        // Thay thế createSortPanel() bằng phương thức mới
        controlsPanel.add(createSortAndStatsPanel());

        // Panel chứa thanh tìm kiếm và bảng ---
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

        // Label Tìm theo
        JLabel lbSearch = new JLabel("Tìm theo:");
        lbSearch.setFont(searchFont);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(lbSearch, gbc);

        // ComboBox tìm kiếm
        cbSearchType = new JComboBox<>(new String[]{"ID", "Name", "DateOfBirth"});
        cbSearchType.setFont(searchFont);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(cbSearchType, gbc);

        // Ô nhập tìm kiếm (co giãn)
        tfSearch = new JTextField();
        tfSearch.setFont(searchFont);
        tfSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 1.0; 
        panel.add(tfSearch, gbc);

        // Nút Search
        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(searchFont);
        btnSearch.setPreferredSize(new Dimension(100, 35));
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(btnSearch, gbc);

        // Nút Reset
        JButton btnReset = new JButton("Reset");
        btnReset.setFont(searchFont);
        btnReset.setPreferredSize(new Dimension(100, 35));
        gbc.gridx = 4; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(btnReset, gbc);
        
        btnSearch.addActionListener(e -> {
        String keyword = tfSearch.getText().trim();
        String type = cbSearchType.getSelectedItem().toString();
        
        search();
        });
        
        btnReset.addActionListener(e -> {
        tfSearch.setText("");
        cbSearchType.setSelectedIndex(0);

        // Gọi hàm reset dữ liệu (ví dụ)
        loadData();
        });

        styleActionButton(btnSearch); 
        styleActionButton(btnReset); 
     
        return panel;
    }

    // Phương thức sắp xếp khách hàng
    private JPanel createSortPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setOpaque(false);
        panel.add(new JLabel("Sắp xếp theo:"));
        
        String[] sortOptions = {"Mặc định", "Tên (A-Z)", "Ngày sinh", "ID (Tăng dần)", "Mới nhất (ID giảm dần)"};
        cbSortOptions = new JComboBox<>(sortOptions);
        
        panel.add(cbSortOptions);

        // Thêm ActionListener để xử lý sự kiện khi người dùng chọn một tùy chọn
        cbSortOptions.addActionListener(e -> {
            if (sorter == null) return; // Nếu sorter chưa được khởi tạo thì không làm gì
            sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
            String selection = (String) cbSortOptions.getSelectedItem();
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();

            int columnIndex;
            switch (selection) {
                case "Tên (A-Z)":
                    columnIndex = 1; // Cột 'Name' ở vị trí 1
                    sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING));
                    break;
                case "Ngày sinh":
                    columnIndex = 3; // Cột 'DateOfBirth' ở vị trí 3
                    sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING));
                    break;
                case "ID (Tăng dần)":
                    columnIndex = 0; // Cột 'ID' ở vị trí 0
                    sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING));
                    break;
                case "Mới nhất (ID giảm dần)":
                    columnIndex = 0; // Cột 'ID' ở vị trí 0
                    sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.DESCENDING));
                    break;
                default: // "Mặc định"
                    sorter.setSortKeys(null); // Bỏ sắp xếp
                    return;
            }
            sorter.setSortKeys(sortKeys);
            sorter.sort();
        });
        return panel;
    }

    private JScrollPane createTablePanel() {
        table = new JTable();
        table.getTableHeader().setFont(FONT_MAIN);
        table.getTableHeader().setBackground(COLOR_PRIMARY);
        table.getTableHeader().setForeground(COLOR_PRIMARY);
        table.setRowHeight(25);
        table.setFont(FONT_LABEL);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(COLOR_SECONDARY);
   
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return scrollPane;
    }
    
    // Phương tùy chỉnh giao diện
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

    private void styleDialogButton(JButton btn) {
        btn.setFont(FONT_MAIN);
        btn.setBackground(COLOR_SECONDARY);
        btn.setForeground(COLOR_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.addActionListener(this);
        return btn;
    }

    private void setDisplayInput(boolean display, boolean isUpdateMode) {
        // Nếu là chế độ update ID không được sửa
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

    private void updateTable(ResultSet rs) {
        DefaultTableModel model = new DefaultTableModel();
        try {
            ResultSetMetaData rsMD = rs.getMetaData();
            int colNumber = rsMD.getColumnCount();
            String[] arr = new String[colNumber];
            for (int i = 0; i < colNumber; i++) {
            }
            model.setColumnIdentifiers(new String[]{"ID", "Name", "Phone", "DateOfBirth"});

            while (rs.next()) {
                Object[] row = new Object[colNumber];
                for (int i = 0; i < colNumber; i++) {
                    row[i] = rs.getString(i + 1);
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setModel(model);

        // Khởi tạo hoặc cập nhật sorter với model mới
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        
        // Reset lựa chọn trong JComboBox về "Mặc định"
        if (cbSortOptions != null) {
            cbSortOptions.setSelectedIndex(0);
        }
    }

    private void loadData() {
        ResultSet rs = myConnect.getData();
        updateTable(rs);
    }
    
    private void clear() {
        tfId.setText("");
        tfName.setText("");
        tfPhone.setText("");
        tfDateOfBirth.setText("");
        table.clearSelection();
    }

    private void cancel() {
        clear();
        isUpdate = false;
        setDisplayInput(false, false);
    }
    
    private void add() {
        clear();
        isUpdate = false;
        setDisplayInput(true, false);
    }

    private void update() {
        if (table.getSelectedRow() >= 0) {
            isUpdate = true;
            setDisplayInput(true, true);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để cập nhật.", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void delete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Bạn phải chọn một hàng trong bảng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int select = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa không?", "Xác Nhận Xóa", JOptionPane.YES_NO_OPTION);
        if (select == JOptionPane.YES_OPTION) {
            myConnect.deleteId((String) table.getValueAt(row, 0));
            loadData();
            clear();
        }
    }
    
    private void addOrUpdate() {
        String id = tfId.getText().trim().toUpperCase();
        String name = tfName.getText().trim();
        String dateOfBirth = tfDateOfBirth.getText().trim();
        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID và Tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double phone = Double.parseDouble(tfPhone.getText().trim());
            Khachhang st = new Khachhang(id, name, phone, dateOfBirth);
            if (isUpdate) {
                myConnect.updateId(st.getId(), st);
            } else {
                myConnect.insert(st);
            }
            loadData();
            cancel();
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void search(){
        String query = tfSearch.getText().trim();
        String selectedCriteria = (String) cbSearchType.getSelectedItem();
        String columnName = "";
        switch (selectedCriteria) {
            case "ID": columnName = "id"; break;
            case "Name": columnName = "name"; break;
            case "DateOfBirth": columnName = "dateOfBirth"; break;
        }
        ResultSet rs = myConnect.search(columnName, query);
        updateTable(rs);
    }
    
    private JPanel createSortAndStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Phần sắp xếp được đặt ở bên trái
        panel.add(createSortPanel(), BorderLayout.WEST);

        // Nút Thống kê để căn lề phải
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsPanel.setOpaque(false);
        JButton btnStats = createButton("Thống kê");
        styleActionButton(btnStats);
        
        // Nút Xuất file
        JButton btnExport = createButton("Xuất file");
        styleActionButton(btnExport);
        
        statsPanel.add(btnStats);
        statsPanel.add(btnExport);

        // Đặt panel chứa nút Thống kê ở bên phải
        panel.add(statsPanel, BorderLayout.EAST);

        return panel;
    }
    
    // Phương thức Xuất file
    private void exportToCSV() {
        // Sử dụng JFileChooser để người dùng chọn nơi lưu file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file CSV");
        
        // Gợi ý tên file mặc định
        fileChooser.setSelectedFile(new File("danh_sach_khach_hang.csv"));
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Đảm bảo file có đuôi .csv
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            try (FileOutputStream fos = new FileOutputStream(fileToSave);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                PrintWriter pw = new PrintWriter(osw)) {
                
                // Ghi UTF-8 BOM để Excel đọc tiếng Việt
                osw.write('\uFEFF'); 

                TableModel model = table.getModel();
                // Ghi dòng tiêu đề (header)
                for (int i = 0; i < model.getColumnCount(); i++) {
                    pw.print(model.getColumnName(i));
                    if (i < model.getColumnCount() - 1) {
                        pw.print(",");
                    }
                }
                pw.println();

                // Ghi dữ liệu từng hàng
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        pw.print(model.getValueAt(i, j).toString());
                        if (j < model.getColumnCount() - 1) {
                            pw.print(",");
                        }
                    }
                    pw.println();
                }

                JOptionPane.showMessageDialog(this, "Xuất file CSV thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xuất file!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void reset() {
        tfSearch.setText("");
        loadData();
    }
    
    private void showBirthYearChart(){
        // Lấy dữ liệu từ MyConnect
        Map<Integer, Integer> data = myConnect.getBirthYearCounts();
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có đủ dữ liệu để tạo biểu đồ!");
            return;
        }

        // Tạo dataset cho biểu đồ tròn từ dữ liệu đã lấy
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
            // Thêm dữ liệu vào dataset: ("Năm 1999", 5 người)
            dataset.setValue("Năm " + entry.getKey(), entry.getValue());
        }

        // Tạo biểu đồ JFreeChart
        JFreeChart pieChart = ChartFactory.createPieChart(
                "BIỂU ĐỒ TỈ LỆ KHÁCH HÀNG THEO NĂM SINH", // Tiêu đề biểu đồ
                dataset, // Dataset
                true,    // Hiển thị legend
                true,
                false);

        // Tạo một panel để chứa biểu đồ
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Tạo một cửa sổ JFrame mới để hiển thị panel biểu đồ
        JFrame chartFrame = new JFrame("Biểu Đồ Thống Kê");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng cửa sổ này
        chartFrame.setContentPane(chartPanel);
        chartFrame.pack();
        chartFrame.setLocationRelativeTo(this); // Hiển thị giữa cửa sổ chính
        chartFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Add": add(); break;
            case "Update": update(); break;
            case "Delete": delete(); break;
            case "Search": search(); break;
            case "Reset": reset(); break;
            case "Ok": addOrUpdate(); break;
            case "Cancel": cancel(); break;
            case "Thống kê": showBirthYearChart(); break;
            case "Xuất file": exportToCSV(); break;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }
        
        SwingUtilities.invokeLater(Nhom4::new);
    }
}