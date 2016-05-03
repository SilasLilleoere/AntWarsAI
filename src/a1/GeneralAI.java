package a1;

import aiantwars.EAction;

import aiantwars.IAntInfo;
import aiantwars.ILocationInfo;
import aiantwars.impl.Location;
import a1.astar_martin.AStar_Martin;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Silas
 */
public class GeneralAI {

    int worldSizeX = 0;
    int worldSizeY = 0;

    EAction turn = null;
    int cameFrom = -1;
    ArrayList turnList = null;
    ArrayList dirList = null;
    Random ran = new Random();
    String direction = "";
    private Location[][] location = null;
    int eggCounter = 0;

    private boolean isUnderAttack = false;
    private boolean retaliation = false;
    List<ILocationInfo> visLocations;
    List turnTo = new ArrayList();
    ILocationInfo[][] worldMap = null;
    ILocationInfo goal = null;
    boolean goingHome = false;
    ILocationInfo digLoc = null;

    int enemyHP = 0;

    AStar_Martin AStarPathFinder = null;
    TheHive hive = null;

    public GeneralAI() {

        this.dirList = null;
    }

    public void resetHive() {
        hive = null;
    }

    //--------------------------------------------------------------------------
    //1. Ant-queen needs to keep itself alive.
    public EAction survival(IAntInfo thisAnt, List<EAction> pA) {
        EAction survAction = null;

        if (thisAnt.getHitPoints() <= 17 && pA.contains(EAction.EatFood)) {
            survAction = EAction.EatFood;

        } else if (retaliation == true && pA.contains(EAction.Attack)) {
            survAction = EAction.Attack;
            retaliation = false;
        } else if (turnTo.size() > 0 && pA.contains(turnTo.get(0))) {
            survAction = (EAction) turnTo.get(0);
            turnTo.remove(0);
        } else {
            return null;
        }

        //updates totalFood in DataCollector if ant eats (subtracts from totalFood). 
        if (survAction == EAction.EatFood) {
            hive.updateFood(true);
        }
        return survAction;
    }

    //2. Lays eggs, whenever this is possible. 
    public EAction layEgg(IAntInfo thisAnt, List<EAction> pA, List<ILocationInfo> visLocations) {
        EAction eggAction = null;

        if (!visLocations.isEmpty() && pA.contains(EAction.LayEgg)) {
            eggAction = EAction.LayEgg;

        } else {
            return null;
        }
        return eggAction;
    }

    public EAction pickUpFood(IAntInfo thisAnt, List<EAction> pA, List<ILocationInfo> visibleLocations) {
        EAction action = null;

        if (pA.contains(EAction.PickUpFood)) {
            action = EAction.PickUpFood;
        } else if (isFoodAhead(visibleLocations)) {
            action = EAction.MoveForward;
        }

        //updates totalFood in DataCollector if ant pick up food (adds to totalFood). 
        if (action == EAction.PickUpFood) {
            hive.updateFood(false);
        }

        return action;
    }

    //Check visibleLocations for food
    public boolean isFoodAhead(List<ILocationInfo> visibleLocations) {
        boolean foodAhead = false;
        if (visibleLocations.size() > 0) {
            for (int i = 0; i < visibleLocations.size(); i++) {
                if (visibleLocations.get(i).getFoodCount() > 0) {
                    foodAhead = true;
                }
            }
        }
        return foodAhead;
    }

    //Drop food for Queen
    public EAction returnFood(IAntInfo thisAnt, ILocationInfo moveLoc, ILocationInfo[][] worldMap, List<EAction> pA) {
        EAction action = null;

        action = moveTo(thisAnt, moveLoc, worldMap);

        if (action != null) {
            return action;
        } else {
            dropFood(thisAnt, pA);
        }

        return action;
    }

    public EAction dropFood(IAntInfo thisAnt, List<EAction> pA) {
        EAction action = null;

        if (thisAnt.getFoodLoad() != 0 && pA.contains(EAction.DropFood)) {
            action = EAction.DropFood;
        }

        if (thisAnt.getLocation() != hive.getStartPos() && action != null) {
            hive.updateFood(true);
        }

        return action;
    }

    //This is mostly for warrior-ants.
//    public EAction searchAndDestory() {
//        EAction fightAction = null;
//        return fightAction;
//    }
    public EAction moveTo(IAntInfo thisAnt, ILocationInfo goal, ILocationInfo[][] worldMap) {

        EAction action = null;
        ArrayList<ILocationInfo> locList;
        locList = hive.getAStarInstance().findShortestPath(thisAnt.getLocation(), goal, worldMap);

        if (locList != null) {

            ILocationInfo loc = locList.get(1);

            if (!loc.isFilled() || !loc.isRock()) {
                action = nextMove(loc, thisAnt);
            }
        } else {
            goingHome = false;
        }
        return action;

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

    //---------------------------------------------SILAS---------------------------------------------------------------------
    public EAction explore(List<EAction> pA, IAntInfo thisAnt, List<ILocationInfo> visibleLocations) {
        EAction action = null;

        if (isBlind(visibleLocations, thisAnt) && pA.contains(EAction.TurnLeft) || getAnt(visibleLocations) != null && pA.contains(EAction.TurnLeft)) {
            action = turnRnd(pA, thisAnt, hive);
        } else if (!isBlind(visibleLocations, thisAnt) && pA.contains(EAction.MoveForward)) {
            action = EAction.MoveForward;
            direction = "";
        }
        return action;
    }

    public IAntInfo getAnt(List<ILocationInfo> visibleLocations) {
        IAntInfo ant = null;

        if (!visibleLocations.isEmpty()) {
            ant = visibleLocations.get(0).getAnt();
        }

        return ant;
    }

    public EAction turnRnd(List<EAction> possibleActions, IAntInfo thisAnt, TheHive hive) {
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

    public boolean isEnemy(List<ILocationInfo> visibleLocations, IAntInfo thisAnt) {
        boolean enemy = false;
        int self = thisAnt.getTeamInfo().getTeamID();
        int target = 0;

        IAntInfo enemyAnt = getAnt(visibleLocations);

        if (enemyAnt != null) {
            target = enemyAnt.getTeamInfo().getTeamID();
            if (target != self) {
                enemy = true;
            }
        }
        return enemy;
    }

    public boolean isBlind(List<ILocationInfo> visibleLocations, IAntInfo thisAnt) {

        IAntInfo antInFront = getAnt(visibleLocations);
        if (antInFront != null) {
            if (antInFront.getTeamInfo() == thisAnt.getTeamInfo()) {
                return true;
            }
        }
        return visibleLocations.isEmpty() || visibleLocations.get(0).isFilled() || visibleLocations.get(0).isRock();
    }

    public boolean isNextToBlind(List<ILocationInfo> visibleLocations) {
        return visibleLocations.get(1).isFilled() || visibleLocations.get(1).isRock();
    }

    public EAction attackEnemy(IAntInfo thisAnt, List<EAction> possibleActions, List<ILocationInfo> visibleLocations) {
        EAction action = null;

        if (isEnemy(visibleLocations, thisAnt) && possibleActions.contains(EAction.Attack)) {
            action = EAction.Attack;
        }

        return action;
    }

    public EAction attackSelf(IAntInfo thisAnt, List<EAction> possibleActions, List<ILocationInfo> visibleLocations) {

        EAction action = null;

        if (!isEnemy(visibleLocations, thisAnt) && possibleActions.contains(EAction.Attack)) {
            action = EAction.Attack;
        }

        return action;
    }

    //--------------------------------------------------------------------------
    //ON ATTACK METHODS - START
    //--------------------------------------------------------------------------
    //SPECIFIC BEHAVIOR VS ENEMY - START
    //--------------------------------------------------------------------------
    public void setEnemyConditions(IAntInfo thisAnt, IAntInfo enemy) {
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
    }

    //--------------------------------------------------------------------------
    //SPECIFIC BEHAVIOR VS ENEMY - END
    //--------------------------------------------------------------------------
    public void getAttacker(IAntInfo thisAnt, int dir, IAntInfo attacker) {
        setEnemyConditions(thisAnt, attacker);

        if (attacker.getHitPoints() <= enemyHP) {
            if (thisAnt.getDirection() == dir) {
                retaliation = true;
            } else if (thisAnt.getDirection() != dir) {
                turnToDir(dir, thisAnt);
            }
        } else {
            goingHome = true;
        }
    }

    //--------------------------------------------------------------------------
    //ON ATTACK METHODS - END
    //--------------------------------------------------------------------------
    private void turnToDir(int dir, IAntInfo thisAnt) {
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
    }

    //--------------------------------------------------------------------------
    //Dig Soil 
    //--------------------------------------------------------------------------
    public EAction dig(List<EAction> pA, List<ILocationInfo> visibleLocations) {
        EAction action = null;

        if (visibleLocations.get(0).isFilled() && pA.contains(EAction.DigOut)) {
            action = EAction.DigOut;
            digLoc = visibleLocations.get(0);

            //Drop soil at startLoc?
            //goingHome = true;
        }

        return action;
    }

    //Not sure if "Go to start location with soil" is a good idea. 
    //Blockage can occur in small tunnels. Comment out if needed.
    public EAction dropSoil(List<EAction> pA, IAntInfo thisAnt, List<ILocationInfo> visibleLocations) {
        EAction action = null;

        if (!isBlind(visibleLocations, thisAnt) && !isNextToBlind(visibleLocations)) {
            return action;
        } else if (visibleLocations.get(0) != digLoc && !isBlind(visibleLocations, thisAnt) && pA.contains(EAction.DropSoil)) {
            action = EAction.DropSoil;
        } else {
            //Use moveTo to A* to startLoc with Soil           
            action = moveTo(thisAnt, hive.getStartPos(), worldMap);

            if (action != null) {
                return action;
            } else if (!isBlind(visibleLocations, thisAnt) && pA.contains(EAction.DropSoil)) {
                action = EAction.DropSoil;
            }

        }

        return action;
    }

}
