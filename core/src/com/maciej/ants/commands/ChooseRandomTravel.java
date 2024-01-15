package com.maciej.ants.commands;

import com.maciej.ants.Ant;
import com.maciej.ants.Node;
import com.maciej.ants.WorldManager;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates random path to walk on for the reflector (ant).
 */
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
        if (goal == reflector.getCurrentNode()){
            execute();
        }
        reflector.addStep(new Travel(reflector,start,goal));
    }

    /**
     *
     * @param ant The traveler.
     * @param start The starting node of the travel.
     */
    public ChooseRandomTravel(Ant ant, Node start){
        this.start = start;
        setReflector(ant);
    }
}
