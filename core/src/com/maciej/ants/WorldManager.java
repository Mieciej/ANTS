package com.maciej.ants;


import com.badlogic.gdx.math.Vector2;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.maciej.ants.Math.haltonSequence;

public class WorldManager {
    private Graph<Node> graph;
    private static WorldManager instance;

    public static WorldManager worldManager(){
        try {
            if(instance == null){
                throw new Exception("World not initialised!");
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        return instance;
    }
    public static void initialiseWorld(int numberOfVertices){
       Graph<Node> newGraph = new Graph<>();
        for (int i = 0; i < numberOfVertices; i++) {
            Node node = new Node();
            newGraph.addVertex(node);
        }
        float[] xSequence = haltonSequence(3,numberOfVertices);
        float[] ySequence = haltonSequence(2,numberOfVertices);
        HashMap<Node,Vector2> positions = new HashMap<>();
        for (Node vertex : newGraph.getVertices()) {
            int n = positions.size();
            positions.put(vertex,new Vector2(xSequence[n],ySequence[n]));
        }
        for (Node vertex: newGraph.getVertices()){
            Vector2 vertexPos = positions.get(vertex);
            ArrayList<Node> alreadyConnected = new ArrayList<>();
            int edgeDensity = ThreadLocalRandom.current().nextInt(2,4);
            while (alreadyConnected.size()<edgeDensity) {
                double minDistance =  Double.POSITIVE_INFINITY;
                Node key = null;
                for (Node neighbour : newGraph.getVertices()) {
                    if (neighbour == vertex || alreadyConnected.contains(neighbour)) continue;
                    float neighPos =  vertexPos.dst(positions.get(neighbour));
                    if(neighPos<minDistance) {
                        minDistance = neighPos;
                        key = neighbour;
                     }
                }
                newGraph.addEdge(vertex, key,   (int)(minDistance * 100) );
                alreadyConnected.add(key);
            }
        }
        instance = new WorldManager();
        instance.setGraph(newGraph);
    }
    public Graph<Node> getGraph(){
        return graph;
    }
    private void setGraph(Graph<Node> value){
       graph =  value;
    }
}
