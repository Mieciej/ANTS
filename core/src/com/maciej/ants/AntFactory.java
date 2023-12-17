package com.maciej.ants;

import com.maciej.ants.commands.*;

import java.util.ArrayList;
import java.util.HashMap;

public class AntFactory {
    public static Ant getWorker(Anthill anthill){
        AntVariant variant = new AntVariant();

        Ant newAnt = new Ant(anthill,variant);
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
        variant.setMapping(matching);

        return  newAnt;
    }
}
