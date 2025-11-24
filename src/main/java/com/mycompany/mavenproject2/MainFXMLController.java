/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Astghik Minasyan
 */
public class MainFXMLController implements Initializable {

    public double simulationSpeed;
    public double radius;

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
    private StackPane mainPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        speedSlider.setDisable(true);
        simulationSpeed = 0;
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
    private void handleResetSatellites(ActionEvent event) {
    }

    @FXML
    private void handleResetAll(ActionEvent event) {
    }

    @FXML
    private void handleSettings(ActionEvent event) {
    }

    @FXML
    private void handleLaunch(ActionEvent event) {
        simulationSpeed = 1;
        speedSlider.setDisable(false);
        simulationSpeedHandler();
    }

    public void secondWindow(String name) {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("New " + name);

        Label sizeLabel = new Label("Size");
        Label colorLabel = new Label("Color");
        Label xLabel = new Label("Layout x: ");
        Label yLabel = new Label("Layout y: ");
        Slider sizeSlider = new Slider(0, 30, 0);
        ColorPicker colorPicker = new ColorPicker(Color.RED);
        TextField xfield = new TextField();
        TextField yfield = new TextField();

        HBox coordinateBox = new HBox(xLabel, xfield, yLabel, yfield);
        coordinateBox.setSpacing(10);
        coordinateBox.setAlignment(Pos.CENTER);

        Button doneButton = new Button("Done");

        VBox layout = new VBox();
        layout.getChildren().addAll(sizeLabel, sizeSlider, colorLabel, colorPicker, coordinateBox, doneButton);
        layout.setPadding(new Insets(5, 5, 5, 5));
        layout.setSpacing(15);
        layout.setAlignment(Pos.CENTER);

        Scene secondaryScene = new Scene(layout, 500, 300);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.show();

        double xProperty = Double.parseDouble(xfield.getText());
        double yProperty = Double.parseDouble(yfield.getText());

        sizeSlider.valueProperty().addListener(cl -> {
            radius = sizeSlider.getValue();
        });

        Color color = colorPicker.getValue();

        doneButton.setOnAction(e -> {
            System.out.println("aaa");
            secondaryStage.close();

//            if (name.equals("Planet")) {
//                addPlanet(xProperty, yProperty, radius, color);
//            } else {
//                addSatellite(xProperty, yProperty, radius, color);
//            }
        });
    }

    public void simulationSpeedHandler() {
        speedSlider.valueProperty().addListener(cl -> {
            simulationSpeed = speedSlider.getValue();
        });
    }

    public void addPlanet(double x, double y, double radius, Color color) {
//        // uncomment once branches merge
//        Planet planet = new Planet(x, y, radius, color);
//        mainPane.getChildren().remove(sidebar);
//        mainPane.getChildren().addAll(planet, sidebar);
    }

    public void addSatellite(double x, double y, double radius, Color color) {
        // TODO
    }
}
