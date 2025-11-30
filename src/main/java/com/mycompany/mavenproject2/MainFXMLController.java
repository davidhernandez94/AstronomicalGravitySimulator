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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    private Duration simulationSpeed;
    private TranslateTransition transition = new TranslateTransition();
    private boolean running = false;
    private ArrayList<Planet> planets = new ArrayList<>();
    private PauseTransition pauseTrans = new PauseTransition(new Duration(1000));

    // should be removed after, see IMPORTANT comment way below
    private Satellite satellite;

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
    private Button settingsButton;
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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        crashLabel.setVisible(false);
        lauchButton.setDisable(true);
        speedSlider.setDisable(true);
        speedSlider.setMin(.5);
        speedSlider.setMax(6);
        resetAllButton.setDisable(true);
        simulationSpeed = new Duration(50);
        pauseTrans.setOnFinished(eh -> crashLabel.setVisible(false));
        transition.setOnFinished(eh -> {
            if (running) {
                launch();
            }
        });

        handleResetAll();
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
    private void handleResetSatellite(ActionEvent event) {
        if (satellite == null) {
            return;
        }

        transition.stop();
        running = false;

        satellite.velX = 0;
        satellite.velY = 0;
        satellite.accX = 0;
        satellite.accY = 0;

        satellite.posX = satellite.initialX;
        satellite.posY = satellite.initialY;

        satellite.circle.setTranslateX(satellite.initialX);
        satellite.circle.setTranslateY(satellite.initialY);

        lauchButton.setDisable(false);
    }

    @FXML
    private void handleResetAll() {
        if (satellite != null) {
            if (satellite.circle != null) {
                simPane.getChildren().remove(satellite.circle);
            }
            satellite = null;
            newSatelliteButton.setDisable(false);
            lauchButton.setDisable(true);
            running = false;
        }

        if (!planets.isEmpty()) {
            for (Planet planet : planets) {
                if (planet != null && planet.circle != null) {
                    simPane.getChildren().remove(planet.circle);
                }
            }
            planets.clear();
        }
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
        Slider sizeSlider = new Slider(8, 50, 10);
        ColorPicker colorPicker = new ColorPicker(Color.RED);
        TextField xfield = new TextField();
        TextField yfield = new TextField();

        sizeSlider.setMajorTickUnit(10);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setShowTickLabels(true);

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
        secondaryStage.setResizable(false);
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
                newSatelliteButton.setDisable(true);
                lauchButton.setDisable(false);
            }
        });
    }

    @FXML
    public void simulationSpeedHandler() {
        System.out.println("speed: " + speedSlider.getValue());
        simulationSpeed = new Duration((int) (60 / speedSlider.getValue()));
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

        transition.setDuration(simulationSpeed.divide(20));

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
        satellite.accX = forceX / 8_000_000;
        satellite.accY = forceY / 8_000_000;
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
