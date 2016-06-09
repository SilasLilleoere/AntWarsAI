/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1.astar;

import aiantwars.ILocationInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author martins
 */
public class AStar {

    static int nodesHeight = 0;
    static int nodesWidth = 0;

    public static Queue<Node> openSet = new PriorityQueue<>();
    public static Set<Node> closedSet = new HashSet<>();
    private Node start;
    private Node goal;
    private Node[][] nodes = null;

    public AStar(int worldWidth, int worldHeight) {
        this.nodesWidth = worldWidth;
        this.nodesHeight = worldHeight;
    }

    public Node[][] makeNodeArray(ILocationInfo[][] antMap) {

        nodes = null;
        //creates Nodes for nodes[][]. Adds locations with soil, rock and ants to closedSet;
        if (nodes == null) {
            Node[][] res = new Node[nodesWidth][nodesHeight];
            for (int x = 0; x < nodesWidth; x++) {
                for (int y = 0; y < nodesHeight; y++) {
                    String name = x + ":" + y;
                    res[x][y] = new Node(name, x, y);
                    //note: we also check if there is an ant in the way. If the ant is placed on the goal, 
                    //then it shouldn't be added to closedSet. 
                    if (antMap[x][y].isFilled() || antMap[x][y].isRock()) {
                        closedSet.add(res[x][y]);
                    }
                }
            }
            nodes = res;
            return nodes;
        }
//           else {
//            //adds all locations with soil and rock to closedSet.
//            for (int i = 0; i < antMap.length; i++) {
//                for (int j = 0; j < antMap[i].length; j++) {
//                    if (antMap[i][j].isFilled() || antMap[i][j].isRock()) {
//                        if (!closedSet.contains(nodes[i][j])) {
//                            closedSet.add(nodes[i][j]);
//                        }
//                    }
//                }
//            }
//        }
        return nodes;
    }

    //G-Cost = How far away current node is from start node.
    //H-cost = How far away current node is from end node.
    //F-cost = G + H
    public ArrayList<ILocationInfo> findShortestPath(ILocationInfo startLoc, ILocationInfo goalLoc, ILocationInfo[][] antMap) {

        //System.out.println("startLoc: " + startLoc.getX() + "   goalLoc: " + goalLoc.getX());
        if (startLoc == goalLoc) {
            return null;
        }

        closedSet.clear();
        openSet.clear();

        //Fills the 2D array 'nodes' with Nodes and adds locations that is isFilled or IsRock to closedSet. 
        nodes = makeNodeArray(antMap);
        //System.out.println("nodes:" + nodes.length);

        start = nodes[startLoc.getX()][startLoc.getY()];
        goal = nodes[goalLoc.getX()][goalLoc.getY()];

        start.setGVal(0);

        openSet.add(start);

        Node currentNode = openSet.poll();

        while (true) {

            closedSet.add(currentNode);
            calculateNeighbours(currentNode);

            for (Edge edge : currentNode) {
                if (edge.getEnd() == null) {
                    continue;
                }
                Node nextNode = edge.getEnd();

                //If nextNode is in closedSet, move on to next edge in the loop
                if (closedSet.contains(nextNode)) {
                    continue;
                }

                double newG = edge.getWeight() + currentNode.getGVal();
                if (nextNode.getGVal() > newG) {
                    nextNode.setGVal(newG);
                    nextNode.setPrev(currentNode);

                }
                if (!openSet.contains(nextNode)) {
                    nextNode.setHVal(getMinimumDist(nextNode, goal));
                    openSet.add(nextNode);

                }
            }

            currentNode = openSet.poll();

            if (currentNode == goal) {
                // System.out.println("Reached goal!");
                break;
            } else if ((currentNode == null) || openSet.isEmpty()) {
                return null;
            }
        }
        return reconstruct_path(currentNode, antMap);
    }

    public void calculateNeighbours(Node currentNode) {

        if (currentNode == null) {
            return;
        }
        int x = (int) currentNode.getXPos();
        int y = (int) currentNode.getYPos();

        int xn;
        int yn;
        Edge edge = null;

        //North
        xn = x;
        yn = y + 1;
        edge = createEdge(currentNode, xn, yn);
        currentNode.addEdge(edge);

//        //North-East
//        xn = x + 1;
//        yn = y + 1;
//        edge = createEdge(currentNode, xn, yn);
//        currentNode.addEdge(edge);
//
//        //North-West
//        xn = x - 1;
//        yn = y + 1;
//        edge = createEdge(currentNode, xn, yn);
//        currentNode.addEdge(edge);
        //East
        xn = x + 1;
        yn = y;
        edge = createEdge(currentNode, xn, yn);
        currentNode.addEdge(edge);

        //West
        xn = x - 1;
        yn = y;
        edge = createEdge(currentNode, xn, yn);
        currentNode.addEdge(edge);

        //South
        xn = x;
        yn = y - 1;
        edge = createEdge(currentNode, xn, yn);
        currentNode.addEdge(edge);
//

    }

    public Edge createEdge(Node currentNode, int x, int y) {

        if (x < 0 || y < 0 || x >= nodesWidth || y >= nodesHeight) {
            return null;
        }
        Node neighbourNode = nodes[x][y];
        Edge e = null;

        if (neighbourNode != null) {
            e = new Edge(currentNode, neighbourNode, getMinimumDist(currentNode, neighbourNode));
        }

        return e;
    }

    //This method starts with creating an ArrayList<Node> with the path. 
    //It then converts it to a ArrayList<ILocationInfo> and returns that.
    public ArrayList<ILocationInfo> reconstruct_path(Node currentNode, ILocationInfo[][] antMap) {

        ArrayList<Node> res = new ArrayList<>();
        do {
            res.add(currentNode);
            currentNode = currentNode.getPrev();
        } while (currentNode != null);
        Collections.reverse(res);
        //We have to convert the node arrayList to ILocationInfo arrayList.
        return convertToILocations(res, antMap);
    }

    public double getMinimumDist(Node a, Node b) {
        double dx = b.getXPos() - a.getXPos();
        double dy = b.getYPos() - a.getYPos();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public ArrayList<ILocationInfo> convertToILocations(ArrayList<Node> nodesArray, ILocationInfo[][] antMap) {

        ArrayList<ILocationInfo> finalPath = new ArrayList();

        for (Node n : nodesArray) {
            finalPath.add(antMap[n.getXPos()][n.getYPos()]);
        }
        return finalPath;
    }

}
