/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.Objects;
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
import model.*;
import model.Inventory;
import javax.swing.*;


/**
 * FXML Controller class
 *
 * @author Tiffany Rolle
 */
public class MainFormController implements Initializable {


    Stage stage;
    Parent scene;
    Product product;

    public TextField partQuery;
    public TableView partsTableView;
    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn partsInvLevelCol;
    public TableColumn partsPriceCol;

    public TextField productQuery;
    public TableView productsTableView;
    public TableColumn productIdCol;
    public TableColumn productNameCol;
    public TableColumn productInvLevelCol;
    public TableColumn productPriceCol;


    @FXML
    void addPart(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void addProduct(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }







    //Delete selected part
    public void deletePart(ActionEvent event)
    {
        if(partsTableView.getSelectionModel().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select an item to delete.");
            alert.showAndWait();
        }

        if((partsTableView.getSelectionModel().isEmpty()) == false) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The selected part will be deleted. Do you want to continue?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Part selectedItem = (Part) partsTableView.getSelectionModel().getSelectedItem();
                Inventory.deletePart(selectedItem);
            }
        }

    }

    //Delete selected product
    public void deleteProduct(ActionEvent event)
    {
        if(productsTableView.getSelectionModel().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select an item to delete.");
            alert.showAndWait();
        }
        product = (Product) productsTableView.getSelectionModel().getSelectedItem();
        if (!product.getAllAssociatedParts().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Product has associated parts. You may not delete product.");
            alert.showAndWait();
        }


        if(((productsTableView.getSelectionModel().isEmpty()) == false) && (product.getAllAssociatedParts().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The selected product will be deleted. Do you want to continue?");

            Optional<ButtonType> result = alert.showAndWait();


            if (result.isPresent() && result.get() == ButtonType.OK) {
                Product selectedProduct = (Product) productsTableView.getSelectionModel().getSelectedItem();
                Inventory.deleteProduct(selectedProduct);
                    Alert alert2 = new Alert(Alert.AlertType.WARNING);
                    alert2.setTitle("Error Dialog");
                    alert2.setContentText("Product deletion successful.");
                    alert2.showAndWait();
                }

            }

        }


    //Exits program
    public void exitMainMenu(ActionEvent event) {
        System.exit(0);

    }


    public void modifyPart(ActionEvent event) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
            loader.load();
            ModifyPartController MPController = loader.getController();
            MPController.sentPart((Part) partsTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: Part not selected.");
            alert.setContentText("Please select a part to continue.");
            alert.showAndWait();
        }

    }


    public void modifyProduct(ActionEvent event) throws IOException {
    try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
            loader.load();
            ModifyProductController ModProdController = loader.getController();
            ModProdController.sentProduct((Product) productsTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    catch(NullPointerException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: Product not selected.");
            alert.setContentText("Please select a product to continue.");
            alert.showAndWait();

        }

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        productsTableView.setItems(Inventory.getAllProducts());
        partsTableView.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));


        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    //parts search bar handler
    public void partsSearchResultHandler(ActionEvent actionEvent) {
        String pq = partQuery.getText();

        ObservableList<Part> parts = Inventory.lookupPart(pq);

        if (parts.size() == 0) {
            try {
                int partId = Integer.parseInt(pq);
                Part pt = searchByPartId(partId);
                if (pt != null)
                    parts.add(pt);
            } catch (NumberFormatException e) {
                //ignore it
            }
        }

        partsTableView.setItems(parts);
        if (parts.size() == 0) {
            System.out.println("Error");
            System.out.println("Part not found");
            partQuery.setText("");
        }
    }

    //Search by partID
    private Part searchByPartId(int partID) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        for (int i = 0; i < allParts.size(); i++) {
            Part pt = allParts.get(i);
            if (pt.getId() == partID) {
                return pt;
            }
        }
        return null;
    }


    // Product search bar handler
    public void productsSearchResultHandler(ActionEvent actionEvent) {
        String partialProductName = productQuery.getText();
        //Here
        ObservableList<Product> products = Inventory.lookupProduct(partialProductName);
        System.out.println(products.size());
        if (products.size() == 0) {
            try {
                int productID = Integer.parseInt(partialProductName);
                //Here
                Product product = Inventory.lookupProduct(productID);
                if (product != null)
                    products.add(product);
            } catch (NumberFormatException e) {
                //ignore
            }
        }

            productsTableView.setItems(products);
            if (products.size() == 0) {
                System.out.println("Error");
                System.out.println("Product not found");
                productQuery.setText("");

            }




    }
}