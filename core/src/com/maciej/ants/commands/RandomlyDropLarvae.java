package com.maciej.ants.commands;

import com.maciej.ants.Ant;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Randomly drop all larvae.
 */
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
