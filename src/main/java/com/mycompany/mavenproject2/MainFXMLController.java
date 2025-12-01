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
 * Git URL: https://github.com/davidhernandez94/AstronomicalGravitySimulator.git
 * @author Astghik Minasyan, David Hernandez, James Ha
 */
public class MainFXMLController implements Initializable {

    // Duration of each individual transition of satellite
    private Duration simulationSpeed;
    // Linear transition of satellite
    private TranslateTransition transition = new TranslateTransition();  
    // True when animation is running
    private boolean running = false; 
    // 1-second transition to show crash label
    private PauseTransition pauseTrans = new PauseTransition(new Duration(1000));
    // Start time of simulation
    private long startTime = 0; 
    // Total elapsed time in milliseconds
    private long elapsedTime = 0; 
    // List of planets currently in animation
    ArrayList<Planet> planets = new ArrayList<>(); 
    // Satellite of animation
    Satellite satellite; 

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
    private Button resetAllButton;
    @FXML
    private StackPane simPane;
    @FXML
    private Button launchButton;
    @FXML
    private Label crashLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Button stopPlayButton;
    @FXML
    private Label velocityLabel;
    @FXML
    private Label accelerationLabel;
    @FXML
    private StackPane mainPane;
    @FXML
    private Button resetSatellitesButton;

    /**
     * Initializes the controller class. Setting up default configurations
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Make the crash label invisible in the beginning
        crashLabel.setVisible(false);
        // Disable the lauch button
        launchButton.setDisable(true);
        // Disable the speed slider
        speedSlider.setDisable(true);
        // Disable the reset all button
        resetAllButton.setDisable(true);
        // Disable the stop play button
        stopPlayButton.setDisable(true);

        // Hide crash label once 1 second is over
        pauseTrans.setOnFinished(eh -> crashLabel.setVisible(false));

        // When transition is finished, run it again so that it's continuous 
        // And there's smooth motion
        transition.setOnFinished(eh -> {
            if (running) {
                updateSimulation();
            }
        });

        // Call handleResetAll method
        handleResetAll();
    }

    /**
     * Collapse sidebar in main scene
     *
     * @param event the triggered action event
     */
    @FXML
    private void collapseHandle(ActionEvent event) {
        // Move sidebar outside/inside of scene by translation
        if (sidebar.getTranslateX() == -180) {
            sidebar.setTranslateX(0);

        } else {
            sidebar.setTranslateX(-180);
        }
    }

    /**
     * When newPlanetButton is clicked, open second window
     *
     * @param event triggered action event
     */
    @FXML
    private void handleNewPlanet(ActionEvent event) {
        // Call newObjectWindow method
        newObjectWindow("Planet");
    }

    /**
     * When newSatelliteButton is clicked, open second window
     *
     * @param event triggered action event
     */
    @FXML
    private void handleNewSatellite(ActionEvent event) {
        // Call newObjectWindow method
        newObjectWindow("Satellite");
    }

    /**
     * When resetSatelliteButton is clicked, restart animation from beginning
     * Satellite fields all return to default values
     *
     * @param event the triggered action event
     */
    @FXML
    private void handleResetSatellite(ActionEvent event) {
        // Check if the satellite is initialized
        if (satellite == null) {
            return;
        }

        // Make the satellite visible
        satellite.circle.setVisible(true);

        // Stop animation
        transition.stop();
        // The animation is not running
        running = false;

        // Reset satellite fields
        satellite.velX = 0;
        satellite.velY = 0;
        satellite.accX = 0;
        satellite.accY = 0;
        satellite.posX = satellite.initialX;
        satellite.posY = satellite.initialY;

        // Bring satellite to original position
        satellite.circle.setTranslateX(satellite.initialX);
        satellite.circle.setTranslateY(satellite.initialY);

        // Reset labels
        elapsedTime = 0;
        updateLabels();

        // Enable launch button
        launchButton.setDisable(false);
    }

    /**
     * Reset the whole scene by removing all planets and the satellite
     */
    @FXML
    private void handleResetAll() {
        // Remove satellite from simPane
        if (satellite != null) {
            if (satellite.circle != null) {
                simPane.getChildren().remove(satellite.circle);
            }

            // Delete the existing satellite
            satellite = null;
            // Enable new satellite button
            newSatelliteButton.setDisable(false);
            // Disable launch button
            launchButton.setDisable(true);
            // The animation is not running
            running = false;
        }

        // Remove all planets from simPane
        if (!planets.isEmpty()) {
            for (Planet planet : planets) {
                if (planet != null && planet.circle != null) {
                    simPane.getChildren().remove(planet.circle);
                }
            }

            // Clear the arraylist of planets
            planets.clear();
        }

        // Reset labels
        elapsedTime = 0;
        // Call updateLabels method
        updateLabels();
    }

    /**
     * When updateSimulation button is clicked, call animation
     *
     * @param event the triggered action event
     */
    @FXML
    private void handleLaunch(ActionEvent event) {
        // Enable the speed slider
        speedSlider.setDisable(false);
        // Call simulationSpeedHandeler method
        simulationSpeedHandler();
        // The animation is now running
        running = true;
        startTime = System.currentTimeMillis();
        // Call updateSimulation method
        updateSimulation();
        // Enable stopPlay button
        stopPlayButton.setDisable(false);
        // Disable launch button
        launchButton.setDisable(true);
    }

    /**
     * When stop/play button is pressed, pauses movement and pauses time
     *
     * @param event the triggered action event
     */
    @FXML
    private void handleStopPlay(ActionEvent event) {
        // Check if the animation is running
        if (running) {
            // Stop animation if it is running
            running = false;
            transition.stop();
            elapsedTime += System.currentTimeMillis() - startTime;
            
            // Change pause button to play button
            stopPlayButton.setStyle("-fx-background-color: #216e5a;"
                    + "-fx-font-family: consolas;"
                    + "-fx-text-fill: white;"
                    + "-fx-font-weight: 900;");
            stopPlayButton.setText("Play");
        } else {
            // Continue animation if it isn't running
            running = true;
            startTime = System.currentTimeMillis();
            // Call updatSimulation method
            updateSimulation();
            
            // Change play button to play pause
            stopPlayButton.setStyle("-fx-background-color: #b54e64;"
                    + "-fx-font-family: consolas;"
                    + "-fx-text-fill: white;"
                    + "-fx-font-weight: 900;");
            stopPlayButton.setText("Stop");
        }
    }

    /**
     * Second window: outside of first window, so MainFXML does not apply here.
     * Lets user create a new planet or satellite choices: color, x-position,
     * y-position, and size (only for planets)
     *
     * @param name either "Satellite" or "Planet"
     */
    public void newObjectWindow(String name) {
        // initialize new window
        Stage newObjectStage = new Stage();
        newObjectStage.setTitle("New " + name);

        // manually creating labels and nodes to put in scene
        Label sizeLabel = new Label("Size");
        sizeLabel.setStyle("-fx-font-family: consolas;"
                + "-fx-text-fill: white;");

        Label colorLabel = new Label("Color");
        colorLabel.setStyle("-fx-font-family: consolas;"
                + "-fx-text-fill: white;");

        Label xLabel = new Label("X Position: ");
        xLabel.setStyle("-fx-font-family: consolas;"
                + "-fx-text-fill: white;");

        Label yLabel = new Label("Y Position: ");
        yLabel.setStyle("-fx-font-family: consolas;"
                + "-fx-text-fill: white;");

        Slider sizeSlider = new Slider(8, 50, 10);
        sizeSlider.setStyle("-fx-control-inner-background: #94bdff; "
                + "-fx-tick-mark-fill: #94bdff;"
                + "-fx-tick-label-fill: white;"
                + "-fx-font-family: consolas;"
                + "-fx-text-fill: white;");

        ColorPicker colorPicker = new ColorPicker(Color.RED);
        colorPicker.setStyle("-fx-background-color: #4b61ab;"
                + "-fx-font-family: consolas;"
                + "-fx-text-fill: white;");

        TextField xfield = new TextField();
        xfield.setStyle("-fx-background-color: #4b61ab;"
                + "-fx-font-family: consolas;"
                + "-fx-text-fill: white;");

        TextField yfield = new TextField();
        yfield.setStyle("-fx-background-color: #4b61ab;"
                + "-fx-font-family: consolas;"
                + "-fx-text-fill: white;");

        // Settings for size slider
        sizeSlider.setMajorTickUnit(10);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setShowTickLabels(true);

        // Satellite size is invariable, so disable the size slider
        if (name.equals("Satellite")) {
            sizeSlider.setDisable(true);
        }

        // Setting for Hbox
        HBox coordinateBox = new HBox(xLabel, xfield, yLabel, yfield);
        coordinateBox.setSpacing(10);
        coordinateBox.setAlignment(Pos.CENTER);

        // Done button 
        Button doneButton = new Button("Done");
        doneButton.setStyle("-fx-background-color: #216e5a;"
                + "-fx-font-family: consolas;"
                + "-fx-text-fill: white;");
        doneButton.setDisable(true);

        // Settings for vbox
        VBox layout = new VBox();
        layout.getChildren().addAll(sizeLabel, sizeSlider, colorLabel, colorPicker, coordinateBox, doneButton);
        layout.setPadding(new Insets(5, 5, 5, 5));
        layout.setSpacing(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2d3369;"
                + "-fx-font-family: consolas;"
                + "-fx-text-fill: white;");

        // Initialize scene 
        Scene secondaryScene = new Scene(layout, 500, 300);
        newObjectStage.setResizable(false);
        newObjectStage.setScene(secondaryScene);
        newObjectStage.show();

        // Handler that checks to see if values are positive integers
        // Note: planets can be placed outside of scene
        EventHandler coordinateHandler = (EventHandler<KeyEvent>) ((KeyEvent) -> {
            if (xfield.getText().matches("^-?\\d+$") && yfield.getText().matches("^-?\\d+$")) {
                doneButton.setDisable(false);
            } else {
                doneButton.setDisable(true);
            }
        });

        // Since a simple double value can't be changed within a lambda expresssion, 
        // Make it an array with only one element. 
        // Links radius of planet with size slider
        double[] radius = new double[1];
        radius[0] = sizeSlider.getValue();
        sizeSlider.valueProperty().addListener(cl -> {
            radius[0] = sizeSlider.getValue();
        });

        // Check to see if x and y values are valid by calling the handler
        xfield.setOnKeyReleased(coordinateHandler);
        yfield.setOnKeyReleased(coordinateHandler);

        // When done is pressed, make new planet/satellite and add to simPane
        doneButton.setOnAction(e -> {
            // If either fields are empty, attribute value 0
            double xProperty = xfield.getText().isEmpty() ? 0.0 : Double.parseDouble(xfield.getText());
            double yProperty = yfield.getText().isEmpty() ? 0.0 : Double.parseDouble(yfield.getText());

            // Set color of the planet/ satellite
            Color color = colorPicker.getValue();

            // Close the window
            newObjectStage.close();

            // Check if the new object is a planet or satellite
            if (name.equals("Planet")) {
                // Call addPlanet method
                addPlanet(xProperty, yProperty, radius[0], color);
            } else {
                if (addSatellite(xProperty, yProperty, color)) {
                    newSatelliteButton.setDisable(true);
                    launchButton.setDisable(false);
                }
            }
        });
    }

    /**
     * update simulationSpeed when slider is changed
     */
    @FXML
    public void simulationSpeedHandler() {
        // Here, the value 150 is what seems most ergonomic, not an exact constant
        simulationSpeed = new Duration((int) (150 / speedSlider.getValue()));
    }

    /**
     * add new planet to simPane
     *
     * @param x x-position (0 = left edge)
     * @param y y-position (0 = top edge)
     * @param radius radius of the planet
     * @param color color of the planet
     */
    public void addPlanet(double x, double y, double radius, Color color) {
        // Alerts user if planet overlaps with another
        if (isOverlapping(x, y, radius)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Position");
            alert.setHeaderText("Planet Overlap");
            alert.setContentText("The new planet overlaps with another planet or the satellite. Choose a different location.");
            alert.show();
            return;
        }

        // Make new planet and add it to simPane
        Planet planet = new Planet(x, y, radius, color);
        simPane.getChildren().add(planet.circle);
        planets.add(planet);

        // Disable reset all button
        resetAllButton.setDisable(false);
    }

    /**
     * Add new satellite to simPane
     *
     * @param x x-position (0 = left edge)
     * @param y y-position (0 = top edge)
     * @param color color of satellite
     * @return true if satellite was added successfully, false if overlap
     * prevented it
     */
    public boolean addSatellite(double x, double y, Color color) {
        // Alerts user if satellite overlaps with planet
        if (isOverlapping(x, y, Satellite.RADIUS)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Position");
            alert.setHeaderText("Satellite Overlap");
            alert.setContentText("The new satellite overlaps with a planet. Choose a different location.");
            alert.show();
            return false;
        }

        // Add new satellite to simPane
        satellite = new Satellite(x, y, color);
        satellite.circle.setVisible(true);
        simPane.getChildren().add(satellite.circle);

        // Change accessibility of certain buttons 
        resetAllButton.setDisable(false);
        return true;
    }

    /**
     * Animation for the satellite, run many times per second
     * Main simulation is here
     */
    public void updateSimulation() {
        running = true;

        // Set length of animation 
        transition.setDuration(simulationSpeed);

        // Force is set to 0
        // The gravitational pull of the planets will change it
        double forceX = 0;
        double forceY = 0;

        // Iterate through each planet in planets list
        // For each, add its gravitational force to forceX and forceY
        for (Planet planet : planets) {
            // Distance between satellite and planet using a^2 + b^2 = c^2
            double distance = Math.hypot(planet.x - satellite.posX, planet.y - satellite.posY);

            /*
            this is the main formula that is being shown in this project
            
            add the planet's gravitational pull to the forces using the formula 
            F = (constant) * (mass1 * mass2 / distance^2)
            where the constant is not the constant G, because units of 
            mass and distance are different here than in real physics application.
            the constant is put further down in the code when the forces are 
            divided by a constant to give the satellite's acceleration
            
            the forces are multiplied by the distance to maintain the ratio
            between x and y accurate
            
            they are also multiplied by either 1 or -1 depending on the 
            direction that the planet is pulling on
             */
            forceX += (planet.mass * satellite.mass * Math.abs(planet.x - satellite.posX) / Math.pow(distance, 2))
                    * (planet.x > satellite.posX ? 1 : -1);
            forceY += (planet.mass * satellite.mass * Math.abs(planet.y - satellite.posY) / Math.pow(distance, 2))
                    * (planet.y > satellite.posY ? 1 : -1);

            // Ff satellite intersects with a planet, crash and show crash label
            if (satellite != null && satellite.circle.getBoundsInParent().intersects(planet.circle.getBoundsInParent())) {
                // Update elapsed time to preserve time up to collision
                elapsedTime += System.currentTimeMillis() - startTime;

                // Make satellite disappear, stop simulation
                newSatelliteButton.setDisable(false);
                launchButton.setDisable(true);
                running = false;
                // Disable reset all button if planets is empty
                resetAllButton.setDisable(planets.isEmpty());
                satellite.circle.setVisible(true);
                crashLabel.setVisible(true);
                stopPlayButton.setDisable(true);
                speedSlider.setDisable(true);
                pauseTrans.play();
                break;
            }
        }

        // Make satellite invisible if its position is less than 0
        if (satellite.posX < 0 || satellite.posY < 0) {
            satellite.circle.setVisible(false);
        } else {
            satellite.circle.setVisible(true);
        }

        // Ipdate satellite's and transition's fields
        satellite.circle.toFront();
        satellite.accX = forceX / 800;
        satellite.accY = forceY / 800;
        satellite.changeVelocity();
        satellite.changePosition();
        transition.setNode(satellite.circle);
        transition.setByX(satellite.velX);
        transition.setByY(satellite.velY);

        // Update labels
        updateLabels();

        // Play transition
        transition.play();
    }

    /**
     * Updates the time, velocity, and acceleration labels
     */
    private void updateLabels() {
        // Calculate current elapsed time
        long currentElapsed = elapsedTime;
        if (running) {
            currentElapsed += System.currentTimeMillis() - startTime;
        }

        // Displays time on top bar as minutes:seconds:milliseconds
        long totalSeconds = currentElapsed / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        long milliseconds = (currentElapsed % 1000) / 10;  // Two-digit milliseconds (00-99)
        timeLabel.setText(String.format("%02d:%02d:%02d", minutes, seconds, milliseconds));

        // Calculate velocity magnitude
        if (satellite != null) {
            double velocity = Math.hypot(satellite.velX, satellite.velY);
            velocityLabel.setText(String.format("Velocity: %.2f AU/s", velocity));

            double acceleration = Math.hypot(satellite.accX, satellite.accY);
            accelerationLabel.setText(String.format("Acceleration: %.2f AU/s²", acceleration));
        } else {
            velocityLabel.setText("Velocity: 00.00 AU/s");
            accelerationLabel.setText("Acceleration: 00.00 AU/s²");
        }
    }

    /**
     * Checks to see if a new planet overlaps with another planet or satellite
     *
     * @param x x position of new planet
     * @param y y position of new planet
     * @param radius radius of new planet
     * @return true if the planet overlaps
     */
    public boolean isOverlapping(double x, double y, double radius) {
        for (Planet p : planets) {
            // Determine distance between planets
            double dist = Math.hypot(p.x - x, p.y - y);
            if (dist < p.radius + radius) {
                return true; // The planets are overlapping
            }
        }

        if (satellite != null) {
            // Determine distance between the satellite and planet
            double dist = Math.hypot(satellite.posX - x, satellite.posY - y);
            if (dist < satellite.circle.getRadius() + radius) {
                return true; // The objects overlap
            }
        }

        return false; // The planets/satellite don't overlap
    }
}
