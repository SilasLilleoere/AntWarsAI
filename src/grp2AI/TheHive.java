package grp2AI;

import aiantwars.ILocationInfo;
import aiantwars.impl.Location;
import astar_martin.AStar_Martin;
import java.util.List;

/**
 *
 * @author martins
 */
public class TheHive {
    

    public static int getBoardSizeY() {
        return boardSizeY;
    }

    private static int boardSizeX = 0;
    private static int boardSizeY = 0;
    private static ILocationInfo[][] hiveMap = null;
    private static AStar_Martin AStarInstance = null;
    private static TheHive hiveInstance;

    public static AStar_Martin getAStarInstance() {

        if (AStarInstance == null) {
            AStarInstance = new AStar_Martin(boardSizeX, boardSizeY);
        }
        return AStarInstance;
    }

    public static TheHive getHiveInstance() {

        if (hiveInstance == null) {
            hiveInstance = new TheHive();
        }
        return hiveInstance;
    }

    private TheHive() {
    }

    public void makeMap(int worldSizeX, int worldSizeY) {

        if (hiveMap == null) {

            boardSizeX = worldSizeX;
            boardSizeY = worldSizeY;

            hiveMap = new ILocationInfo[boardSizeX][boardSizeY];

            for (int y = 0; y < boardSizeY; ++y) {
                for (int x = 0; x < boardSizeX; x++) {
                    hiveMap[x][y] = new Location(x, y);
                }
            }
        }
        else{
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
}
