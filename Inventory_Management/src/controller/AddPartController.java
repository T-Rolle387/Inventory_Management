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
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**
 * FXML Controller class
 *
 * @author Tiffany Rolle
 */

public class AddPartController implements Initializable {
    //Declares fields
    Stage stage;
    Parent scene;
    public RadioButton inHouseRBtn;
    public RadioButton outsourcedRBtn;
    public Label changeMe;
    public TextField partIdTxt;
    public TextField partNameTxt;
    public TextField partInvTxt;
    public TextField partPriceTxt;
    public TextField maxInvTxt;
    public TextField machineIdTxt;
    public TextField minInvTxt;
    private int id;




    //Brings you back to the main menu upon clicking cancel button
    public void onActionCxlPartAddition(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all text field values. Do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
        {
           Inventory.returnHome(stage, event, scene);
        }
    }

    //sets last text field to Machine ID if InHouse is selected
    public void onActionInHouseMenu(ActionEvent event) {
        changeMe.setText("Machine ID ");
    }

    //sets last text field to Company Name if Outsourced is selected
    public void onActionOutsourcedMenu(ActionEvent event) {
        changeMe.setText("Company Name ");
    }

    /** RUNTIME ERROR - NumberFormatException
     The most common runtime error I encountered was a NumberFormatException Error. This can be caused if a user
     types a letter in a text field designated for an integer or double. I fixed this error using try and catch blocks.
     The try block allowed me to define the code I wanted tested for errors while being executed. The catch statement
     allowed me to handle the error if it occurred. I placed an error dialog box in my catch statement to notify the
     user to enter appropriate values if the error occurred.
     */

    public void onActionSavePart(ActionEvent event) throws IOException
    {

    try

    {
        //converts strings to desired data types in our constructor
        int id = Inventory.getPartIdCt();
        String name = partNameTxt.getText();
        int stock = Integer.parseInt(partInvTxt.getText());
        double price = Double.parseDouble(partPriceTxt.getText());
        int min = Integer.parseInt(minInvTxt.getText());
        int max = Integer.parseInt(maxInvTxt.getText());



        //Alerts user to enter part name if text field left blank
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter product name.");
            alert.showAndWait();
        }

        //Sets default values for text fields expecting integers and doubles
        if ("".equals(id)) { id = 0; }
        if ("".equals(stock)) { stock = 0; }
        if("".equals(price)){ price = 0.0;}
        if("".equals(min)){ min = 0;}
        if("".equals(max)){ max = 0;}


        //Creates a new in-house part and sets machineId to default 0 if machineId not entered
        if (inHouseRBtn.isSelected()) {
            int machineId = Integer.parseInt(machineIdTxt.getText());
            if ("".equals(machineId)){ machineId = 0; }
            Part inhousePart = new InHouse(id, name, stock, price, min, max, machineId);
            Inventory.logicalErrorCheck(stock, min, max);
            if (InHouse.ispartValid(stock, price, min, max))
            {
                Inventory.addPart(inhousePart);
                Inventory.returnHome(stage, event, scene);
            }
        }

        //Creates a new outsourced part
        else if (outsourcedRBtn.isSelected()) {
            String companyName = machineIdTxt.getText();
            Part outsourcedPart = new Outsourced(id, name, stock, price, min, max, companyName);
            Inventory.logicalErrorCheck(stock, min, max);
            if(Outsourced.ispartValid(stock, price, min, max)) {
                Inventory.addPart(outsourcedPart);
                Inventory.returnHome(stage, event, scene);

            }
        }

    }
    catch(NumberFormatException e)

    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText("Please enter a valid value for each text field.");
        alert.showAndWait();
    }


}





    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id = Inventory.getPartIdCt();
        partIdTxt.setText("Auto Gen: " + id);
    }    
    
}
