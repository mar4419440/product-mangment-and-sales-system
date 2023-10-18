/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pods;

import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class product {
    
    private int id ;
    private String name ;
    private double price ;
    private int quantity ;
    private int quantitySold;
    private double PriceSold;
    private Date saleTime;
    
    public product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    public product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    //selling a product
    public product( String name, double price, int quantitySold, double PriceSold, Date saleTime){
        this.name = name;
        this.price = price;
        this.quantitySold = quantitySold;
        this.PriceSold = PriceSold;
        this.saleTime = saleTime;
    }
    public product( String name, double price, int quantitySold, double PriceSold){
        this.name = name;
        this.price = price;
        this.quantitySold = quantitySold;
        this.PriceSold = PriceSold;
    }
    public product( int id,String name, double price, int quantitySold, double PriceSold, Date saleTime){
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantitySold = quantitySold;
        this.PriceSold = PriceSold;
        this.saleTime = saleTime;
    }
    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getPriceSold() {
        return PriceSold;
    }

    public void setPriceSold(double PriceSold) {
        this.PriceSold = PriceSold;
    }

    public Date getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Date saleTime) {
        this.saleTime = saleTime;
    }

    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "product{" + "id=" + id + ", name=" + name + ", price=" + price + ", quantity=" + quantity + '}';
    }
    
    
}
