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
import a1.astar_martin.AStar_Martin;
import java.util.List;

/**
 *
 * @author Silas
 */
public class CarrierAI extends GeneralAI implements IAntAI {

    //Pathfinding
    private AStar_Martin AStarPathFinder = null;
    private TheHive hive = null;
    
    private ILocationInfo queenLoc = null;

    @Override
    public void onHatch(IAntInfo thisAnt, ILocationInfo thisLocation, int worldSizeX, int worldSizeY) {
        hive = getHiveInstance();
        hive.makeMap(worldSizeX, worldSizeY);
        AStarPathFinder = getAStarInstance();
//        hive.updateAnts();
    }

    @Override
    public void onStartTurn(IAntInfo thisAnt, int turn) {
        queenLoc = hive.getCurrPos();
    }

    @Override
    public EAction chooseAction(IAntInfo thisAnt, ILocationInfo thisLocation, List<ILocationInfo> visibleLocations, List<EAction> possibleActions) {
        EAction action = null;

        //       #1 Survival
        action = survival(thisAnt, possibleActions);

        //       #2 Gather
        if (thisAnt.getFoodLoad() < 15 && action == null) {
            action = pickUpFood(thisAnt, possibleActions, visibleLocations);
        } else if (action == null) {
            action = returnFood(thisAnt, queenLoc, worldMap, possibleActions);
        }
        //       #3 Dig
        if (action == null) {
            action = dig(possibleActions, visibleLocations);
        }
        //       #4 Drop
        if (action == null) {
            action = dropSoil(possibleActions, thisAnt, visibleLocations);
        }
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
        System.out.println("I CAN'T LAY EGGS LOL");
    }

    @Override
    public void onAttacked(IAntInfo thisAnt, int dir, IAntInfo attacker, int damage) {
        getAttacker(thisAnt, dir, attacker);
    }

    @Override
    public void onDeath(IAntInfo thisAnt) {
//        hive.updateAnts();
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
