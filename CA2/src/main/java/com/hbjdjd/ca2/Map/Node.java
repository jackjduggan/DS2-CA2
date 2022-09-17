package com.hbjdjd.ca2.Map;

import com.hbjdjd.ca2.Controllers.MainController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.util.LinkedList;
import java.util.Random;

public class Node {

    private final String name;
    private final int x, y;
    private final double radius = 10;
    private final int id;
    private final double colorR, colorG, colorB; //Stored this instead of a Color object in order to save the object instance.
    private int interimCost = Integer.MAX_VALUE;
    private LinkedList<Edge> edges = new LinkedList<>();

    public Node(String name, int id, int x, int y) {
        Random r = new Random();
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
        colorR = r.nextDouble();
        colorG = r.nextDouble();
        colorB = r.nextDouble();
    }

    public boolean isNeighbour(Node p) {
        if (p == null) {
            return false;
        }
        return p.posEquals(x, y - 1)
                || p.posEquals(x - 1, y)
                || p.posEquals(x + 1, y)
                || p.posEquals(x, y + 1);
    }

    public boolean posEquals(int x, int y) {
        return x == this.x && y == this.y;
    }

    public LinkedList<Edge> getEdges() {
        return edges;
    }

    public String getName() {
        return name;
    }


    public int getInterimCost() {
        return interimCost;
    }

    public void setInterimCost(int interimCost) {
        this.interimCost = interimCost;
    }

    @Override
    public String toString() {
        return name;
    }

    public String display(MapOverlay mapOverlay) {
        String s = "Node: " + name + " {";
        for (Edge e : getEdges()) {
            s = s.concat("\n\tEdge: " + e);
        }
        s = s.concat("\n}");
        return s;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNodeID() {
        return id;
    }

    public void resetColor() {
        getCircle().setFill(getColor());
    }

    public Color getColor() {
        return Color.color(colorR, colorG, colorB);
    }

    public Circle getCircle() {
        for (javafx.scene.Node n : MainController.instance.imagePane.getChildren()) {
            if (n instanceof Circle) {
                Circle c = (Circle) n;
                if (nearby(c.getCenterX(), c.getCenterY()) && c.getId().equals(name)) {
                    return c;
                }
            }
        }
        throw new IllegalStateException("Circle not found");
    }

    public boolean nearby(double a, double b) {
        return a >= x - 5 && x <= x + 5 && b >= y - 5 && b <= y + 5;
    }

    public Circle createCircle() {
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(radius);
        circle.setId(name);
        circle.setFill(getColor());
        return circle;
    }

    public Label createLabel() {
        Label label = new Label();
        label.setLayoutX(x - 5);
        label.setLayoutY(y - 7);
        label.setId(name);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setText(String.valueOf(id));
        label.setTextFill(getColor().invert().saturate().brighter());
        return label;
    }

    public Label getLabel() {
        for (javafx.scene.Node n : MainController.instance.imagePane.getChildren()) {
            if (n instanceof Label) {
                Label l = (Label) n;
                if (l.getLayoutX() == x - 5 && l.getLayoutY() == y - 7 && l.getId().equals(name)) {
                    return l;
                }
            }
        }
        throw new IllegalStateException("Label not found");
    }

    public void hide() {
        getCircle().setVisible(false);
        getLabel().setVisible(false);
    }

    public double getRadius() {
        return radius;
    }

    public void setEdges(LinkedList<Edge> edges) {
        this.edges = edges;
    }

    public void show() {
        getCircle().setVisible(true);
        getLabel().setVisible(true);
    }
}
