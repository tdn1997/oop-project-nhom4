package oop.project.nhom8.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import oop.project.nhom8.dao.CustomerDAO;
import oop.project.nhom8.model.Customer;
import oop.project.nhom8.view.IndexFrame;
import oop.project.nhom8.view.ProductFrame;

public class CustomerController implements ActionListener {

    private final IndexFrame view;
    private final CustomerDAO dao;

    public CustomerController(IndexFrame view, CustomerDAO dao) {
        this.view = view;
        this.dao = dao;
    }

    // Phương thức để tải dữ liệu ban đầu
    public void loadDataToView() {
        List<Customer> customers = dao.getAll();
        view.updateTable(customers);
    }

    // Xử lý tất cả sự kiện
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Thêm":
                view.showAddMode();
                break;
            case "Sửa":
                view.showUpdateMode();
                break;
            case "Xóa":
                handleDelete();
                break;
            case "OK":
                handleSave();
                break;
            case "Hủy":
                view.cancelAction();
                break;
            case "Tìm":
                handleSearch();
                break;
            case "Làm mới":
                handleReset();
                break;
            case "Thống kê":
                handleShowStats();
                break;
            case "Sản phẩm":
                handleShowProducts();
                break;
            case "Xuất dữ liệu":
                handleExport();
                break;
        }
    }

    private void handleDelete() {
        String id = view.getSelectedCustomerId();
        if (id == null) {
            view.showMessage("Vui lòng chọn một khách hàng để xóa.");
            return;
        }
        if (view.showConfirmDialog("Xác Nhận Xóa", "Bạn có chắc chắn muốn xóa không?")) {
            dao.deleteId(id);
            loadDataToView();
            view.cancelAction();
        }
    }

    private void handleSave() {
        Customer kh = view.getCustomerFromInput();
        if (kh == null) {
            return;
        }

        // Kiểm tra Số điện thoại
        String phone = kh.getPhone();
        if (!phone.matches("^\\d+$")) {
            view.showMessage("Số điện thoại không hợp lệ. Vui lòng chỉ nhập số.");
            return;
        }

        // Kiểm tra định dạng Ngày sinh
        String dob = kh.getDateOfBirth();
        if (dob != null && !dob.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false); // Bật chế độ kiểm tra nghiêm ngặt
                sdf.parse(dob); // Thử phân tích chuỗi
            } catch (ParseException e) {
                view.showMessage("Ngày sinh không hợp lệ. Vui lòng nhập đúng định dạng dd/MM/yyyy.");
                return;
            }
        }

        // Kiểm tra trùng ID
        if (!view.isUpdateMode()) {
            if (dao.isIdExists(kh.getId())) {
                view.showMessage("ID khách hàng này đã tồn tại. Vui lòng nhập ID khác.");
                return;
            }
        }

        // Nếu tất cả validation đều qua tiến hành lưu
        if (view.isUpdateMode()) {
            dao.updateId(kh);
        } else {
            dao.insert(kh);
        }
        loadDataToView();
        view.cancelAction();
    }

    private void handleSearch() {
        String[] searchInfo = view.getSearchInfo();
        List<Customer> customers = dao.search(searchInfo[0], searchInfo[1]);
        view.updateTable(customers);
    }

    private void handleReset() {
        view.clearSearchField();
        loadDataToView();
    }

    private void handleShowProducts() {
        ProductFrame frame = new ProductFrame();
        frame.setVisible(true);
    }

    private void handleShowStats() {
        Map<Integer, Integer> data = dao.getBirthYearCounts();
        if (data.isEmpty()) {
            view.showMessage("Không có đủ dữ liệu để tạo biểu đồ!");
            return;
        }
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
            dataset.setValue("Năm " + entry.getKey(), entry.getValue());
        }
        JFreeChart pieChart = ChartFactory.createPieChart(
                "BIỂU ĐỒ TỈ LỆ KHÁCH HÀNG THEO NĂM SINH", dataset, true, true, false);

        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        JFrame chartFrame = new JFrame("Biểu Đồ Thống Kê");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setContentPane(chartPanel);
        chartFrame.pack();
        chartFrame.setLocationRelativeTo(view); // Hiển thị so với cửa sổ chính
        chartFrame.setVisible(true);
    }

    private void handleExport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file CSV");
        fileChooser.setSelectedFile(new File("danh_sach_khach_hang.csv"));

        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }
            try (FileOutputStream fos = new FileOutputStream(fileToSave);
                    OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                    PrintWriter pw = new PrintWriter(osw)) {

                osw.write('\uFEFF'); // BOM for Excel
                TableModel model = view.getTableModel();

                for (int i = 0; i < model.getColumnCount(); i++) {
                    pw.print(model.getColumnName(i));
                    if (i < model.getColumnCount() - 1) {
                        pw.print(",");
                    }
                }
                pw.println();

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        pw.print(model.getValueAt(i, j).toString());
                        if (j < model.getColumnCount() - 1) {
                            pw.print(",");
                        }
                    }
                    pw.println();
                }
                view.showMessage("Xuất dữ liệu CSV thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                view.showMessage("Có lỗi xảy ra khi xuất file!");
            }
        }
    }
}
