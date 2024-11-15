

import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;
/**
 * class mazetocsv, used for output the data of maze
 * , and shortest Path
 * */
public class mazetocsv{
    public Vertex[][] vertices_maze;
    public maze maze;
    public ShortestPath shortestPath;

    /** mazetocsv() constructor, used to create object of mazetocsv
     * */
    public mazetocsv(Vertex[][] vertices_maze,maze maze,ShortestPath shortestPath){
        this.vertices_maze = vertices_maze;
        this.maze= maze;
        this.shortestPath= shortestPath;
    }

    /** output_maze() function, used to output two csv files of maze_data and shortest Path
     * */
    public void output_maze() {
        try {
            FileWriter myObj = new FileWriter("maze_data.csv");
            //myObj.createNewFile();
            for (int i=0;i< maze.getRows();i++){
                for (int j=0;j< maze.getCols();j++){
                    if(vertices_maze[i][j].isWall()){
                        myObj.write("1");
                    }else {
                        myObj.write("0");
                    }
                    if(j!= maze.getCols()-1){
                        myObj.write(", ");
                    }
                }
                myObj.write("\n");
            }
            myObj.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ShortestPath shortestPath = new ShortestPath(maze.getMaze());
        //shortestPath.displayShortestPath(entryY, entryX, exitY, exitX);
        //(int startRow, int startCol, int endRow, int endCol)

        shortestPath.displayShortestPath(maze.getEntryRow(),maze.getEntryCol(),maze.getExitRow(),maze.getExitCol());
    }

}