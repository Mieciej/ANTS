package com.maciej.ants;


import com.badlogic.gdx.math.Vector2;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import static com.maciej.ants.Math.haltonSequence;

public class WorldManager {
    private Graph<Node> graph;
    private static WorldManager instance;
    private HashMap<String,Anthill> anthills;
    private final Object antsMutex = new Object();
    private ArrayList<Ant> ants;
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

    public static void initialiseWorld(int numberOfVertices,int numberOfAnts) {
        instance = new WorldManager();
        worldManager().anthills = new HashMap<>();
        worldManager().ants = new ArrayList<>();
        Graph<Node> newGraph = new Graph<>();
        for (int i = 0; i < numberOfVertices - 2; i++) {
            Node node = new Node();
            newGraph.addVertex(node);
        }

        float[] xSequence = haltonSequence(3, numberOfVertices);
        float[] ySequence = haltonSequence(2, numberOfVertices);
        ArrayList<Vector2> positions = new ArrayList<>();
        for (int i = 0; i < numberOfVertices; i++) {
            positions.add(new Vector2(xSequence[i], ySequence[i]));
        }
        positions.sort(new Comparator<Vector2>() {
            @Override
            public int compare(Vector2 t1, Vector2 t2) {
                return Float.compare(t1.dst(new Vector2(0, 0)), t2.dst(new Vector2(0, 0)));
            }
        });
        Vector2 anthillPos = positions.remove(0);
        for (Node vertex : newGraph.getVertices()) {
            vertex.setAbstractPosition(positions.remove(0));
        }
        Anthill anthill = new Anthill("RED");
        worldManager().anthills.put("RED", anthill);
        newGraph.addVertex(anthill);
        anthill.setAbstractPosition(anthillPos);
        anthill = new Anthill("BLUE");
        newGraph.addVertex(anthill);
        worldManager().anthills.put("BLUE", anthill);
        anthill.setAbstractPosition(positions.remove(0));
        for (Node vertex : newGraph.getVertices()) {
            Vector2 vertexPos = vertex.getAbstractPosition();
            ArrayList<Node> alreadyConnected = new ArrayList<>();
            int edgeDensity = ThreadLocalRandom.current().nextInt(2, 4);
            while (alreadyConnected.size() < edgeDensity) {
                double minDistance = Double.POSITIVE_INFINITY;
                Node key = null;
                for (Node neighbour : newGraph.getVertices()) {
                    if (neighbour == vertex || alreadyConnected.contains(neighbour)) continue;
                    float neighPos = vertexPos.dst(neighbour.getAbstractPosition());
                    if (neighPos < minDistance) {
                        minDistance = neighPos;
                        key = neighbour;
                    }
                }
                newGraph.addEdge(vertex, key, (float) minDistance);
                alreadyConnected.add(key);
            }
        }
        instance.setGraph(newGraph);
        for (int i = 0; i < numberOfAnts; i++) {
            if (i < numberOfAnts / 2) {
                Anthill a = worldManager().getAnthills().get("RED");
                switch (ThreadLocalRandom.current().nextInt(2)) {
                    case 0:
                        worldManager().createAnt("WORKER",a);
                        break;
                    case 1:
                        worldManager().createAnt("DRONE",a);
                        break;
                }
            } else {
                Anthill a = worldManager().getAnthills().get("BLUE");
                switch (ThreadLocalRandom.current().nextInt(3)) {
                    case 0:
                        worldManager().createAnt("SOLDIER",a);
                        break;
                    case 1:
                        worldManager().createAnt("COLLECTOR",a);
                        break;
                    case 2:
                        worldManager().createAnt("BLUNDERER",a);
                        break;
                }
            }
        }
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
    public void createAnt(String antVariantName,Anthill anthill){
        Ant ant = null;
       switch (antVariantName) {
           case "SOLDIER":
               ant = AntFactory.getSoldier(anthill);
               break;
           case "DRONE":
               ant = AntFactory.getDrone(anthill);
               break;
           case "WORKER":
               ant = (AntFactory.getWorker(anthill));
               break;
           case "BLUNDERER":
               ant = (AntFactory.getBlunderer(anthill));
               break;
           case "COLLECTOR":
               ant = (AntFactory.getCollector(anthill));
               break;
           default:
               throw new IllegalStateException("Unexpected value: " + antVariantName);
       }
       ants.add(ant);
       ant.start();

    }
    public void removeAnt(Ant ant){
        synchronized (antsMutex) {
            ants.remove(ant);
        }
    }
    public ArrayList<Ant> getAnts(){
        ArrayList<Ant> tmp;
        synchronized (antsMutex) {
            tmp = new ArrayList<>(ants);
        }
        return tmp;
    }
}
