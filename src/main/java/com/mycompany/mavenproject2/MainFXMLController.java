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

    // should be removed after, see IMPORTANT comment way below
    private Satellite satellite;

    @FXML
    private Button collapseButton;
    @FXML
    private VBox sidebar;
    @FXML
    private Button newPlanetButton;
    @FXML
    private Button newSatelliteButton;
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
    @FXML
    private Label timeLabel;
    @FXML
    private Button stopPlayButton;
    @FXML
    private Label velocityLabel;
    @FXML
    private Label accelerationLabel;
    @FXML
    private StackPane simPane;
    @FXML
    private Button launchButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        satellite = addSatellite(400, 400, Color.CORAL);
        speedSlider.setDisable(true);
        simulationSpeed = new Duration(50);
        stopPlayButton.setDisable(true);
        transition.setOnFinished(eh -> {
            if (running) {
                updateSimulation();
            }
        });
    }

    @FXML
    private void collapseHandler(ActionEvent event) {
        if (sidebar.getTranslateX() == -180) {
            sidebar.setTranslateX(0);
        } else {
            sidebar.setTranslateX(-180);
        }
    }

    @FXML
    private void handleNewPlanet(ActionEvent event) {
        newObjectWindow("Planet");
    }

    @FXML
    private void handleNewSatellite(ActionEvent event) {
        newObjectWindow("Satellite");
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

        Scene secondaryScene = new Scene(volumeBox, 250, 50);
        settingsStage.setScene(secondaryScene);
        settingsStage.show();
    }

    @FXML
    private void handleLaunch(ActionEvent event) {
        speedSlider.setDisable(false);
        simulationSpeedHandler();
        running = true;
        updateSimulation();
        stopPlayButton.setDisable(false);
        launchButton.setDisable(true);
    }

    @FXML
    private void handleStopPlay(ActionEvent event) {
        if (running) {
            running = false;
            transition.stop();
            stopPlayButton.setStyle("-fx-background-color: #216e5a;"
                    + "-fx-font-family: consolas;"
                    + "-fx-text-fill: white;"
                    + "-fx-font-weight: 900;");
            stopPlayButton.setText("Play");
        } else {
            running = true;
            updateSimulation();
            stopPlayButton.setStyle("-fx-background-color: #b54e64;"
                    + "-fx-font-family: consolas;"
                    + "-fx-text-fill: white;"
                    + "-fx-font-weight: 900;");
            stopPlayButton.setText("Stop");
        }
    }

    public void newObjectWindow(String name) {
        Stage newObjectStage = new Stage();
        newObjectStage.setTitle("New " + name);

        Label sizeLabel = new Label("Size");
        Label colorLabel = new Label("Color");
        Label xLabel = new Label("X Position: ");
        Label yLabel = new Label("Y Position: ");
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
        newObjectStage.setScene(secondaryScene);
        newObjectStage.show();

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
            double xProperty = xfield.getText().isEmpty() ? 0.0 : Double.parseDouble(xfield.getText());
            double yProperty = yfield.getText().isEmpty() ? 0.0 : Double.parseDouble(yfield.getText());

            Color color = colorPicker.getValue();
            newObjectStage.close();

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
        simPane.getChildren().add(planet.circle);
        planets.add(planet);
    }

    public Satellite addSatellite(double x, double y, Color color) {
        Satellite satellite = new Satellite(x, y, color);
        simPane.getChildren().add(new Satellite(x, y, color).circle);
        return satellite;
    }

    public void updateSimulation() {
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
        simulationSpeed = new Duration(800);
        transition.setDuration(simulationSpeed);

        running = true;
        double forceX = 0;
        double forceY = 0;
        for (Planet planet : planets) {
            double distance = Math.sqrt(Math.pow(planet.x - satellite.posX, 2) + Math.pow(planet.y - satellite.posY, 2));
            forceX += (planet.mass * satellite.mass / (distance + 30))
                    * (planet.x > satellite.posX ? 1 : -1);
            System.out.println("planet: mass" + planet.mass + " x" + planet.x + " y" + planet.y);
            System.out.println("satellite: mass" + satellite.mass + " x" + satellite.posX + " y" + satellite.posY);
            System.out.println("distanceX: " + (planet.x - satellite.posX));
            System.out.println("distanceY: " + (planet.y - satellite.posY));
            forceY += (planet.mass * satellite.mass / (distance + 30))
                    * (planet.y > satellite.posY ? 1 : -1);
            }
        System.out.println("force x: " + forceX + " force y: " + forceY);
        satellite.accX = forceX / 100000; // arbitrary value
        satellite.accY = forceY / 100000;
        satellite.changeVelocity();
        satellite.changePosition();
        System.out.println("satellite posx" + satellite.posX + " posy" + satellite.posY + " velx" + satellite.velX + " vely" + satellite.velY);
        transition.setNode(satellite.circle);
        transition.setByX(satellite.velX);
        transition.setByY(satellite.velY);
        System.out.println(transition.getDuration().toSeconds() + " trans: " + transition.getByX() + " " + transition.getByY());
        System.out.println("end");
        transition.play();
    }
}
