/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject2;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Astghik Minasyan, David Hernandez, James Ha
 */
public class MainFXMLController implements Initializable {

    private Duration simulationSpeed;
    private TranslateTransition transition = new TranslateTransition();
    private boolean running = false;
    private ArrayList<Planet> planets = new ArrayList<>();
    
    // should be removed after, see IMPORTANT comment way below
    private final Satellite satellite = addSatellite(600, 10, Color.CORAL);


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
    //private Pane simPane;
    private BorderPane mainPane;
    @FXML
    private Pane simPane;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        speedSlider.setDisable(true);
        simulationSpeed = new Duration(1000);
        transition.setOnFinished(eh -> {
            if (running) {
                launch();
            }
        });
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
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Settings");

        Label volumeLabel = new Label("Volume: ");
        Slider volumeSlider = new Slider(0, 100, 100);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(20);

        HBox volumeBox = new HBox(volumeLabel, volumeSlider);

        volumeBox.setPadding(new Insets(5, 5, 5, 5));
        volumeBox.setSpacing(15);
        volumeBox.setAlignment(Pos.CENTER);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // TODO: sound
            }
        });
    }

    @FXML
    private void handleLaunch(ActionEvent event) {
        speedSlider.setDisable(false);
        simulationSpeedHandler();
        launch();
    }

    public void secondWindow(String name) {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("New " + name);

        Label sizeLabel = new Label("Size");
        Label colorLabel = new Label("Color");
        Label xLabel = new Label("Layout x: ");
        Label yLabel = new Label("Layout y: ");
        Slider sizeSlider = new Slider(0, 150, 10);
        ColorPicker colorPicker = new ColorPicker(Color.RED);
        TextField xfield = new TextField();
        TextField yfield = new TextField();
        
        sizeSlider.setMajorTickUnit(10);
        sizeSlider.setShowTickMarks(true);
        
        if (name.equals("Satellite")) {
            sizeSlider.setDisable(true);
        }

        HBox coordinateBox = new HBox(xLabel, xfield, yLabel, yfield);
        coordinateBox.setSpacing(10);
        coordinateBox.setAlignment(Pos.CENTER);

        Button doneButton = new Button("Done");
        doneButton.setDisable(true);

        VBox layout = new VBox();
        layout.getChildren().addAll(sizeLabel, sizeSlider, colorLabel, colorPicker, coordinateBox, doneButton);
        layout.setPadding(new Insets(5, 5, 5, 5));
        layout.setSpacing(15);
        layout.setAlignment(Pos.CENTER);

        Scene secondaryScene = new Scene(layout, 500, 300);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.show();

        EventHandler coordinateHandler = (EventHandler<KeyEvent>) ((KeyEvent) -> {
            if (xfield.getText().matches("^\\d+$") && yfield.getText().matches("^\\d+$")) {
                doneButton.setDisable(false);
            } else {
                doneButton.setDisable(true);
            }
        });

        double[] radius = new double[1];
        radius[0] = sizeSlider.getValue();
        sizeSlider.valueProperty().addListener(cl -> {
            radius[0] = sizeSlider.getValue();
        });

        xfield.setOnKeyReleased(coordinateHandler);
        yfield.setOnKeyReleased(coordinateHandler);

        doneButton.setOnAction(e -> {
            double xProperty = Double.parseDouble(xfield.getText());
            double yProperty = Double.parseDouble(yfield.getText());

            Color color = colorPicker.getValue();
            
            secondaryStage.close();

            if (name.equals("Planet")) {
                addPlanet(xProperty, yProperty, radius[0], color);
            } else {
                addSatellite(xProperty, yProperty, color);
            }
        });
    }

    public void simulationSpeedHandler() {
        speedSlider.valueProperty().addListener(cl -> {
            simulationSpeed = new Duration(((int) speedSlider.getValue()) * 100);
        });
    }

    public void addPlanet(double x, double y, double radius, Color color) {
        Planet planet = new Planet(x, y, radius, color);
        mainPane.getChildren().add(planet.circle);
        planets.add(planet);
    }

    public Satellite addSatellite(double x, double y, Color color) {
        Satellite satellite = new Satellite(x, y, color);
        mainPane.getChildren().add(satellite.circle);
        return satellite;
    }
    
    public void launch() {
        // main simulation is here
        
        /*
        TODO: IMPORTANT (written by David)
        I think we should have only one satellite going at once because
        otherwise there's too many animation going and it gets complicated.
        I put this line (right below) to make it easier but we have to
        change the UI so that only one satellite can go at once
        Let me know if that's ok by text or whatever
        */
        
        // change this after 
        simulationSpeed = new Duration(1000);
        transition.setDuration(simulationSpeed);
        
        running = true;
        double forceX;
        double forceY;
        System.out.println("start");
        forceX = 0;
        forceY = 0;
        for (Planet planet : planets) {
            System.out.println("??");
            forceX += planet.mass * satellite.mass / Math.pow(planet.x - satellite.posX, 2)
                    * planet.x > satellite.posX ? 1 : -1;
            System.out.println("planet: " + planet.mass);
            System.out.println("satellite: " + satellite.mass);
            System.out.println("distance: " + Math.pow(planet.y - satellite.posY, 2));
            forceY += planet.mass * satellite.mass / Math.pow(planet.y - satellite.posY, 2)
                    * planet.y > satellite.posY ? 1 : -1;
        }
        System.out.println("force x: " + forceX + " force y: " + forceY);
        satellite.accX = forceX * 50; // arbitrary value
        satellite.accY = forceY * 50;
        satellite.changeVelocity();
        satellite.changePosition();
        transition.setNode(satellite.circle);
        transition.setByX(satellite.velX);
        transition.setByY(satellite.velY); 
        System.out.println(transition.getDuration().toSeconds() + " trans: " + transition.getByX() + " " + transition.getByY());
        System.out.println("end");
        transition.play();
    }
}
