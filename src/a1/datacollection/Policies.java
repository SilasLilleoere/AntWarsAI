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

    State startState;
    State exploreState;
    State defendState;
    State searchAndDestroy;
    State saveFoodState;
    DataCollector collector;
    State states;
    DataObject data = new DataObject();

    public Policies() {
        collector = new DataCollector();
        states = new State();

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

    }

}
