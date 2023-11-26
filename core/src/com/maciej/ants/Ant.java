package com.maciej.ants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class Ant extends Thread {
    public float speed = 0.15f;
    private float roadCovered;
    private int distanceToCover;
    public int sourceVertex;
    public int targetVertex;
    public Ant(){
       sourceVertex = 0;
      chooseRandomTravel();
    }
    @Override
    public void run() {
        while (true){
            try {
                sleep(16);
            } catch (InterruptedException e) {
                return;
            }
            roadCovered+=speed;
            if(roadCovered >= distanceToCover){
                chooseRandomTravel();
            }


        }
    }
    public float getTravelProgress(){
        return roadCovered/distanceToCover;
    }
    private void chooseRandomTravel(){
        HashMap<Integer,Integer> travelOptions = WorldManager.worldManager().getGraph().getSuccessors(targetVertex);
        Iterator it = travelOptions.keySet().iterator();
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(travelOptions.size()); i++) {
            it.next();
        }
        sourceVertex = targetVertex;
        roadCovered = 0;
        targetVertex = (int) it.next();
        distanceToCover = travelOptions.get(targetVertex);

    }

}
