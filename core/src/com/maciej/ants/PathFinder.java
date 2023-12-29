package com.maciej.ants;

import java.util.*;

public class PathFinder {
    private Graph<Node> graph;
    HashMap<Node,HashMap<Node,Float>> heuristic;
    public ArrayList<Node> getPath(final Node start, final Node end ) {
        final HashMap<Node, Float> reached = new HashMap<>();

        reached.put(start, 0.0f);
        HashMap<Node, ArrayList<Node>> reachedPath = new HashMap<>();
        Node node;
        reachedPath.put(start,new ArrayList<Node>());
        PriorityQueue<Node> frontier = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node t, Node t1) {
                float hT = heuristic.get(t).get(end);
                float hT1 = heuristic.get(t1).get(end);
                float evalT = hT + reached.get(t);
                float evalT1 = hT1 + reached.get(t1);
                if (evalT < evalT1)
                    return -1;
                else if (evalT > evalT1)
                    return 1;
                return 0;
            }
        });
        frontier.add(start);
        while (!frontier.isEmpty()) {
            node = frontier.poll();
            if (node == end) {
                ArrayList<Node> tmp = new ArrayList<>();
                for (Node t : reachedPath.get(node)) {
                   tmp.add(t);
                }
                tmp.add(node);
                return tmp;
            }
            for (Node child : graph.getSuccessors(node).keySet()) {
                float dist = graph.getSuccessors(node).get(child);
                if (!reached.containsKey(child) || reached.get(child) > reached.get(node) + dist) {
                    reached.put(child, dist + reached.get(node));
                    ArrayList<Node> tmp = new ArrayList<>();
                    for (Node t : reachedPath.get(node)) {
                        tmp.add(t);
                    }
                    tmp.add(node);
                    reachedPath.put(child, tmp);
                    frontier.add(child);
                }
            }
        }
        return null;
    }
    public PathFinder(Graph<Node> graph ){
        this.graph = graph;
        heuristic = new HashMap<>();
        for (Node v1 : graph.getVertices()) {
            for (Node v2 : graph.getVertices()) {
               if(!heuristic.containsKey(v1))
                    heuristic.put(v1,new HashMap<Node,Float>());
               heuristic.get(v1).put(v2,v1.getAbstractPosition().dst(v2.getAbstractPosition()));
            }

        }

    }
}
