package com.hbjdjd.ca2.Algorithms;

import com.hbjdjd.ca2.Map.*;
import com.hbjdjd.ca2.Util;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class BFS {

    private Image bwImage;
    private MapOverlay mapOverlay;
    private Image originalImage;
    private HashMap<Node, Boolean> encounteredNodes = new HashMap<>();
    private HashMap<Edge, Boolean> encounteredEdges = new HashMap<>();

    public BFS(MapOverlay mapOverlay, Image originalImage) {
        this.mapOverlay = mapOverlay;
        this.originalImage = originalImage;
        this.bwImage = convert();
    }

    private Image convert() {
        PixelReader pR = originalImage.getPixelReader();
        WritableImage bWImage = new WritableImage(pR, (int) originalImage.getWidth(), (int) originalImage.getHeight());
        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                if (!pR.getColor(x, y).equals(Color.BLACK)) {
                    bWImage.getPixelWriter().setColor(x, y, Color.WHITE);
                }
            }
        }
        return bWImage;
    }

    public Image getBWImage() {
        return this.bwImage;
    }

    public void computePath(Node[] clickedNodes, ImageView imageView, Label costLabel) {
        AtomicBoolean failed = new AtomicBoolean(false);
        AtomicBoolean success = new AtomicBoolean(false);
        new Thread(() -> {
            Node startNode = clickedNodes[0];
            Node endNode = clickedNodes[1];
            PixelReader pR = bwImage.getPixelReader();
            int width = (int) bwImage.getWidth();
            int height = (int) bwImage.getHeight();
            WritableImage i = new WritableImage(pR, width, height);
            imageView.setImage(i);
            Node[][] nodes = new Node[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (pR.getColor(x, y).equals(Color.WHITE)) {
                        Node n = new Node(x + " , " + y, -1, x, y);
                        if (x == startNode.getX() && y == startNode.getY()) {
                            n = startNode;
                        } else if (x == endNode.getX() && y == endNode.getY()) {
                            n = endNode;
                        }
                        n.setEdges(new LinkedList<>());
                        if (nodes[x][y] == null) {
                            nodes[x][y] = n;
                        } else {
                            throw new IllegalStateException("Node was being replaced.");
                        }
                    }
                }
            }
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Node n1 = nodes[x][y];
                    if (n1 == null) {
                        continue;
                    }
                    for (int a = x - 1; a <= x + 1; a++) {
                        for (int b = y - 1; b <= y + 1; b++) {
                            if (!Util.withinBoundsForMatrix(a, b, width, height)) {
                                continue;
                            }
                            Node n2 = nodes[a][b];
                            if (n2 == null || n2 == n1 || !n1.isNeighbour(n2)) {
                                continue;
                            }
                            Edge e1 = new Edge(1, n1, n2);
                            n1.getEdges().add(e1);
                        }
                    }
                }
            }
            try {
                Path path = findPathBreadthFirst(mapOverlay, startNode, endNode);
                Platform.runLater(()-> costLabel.setText(path.getCostString()));
                for (Node n : path.getRoute()) {
                    Platform.runLater(() -> i.getPixelWriter().setColor(n.getX(), n.getY(), Color.RED));
                }
                success.set(true);
            } catch (NullPointerException e) {
                failed.set(true);
            }
        }).start();
        Platform.runLater(() -> getStatus(success, failed));
    }

    private void getStatus(AtomicBoolean success, AtomicBoolean failed) {
        if (failed.get()) {
            Platform.runLater(()-> Util.messageAlert("Error", "No path found"));
        } else if (!success.get()) {
            Platform.runLater(() -> getStatus(success, failed));
        }
    }

    public Path findPathBreadthFirst(MapOverlay mapOverlay, Node startNode, Node end) {
        encounteredNodes = new HashMap<>();
        encounteredEdges = new HashMap<>();
        List<Path> agenda = new ArrayList<>();
        Path firstAgendaPath = new Path();
        firstAgendaPath.addToFront(startNode);
        agenda.add(firstAgendaPath);
        Path resultPath = findPathBreadthFirst(mapOverlay, agenda, end);
        Collections.reverse(resultPath.getRoute());
        return resultPath;
    }

    public Path findPathBreadthFirst(MapOverlay mapOverlay, List<Path> agenda, Node end) {
        if (agenda.isEmpty()) {
            return null;
        }
        Path nextPath = agenda.remove(0);
        Node currentNode = nextPath.get(0);
        if (currentNode.equals(end)) {
            return nextPath;
        }
        encounteredNodes.put(currentNode, true);
        for (Edge edge : currentNode.getEdges()) {
            if (encounteredEdges.get(edge) != null) {
                if (encounteredEdges.get(edge)) {
                    continue;
                }
            }
            encounteredEdges.put(edge, true);
            if (encounteredNodes.get(edge.getDestination()) == null) {
                Path newPath = new Path(nextPath);
                newPath.addToFront(edge.getDestination());
                agenda.add(newPath);
            }
        }
        return findPathBreadthFirst(mapOverlay, agenda, end);
    }
}
