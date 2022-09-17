package com.hbjdjd.ca2.Map;

import com.hbjdjd.ca2.Controllers.MainController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Edge {

    private final int cost;
    private final Node source, destination;

    public Edge(int cost, Node source, Node destination){
        if(source == null || destination == null){
            throw new IllegalStateException("Cannot have a null source or destination");
        }
        this.cost = cost;
        this.source = source;
        this.destination = destination;
    }

    public Line createLine(){
        Line l = new Line();
        l.setStartX(source.getX());
        l.setStartY(source.getY());
        l.setEndX(destination.getX());
        l.setEndY(destination.getY());
        return l;
    }

    public Line getLine(){
        for(javafx.scene.Node n : MainController.instance.imagePane.getChildren()){
            if(n instanceof Line){
                Line l = (Line) n;
                if(l.getStartX() == source.getX() && l.getStartY() == source.getY() && l.getEndX() == destination.getX() && l.getEndY() == destination.getY()){
                    return l;
                }
            }
        }
        throw new IllegalStateException("Line not found");
    }

    public int getCost() {
        return cost;
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public void resetColor() {
        getLine().setStroke(Color.BLACK);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "cost=" + cost +
                ", source=" + source.getName() +
                ", destination=" + destination.getName() +
                '}';
    }

    public void hide() {
        getLine().setVisible(false);
    }

    public void show() {
        getLine().setVisible(true);
    }
}
