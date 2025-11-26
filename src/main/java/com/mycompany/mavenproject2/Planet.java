/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author David Hernandez
 */
public class Planet {
    protected double x;
    protected double y;
    protected double radius;
    protected Color colour;
    protected Circle circle;
    protected double mass;
    

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
