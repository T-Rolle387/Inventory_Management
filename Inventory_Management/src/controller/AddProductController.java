/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.Product;


/**
 * FXML Controller class
 *
 * @author Tiffany Rolle
 */
public class AddProductController implements Initializable {
    //Declare all fields
    Stage stage;
    Parent scene;
    private int id;
    Product product;

    public TextField productIdTxt;
    public TextField productNameTxt;
    public TextField productInvTxt;
    public TextField productPriceTxt;
    public TextField productInvMaxTxt;
    public TextField productInvMinTxt;


    public TextField searchBar;


    public TableView <Part> addAssocPartTableView;
    public TableColumn<Part, Integer> prodPartIdCol;
    public TableColumn <Part, String>prodPartNameCol;
    public TableColumn<Part, Integer> prodInvLvlCol;
    public TableColumn<Part, Double> prodPriceCol;

    public TableView<Part>  viewAssocParts;
    public TableColumn <Part, Integer>partIdCol;
    public TableColumn <Part, String> partNameCol;
    public TableColumn <Part, Integer>partInvLvlCol;
    public TableColumn <Part, Double>partPriceCol;

    public void onActionAdd(ActionEvent event){

        //Adds part to associated parts
        Part partToAdd = addAssocPartTableView.getSelectionModel().getSelectedItem();
        product.addAssociatedPart(partToAdd);

    }

    //Search Bar
    public void onActionReturnSearchResults(ActionEvent event)
    {
        String pq = searchBar.getText();

        ObservableList<Part> parts = Inventory.lookupPart(pq);

        if (parts.size() == 0) {
            try {
                int partId = Integer.parseInt(pq);
                Part pt = Inventory.lookupPart(partId);
                if (pt != null)
                    parts.add(pt);
            } catch (NumberFormatException e) {
                //Do nothing with the exception. This is used for search only.
            }
        }

        addAssocPartTableView.setItems(parts);
        if (parts.size() == 0) {
            System.out.println("Error");
            System.out.println("Part not found");
            searchBar.setText("");
        }
    }



    //Removes associated part
    public void onActionRemoveAssocPart(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The selected part will be removed from the product listing. Do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();


        if (result.isPresent() && result.get() == ButtonType.OK) {
            Part selectedPart = viewAssocParts.getSelectionModel().getSelectedItem();
            product.deleteAssociatedPart(selectedPart);
            Alert alert2 = new Alert(Alert.AlertType.WARNING);
            alert2.setTitle("Error Dialog");
            alert2.setContentText("Part removal successful.");
            alert2.showAndWait();
        }


    }

    /** RUNTIME ERROR - NumberFormatException
     The most common runtime error I encountered was a NumberFormatException Error. This can be caused if a user
     types a letter in a text field designated for an integer or double. I fixed this error using try and catch blocks.
     The try block allowed me to define the code I wanted tested for errors while being executed. The catch statement
     allowed me to handle the error if it occurred. I placed an error dialog box in my catch statement to notify the
     user to enter appropriate values if the error occurred.
     */

    public void onActionSaveAssocPart(ActionEvent event) throws IOException {

        try {
            //converts strings to desired data types in our constructor
            int id = Inventory.getProductIdCt();
            String name = productNameTxt.getText();
            int stock = Integer.parseInt(productInvTxt.getText());
            double price = Double.parseDouble(productPriceTxt.getText());
            int max = Integer.parseInt(productInvMaxTxt.getText());
            int min = Integer.parseInt(productInvMinTxt.getText());

            //Adds product
           product.setId(id);
           product.setName(name);
           product.setStock(stock);
           product.setPrice(price);
           product.setMax(max);
           product.setMin(min);

            //Sets default values for text fields expecting integers and doubles
            if ("".equals(id)) {
                id = 0;
            }
            if ("".equals(stock)) {
                stock = 0;
            }
            if ("".equals(price)) {
                price = 0.0;
            }
            if ("".equals(min)) {
                min = 0;
            }
            if ("".equals(max)) {
                max = 0;
            }
            //Logical error check
            Inventory.logicalErrorCheck(stock, min, max);


            if (Inventory.isValid(stock, min, max))
            {
                Inventory.addProduct(product);
                Inventory.returnHome(stage, event, scene);
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each text field.");
            alert.showAndWait();
        }

    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //used to auto generate id
        id = Inventory.getProductIdCt();
        productIdTxt.setText("Auto Gen: " + id);
        product = new Product( 0,  "null", 0,  0.0, 0,  0);


        //All available parts table columns
        prodPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Populate available parts table
        addAssocPartTableView.setItems(Inventory.getAllParts());

        //All associated parts table columns
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Places associated parts in table
        viewAssocParts.setItems(product.getAllAssociatedParts());


    }

    public void onActionCxlAddition(ActionEvent actionEvent) throws IOException{
        //confirms deletion and takes user back to main screen if ok was pressed
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Product will not be saved. Do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)

            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
    }



}
