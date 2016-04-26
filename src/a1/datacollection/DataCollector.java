/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1.datacollection;

import aiantwars.EAntType;
import aiantwars.IAntInfo;
import aiantwars.ILocationInfo;

/**
 *
 * @author martins
 */

//Start State
//Save Food State
//Defend State
//Explore State
//SearchAndDestroy State


public class DataCollector {
      //Cal is short for "Calculate".
    
    //---------------Map-----------------------
    private int mapExplored = 0;

    //---------------enemyQueen----------------
    //location of where enemy queen was last spotted at on what turn/time.
    private ILocationInfo QueenLastSpottedLoc;
    private int QueenSpottedTurn;
    private ILocationInfo enemyStartPos;
    
    //---------------OurQueen------------------
    
    private ILocationInfo queenStartPos;
    
    //---------------AntsCount-----------------
    private int warriorCount = 0;
    private int carrierCount = 0;
    private int scoutCount = 0;
    private int totalAnts = 0;

    //---------------AttackRate------------------
    //attackrate is totalAttacks devided with totalTurns/attackConstant.
    private int attackRate = 0;

    private int attackConstant = 0;
    private int totalTurns = 0;
    private int totalAttacks;

    //--------------FoodResources----------------
    private int foodAntRatio = 0;
    private int totalFood = 0;

    public void calAttackRate() {
        attackRate = totalAttacks / (totalTurns / attackConstant);
    }

    public void calFoodRatio() {
        foodAntRatio = totalFood / totalAnts;
    }

    public void incrementAttacks() {
        totalAttacks++;
    }

    public void updateFood(Boolean hasEaten) {
        if (!hasEaten) {
            totalFood++;
        } else if (hasEaten) {
            totalFood--;
        }
    }
    
    public DataObject getDataObject(){
    DataObject o = new DataObject(warriorCount, carrierCount, scoutCount, totalAnts, attackRate, foodAntRatio, QueenLastSpottedLoc, QueenSpottedTurn, mapExplored);
    return o;
    }

    //Updates Ants: subtracked is ant died. added if ant hatches.
    public void updateAnts(IAntInfo thisAnt, boolean isAlive) {

        String TheAntType = thisAnt.getAntType().getTypeName();
        switch (TheAntType) {
            case "WARRIOR": {
                int cal = (isAlive) ? 1 : -1;
                warriorCount += cal;
            }
            case "CARRIER": {
                int cal = (isAlive) ? 1 : -1;
                carrierCount += cal;
            }
            case "SCOUT": {
                int cal = (isAlive) ? 1 : -1;
                scoutCount += cal;
            }
        }
        if (isAlive) {
            totalAnts++;
        } else if (!isAlive) {
            totalAnts--;
        }
    }

    public static void main(String[] args) {
        DataCollector s = new DataCollector();
    }
}
