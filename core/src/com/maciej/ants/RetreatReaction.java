package com.maciej.ants;

public class RetreatReaction extends ReactionCommand <Ant>{
    @Override
    public void execute() {
        reactor.takeDamage();
    }
}
