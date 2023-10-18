/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pods;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.*;
import javafx.collections.ObservableList;

/**
 *
 * @author ADMIN
 */
public interface ProductMangerPODS {
    
    void addProduct(product product) throws SQLException;
    
    boolean MacAdress() throws SQLException,UnknownHostException, SocketException;
    
    boolean NewMacAddress(String PasswordMAC) throws SQLException,UnknownHostException, SocketException;
    
    void updateProductPrice(product product,int quantity,double price) throws SQLException ;
    
    void updateProductName(product product, String name) throws SQLException;
    
    void deleteProduct(int id) throws SQLException;
    
    void deleteProductPrice(product product) throws SQLException;
    
    void sellproduct(product product) throws SQLException;
    
    ObservableList<product> SalesSearchByName(String name) throws SQLException;
    
    ObservableList<product> getProductByDate(String Date1, String Date2) throws SQLException;
    
    double getSalesByDate(String Date1, String Date2) throws SQLException;
    
    void DeleteProductFromSales(int id) throws SQLException;
    
    void AddMaintenance(product product) throws SQLException;
    
    void ReturnProduct(int id) throws SQLException;
    
    double TotalDaySales (String date ) throws SQLException;
    
    double TotalMonthSales (String month) throws SQLException;
    
    double TotalAnnualSales (int year) throws SQLException;
    
    
    product getProductById(int id) throws SQLException;
    
    
    product getProductByName(String name) throws SQLException;
    
    ObservableList <product> getAllSales()throws SQLException;
        
    ObservableList<product> getAllProducts() throws SQLException;
    
    
}
