package com.mycompany.mavenproject2;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import javafx.scene.paint.Color;

public class MainFXMLControllerTest {

    private MainFXMLController controller;

    @BeforeEach
    public void setUp() {
        controller = new MainFXMLController();
        controller.planets = new ArrayList<>();
        controller.satellite = null;
    }

    /**
     * Checks if planets are correctly created with proper characteristics
     */
    @Test
    public void testPlanetCreation() {
        Planet planet = new Planet(100, 200, 15, Color.RED);
        assertEquals(100, planet.getX());
        assertEquals(200, planet.getY());
        assertEquals(15, planet.getRadius());
        assertEquals(Color.RED, planet.getColour());
        assertEquals(Math.pow(15, 3), planet.mass);
    }

    /**
     * Checks if planet setters work
     */
    @Test
    public void testPlanetSetters() {
        Planet planet = new Planet(0, 0, 10, Color.BLUE);
        planet.setX(50.0);
        planet.setY(75.0);
        planet.setRadius(20.0);
        planet.setColour(Color.GREEN);
        assertEquals(50, planet.getX());
        assertEquals(75, planet.getY());
        assertEquals(20, planet.getRadius());
        assertEquals(Color.GREEN, planet.getColour());
    }

    /**
     * Checks if satellite creation works and if the properties align
     */
    @Test
    public void testSatelliteCreation() {
        Satellite satellite = new Satellite(300, 400, Color.YELLOW);
        assertEquals(300, satellite.getInitialX());
        assertEquals(400, satellite.getInitialY());
        assertEquals(300, satellite.getPosX());
        assertEquals(400, satellite.getPosY());
        assertEquals(0, satellite.getVelX());
        assertEquals(0, satellite.getVelY());
        assertEquals(0, satellite.getAccX());
        assertEquals(0, satellite.getAccY());
        assertEquals(Math.pow(Satellite.RADIUS, 3), satellite.mass);
    }

    /**
     * Checks if satellite changes velocity as expected
     */
    @Test
    public void testSatelliteChangeVelocity() {
        Satellite satellite = new Satellite(0, 0, Color.BLUE);
        satellite.setAccX(2.0);
        satellite.setAccY(3.0);
        satellite.changeVelocity();
        assertEquals(2.0, satellite.getVelX());
        assertEquals(3.0, satellite.getVelY());
    }

    /**
     * Checks if satellite changes position as expected
     */
    @Test
    public void testSatelliteChangePosition() {
        Satellite satellite = new Satellite(0, 0, Color.BLUE);
        satellite.setVelX(5.0);
        satellite.setVelY(7.0);
        satellite.changePosition();
        assertEquals(5.0, satellite.getPosX());
        assertEquals(7.0, satellite.getPosY());
    }

    /**
     * Checks if planets are marked as non-overlapping when they shouldn't be
     */
    @Test
    public void testIsOverlapping_NoOverlap() {
        // Adds a planet
        Planet planet = new Planet(100, 100, 10, Color.RED);
        controller.planets.add(planet);

        // Checks a point far away from both
        boolean overlap = controller.isOverlapping(200, 200, 5);
        assertFalse(overlap);
    }

    /**
     * Checks if planets are marked as overlapping when they do
     */
    @Test
    public void testIsOverlapping_WithPlanetOverlap() {
        // Adds a planet
        Planet planet = new Planet(100, 100, 10, Color.RED);
        controller.planets.add(planet);

        // Checks a point that overlaps
        boolean overlap = controller.isOverlapping(105, 105, 10);
        assertTrue(overlap);
    }

    /**
     * Checks if satellite and planet are marked as overlapping when they do
     */
    @Test
    public void testIsOverlapping_WithSatelliteOverlap() {
        // Adds a satellite
        Satellite satellite = new Satellite(200, 200, Color.BLUE);
        controller.satellite = satellite;

        // Checks a point that overlaps
        boolean overlap = controller.isOverlapping(205, 205, 5);
        assertTrue(overlap);
    }

    /**
     * Checks if satellite and planet are marked as overlapping when there is no satellite
     */
    @Test
    public void testIsOverlapping_NoSatellite() {
        // No satellite, checks with planet
        Planet planet = new Planet(100, 100, 10, Color.RED);
        controller.planets.add(planet);

        boolean overlap = controller.isOverlapping(200, 200, 5);
        assertFalse(overlap);
    }
}
