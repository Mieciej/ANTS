package com.maciej.ants;

import com.maciej.ants.commands.*;

import java.util.ArrayList;
import java.util.HashMap;

public class AntFactory {
    public static Ant getWorker(Anthill anthill){
        AntVariant variant = new AntVariant("WORKER");
        Ant newAnt = new Ant(anthill,variant,1,1);
        HashMap<String, ArrayList<ReflectiveCommand<Node>>> matching = new HashMap<>();
        ArrayList<ReflectiveCommand<Node>> stoneActions = new ArrayList<>();
        stoneActions.add(new Attack(newAnt,new Retreat()));
        stoneActions.add(new PickLarvae(newAnt));
        matching.put("STONE",stoneActions);
        ArrayList<ReflectiveCommand<Node>> anthillActions = new ArrayList<>();
        anthillActions.add(new DropLarvae(newAnt));
        matching.put("ANTHILL"+anthill.getTeam(),anthillActions);
        ArrayList<ReflectiveCommand<Node>> defaultAction = new ArrayList<>();
        defaultAction.add(new PickLarvae(newAnt));
        defaultAction.add(new Attack(newAnt,new Retreat()));
        matching.put("DEFAULT",defaultAction);
        ArrayList<ReflectiveCommand<Node>> leafActions = new ArrayList<>();
        leafActions.add(new PickLarvae(newAnt));
        matching.put("LEAF",leafActions);
        variant.setMapping(matching);
        return  newAnt;
    }

    public static Ant getDrone(Anthill anthill){
        AntVariant variant = new AntVariant("DRONE");
        Ant newAnt = new Ant(anthill,variant,0,1);

        HashMap<String, ArrayList<ReflectiveCommand<Node>>> matching = new HashMap<>();
        matching.put("DEFAULT",new ArrayList<ReflectiveCommand<Node>>());
        variant.setMapping(matching);
        return  newAnt;
    }
    public static Ant getSoldier(Anthill anthill){
        AntVariant variant = new AntVariant("SOLDIER");
        Ant newAnt = new Ant(anthill,variant,0,1);

        HashMap<String, ArrayList<ReflectiveCommand<Node>>> matching = new HashMap<>();

        ArrayList<ReflectiveCommand<Node>> stoneActions = new ArrayList<>();
        stoneActions.add(new Attack(newAnt,new DoNothing()));
        matching.put("STONE",stoneActions);

        matching.put("LEAF",new ArrayList<ReflectiveCommand<Node>>());

        ArrayList<ReflectiveCommand<Node>> defaultAction = new ArrayList<>();
        defaultAction.add(new Attack(newAnt,new DoNothing()));
        matching.put("DEFAULT",defaultAction);
        variant.setMapping(matching);
        return  newAnt;
    }
    public static Ant getCollector(Anthill anthill){
        AntVariant variant = new AntVariant("COLLECTOR");
        Ant newAnt = new Ant(anthill,variant,3,1);

        HashMap<String, ArrayList<ReflectiveCommand<Node>>> matching = new HashMap<>();

        ArrayList<ReflectiveCommand<Node>> anthillActions = new ArrayList<>();
        anthillActions.add(new DropLarvae(newAnt));
        matching.put("ANTHILL"+anthill.getTeam(),anthillActions);

        ArrayList<ReflectiveCommand<Node>> defaultAction = new ArrayList<>();
        defaultAction.add(new PickLarvae(newAnt));
        matching.put("DEFAULT",defaultAction);

        variant.setMapping(matching);
        return  newAnt;
    }
    public static Ant getBlunderer(Anthill anthill){
        AntVariant variant = new AntVariant("BLUNDERER");
        Ant newAnt = new Ant(anthill,variant,3,1);

        HashMap<String, ArrayList<ReflectiveCommand<Node>>> matching = new HashMap<>();

        ArrayList<ReflectiveCommand<Node>> anthillActions = new ArrayList<>();
        anthillActions.add(new DropLarvae(newAnt));
        matching.put("ANTHILL"+anthill.getTeam(),anthillActions);

        ArrayList<ReflectiveCommand<Node>> defaultAction = new ArrayList<>();
        defaultAction.add(new PickLarvae(newAnt));
        defaultAction.add(new RandomlyDropLarvae(newAnt));
        matching.put("DEFAULT",defaultAction);

        variant.setMapping(matching);
        return  newAnt;
    }
}
