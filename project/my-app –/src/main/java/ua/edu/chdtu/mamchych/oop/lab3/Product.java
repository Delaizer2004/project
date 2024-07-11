package ua.edu.chdtu.mamchych.oop.lab3;

public class Product {
    private String name;
    private double price;
    private String imagePath;
    private String description;

    public Product(String name, double price, String imagePath, String description) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " - $" + price;
    }
}