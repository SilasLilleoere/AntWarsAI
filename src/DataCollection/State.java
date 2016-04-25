/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollection;

/**
 *
 * @author martins
 */
public class State {

//1.Start State
//2.Explore State
//3.Defend State
//4.SearchAndDestroy State
//5.SaveFood State
    private int scoutAmount;
    private int warriorAmount;
    private int carrierAmount;
    private boolean queenGoHome;
    private boolean defendQueen;
    private boolean reducePopulation;
    private boolean searchForEnemy;

    State startState;
    State exploreState;
    State defendState;
    State seachAndDestoryState;
    State saveFoodState;

    //scouts, warriors, carrier, queenGoHome, defendQueen, SearchForEnemy, reducePopulation.
    //-1 means not to take into account.
    public State() {

        //start producing/have atleast:
        //1 scouts
        //2 warriors (these warriors act as 'bodyguards')
        //2 carriers
        startState = new State(1, 2, 2, false, false, false, false);

        //have atleast:
        //2 scouts
        //6 warriors
        //2 carrier 
        //activity:
        //Queen can move relative freely. queenGoHome = false;
        exploreState = new State(2, 6, 2, false, false, false, false);

        //have atleast:
        //10 warriors
        //activity:
        //up to 5 warriors go to queens location.
        //queenGoHome = true.
        defendState = new State(10, true, true);

        //have atleast:
        //1 scout      
        //8 warriors
        //1 carrier  
        //activity:
        //Send 5 warrior to location where we think enemyQueen could be.        
        seachAndDestoryState = new State(1, 10, 1, false, false, true, false);

        //have atleast/have maximum:
        //1 scout
        //4 warriors
        //1 carrier       
        //activity:
        //kill off own ants, until this state is meet.          
        saveFoodState = new State(1, 4, 2, false, false, false, true);
    }

    public State(int scoutAmount, int warriorAmount, int carrierAmount, boolean queenGoHome, boolean defendQueen, boolean reducePopulation, boolean searchForEnemy) {
        this.scoutAmount = scoutAmount;
        this.warriorAmount = warriorAmount;
        this.carrierAmount = carrierAmount;
        this.queenGoHome = queenGoHome;
        this.defendQueen = defendQueen;
        this.reducePopulation = reducePopulation;
        this.searchForEnemy = searchForEnemy;
    }

    //For defendState only
    public State(int warriorAmount, boolean queenGoHome, boolean defendQueen) {
        this.warriorAmount = warriorAmount;
        this.queenGoHome = queenGoHome;
        this.defendQueen = defendQueen;
    }  

    public int getScoutAmount() {
        return scoutAmount;
    }

    public int getWarriorAmount() {
        return warriorAmount;
    }

    public int getCarrierAmount() {
        return carrierAmount;
    }

    public boolean isQueenGoHome() {
        return queenGoHome;
    }

    public boolean isDefendQueen() {
        return defendQueen;
    }

    public boolean isReducePopulation() {
        return reducePopulation;
    }

    public boolean isSearchForEnemy() {
        return searchForEnemy;
    }

    public State getStartState() {
        return startState;
    }

    public State getExploreState() {
        return exploreState;
    }

    public State getDefendState() {
        return defendState;
    }

    public State getSeachAndDestoryState() {
        return seachAndDestoryState;
    }

    public State getSaveFoodState() {
        return saveFoodState;
    }
    
    
    
    
}
