package com.hbjdjd.ca2.Algorithms;

import com.hbjdjd.ca2.Map.*;

import java.util.ArrayList;
import java.util.LinkedList;

public class DFS implements PathFindAlgo {

    @Override
    public Path computePath(MapOverlay mapOverlay, Node start, Node end) {
        LinkedList<Path> paths = computePaths(mapOverlay, start, end);
        Path cheapest = new Path();
        cheapest.setCost(Integer.MAX_VALUE);
        for (Path p : paths) {
            if (p.getCost() < cheapest.getCost()) {
                cheapest = p;
            }
        }
        return cheapest;
    }


    @Override
    public LinkedList<Path> computePaths(MapOverlay mapOverlay, Node start, Node end) {
        return findPathsDFS(mapOverlay, start, end, null);
    }

    public LinkedList<Path> findPathsDFS(MapOverlay mapOverlay, Node start, Node end, ArrayList<Node> encountered) {
        LinkedList<Path> result = new LinkedList<>(), result2;

        if (start.equals(end)) {
            result = new LinkedList<>();
            Path p = new Path();
            p.addNode(start);
            result.add(p);
            return result;
        }
        if (encountered == null) {
            encountered = new ArrayList<>();
        }
        encountered.add(start);

        for (Edge edge : start.getEdges()) {
            if (!encountered.contains(edge.getDestination())) {
                result2 = findPathsDFS(mapOverlay, edge.getDestination(), end, new ArrayList<>(encountered));
                if (result2 != null) {
                    for (Path a : result2) {
                        a.addToFront(start);
                        a.setCost(edge.getCost());
                    }
                    result.addAll(result2);
                }
            }
        }
        return result;
    }


}
