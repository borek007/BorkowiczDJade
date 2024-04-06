package org.example;

import org.example.Agents.ManagerAgent;
import org.example.Agents.SellerAgent;

import java.util.List;

public class Bookstore {
    private String name;
    private int id;
    private List<String> bookTypes;
    private SellerAgent sellerAgent;

    public Bookstore(String name, int id, List<String> bookTypes, SellerAgent sellerAgent) {
        this.name = name;
        this.id = id;
        this.bookTypes = bookTypes;
        this.sellerAgent = new SellerAgent(id);

    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBookTypes() {
        return bookTypes;
    }

    public void setBookTypes(List<String> bookTypes) {
        this.bookTypes = bookTypes;
    }

    public SellerAgent getSellerAgent() {
        return sellerAgent;
    }

    public void setSellerAgent(SellerAgent sellerAgent) {
        this.sellerAgent = sellerAgent;
    }

    public ManagerAgent getManagerAgent() {
        return managerAgent;
    }

    public void setManagerAgent(ManagerAgent managerAgent) {
        this.managerAgent = managerAgent;
    }
}