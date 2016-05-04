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
import a1.datacollection.DataObject;
import java.util.List;

/**
 *
 * @author Silas
 */
public class QueenAI extends GeneralAI implements IAntAI {

    @Override
    public void onHatch(IAntInfo thisAnt, ILocationInfo thisLocation, int worldSizeX, int worldSizeY) {
        hive = new TheHive(worldSizeX, worldSizeY);
        hive.makeMap(worldSizeX, worldSizeY); //only queen should make map
        hive.setStartPos(thisAnt.getLocation());
        hive.updateAnts(thisAnt, true);
    }

    @Override
    public void onStartTurn(IAntInfo thisAnt, int turn) {
        DataObject d = hive.getData();
        // System.out.println("TotalAnts: " + d.getTotalAnts() + " TotalFood:" + d.getTotalFood());
    }

    @Override
    public EAction chooseAction(IAntInfo thisAnt, ILocationInfo thisLocation, List<ILocationInfo> visibleLocations, List<EAction> possibleActions) {

        //--------------------------------------------
        pA = possibleActions;
        visLoc = visibleLocations;
        hive.updateMap(visibleLocations);
        EAction action = null;
        //---------------------------------------------

        //Update current Queen location, so ants can A* to her.
        hive.setCurrPos(thisAnt.getLocation());

        //#1 Survival
        action = survival(thisAnt);

        //#2 Expand
        if (action == null && !goingHome) {
            action = layEgg(thisAnt);
        }

        //#3 Gather
        if (action == null && !goingHome) {
            action = pickUpFood(thisAnt);
            if (action != null) {
                //  System.out.println("Queen: Gather");
            }
        }

        //#4 Scout
        if (action == null && !goingHome) {
            action = explore(thisAnt);
            if (action != null) {
                //  System.out.println("Queen: Scout");
            }
        }

        if (action == null) {
            action = EAction.Pass;
        }

        // System.out.println("Action: " + action.toString());
        return action;
    }

    @Override
    public void onLayEgg(IAntInfo thisAnt, List<EAntType> types, IEgg egg) {
        EAntType type = hive.getEggType(types);
        IAntAI AI = null;

        System.out.println("eggType: " + type.getTypeName());
//
        switch (type.getTypeName()) {
            case "Carrier": {
                AI = new CarrierAI(hive);
                break;
            }
            case "Scout": {
                AI = new ScoutAI(hive);
                break;
            }
            case "Warrier": {
                System.out.println("warriorAI being made");
                AI = new WarriorAI(hive);
                break;
            }
        }
        //0 = Carrier
        //1 = Scout
        //2 = warrior
        //  type = types.get(0);
        egg.set(type, AI);
        //egg.set(type, new CarrierAI(hive));
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

    }

    @Override
    public void onStartRound(int round) {

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
