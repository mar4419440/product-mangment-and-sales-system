/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 *
 * @author ADMIN
 */
public class PODS extends Application {
    
    private ProductMangerPODS ProductManagerPODS;
    private Connection connection;
    private product product ;
    
    @Override
    public void start(Stage stage) throws UnknownHostException, SocketException, Exception {
        
        String url = "jdbc:mysql://localhost:3306/products";
        String username = "root";
        String password = "aux12mar";
        connection = DriverManager.getConnection(url, username, password);
//        
        ProductManagerPODS = new JdbcPodsDAO(connection);
        
        
        
         

        Parent root = FXMLLoader.load(getClass().getResource("Password.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        stop();
    }
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
        public void stop() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
    
}
