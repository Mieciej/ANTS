package com.maciej.ants;

import com.maciej.ants.commands.Command;
import com.maciej.ants.commands.ReflectiveCommand;

import java.util.ArrayList;
import java.util.HashMap;

public class AntVariant {
    private String variantName;
    public AntVariant(String name){ setVariantName(name);}
    public ArrayList<ReflectiveCommand<Node>> matchNodeToCommands(Node node){
        return mapping.getOrDefault(node.getNodeType(),mapping.get("DEFAULT"));

    }
    private HashMap<String, ArrayList<ReflectiveCommand<Node>>> mapping;

    public void setMapping(HashMap<String, ArrayList<ReflectiveCommand<Node>>> mapping) {
        this.mapping = mapping;
    }

    private void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getVariantName() {
        return variantName;
    }
}
