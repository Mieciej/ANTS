package com.maciej.ants;

import com.badlogic.gdx.math.Vector2;

import java.security.InvalidKeyException;
import java.util.*;

/**
 * Abstract Graph Representation
 * @param <V> Node type.
 */
public class Graph<V> {

    private HashMap<V,HashMap<V,Float> > adjacencyList;
    public Graph(){
        adjacencyList = new HashMap<>();
    }

    /**
     * Adds vertex to the graph.
     * @param vertex
     */
    public void addVertex(V vertex){
        if(adjacencyList.get(vertex)== null){
            adjacencyList.put(vertex,new HashMap<V, Float>());
        }
    }

    /**
     * Adds edge between vertex1 and vertex2 of given weight.
     * @param vertex1
     * @param vertex2
     * @param weight
     */
    public void addEdge(V vertex1, V vertex2, float weight){

        try {
            if (adjacencyList.get(vertex1) == null || adjacencyList.get(vertex2) == null)
                throw new InvalidKeyException("Vertex not in graph");
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        adjacencyList.get(vertex1).put(vertex2,weight);
        adjacencyList.get(vertex2).put(vertex1,weight);
    }
    public Set<V> getVertices(){
        return adjacencyList.keySet();
    }
    public HashMap<V, Float> getSuccessors(V vertex){
        return adjacencyList.get(vertex);
    }

    /**
     *
     * @return list of pairs of vertices with associated weight.
     */
    public ArrayList<AbstractMap.SimpleEntry<V,V>> getEdges(){
        ArrayList<AbstractMap.SimpleEntry<V,V>> edges = new ArrayList<>();
        for (V vertex :
                getVertices()) {
            for (V successor :
                    getSuccessors(vertex).keySet()) {
                AbstractMap.SimpleEntry<V,V> edge1 = new AbstractMap.SimpleEntry<>(vertex,successor);
                AbstractMap.SimpleEntry<V,V> edge2 = new AbstractMap.SimpleEntry<>(successor,vertex);
                if(edges.contains(edge1) || edges.contains(edge2)) continue;
                edges.add(edge1);

            }
        }
        return edges;
    }
}
