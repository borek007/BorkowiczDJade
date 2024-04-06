package org.example;

import org.example.Agents.ManagerAgent;
import org.example.Agents.SellerAgent;

import java.util.List;

public class Bookstore {
    private final String areaCode;
    private final String name;
    private final int id;
    private List<String> books;
    private SellerAgent sellerAgent;

    public Bookstore(String name, int id, List<String> books,String areaCode) {
        this.name = name;
        this.id = id;
        this.books = books;
        this.sellerAgent = new SellerAgent(id);
        this.areaCode = areaCode;

    }

    // getters and setters
    public String getName() {
        return name;
    }


    public List<String> getBooks() {
        return books;
    }


    public SellerAgent getSellerAgent() {
        return sellerAgent;
    }

    public void setSellerAgent(SellerAgent sellerAgent) {
        this.sellerAgent = sellerAgent;
    }

//    public ManagerAgent getManagerAgent() {
//        return managerAgent;
//    }

//    public void setManagerAgent(ManagerAgent managerAgent) {
//        this.managerAgent = managerAgent;
//    }

    public String getAreaCode() {
        return areaCode;
    }
}