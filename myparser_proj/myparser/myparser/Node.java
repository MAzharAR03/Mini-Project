package myparser;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.List;  
import java.util.ArrayList;
/**
 *
 * @author MUHAMMADAZHARBINAHMA
 */
public class Node {
    public String value;
    public String type;
    public int line;
    public List<Node> children; 


    public Node(String value) {
        this(value, null, -1);
    }

    public Node(String value, String type) {
        this(value, type, -1);
    }

    public Node(String value, String type, int line) {
        this.value = value;
        this.type = type;
        this.line = line;
        this.children = new ArrayList<>();
    }

    public void addChild(Node child) {  
        if (child != null) {  
            children.add(child);  
        }  
    }  
    // Method to print the tree  
    public void printTree(String indent) {
        String label = value;
        if (type != null) {
            label += " (" + type + ")";
        }
        System.out.println(indent + label);
        for (Node child : children) {
            child.printTree(indent + " ");
        }
    }
} 
