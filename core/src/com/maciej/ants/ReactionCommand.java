package com.maciej.ants;

public class ReactionCommand <T> implements Command {
    public T reactor;
    @Override
    public void execute() {
    }
    public void setReactor(T reactor){
        this.reactor = reactor;
    }
}
