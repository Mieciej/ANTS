package com.maciej.ants.commands;

import com.maciej.ants.Ant;

/**
 * Make reflector (ant) take damage.
 */
public class TakeDamage extends ReflectiveCommand<Ant>{
    @Override
    public void execute() {
         reflector.takeDamage();
    }
}
