package com.maciej.ants;

import java.util.concurrent.TimeUnit;

public class AttackCommand implements Command{
    private Ant attacker;
    private Node node;
    @Override
    public void execute() {
        System.out.println("Attack");

        boolean attacked = node.sendReactionToOtherAnt(attacker, new RetreatReaction());
        if(attacked)attacker.reactions.offer(new Command() {
            @Override
            public void execute() {
                System.out.println("Moving on!");
            }
        });
        else attacker.reactions.offer(new Command() {
            @Override
            public void execute() {
                try {
                    Command c = attacker.reactions.poll(2000,TimeUnit.MILLISECONDS);
                    if (c != null) {
                        c.execute();
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
    }
    public AttackCommand(Ant attacker, Node node){
        this.attacker = attacker;
        this.node = node;
    }
}
