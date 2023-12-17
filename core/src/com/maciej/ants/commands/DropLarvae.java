package com.maciej.ants.commands;

import com.maciej.ants.Ant;
import com.maciej.ants.Node;

public class DropLarvae extends ReflectiveCommand<Node> {
    Ant ant;
    public DropLarvae(Ant ant){
        this.ant = ant;

    }

    @Override
    public void execute() {
        reflector.leaveLarvae(ant.dropLarva());
    }
}
