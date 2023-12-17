package com.maciej.ants;

import com.maciej.ants.commands.Command;
import com.maciej.ants.commands.ReflectiveCommand;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Node  {
    private ArrayList<Ant> antRegister;
    private ArrayList<AbstractMap.SimpleEntry<Ant, Command>> commandQueue;
    private final Object  commandMutex = new Object();
    private final Object  registrationMutex = new Object();
    private String nodeName;
    private int larvaCount;
    private final Object larvaMutex = new Object();
    private String nodeType;
    public void addCommand(Ant sender,Command command){
        synchronized (commandMutex){
               commandQueue.add(new AbstractMap.SimpleEntry<Ant, Command>(sender,command));
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
           synchronized (commandMutex){
               for (int i = 0; i < commandQueue.size(); i++) {
                   if(commandQueue.get(i).getKey() == ant) commandQueue.remove(i);
               }
           }
       }
    }
    public boolean sendReactionToOtherAnt(Ant invokingAnt, ReflectiveCommand<Ant> e){
        synchronized (registrationMutex) {
            for (Ant ant : antRegister)
            {
                if(ant!=invokingAnt&&!ant.getTeam().equals(invokingAnt.getTeam())){
                   e.setReflector(ant);
                   return ant.addReaction(e);
                }
            }
        }
        return  false;
    }

    public Node(){
        commandQueue = new ArrayList<>();
        antRegister = new ArrayList<>();
        larvaCount = ThreadLocalRandom.current().nextInt(3);
        String[] militaryBaseNames = {
                "Fort Formica",
                "Camp Carapace",
                "Antarctica Barracks",
                "Bunker Beetle",
                "Citadel Crawler",
                "Hive Hq",
                "Stronghold Stinger",
                "Outpost Ovipositor",
                "Nest Nexus",
                "Colony Citadel",
                "Guardian Garrison",
                "Swarm Station",
                "Armory Anthill",
                "Chitin Citadel",
                "Soldier Strongpoint",
                "Pincer Point",
                "Insect Infirmary",
                "Mandible Barricade",
                "Sting Stronghold",
                "Troop Termite",
                "Bivouac Bumble",
                "Crusader Chrysalis",
                "Harbor Hornet",
                "Bolt Borer Base",
                "Buzz Brigade Barracks",
                "Centipede Compound",
                "Dragonfly Den",
                "Exoskeleton Encampment",
                "Fury Formicary",
                "Guerilla Grub Grounds",
                "Hornet Haven",
                "Infantry Insectarium",
                "Jungle Jumper Junction",
                "Kaleidoscope Keep",
                "Larva Lookout",
                "Mantis Mansion",
                "Nebula Nest",
                "Omega Ootheca Outpost",
                "Pupa Perimeter",
                "Quasar Quarters",
                "Reconnaissance Roost",
                "Scout Swarm Stronghold",
                "Tarantula Terminus",
                "Underground Ulcer",
                "Vanguard Vespiary",
                "Wasp Watchpost",
                "Xenon Xerophyte Xenodrome",
                "Yonder Yellowjacket Yard",
                "Zero-zone Zooid Zenith",
                "Ambush Anthill",
                "Barricade Beetle Base",
                "Cocoon Citadel",
                "Dusk Drone Depot",
                "Emerald Exoskeleton Encampment",
                "Frontline Fly Fortress",
                "Gamma Gnat Garrison",
                "Hazard Hive",
                "Imperial Insectarium",
                "Juggernaut Jumper Junction",
                "Kaleidoscope Keep",
                "Lair Lepidoptera",
                "Militant Millipede Motel",
                "Nectar Nexus",
                "Ovipositor Outpost",
                "Patrol Pheromone Point",
                "Quasar Quarters",
                "Rapid Roach Redoubt",
                "Strike Scorpion Station",
                "Thunder Termite Trench",
                "Underground Utopia",
                "Vortex Vespiary",
                "Warden Wasp Watchpost",
                "Xenon Xerophyte Xenodrome",
                "Yellowjacket Yard",
                "Zero-zone Zooid Zenith"
        };
        setNodeType("STONE");
        setNodeName(militaryBaseNames[ThreadLocalRandom.current().nextInt(militaryBaseNames.length)]);
    }
    public void update() {
        synchronized (commandMutex) {
            if (!commandQueue.isEmpty()) {
                commandQueue.remove(0).getValue().execute();
            }
        }
    }
    public boolean pickLarvae()
    {
        synchronized (larvaMutex){
            if(larvaCount>0)
            {
                larvaCount--;
                return true;
            }
            else return false;
        }
    }
    public void leaveLarvae(int count){
       synchronized (larvaMutex)
       {
           larvaCount+=count;
       }
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(getNodeName()).append("\n");
        if(larvaCount>0) ret.append("There is ").append(larvaCount).append(" larvae.\n");
        if(antRegister.isEmpty()) return  ret.toString();
        ret.append("Ants:\n");
        for (int i = 0; i < java.lang.Math.min(antRegister.size(),3); i++) {
           ret.append(antRegister.get(i).getAntName()).append("\n");
        }
        if(antRegister.size() > 3)
            ret.append("...\n");
        return  ret.toString();
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
}
