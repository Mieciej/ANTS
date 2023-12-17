package com.maciej.ants;

public class Anthill extends Node{
    public String team;
    public Anthill(String team){
        super();
        this.team = team;
        setNodeType("ANTHILL"+team);
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
