package com.example.product;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MatHang {
    private StringProperty name;
    private IntegerProperty soluong;
    private IntegerProperty price;
    private IntegerProperty thanhtien;

    public MatHang(String name, int price) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleIntegerProperty(price);
        this.soluong = new SimpleIntegerProperty(1);
        this.thanhtien = new SimpleIntegerProperty(soluong.get() * price);

        // Lắng nghe sự thay đổi của soluong để cập nhật thanhtien
        soluong.addListener((observable, oldValue, newValue) ->
                thanhtien.set(newValue.intValue() * price)
        );
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getSoluong() {
        return soluong.get();
    }

    public void setSoluong(int soluong) {
        this.soluong.set(soluong);
    }

    public IntegerProperty soluongProperty() {
        return soluong;
    }

    public int getPrice() {
        return price.get();
    }

    public void setPrice(int price) {
        this.price.set(price);
        // Cập nhật lại thanhtien khi thay đổi giá
        thanhtien.set(soluong.get() * price);
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public int getThanhTien() {
        return thanhtien.get();
    }

    public IntegerProperty thanhtienProperty() {
        return thanhtien;
    }
}

