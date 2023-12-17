package com.maciej.ants.commands;

public class ReflectiveCommand<T> implements Command {
    public T reflector;
    @Override
    public void execute() {
    }
    public void setReflector(T reflector){
        this.reflector = reflector;
    }
}
