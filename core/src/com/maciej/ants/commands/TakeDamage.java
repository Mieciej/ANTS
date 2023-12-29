package com.maciej.ants.commands;

import com.maciej.ants.Ant;

public class TakeDamage extends ReflectiveCommand<Ant>{
    @Override
    public void execute() {
         reflector.takeDamage();
    }
}
