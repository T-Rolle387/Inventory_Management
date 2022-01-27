/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author Tiffany Rolle
 */
public class ModifyProductController implements Initializable {
    //stage and scene used to return to main
    Stage stage;
    Parent scene;

    //generic product assignment used for product replacement
    Product product;

    //Array to hold associated parts while changes are being made
    public static ObservableList<Part> tempAssocParts = FXCollections.observableArrayList();

    //Search bar
    public TextField availPartSearchTxt;

    //Product to modify text fields
    public TextField prodIDTxt;
    public TextField prodNameTxt;
    public TextField prodInvTxt;
    public TextField prodPriceTxt;
    public TextField prodMaxInvTxt;
    public TextField prodMinInvTxt;

    //Available parts table
    public TableView<Part> allPartsTableView;

    //Available parts columns
    public TableColumn<Part, Integer> availPartID;
    public TableColumn<Part, String> availPartName;
    public TableColumn <Part, Integer>availPartInv;
    public TableColumn <Part, Double> availPartPrice;

    //Associated parts table

    public TableView <Part> assocPartsTableView;

    //Associated parts columns
    public TableColumn <Part, Integer> assocPartID;
    public TableColumn <Part, String>assocPartName;
    public TableColumn <Part, Integer>assocPartInv;
    public TableColumn <Part, Double> assocPartPrice;


    //Populates data from received product into corresponding text fields
    public void sentProduct(Product productToReceive)
    {
        //Converts text to acceptable data types
        prodIDTxt.setText(String.valueOf(productToReceive.getId()));
        prodNameTxt.setText(productToReceive.getName());
        prodInvTxt.setText(String.valueOf(productToReceive.getStock()));
        prodPriceTxt.setText(String.valueOf(productToReceive.getPrice()));
        prodMaxInvTxt.setText(String.valueOf(productToReceive.getMax()));
        prodMinInvTxt.setText(String.valueOf(productToReceive.getMin()));

        //All associated parts table columns
        assocPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Populates associated part table
        assocPartsTableView.setItems(productToReceive.getAllAssociatedParts());

        //product received from main
        product = productToReceive;


    }

    //Confirms cancellation and takes user back to main screen
    public void onActionCxlProductMod(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All modifications made will not be saved. Do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
            Inventory.returnHome(stage, event, scene);

    }

    //Search bar handler that allows search by available part name or ID
    public void onActionSearchAvailParts(ActionEvent event)
    {
        String pq = availPartSearchTxt.getText();

        ObservableList<Part> parts = Inventory.lookupPart(pq);

        if (parts.size() == 0) {
            try {
                int partId = Integer.parseInt(pq);
                Part pt = Inventory.lookupPart(partId);
                if (pt != null)
                    parts.add(pt);
            } catch (NumberFormatException e) {
                //ignore it
            }
        }

        allPartsTableView.setItems(parts);
        if (parts.size() == 0) {
            System.out.println("Error");
            System.out.println("Part not found");
            availPartSearchTxt.setText("");
        }
    }
    //Checks first to see if part is selected, if so confirmation box pops up and part is removed upon confirmation
    public void onActionRemoveAssocPart(ActionEvent event) {
        Part selectedPart = assocPartsTableView.getSelectionModel().getSelectedItem();
        if ((assocPartsTableView.getSelectionModel().isEmpty()) == false) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The selected part will be removed from the product listing. Do you want to continue?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                    product.deleteAssociatedPart(selectedPart);
                    Alert alert2 = new Alert(Alert.AlertType.WARNING);
                    alert2.setTitle("Error Dialog");
                    alert2.setContentText("Part removal successful.");
                    alert2.showAndWait();
                }

            }

        }


        public void  onActionAddAssocPart(ActionEvent event)
        {
            //Checks to see if a part is selected
            if (allPartsTableView.getSelectionModel().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Please select an item to add.");
                alert.showAndWait();
            }
            //Adds selected part to temporary associated part array to be stored until saved
            Part partToAdd = allPartsTableView.getSelectionModel().getSelectedItem();
            tempAssocParts.add(partToAdd);
            assocPartsTableView.setItems(tempAssocParts);
            assocPartsTableView.getSelectionModel();


        }

    /** RUNTIME ERROR - NumberFormatException
     The most common runtime error I encountered was a NumberFormatException Error. This can be caused if a user
     types a letter in a text field designated for an integer or double. I fixed this error using try and catch blocks.
     The try block allowed me to define the code I wanted tested for errors while being executed. The catch statement
     allowed me to handle the error if it occurred. I placed an error dialog box in my catch statement to notify the
     user to enter appropriate values if the error occurred.
     */

        public void onActionSaveProd (ActionEvent event) throws IOException
        {
            try {

                //converts strings to desired data types in our constructor
                int id = Inventory.getProductIdCt();
                String name = prodNameTxt.getText();
                int stock = Integer.parseInt(prodInvTxt.getText());
                double price = Double.parseDouble(prodPriceTxt.getText());
                int min = Integer.parseInt(prodMinInvTxt.getText());
                int max = Integer.parseInt(prodMaxInvTxt.getText());

                //Alerts user to enter product name if text field left blank
                if (name.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("Please enter product name.");
                    alert.showAndWait();
                }
                //Checking for logical errors described in the Inventory class
                Inventory.logicalErrorCheck(stock, min, max);

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


                //Associating data to modified product
                Product prodToMod = new Product(id, name, stock, price, max, min);

                //Assigning associated parts to modified product
                for (Part assocPart : tempAssocParts) {
                    prodToMod.addAssociatedPart(assocPart);
                }
                //Checks for product validation and upon validation saves product/returns to home screen
                if (Inventory.isValid(stock, min, max)) {
                    Inventory.addProduct(prodToMod);
                    Inventory.deleteProduct(product);
                    Inventory.returnHome(stage, event, scene);
                }


                //Catches format exceptions(i.e. int typed in a string, etc.)
            } catch (NumberFormatException | IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Please enter a valid value for each text field.");
                alert.showAndWait();
            }

        }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //All available parts table columns
        availPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        availPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        availPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        availPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Populate available parts table
        allPartsTableView.setItems(Inventory.getAllParts());

    }

}
