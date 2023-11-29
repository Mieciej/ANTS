package com.maciej.ants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Ant extends Thread {
    private final float speed = 0.15f;
    private float roadCovered;
    private int distanceToCover;
    public Node sourceVertex;
    public Node targetVertex;
    private static int counter =0;
    public Ant(){
        reactions = new LinkedBlockingQueue<Command>(1);
        sourceVertex = WorldManager.worldManager().getGraph().getVertices().iterator().next();
        targetVertex = sourceVertex;
        chooseRandomTravel();

    }
    LinkedBlockingQueue<Command> reactions;
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
                targetVertex.registerAnt(this);
                targetVertex.addCommand(new AttackCommand(this, targetVertex));
                try {
                    reactions.take().execute();
                } catch (InterruptedException e) {
                    return;
                }
                targetVertex.unregisterAnt(this);
                chooseRandomTravel();
            }


        }
    }
    public float getTravelProgress(){
        return roadCovered/distanceToCover;
    }
    private void chooseRandomTravel(){
        HashMap<Node,Integer> travelOptions = WorldManager.worldManager().getGraph().getSuccessors(targetVertex);
        Iterator<Node> it = travelOptions.keySet().iterator();
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(travelOptions.size()); i++) {
            it.next();
        }
        sourceVertex = targetVertex;
        roadCovered = 0;
        targetVertex =  it.next();
        distanceToCover = travelOptions.get(targetVertex);

    }
    public void takeDamage(){
        System.out.println("Taking damage!");
    }

}
