package a1;

import aiantwars.ILocationInfo;
import aiantwars.impl.Location;
import a1.astar_martin.AStar_Martin;
import a1.datacollection.DataCollector;
import a1.datacollection.DataObject;
import aiantwars.IAntInfo;
import java.util.List;

/**
 *
 * @author martins
 */
public class TheHive {

    //Sorry, this needs to be in DataCollection---
    private ILocationInfo currPos = null;//-------
    //--------------------------------------------

    private ILocationInfo startPos = null;
    private int boardSizeX = 0;
    private int boardSizeY = 0;
    private ILocationInfo[][] hiveMap = null;
    private AStar_Martin AStarInstance = null;
    private DataCollector d = new DataCollector();

    public AStar_Martin getAStarInstance() {

        if (AStarInstance == null) {
            System.out.println("Laver en instance af ASTAR :" + boardSizeX);
            AStarInstance = new AStar_Martin(boardSizeX, boardSizeY);
        }
        return AStarInstance;
    }

    public TheHive(int boardSizeX, int boardSizeY) {
        this.boardSizeX = boardSizeX;
        this.boardSizeY = boardSizeY;
    }

    public void makeMap(int worldSizeX, int worldSizeY) {

        if (hiveMap == null) {

            boardSizeX = worldSizeX;
            boardSizeY = worldSizeY;
            System.out.println("X: " + boardSizeX);
            System.out.println("Y: " + boardSizeY);

            hiveMap = new ILocationInfo[boardSizeX][boardSizeY];

            for (int y = 0; y < boardSizeY; y++) {
                for (int x = 0; x < boardSizeX; x++) {
                    hiveMap[x][y] = new Location(x, y);
                }
            }
        } else {
            System.out.println("A map has already been made!");
        }
    }

    public ILocationInfo[][] getMap() {
        return hiveMap;
    }

    public void updateMap(List<ILocationInfo> visibleLocations) {

        for (ILocationInfo loc : visibleLocations) {
            hiveMap[loc.getX()][loc.getY()] = loc;
        }
    }

    public void clearMap() {
        hiveMap = null;
    }

    public int getBoardSizeY() {
        return boardSizeY;
    }

    public void setBoardSizeX(int boardSizeX) {
        this.boardSizeX = boardSizeX;
    }

    public void setBoardSizeY(int boardSizeY) {
        this.boardSizeY = boardSizeY;
    }

    public ILocationInfo getStartPos() {
        return startPos;
    }

    public void setStartPos(ILocationInfo queenLoc) {
        startPos = queenLoc;
    }

    public ILocationInfo getCurrPos() {
        return currPos;
    }

    public void setCurrPos(ILocationInfo currPos) {
        this.currPos = currPos;
    }

    //-----------------------DATACOLLECTOR METHODS-----------------------------------------
    public void updateAnts(IAntInfo thisAnt, boolean isAlive) {
        d.updateAnts(thisAnt, isAlive);
        System.out.println("update ant runs!");
    }

    public void updateFood(Boolean hasEaten) {
        d.updateFood(hasEaten);
    }

    public DataObject getData() {
        return d.getDataObject();
    }
}
