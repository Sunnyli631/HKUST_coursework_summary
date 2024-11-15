/** class Vertex, used for storing data of vertex of maze
 * */
class Vertex {
    private int row;
    private int col;
    private boolean visited;
    private boolean shortestPath;
    private boolean wall;

    /** Vertex() constructor, used to create object of Vertex
     * */
    public Vertex(int row, int col) {
        this.row = row;
        this.col = col;
        this.visited = false;
        this.wall = true;
        this.shortestPath = false;

    }

    /** getRow() function, used to get corresponding row of vertex
     * */
    public int getRow() {
        return row;
    }

    /** getCol() function, used to get corresponding col of vertex
     * */
    public int getCol() {
        return col;
    }

    /** isVisited() function, used to get state of visited of vertex
     * */
    public boolean isVisited() {
        return visited;
    }

    /** isShortestPath() function, used to get state of shortestPath of vertex
     * */
    public boolean isShortestPath() {
        return shortestPath;
    }

    /** setVisited() function, used to modify state of visited of vertex
     * (either to be true or false)
     * */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /** setshortestPath() function, used to modify state of shortestPath of vertex
     * (either to be true or false)
     * */
    public void setShortestPath(boolean shortestPath) {
        this.shortestPath = shortestPath;
    }

    /** isWall() function, used to get state of walls of vertex
     * */
    public boolean isWall() {
        return wall;
    }

    /** setWall() function, used to modify the state of walls of vertex
     * */
    public void setWall(boolean wall) {
        this.wall = wall;
    }
}
