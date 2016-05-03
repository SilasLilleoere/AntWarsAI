/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import aiantwars.EAction;
import aiantwars.EAntType;
import aiantwars.IAntAI;
import aiantwars.IAntInfo;
import aiantwars.IEgg;
import aiantwars.ILocationInfo;
import java.util.List;

/**
 *
 * @author Silas
 */
public class WarriorAI extends GeneralAI implements IAntAI {

    //Pathfinding

    
    public WarriorAI (TheHive hiveFromQueen){
    this.hive = hiveFromQueen;
    }

    @Override
    public void onHatch(IAntInfo thisAnt, ILocationInfo thisLocation, int worldSizeX, int worldSizeY) {
        hive.updateAnts(thisAnt, true);
        AStarPathFinder = hive.getAStarInstance();
    }

    @Override
    public void onStartTurn(IAntInfo thisAnt, int turn) {

    }

    @Override
    public EAction chooseAction(IAntInfo thisAnt, ILocationInfo thisLocation, List<ILocationInfo> visibleLocations, List<EAction> possibleActions) {
        EAction action = null;

        //       #1 Survival
        action = survival(thisAnt, possibleActions);

        //       #2 Attack
        if (action == null) {
            action = attackEnemy(thisAnt, possibleActions, visibleLocations);
        }

        //       #3 Gather
        if (thisAnt.getFoodLoad() < 2 && action == null) {
            action = pickUpFood(thisAnt, possibleActions, visibleLocations);
        }
        //       #4 Search n Destroy 
        //       Might be relocated to GeneralAI
        //  hive.searchAndDestroy();
        //  action = nextStepInSearchAndDestroy();

        //       #5 Scout
        if (action == null) {
            action = explore(possibleActions, thisAnt, visibleLocations);
        }

        if (action == null) {
            action = EAction.Pass;
        }
        return action;
    }

    @Override
    public void onLayEgg(IAntInfo thisAnt, List<EAntType> types, IEgg egg) {
        throw new UnsupportedOperationException("I CAN'T LAY EGGS LOL."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAttacked(IAntInfo thisAnt, int dir, IAntInfo attacker, int damage) {
        getAttacker(thisAnt, dir, attacker);
    }

    @Override
    public void onDeath(IAntInfo thisAnt) {
        hive.updateAnts(thisAnt, false);
    }

    @Override
    public void onStartMatch(int worldSizeX, int worldSizeY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onStartRound(int round) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onEndRound(int yourMajor, int yourMinor, int enemyMajor, int enemyMinor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onEndMatch(int yourScore, int yourWins, int enemyScore, int enemyWins) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
