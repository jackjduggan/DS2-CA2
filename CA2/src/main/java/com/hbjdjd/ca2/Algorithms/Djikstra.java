package com.hbjdjd.ca2.Algorithms;

import com.hbjdjd.ca2.Map.*;

import java.util.*;
//GraphNodeDw == Node
//Node value = getInterimCost
//GraphLinkDw == Edge
//Adjacency == Linked list of all
public class Djikstra implements PathFindAlgo {

    public Path computePath(MapOverlay mapOverlay, Node start, Node end) {
        ArrayList<Node> encountered = new ArrayList<>();
        ArrayList<Node> unEncountered = new ArrayList<>();
        start.setInterimCost(0);
        unEncountered.add(start);
        Node currentNode;
        Path path = new Path();
        do {
            currentNode = unEncountered.remove(0);
            System.out.println(currentNode.display(mapOverlay));
            encountered.add(currentNode);
            if (currentNode.equals(end)) {
                path.addNode(currentNode);
                path.setCost(currentNode.getInterimCost());
                while (currentNode != start) {
                    boolean foundPrev = false;
                    for (Node n : encountered) {
                        for (Edge e : n.getEdges()) {
                            if ((e.getDestination() == currentNode) && ((currentNode.getInterimCost() - e.getCost()) == n.getInterimCost())) {
                                path.addToFront(n);
                                currentNode = n;
                                foundPrev = true;
                                break;
                            }
                        }
                        if (foundPrev) {
                            break;
                        }
                    }
                }
                for (Node n : encountered) {
                    n.setInterimCost(Integer.MAX_VALUE);
                }
                return path;
            }
            System.out.println(currentNode.display(mapOverlay));
            for (Edge e : currentNode.getEdges()) {
                if (!encountered.contains(e.getDestination())) {
                    e.getDestination().setInterimCost(Integer.min(e.getDestination().getInterimCost(), currentNode.getInterimCost() + e.getCost()));
                    unEncountered.add(e.getDestination());
                }
            }
            Set<Node> set = new HashSet<>(unEncountered);
            unEncountered.clear();
            unEncountered.addAll(set);
            unEncountered.sort(Comparator.comparingInt(Node::getInterimCost));
        } while (!unEncountered.isEmpty());
        return null;
    }

    @Override
    public LinkedList<Path> computePaths(MapOverlay mapOverlay, Node start, Node end) {
        return null;
    }

}
