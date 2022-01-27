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

 */

public class ModifyPartController implements Initializable {
    //stage and scene used to return to main
    Stage stage;
    Parent scene;
    //Declares fields
    public ToggleGroup partSource;
    public RadioButton inhouseRBtn;
    public RadioButton outsourcedRBtn;
    public Label switchLbl;
    public TextField partIdTxt;
    public TextField partNameTxt;
    public TextField partInvTxt;
    public TextField partPriceTxt;
    public TextField maxInvTxt;
    public TextField machineIdTxt;
    public TextField minInvTxt;

    //Sets label to "Machine ID" for InHouse part
    public void onActionInHouseMenu(ActionEvent event) {

        switchLbl.setText("Machine ID ");
    }

    //Sets label to "Company Name" for Outsourced Part
    public void onActionOutsourcedMenu(ActionEvent event) {

        switchLbl.setText("Company Name ");
    }


    public void onActionCxlPartMod(ActionEvent event) throws IOException {
        //Confirms cancellation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All modifications made will not be saved. Do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            Inventory.returnHome(stage, event, scene);
        }

    }

    //Populates data from received part into corresponding text fields
    public void sentPart(Part partToReceive) {
        partIdTxt.setText(String.valueOf(partToReceive.getId()));
        partNameTxt.setText(partToReceive.getName());
        partInvTxt.setText(String.valueOf(partToReceive.getStock()));
        partPriceTxt.setText(String.valueOf(partToReceive.getPrice()));
        maxInvTxt.setText(String.valueOf(partToReceive.getMax()));
        minInvTxt.setText(String.valueOf(partToReceive.getMin()));


        //Selects an appropriate radio button and switches label and data innput type to reflect InHouse part
        if (partToReceive instanceof Outsourced) {
            switchLbl.setText("Company Name ");
            machineIdTxt.setText(((Outsourced) partToReceive).getCompanyName());
            inhouseRBtn.setSelected(false);
            outsourcedRBtn.setSelected(true);
        }
        //Selects an appropriate radio button and switches label and data input type to reflect Outsourced part
        else if (partToReceive instanceof InHouse)
        {
            machineIdTxt.setText(String.valueOf(((InHouse) partToReceive).getMachineId()));
            inhouseRBtn.setSelected(true);
        }


    }

    /** RUNTIME ERROR - NumberFormatException
        The most common runtime error I encountered was a NumberFormatException Error. This can be caused if a user
        types a letter in a text field designated for an integer or double. I fixed this error using try and catch blocks.
        The try block allowed me to define the code I wanted tested for errors while being executed. The catch statement
        allowed me to handle the error if it occurred. I placed an error dialog box in my catch statement to notify the
        user to enter appropriate values if the error occurred.
     */
    public void onActionSavePart(ActionEvent event) throws IOException {

        try {
            //converts received strings into appropriate data types
            int id = Integer.parseInt(partIdTxt.getText());
            String name = partNameTxt.getText();
            int stock = Integer.parseInt(partInvTxt.getText());
            double price = Double.parseDouble(partPriceTxt.getText());
            int min = Integer.parseInt(minInvTxt.getText());
            int max = Integer.parseInt(maxInvTxt.getText());
            String companyName = machineIdTxt.getText();
            int machineId = Integer.parseInt(machineIdTxt.getText());

            //Sets default values for text fields expecting integers and doubles
            if ("".equals(id)) { id = 0; }
            if ("".equals(stock)) { stock = 0; }
            if("".equals(price)){ price = 0.0;}
            if("".equals(min)){ min = 0;}
            if("".equals(max)){ max = 0;}

            //Checking for logical errors
            Inventory.logicalErrorCheck(stock, min, max);

            //Creates a new in-house part and takes user back to main menu
            if (inhouseRBtn.isSelected()) {
                Part inhousePart = new InHouse(id, name, stock, price, min, max, machineId);
                if ((InHouse.ispartValid(stock, price, min, max)))
                {
                    Inventory.amendPart(id, inhousePart);
                    Inventory.returnHome(stage, event, scene);
                }
            }
            //Creates a new outsourced part
            else if (outsourcedRBtn.isSelected()) {
                Part outsourcedPart = new Outsourced(id, name, stock, price, min, max, companyName);
                if ((Outsourced.ispartValid(stock, price, min, max)))
                {
                    Inventory.amendPart(id, outsourcedPart);
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



    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
}
