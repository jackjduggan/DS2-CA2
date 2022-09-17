package com.hbjdjd.ca2.Map;

import com.hbjdjd.ca2.Algorithms.BFS;
import com.hbjdjd.ca2.Algorithms.DFS;
import com.hbjdjd.ca2.Algorithms.Djikstra;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PathFinder {

    private BFS bfs;
    private Label costLabel;

    public PathFinder(Label costLabel){
        this.costLabel = costLabel;
    }

    public void initialiseBFS(MapOverlay mapOverlay, Image image) {
        bfs = new BFS(mapOverlay, image);
    }

    public BFS getBFS() {
        return this.bfs;
    }

    public void findPathBFS(Node[] clickedNodes, ImageView i) {
        bfs.computePath(clickedNodes, i, costLabel);
    }

    public void findPathDFS(MapOverlay mapOverlay, Node[] clickedNodes, Node n, Circle c) {
        if (clickedNodes[0] != null && clickedNodes[1] != null) {
            Path p = new DFS().computePath(mapOverlay, clickedNodes[0], clickedNodes[1]);
            clickedNodes[0] = null;
            clickedNodes[1] = null;
            displayPath(mapOverlay, p);
        } else if (clickedNodes[0] == null) {
            clickedNodes[0] = n;
            c.setFill(Color.RED);
        } else {
            clickedNodes[1] = n;
            c.setFill(Color.RED);
            findPathDjikstra(mapOverlay, clickedNodes, n, c);
        }
    }

    public void findPathDjikstra(MapOverlay mapOverlay, Node[] clickedNodes, Node n, Circle c) {
        if (clickedNodes[0] != null && clickedNodes[1] != null) {
            Path p = new Djikstra().computePath(mapOverlay, clickedNodes[0], clickedNodes[1]);
            clickedNodes[0].resetColor();
            clickedNodes[1].resetColor();
            clickedNodes[0] = null;
            clickedNodes[1] = null;
            displayPath(mapOverlay, p);
        } else if (clickedNodes[0] == null) {
            clickedNodes[0] = n;
            c.setFill(Color.RED);
        } else {
            clickedNodes[1] = n;
            c.setFill(Color.RED);
            findPathDjikstra(mapOverlay, clickedNodes, n, c);
        }
    }

    private void displayPath(MapOverlay mapOverlay, Path path) {
        resetNodes(mapOverlay);
        costLabel.setText(path.getCostString());
        for (Node node : path.getRoute()) {
            node.getCircle().setFill(Color.RED);
            for (Edge edge : node.getEdges()) {
                if (path.getRoute().contains(edge.getDestination()) && path.getRoute().contains(edge.getSource())) {
                    edge.getLine().setStroke(Color.RED);
                }
            }
        }
    }

    private void resetNodes(MapOverlay mapOverlay) {
        for (Node node : mapOverlay.getNodes()) {
            node.resetColor();
            for (Edge edge : node.getEdges()) {
                edge.resetColor();
            }
        }
    }


}
