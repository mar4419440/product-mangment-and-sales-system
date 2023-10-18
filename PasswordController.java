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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ADMIN
 */
public class PasswordController implements Initializable {
     
    ProductMangerPODS porductmanagerPODS;
    
    @FXML
    private TextField PasswordMacField;

    /**
     * Initializes the controller class.
     */
    @Override
public void initialize(URL url, ResourceBundle rb) {
    try {
        // Create a new instance of ProductMangerPODS
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/products", "root", "aux12mar");
        porductmanagerPODS = new JdbcPodsDAO(connection);
        
        // Check if this computer is allowed to work with this program
//        CheckMaC();
    } catch (SQLException ex) {
        Logger.getLogger(PasswordController.class.getName()).log(Level.SEVERE, null, ex);
    }
}
   @FXML
    private void CheckMaC() throws SQLException, SocketException, UnknownHostException, IOException{
        if(porductmanagerPODS.MacAdress()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }else{
            if(CheckPassword()){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }else{
                 System.exit(0);
            }
            
        }
    }
    private boolean CheckPassword() throws SQLException, UnknownHostException, SocketException {
        String PasswordMac = PasswordMacField.getText();
        if (!PasswordMac.isEmpty()) {
            if (porductmanagerPODS.NewMacAddress(PasswordMac)) {
                return true;
            } else {
                System.exit(0);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter the password");
            alert.showAndWait();
            return false;
        }
        return false;
    }

    
}
