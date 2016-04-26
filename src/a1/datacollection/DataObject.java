/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1.datacollection;

import aiantwars.ILocationInfo;

/**
 *
 * @author martins
 */
public class DataObject {

    private int warriorCount;
    private int carrierCount;
    private int scoutCount;
    private int totalAnts;
    private int attackRate;
    private int foodAntRatio;
    private ILocationInfo QueenLastSpottedLoc;
    private int QueenSpottedTurn;
    private int mapExplored;

    public DataObject() {
    }

    public DataObject(int warriorCount, int carrierCount, int scoutCount, int totalAnts, int attackRate, int foodAntRatio, ILocationInfo QueenLastSpotted, int QueenSpottedTurn, int mapExploredProcentage) {
        this.warriorCount = warriorCount;
        this.carrierCount = carrierCount;
        this.scoutCount = scoutCount;
        this.totalAnts = totalAnts;
        this.attackRate = attackRate;
        this.foodAntRatio = foodAntRatio;
        this.QueenLastSpottedLoc = QueenLastSpotted;
        this.QueenSpottedTurn = QueenSpottedTurn;
        this.mapExplored = mapExploredProcentage;
    }

}
