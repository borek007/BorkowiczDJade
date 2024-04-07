package org.example;

import jade.core.Agent;
import org.example.Agents.ManagerAgent;
import org.example.Agents.SellerAgent;

import java.util.List;

public class Bookstore  implements java.io.Serializable{
    private final String areaCode;
    private final String name;
    private ManagerAgent managerAgent;
    private List<String> books;
    private List<Double> prices;
    private SellerAgent sellerAgent;




    public Bookstore(String name, List<String> books, List<Double>prices, String areaCode) {
        this.name = name;
        this.books = books;
        this.sellerAgent = new SellerAgent(this);
        this.managerAgent = new ManagerAgent(this);
        this.areaCode = areaCode;
        this.prices = prices;

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
    public double getPrice(String book){
        return prices.get(books.indexOf(book));
    }

    public String getAreaCode() {
        return areaCode;
    }

    public Agent getManagerAgent() {
        return managerAgent;
    }
    public Bookstore (){
        this.name = "";
        this.books = null;
        this.sellerAgent = null;
        this.managerAgent = null;
        this.areaCode = "";
        this.prices = null;
        setupAgents();
    }// Getter for prices
    public List<Double> getPrices() {
        return prices;
    }

    // Setter for prices
    public void setPrices(List<Double> prices) {
        this.prices = prices;
    }
    private void setupAgents(){
       this.sellerAgent = new SellerAgent(this);
       this.managerAgent = new ManagerAgent(this);
    }




}