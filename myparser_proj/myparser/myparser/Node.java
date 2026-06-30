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
    public List<Node> children; 
    
    public Node(String value) {  
        this.value = value;  
        this.children = new ArrayList<>();  
    } 
    
    public void addChild(Node child) {  
        if (child != null) {  
            children.add(child);  
        }  
    }  
    // Method to print the tree  
    public void printTree(String indent) {  
        System.out.println(indent + value);  
        for (Node child : children) {  
            child.printTree(indent + " ");  
        }  
    }  
} 
