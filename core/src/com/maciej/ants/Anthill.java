package com.maciej.ants;

/**
 * Represents home of the given team.
 */
public class Anthill extends Node{
    public String team;
    public Anthill(String team){
        super();
        this.team = team;
        setNodeType("ANTHILL"+team);
    }

    /**
     * Get anthill team.
     * @return the team name.
     */
    public String getTeam() {
        return team;
    }

    private void setTeam(String team) {
        this.team = team;
    }
}
