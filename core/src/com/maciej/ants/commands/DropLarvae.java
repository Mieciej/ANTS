package com.maciej.ants.commands;

import com.maciej.ants.Ant;
import com.maciej.ants.Node;

/**
 * Used to drop all larvae on the reflector (node) by the given ant.
 */
public class DropLarvae extends ReflectiveCommand<Node> {
    Ant ant;

    /**
     *
     * @param ant The ant which will drop all of its Larvae.
     */
    public DropLarvae(Ant ant){
        this.ant = ant;

    }

    @Override
    public void execute() {
        reflector.leaveLarvae(ant.dropLarva());
    }
}
