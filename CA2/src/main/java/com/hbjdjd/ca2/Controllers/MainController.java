package com.hbjdjd.ca2.Controllers;

import com.hbjdjd.ca2.Map.*;
import com.hbjdjd.ca2.Storage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.File;
import java.io.IOException;

public class MainController {


    public ImageView imageView;
    public TextField nodeNameTextField;
    public double xCor, yCor;
    public Spinner<Integer> idSpinner;
    public Pane imagePane;
    public RadioButton addNodeRadio;
    public RadioButton addEdgeRadio;
    public RadioButton findPathDjikstraRadio;
    public RadioButton findPathDFSRadio;
    public RadioButton findPathBFSRadio;
    public Tab pathTab;
    public Tab setupTab;
    public RadioButton removeNodeRadio;
    public RadioButton removeEdgeRadio;
    public Label costLabel;
    private Node[] clickedNodes;
    private Node[] clickedNodes2;
    public static MainController instance;
    public PathFinder pathFinder;
    private MapOverlay mapOverlay;
    private Image originalImage;

    public void initialize() {
        instance = this;
        mapOverlay = new MapOverlay(imageView.getImage().getUrl());
        idSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        nodeNameTextField.setText("Room: 1");
        clickedNodes = new Node[2];
        clickedNodes2 = new Node[2];
        updateMode();
        pathFinder = new PathFinder(costLabel);
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleGroup toggleGroup2 = new ToggleGroup();
        addNodeRadio.setToggleGroup(toggleGroup);
        addEdgeRadio.setToggleGroup(toggleGroup);
        removeEdgeRadio.setToggleGroup(toggleGroup);
        removeNodeRadio.setToggleGroup(toggleGroup);
        findPathDjikstraRadio.setToggleGroup(toggleGroup2);
        findPathBFSRadio.setToggleGroup(toggleGroup2);
        findPathDFSRadio.setToggleGroup(toggleGroup2);
        originalImage = imageView.getImage();
    }

    public void nodeClicked(Node n, Circle c) {
        if (addEdgeRadio.isSelected() && setupTab.isSelected()) {
            addEdge(n, c);
        } else if (findPathDjikstraRadio.isSelected() && !setupTab.isSelected()) {
            pathFinder.findPathDjikstra(mapOverlay, clickedNodes, n, c);
        } else if (findPathDFSRadio.isSelected() && !setupTab.isSelected()) {
            pathFinder.findPathDFS(mapOverlay, clickedNodes, n, c);

        } else if (removeNodeRadio.isSelected() && setupTab.isSelected()) {
            removeNode(n, c);
        } else if (removeEdgeRadio.isSelected() && setupTab.isSelected()) {
            removeEdge(n, c);
        }
    }

    private void removeEdge(Node n, Circle c) {
        if (clickedNodes[0] == n) {
            clickedNodes[0].resetColor();
            clickedNodes[1].resetColor();
            clickedNodes[0] = null;
            clickedNodes[1] = null;
            return;
        }
        if (clickedNodes[0] == null) {
            clickedNodes[0] = n;
            c.setFill(Color.RED);
        } else if (clickedNodes[1] == null) {
            clickedNodes[1] = n;
            mapOverlay.removeEdge(clickedNodes);
            clickedNodes[0].resetColor();
            clickedNodes[1].resetColor();
        } else {
            clickedNodes[0] = null;
            clickedNodes[1] = null;
            nodeClicked(n, c);
        }
    }

    private void removeNode(Node n, Circle c) {
        imagePane.getChildren().remove(c);
        imagePane.getChildren().remove(n.getLabel());
        mapOverlay.getNodes().remove(n);
    }

    private void addEdge(Node n, Circle c) {
        if (clickedNodes[0] != null && clickedNodes[1] != null) {
            mapOverlay.addEdge(clickedNodes[0], clickedNodes[1]);
            clickedNodes[0].resetColor();
            clickedNodes[1].resetColor();
            clickedNodes[0] = null;
            clickedNodes[1] = null;
        } else if (clickedNodes[0] == null) {
            clickedNodes[0] = n;
            c.setFill(Color.RED);
        } else {
            clickedNodes[1] = n;
            c.setFill(Color.RED);
            addEdge(n, c);
        }
    }

    public void imageClicked(MouseEvent mouseEvent) {
        xCor = mouseEvent.getX();
        yCor = mouseEvent.getY();
        if (addNodeRadio.isSelected() && setupTab.isSelected()) {
            Node n = new Node(nodeNameTextField.getText(), idSpinner.getValue(), (int) xCor, (int) yCor);
            mapOverlay.addNode(n);
            Circle c = n.getCircle();
            Label l = n.getLabel();
            c.setOnMouseClicked(e -> nodeClicked(n, c));
            l.setOnMouseClicked(e -> nodeClicked(n, c));
            idSpinner.increment();
            nodeNameTextField.setText("Room: " + idSpinner.getValue());
        } else {
            if (pathTab.isSelected() && findPathBFSRadio.isSelected()) {
                Node n = new Node(nodeNameTextField.getText(), idSpinner.getValue(), (int) xCor, (int) yCor);
                Circle c = n.createCircle();
                if (clickedNodes2[0] == null) {
                    clickedNodes2[0] = n;
                    imagePane.getChildren().add(c);
                } else if (clickedNodes2[1] == null) {
                    clickedNodes2[1] = n;
                    imagePane.getChildren().add(c);
                } else {
                    imagePane.getChildren().remove(clickedNodes2[0].getCircle());
                    imagePane.getChildren().remove(clickedNodes2[1].getCircle());
                    clickedNodes2[0] = null;
                    clickedNodes2[1] = null;
                    pathFinder.initialiseBFS(mapOverlay, imageView.getImage());
                    imageView.setImage(pathFinder.getBFS().getBWImage());
                }
                if (clickedNodes2[0] != null && clickedNodes2[1] != null) {
                    Platform.runLater(() -> pathFinder.findPathBFS(clickedNodes2, imageView));
                }
                idSpinner.increment();
                nodeNameTextField.setText("Room: " + idSpinner.getValue());
            }
        }
    }

    public void save(ActionEvent actionEvent) {
        try {
            File file = new File(System.getProperty("user.dir") + "/saves/save.xml");
            assert (file.createNewFile());
            Storage.writeObject(file, mapOverlay);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(ActionEvent actionEvent) {
        File file = new File(System.getProperty("user.dir") + "/saves/save.xml");
        try {
            mapOverlay = (MapOverlay) Storage.readObject(file);
            imagePane.getChildren().removeIf(n -> n instanceof Circle || n instanceof Line || n instanceof Label);
            mapOverlay.generateGraphics();
            for (Node node : mapOverlay.getNodes()) {
                Circle c = node.getCircle();
                Label l = node.getLabel();
                c.setOnMouseClicked(e -> nodeClicked(node, c));
                l.setOnMouseClicked(e -> nodeClicked(node, c));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void modeRadio(Event event) {
        Platform.runLater(this::updateMode);
    }

    private void updateMode() {
        if (clickedNodes2[0] != null) {
            Node a = clickedNodes2[0];
            imagePane.getChildren().remove(a.getCircle());
            clickedNodes2[0] = null;
        }
        if (clickedNodes2[1] != null) {
            Node a = clickedNodes2[1];
            imagePane.getChildren().remove(a.getCircle());
            clickedNodes2[1] = null;
        }
        if (originalImage != null) {
            imageView.setImage(originalImage);
        }
        if (setupTab.isSelected()) {
            mapOverlay.showAll();
            if (addNodeRadio.isSelected()) {
                nodeNameTextField.setVisible(true);
                idSpinner.setVisible(true);
            } else {
                nodeNameTextField.setVisible(false);
                idSpinner.setVisible(false);
            }
        } else {
            nodeNameTextField.setVisible(false);
            idSpinner.setVisible(false);
            if (findPathBFSRadio.isSelected()) {
                mapOverlay.hideAll();
                pathFinder.initialiseBFS(mapOverlay, imageView.getImage());
                imageView.setImage(pathFinder.getBFS().getBWImage());
            } else {
                mapOverlay.showAll();
            }
        }
    }
}