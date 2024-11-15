import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * class maze, used for managing the data of maze
 * , and generating the maze
 * */
public class maze {
    private int rows;
    private int cols;
    protected Vertex[][] maze;
    private Random random;
    private int entryRow;
    private int entryCol;
    private int exitRow;
    private int exitCol;
    /** getEntryCol() function, used to return entryCol
     * */
    public int getEntryCol(){
        return entryCol;
    }

    /** getEntryRow() function, used to return entryRow
     * */
    public int getEntryRow(){
        return entryRow;
    }

    /** getExitCol() function, used to return exitCol
     * */
    public int getExitCol(){
        return exitCol;
    }

    /** getExitRow() function, used to return exitRow
     * */
    public int getExitRow(){
        return exitRow;
    }

    /** maze() constructor, used to create maze object
     * */
    public maze(int rows, int cols,int entryRow, int entryCol, int exitRow, int exitCol) {

        this.rows = rows;
        this.cols = cols;
        maze = new Vertex[this.rows][this.cols];
        random = new Random();
        this.entryRow=entryRow;
        this.entryCol=entryCol;
        this.exitRow=exitRow;
        this.exitCol=exitCol;

        initializeMaze();
    }

    /** initializeMaze() function, used to initialize a maze
     * with all vertex are walls, unvisited and notShortestPath
     * */
    protected void initializeMaze() {
        if(rows<1||cols<1||entryRow>rows-1||entryRow<0||entryCol>cols-1||entryCol<0||exitRow>rows-1||exitRow<0||exitCol>cols-1||exitCol<0){
            return;
        }
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                maze[i][j] = new Vertex(i, j);
            }
        }
    }

    /** generateMaze() function, used to generate a maze with multiple paths to exit
     * (using randomized dfs algo)
     * */
    public void generateMaze() {
        if(rows<1||cols<1||entryRow>rows-1||entryRow<0||entryCol>cols-1||entryCol<0||exitRow>rows-1||exitRow<0||exitCol>cols-1||exitCol<0){
            return;
        }
        Vertex current = maze[entryRow][entryCol];
        current.setVisited(true);
        Stack<Vertex> stack = new Stack<>();
        stack.push(current);

        while (!stack.isEmpty()) {
            int row = current.getRow();
            int col = current.getCol();
            List<Vertex> neighbors = getUnvisitedNeighbors(row, col);

            if (!neighbors.isEmpty()) {
                Vertex neighbor = neighbors.get(random.nextInt(neighbors.size()));
                stack.push(current);
                removeWall(current, neighbor);
                current=neighbor;
                neighbor.setVisited(true);
            } else if(!stack.isEmpty()){
                current = stack.pop();
            }
        }

        // Set entry and exit points

        maze[entryRow][entryCol].setWall(false);
        if(entryCol-1>=0) {
            maze[entryRow][entryCol - 1].setWall(false);
        }
        maze[entryRow][entryCol+1].setWall(false);
        if(entryRow-2>0) {
            maze[entryRow - 1][entryCol + 1].setWall(false);
            maze[entryRow - 2][entryCol + 1].setWall(false);
        }
        if(entryRow+2<rows-1){
            maze[entryRow+1][entryCol+1].setWall(false);
            maze[entryRow+2][entryCol+1].setWall(false);
        }

        maze[exitRow][exitCol].setWall(false);
        maze[exitRow][exitCol-1].setWall(false);
        maze[exitRow][exitCol-2].setWall(false);
        if(exitRow+1<rows) {
            maze[exitRow + 1][exitCol - 1].setWall(false);
        }
        if(exitRow-1>0){
            maze[exitRow - 1][exitCol - 1].setWall(false);
        }
        randomly_remove_walls(6*getRows()*getCols()/100);
    }

    /** randomly_remove_walls() function, used to remove a certain number
     * of walls to create more ways to exit
     * */
    protected int randomly_remove_walls(int num){
        int count=0;
        if(num>(rows-2)*(cols-2)||num<0){
            return -1;
        }
        for(int i=1;i<cols-1;i++){
            int ran= random.nextInt(2);
            if(ran==1){
                maze[1][i].setWall(false);
            }
        }
        for(int i=1;i<rows-1;i++){
            int ran= random.nextInt(2);
            if(ran==1){
                maze[i][1].setWall(false);
            }
        }

        while(count<num){
            int ran_col= random.nextInt(cols);
            int ran_row = random.nextInt(rows);
            int col=cols-1;
            int row=rows-1;
            if(ran_col>0&&ran_col<col&&ran_row>0&&ran_row<row&&(ran_col!=entryCol&&ran_row!=entryRow)&&(ran_col!=exitCol&&ran_row!=exitRow)){
                if(maze[ran_row][ran_col].isWall()) {
                    maze[ran_row][ran_col].setWall(false);
                    count++;
                }
            }
        }

        return count;
    }

    /** getUnvisitedNeighbors() function, used to remove a certain number
     * of walls to create more ways to exit
     * */
    protected List<Vertex> getUnvisitedNeighbors(int row, int col) {
        List<Vertex> neighbors = new ArrayList<>();

        if (isValidCell(row - 2, col) && !maze[row - 2][col].isVisited()) {
            neighbors.add(maze[row - 2][col]);
        }
        if (isValidCell(row + 2, col) && !maze[row + 2][col].isVisited()) {
            neighbors.add(maze[row + 2][col]);
        }
        if (isValidCell(row, col - 2) && !maze[row][col - 2].isVisited()) {
            neighbors.add(maze[row][col - 2]);
        }
        if (isValidCell(row, col + 2) && !maze[row][col + 2].isVisited()) {
            neighbors.add(maze[row][col + 2]);
        }

        return neighbors;
    }

    /** isValidCell() function, used to check whether the vertex is valid or not
     * */
    protected boolean isValidCell(int row, int col) {
        return row > 0 && row < rows-1 && col > 0 && col < cols-1;
    }

    /** removeWall() function, used to remove walls in maze
     * */
    protected void removeWall(Vertex current, Vertex neighbor) {
        int currentRow = current.getRow();
        int currentCol = current.getCol();
        int neighborRow = neighbor.getRow();
        int neighborCol = neighbor.getCol();

        current.setWall(false);
        neighbor.setWall(false);
        if (currentRow < neighborRow) {
            maze[currentRow+1][currentCol].setWall(false);
        } else if (currentRow > neighborRow) {
            maze[currentRow-1][currentCol].setWall(false);
        } else if (currentCol < neighborCol) {
            maze[currentRow][currentCol+1].setWall(false);
        } else if (currentCol > neighborCol) {
            maze[currentRow][currentCol-1].setWall(false);
        }
    }


    /** getMaze() function, used to get vertex maze
     * */
    public Vertex[][] getMaze(){
        return maze;
    }

    /** getRows() function, used to get number of rows of the maze
     * */
    public int getRows(){
        return rows;
    }

    /** getCols() function, used to get number of cols of the maze
     * */
    public int getCols(){
        return cols;
    }
}
