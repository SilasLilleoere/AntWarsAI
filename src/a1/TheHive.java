package a1;

import aiantwars.ILocationInfo;
import aiantwars.impl.Location;
import a1.astar.AStar;
import a1.datacollection.DataCollector;
import a1.datacollection.DataObject;
import aiantwars.EAntType;
import java.util.List;

/**
 *
 * @author martins
 */
public class TheHive {

    private int friendlyAntTeamID;
    private ILocationInfo startPos = null;
    private int boardSizeX = 0;
    private int boardSizeY = 0;
    private ILocationInfo[][] hiveMap = null;
    private AStar AStarInstance = null;
    private DataCollector d = new DataCollector();
    private HighLevelAI p = new HighLevelAI(this);

    public AStar getAStarInstance() {

        if (AStarInstance == null) {
            AStarInstance = new AStar(boardSizeX, boardSizeY);
        }
        return AStarInstance;
    }

    public void updateHive() {

    }

    public TheHive(int boardSizeX, int boardSizeY) {
        this.boardSizeX = boardSizeX;
        this.boardSizeY = boardSizeY;
    }

    public void makeMap(int worldSizeX, int worldSizeY) {

        if (hiveMap == null) {

            boardSizeX = worldSizeX;
            boardSizeY = worldSizeY;

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

    public void updateMap(List<ILocationInfo> visLoc) {

        for (ILocationInfo loc : visLoc) {
            hiveMap[loc.getX()][loc.getY()] = loc;
            //checks if enemyQueen is in visLoc
            if (loc.getAnt() != null) {
                if (loc.getAnt().getTeamInfo().getTeamID() != friendlyAntTeamID) {
                    if (loc.getAnt().getAntType().getTypeName().equalsIgnoreCase("Queen")) {
                        setEnemyQueenSpotted(loc.getAnt().getLocation());
                    }
                }
            }
        }
        //updates information in policies
        p.updateData();
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

    public void setFriendlyAntTeamID(int friendlyAntTeamID) {
        this.friendlyAntTeamID = friendlyAntTeamID;
    }
    //-----------------------POLICIES METHODS---------------------------------------------

    public EAntType getEggType(List<EAntType> types) {
        return p.getEggType(types);
    }

    public ILocationInfo getMoveLocation() {
        return null;
    }

    //-----------------------DATACOLLECTOR METHODS-----------------------------------------
    public void updateAnts(String antType, boolean isAlive) {
        d.updateAnts(antType, isAlive);
    }

    public void updateFood(Boolean hasEaten) {
        d.updateFood(hasEaten);
    }

    public void updateAttacks() {
        d.updateAttacks();
        d.updateAttackRate(true);
    }

    public void setTotalTurns(int turns) {
        d.setTotalTurns(turns);
        d.updateAttackRate(false);
    }

    public DataObject getData() {
        return d.getDataObject();
    }

    public void setEnemyQueenSpotted(ILocationInfo loc) {
      //  System.out.println("enemyQueen spotted!!");
        d.setEnemyQueenSpotted(loc);
        p.fillAttackStack();
    }

    public ILocationInfo attackOrder() {
        return p.attackOrder();

    }
}
