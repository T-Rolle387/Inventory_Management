package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;


/** Inventory class containing arrays for all parts and products and related methods */
public class Inventory {

    /** declare all parts and all products array and part and product id fields */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static int partId = 0;
    private static int productId = 1;


    /** sets parts */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /** sets products */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    /** look up part by partID */
    public static Part lookupPart(int partId) {
        for (Part prt : Inventory.getAllParts()) {
            if (prt.getId() == partId)
                return prt;
        }
        return null;
    }

    /** This method will look up part name by partial string and list of all possible parts. */
    public static ObservableList<Part> lookupPart(String partialPartName) {
        ObservableList<Part> partName = FXCollections.observableArrayList();

        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part pt : allParts) {
            if (pt.getName().contains(partialPartName)) {
                partName.add(pt);
            }
        }

        return partName;

    }

    /** This method will look up product using partial string and return list of all possible products. */
    public static ObservableList<Product> lookupProduct(String partialProductName) {
        ObservableList<Product> productName = FXCollections.observableArrayList();

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product pd : allProducts) {
            if (pd.getName().contains(partialProductName)) {
                productName.add(pd);
                System.out.println(pd.getName());
            }
        }
        return productName;
    }

    /** This method looks up product by ID and returns product. */
    public static Product lookupProduct(int productId) {
        for (Product prdct : Inventory.getAllProducts()) {
            if (prdct.getId() == productId)
                return prdct;
        }
        return null;
    }

    /** This method modifies product by index, product name. */
    public static void updateProduct(int index, Product newProduct)
    {
        allProducts.set(index, newProduct);
    }

    /** This method modifies part by index, part name. */
    public static void updatePart(int index, Part selectedPart)
    {
        allParts.set(index, selectedPart);
    }

    /**Delete method that returns true when the part is deleted. */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.size() > 0) {
            allParts.remove(selectedPart);
        }

        return false;
    }

    /** This is a delete method that returns true when the product is deleted. */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.size() > 0) {
            allProducts.remove(selectedProduct);
        }
        return false;
    }

    /** This method returns all parts from Observable Parts List. */
    public static ObservableList<Part> getAllParts(){

        return allParts;
    }

    /** This method returns all products from Observable Products List */
    public static ObservableList<Product> getAllProducts(){

        return allProducts;
    }


    /** This method auto increments partID count by 2 (even numbers). */
    public static int getPartIdCt(){
         partId += 2;
         return partId;
    }
    /** This method auto increments productID count by 2 (odd numbers). */
    public static int getProductIdCt(){
        productId += 2;
        return productId;
    }

    /** This is the method to return to main screen. */
    public static void returnHome(Stage stage, Event event, Parent scene) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Inventory.class.getResource("/view/MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** RUNTIME ERROR - Logical Errors
        Logical errors that I encountered were: making sure the inventory levels were to be between the minimum and
        maximum for the specified part or product, ensuring the maximum value must be greater than the minimum, and also
        ensuring the minimum value be less than the maximum value. I defined a method using if decision structures with
        conditional statements to prevent overflow and underflow. I chose to make the method public and static so it
        could easily be accessed in all controllers. */

    public static void logicalErrorCheck(int stock, int min, int max){
        if (stock < min)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Inventory stock must be greater than minimum allowable inventory levels.");
            alert.showAndWait();
        }
        //Checks to make sure the inventory stock level is not greater than the maximum level.
        if (stock > max)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Inventory stock must be less than maximum allowable inventory levels.");
            alert.showAndWait();

        }
        //Checks to make sure the max user entered field is not less than the minimum.
        if (max <= min)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Maximum inventory levels must be greater than minimum inventory levels. ");
            alert.showAndWait();

        }
        //Checks to make sure the minimum user entered field is not greater than the max.
        if (min >= max)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Minimum inventory levels must be less than maximum inventory levels. ");
            alert.showAndWait();

        }

    }

    /** Method that returns true if the part or product is free from specified errors. */
    public static boolean isValid( int stock, int min, int max)
    {
        if((stock > max) || (stock < min) )
            return false;
        if((min > max) || (max < min))
            return false;

        else return true;
    }

    /** Method that amends part by id, part. */
    public static void amendPart(int id, Part part)
    {
        int index = -1;

        for(Part prt : Inventory.getAllParts())
        {
            index++;

            if(prt.getId() == id)
            {
                Inventory.getAllParts().set(index, part);
            }
        }
    }
}
