/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Tiffany Rolle
 */
/** This is the product class. */
public class Product {
    /**Declares fields*/
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private int stock;
    private double price;
    private int max;
    private int min;
    
    /** Product constructor */
    public Product(int id, String name, int stock, double price, int max, int min) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.max = max;
        this.min = min;
    }
    /**
     @return the id
     */
    public int getId() {
        return id;
    }

    /**
     @param id set the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     @return the name
     */
    public String getName() {
        return name;
    }

    /**
     @param name set the name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     @return the stock - inventory level
     */
    public int getStock() {
        return stock;
    }

    /**
     @param stock set the inventory level of the product
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     @param price set the price of the product
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     @return the maximum inventory level
     */
    public int getMax() {
        return max;
    }

    /**
     @param max sets the maximum inventory level of the product
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     @return the minimum inventory level
     */
    public int getMin() {
        return min;
    }

    /**
     @param min sets the minimum inventory level of the product
     */
    public void setMin(int min) {
        this.min = min;
    }

    /** This method adds part to associated parts array. */
    public void addAssociatedPart(Part part){

        associatedParts.add(part);
    }
    /** This method sets parts in associated part array. */
    public ObservableList<Part> getAllAssociatedParts(){

        return associatedParts;
    }

    /** This method deletes part(s) in associated part array. */
    public boolean deleteAssociatedPart(Part selectedPart) {
        if (associatedParts.size() > 0) {
            associatedParts.remove(selectedPart);
        }

        return false;
        }
    
    
}

