/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Tiffany Rolle
 */

/** FUTURE ENHANCEMENT
    A possible future enhancement to the program would include the location of each part and product in the warehouse or
    where ever the parts or products are being kept. This would make it easier for users to access the specified items in
    stock.
 */
/** This class creates an inventory management system application. */
public class MainForm extends Application{
    /** This is the main method. It is the first method that gets called when you run your java program. */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("TR JavaFX Menu");
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InHouse ihPt1 = new InHouse(Inventory.getPartIdCt(), "Capacitor", 12, 17.00, 3, 20, 6);

        Inventory.addPart(ihPt1);
        Product product1 = new Product(Inventory.getProductIdCt(), "Condenser", 5, 950.00, 20, 3);
        product1.addAssociatedPart(ihPt1);
        Inventory.addProduct(product1);


        Inventory.getAllParts().add(new InHouse(Inventory.getPartIdCt(), "Contactor", 10, 18.00, 3, 20, 5));
        Inventory.getAllParts().add(new Outsourced(Inventory.getPartIdCt(), "Compressor", 3, 1200.00, 3, 20, "Trane"));
        Inventory.getAllParts().add(new Outsourced(Inventory.getPartIdCt(), "Fan", 4, 250.00, 2, 20, "Trane"));




        Inventory.getAllProducts().add(new Product(Inventory.getProductIdCt(), "Thermostat", 4, 250.00, 30, 2));
        Inventory.getAllProducts().add(new Product(Inventory.getProductIdCt(), "Filter", 14, 15.00, 30, 2));


        launch(args);




    }

    
}
