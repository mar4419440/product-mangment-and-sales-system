/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pods;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalTime; // import the LocalTime class
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author ADMIN
 */
public class PodsDAO {

    private Connection connection;
    private String PasswordMAC = "**f+MM=170cm?M'sD=20cmAtSoft4u";

    public PodsDAO(Connection connection) {
        this.connection = connection;
    }
    public void addProduct(product product) throws SQLException {
    // check if the product name already exists in the productname table
    PreparedStatement statement = connection.prepareStatement("SELECT id FROM productname WHERE name = ?");
    statement.setString(1, product.getName());
    ResultSet resultSet = statement.executeQuery();

    if (resultSet.next()) {
        // product name exists, update procuctprices table
        int id = resultSet.getInt("id");

        // check if the product price already exists in the procuctprices table
        statement = connection.prepareStatement("SELECT id FROM procuctprices WHERE id_productname = ? AND price = ?");
        statement.setInt(1, id);
        statement.setDouble(2, product.getPrice());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            // product price exists, update quantity
            int id_price = resultSet.getInt("id");

            statement = connection.prepareStatement("UPDATE procuctprices SET quantity = quantity + ? WHERE id = ?");
            statement.setInt(1, product.getQuantity());
            statement.setInt(2, id_price);
            statement.executeUpdate();
        } else {
            // product price does not exist, insert new row
            statement = connection.prepareStatement("INSERT INTO procuctprices (price, quantity, id_productname) VALUES (?, ?, ?)");
            statement.setDouble(1, product.getPrice());
            statement.setInt(2, product.getQuantity());
            statement.setInt(3, id);
            statement.executeUpdate();
        }
    } else {
        // product name does not exist, insert new row into productname table
        statement = connection.prepareStatement("INSERT INTO productname (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, product.getName());
        statement.executeUpdate();

        // get the generated product ID
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            int id = generatedKeys.getInt(1);

            // insert new row into procuctprices table
            statement = connection.prepareStatement("INSERT INTO procuctprices (price, quantity, id_productname) VALUES (?, ?, ?)");
            statement.setDouble(1, product.getPrice());
            statement.setInt(2, product.getQuantity());
            statement.setInt(3, id);
            statement.executeUpdate();
        }
    }
}



    public ObservableList<product> getAllProducts() throws SQLException {
    String query = "SELECT productname.id, productname.name, procuctprices.price, procuctprices.quantity FROM productname JOIN procuctprices ON productname.id = procuctprices.id_productname WHERE procuctprices.quantity <> 0";
    PreparedStatement statement = connection.prepareStatement(query);
    ResultSet resultSet = statement.executeQuery();
    ObservableList<product> productList = FXCollections.observableArrayList();

    while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        int quantity = resultSet.getInt("quantity");
        product product = new product(id, name, price, quantity);
        productList.add(product);
    }

    return productList;
}

public product getProductById(int id) throws SQLException {
    String query = "SELECT * FROM productname WHERE id = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setInt(1, id);
    ResultSet resultSet = statement.executeQuery();

    if (resultSet.next()) {
        String name = resultSet.getString("name");

        query = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER(PARTITION BY id_productname ORDER BY id_productname ASC, price ASC) AS k FROM procuctprices WHERE quantity <> 0) AS a WHERE k = 1 AND id_productname = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, id);
        ResultSet resultSet1 = pstmt.executeQuery();

        if (resultSet1.next()) {
            double price = resultSet1.getDouble("price");
            int quantity = resultSet1.getInt("quantity");
            return new product(id, name, price, quantity);
        }
    }

    return null;
}

public product getProductByName(String name) throws SQLException {
    name = "%"+name+"%";
    String query = "SELECT * FROM productname WHERE name like ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, name);
    ResultSet resultSet = statement.executeQuery();

    if (resultSet.next()) {
        int id = resultSet.getInt("id");

        String query1 = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER(PARTITION BY id_productname ORDER BY id_productname ASC, price ASC) AS k FROM procuctprices WHERE quantity <> 0) AS a WHERE k = 1 AND id_productname = ?";
        PreparedStatement pstmt = connection.prepareStatement(query1);
        pstmt.setInt(1, id);
        ResultSet resultSet1 = pstmt.executeQuery();

        if (resultSet1.next()) {
            String name1 = resultSet.getString("name");
            double price = resultSet1.getDouble("price");
            int quantity = resultSet1.getInt("quantity");
            return new product(id, name1, price, quantity);
        }
    }

    return null;
}

    public void updateProductName(product product, String name) throws SQLException {
    // update the productname table
    String query = "UPDATE productname SET name = ? WHERE id = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, name);
    statement.setInt(2, product.getId());
    statement.executeUpdate();
    
    
    }
    
    public void updateProductPrice(product product, int quantity,double newprice) throws SQLException {

        // check if a record with the same price already exists for the specified product ID
        product.setName("%"+product.getName()+"%");
        String query = "SELECT id FROM productname WHERE name LIKE ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, product.getName());
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            int id = resultSet.getInt(1);
            query = "SELECT id FROM procuctprices WHERE id_productname = ? AND price = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setDouble(2, product.getPrice());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                int id_prices = resultSet.getInt(1);
                // update the productprices table if a match is found, otherwise insert a new record
                query = "UPDATE procuctprices SET quantity = ? , price = ?WHERE id = ? AND price = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, quantity);
                statement.setDouble(2,newprice);
                statement.setInt(3, id_prices);
                statement.setDouble(4, product.getPrice());
                statement.executeUpdate();
                
                
            }
        }
    }
    
    public void deleteProduct(int id) throws SQLException {
        // delete from productname table
        String query = "DELETE FROM productname WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.executeUpdate();

        // delete from productprices table
        query = "DELETE FROM procuctprices WHERE id_productname = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.executeUpdate();
    }
    
    public void deleteProductPrice(product product) throws SQLException {
    //getting productname id
        String query = "select id from productname where name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,product.getName());
        ResultSet resultSet = statement.executeQuery();

        //checking if it's found in the table
        if(resultSet.next()){
            // if the name is found  i delete the price where of name 
            int id = resultSet.getInt(1);
            query = "DELETE FROM procuctprices WHERE price = ? AND id_productname = ?";
            statement = connection.prepareStatement(query);
            statement.setDouble(1, product.getPrice());
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }
    public void DeleteProductFromSales(int id) throws SQLException{
        String query = "Delete from sales where id = ?";
        PreparedStatement statment = connection.prepareStatement(query);
        statment.setInt(1, id);
        statment.executeUpdate();
    }
    public void sellproduct(product product) throws SQLException {
    String query = "select id from productname where name = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, product.getName());

    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
        int id = resultSet.getInt(1);
        query = "SELECT id FROM procuctprices WHERE id_productname = ? AND price = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.setDouble(2, product.getPrice());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int id_price = resultSet.getInt(1);
            query = "SELECT quantity FROM procuctprices WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id_price);
            resultSet = statement.executeQuery();
            int quantity;
            if (resultSet.next()) {
                quantity = resultSet.getInt(1);
                
                if (quantity != 0) {
                    query = "UPDATE procuctprices SET quantity = quantity - ? WHERE id = ?";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, product.getQuantitySold());
                    statement.setInt(2, id_price);
                    statement.executeUpdate();
                    query = "SELECT quantity  FROM procuctprices WHERE id = ?";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, id_price);
                    resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        quantity = resultSet.getInt(1);
                        if (quantity == 0) {
                            query = "DELETE FROM procuctprices WHERE id = ?";
                            statement = connection.prepareStatement(query);
                            statement.setInt(1, id_price);
                            statement.executeUpdate();
                        }
                        query = "INSERT INTO sales (name, quantity_sold, price_sold, price, id_product_price, saleTime, SaleDate) VALUES (?, ?, ?, ?, ?, now(), curdate())";
                        statement = connection.prepareStatement(query);

                        statement.setString(1, product.getName());
                        statement.setInt(2, product.getQuantitySold());
                        statement.setDouble(3, product.getPriceSold());
                        statement.setDouble(4, product.getPrice());
                        statement.setInt(5, id_price);
                        statement.executeUpdate();
                    }
                } else {
                    query = "DELETE FROM procuctprices WHERE id = ?";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, id_price);
                    statement.executeUpdate();
                }
            }
        }
    }
}

    
    public ObservableList<product> getAllSales() throws SQLException {
        String query = "SELECT * from sales";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        ObservableList<product> productList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int quantitySold = resultSet.getInt("quantity_sold");
            double PriceSold = resultSet.getDouble("price_sold");
            double price = resultSet.getDouble("price");
            Date saleTime = resultSet.getDate("saleTime");
            product sales = new product(id ,name, price, quantitySold, PriceSold, saleTime);
            productList.add(sales);
        }

        return productList;
    }
    
    public void AddMaintenance(product product) throws SQLException{
        String query ="INSERT INTO sales (name, quantity_sold, price_sold, price, id_product_price, saleTime, SaleDate) VALUES (?, ?, ?, ?, ?, now(), curdate())";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, product.getName());
        statement.setInt(2, product.getQuantitySold());
        statement.setDouble(3, product.getPriceSold());
        statement.setDouble(4, product.getPrice());
        statement.setInt(5, -1);
        statement.executeUpdate();
    }
    
    
    public void ReturnProduct(int id) throws SQLException {
    String name;
    double price;
    int quantity;
    String query = "select * from sales where id = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setInt(1, id); // Set the id parameter
    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next()) {
        name = resultSet.getString("name");
        price = resultSet.getDouble("price");
        quantity = resultSet.getInt("quantity_sold");
        product product = new product(name, price, quantity);
        addProduct(product);
    }
    query = "delete from sales where id = ?";
    statement = connection.prepareStatement(query);
    statement.setInt(1, id); // Set the id parameter
    statement.executeUpdate();
}


    
    public double TotalDaySales(String date) throws SQLException {
        // List<Double> TotalProfit = new ArrayList<>();
        double totalProfit = 0 ;
        int quantity;
        double PriceSold;
        double price;
        double profit;
        String query = "SELECT * FROM sales WHERE SaleDate = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,date);
        ResultSet rs = statement.executeQuery();
        // Loop over the results and add them to the list
        while (rs.next()) {
            quantity = rs.getInt("quantity_sold");
            PriceSold = rs.getDouble("price_sold");
            price = rs.getDouble("price");
            profit = (PriceSold - price)*quantity;
            
            totalProfit =+ profit;
        }
        return totalProfit;
    }
    
    public double TotalMonthSales(String month) throws SQLException {
        // List<Double> TotalProfit = new ArrayList<>();
        int count=0;
        
        int quantity =0;
        double PriceSold = 0;
        double price =0;
        double profit =0;
        double totalProfit = 0 ;
        String query ="select * from sales where monthname(SaleDate)=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,month);
        ResultSet rs = statement.executeQuery();
        // Loop over the results and add them to the list
        while (rs.next()) {
            quantity = rs.getInt("quantity_sold");
            PriceSold = rs.getDouble("price_sold");
            price = rs.getDouble("price");
            profit = (PriceSold - price)*quantity;
            count++;
            totalProfit = totalProfit+ profit;
        }
        
        return totalProfit;
    }

    public double TotalAnnualSales(int year) throws SQLException {
        // List<Double> TotalProfit = new ArrayList<>();
        double totalProfit = 0 ;
        String query =" SELECT * FROM sales WHERE YEAR(SaleDate) = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,year);
        ResultSet rs = statement.executeQuery();
        // Loop over the results and add them to the list
        while (rs.next()) {
            int quantity = rs.getInt("quantity_sold");
            double PriceSold = rs.getDouble("price_sold");
            double price = rs.getDouble("price");
            double profit = (PriceSold - price)*quantity;
            
            totalProfit =totalProfit+ profit;
        }
        return totalProfit;
    }
    public boolean MacAdress() throws SQLException,UnknownHostException, SocketException{
        
        // Get the network interface for the current machine
        NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());

        // Get the MAC address for the interface
        byte[] mac = ni.getHardwareAddress();

        // Convert the MAC address to a string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        String macAddress = sb.toString();
        
        String query = "SELECT * FROM macaddresses ";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            String MACAddress =resultSet.getString("MacAddress");
            if(macAddress.equals(MACAddress)){
                return true;
            }
        }
        
        
         
        return false;
    }
    
    public ObservableList<product> SalesSearchByName(String name) throws SQLException {
    int id;
    String name1;
    double price;
    int quantitySold;
    double PriceSold;
    Date saleTime;
    name = "%" + name + "%";
    String query = "SELECT id FROM sales WHERE name LIKE ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, name);
    ResultSet resultSet = statement.executeQuery();
    ObservableList<product> productList = FXCollections.observableArrayList();
    while (resultSet.next()) {
        id = resultSet.getInt("id");
        query = "SELECT * FROM sales WHERE id = ?";
        PreparedStatement innerStatement = connection.prepareStatement(query);
        innerStatement.setInt(1, id);
        ResultSet innerResultSet = innerStatement.executeQuery();
        while (innerResultSet.next()) {
            id = innerResultSet.getInt("id");
            name1 = innerResultSet.getString("name");
            price = innerResultSet.getDouble("price");
            quantitySold = innerResultSet.getInt("quantity_sold");
            PriceSold = innerResultSet.getDouble("price_sold");
            saleTime = innerResultSet.getDate("saleTime");
            product product = new product(id, name1, price, quantitySold, PriceSold, saleTime);
            productList.add(product);
            
        }
    }
    return productList;
}

    public ObservableList<product> getProductByDate(String Date1, String Date2) throws SQLException{
        
        int id;
        String name;
        double price;
        int quantitySold;
        double PriceSold; 
        Date saleTime;
        ObservableList<product> productList = FXCollections.observableArrayList();
        
        String query ="select * from sales where SaleDate BETWEEN ? AND ?";
        PreparedStatement statment = connection.prepareStatement(query);
        statment.setString(1, Date1);
        statment.setString(2, Date2);
        ResultSet resultSet = statment.executeQuery();
        while(resultSet.next()){
            id = resultSet.getInt("id");
            name = resultSet.getString("name");
            price = resultSet.getDouble("price");
            quantitySold = resultSet.getInt("quantity_sold");
            PriceSold = resultSet.getDouble("price_sold");
            saleTime = resultSet.getDate("saleTime");
            product product1 = new product(id, name, price, quantitySold, PriceSold, saleTime);
            productList.add(product1);
        }
         return productList;
    }
    public double getSalesByDate(String Date1, String Date2) throws SQLException{
        int id;
        String name;
        double price;
        int quantitySold;
        double PriceSold; 
        double total = 0;
        Date saleTime;
        ObservableList<product> productList = FXCollections.observableArrayList();
        
        String query ="select * from sales where SaleDate BETWEEN ? AND ?";
        PreparedStatement statment = connection.prepareStatement(query);
        statment.setString(1, Date1);
        statment.setString(2, Date2);
        ResultSet resultSet = statment.executeQuery();
        while(resultSet.next()){
            id = resultSet.getInt("id");
            name = resultSet.getString("name");
            price = resultSet.getDouble("price");
            quantitySold = resultSet.getInt("quantity_sold");
            PriceSold = resultSet.getDouble("price_sold");
            saleTime = resultSet.getDate("saleTime");
            product product1 = new product(id, name, price, quantitySold, PriceSold, saleTime);
            productList.add(product1);
            total = total+((PriceSold-price)*quantitySold);
        }
        
        return total;
    }
    public boolean NewMacAddress(String PasswordMAC) throws SQLException,UnknownHostException, SocketException{
        NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());

        // Get the MAC address for the interface
        byte[] mac = ni.getHardwareAddress();

        // Convert the MAC address to a string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        String macAddress = sb.toString();
        
        if(this.PasswordMAC.equals(PasswordMAC)){
                    String query = "INSERT INTO macaddresses (MacAddress) VALUES(?)";
                    PreparedStatement statement =connection.prepareStatement(query);
                    statement.setString(1, macAddress);
                    statement.executeUpdate();
                    return true;
        }
        return false;
    }
}
