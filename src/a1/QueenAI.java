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
public class QueenAI extends GeneralAI implements IAntAI {
    
    

    //Pathfinding
    private AStar_Martin AStarPathFinder = null;
    private TheHive hive = null;

    
    @Override
    public void onHatch(IAntInfo thisAnt, ILocationInfo thisLocation, int worldSizeX, int worldSizeY) {
        this.worldSizeX = worldSizeX;
        this.worldSizeY = worldSizeY;
        hive = getHiveInstance();
        hive.makeMap(worldSizeX, worldSizeY);
        hive.setStartPos(thisAnt.getLocation());
        AStarPathFinder = getAStarInstance();
        
        //hive.updateAnts(true);
    }

    @Override
    public void onStartTurn(IAntInfo thisAnt, int turn) {
        
        //Where am I on the map?
        //Should I go home?
    }

    @Override
    public EAction chooseAction(IAntInfo thisAnt, ILocationInfo thisLocation, List<ILocationInfo> visibleLocations, List<EAction> possibleActions) {
        EAction action = null;
        hive.updateMap(visibleLocations);
        worldMap = hive.getMap();        
        
        //Update current Queen location, so ants can A* to her.
        hive.setCurrPos(thisAnt.getLocation());
        
        //#1 Survival
        action = survival(thisAnt, possibleActions);
        
        //#2 Expand
        if(action == null && !goingHome){
        action = layEgg(thisAnt, possibleActions, visibleLocations);
        }
        
        //#3 Gather
        if(action == null && !goingHome){
        action = pickUpFood(thisAnt, possibleActions, visibleLocations);
        }
        
        //#4 Scout
        if(action == null && !goingHome){
        action = explore(possibleActions, thisAnt, visibleLocations);
        }
        
        if(goingHome){
        action = moveTo(thisAnt, hive.getStartPos(), worldMap);
        }        
        
        if(action == null){
            action = EAction.Pass;
        }       
        
        return action;
    }

    @Override
    public void onLayEgg(IAntInfo thisAnt, List<EAntType> types, IEgg egg) {
        EAntType type;
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

//--------------------------------------------------
//--------------- TEMP WARRRIOOOORRZ---------------------
//--------------------------------------------------
    type = types.get(2);
    egg.set(type, new WarriorAI());    
    }

    @Override
    public void onAttacked(IAntInfo thisAnt, int dir, IAntInfo attacker, int damage) {
        getAttacker(thisAnt, dir, attacker);
        goingHome = true;
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
        resetHive();
    }

    @Override
    public void onEndMatch(int yourScore, int yourWins, int enemyScore, int enemyWins) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
