package com.hbjdjd.ca2.Map;

import java.util.LinkedList;

public class Path {

    private final LinkedList<Node> route = new LinkedList<>();
    private int cost = 0;

    public Path(){

    }
    public Path(Path nextPath) {
        this.route.addAll(nextPath.route);
        this.cost = nextPath.cost;
    }

    public void addNode(Node node) {
        route.add(node);
    }

    @Override
    public String toString() {
        String s = "Path: {";
        for (Node node : route) {
            s = s.concat("\n\t" + node.getNodeID());
        }
        s = s.concat("\n}");
        return s;
    }

    public String getCostString() {
        if(cost != 0){
            return String.valueOf(cost);
        } else{
            for(Node n : route){
                for(Edge e : n.getEdges()){
                    if(e.getDestination() != n){
                        cost+= e.getCost();
                    }
                }
            }
        }
        return String.valueOf(cost);
    }

    public int getCost(){
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Node get(int index){
        return route.get(index);
    }

    public LinkedList<Node> getRoute() {
        return route;
    }

    public void addToFront(Node n) {
        route.add(0, n);
    }

    public void add(Node node) {
        route.add(node);
    }
}
