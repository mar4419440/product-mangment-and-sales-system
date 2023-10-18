/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pods;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;



public class JdbcPodsDAO implements ProductMangerPODS{
    private Connection connection;
    private PodsDAO podsDAO;
    
    public JdbcPodsDAO(Connection connection) throws SQLException {
        this.connection = connection;
        podsDAO = new PodsDAO(connection);
    }
    
    @Override
    public void addProduct(product product) throws SQLException {
        podsDAO.addProduct(product);
    }
    
    @Override
    public product getProductByName(String name) throws SQLException {
        return podsDAO.getProductByName(name);
    }
    
    @Override
    public product getProductById(int id) throws SQLException {
         return podsDAO.getProductById(id);
    }
    
    @Override
    public ObservableList<product> getAllProducts() throws SQLException {
        return podsDAO.getAllProducts();
    }
    
    @Override
    public void updateProductName(product product, String name) throws SQLException {
        podsDAO.updateProductName(product, name);
    }
    @Override
    public void updateProductPrice(product product,int quantity,double price) throws SQLException {
        podsDAO.updateProductPrice(product,quantity,price);
    }
    
    @Override
    public void deleteProduct(int id) throws SQLException {
        podsDAO.deleteProduct(id);
    }
    
    @Override
    public void deleteProductPrice(product product) throws SQLException {
        podsDAO.deleteProductPrice(product);
    }
    
    //sales
    
    @Override
    public void sellproduct(product product) throws SQLException {
        podsDAO.sellproduct(product);
    }
        
    @Override
    public ObservableList<product> getAllSales() throws SQLException {
        return podsDAO.getAllSales();
    }
    
    @Override
    public void AddMaintenance(product product) throws SQLException {
        podsDAO.AddMaintenance(product);
    }
    
    @Override
    public void ReturnProduct(int id) throws SQLException {
        podsDAO.ReturnProduct(id);
    }

    
    @Override
    public double TotalDaySales(String date) throws SQLException {
        return podsDAO.TotalDaySales(date);
    }

    @Override
    public double TotalMonthSales(String month) throws SQLException {
        return podsDAO.TotalMonthSales(month);
    }

    @Override
    public double TotalAnnualSales(int year) throws SQLException {
        return podsDAO.TotalAnnualSales(year);
    }
    
    @Override
    public ObservableList<product> SalesSearchByName(String name) throws SQLException {
        return podsDAO.SalesSearchByName(name);
    }

    public void close() throws SQLException {
        connection.close();
    }   

    @Override
    public boolean MacAdress() throws SQLException, UnknownHostException, SocketException {
        return podsDAO.MacAdress();
    }

    @Override
    public boolean NewMacAddress(String PasswordMAC) throws SQLException, UnknownHostException, SocketException {
        return podsDAO.NewMacAddress(PasswordMAC);
    }

    @Override
    public ObservableList<product> getProductByDate(String Date1, String Date2) throws SQLException {
        return podsDAO.getProductByDate(Date1, Date2);
    }

    @Override
    public double getSalesByDate(String Date1, String Date2) throws SQLException {
        return podsDAO.getSalesByDate(Date1, Date2);
    }
    
    @Override
    public void DeleteProductFromSales(int id) throws SQLException{
        podsDAO.DeleteProductFromSales(id);
    }

    
}
