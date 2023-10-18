/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pods;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionModel;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ADMIN
 */
public class SalesController implements Initializable {
    
    product product;
    
    ProductMangerPODS porductmanagerPODS;
    
    @FXML
    private TextField QuantityField;
    @FXML
    private TextField PriceField;
    @FXML
    private TextField NameField;
    @FXML
    private TableView<product> productTable;
    @FXML
    private TableColumn<product, Integer> idColumn;
    @FXML
    private TableColumn<product, String> nameColumn;
    @FXML
    private TableColumn<product, Double> priceColumn;
    @FXML
    private TableColumn<product, Double> PriceSoldColumn;
    @FXML
    private Label SalesId;
    @FXML
    private TextField PriceSoldField;
    @FXML
    private TableColumn<product, Date> SalesTimeColumn;
    @FXML
    private TableColumn<product, Integer> QuantitySoldColumn;
    @FXML
    private TextField totalSalesField;
    @FXML
    private TextField StartDateField;
    @FXML
    private TextField EndDateField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Create a new instance of ProductMangerPODS
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/products", "root", "aux12mar");
            porductmanagerPODS = new JdbcPodsDAO(connection);

            // Set up the table columns
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            QuantitySoldColumn.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
            PriceSoldColumn.setCellValueFactory(new PropertyValueFactory<>("PriceSold"));
            SalesTimeColumn.setCellValueFactory(new PropertyValueFactory<>("saleTime"));

            // Load the products into the table
             productTable.setItems(porductmanagerPODS.getAllSales());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    } 
    @FXML
    private void handleAddMaintenance(){
        String name ="";
        double priceSold = 0.0;
        double price = 0.0;
        int QuantitySold = 0;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        
        if(!NameField.getText().isEmpty()&&!PriceSoldField.getText().isEmpty()&&!PriceField.getText().isEmpty()&&!QuantityField.getText().isEmpty()){
            name = NameField.getText();
            priceSold = Double.parseDouble(PriceSoldField.getText());
            price = Double.parseDouble(PriceField.getText());
            QuantitySold = Integer.parseInt(QuantityField.getText());
            product salesproduct = new product(name, price, QuantitySold, priceSold, date);
            try {
                    // Do something with the selected item
                    porductmanagerPODS.AddMaintenance(salesproduct);
                } catch (SQLException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("please enter the name , price sold ,price and quantity sold");
            alert.showAndWait();
        }
        refreshTable();
    }
    @FXML
    private void handleAddExpinxes(){
        String name ="";
        double priceSold = 0.0;
        double price = 0.0;
        int QuantitySold = 0;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        
        if(!NameField.getText().isEmpty()&&!PriceField.getText().isEmpty()&&!QuantityField.getText().isEmpty()){
            name = NameField.getText();
            priceSold = 0;
            price = Double.parseDouble(PriceField.getText());
            QuantitySold = Integer.parseInt(QuantityField.getText());
            product salesproduct = new product(name, price, QuantitySold, priceSold, date);
            try {
                    // Do something with the selected item
                    porductmanagerPODS.AddMaintenance(salesproduct);
                } catch (SQLException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("please enter the name , price sold ,price and quantity sold");
            alert.showAndWait();
        }
        refreshTable();
    }
    
    
    @FXML
    private void handleReturnProduct(){
        TableView<product> table = new TableView<>();
        // ... add columns and data to the table ...

        // Get the selection model
        product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if(selectedProduct!= null){
            try {
                        // Do something with the selected item
                        porductmanagerPODS.ReturnProduct(selectedProduct.getId());
                    } catch (SQLException ex) {
                        Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("please select the wanted product");
            alert.showAndWait();
        }
        refreshTable();
    }
    @FXML
    private void handleToatalDaySales() throws SQLException{
        String date = "";
        if(!totalSalesField.getText().isEmpty()){
            date = totalSalesField.getText();
            double TotalDaySales = porductmanagerPODS.TotalDaySales(date);
            SalesId.setText("Total sales of Day"+ date+ ": "+TotalDaySales);
            refreshTable();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("enter the date of the day");
            alert.showAndWait();
        }
       
    } 
    @FXML
    private void handleTotalMonthSales() throws SQLException{
        String month =  "";
        if(!totalSalesField.getText().isEmpty()){
            month =  totalSalesField.getText();
            double TotalMonthSales = porductmanagerPODS.TotalMonthSales(month);
            SalesId.setText("Total sales of Month"+ month+ ": "+ TotalMonthSales);
            refreshTable();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("enter the Month name");
            alert.showAndWait();
        }
        
    }
    @FXML
    private void handleTotalYearSales() throws SQLException{
        int year = 0;
        if(!totalSalesField.getText().isEmpty()){
            year = Integer.parseInt(totalSalesField.getText());
            double TotalYearSales = porductmanagerPODS.TotalAnnualSales(year);
            SalesId.setText("Total sales of year"+ year+ ": "+ TotalYearSales);
            refreshTable();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("enter the year");
            alert.showAndWait();
        }
        
    }
    
    @FXML
    private void handleSalesSearchByName() throws IOException, SQLException{
        String name = "";
        if(!NameField.getText().isEmpty()){
            name = NameField.getText();
            productTable.getItems().clear();
            refreshTable();
            productTable.setItems(FXCollections.observableArrayList(porductmanagerPODS.SalesSearchByName(name)));

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("enter the name of product");
            alert.showAndWait();
        }
        
    }
    
    @FXML
    private void handlesTotalSalesByDate() throws IOException, SQLException{
        String Date;
        String Date1;
        if(!StartDateField.getText().isEmpty()&&!EndDateField.getText().isEmpty()){
            Date = StartDateField.getText();
            Date1 = EndDateField.getText();
            productTable.getItems().clear();
            refreshTable();
            double TotalSales = porductmanagerPODS.getSalesByDate(Date, Date1);
            SalesId.setText("Total sales from "+ Date+ "to "+Date1+"is: "+ TotalSales);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Enter start Date And End Date");
            alert.showAndWait();
        }
        
        
    }
    @FXML
    private void handleSalesSearchByDate() throws SQLException{
        String Date;
        String Date1;
        if(!StartDateField.getText().isEmpty()&&!EndDateField.getText().isEmpty()){
            Date = StartDateField.getText();
            Date1 = EndDateField.getText();
            productTable.getItems().clear();
            refreshTable();
            productTable.setItems(FXCollections.observableArrayList(porductmanagerPODS.getProductByDate(Date, Date1)));
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Enter start Date And End Date");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleProductsTable() throws IOException{
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        Parent root = loader.load();
        newStage.setScene(new Scene(root));
        newStage.show();
    }
    
    
    private void refreshTable() {
        try {
            // Clear the current data
            productTable.getItems().clear();

            // Reload the products into the table
            productTable.setItems(porductmanagerPODS.getAllSales());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void HandleDeletProductFromSales(){
        int id;
        TableView<product> table = new TableView<>();
        // ... add columns and data to the table ...

        // Get the selection model
        product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if(selectedProduct!= null){
            try {
                        // Do something with the selected item
                        porductmanagerPODS.DeleteProductFromSales(selectedProduct.getId());
                    } catch (SQLException ex) {
                        Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("please select the wanted product");
            alert.showAndWait();
        }
        refreshTable();
    }
    @FXML
    private void handleSalesTable() throws SQLException{
        productTable.setItems(porductmanagerPODS.getAllSales());
        
    }
    
}
