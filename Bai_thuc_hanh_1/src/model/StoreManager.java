package model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class StoreManager extends JFrame {
    private List<Product> products = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private DecimalFormat moneyFormat = new DecimalFormat("#,### VND");

    public StoreManager() {
        setTitle("🛍️ Quản Lý Sản Phẩm Cửa Hàng");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== MENU BAR =====
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Thoát");
        exitItem.addActionListener(e -> System.exit(0));
        menuFile.add(exitItem);

        JMenu menuHelp = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Phần mềm Quản lý sản phẩm\nCode by An 💻",
                "About", JOptionPane.INFORMATION_MESSAGE));
        menuHelp.add(aboutItem);

        menuBar.add(menuFile);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);

        // ===== TOOLBAR =====
        JToolBar toolBar = new JToolBar();
        JButton addBtn = new JButton("➕ Thêm");
        JButton discountBtn = new JButton("💸 Giảm giá");
        JButton orderBtn = new JButton("🛒 Đặt hàng");
        JButton sortBtn = new JButton("⬇️ Sắp giá");
        JButton filterBtn = new JButton("📂 Lọc");
        JButton valueBtn = new JButton("📊 Giá trị");
        JButton deleteBtn = new JButton("🗑️ Xóa");
        

        toolBar.add(deleteBtn);
        toolBar.add(addBtn);
        toolBar.add(discountBtn);
        toolBar.add(orderBtn);
        toolBar.add(sortBtn);
        toolBar.add(filterBtn);
        toolBar.add(valueBtn);

        add(toolBar, BorderLayout.NORTH);

        String[] columns = {"Ảnh", "Tên", "Giá", "Mô tả", "Danh mục", "Tồn kho"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        table.setRowHeight(60);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof ImageIcon) {
                    setIcon((ImageIcon) value);
                    setText("");
                } else {
                    super.setValue(value);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);


        JScrollPane scrollPane1 = new JScrollPane(table);
        add(scrollPane1, BorderLayout.CENTER);

        // ===== SỰ KIỆN =====
        addBtn.addActionListener(e -> addProduct());
        discountBtn.addActionListener(e -> applyDiscount());
        orderBtn.addActionListener(e -> orderProduct());
        sortBtn.addActionListener(e -> sortByPrice());
        filterBtn.addActionListener(e -> filterByCategory());
        valueBtn.addActionListener(e -> calculateInventoryValue());
        deleteBtn.addActionListener(e -> deleteProduct());

     // ===== DỮ LIỆU MẪU =====
        products.add(new Product("Áo thun", 120000, "Áo thun nam nữ", "Thời trang", 50,
                loadImage("/aothun.jpg")));
        products.add(new Product("Laptop", 15000000, "Laptop gaming", "Điện tử", 10,
                loadImage("/laptop.jpg")));
        products.add(new Product("Tai nghe", 500000, "Tai nghe Bluetooth", "Điện tử", 30,
                loadImage("/tainghe.jpg")));
        products.add(new Product("Máy quạt", 8100, "Máy quạt", "Gia dụng", 999,
                loadImage("/mayquat.jpg")));




        refreshTable();
    }
    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa sản phẩm này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            products.remove(row);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Đã xóa sản phẩm thành công!");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Product p : products) {
        	tableModel.addRow(new Object[]{
        	        p.getImage(),   
        	        p.getName(),
        	        moneyFormat.format(p.getPrice()),
        	        p.getDescription(),
        	        p.getCategory(),
        	        p.getStock()
        	});

        }
    }
    private ImageIcon loadImage(String resourcePath) {
        java.net.URL url = getClass().getResource(resourcePath);
        if (url == null) {
            System.err.println("⚠️ Không tìm thấy resource: " + resourcePath);
            return null;
        }
       

        ImageIcon icon = new ImageIcon(url);
        if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            System.err.println("❌ Ảnh lỗi hoặc không đọc được: " + resourcePath);
            return null;
        }

        Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }


    private void addProduct() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField descField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField stockField = new JTextField();

        JButton imageBtn = new JButton("Chọn ảnh");
        final ImageIcon[] selectedImage = {null};

        imageBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                ImageIcon img = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());
                Image scaled = img.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                selectedImage[0] = new ImageIcon(scaled);

                imageBtn.setText("Đã chọn ✔");
            }
        });

        Object[] fields = {
        		"Ảnh",imageBtn,
                "Tên:", nameField,
                "Giá:", priceField,
                "Mô tả:", descField,
                "Danh mục:", categoryField,
                "Tồn kho:", stockField
               
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Thêm sản phẩm", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            products.add(new Product(
                    nameField.getText(),
                    Double.parseDouble(priceField.getText()),
                    descField.getText(),
                    categoryField.getText(),
                    Integer.parseInt(stockField.getText()),
                    selectedImage[0]
            ));
            refreshTable();
        }
    }


    private void applyDiscount() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm để giảm giá!");
            return;
        }
        String discountStr = JOptionPane.showInputDialog(this, "Nhập % giảm giá:");
        if (discountStr != null) {
            double discount = Double.parseDouble(discountStr);
            Product p = products.get(row);
            p.setPrice(p.getPrice() * (1 - discount / 100));
            refreshTable();
        }
    }

    private void orderProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm để đặt!");
            return;
        }
        Product p = products.get(row);
        String qtyStr = JOptionPane.showInputDialog(this, "Nhập số lượng muốn đặt:");
        if (qtyStr != null) {
            int qty = Integer.parseInt(qtyStr);
            if (qty > p.getStock()) {
                JOptionPane.showMessageDialog(this, "Không đủ hàng trong kho!");
            } else {
                p.setStock(p.getStock() - qty);
                double total = qty * p.getPrice();
                JOptionPane.showMessageDialog(this, "Đặt thành công! Tổng tiền: " + moneyFormat.format(total));
                refreshTable();
            }
        }
    }

    private boolean sortAsc = true; 

    private void sortByPrice() {
        if (sortAsc) {
            products = products.stream()
                    .sorted(Comparator.comparingDouble(Product::getPrice))
                    .collect(Collectors.toList());
        } else {
            products = products.stream()
                    .sorted(Comparator.comparingDouble(Product::getPrice).reversed())
                    .collect(Collectors.toList());
        }
        sortAsc = !sortAsc;
        refreshTable();
    }


    private void filterByCategory() {
        Set<String> categories = new HashSet<>();
        for (Product p : products) {
            categories.add(p.getCategory());
        }

        if (categories.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có danh mục nào!");
            return;
        }

        // Tạo combo box để chọn
        JComboBox<String> combo = new JComboBox<>(categories.toArray(new String[0]));
        int option = JOptionPane.showConfirmDialog(this, combo, "Chọn danh mục cần lọc", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String selectedCategory = (String) combo.getSelectedItem();
            tableModel.setRowCount(0);
            for (Product p : products) {
                if (p.getCategory().equalsIgnoreCase(selectedCategory)) {
                    tableModel.addRow(new Object[]{
                    		p.getImage(),
                            p.getName(),
                            moneyFormat.format(p.getPrice()),
                            p.getDescription(),
                            p.getCategory(),
                            p.getStock()
                    });
                }
            }
        }
    }


    private void calculateInventoryValue() {
        Map<String, Double> categoryValue = new HashMap<>();
        for (Product p : products) {
            categoryValue.put(p.getCategory(),
                    categoryValue.getOrDefault(p.getCategory(), 0.0) + p.getPrice() * p.getStock());
        }

        StringBuilder result = new StringBuilder("Giá trị tồn kho:\n");
        for (Map.Entry<String, Double> entry : categoryValue.entrySet()) {
            result.append(entry.getKey())
                  .append(": ")
                  .append(moneyFormat.format(entry.getValue()))
                  .append("\n");
        }
        JOptionPane.showMessageDialog(this, result.toString());
    }
}
