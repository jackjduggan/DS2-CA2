package com.hbjdjd.ca2.Map;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

import static com.hbjdjd.ca2.Controllers.MainController.instance;

public class MapOverlay {

    private final ArrayList<Node> nodes;
    private final String imageURL;

    public MapOverlay(String imageURL) {
        nodes = new ArrayList<>();
        this.imageURL = imageURL;
    }

    public Image createImage() {
        return new Image(imageURL);
    }

    public Node getNode(int id) {
        for (Node n : nodes) {
            for (Edge e : n.getEdges()) {
                if (n.getNodeID() == id) {
                    return n;
                }
            }
        }
        return null;
    }

    public void addNode(Node node) {
        javafx.scene.Node n1 = node.createCircle();
        javafx.scene.Node n2 = node.createLabel();
        instance.imagePane.getChildren().add(n1);
        instance.imagePane.getChildren().add(n2);
        nodes.add(node);
    }

    public boolean isNeighbour(Node a, Node b) {
        if (a == null || b == null) {
            return false;
        }
        LinkedList<Edge> x = a.getEdges();
        for (Edge e : x) {
            if ((e.getSource() == a && e.getDestination() == b) || (e.getSource() == b && e.getDestination() == a)) {
                return true;
            }
        }
        return false;
    }

    public void addEdge(Node a, Node b) {
        LinkedList<Edge> edges1 = a.getEdges();
        LinkedList<Edge> edges2 = b.getEdges();
        Edge e = new Edge(getDistance(a, b), a, b);
        Edge f = new Edge(getDistance(a, b), b, a);
        edges1.add(e);
        edges2.add(f);
        instance.imagePane.getChildren().add(e.createLine());
        instance.imagePane.getChildren().add(f.createLine());
        System.out.println("Added edge: " + a.getName() + " -> " + b.getName());
    }

    public static int getDistance(Node a, Node b) {
        return (int) Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void generateGraphics() {
        for (Node node : nodes) {
            instance.imagePane.getChildren().add(node.createCircle());
            instance.imagePane.getChildren().add(node.createLabel());
            for (Edge edge : node.getEdges()) {
                instance.imagePane.getChildren().add(edge.createLine());
            }
        }
    }

    public ArrayList<Edge> getEdges() {
        ArrayList<Edge> edges = new ArrayList<>();
        for (Node n : nodes) {
            edges.addAll(n.getEdges());
        }
        return edges;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void showAll() {
        for (Node n : nodes) {
            n.getCircle().setVisible(true);
            n.getLabel().setVisible(true);
            for (Edge e : n.getEdges()) {
                e.getLine().setVisible(true);
            }
        }
    }

    public void hideAll() {
        for (Node n : nodes) {
            n.getCircle().setVisible(false);
            n.getLabel().setVisible(false);
            for (Edge e : n.getEdges()) {
                e.getLine().setVisible(false);
            }
        }
    }

    public void removeEdge(Node[] clickedNodes) {
        Node a = clickedNodes[0];
        Node b = clickedNodes[1];
        LinkedList<Edge> e = a.getEdges();
        LinkedList<Edge> f = b.getEdges();
        LinkedList<Edge> remove = new LinkedList<>();
        for (Edge g : e) {
            if (g.getDestination().equals(b)) {
                remove.add(g);
            }
        }
        for (Edge g : f) {
            if (g.getDestination().equals(a)) {
                remove.add(g);
            }
        }
        e.removeAll(remove);
        f.removeAll(remove);
        for (Edge h : remove) {
            instance.imagePane.getChildren().remove(h.getLine());
        }
    }
}
