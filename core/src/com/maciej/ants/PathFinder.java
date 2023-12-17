package com.maciej.ants;

import java.util.*;

public class PathFinder<T> {
    private Graph<T> graph;
    HashMap<T,HashMap<T,Float>> heuristic;
    public ArrayList<T> getPath(final T start, final T end ) {
        final HashMap<T, Float> reached = new HashMap<>();

        reached.put(start, 0.0f);
        HashMap<T, ArrayList<T>> reachedPath = new HashMap<>();
        T node;
        reachedPath.put(start,new ArrayList<T>());
        PriorityQueue<T> frontier = new PriorityQueue<>(new Comparator<T>() {
            @Override
            public int compare(T t, T t1) {
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
                ArrayList<T> tmp = new ArrayList<>();
                for (T t : reachedPath.get(node)) {
                   tmp.add(t);
                }
                tmp.add(node);
                return tmp;
            }
            for (T child : graph.getSuccessors(node).keySet()) {
                float dist = graph.getSuccessors(node).get(child);
                if (!reached.containsKey(child) || reached.get(child) > reached.get(node) + dist) {
                    reached.put(child, dist + reached.get(node));
                    ArrayList<T> tmp = new ArrayList<>();
                    for (T t : reachedPath.get(node)) {
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
    public PathFinder(Graph<T> graph ){
        this.graph = graph;
        heuristic = new HashMap<>();
        for (T v1 : graph.getVertices()) {
            for (T v2 : graph.getVertices()) {
               if(!heuristic.containsKey(v1))
                    heuristic.put(v1,new HashMap<T,Float>());
               heuristic.get(v1).put(v2,graph.getAbstractPosition(v1).dst(graph.getAbstractPosition(v2)));
            }

        }

    }
}
