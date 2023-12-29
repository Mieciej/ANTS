package com.maciej.ants.commands;

import com.maciej.ants.Ant;

public class Retreat extends ReflectiveCommand<Ant> {
    @Override
    public void execute() {
        new Travel(reflector,reflector.getCurrentVertex(),reflector.getAnthill()).execute();
    }

}
