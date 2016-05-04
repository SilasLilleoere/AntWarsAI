/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1.datacollection;

//1.Start State
import a1.TheHive;
import aiantwars.EAntType;
import java.util.List;

//2.Explore State
//3.Defend State
//4.SearchAndDestroy State
//5.SaveFood State
public class Policies {

    private State currentState;
    private State startState;
    private State exploreState;
    private State defendState;
    private State searchAndDestroy;
    private State saveFoodState;
    private State states;
    private TheHive hive;
    private DataObject data = new DataObject();

    public Policies(TheHive hive) {

        this.hive = hive;
        states = new State();

        currentState = null;
        startState = states.getStartState();
        exploreState = states.getExploreState();
        defendState = states.getDefendState();
        searchAndDestroy = states.getSeachAndDestoryState();
        saveFoodState = states.getSaveFoodState();
    }

    public void updateData() {
        this.data = hive.getData();
        decideState();
    }

    public void decideState() {

        if (data.getTurns() <= 60) {
            currentState = startState;
//        } else {
//
//            //if ants get attacked often, go to defendstate.
//            if (data.getAttackRate() > 1) {
//                currentState = defendState;
//            }
//
//            if (data.getTurns() > 1000 && data.getFoodAntRatio() < 10 && data.getMapExplored() > 50) {
//                currentState = saveFoodState;
//            }
//
//            if (data.getTurns() < 1000) {
//
//            }
        }
    }

    public void handleState() {
    }

    public EAntType getEggType(List<EAntType> types) {

        System.out.println("State scouts: " + currentState.getScoutAmount());
        System.out.println("Current scouts " + data.getScoutCount());

        System.out.println("State carriers: " + currentState.getCarrierAmount());
        System.out.println("Current carriers: " + data.getCarrierCount());

        System.out.println("State warriors: " + currentState.getWarriorAmount());
        System.out.println("Current warriors: " + data.getWarriorCount());

        if (data.getCarrierCount() < currentState.getCarrierAmount()) {
            return types.get(0); // Carrier
        } else if (data.getWarriorCount() < currentState.getWarriorAmount()) {
            return types.get(2); // Warrior
        } else if (data.getScoutCount() < currentState.getScoutAmount()) {
            return types.get(1); // Scout
        } else {
            return types.get(2); // return warrior, if other minimum kvotes are meet.
        }
    }

    public void Defend() {

    }

    public void SearchAndDestory() {

    }

}
