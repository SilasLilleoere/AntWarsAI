/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1.datacollection;

//1.Start State
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
    private DataCollector collector;
    private State states;
    private DataObject data = new DataObject();

    public Policies() {
        collector = new DataCollector();
        states = new State();

        currentState = null;
        startState = states.getStartState();
        exploreState = states.getExploreState();
        defendState = states.getDefendState();
        searchAndDestroy = states.getSeachAndDestoryState();
        saveFoodState = states.getSaveFoodState();
    }

    public void updateData() {
        this.data = collector.getDataObject();
    }

    public void DecideState() {

        if (data.getTurns() <= 40) {
            currentState = startState;
        } else {

            //if ants get attacked often, go to defendstate.
            if (data.getAttackRate() > 1) {
                currentState = defendState;
            }

            if (data.getTurns() > 1000 && data.getFoodAntRatio() < 10 && data.getMapExplored() > 50) {
                currentState = saveFoodState;
            }

            if (data.getTurns() < 1000) {
                
            }
        }
    }

    public void handleState() {
    }

    public void getEggType() {

    }

    public void Defend() {

    }

    public void SearchAndDestory() {

    }

}
