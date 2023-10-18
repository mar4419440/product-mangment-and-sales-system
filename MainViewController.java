/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pods;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ADMIN
 */
public class MainViewController implements Initializable {
    product product;
    
    ProductMangerPODS porductmanagerPODS;
    @FXML
    private TableView<product> productTable;
    @FXML
    private TableColumn<product, Integer> idColumn;
    @FXML
    private TableColumn<product, String> nameColumn;
    @FXML
    private TableColumn<product, Double> priceColumn;
    @FXML
    private TableColumn<product, Integer> quantityColumn;
    @FXML
    private TextField QuantityField;
    @FXML
    private TextField PriceField;
    @FXML
    private TextField NameField;
    @FXML
    private TextField IdField;
    @FXML
    private TextField PriceSoldField;
    @FXML
    private TextField QuantitySoldField;

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
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

            // Load the products into the table
            productTable.setItems(porductmanagerPODS.getAllProducts());

            // Check if this computer is allowed to work with this program
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void HandleNewProduct() throws SQLException{
        //getting required info from text
        String name ;
        double price = 0.0;
        int quantity = 0;
        if(!NameField.getText().isEmpty()&&!PriceField.getText().isEmpty()&&!QuantityField.getText().isEmpty()){
            name = NameField.getText();
            price = Double.parseDouble(PriceField.getText());
            quantity = Integer.parseInt(QuantityField.getText());
            //putting the data inside product object
        
            // Adding a new product
            product newProduct = new product(name, price, quantity);
            porductmanagerPODS.addProduct(newProduct);
            refreshTable();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("please enter The product name ,price and quantity");
            alert.showAndWait();
        }
        
        
        
    }
  
    // Other methods omitted for brevity

    @FXML
    private void HandleEditeProductName(){
        String name = "";
        TableView<product> table = new TableView<>();
        product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        // ... add columns and data to the table ...
        if(!NameField.getText().isEmpty()){
                name = NameField.getText();
                if((selectedProduct != null)){
                try {
                        // Do something with the selected item
                        porductmanagerPODS.updateProductName(selectedProduct,name);
                    } catch (SQLException ex) {
                        Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 refreshTable();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("please select the wanted product");
                    alert.showAndWait();
                }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("please enter The new name");
            alert.showAndWait();
        }
        // Get the selection model
        
     }
    @FXML
    private void HandleEditeProductprice(){
        
        double price =0.0;
        int quantity = 0;
        product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        TableView<product> table = new TableView<>();
        // ... add columns and data to the table ...
        if(!PriceField.getText().isEmpty()&&!QuantityField.getText().isEmpty()){
            
            price = Double.parseDouble(PriceField.getText());
            quantity = Integer.parseInt(QuantityField.getText());
            if(selectedProduct!=null){
            // Get the selection model
        
                try {
                        // Do something with the selected item
                        porductmanagerPODS.updateProductPrice(selectedProduct, quantity, price);
                } catch (SQLException ex) {
                        Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("please select the wanted product");
                    alert.showAndWait();
                }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("please enter The new name, price and quantity");
            alert.showAndWait();
        }

        
        // Listen for selection changes
        
        refreshTable();
    }
    @FXML
    private void HandleDeleteProductName(){
        TableView<product> table = new TableView<>();
        // ... add columns and data to the table ...
        
        // Get the selection model
         product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if(selectedProduct!=null){
             try {
                   // Do something with the selected item
                   porductmanagerPODS.deleteProduct(selectedProduct.getId());
               } catch (SQLException ex) {
                   Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
               }
        }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("please select the wanted product");
                    alert.showAndWait();
        }
         
        // Listen for selection changes
        
        refreshTable();
    }
    @FXML
    private void HandleDeleteProductprice(){
        TableView<product> table = new TableView<>();
        // ... add columns and data to the table ...

        // Get the selection model
        product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if(selectedProduct!=null){
        try {
                    // Do something with the selected item
                    porductmanagerPODS.deleteProductPrice(selectedProduct);
                } catch (SQLException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
        refreshTable();
        }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("please select the wanted product");
                    alert.showAndWait();
        }
        
    }
    @FXML
    private void HandleSellProduct() {
        double priceSold = 0.0;
        int quantitySold = 0;
        if (!PriceSoldField.getText().isEmpty()) {
            priceSold = Double.parseDouble(PriceSoldField.getText());
        
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter the price sold");
            alert.showAndWait();
        }
        if (!QuantitySoldField.getText().isEmpty()) {
            quantitySold = Integer.parseInt(QuantitySoldField.getText());
        
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter the the Quantity sold");
            alert.showAndWait();
        }
        // Get the currently selected product
        product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if ((selectedProduct != null) && !PriceSoldField.getText().isEmpty()&& (!QuantitySoldField.getText().isEmpty())) {
            try {
                 
                product newProduct = new product(selectedProduct.getName() ,selectedProduct.getPrice(),quantitySold,priceSold);
                // Sell the selected product
                porductmanagerPODS.sellproduct(newProduct);
                refreshTable();
            } catch (SQLException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a product to sell");
            alert.showAndWait();
        }
        refreshTable();
    }
    @FXML
    private void handleSalesTable() throws IOException {
        
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sales.fxml"));
        Parent root = loader.load();
        newStage.setScene(new Scene(root));
        newStage.show();
    }
    private void refreshTable() {
        try {
            // Clear the current data
            productTable.getItems().clear();

            // Reload the products into the table
            productTable.setItems(porductmanagerPODS.getAllProducts());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void HandleProductTable() throws SQLException{
        productTable.setItems(porductmanagerPODS.getAllProducts());
    }
    @FXML
    private void HandlegetProductByName(){
        String name = "";
        if(!NameField.getText().isEmpty()){
            name = NameField.getText();
            try{
            productTable.getItems().clear();
            refreshTable();
            productTable.setItems(FXCollections.observableArrayList(porductmanagerPODS.getProductByName(name)));
//            refreshTable();
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }else {
            // No product is selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a product to sell");
            alert.showAndWait();
        }
        
        
    }
    
    
}
