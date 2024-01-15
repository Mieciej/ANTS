package com.maciej.ants.commands;

import com.maciej.ants.Ant;

/**
 * Creates route from current vertex to anthill.
 */
public class Retreat extends ReflectiveCommand<Ant> {
    @Override
    public void execute() {
        new Travel(reflector,reflector.getCurrentNode(),reflector.getAnthill()).execute();
    }

}
