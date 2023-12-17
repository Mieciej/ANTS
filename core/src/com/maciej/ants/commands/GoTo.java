package com.maciej.ants.commands;

import com.badlogic.gdx.math.Vector2;
import com.maciej.ants.Ant;
import com.maciej.ants.Node;
import com.maciej.ants.WorldManager;

import static java.lang.Thread.getAllStackTraces;
import static java.lang.Thread.sleep;

public class GoTo extends ReflectiveCommand<Ant> {
    Node start;
    Node end;

    private float progress;
    private float speed;
    @Override
    public void execute() {
        reflector.setCurrentVertex(null);
        reflector.setAbstractPosition(WorldManager.worldManager().getGraph().getAbstractPosition(start));
        float distanceToCover ;
        if(start==end)
            distanceToCover = 0;
        else distanceToCover = WorldManager.worldManager().getGraph().getSuccessors(start).get(end);
        boolean exit = false;
        Vector2 antPosSource = WorldManager.worldManager().getGraph().getAbstractPosition(start);
        Vector2 antPosTarget = WorldManager.worldManager().getGraph().getAbstractPosition(end);
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
                reflector.setAbstractPosition(WorldManager.worldManager().getGraph().getAbstractPosition(end));
                reflector.setCurrentVertex(end);
                reflector.onNodeReached(end);
                exit = true;
            }
        }

    }
    public GoTo( Node start, Node end, Ant ant){
        this.start = start;
        this.end = end;
        progress = 0;
        setReflector(ant);
        if(ant.isRetreating()) speed = 0.0015f;
        else speed = 0.002f;
    }

    @Override
    public String toString() {
        return "From "+ start.getNodeName() +  " to " + end.getNodeName();
    }

}
