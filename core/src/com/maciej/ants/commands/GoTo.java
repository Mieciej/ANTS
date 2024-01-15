package com.maciej.ants.commands;

import com.badlogic.gdx.math.Vector2;
import com.maciej.ants.Ant;
import com.maciej.ants.Node;
import com.maciej.ants.WorldManager;

import static java.lang.Thread.sleep;

/**
 * Handles movement between two nodes on the world graph.
 */
public class GoTo extends ReflectiveCommand<Ant> {
    Node start;
    Node end;

    private float progress;
    private float speed;

    /**
     * Changes ant abstract position every 16 milliseconds until the end node is reached.
     * When the end node is reached it executes ant.onNodeReached() method.
     */
    @Override
    public void execute() {
        reflector.setCurrentNode(null);
        reflector.setAbstractPosition(start.getAbstractPosition());
        float distanceToCover ;
        if(start==end)
            distanceToCover = 0;
        else distanceToCover = WorldManager.worldManager().getGraph().getSuccessors(start).get(end);
        boolean exit = false;
        Vector2 antPosSource = start.getAbstractPosition();
        Vector2 antPosTarget = end.getAbstractPosition();
        while (!reflector.getExit()&&!exit){
            try {
                sleep(16);
            } catch (InterruptedException e) {
                reflector.setExit(true);
                break;
            }
            progress+= speed;
            reflector.setAbstractPosition(new Vector2((antPosTarget.x - antPosSource.x) * progress/distanceToCover + antPosSource.x, (antPosTarget.y - antPosSource.y) * progress/distanceToCover + antPosSource.y));
            if(progress >= distanceToCover){
                reflector.setAbstractPosition(antPosTarget  );
                reflector.setCurrentNode(end);
                reflector.onNodeReached(end);
                exit = true;
            }
        }

    }

    /**
     *
     * @param start Node adjacent to end.
     * @param end Node adjacent to start.
     * @param ant The traveler.
     */
    public GoTo( Node start, Node end, Ant ant){
        this.start = start;
        this.end = end;
        progress = 0;
        setReflector(ant);
        speed = 0.002f;
    }

    @Override
    public String toString() {
        return "From "+ start.getNodeName() +  " to " + end.getNodeName();
    }

}
