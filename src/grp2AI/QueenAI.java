/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grp2AI;

import aiantwars.EAction;
import aiantwars.EAntType;
import aiantwars.IAntAI;
import aiantwars.IAntInfo;
import aiantwars.IEgg;
import aiantwars.ILocationInfo;
import astar_martin.AStar_Martin;
import java.util.List;

/**
 *
 * @author Silas
 */
public class QueenAI extends GeneralAI implements IAntAI {

    //Pathfinding
    private AStar_Martin AStarPathFinder = null;
    private TheHive hive = null;

    @Override
    public void onHatch(IAntInfo thisAnt, ILocationInfo thisLocation, int worldSizeX, int worldSizeY) {
        hive = TheHive.getHiveInstance();
        hive.makeMap(worldSizeX, worldSizeY);
        AStarPathFinder = TheHive.getAStarInstance();
//        hive.updateAnts();
    }

    @Override
    public void onStartTurn(IAntInfo thisAnt, int turn) {
        
    }

    @Override
    public EAction chooseAction(IAntInfo thisAnt, ILocationInfo thisLocation, List<ILocationInfo> visibleLocations, List<EAction> possibleActions) {
        EAction action = null;
        
        
        //#1 Survival
        action = survival(thisAnt, possibleActions);
        
        //#2 Expand
        action = layEgg(thisAnt, possibleActions);
        
        //#3 Gather
        action = pickUpFood(thisAnt, possibleActions);
        
        //#4 Scout
        action = explore(possibleActions, thisAnt, visibleLocations);
        
        
        if(action == null){
            action = EAction.Pass;
        }
        return action;
    }

    @Override
    public void onLayEgg(IAntInfo thisAnt, List<EAntType> types, IEgg egg) {
//        EAntType type;
//        type = policies.getEggType();
//        switch (type.getTypeName()){
//            case "CARRIER": {
//                egg.set(type, Carrier);
//            }case "SCOUT": {
//                egg.set(type, Scout);
//            }case "WARRIOR": {
//                egg.set(type, Warrior);
//            }
//        }
    }

    @Override
    public void onAttacked(IAntInfo thisAnt, int dir, IAntInfo attacker, int damage) {
        getAttacker(thisAnt, dir, attacker);
    }

    @Override
    public void onDeath(IAntInfo thisAnt) {
//        hive.updateAnts();
    }

}
