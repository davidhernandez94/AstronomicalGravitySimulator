package com.mycompany.mavenproject2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author David Hernandez, James Ha, Astghik Minasyan
 */
public class Planet {
    protected double x; // x position
    protected double y; // y position
    protected double radius; // radius of circle
    protected Color colour; // colour of circle
    protected Circle circle; // circle that's shown on scene
    protected double mass; // mass of planet
    
    /**
     * Constructor
     * @param x position x
     * @param y position y
     * @param radius radius of the circle
     * @param colour colour of the circle
     */
    public Planet(double x, double y, double radius, Color colour) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.colour = colour;
        this.circle = new Circle(x, y, radius, colour);
        circle.setTranslateX(x);
        circle.setTranslateY(y);
        this.mass = Math.pow(radius, 3);
    }

    /**
     * Getter for the x position
     * @return the x position
     */
    public Double getX() {
        return x;
    }

    /**
     * Setter for the x position
     * @param x the new x position
     */
    public void setX(Double x) {
        this.x = x;
    }

    /**
     * Getter for the y position
     * @return the y position
     */
    public Double getY() {
        return y;
    }

    /**
     * Setter for the y position
     * @param y the new y position
     */
    public void setY(Double y) {
        this.y = y;
    }

    /**
     * Getter for the radius
     * @return the radius
     */
    public Double getRadius() {
        return radius;
    }

    /**
     * Setter for the radius
     * @param radius the new radius
     */
    public void setRadius(Double radius) {
        this.radius = radius;
    }

    /**
     * Getter for the colour
     * @return the colour
     */
    public Color getColour() {
        return colour;
    }

    /**
     * Setter for the colour
     * @param colour the new colout
     */
    public void setColour(Color colour) {
        this.colour = colour;
    }
}
