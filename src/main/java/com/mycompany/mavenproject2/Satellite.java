package com.mycompany.mavenproject2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author David Hernandez, James Ha, Astghik Minasyan
 */
public class Satellite {
    protected double initialX; // Initial x position
    protected double initialY; // Initial y postion
    protected double posX; // Current x position
    protected double posY; // Current y position
    protected double velX; // Current x velocity
    protected double velY; // Current y velocity
    protected double accX; // Current x acceleration
    protected double accY; // Current y acceleration
    protected Circle circle; // Circle that's shown on scene
    protected final double mass = Math.pow(RADIUS, 3); // Mass of the satellite
    
    protected final static double RADIUS = 4; // Radius of the satellite
    
    /**
     * Constructor
     * @param x the initial x position
     * @param y the initial y position
     * @param colour the colour of the satellite
     */
    public Satellite(double x, double y, Color colour) {
        this.initialX = x; 
        this.initialY = y; 
        this.posX = x; 
        this.posY = y; 
        this.velX = 0; 
        this.velY = 0; 
        this.accX = 0; 
        this.accY = 0; 
        this.circle = new Circle(x, y, RADIUS, colour); 
        this.circle.setId("satellite");
        this.circle.setTranslateX(x);
        this.circle.setTranslateY(y);
    }
    
    /**
     * Update velocity based on acceleration every iteration of the animation
     */
    public void changeVelocity() {
        velX += accX;
        velY += accY;
    }
    
    /**
     * Update position based on velocity every iteration of the animation
     */
    public void changePosition() {
        posX += velX;
        posY += velY;
    }

    /**
     * Getter for the initial x position
     * @return the initial x position
     */
    public double getInitialX() {
        return initialX;
    }

    /**
     * Getter for the initial y position
     * @return the initial y position
     */
    public double getInitialY() {
        return initialY;
    }
    
    /**
     * Getter for the current x position
     * @return the current x position
     */
    public double getPosX() {
        return posX;
    }

    /**
     * Setter for the current x position
     * @param posX the new x position
     */
    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * Getter for the current y position
     * @return the current y position
     */
    public double getPosY() {
        return posY;
    }

    /**
     * Setter for the current y position
     * @param posY the new y position
     */
    public void setPosY(double posY) {
        this.posY = posY;
    }

    /**
     * Getter for the current x velocity
     * @return the current x velocity
     */
    public double getVelX() {
        return velX;
    }

    /**
     * Setter for the current x velocity
     * @param velX the new x velocity
     */
    public void setVelX(double velX) {
        this.velX = velX;
    }

    /**
     * Getter for the current y velocity
     * @return the current y velocity
     */
    public double getVelY() {
        return velY;
    }

    /**
     * Setter for the current y velocity
     * @param velY the new y velocity
     */
    public void setVelY(double velY) {
        this.velY = velY;
    }

    /**
     * Getter for the current x acceleration
     * @return the current x acceleration
     */
    public double getAccX() {
        return accX;
    }

    /**
     * Setter for the current x acceleration
     * @param accX the new x acceleration
     */
    public void setAccX(double accX) {
        this.accX = accX;
    }

    /**
     * Getter for the current y acceleration
     * @return the current y acceleration
     */
    public double getAccY() {
        return accY;
    }

    /**
     * Setter for the current y acceleration
     * @param accY the new y acceleration
     */
    public void setAccY(double accY) {
        this.accY = accY;
    }
}
