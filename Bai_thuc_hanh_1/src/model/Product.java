package model;

import javax.swing.*;

public class Product {
    private String name;
    private String description;
    private String category;
    private double price;
    private int stock;
    private ImageIcon image; 

    public Product(String name, double price, String description, String category, int stock, ImageIcon image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.stock = stock;
        this.image = image;
    }


    // Getter
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public int getStock() { return stock; }
    public ImageIcon getImage() { return image; }

    // Setter
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setImage(ImageIcon image) { this.image = image; }
}
