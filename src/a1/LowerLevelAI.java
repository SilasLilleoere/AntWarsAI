/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import a1.astar_martin.AStar_Martin;
import aiantwars.EAction;
import aiantwars.IAntInfo;
import aiantwars.ILocationInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Silas og Martin
 */
public class LowerLevelAI {

    String direction = "";
    List<ILocationInfo> visLoc; // visibleLocations
    List<EAction> pA; //possibleActions

    ILocationInfo enemyQueenLoc = null; //used by warrior only
    ILocationInfo goal = null;
    ILocationInfo digLoc = null;
    EAction turn = null;
    ArrayList turnList = null;
    ArrayList dirList = null;

    List<EAction> turnTo = new ArrayList();
    Random ran = new Random();

    TheHive hive = null;
    AStar_Martin AStarPathFinder = null;

    public IAntInfo getAnt() {
        IAntInfo ant = null;

        if (!visLoc.isEmpty()) {
            ant = visLoc.get(0).getAnt();
        }
        return ant;
    }

    public void turnToDir(int dir, IAntInfo thisAnt) {

        if (thisAnt.getDirection() == 0 && dir == 3) {
            turnTo.add(EAction.TurnLeft);
        } else if (thisAnt.getDirection() == 3 && dir == 0) {
            turnTo.add(EAction.TurnRight);
        } else if (thisAnt.getDirection() == dir + 2 || thisAnt.getDirection() == dir - 2) {
            turnTo.add(EAction.TurnRight);
            turnTo.add(EAction.TurnRight);
        } else if (thisAnt.getDirection() > dir) {
            turnTo.add(EAction.TurnLeft);
        } else if (thisAnt.getDirection() < dir) {
            turnTo.add(EAction.TurnRight);
        }

        //if ant is facing dir, then clear turnTo array
        if (thisAnt.getDirection() == dir) {
            turnTo.clear();
        }

    }

    public boolean isEnemy(IAntInfo thisAnt) {
        boolean enemy = false;
        int self = thisAnt.getTeamInfo().getTeamID();
        int target = 0;

        IAntInfo enemyAnt = getAnt();

        if (enemyAnt != null) {

            target = enemyAnt.getTeamInfo().getTeamID();
            if (target != self) {
                enemy = true;
            }
            if (enemy && enemyAnt.getAntType().getTypeName().equalsIgnoreCase("QUEEN")) {
                hive.setEnemyQueenSpotted(enemyAnt.getLocation());
            }
        }
        return enemy;
    }

    public boolean isBlind(IAntInfo thisAnt) {

        IAntInfo antInFront = getAnt();
        if (antInFront != null) {
            if (antInFront.getTeamInfo() == thisAnt.getTeamInfo()) {
                return true;
            }
        }
        return visLoc.isEmpty() || visLoc.get(0).isFilled() || visLoc.get(0).isRock();
    }

    public boolean isNextToBlind() {
        return visLoc.get(1).isFilled() || visLoc.get(1).isRock();
    }

    //Check visibleLocations for food
    public boolean isFoodAhead() {
        boolean foodAhead = false;
        if (visLoc.size() > 0) {
            for (int i = 0; i < visLoc.size(); i++) {
                if (visLoc.get(i).getFoodCount() > 0) {
                    foodAhead = true;
                }
            }
        }
        return foodAhead;
    }

    public EAction nextMove(ILocationInfo loc, IAntInfo thisAnt) {
        EAction action = null;

        int nextDir = nextDir(loc, thisAnt);

        if (thisAnt.getDirection() == nextDir) {
            return EAction.MoveForward;
        }
        switch (thisAnt.getDirection()) {
            case 0:
                if (nextDir == 1 || nextDir == 2) {
                    action = EAction.TurnRight;
                }
                if (nextDir == 3) {
                    action = EAction.TurnLeft;
                }
                break;
            case 1:
                if (nextDir == 2 || nextDir == 3) {
                    action = EAction.TurnRight;
                }
                if (nextDir == 0) {
                    action = EAction.TurnLeft;
                }
                break;
            case 2:
                if (nextDir == 3 || nextDir == 0) {
                    action = EAction.TurnRight;
                }
                if (nextDir == 1) {
                    action = EAction.TurnLeft;
                }
                break;
            case 3:
                if (nextDir == 0 || nextDir == 1) {
                    action = EAction.TurnRight;
                }
                if (nextDir == 2) {
                    action = EAction.TurnLeft;
                }
                break;
        }
        return action;
    }

    public int nextDir(ILocationInfo path, IAntInfo thisAnt) {
        int nextDir = -1;

        int currX;
        int currY;

        //Starting point
        currX = thisAnt.getLocation().getX();
        currY = thisAnt.getLocation().getY();

        //First node in list
        int currGoalX = path.getX();
        int currGoalY = path.getY();

        if (currX == currGoalX && currY + 1 == currGoalY) {
            //add dir North
            nextDir = 0;
        } else if (currX + 1 == currGoalX && currY == currGoalY) {
            //add dir East
            nextDir = 1;
        } else if (currX == currGoalX && currY - 1 == currGoalY) {
            //set dir South
            nextDir = 2;
        } else if (currX - 1 == currGoalX && currY == currGoalY) {
            //set dir West
            nextDir = 3;
        }
        return nextDir;

    }

    public EAction turnRnd(IAntInfo thisAnt, TheHive hive) {
        EAction action = null;

        int height = hive.getBoardSizeY();

        Random rnd = new Random();

        int antY = thisAnt.getLocation().getY();

        //0,0
        if (antY == 0 && thisAnt.getDirection() == 1) {
            action = EAction.TurnLeft;
        } //0,8
        else if (antY == height && thisAnt.getDirection() == 1) {
            action = EAction.TurnRight;
        } //15,0
        else if (antY == 0 && thisAnt.getDirection() == 3) {
            action = EAction.TurnRight;
        } //15,8
        else if (antY == height && thisAnt.getDirection() == 3) {
            action = EAction.TurnLeft;
        } else if (direction.equalsIgnoreCase("Right")) {
            action = EAction.TurnRight;
        } else if (direction.equalsIgnoreCase("Left")) {
            action = EAction.TurnRight;
        } else if (rnd.nextBoolean()) {
            action = EAction.TurnRight;
            direction = "Right";
        } else {
            action = EAction.TurnLeft;
            direction = "Left";
        }

        return action;
    }

    public int setEnemyConditions(IAntInfo thisAnt, IAntInfo enemy) {
        int enemyHP = 0;
        String meType = thisAnt.getAntType().getTypeName();
        String enemyType = enemy.getAntType().getTypeName();

        //QUEEN
        if (meType.equalsIgnoreCase("QUEEN")) {
            if (enemyType.equalsIgnoreCase("QUEEN")) {
                enemyHP = thisAnt.getHitPoints() - 1;
            } else {
                enemyHP = 1;
            }

        } else if (enemyType.equalsIgnoreCase("QUEEN")) {
            enemyHP = Integer.MAX_VALUE;

        } //CARRIER
        else if (meType.equalsIgnoreCase("CARRIER") && enemyType.equalsIgnoreCase("SCOUT")) {
            enemyHP = Integer.MAX_VALUE;
        } else if (meType.equalsIgnoreCase("CARRIER") && enemyType.equalsIgnoreCase("CARRIER")) {
            enemyHP = Integer.MAX_VALUE;

        } //SCOUT
        else if (meType.equalsIgnoreCase("SCOUT") && enemyType.equalsIgnoreCase("SCOUT")) {
            enemyHP = 4;

        } else if (meType.equalsIgnoreCase("SCOUT") && enemyType.equalsIgnoreCase("CARRIER")) {
            enemyHP = 4;

        } //WARRIOR
        else if (meType.equalsIgnoreCase("WARRIOR") && enemyType.equalsIgnoreCase("SCOUT")) {
            enemyHP = Integer.MAX_VALUE;
        } else if (meType.equalsIgnoreCase("WARRIOR") && enemyType.equalsIgnoreCase("CARRIER")) {
            enemyHP = Integer.MAX_VALUE;
        } else if (meType.equalsIgnoreCase("WARRIOR") && enemyType.equalsIgnoreCase("WARRIOR")) {
            enemyHP = Integer.MAX_VALUE;

        }//NON WARRIOR
        else if (enemyType.equalsIgnoreCase("WARRIOR")) {
            enemyHP = 1;
        } else {
            enemyHP = 1;
        }
        return enemyHP;
    }

}
