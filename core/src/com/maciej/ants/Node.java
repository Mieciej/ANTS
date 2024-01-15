package com.maciej.ants;

import com.badlogic.gdx.math.Vector2;
import com.maciej.ants.commands.Command;
import com.maciej.ants.commands.ReflectiveCommand;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents locations on the map.
 */
public class Node  implements AbstractPositionable {
    private ArrayList<Ant> antRegister;
    private Vector2 abstractPosition;
    private ArrayList<AbstractMap.SimpleEntry<Ant, Command>> commandQueue;
    private final Object commandMutex = new Object();
    private final Object registrationMutex = new Object();
    private String nodeName;
    private int larvaCount;
    private final Object larvaMutex = new Object();
    private String nodeType;

    /**
     *  Add Command to be executed during update()
     * @param sender The Ant that sends the command.
     * @param command The command.
     */
    public void addCommand(Ant sender, Command command) {
        synchronized (commandMutex) {
            commandQueue.add(new AbstractMap.SimpleEntry<Ant, Command>(sender, command));
        }
    }

    /**
     * Add ant to antRegister to allow Node to manage which ants are staying in node.
     * @param ant The ant to be registered.
     */
    public void registerAnt(Ant ant) {
        synchronized (registrationMutex) {
            antRegister.add(ant);
        }
    }

    /**
     * Remove ant from antRegister to allow Node to remove Commands which where added to commandQueue by the Ant.
     * @param ant The ant to be unregistered.
     */
    public void unregisterAnt(Ant ant) {
        synchronized (registrationMutex) {
            antRegister.remove(ant);
            synchronized (commandMutex) {
                ArrayList<Integer> indices = new ArrayList<>();
                for (int i = 0; i < commandQueue.size(); i++) {
                    if (commandQueue.get(i).getKey() == ant) indices.add(i-indices.size());
                }

                for (int index : indices) commandQueue.remove(index);
            }
        }
    }

    /**
     * Send reaction (command) to ant of opposing team to the sender.
     * @param invokingAnt The ant which is sending the reaction.
     * @param e The command to be executed by receiving ant.
     * @return True if reactor was found, false otherwise.
     */
    public boolean sendReactionToOtherAnt(Ant invokingAnt, ReflectiveCommand<Ant> e) {
        synchronized (registrationMutex) {
            for (Ant ant : antRegister) {
                if (ant != invokingAnt && !ant.getTeam().equals(invokingAnt.getTeam())) {
                    e.setReflector(ant);
                    return ant.addReaction(e);
                }
            }
        }
        return false;
    }

    public Node() {
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
        if (ThreadLocalRandom.current().nextBoolean()) setNodeType("STONE");
        else setNodeType("LEAF");
        setNodeName(militaryBaseNames[ThreadLocalRandom.current().nextInt(militaryBaseNames.length)]);
    }

    /**
     * Execute one command of commandQueue. Run every tick (frame).
     */
    public void update() {
        synchronized (commandMutex) {
            if (!commandQueue.isEmpty()) {
                commandQueue.remove(0).getValue().execute();
            }
        }
    }

    /**
     * Decreases number of larvae by 1.
     * @return True if larvae was picked, false otherwise.
     */
    public boolean pickLarvae() {
        synchronized (larvaMutex) {
            if (larvaCount > 0) {
                larvaCount--;
                return true;
            } else return false;
        }
    }

    /**
     * Increase number of larvae by count.
     * @param count
     */
    public void leaveLarvae(int count) {
        synchronized (larvaMutex) {
            larvaCount += count;
        }
    }

    private void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Get Node Name.
     * @return string - node name.
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Get description of the node.
     * @return node description.
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(getNodeName()).append("\n");
        if (larvaCount > 0) ret.append("There is ").append(larvaCount).append(" larvae.\n");
        if (antRegister.isEmpty()) return ret.toString();
        ret.append("Ants:\n");
        for (int i = 0; i < java.lang.Math.min(antRegister.size(), 2); i++) {
            ret.append(antRegister.get(i).getAntName()).append("\n");
        }
        if (antRegister.size() > 2)
            ret.append("...\n");
        return ret.toString();
    }

    /**
     * Get node type.
     * @return string - node type.
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * Set node type.
     * @param nodeType string - node type.
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public void setAbstractPosition(Vector2 abstractPosition) {
        this.abstractPosition = abstractPosition;
    }

    public Vector2 getAbstractPosition() {
        return abstractPosition;
    }
}