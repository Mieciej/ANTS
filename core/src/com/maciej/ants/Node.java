package com.maciej.ants;

import java.util.ArrayList;

public class Node  {
    private ArrayList<Ant> antRegister;
    private ArrayList<Command> commandQueue;
    private final Object  commandMutex = new Object();
    private final Object  registrationMutex = new Object();

    public void addCommand(Command command){
        synchronized (commandMutex){
            commandQueue.add(command);
        }
    }
    public void registerAnt(Ant ant){
       synchronized (registrationMutex){
          antRegister.add(ant);
       }
    }
    public void unregisterAnt(Ant ant){
       synchronized (registrationMutex){
           antRegister.remove(ant);
       }
    }
    public boolean sendReactionToOtherAnt(Ant invokingAnt, ReactionCommand<Ant> e){
        synchronized (registrationMutex) {
            for (Ant ant : antRegister)
            {
                if(ant!=invokingAnt){
                   e.setReactor(ant);
                   return ant.reactions.offer(e);
                }
            }
        }
        return  false;
    }

    public Node(){
        commandQueue = new ArrayList<>();
        antRegister = new ArrayList<>();
    }
    public void update() {
        synchronized (commandMutex) {
            if (!commandQueue.isEmpty()) {
                commandQueue.remove(0).execute();
            }
        }
    }
}
