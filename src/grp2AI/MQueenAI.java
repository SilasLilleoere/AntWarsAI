package grp2AI;

import aiantwars.EAction;
import aiantwars.EAntType;
import aiantwars.IAntAI;
import aiantwars.IAntInfo;
import aiantwars.IEgg;
import aiantwars.ILocationInfo;
import aiantwars.impl.Location;
import astar_martin.AStar_Martin;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author Silas
 */
public class MQueenAI implements IAntAI {

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

    private AStar_Martin AStarPathFinder = null;
    private TheHive hive = null;

    public MQueenAI() {
        this.dirList = null;
    }

    //--------------------------------------------------------------------------
    //1. Ant-queen needs to keep itself alive.
    public EAction survival(IAntInfo thisAnt, List<EAction> pA) {
        EAction surAction = null;

        if (thisAnt.getHitPoints() <= 10 && pA.contains(EAction.EatFood)) {
            surAction = EAction.EatFood;
            System.out.println("ID 1: Survival metode --- HAAAAAAPPSSSS");
        } else {
            //findFood();
            return null;
        }
        return surAction;
    }

    //2. Lays eggs, whenever this is possible. 
    public EAction layEggs(IAntInfo thisAnt, List<EAction> pA) {
        EAction eggAction = null;

        if (!visLocations.isEmpty() && pA.contains(EAction.LayEgg)) {
            eggAction = EAction.LayEgg;
            System.out.println("ID 1: LayEggs metode --- LÆGGER ÆG!!!");
        } else {
            return null;
        }
        return eggAction;
    }

    //This is mostly for warrior-ants.
//    public EAction searchAndDestory() {
//        EAction fightAction = null;
//        return fightAction;
//    }
    public IAntInfo antInFront() {

        IAntInfo ant = null;

        if (!visLocations.isEmpty()) {
            ant = visLocations.get(0).getAnt();
        }
        return ant;
    }

    @Override
    public EAction chooseAction(IAntInfo thisAnt, ILocationInfo thisLocation, List<ILocationInfo> visibleLocations, List<EAction> possibleActions) {

        //System.out.println("I'm at this location now: " + thisAnt.getLocation().getX() + "," + thisAnt.getLocation().getY());
        this.visLocations = visibleLocations;
        boolean blind = visibleLocations.isEmpty() || visibleLocations.get(0).isFilled() || visibleLocations.get(0).isRock();

        List<EAction> pA = possibleActions;
        hive.updateMap(visLocations);
        worldMap = hive.getMap();
        System.out.println(eggCounter);

        //ASTAR stuff
        goal = worldMap[0][0];

        EAction action = null;

        if (pA.contains(EAction.PickUpFood) && !goingHome) {
            action = EAction.PickUpFood;
        }

        // System.out.println("GoingHome is now: " + goingHome);
        //1
        //SURVIVAL
        if (action == null && !goingHome) {
            //SURVIVAL
            System.out.println("survival");
            if (thisAnt.getHitPoints() < 17 && pA.contains(EAction.EatFood)) {
                action = EAction.EatFood;

            } else if (retaliation == true && pA.contains(EAction.Attack)) {
                action = EAction.Attack;
                System.out.println("ATTACKS!");
                retaliation = false;
            } else if (retaliation == true && turnTo.size() > 0 && pA.contains(turnTo.get(0))) {
                action = (EAction) turnTo.get(0);
                turnTo.remove(0);
            } //2
            //SPAWN SHIT
            else if (!blind && !isFoodAhead(visibleLocations) && pA.contains(EAction.LayEgg) || !blind && thisAnt.getFoodLoad() == 10 && possibleActions.contains(EAction.LayEgg)) {
                if (eggCounter < 20) {
                    action = EAction.LayEgg;
                    eggCounter++;
                    System.out.println("eggCounter: " + eggCounter);
                } else {
                    action = null;
                }

            } 
            //3
            //PICK FOOD SO WE CAN  #2
            else if (pA.contains(EAction.PickUpFood)) {
                action = EAction.PickUpFood;
            } //4
        } 
        //3
        //GoingHome
        if (goingHome) {
            action = returnHome(thisAnt, goal);
            if (action == null) {
                goingHome = false;
                System.out.println("Going Home set to false!");
            }
        }
        //4
        //Explore
        if (action == null && !goingHome) {
            System.out.println("Explore");
            action = explore(pA, thisAnt, visibleLocations);
        }

        //Check if you can move forward
        //4 
        //searchAndDestroy
        //5 
        //buildCastle
        //6
        //Pass
        if (action == null) {
            action = EAction.Pass;
        }
        return action;
    }

    public EAction returnHome(IAntInfo thisAnt, ILocationInfo goal) {

        EAction action = null;
        ArrayList<ILocationInfo> locList;
        locList = AStarPathFinder.findShortestPath(thisAnt.getLocation(), goal, worldMap);

        if (locList != null) {

            ILocationInfo loc = locList.get(1);

            if (!loc.isFilled() || !loc.isRock()) {
                action = nextMove(loc, thisAnt);
            }
        }
        return action;
    }

    @Override
    public void onHatch(IAntInfo thisAnt, ILocationInfo thisLocation, int worldSizeX, int worldSizeY) {

        //hive = TheHive.getHiveInstance();
        //hive.makeMap(worldSizeX, worldSizeY);
        //AStarPathFinder = TheHive.getAStarInstance();
    }

    @Override
    public void onLayEgg(IAntInfo thisAnt, List<EAntType> types, IEgg egg) {
        EAntType type = types.get(2);
        egg.set(type, this);
    }

    @Override
    public void onAttacked(IAntInfo thisAnt, int dir, IAntInfo attacker, int damage) {

        //Alt efter hvor meget vi bliver angrebet, vil produktion af warrior ants gå op eller ned.
        //Hvor meget mad har vi, iforhold til de felter der er blevet udforsket.
        //Hvilket område af mappet er der flest angreb.
        //
        isUnderAttack = true; 
    }

    @Override
    public void onDeath(IAntInfo thisAnt) {
        System.out.println("I'm dead fool!");
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

    @Override
    public void onStartTurn(IAntInfo thisAnt, int turn) {
        //System.out.println(": )");
        System.out.println("Turn: " + turn);
        if(turn == 20) {
        goingHome = true;
        }
    }

    //---------------------------------------------SILAS---------------------------------------------------------------------
    public EAction explore(List<EAction> pA, IAntInfo thisAnt, List<ILocationInfo> visibleLocations) {
        EAction action = null;
        boolean blind = visibleLocations.isEmpty() || visibleLocations.get(0).isFilled() || visibleLocations.get(0).isRock();

        if (blind && pA.contains(EAction.TurnLeft) || getAnt(visibleLocations) != null && pA.contains(EAction.TurnLeft)) {
            action = turnRnd(pA, thisAnt, hive);
        } else if (!blind && pA.contains(EAction.MoveForward)) {
            action = EAction.MoveForward;
            direction = "";
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
        } else {
            if (direction.equalsIgnoreCase("Right")) {
                action = EAction.TurnRight;
            } else if (direction.equalsIgnoreCase("Left")) {
                action = EAction.TurnRight;
            } else {
                if (rnd.nextBoolean()) {
                    action = EAction.TurnRight;
                    direction = "Right";
                } else {
                    action = EAction.TurnLeft;
                    direction = "Left";
                }
            }
        }
        return action;
    }

    //--------------------------------------------------------------------------
    //ON ATTACK METHODS - START
    //--------------------------------------------------------------------------
    //SPECIFIC ATTACKER AI (USED BY GETATTACKER) - START
    //--------------------------------------------------------------------------
    public void attackerWarrior(IAntInfo thisAnt, int dir, IAntInfo attacker) {

        if (attacker.getHitPoints() >= 20) {
            if (thisAnt.getDirection() == dir) {
                retaliation = true;
            } else if (thisAnt.getDirection() != dir) {
                 turnToDir(dir, thisAnt);
            }
        }
    }

    public void attackerScout(IAntInfo thisAnt, int dir, IAntInfo attacker) {
        if (attacker.getHitPoints() == 1) {
            if (thisAnt.getDirection() == dir) {
                retaliation = true;
            } else if (thisAnt.getDirection() != dir) {
                goingHome = true;
            }
        }
    }

    public void attackerCarrier(IAntInfo thisAnt, int dir, IAntInfo attacker) {
        if (attacker.getHitPoints() == 1) {
            if (thisAnt.getDirection() == dir) {
                retaliation = true;
            } else if (thisAnt.getDirection() != dir) {
                goingHome = true;
            }
        }
    }

    public void attackerQueen(IAntInfo thisAnt, int dir, IAntInfo attacker) {
        if (attacker.getHitPoints() < thisAnt.getHitPoints()) {
            if (thisAnt.getDirection() == dir) {
                retaliation = true;
            } else if (thisAnt.getDirection() != dir) {               
                goingHome = true;
            }
        }
    }
    //--------------------------------------------------------------------------
    //SPECIFIC ATTACKER AI (USED BY GETATTACKER) - END
    //--------------------------------------------------------------------------

    public void getAttacker(IAntInfo thisAnt, int dir, IAntInfo attacker) {
        EAntType enemy = attacker.getAntType();

        //CASE WARRIOR
        if (enemy == enemy.WARRIOR) {
            attackerWarrior(thisAnt, dir, attacker);
        } //CASE SCOUT
        else if (enemy == enemy.SCOUT) {
            attackerScout(thisAnt, dir, attacker);
        } //CASE CARRIER
        else if (enemy == enemy.CARRIER) {
            attackerCarrier(thisAnt, dir, attacker);
        } //CASE QUEEN
        else if (enemy == enemy.QUEEN) {
            attackerQueen(thisAnt, dir, attacker);
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

}
