/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

//1.Start State
import a1.TheHive;
import a1.datacollection.DataObject;
import a1.datacollection.State;
import aiantwars.EAntType;
import aiantwars.ILocationInfo;
import java.util.List;
import java.util.Stack;

//2.Explore State
//3.Defend State
//4.SearchAndDestroy State
//5.SaveFood State
public class HighLevelAI {

    private Stack<Boolean> attackStack = new Stack();
    private State currentState;
    private State startState;
    private State exploreState;
    private State defendState;
    private State searchAndDestroy;
    private State saveFoodState;
    private State states;
    private TheHive hive;
    private DataObject data = new DataObject();

    public HighLevelAI(TheHive hive) {

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

        if (data.getTurn() <= 50) {
            currentState = startState;
            return;
        }

        if (data.getAttackRate() < 15) {
            currentState = exploreState;
        }

        if (data.getAttackRate() >= 15) {
            currentState = defendState;
        }

        if (data.getFoodAntRatio() < 2 && data.getAttackRate() < 1 && data.getTurn() > 150) {
            currentState = saveFoodState;
        }

    }

    public void handleState() {
    }

    public EAntType getEggType(List<EAntType> types) {

        if (data.getCarrierCount() < currentState.getCarrierAmount()) {
            return types.get(0); // Carrier
        } else if (data.getWarriorCount() < currentState.getWarriorAmount()) {
            return types.get(2); // Warrior
        } else if (data.getScoutCount() < currentState.getScoutAmount()) {
            return types.get(1); // Scout
        } else {
            if (currentState != saveFoodState) {
                return types.get(2); // return warrior, if other minimum kvotes are meet.
            } else {
                return null;
            }
        }
    }

    public void Defend() {

    }

    public void SearchAndDestory() {

    }

    public void fillAttackStack() {

        // only fill attackStack if empty and have enough warriors.
        if (attackStack.empty() && data.getWarriorCount() > 1) {

            int ants = (data.getWarriorCount() >= 6) ? 4 : 2;
            for (int i = 0; i <= ants; i++) {
                attackStack.add(true);
            }
        }
    }

    public ILocationInfo attackOrder() {

        //if enemyQueen was spotted in current og last turn, 
        //then give warriorAnt order to go to enemyQueen's last spotted location.
        if (data.getEnemyQueenSpottedTurn() >= data.getTurn() - 1) {
            if (!attackStack.empty()) {
                attackStack.pop();
                return data.getEnemyQueenSpotted();
            }
        }
        else{
        attackStack.clear();
        }
        
        return null;
    }
}
