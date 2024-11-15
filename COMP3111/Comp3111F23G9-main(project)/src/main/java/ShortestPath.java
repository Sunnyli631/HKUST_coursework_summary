import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;
public class ShortestPath {
    private Vertex[][] maze;
    private int numRows;
    private int numCols;
    public LinkedList<Vertex>path;

    /**
     *  draw path green on maze
     */
    public void draw(Graphics2D g2){
        for(int i = 0; i < path.size(); i++){
            Vertex cell = path.get(i);
            g2.setColor(Color.green);
            g2.fillRect(cell.getCol()*10, cell.getRow()*10, 10, 10);
        }
    }

    /**
     *   a constructor that takes a 2D array of vertices representing the maze
     */
    public ShortestPath(Vertex[][] maze) {
        this.numRows = maze.length;
        this.numCols = maze[0].length;
        this.path = new LinkedList<>();

        // Perform deep copy of the maze array
        this.maze = new Vertex[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Vertex originalVertex = maze[i][j];
                Vertex copiedVertex = new Vertex(originalVertex.getRow(), originalVertex.getCol());
                copiedVertex.setVisited(originalVertex.isVisited());
                copiedVertex.setShortestPath(originalVertex.isShortestPath());
                copiedVertex.setWall(originalVertex.isWall());
                this.maze[i][j] = copiedVertex;
            }
        }
    }

    /**
     *   takes the starting and ending coordinates of the path in the maze and creates the shortest path to display it in csv format
     */
    public void displayShortestPath (int startRow, int startCol, int endRow, int endCol){
        createShortestPath(startRow,startCol,endRow,endCol);
        try {
            FileWriter myObj = new FileWriter("shortestPath.csv");
            myObj.write("ShortestPath, Index, RowX, ColY");
            int count = 0;
            for (Vertex vertex : path) {
                myObj.write("\n");
                myObj.write("SP"+ ", " + count + ", ");
                myObj.write( vertex.getRow() + ", " + vertex.getCol() + " ");
                count++;
            }
            myObj.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *   initializes the maze by marking all vertices as unvisited and clear shortest path. then calls the "findShortestPath" method to perform the BFS and find the shortest path.
     */
    public boolean createShortestPath ( int startRow, int startCol, int endRow, int endCol ){
        path.clear();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                maze[i][j].setVisited(false);
                maze[i][j].setShortestPath(false);
            }
        }
        boolean isFind = findShortestPath(startRow,startCol,endRow,endCol,path);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                maze[i][j].setVisited(false);
            }
        }
        //make the vertex in path to be visited in maze
        for (Vertex vertex : path) {
            vertex.setShortestPath(true);
        }
        return isFind;
    }

    /**
     *    implements the BFS algorithm. reconstructs the shortest path if path is found
     */
    public boolean findShortestPath(int startRow, int startCol, int endRow, int endCol, LinkedList<Vertex> path) {
        if (startRow < 0 || startRow >= numRows || startCol < 0 || startCol >= numCols ||
        endRow < 0 || endRow >= numRows || endCol < 0 || endCol >= numCols) {
            // Out of bounds, return false
            return false;
        }

        Vertex startVertex = maze[startRow][startCol];
        Vertex endVertex = maze[endRow][endCol];

        // Create a queue to perform BFS
        Queue<Vertex> queue = new LinkedList<>();
        queue.offer(startVertex);

        // Set the start vertex as visited
        startVertex.setVisited(true);

        // Use a map to keep track of the parent vertex for each visited vertex
        HashMap<Vertex, Vertex> parentMap = new HashMap<>();
        parentMap.put(startVertex, null);

        while (!queue.isEmpty()) {
            Vertex currentVertex = queue.poll();

            if (currentVertex == endVertex) {
                Vertex vertex = currentVertex;
                while (vertex != null) {
                    path.addFirst(vertex);
                    vertex = parentMap.get(vertex);
                }
                return true;
            }

            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] direction : directions) {
                int dx = direction[0];
                int dy = direction[1];
                int newRow = currentVertex.getRow() + dx;
                int newCol = currentVertex.getCol() + dy;

                if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols) {
                    Vertex neighborVertex = maze[newRow][newCol];
                    if (!neighborVertex.isVisited() && !neighborVertex.isWall()) {
                        neighborVertex.setVisited(true);
                        queue.offer(neighborVertex);
                        parentMap.put(neighborVertex, currentVertex);
                    }
                }
            }
        }
        // No path found
        return false;
    }
}