/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Astghik Minasyan
 */
public class MainFXMLController implements Initializable {

    @FXML
    private Button collapseButton;
    @FXML
    private VBox sidebar;
    @FXML
    private Button boton;
    @FXML
    private Button newPlanetButton;
    @FXML
    private Button newSatelliteButton;
    @FXML
    private Button newButton;
    @FXML
    private Slider speedSlider;
    @FXML
    private Button resetSatellitesButton;
    @FXML
    private Button resetAllButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Pane mainPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void collapseHandle(ActionEvent event) {
        if (collapseButton.getText().equals(">")) {
            collapseButton.setText("<");
            sidebar.setTranslateX(0);
        } else {
            collapseButton.setText(">");
            sidebar.setTranslateX(-180);
        }
    }

    @FXML
    private void handleNewPlanet(ActionEvent event) {
        secondWindow("Planet");
    }

    @FXML
    private void handleNewSatellite(ActionEvent event) {
        secondWindow("Satellite");
    }

    @FXML
    private void handeLaunch(ActionEvent event) {
    }

    @FXML
    private void handleResetSatellites(ActionEvent event) {
    }

    @FXML
    private void handleResetAll(ActionEvent event) {
    }

    @FXML
    private void handleSettings(ActionEvent event) {
    }
    
    public void secondWindow (String name) {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("New " + name);
        
        Label sizeLabel = new Label("Size");
        Label colorLabel = new Label("Color");
        Slider sizeSlider = new Slider(0, 30, 0);
        ColorPicker colorPicker = new ColorPicker(Color.RED); 
        Button okButton = new Button("Ok");
        
        VBox layout = new VBox();
        layout.getChildren().addAll(sizeLabel, sizeSlider, colorLabel, colorPicker, okButton);
        layout.setPadding(new Insets(5, 5, 5, 5));
        layout.setSpacing(15);
        layout.setAlignment(Pos.CENTER);
        
        Scene secondaryScene = new Scene(layout, 500, 300);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.show();
        
        okButton.setOnAction(e -> secondaryStage.close());
    }
}
