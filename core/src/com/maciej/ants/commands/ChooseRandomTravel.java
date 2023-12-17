package com.maciej.ants.commands;

import com.maciej.ants.Ant;
import com.maciej.ants.Node;
import com.maciej.ants.WorldManager;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ChooseRandomTravel extends ReflectiveCommand<Ant>{
    private Node start;
    @Override
    public void execute() {
        Set<Node> travelOptions = WorldManager.worldManager().getGraph().getVertices();
        Iterator<Node> it = travelOptions.iterator();

        for (int i = 0; i < ThreadLocalRandom.current().nextInt(travelOptions.size()); i++) {
            it.next();
        }
        Node goal =it.next();
        if (goal == reflector.getCurrentVertex()){
            execute();
        }
        reflector.addStep(new Travel(reflector,start,goal));
    }
    public ChooseRandomTravel(Ant ant, Node start){
        this.start = start;
        setReflector(ant);
    }
}
