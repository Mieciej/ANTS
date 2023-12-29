package com.maciej.ants.commands;

import com.maciej.ants.Ant;

import java.util.concurrent.ThreadLocalRandom;

public class RandomlyDropLarvae extends DropLarvae{
    @Override
    public void execute() {
        if(ThreadLocalRandom.current().nextBoolean()) {
            super.execute();
        }
    }

    public RandomlyDropLarvae(Ant ant) {
        super(ant);
    }
}
