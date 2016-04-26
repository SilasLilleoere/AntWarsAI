/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1.astar_martin;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Tobias Grundtvig
 */
public class Node implements Iterable<Edge>, Comparable<Node> {
    
    // NOTE: 30-03-2016 - Jeg ændrede xPos og yPos fra double til int typer. Var originalt int i Tobiases kode.

    private final String name;
    private final int xPos;
    private final int yPos;
    private Node prev;
    private double gVal;
    private double hVal;
    private final ArrayList<Edge> edges;
    private boolean isBlock = false;

    public boolean getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    public Node(String name, int xPos, int yPos) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        prev = null;
        gVal = Double.POSITIVE_INFINITY;
        hVal = Double.POSITIVE_INFINITY;
        edges = new ArrayList<>();
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void addEdge(Edge edge) {
        if (edge != null) {
            edges.add(edge);
        }
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public double getGVal() {
        return gVal;
    }

    public void setGVal(double gVal) {
        this.gVal = gVal;
    }

    public double getFVal() {
        return gVal + hVal;
    }

    public double getHVal() {
        return hVal;
    }

    public void setHVal(double hVal) {
        this.hVal = hVal;
    }

    @Override
    public String toString() {
        return name + ": (" + xPos + ", " + yPos + ")";
    }

    @Override
    public Iterator<Edge> iterator() {
        return edges.iterator();
    }

    @Override
    public int compareTo(Node o) {
        if (this.getFVal() < o.getFVal()) {
            return -1;
        }
        if (this.getFVal() > o.getFVal()) {
            return 1;
        }
        if (this.getHVal() < o.getHVal()) {
            return -1;
        }
        if (this.getHVal() > o.getHVal()) {
            return 1;
        }
        return 0;
    }

}
