package com.maciej.ants;


import com.badlogic.gdx.math.Vector2;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.maciej.ants.Math.haltonSequence;

public class WorldManager {
    private Graph<Node> graph;
    private static WorldManager instance;
    private HashMap<String,Anthill> anthills;

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
        final String[] antHillNames = new String[]{"RED", "BLUE"};
        instance = new WorldManager();
        worldManager().anthills = new HashMap<>();
        Graph<Node> newGraph = new Graph<>();
        for (int i = 0; i < numberOfVertices -2; i++) {
            Node node = new Node();
            newGraph.addVertex(node);
        }

        float[] xSequence = haltonSequence(3,numberOfVertices);
        float[] ySequence = haltonSequence(2,numberOfVertices);
        ArrayList<Vector2> positions = new ArrayList<>();
        for (int i = 0; i < numberOfVertices; i++) {
           positions.add( new Vector2(xSequence[i],ySequence[i]));
        }
        positions.sort(new Comparator<Vector2>() {
            @Override
            public int compare(Vector2 t1, Vector2 t2) {
                return Float.compare(t1.dst(new Vector2(0,0)), t2.dst(new Vector2(0,0)));
            }
        });
        Vector2 anthillPos = positions.remove(0);
        for (Node vertex: newGraph.getVertices()){
            newGraph.setAbstractPosition(vertex,positions.remove(0));
        }
        Anthill anthill = new Anthill("RED");
        worldManager().anthills.put("RED",anthill);
        newGraph.addVertex(anthill);
        newGraph.setAbstractPosition(anthill,anthillPos);
        anthill = new Anthill("BLUE");
        newGraph.addVertex(anthill);
        worldManager().anthills.put("BLUE",anthill);
        newGraph.setAbstractPosition(anthill,positions.remove(0));
        for (Node vertex: newGraph.getVertices()){
            Vector2 vertexPos = newGraph.getAbstractPosition(vertex);
            ArrayList<Node> alreadyConnected = new ArrayList<>();
            int edgeDensity = ThreadLocalRandom.current().nextInt(2,4);
            while (alreadyConnected.size()<edgeDensity) {
                double minDistance =  Double.POSITIVE_INFINITY;
                Node key = null;
                for (Node neighbour : newGraph.getVertices()) {
                    if (neighbour == vertex || alreadyConnected.contains(neighbour)) continue;
                    float neighPos =  vertexPos.dst(newGraph.getAbstractPosition(neighbour));
                    if(neighPos<minDistance) {
                        minDistance = neighPos;
                        key = neighbour;
                    }
                }
                newGraph.addEdge(vertex, key, (float) minDistance);
                alreadyConnected.add(key);
            }
        }
        instance.setGraph(newGraph);
    }
    public Graph<Node> getGraph(){
        return graph;
    }
    private void setGraph(Graph<Node> value){
        graph =  value;
    }

    public HashMap<String,Anthill> getAnthills() {
        return anthills;
    }
}
