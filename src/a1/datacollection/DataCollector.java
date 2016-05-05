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
    //location of where enemy queen was last spotted and on what turn/time.
    private ILocationInfo enemyQueenSpotted;
    private int enemyQueenSpottedTurn;
    private ILocationInfo enemyStartPos;

    //---------------OurQueen------------------
    private ILocationInfo queenStartPos;

    //---------------AntsCount-----------------
    private int warriorCount = 0;
    private int carrierCount = 0;
    private int scoutCount = 0;
    private int totalAnts = 0;

    //---------------AttackRate------------------
    private float attackRate = 0;
    private int attackConstant = 10;
    private int totalTurns = 0;
    private int totalAttacks;
    private int queenAttackedAtTurn = -1;

    //--------------FoodResources----------------
    private int foodAntRatio = 0;
    private int totalFood = 0;

    public void setTotalTurns(int turns) {
        this.totalTurns = turns;

    }

//    public void calAttackRate() {
//        if (totalTurns != 0 && totalAttacks != 0) {
//            attackRate = totalAttacks / (totalTurns / attackConstant);
//        }
//    }
    public void updateAttackRate(boolean wasAttacked) {

        if (wasAttacked) {
            attackRate += 1.5f;
        } else {
            attackRate -= 0.2f;
        }

        if (attackRate < 0) { //attackRate should never be under zero.
            attackRate = 0;
        }
        if (attackRate > 20) { //cap attackRate at 20.
            attackRate = 20;
        }

//        DataObject data = getDataObject();
//        System.out.println("-----------------------------------------------");
//        System.out.println("Current AttackedRate: " + data.getAttackRate());
//        System.out.println("Current Turn: " + data.getTurn());
//        System.out.println("Current totalAttacks: " + totalAttacks);
//        System.out.println("-----------------------------------------------");

    }

    public void calFoodRatio() {
        if (totalAnts != 0 && totalFood != 0) {
            foodAntRatio = totalFood / totalAnts;
        }
    }

    public void updateAttacks() {
        totalAttacks++;
    }

    //Note: food that as been added to one of the food-supply locations, will be
    //considered a part of the totalFood supply for the ants.
    public void updateFood(Boolean hasEaten) {
        if (!hasEaten) {
            totalFood++;
        } else if (hasEaten) {
            totalFood--;
        }
    }

    public DataObject getDataObject() {
        calFoodRatio();
        DataObject o = new DataObject(
                totalTurns,
                totalFood,
                warriorCount,
                carrierCount,
                scoutCount,
                totalAnts,
                attackRate,
                foodAntRatio,
                enemyQueenSpotted,
                enemyQueenSpottedTurn,
                mapExplored
        );
        return o;
    }

    //Updates Ants: subtracked is ant died. added if ant hatches.
    public void updateAnts(String antType, boolean isAlive) {

        //----------------------debug-------------------------------
        // DataObject data = getDataObject();
//        System.out.println("Current scouts " + data.getScoutCount());
//        System.out.println("Current carriers: " + data.getCarrierCount());
//        System.out.println("Current warriors: " + data.getWarriorCount());
//        System.out.println("Current TotalAnts: " + data.getTotalAnts());
//        System.out.println("------------------------------------------------");
        //----------------------debug-------------------------------
        String TheAntType = antType;
        switch (TheAntType) {
            case "Warrier": { //warrior return of getAntType() is spelled wrong: warrier
                System.out.println("warrior bliver talt");
                int cal = (isAlive) ? 1 : -1;
                warriorCount += cal;
                break;
            }
            case "Carrier": {
                int cal = (isAlive) ? 1 : -1;
                carrierCount += cal;
                break;
            }
            case "Scout": {
                int cal = (isAlive) ? 1 : -1;
                scoutCount += cal;
                break;
            }
        }
        if (isAlive) {
            totalAnts++;
        } else if (!isAlive) {
            totalAnts--;
        }
    }

    public void setEnemyQueenSpotted(ILocationInfo loc) {
        enemyQueenSpotted = loc;
        enemyQueenSpottedTurn = totalTurns;
    }
}
