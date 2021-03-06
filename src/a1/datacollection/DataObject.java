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

    private int totalTurns;
    private int totalFood;
    private int warriorCount;
    private int carrierCount;
    private int scoutCount;
    private int totalAnts;
    private float attackRate;
    private int foodAntRatio;
    private ILocationInfo enemyQueenSpotted;
    private int enemyQueenSpottedTurn;
    private int mapExplored;

    public DataObject() {
    }

    public DataObject(int turns, int totalFood, int warriorCount, int carrierCount, int scoutCount, int totalAnts, float attackRate, int foodAntRatio, ILocationInfo enemyQueenSpotted, int enemyQueenSpottedTurn, int mapExploredProcentage) {

        this.totalTurns = turns;
        this.totalFood = totalFood;
        this.warriorCount = warriorCount;
        this.carrierCount = carrierCount;
        this.scoutCount = scoutCount;
        this.totalAnts = totalAnts;
        this.attackRate = attackRate;
        this.foodAntRatio = foodAntRatio;
        this.enemyQueenSpotted = enemyQueenSpotted;
        this.enemyQueenSpottedTurn = enemyQueenSpottedTurn;
        this.mapExplored = mapExploredProcentage;
    }

    public int getTotalFood() {
        return totalFood;
    }

    public int getTurn() {
        return totalTurns;
    }

    public int getWarriorCount() {
        return warriorCount;
    }

    public int getCarrierCount() {
        return carrierCount;
    }

    public int getScoutCount() {
        return scoutCount;
    }

    public int getTotalAnts() {
        return totalAnts;
    }

    public float getAttackRate() {
        return attackRate;
    }

    public int getFoodAntRatio() {
        return foodAntRatio;
    }

    public ILocationInfo getEnemyQueenSpotted() {
        return enemyQueenSpotted;
    }

    public int getEnemyQueenSpottedTurn() {
        return enemyQueenSpottedTurn;
    }

    public int getMapExplored() {
        return mapExplored;
    }

}
