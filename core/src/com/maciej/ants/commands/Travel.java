package com.maciej.ants.commands;

import com.maciej.ants.*;

import java.util.ArrayList;

public class Travel extends ReflectiveCommand<Ant>{
    Node start;
    Node end;
    @Override
    public void execute() {
        PathFinder pathFinder = new PathFinder(WorldManager.worldManager().getGraph());
        ArrayList<Node> path = pathFinder.getPath(start, end);
        reflector.clearSteps();
        if(path.size() <=1){
            reflector.addStep(new GoTo(start,end,reflector));
        }
        for (int i = 1; i < path.size(); i++) {
            reflector.addStep(new GoTo(path.get(i-1), path.get(i),reflector));
        }
    }
    public Travel(Ant ant,Node start, Node end){
        setReflector(ant);
        this.start = start;
        this.end = end;

    }

}
