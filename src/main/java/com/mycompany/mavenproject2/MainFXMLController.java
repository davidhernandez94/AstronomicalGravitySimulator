/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject2;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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

    private Duration simulationSpeed; // duration of each individual transition of satellite
    private TranslateTransition transition = new TranslateTransition(); // linear transition of satellite 
    private boolean running = false; // true when animaiton is running
    private ArrayList<Planet> planets = new ArrayList<>(); // list of planets currently in animation
    private PauseTransition pauseTrans = new PauseTransition(new Duration(1000)); // 1-second transition to show crash label
    private Satellite satellite; // satellite of animation

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
    private Slider speedSlider;
    @FXML
    private Button resetAllButton;
    @FXML
    private StackPane simPane;
    @FXML
    private Button lauchButton;
    @FXML
    private Button resetSatelliteButton;
    @FXML
    private Label crashLabel;

    /**
     * Initializes the controller class.
     * Setting up default configurations
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        crashLabel.setVisible(false);
        lauchButton.setDisable(true);
        speedSlider.setDisable(true);
        resetAllButton.setDisable(true);
        
        // hides crash label once 1 second is over
        pauseTrans.setOnFinished(eh -> crashLabel.setVisible(false)); 
        
        // when transition is finished, run it again so that it's continuous 
        // and there's smooth motion
        transition.setOnFinished(eh -> {
            if (running) {
                launch();
            }
        });

        handleResetAll();
    }

    /**
     * collapse sidebar in main scene
     * @param event triggered action event
     */
    @FXML
    private void collapseHandle(ActionEvent event) {
        // moves sidebar outside/inside of scene by translation
        if (collapseButton.getText().equals(">")) {
            collapseButton.setText("<");
            sidebar.setTranslateX(0);
            
        } else {
            collapseButton.setText(">");
            sidebar.setTranslateX(-180);
        }
    }

    /**
     * when newPlanetButton is clicked, open second window
     * @param event triggered action event
     */
    @FXML
    private void handleNewPlanet(ActionEvent event) {
        secondWindow("Planet");
    }

    /**
     * when newSatelliteButton is clicked, open second window
     * @param event triggered action event 
     */
    @FXML
    private void handleNewSatellite(ActionEvent event) {
        secondWindow("Satellite");
    }

    /**
     * when resetSatelliteButton is clicked, restart animation from beginning
     * satellite fields all return to default values
     * @param event triggered action event
     */
    @FXML
    private void handleResetSatellite(ActionEvent event) {
        if (satellite == null) {
            return;
        }

        // stop animation
        transition.stop();
        running = false;

        // reset satellite fields
        satellite.velX = 0;
        satellite.velY = 0;
        satellite.accX = 0;
        satellite.accY = 0;
        satellite.posX = satellite.initialX;
        satellite.posY = satellite.initialY;

        // bring satellite to original position
        satellite.circle.setTranslateX(satellite.initialX);
        satellite.circle.setTranslateY(satellite.initialY);

        lauchButton.setDisable(false);
    }

    /**
     * reset the whole scene
     */
    @FXML
    private void handleResetAll() {
        // remove satellite from simPane
        if (satellite != null) {
            if (satellite.circle != null) {
                simPane.getChildren().remove(satellite.circle);
            }
            
            satellite = null;
            newSatelliteButton.setDisable(false);
            lauchButton.setDisable(true);
            running = false;
        }

        // remove all planets from simPane
        if (!planets.isEmpty()) {
            for (Planet planet : planets) {
                if (planet != null && planet.circle != null) {
                    simPane.getChildren().remove(planet.circle);
                }
            }
            
            planets.clear();
        }
    }

    /**
     * when launch button is clicked, call animation 
     * @param event triggered action event
     */
    @FXML
    private void handleLaunch(ActionEvent event) {
        speedSlider.setDisable(false);
        simulationSpeedHandler();
        launch();
    }

    /**
     * second window: outside of first window, so MainFXML does not apply here.
     * lets user create a new planet or satellite
     * choices: colour, x-position, y-position, and size (only for planets)
     * @param name either "Satellite" or "Planet"
     */
    public void secondWindow(String name) {
        // initialize new window
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("New " + name);

        // manually creating labels and nodes to put in scene
        Label sizeLabel = new Label("Size");
        Label colorLabel = new Label("Color");
        Label xLabel = new Label("Layout x: ");
        Label yLabel = new Label("Layout y: ");
        Slider sizeSlider = new Slider(8, 50, 10);
        ColorPicker colorPicker = new ColorPicker(Color.RED);
        TextField xfield = new TextField();
        TextField yfield = new TextField();

        // settings for size slider
        sizeSlider.setMajorTickUnit(10);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setShowTickLabels(true);

        // satellite size is invariable, so disable the size slider
        if (name.equals("Satellite")) {
            sizeSlider.setDisable(true);
        }

        // setting for Hbox
        HBox coordinateBox = new HBox(xLabel, xfield, yLabel, yfield);
        coordinateBox.setSpacing(10);
        coordinateBox.setAlignment(Pos.CENTER);

        // done button 
        Button doneButton = new Button("Done");
        doneButton.setDisable(true);

        // settings for vbox
        VBox layout = new VBox();
        layout.getChildren().addAll(sizeLabel, sizeSlider, colorLabel, colorPicker, coordinateBox, doneButton);
        layout.setPadding(new Insets(5, 5, 5, 5));
        layout.setSpacing(15);
        layout.setAlignment(Pos.CENTER);

        // initialize scene 
        Scene secondaryScene = new Scene(layout, 500, 300);
        secondaryStage.setResizable(false);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.show();

        // handler that checks to see if values are integers
        // note: planets can be placed outside of scene
        EventHandler coordinateHandler = (EventHandler<KeyEvent>) ((KeyEvent) -> {
            if (xfield.getText().matches("^-?\\d+$") && yfield.getText().matches("^-?\\d+$")) {
                doneButton.setDisable(false);
            } else {
                doneButton.setDisable(true);
            }
        });

        // since a simple double value can't be changed within a lambda expresssion, 
        // make it an array with only one element. 
        // links radius of planet with size slider
        double[] radius = new double[1];
        radius[0] = sizeSlider.getValue();
        sizeSlider.valueProperty().addListener(cl -> {
            radius[0] = sizeSlider.getValue();
        });

        // check to see if x and y values are valid
        xfield.setOnKeyReleased(coordinateHandler);
        yfield.setOnKeyReleased(coordinateHandler);

        // when done, make new planet/satellite and add to simPane
        doneButton.setOnAction(e -> {
            double xProperty = Double.parseDouble(xfield.getText());
            double yProperty = Double.parseDouble(yfield.getText());

            Color color = colorPicker.getValue();

            secondaryStage.close();

            if (name.equals("Planet")) {
                addPlanet(xProperty, yProperty, radius[0], color);
            } else {
                addSatellite(xProperty, yProperty, color);
                newSatelliteButton.setDisable(true);
                lauchButton.setDisable(false);
            }
        });
    }

    /**
     * update simulationSpeed when slider is changed
     */
    @FXML
    public void simulationSpeedHandler() {
        simulationSpeed = new Duration((int) (100 / speedSlider.getValue()));
    }

    public void addPlanet(double x, double y, double radius, Color color) {
        if (isOverlapping(x, y, radius)) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Position");
        alert.setHeaderText("Planet Overlap");
        alert.setContentText("The new planet overlaps with another planet or the satellite. Choose a different location.");
        alert.show();
        return;
    }
        
        Planet planet = new Planet(x, y, radius, color);
        simPane.getChildren().add(planet.circle);
        planets.add(planet);

        resetAllButton.setDisable(false);
    }

    public void addSatellite(double x, double y, Color color) {
        satellite = new Satellite(x, y, color);
        simPane.getChildren().add(satellite.circle);

        resetAllButton.setDisable(false);
        newSatelliteButton.setDisable(true);
        lauchButton.setDisable(false);
    }

    public void launch() {
        // main simulation is here

        transition.setDuration(simulationSpeed);

        running = true;
        double forceX = 0;
        double forceY = 0;
        for (Planet planet : planets) {
            double distance = Math.sqrt(Math.pow(planet.x - satellite.posX, 2) + Math.pow(planet.y - satellite.posY, 2));
            forceX += (planet.mass * satellite.mass * Math.abs(planet.x - satellite.posX)/ (distance + 30))
                    * (planet.x > satellite.posX ? 1 : -1);
            forceY += (planet.mass * satellite.mass * Math.abs(planet.y - satellite.posY)/ (distance + 30))
                    * (planet.y > satellite.posY ? 1 : -1);

            if (satellite != null && satellite.circle.getBoundsInParent().intersects(planet.circle.getBoundsInParent())) {
                newSatelliteButton.setDisable(false);
                lauchButton.setDisable(true);
                running = false;
                resetAllButton.setDisable(planets.isEmpty());
                simPane.getChildren().remove(satellite.circle);
                crashLabel.setVisible(true);
                pauseTrans.play();
                break;
            }
        }
        satellite.circle.toFront();
        satellite.accX = forceX / 80_000;
        satellite.accY = forceY / 80_000;
        satellite.changeVelocity();
        satellite.changePosition();
        transition.setNode(satellite.circle);
        transition.setByX(satellite.velX);
        transition.setByY(satellite.velY);
        transition.play();
    }

    private boolean isOverlapping(double x, double y, double radius) {
        for (Planet p : planets) {
            double dist = Math.hypot(p.x - x, p.y - y);
            if (dist < p.radius + radius) {
                return true;
            }
        }

        if (satellite != null) {
            double dist = Math.hypot(satellite.posX - x, satellite.posY - y);
            if (dist < satellite.circle.getRadius() + radius) {
                return true;
            }
        }

        return false; 
    }
}
