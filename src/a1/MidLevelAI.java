package a1;

import aiantwars.EAction;
import aiantwars.IAntInfo;
import aiantwars.ILocationInfo;
import java.util.ArrayList;

/**
 *
 * @author Silas og Martin
 */
public class MidLevelAI extends LowLevelAI {

    boolean findEnemyQueen = false;
    boolean retaliation = false;
    boolean goingHome = false;
    int stuckCounter = 0;
    ILocationInfo lastPos = null;

    public MidLevelAI() {
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
            if (pA.contains(EAction.Attack) && survAction == null) {
                survAction = attackEnemy(thisAnt);
            } else {
                survAction = moveTo(thisAnt, hive.getStartPos(), hive.getMap());
                checkIfStuck(thisAnt);
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
            findEnemyQueen = false;
        }
        return action;
    }

    public EAction explore(IAntInfo thisAnt) {
        EAction action = null;

        //This is so scout, carrier and Queen will setEnemyQueenSpotted if they run into enemyQueen.
        //Warrior does this in attackEnemy.
        if (!thisAnt.getAntType().getTypeName().equalsIgnoreCase("Warrier")) {
            isEnemy(thisAnt);
        }

        if (isBlind(thisAnt) && pA.contains(EAction.TurnLeft) || getAnt() != null && pA.contains(EAction.TurnLeft)) {
            action = turnRnd(thisAnt);
        } else if (!isBlind(thisAnt) && pA.contains(EAction.MoveForward)) {
            action = EAction.MoveForward;
            direction = "";
        }
        return action;
    }

    public EAction attackEnemy(IAntInfo thisAnt) {
        EAction action = null;

        //Warrior goal will be set to enemyQueen's last know location. 
        //However, will still attack other ants along the way.
        if (enemyQueenLoc != null) {
            findEnemyQueen = true;
            goal = enemyQueenLoc;
        }

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

    // Silas method
//    public void getAttacker(IAntInfo thisAnt, int dir, IAntInfo attacker) {
//        int enemyHP = setEnemyConditions(thisAnt, attacker);
//
//        if (attacker.getAntType().getTypeName().equalsIgnoreCase("QUEEN")) {
//            turnToDir(dir, thisAnt);
//            retaliation = true;
//        }
//
//        if (attacker.getHitPoints() <= enemyHP) {
//            if (thisAnt.getDirection() == dir) {
//                retaliation = true;
//            } else if (thisAnt.getDirection() != dir) {
//                turnToDir(dir, thisAnt);
//            }
//        } else {
//            goingHome = true;
//        }
//    }
    // Martin method
    public void decideAttackRespons(int dir, IAntInfo thisAnt, IAntInfo attacker) {

        String thisAntT = thisAnt.getAntType().getTypeName();
        String attackerT = attacker.getAntType().getTypeName();

        if (attackerT.equalsIgnoreCase("QUEEN")) {
            turnToDir(dir, thisAnt);
            retaliation = true;
            hive.setEnemyQueenSpotted(attacker.getLocation());
        } else if (thisAntT.equalsIgnoreCase("QUEEN")) {
            retaliation = false;
            goingHome = true;
        } else if (thisAntT.equalsIgnoreCase("SCOUT") || thisAntT.equalsIgnoreCase("CARRIER")) {
            if (attackerT.equalsIgnoreCase("WARRIER")) {
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
    //Dig Soil 
    //--------------------------------------------------------------------------
    public EAction checkIfStuck(IAntInfo thisAnt) {

        ILocationInfo currentPos = thisAnt.getLocation();
        if (lastPos == null) {
            lastPos = hive.getStartPos();
        }

        if (currentPos.getX() == lastPos.getX() && currentPos.getY() == lastPos.getY()) {
            stuckCounter++;
            System.out.println("Count: " + stuckCounter);
        } else {
            System.out.println("Not same pos!");
            stuckCounter = 0;
            lastPos = currentPos;
        }

        if (stuckCounter >= 10) {
            System.out.println("I was stuck!!");
            if (goingHome) {
                goingHome = false;
                findEnemyQueen = false;
            } else {
                goingHome = true;
                findEnemyQueen = false;
            }
            stuckCounter = 0;
        }

        return EAction.Pass;
    }

    public EAction handleSoil(IAntInfo thisAnt) {
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
