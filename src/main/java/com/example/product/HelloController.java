package com.example.product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private ChoiceBox<String> loaiHangChoiceBox;
    @FXML
    private TextField khachHangTextField;
    @FXML
    private TextField diaChiTextField;
    @FXML
    private TextField soDienThoaiTextField;

    @FXML
    private TableView<MatHang> table;

    @FXML
    private TableColumn<MatHang, String> namecolumn;

    @FXML
    private TableColumn<MatHang, Integer> soluongcolumn;

    @FXML
    private TableColumn<MatHang, Integer> dongiacolumn;

    @FXML
    private TableColumn<MatHang, Integer> thanhtiencolumn;

    @FXML
    private TextField tongTienTextField;

    @FXML
    private TextField nguoiNhanHang ;

    @FXML
    private DatePicker ngayNhanHang;

    @FXML
    private Button luuButton;


    private ListView<String> customerListView;  // Danh sách hiển thị khách hàng

    private ObservableList<MatHang> matHangList;// danh sách mặt hàng trogn bảng
    private ObservableList<MatHang> availableProducts;//danh sách sản phẩm có sẵn trong csdl

    private Popup customerPopup; //Popup chứa danh sách khách hàng
    private Popup popupMatHang; // Popup chứa danh sách mặt hàng

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//PHẦN KHÁCH HÀNG-------------------------------------------------------------------------------------------------
        // Khởi tạo lựa chọn loại hàng
        loaiHangChoiceBox.setItems(FXCollections.observableArrayList("Hàng nhập", "Hàng xuất"));

        // xử lýkhi nhấn vào khachHangTextField
        khachHangTextField.setOnMouseClicked(event -> {
            System.out.println("chọn khách hàng");
            // viết phần xử lý danh sách khách hàng NHập hoặc Xuất
            //
        });
//END PHẦN KHÁCH HÀNG---------------------------------------------------------------------------


//phan table--------------------------------------------------------------------------
        // Thiết lập độ rộng cho các cột
        namecolumn.setPrefWidth(150);
        soluongcolumn.setPrefWidth(100);
        dongiacolumn.setPrefWidth(100);
        thanhtiencolumn.setPrefWidth(100);

        // Tạo danh sách các mặt hàng trống ban đầu
        matHangList = FXCollections.observableArrayList();

        // Danh sách mặt hàng có sẵn
        availableProducts = FXCollections.observableArrayList(
                new MatHang("Tôn 3 ly", 19000),
                new MatHang("Xi măng", 50000),
                new MatHang("Gạch đỏ", 1200)
        );

        // Tạo Popup chứa danh sách mặt hàng
        popupMatHang = new Popup();
        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(
                availableProducts.stream().map(MatHang::getName).toList()
        ));

        // Xử lý khi chọn mặt hàng trong Popup
        listView.setOnMouseClicked(event -> {
            String selectedProductName = listView.getSelectionModel().getSelectedItem();
            if (selectedProductName != null) {
                availableProducts.stream()
                        .filter(product -> product.getName().equals(selectedProductName))
                        .findFirst()
                        .ifPresent(selectedProduct -> {
                            // Thêm mặt hàng mới vào bảng với số lượng mặc định là 1
                            MatHang newMatHang = new MatHang(selectedProduct.getName(), selectedProduct.getPrice());
                            newMatHang.setSoluong(1);
                            matHangList.add(newMatHang);
                        });
                popupMatHang.hide(); // Ẩn Popup sau khi chọn
            }
        });

        // Đặt ListView vào Popup
        popupMatHang.getContent().add(listView);

        // Thiết lập tiêu đề của cột "Mặt Hàng" và đặt sự kiện click
        Label columnHeader = new Label("Mặt Hàng");
        columnHeader.setOnMouseClicked(this::showPopup); // Hiển thị Popup khi nhấp vào tiêu đề
        namecolumn.setGraphic(columnHeader); // Đặt Graphic cho cột để có thể lắng nghe sự kiện

        // Thiết lập các cột còn lại
        namecolumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        soluongcolumn.setCellValueFactory(cellData -> cellData.getValue().soluongProperty().asObject());
        dongiacolumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        thanhtiencolumn.setCellValueFactory(cellData -> cellData.getValue().thanhtienProperty().asObject());

        // Cho phép người dùng chỉnh sửa cột số lượng
        soluongcolumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        soluongcolumn.setOnEditCommit(event -> {
            MatHang matHang = event.getRowValue();
            matHang.setSoluong(event.getNewValue());
            table.refresh();
            tinhTongTien();
        });

        //xoa mat hang
        table.setOnKeyPressed(event -> {
            System.out.println("Phím được nhấn: " + event.getCode()); // In ra phím được nhấn để kiểm tra
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                MatHang selectedItem = table.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    System.out.println("dang chon mat hang"); // In ra thông báo khi hàng được chọn
                    matHangList.remove(selectedItem); // Xóa mặt hàng đã chọn
                    table.setItems(matHangList); // Cập nhật lại TableView
                    tinhTongTien();
                }
            }
        });

        table.requestFocus();
        table.setItems(matHangList);
        table.setEditable(true); // Cho phép chỉnh sửa TableView

        /// END TABLE----------------------------------------------------------------------------------



        //PHẦN NGƯỜI NHẬN, TIME------------------------------------------------
        nguoiNhanHang.setOnMouseClicked(event->{
            System.out.println("chon nguoi nhan hang");
            ////xu ly
        });
        //END PHẦN NGƯỜI NHẬN ,TIME-----------------------------------------------


        //PHẦN NÚT LƯU--------------------------------------------
        luuButton.setOnMouseClicked(mouseEvent -> {
            System.out.println("nút lưu vào csdl");
            //xử lý lưu vào csdl
        });
        //END PHẦN NÚT LƯU---------------------------------------------
    }




    // Hiển thị Popup tại vị trí cụ thể khi nhấp vào tiêu đề cột "Mặt Hàng"
    private void showPopup(MouseEvent event) {
        // Kiểm tra nếu popupMatHang đã hiển thị thì không mở lại
        if (popupMatHang.isShowing()) {
            return; // Thoát khỏi phương thức nếu popupMatHang đã được hiển thị
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("product_popup.fxml"));
            Parent popupRoot = loader.load();

            PopUpController popupController = loader.getController();
            popupController.setHelloController(this); // Truyền tham chiếu
            // Truyền danh sách sản phẩm

            Scene popupScene = new Scene(popupRoot);
            Stage popupStage = new Stage();
            popupStage.setScene(popupScene);
            popupStage.setTitle("Chọn Mặt Hàng");
            popupStage.setResizable(false);
            popupStage.setX(event.getScreenX());
            popupStage.setY(event.getScreenY());
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addProductToTable(MatHang product) {
        boolean exists = matHangList.stream()
                .anyMatch(matHang -> matHang.getName().equals(product.getName()));

        if (exists) {
            // Nếu sản phẩm đã có trong danh sách, không thêm mới mà chỉ tăng số lượng
            MatHang existingProduct = matHangList.stream()
                    .filter(matHang -> matHang.getName().equals(product.getName()))
                    .findFirst().orElse(null);
            if (existingProduct != null) {
                existingProduct.setSoluong(existingProduct.getSoluong() + 1);
                tinhTongTien();
            }
        } else {
            // Thêm mặt hàng mới vào danh sách
            MatHang newMatHang = new MatHang(product.getName(), product.getPrice());
            newMatHang.setSoluong(1);
            matHangList.add(newMatHang);
            tinhTongTien();
        }
        table.setItems(matHangList); // Cập nhật lại TableView
    }

    private void tinhTongTien() {
        int tongTien = matHangList.stream()
                .mapToInt(matHang -> matHang.getPrice() * matHang.getSoluong())
                .sum();
        tongTienTextField.setText(String.valueOf(tongTien));
    }
}
