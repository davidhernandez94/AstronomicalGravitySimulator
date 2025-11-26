/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author 6309110
 */
public class Satellite {
    protected double posX;
    protected double posY;
    protected double velX;
    protected double velY;
    protected double accX;
    protected double accY;
    protected Circle circle;
    protected final double mass = Math.pow(RADIUS, 2);
    
    protected final static double RADIUS = 15;
    
    public Satellite(double x, double y, Color colour) {
        this.posX = x;
        this.posY = y;
        this.velX = 0;
        this.velY = 0;
        this.accX = 0;
        this.accY = 0;
        this.circle = new Circle(x, y, RADIUS, colour);
        this.circle.setId("satellite");
    }
    
    /**
     * update velocity based on acceleration every iteration of the animation
     */
    public void changeVelocity() {
        velX += accX;
        velY += accY;
    }
    
    /**
     * update position based on velocity every iteration of the animation
     */
    public void changePosition() {
        posX += velX;
        posY += velY;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getAccX() {
        return accX;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }
    
    
    
}
