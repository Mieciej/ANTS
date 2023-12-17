package com.maciej.ants.commands;

import com.maciej.ants.Ant;

public class Retreat extends ReflectiveCommand<Ant> {
    @Override
    public void execute() {
        reflector.setRetreating(true);
        new Travel(reflector,reflector.getCurrentVertex(),reflector.getAnthill()).execute();
    }

}
