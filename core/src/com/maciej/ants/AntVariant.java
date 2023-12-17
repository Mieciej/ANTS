package com.maciej.ants;

import com.maciej.ants.commands.Command;
import com.maciej.ants.commands.ReflectiveCommand;

import java.util.ArrayList;
import java.util.HashMap;

public class AntVariant {
    public ArrayList<ReflectiveCommand<Node>> matchNodeToCommands(Node node){
        return mapping.getOrDefault(node.getNodeType(),mapping.get("DEFAULT"));

    }
    private HashMap<String, ArrayList<ReflectiveCommand<Node>>> mapping;

    public void setMapping(HashMap<String, ArrayList<ReflectiveCommand<Node>>> mapping) {
        this.mapping = mapping;
    }
}
