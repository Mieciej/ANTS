package com.maciej.ants.commands;

import com.maciej.ants.Ant;
import com.maciej.ants.Node;

public class PickLarvae extends ReflectiveCommand<Node> {
    Ant ant;
    @Override
    public void execute() {
        if(reflector.pickLarvae())
        {
            switch (ant.pickLarvae()){
                case -1:
                    break;
                case 0:
                    ant.addReaction(new Travel(ant,reflector,ant.getAnthill()));
                    break;
                case 1:
                    reflector.leaveLarvae(1);
                    ant.addReaction(new Travel(ant,reflector,ant.getAnthill()));
                    break;
            }

        }

    }
    public PickLarvae(Ant ant){
        this.ant = ant;
    }
}
