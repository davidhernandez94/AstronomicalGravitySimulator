/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }
}
