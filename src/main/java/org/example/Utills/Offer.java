package org.example.Utills;

public class Offer {
    private String seller;
    private String book;
    private double price;

    public Offer(String seller, String book, double price) {
        this.seller = seller;
        this.book = book;
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public String getBook() {
        return book;
    }

    public double getPrice() {
        return price;
    }
}
