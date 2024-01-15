package com.maciej.ants.commands;

import com.maciej.ants.Ant;
import com.maciej.ants.Node;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Command used to attack other ant.
 */
public class Attack extends ReflectiveCommand<Node>{
    private Ant attacker;
    private ReflectiveCommand<Ant>  afterAttack;
    @Override
    public void execute() {
        boolean attacked = reflector.sendReactionToOtherAnt(attacker, new TakeDamage());
        afterAttack.setReflector(attacker);
        if (attacked) attacker.addReaction(afterAttack);
    }

    /**
     *
     * @param attacker The ant which attacks.
     * @param afterAttack Command to be executed by the ant after attacking.
     */
    public Attack(Ant attacker, ReflectiveCommand<Ant> afterAttack){
        this.attacker = attacker;
        this.afterAttack = afterAttack;

    }
}
