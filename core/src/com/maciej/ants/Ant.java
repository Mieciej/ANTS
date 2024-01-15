package com.maciej.ants;

import com.badlogic.gdx.math.Vector2;
import com.maciej.ants.commands.*;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Class which represents every ant on the screen.
 */
public class Ant extends Thread implements AbstractPositionable {
    private Vector2 abstractPosition;
    private Node currentNode;
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
    private final Object larvaeMutex = new Object();

    /**
     *
     * @param anthill Team anthill of the ant.
     * @param variant The ant variant.
     * @param maxLarvae Maximum number of larvae which ant can carry.
     * @param hp Maximum health points of the ant.
     */
    public Ant(Anthill anthill,AntVariant variant , int maxLarvae, int hp){
        numberOfLarvae = 0;
        this.maxLarvae = maxLarvae;
        this.currentHP = hp;
        this.maxHP = hp;
        setExit(false);
        reactions = new LinkedBlockingQueue<>(1);
        setAnthill(anthill);
        setCurrentNode(getAnthill());
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

    /**
     * Method executed on its own thread.
     */
    @Override
    public void run() {
        while (!getExit()){
            Command c = getStep();
            if(c!=null){
                c.execute();
            }
            else {
                addStep(new ChooseRandomTravel(this, getCurrentNode()));
            }
        }
        WorldManager.worldManager().removeAnt(this);
    }

    /**
     *
     * @return The node on which the ant resides.
     */
    public Node getCurrentNode() {
        return currentNode;
    }

    /**
     * Set current node to the currentNode
     * @param currentNode The value to be set as a currentNode.
     */
    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    /**
     *
     * @param reactionCommand Reaction to be executed by ant.
     * @return True if adding reaction was successful, false if it was not.
     */
    public boolean addReaction(Command reactionCommand){
       return reactions.offer(reactionCommand);
    }
    private Command getReaction(int milliseconds){
        try {
            return reactions.poll(milliseconds, TimeUnit.MILLISECONDS);
        }catch (InterruptedException e){
            setExit(true);
        }
        return null;
    }
    private void clearReactions(){
       reactions.clear();
    }

    /**
     * Set true if thread should cease execution.
     * @param exit
     */
    public void setExit(boolean exit) {
        this.exit = exit;
    }

    /**
     * Check if thread should stop executing.
      * @return True if it should, otherwise false.
     */
    public boolean getExit(){
        return exit;
    }

    /**
     *  Add step to the ant movement
      * @param command
     */
    public void addStep(Command command){
        steps.add(command);
    }
    private Command getStep(){
        if(!steps.isEmpty()) return steps.remove(0);
        else return null;
    }
    public void setAbstractPosition(Vector2 abstractPosition) {
        this.abstractPosition = abstractPosition;
    }

    public Vector2 getAbstractPosition() {
        return abstractPosition;
    }

    /**
     * Get team of the ant.
     * @return String - team name.
     */
    public String getTeam() {
        return team;
    }

    private void setTeam(String team) {
        this.team = team;
    }

    /**
     * Clear stepQueue.
     */
    public void clearSteps(){
        steps.clear();
    }

    /**
     * Get home node of the ant.
     * @return Node of type Anthill representing ant's team home.
     */
    public Anthill getAnthill() {
        return anthill;
    }

    private void setAnthill(Anthill anthill) {
        this.anthill = anthill;
    }

    /**
     * Matches AntVariant behaviour to node type, sending related commands to the node. Then waits for the reaction by the node and executes it.
     * @param node The node that was reached
     */
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

    /**
     * Description of the ant.
     * @return Description string.
     */
    @Override
    public String toString() {
        String ret= getAntName() + " is a " + getTeam()+ " "+variant.getVariantName() +   "\n";
        ret += "HP: "+getCurrentHP()+"/"+getMaxHP()+"\n";
        if(numberOfLarvae>0) ret += "The ant is carrying " +numberOfLarvae+"/"+maxLarvae + " larvae.\n";
        if(!steps.isEmpty()){
            StringBuilder stepString = new StringBuilder("His current route is: \n");
            for (int i = 0; i < java.lang.Math.min(steps.size(),2);++i) {
                stepString.append(steps.get(i).toString()).append("\n");
            }
            if(steps.size()>2) stepString.append("...\n");
            ret +=stepString;
        }
        
        return ret;
    }

    /**
     * Sent name of the ant.
     * @param antName
     */
    public void setAntName(String antName) {
        this.antName = antName;
    }

    /**
     * Get ant name.
     * @return
     */
    public String getAntName() {
        return antName;
    }

    /**
     *
     * @return -1 if larvae was picked, 0 if the larvae was picked and Ant reached its maxLarvae, 1 if ant was not able to pick larvae
     */
    public int pickLarvae(){
        synchronized (larvaeMutex) {
            if (numberOfLarvae + 1 < maxLarvae) {
                numberOfLarvae++;
                return -1;
            } else if (numberOfLarvae + 1 == maxLarvae) {
                numberOfLarvae++;
                return 0;
            } else return 1;
        }
    }

    /**
     *
     * @return The number of larvae dropped.
     */
    public int dropLarva(){
        synchronized (larvaeMutex) {
            int ret = numberOfLarvae;
            numberOfLarvae = 0;
            return ret;
        }
    }

    /**
     *  Reduce Ant HP by 1. If HP reaches 0, setExit to true.
     */
    public void takeDamage(){
        if(--currentHP <= 0 ){
            ReflectiveCommand<Node> c = new DropLarvae(this);
            c.setReflector(getCurrentNode());
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