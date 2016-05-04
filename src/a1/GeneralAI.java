package a1;

import aiantwars.EAction;

import aiantwars.IAntInfo;
import aiantwars.ILocationInfo;
import a1.astar_martin.AStar_Martin;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Silas
 */
public class GeneralAI {

    List<ILocationInfo> visLoc; // visibleLocations
    List<EAction> pA; //possibleActions

    EAction turn = null;
    ArrayList turnList = null;
    ArrayList dirList = null;
    Random ran = new Random();
    String direction = "";
    boolean retaliation = false;
    List<EAction> turnTo = new ArrayList();
    ILocationInfo goal = null;
    boolean goingHome = false;
    ILocationInfo digLoc = null;

    //The Hive and Astar
    AStar_Martin AStarPathFinder = null;
    TheHive hive = null;

    public GeneralAI() {
    }

    public void resetHive() {
        hive = null;
    }

    //--------------------------------------------------------------------------
    //1. Ant-queen needs to keep itself alive.
    public EAction survival(IAntInfo thisAnt) {
        EAction survAction = null;

        //Will eat food if hitpoints get under a certain level.
        if (thisAnt.getHitPoints() <= 17 && pA.contains(EAction.EatFood)) {
            survAction = EAction.EatFood;
        }

        //Ant goes to start position because its fleeing or needs food from storage. 
        if (goingHome) {
            survAction = moveTo(thisAnt, hive.getStartPos(), hive.getMap());
            if (pA.contains(EAction.Attack) && survAction == null) {
                survAction = attackEnemy(thisAnt);
            }
        }

        //updates totalFood in DataCollector if ant eats (subtracts from totalFood). 
        if (survAction == EAction.EatFood) {
            hive.updateFood(true);
        }
        return survAction;
    }

    //2. Lays eggs, whenever this is possible. 
    public EAction layEgg(IAntInfo thisAnt) {
        EAction eggAction = null;

        if (!visLoc.isEmpty() && pA.contains(EAction.LayEgg)) {
            eggAction = EAction.LayEgg;

        } else {
            return null;
        }
        return eggAction;
    }

    public EAction gatherFood(IAntInfo thisAnt) {
        EAction action = null;
        boolean ifHome = thisAnt.getLocation().getY() == hive.getStartPos().getY();

        if (ifHome && thisAnt.getFoodLoad() > 2) {
            goingHome = false;
            action = EAction.DropFood;
        }

        if (!ifHome && thisAnt.getFoodLoad() < 10 && pA.contains(EAction.PickUpFood) && action == null) {
            action = EAction.PickUpFood;
        }

        if (thisAnt.getFoodLoad() >= 10 && action == null) {
            goingHome = true;
        }

        return action;
    }

    public EAction pickUpFood(IAntInfo thisAnt) {
        EAction action = null;

        if (pA.contains(EAction.PickUpFood)) {
            action = EAction.PickUpFood;
        }
 //       else if (isFoodAhead(visibleLocations)) {
//            action = EAction.MoveForward;
//        }

        //updates totalFood in DataCollector if ant pick up food (adds to totalFood). 
        if (action == EAction.PickUpFood) {
            hive.updateFood(false);
        }

        return action;
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

    //Drop food for Queen
    public EAction returnFood(IAntInfo thisAnt, ILocationInfo moveLoc, ILocationInfo[][] worldMap) {
        EAction action = null;

        action = moveTo(thisAnt, moveLoc, worldMap);

        if (action != null) {
            return action;
        } else {
            action = dropFood(thisAnt);
        }
        return action;
    }

    public EAction dropFood(IAntInfo thisAnt) {
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

        // System.out.println("Carrier WorldMap: ");
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
    public EAction explore(IAntInfo thisAnt) {
        EAction action = null;

        if (isBlind(thisAnt) && pA.contains(EAction.TurnLeft) || getAnt() != null && pA.contains(EAction.TurnLeft)) {
            action = turnRnd(thisAnt, hive);
        } else if (!isBlind(thisAnt) && pA.contains(EAction.MoveForward)) {
            action = EAction.MoveForward;
            direction = "";
        }
        return action;
    }

    public IAntInfo getAnt() {
        IAntInfo ant = null;

        if (!visLoc.isEmpty()) {
            ant = visLoc.get(0).getAnt();
        }

        return ant;
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

    public EAction attackEnemy(IAntInfo thisAnt) {
        EAction action = null;

        if (isEnemy(thisAnt)) {

            if (pA.contains(EAction.Attack)) {
                action = EAction.Attack;
            } else if (pA.contains(EAction.PickUpFood)) {
                action = EAction.PickUpFood;
            } else {
                action = EAction.Pass;
            }
        }

        //turn to attacker
        if (retaliation == true && action == null) {

            if (!turnTo.isEmpty() && pA.contains(turnTo.get(0))) {
                action = turnTo.get(0);
                turnTo.remove(0);
            }
            retaliation = false;
        }

        return action;
    }

    public EAction attackSelf(IAntInfo thisAnt) {

        EAction action = null;

        if (!isEnemy(thisAnt) && pA.contains(EAction.Attack)) {
            action = EAction.Attack;
        }

        return action;
    }

    //--------------------------------------------------------------------------
    //ON ATTACK METHODS - START
    //--------------------------------------------------------------------------
    //SPECIFIC BEHAVIOR VS ENEMY - START
    //--------------------------------------------------------------------------
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

    //--------------------------------------------------------------------------
    //SPECIFIC BEHAVIOR VS ENEMY - END
    //--------------------------------------------------------------------------
    // Silas method
    public void getAttacker(IAntInfo thisAnt, int dir, IAntInfo attacker) {
        int enemyHP = setEnemyConditions(thisAnt, attacker);

        if (attacker.getAntType().getTypeName().equalsIgnoreCase("QUEEN")) {
            turnToDir(dir, thisAnt);
            retaliation = true;
        }

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

    // Martin method
    public void decideAttackRespons(int dir, IAntInfo thisAnt, IAntInfo attacker) {

        String thisAntT = thisAnt.getAntType().getTypeName();
        String attackerT = attacker.getAntType().getTypeName();

        if (attackerT.equalsIgnoreCase("QUEEN")) {
            turnToDir(dir, thisAnt);
            retaliation = true;
            return;
        } else if (thisAntT.equalsIgnoreCase("QUEEN")) {
            retaliation = false;
            goingHome = true;
            return;
        } else if (thisAntT.equalsIgnoreCase("SCOUT") || thisAntT.equalsIgnoreCase("CARRIER")) {
            if (attackerT.equalsIgnoreCase("WARRIOR")) {
                retaliation = false;
                goingHome = true;
            } else {
                retaliation = true;
            }
        } else {
            turnToDir(dir, thisAnt);
            retaliation = true;
        }
    }

    //--------------------------------------------------------------------------
    //ON ATTACK METHODS - END
    //--------------------------------------------------------------------------
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

    //--------------------------------------------------------------------------
    //Dig Soil 
    //--------------------------------------------------------------------------
    public EAction dig() {
        EAction action = null;

        if (visLoc.get(0).isFilled() && pA.contains(EAction.DigOut)) {
            action = EAction.DigOut;
            digLoc = visLoc.get(0);

            //Drop soil at startLoc?
            //goingHome = true;
        }

        return action;
    }

    //Not sure if "Go to start location with soil" is a good idea. 
    //Blockage can occur in small tunnels. Comment out if needed.
    public EAction dropSoil(IAntInfo thisAnt) {
        EAction action = null;

        if (!isBlind(thisAnt) && !isNextToBlind()) {
            return action;
        } else if (visLoc.get(0) != digLoc && !isBlind(thisAnt) && pA.contains(EAction.DropSoil)) {
            action = EAction.DropSoil;
        } else {
            //Use moveTo to A* to startLoc with Soil           
            action = moveTo(thisAnt, hive.getStartPos(), hive.getMap());

            if (action != null) {
                return action;
            } else if (!isBlind(thisAnt) && pA.contains(EAction.DropSoil)) {
                action = EAction.DropSoil;
            }

        }

        return action;
    }

}
