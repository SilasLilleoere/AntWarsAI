/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1.astar_martin;

/**
 *
 * @author Tobias Grundtvig
 */
public class Edge
{
    private final Node begin;
    private final Node end;
    private final double weight;

    public Edge(Node begin, Node end, double weight)
    {
        this.begin = begin;
        this.end = end;
        this.weight = weight;
        this.begin.addEdge(this);
    }

    public Node getBegin()
    {
        return begin;
    }

    public Node getEnd()
    {
        return end;
    }

    public double getWeight()
    {
        return weight;
    }
}
