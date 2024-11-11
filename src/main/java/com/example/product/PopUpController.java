package com.example.product;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

public class PopUpController {

    @FXML
    private TextField productNameField; // Trường nhập tên mặt hàng

    @FXML
    private TextField productPriceField; // Trường nhập giá

    @FXML
    private TableView<MatHang> tableView; // Bảng hiển thị mặt hàng

    @FXML
    private TableColumn<MatHang, String> nameColumn; // Cột tên mặt hàng

    @FXML
    private TableColumn<MatHang, Integer> priceColumn; // Cột giá

    private ObservableList<MatHang> availableProducts; // Danh sách mặt hàng

    private HelloController helloController;

    public void setHelloController(HelloController controller) {
        this.helloController = controller; // Thiết lập tham chiếu
    }

    public void initialize() {
        // Thiết lập độ rộng cho các cột
        nameColumn.setPrefWidth(150);
        priceColumn.setPrefWidth(100);

        // Khởi tạo danh sách mặt hàng
        availableProducts = FXCollections.observableArrayList(
                new MatHang("Tôn 3 ly", 19000),
                new MatHang("Xi măng", 50000),
                new MatHang("Gạch đỏ", 1200)
        );
        // Thiết lập cột cho bảng
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.setItems(availableProducts); // Cập nhật TableView
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra xem có phải nhấp đúp không
                MatHang selectedProduct = tableView.getSelectionModel().getSelectedItem(); // Lấy sản phẩm được chọn
                if (selectedProduct != null) {
                    // Xử lý thêm sản phẩm vào bảng hoặc thực hiện thao tác khác
                    handleDoubleClick(selectedProduct);
                }
            }
        });
        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE ||event.getCode() == KeyCode.DELETE) {
                MatHang selectedItem = tableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    availableProducts.remove(selectedItem); // Xóa mặt hàng đã chọn
                    tableView.setItems(availableProducts); // Cập nhật bảng
                }
            }
        });
    }
    private void handleDoubleClick(MatHang selectedProduct) {
        if (helloController != null) {
            helloController.addProductToTable(selectedProduct); // Gọi phương thức để thêm mặt hàng vào bảng trong HelloController
        }
    }


    @FXML
    private void handleAddProduct() {
        String name = productNameField.getText();
        String priceText = productPriceField.getText();

        if (name.isEmpty() || priceText.isEmpty()) {
            System.out.println("Vui lòng nhập đủ thông tin.");
            return;
        }

        try {
            int price = Integer.parseInt(priceText);
            MatHang newProduct = new MatHang(name, price);
            availableProducts.add(newProduct); // Thêm mặt hàng mới vào danh sách

            // Cập nhật vào bảng trong HelloController
            if (helloController != null) {
                helloController.addProductToTable(newProduct);
            }

            tableView.setItems(availableProducts); // Cập nhật bảng
            productNameField.clear(); // Xóa trường nhập
            productPriceField.clear(); // Xóa trường nhập
        } catch (NumberFormatException e) {
            System.out.println("Giá phải là một số hợp lệ.");
        }
    }
    private void handleKeyPress(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            MatHang selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                availableProducts.remove(selectedItem); // Xóa mặt hàng đã chọn
            }
        }
    }
}
