package com.maciej.ants;

import com.maciej.ants.commands.Command;
import com.maciej.ants.commands.ReflectiveCommand;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents type of the Ant.
 */
public class AntVariant {
    private String variantName;
    public AntVariant(String name){ setVariantName(name);}

    /**
     * Given the node matches commands related to it.
     * @param node Node to match list of commands
     * @return list of commands to be executed on the node.
     */
    public ArrayList<ReflectiveCommand<Node>> matchNodeToCommands(Node node){
        return mapping.getOrDefault(node.getNodeType(),mapping.get("DEFAULT"));

    }
    private HashMap<String, ArrayList<ReflectiveCommand<Node>>> mapping;

    /**
     * Set variant mapping, i.e. node-command mapping.
     * @param mapping Node-command mapping (HashMap).
     */
    public void setMapping(HashMap<String, ArrayList<ReflectiveCommand<Node>>> mapping) {
        this.mapping = mapping;
    }

    private void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    /**
     * Get variant name.
     * @return string - variant name.
     */
    public String getVariantName() {
        return variantName;
    }
}
