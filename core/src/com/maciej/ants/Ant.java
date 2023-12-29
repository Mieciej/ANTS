package com.maciej.ants;

import com.badlogic.gdx.math.Vector2;
import com.maciej.ants.commands.*;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Ant extends Thread implements AbstractPositionable {
    private Vector2 abstractPosition;
    private Node currentVertex;
    public boolean exit;
    private String team;
    private LinkedBlockingQueue<Command> reactions;
    private Anthill anthill;
    private ArrayList<Command> steps;
    private int numberOfLarvae;
    private int maxLarvae;
    private String antName;
    private AntVariant variant;
    private int currentHP;
    private int maxHP;
    public Ant(Anthill anthill,AntVariant variant , int maxLarvae, int hp){
        numberOfLarvae = 0;
        this.maxLarvae = maxLarvae;
        this.currentHP = hp;
        this.maxHP = hp;
        setExit(false);
        reactions = new LinkedBlockingQueue<>(1);
        setAnthill(anthill);
        setCurrentVertex(getAnthill());
        setAbstractPosition(getAnthill().getAbstractPosition());
        steps = new ArrayList<>();
        addStep(new ChooseRandomTravel(this,getAnthill()));
        setTeam(anthill.team);
        this.variant = variant;
        String[] antNames = {
                "Sergeant Stinger",
                "Major Mandible",
                "Captain Crawler",
                "Private Pincers",
                "Colonel Carapace",
                "General Grit",
                "Lieutenant Legs",
                "Corporal Claw",
                "Commander Chitin",
                "Brigadier Beetle",
                "Master Sergeant Mantis",
                "First Sergeant Forelimb",
                "Staff Sergeant Scuttle",
                "Sergeant First Class Swarm",
                "Technical Sergeant Thorax",
                "Chief Warrant Officer Chelicera",
                "Warrant Officer Web",
                "Chief Petty Officer Pheromone",
                "Petty Officer Pincer",
                "Specialist Soldier Ant"
        };
        setAntName(antNames[ThreadLocalRandom.current().nextInt(antNames.length)]);
    }
    @Override
    public void run() {
        while (!getExit()){
            Command c = getStep();
            if(c!=null){
                c.execute();
            }
            else {
                addStep(new ChooseRandomTravel(this,getCurrentVertex()));
            }
        }
        WorldManager.worldManager().removeAnt(this);
    }

    public Node getCurrentVertex() {
        return currentVertex;
    }

    public void setCurrentVertex(Node currentVertex) {
        this.currentVertex = currentVertex;
    }
    public boolean addReaction(Command reactionCommand){
       return reactions.offer(reactionCommand);
    }
    public Command getReaction(){
        try {
            return reactions.take();
        } catch (InterruptedException  e) { setExit(true); }
        return null;

    }
    public Command getReaction(int milliseconds){
        try {
            return reactions.poll(milliseconds, TimeUnit.MILLISECONDS);
        }catch (InterruptedException e){
            setExit(true);
        }
        return null;
    }
    public void clearReactions(){
       reactions.clear();
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }
    public boolean getExit(){
        return exit;
    }
    public void addStep(Command command){
        steps.add(command);
    }
    public Command getStep(){
        if(!steps.isEmpty()) return steps.remove(0);
        else return null;
    }

    public void setAbstractPosition(Vector2 abstractPosition) {
        this.abstractPosition = abstractPosition;
    }

    public Vector2 getAbstractPosition() {
        return abstractPosition;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
    public void clearSteps(){
        steps.clear();
    }

    public Anthill getAnthill() {
        return anthill;
    }

    private void setAnthill(Anthill anthill) {
        this.anthill = anthill;
    }
    public void onNodeReached(Node node){
        node.registerAnt(this);
        for (ReflectiveCommand<Node> command : variant.matchNodeToCommands(node)) {
            command.setReflector(node);
            node.addCommand(this,command);
        }
        Command c = getReaction(ThreadLocalRandom.current().nextInt(500,1500));
        if(c!= null) c.execute();
        node.unregisterAnt(this);
        clearReactions();
    }


    @Override
    public String toString() {
        String ret= getAntName() + " is a " + variant.getVariantName() + " of the " + getTeam() + " team.\n";
        ret += "HP: "+getCurrentHP()+"/"+getMaxHP()+"\n";
        if(numberOfLarvae>0) ret += " is carrying " +numberOfLarvae + " larvae.\n";
        if(!steps.isEmpty()){
            StringBuilder stepString = new StringBuilder("His current route is: \n");
            for (int i = 0; i < java.lang.Math.min(steps.size(),3);++i) {
                stepString.append(steps.get(i).toString()).append("\n");
            }
            if(steps.size()>3) stepString.append("...\n");
            ret +=stepString;
        }
        
        return ret;
    }

    public void setAntName(String antName) {
        this.antName = antName;
    }

    public String getAntName() {
        return antName;
    }
    public int pickLarvae(){
        if(numberOfLarvae+1<maxLarvae){
            numberOfLarvae++;
            return -1;
        } else if (numberOfLarvae+1 == maxLarvae) {
            numberOfLarvae++;
            return 0;
        }
        else return 1;
    }

    public int dropLarva(){
        int ret = numberOfLarvae;
        numberOfLarvae = 0;
        return  ret;
    }
    public void takeDamage(){
        if(--currentHP <= 0 ){
            ReflectiveCommand<Node> c = new DropLarvae(this);
            c.setReflector(getCurrentVertex());
            c.execute();
            setExit(true);
        }
    }
    private int getCurrentHP(){
        return currentHP;
    }
    private int getMaxHP(){
        return maxHP;
    }
}