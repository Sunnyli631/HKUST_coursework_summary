import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;
import org.junit.jupiter.api.Assertions;


class UnitTest extends Main{
    final int width = 30;
    final int height = 30;
    final int entryX = 0;
    final int entryY = 12;
    final int exitX = 29;
    final int exitY = 12;
    maze maze;
    GamePanel gp;

    /**
     * helper function for unit testing
     * used to init all necessary component
     */
    public void init(){
        maze = new maze(width,height, entryY, entryX, exitY, exitX);
        maze.generateMaze();
        gp = new GamePanel(maze,maze.getCols(),maze.getRows());
    }
    //player

    /**
     *  unit test to test update() function in Player Class
     *  simulate WASD press and test if it can change its position accordingly
     */
    @Test
    void testPlayerUpdate(){
        init();
        gp.jerry.update();//target function
        assertEquals(gp.jerry.x, entryX* gp.tileSize);

        init();
        gp.kH.rightPressed = true;
        gp.jerry.update();//target function
        assertEquals(gp.jerry.x, entryX*gp.tileSize+gp.jerry.speed);

        init();
        gp.kH.leftPressed = true;
        gp.jerry.update();//target function
        assertEquals(gp.jerry.x, entryX*gp.tileSize-gp.jerry.speed);

        init();
        gp.kH.upPressed = true;
        gp.jerry.update();//target function
        assertEquals(gp.jerry.y, entryY*gp.tileSize-gp.jerry.speed);

        init();
        gp.kH.downPressed = true;
        gp.jerry.update();//target function
        assertEquals(gp.jerry.y, entryY*gp.tileSize+gp.jerry.speed);
    }

    //Enemy
    /**
     *  unit test to test update() function in Enemy Class
     *  create a shortest path from Enemy position to tile on the left and test if it can move to left.
     */
    @Test
    void testEnemyUpdate() { //check if tom can follow the shortest path
        init(); //Enemy is initialized in GamePanel aka tom
        gp.tom.path.createShortestPath(gp.tom.y/gp.tileSize, gp.tom.x/gp.tileSize, gp.tom.y/gp.tileSize, gp.tom.x/gp.tileSize-1);
        gp.tom.update();
        assertEquals(gp.tom.x, exitX*gp.tileSize-gp.tom.speed);
        init();
        gp.tom.path.path.clear();
        gp.tom.update();//target function
        assertEquals(gp.tom.x, exitX*gp.tileSize);
    }


    //KeyHandler
    /**
     *  unit test to test constructor function in KeyHandler Class
     *  test if all the variables are false at init
     */
    @Test
    void testKeyHandler(){
        init();
        assertEquals(gp.kH.upPressed, false);//target function
        assertEquals(gp.kH.downPressed, false);//target function
        assertEquals(gp.kH.leftPressed, false);//target function
        assertEquals(gp.kH.rightPressed, false);//target function
    }

    /**
     * unit test to test keyPressed() function in KeyHandler class
     * simulate all WASD key press event using KeyEvent class and test the function
     */
    @Test
    void testKeyPressed() {
        init();
        Button a = new Button("click");
        KeyEvent eventW = new KeyEvent(a,1, 0, 0, KeyEvent.VK_W);
        KeyEvent eventA = new KeyEvent(a,2, 0, 0, KeyEvent.VK_A);
        KeyEvent eventS = new KeyEvent(a,3, 0, 0, KeyEvent.VK_S);
        KeyEvent eventD = new KeyEvent(a,4, 0, 0, KeyEvent.VK_D);
        gp.gameState = gp.functionCState;
        gp.kH.keyPressed(eventW);//target function
        assertEquals(gp.kH.upPressed, true);
        gp.kH.keyPressed(eventA);//target function
        assertEquals(gp.kH.leftPressed, true);
        gp.kH.keyPressed(eventS);//target function
        assertEquals(gp.kH.downPressed, true);
        gp.kH.keyPressed(eventD);//target function
        assertEquals(gp.kH.rightPressed, true);
    }

    /**
     * unit test to test keyReleased() function in KeyHandler class
     * simulate all WASD key release event using KeyEvent class and test the function
     */
    @Test
    void testKeyReleased() {
        init();
        Button a = new Button("release");
        KeyEvent eventW = new KeyEvent(a,1, 0, 0, KeyEvent.VK_W);
        KeyEvent eventA = new KeyEvent(a,2, 0, 0, KeyEvent.VK_A);
        KeyEvent eventS = new KeyEvent(a,3, 0, 0, KeyEvent.VK_S);
        KeyEvent eventD = new KeyEvent(a,4, 0, 0, KeyEvent.VK_D);
        gp.gameState = gp.functionCState;
        gp.kH.upPressed = gp.kH.leftPressed = gp.kH.downPressed = gp.kH.rightPressed = true;
        gp.kH.keyReleased(eventW);//target function
        gp.kH.keyReleased(eventA);//target function
        gp.kH.keyReleased(eventS);//target function
        gp.kH.keyReleased(eventD);//target function
        assertEquals(gp.kH.upPressed, false);
        assertEquals(gp.kH.leftPressed, false);
        assertEquals(gp.kH.downPressed, false);
        assertEquals(gp.kH.rightPressed, false);
    }

    /**
     * unit test to test handleCommand() function in KeyHandler class
     * simulate real situation by manually setting selectedCommand in UI class since this function changes the game state according to the selected command
     */
    @Test
    void testHandleCommand(){
        init();
        gp.ui.selectedCommand = "function A";
        gp.kH.handleCommand();//target function
        assertEquals(gp.gameState, gp.functionAState);
        gp.ui.selectedCommand = "function B";
        gp.kH.handleCommand();//target function
        assertEquals(gp.gameState, gp.functionBState);
        gp.ui.selectedCommand = "function C";
        gp.kH.handleCommand();//target function
        assertEquals(gp.gameState, gp.functionCState);
        gp.ui.selectedCommand = "title";
        gp.kH.handleCommand();//target function
        assertEquals(gp.gameState, gp.titleState);
        gp.ui.selectedCommand = "quit";
        gp.kH.handleCommand();//target function
        assertEquals(gp.gameState, gp.quitState);
    }

    //CollisionChecker

    /**
     * unit test for checkEntity() function in CollisionChecker class
     * simulate 2 entities collides by setting tom and jerry at the same position (they should collide)
     */
    @Test
    void testCheckEntity_Collide() {   //check if 2 entity collide
        init();
        gp.tom.setPos(entryX*gp.tileSize,entryY*gp.tileSize);
        gp.cChecker.checkEntity(gp.jerry,gp.tom);//target function
        assertEquals(gp.isEnded, true);
        assertEquals(gp.jerryWin, false);
    }

    /**
     * unit test for checkEntity() function in CollisionChecker class
     * simulate 2 entities does not collide by setting tom and jerry at the different position (they should not collide)
     */
    @Test
    void testCheckEntity_NotCollide(){ //check if 2 entity not collide
        init();
        gp.cChecker.checkEntity(gp.jerry, gp.tom); //target function
        assertEquals(gp.isEnded, false);
    }

    /**
     * unit test for checkTile() function in CollisionChecker class
     * simulate entity collides with the wall in maze by setting the Player class position at the wall
     */
    @Test
    void testCheckTile_Collide() { //check if entity collide with the wall
        init();
        gp.jerry.setPos(entryX*gp.tileSize, (entryY+1)*gp.tileSize);
        gp.jerry.direction = "up";
        gp.cChecker.checkTile(gp.jerry);//target function
        assertEquals(gp.jerry.collisionOn, true);
    }

    /**
     * unit test for checkTile() function in CollisionChecker class
     * simulate entity does not collide with the wall in maze by setting the Player class position at non wall vertex.
     */
    @Test
    void testCheckTile_NotCollide() { //check if entity not collide with the wall
        init();
        gp.jerry.direction = "right";
        gp.cChecker.checkTile(gp.jerry);//target function
        assertEquals(gp.jerry.collisionOn, false);
    }













    //maze
    /**
     * unit test for maze() constructor in maze class,
     * create a maze and check whether the values are
     * assigned successfully
     * */
    @Test
    void testmaze(){
        maze maze=new maze(50,50,20,1,1,34);//target function
        assertEquals(20,maze.getEntryRow());
        assertEquals(1,maze.getEntryCol());
        assertEquals(1,maze.getExitRow());
        assertEquals(34,maze.getExitCol());
        assertEquals(50,maze.getCols());
        assertEquals(50,maze.getRows());
    }

    /**
     * unit test for getEntryCol() function in maze class,
     * create a maze and check whether the value can be gotten
     * successfully
     * */
    @Test
    void testgetEntryCol() {
        maze maze=new maze(30,30,12,1,1,29);
        assertEquals(1,maze.getEntryCol());//target function
    }

    /**
     * unit test for getEntryRow() function in maze class,
     * create a maze and check whether the value can be gotten
     * successfully
     * */
    @Test
    void testgetEntryRow() {
        maze maze=new maze(30,30,12,1,1,29);
        assertEquals(1,maze.getEntryCol());//target function
    }

    /**
     * unit test for getExitCol() function in maze class,
     * create a maze and check whether the value can be gotten
     * successfully
     * */
    @Test
    void testgetExitCol() {
        maze maze=new maze(30,30,12,1,1,29);
        assertEquals(1,maze.getEntryCol());//target function
    }

    /**
     * unit test for getExitRow() function in maze class,
     * create a maze and check whether the value can be gotten
     * successfully
     * */
    @Test
    void testgetExitRow() {
        maze maze=new maze(30,30,12,1,1,29);
        assertEquals(1,maze.getEntryCol());//target function
    }

    /**
     * unit test for generateMaze() function in maze class,
     * check whether a maze can be successfully generated
     * */
    @Test
    void testgenerateMaze() {
        maze maze=new maze(70,70,5,1,5,9);
        maze.generateMaze();//target function
        assertFalse(maze.getMaze()[5][1].isWall());
        assertFalse(maze.getMaze()[5][9].isWall());

        maze maze_1=new maze(70,70,5,1,-5,9);
        maze_1.generateMaze();//target function
        assertNull(maze_1.getMaze()[5][1]);
    }

    /**
     * unit test for getMaze() function in maze class,
     * check whether a maze can be gotten successfully
     * */
    @Test
    void testgetMaze() {
        maze maze=new maze(36,30,22,1,17,29);
        assertEquals(maze.maze,maze.getMaze());//target function
    }

    /**
     * unit test for getRows() function in maze class,
     * check whether the number of rows of a maze can be gotten successfully
     * */
    @Test
    void testgetRows() {
        maze maze=new maze(36,30,12,1,1,29);
        assertEquals(36,maze.getRows());//target function
    }

    /**
     * unit test for getCols() function in maze class,
     * check whether the number of cols of a maze can be gotten successfully
     * */
    @Test
    void testgetCols() {
        maze maze=new maze(30,100,12,1,1,29);
        assertEquals(100,maze.getCols());//target function
    }

    /**
     * unit test for removeWall() function in maze class,
     * check whether walls in maze can be removed successfully
     * */
    @Test
    void testremoveWall(){
        maze maze=new maze(30,100,12,1,1,29);
        maze.removeWall(maze.getMaze()[12][1],maze.getMaze()[14][1]);//target function
        assertEquals(false,maze.getMaze()[12][1].isWall());
        assertEquals(false,maze.getMaze()[13][1].isWall());
        assertEquals(false,maze.getMaze()[14][1].isWall());
    }

    /**
     * unit test for isValidCell() function in maze class,
     * check whether a vertex is valid in maze
     * */
    @Test
    void testisValidCell(){
        maze maze=new maze(100,100,1,1,99,99);
        assertEquals(true,maze.isValidCell(1,1));//target function
        assertEquals(false,maze.isValidCell(-3,10));//target function
        assertEquals(false,maze.isValidCell(3,-10));//target function
        assertEquals(false,maze.isValidCell(99,100));//target function
    }

    /**
     * unit test for getUnvisitedNeighbors() function in maze class,
     * check whether the neighbors of A vertex in maze can be gotten successfully
     * */
    @Test
    void testgetUnvisitedNeighbors(){
        maze maze=new maze(10,10,1,1,9,9);
        ArrayList<Vertex> vertexArrayList = new ArrayList<Vertex>();
        vertexArrayList.add(maze.getMaze()[3][1]);
        vertexArrayList.add(maze.getMaze()[1][3]);
        assertEquals(vertexArrayList,maze.getUnvisitedNeighbors(1,1));//target function
        ArrayList<Vertex> vertexArrayList_1 = new ArrayList<Vertex>();
        vertexArrayList_1.add(maze.getMaze()[3][5]);
        vertexArrayList_1.add(maze.getMaze()[7][5]);
        vertexArrayList_1.add(maze.getMaze()[5][3]);
        vertexArrayList_1.add(maze.getMaze()[5][7]);
        assertEquals(vertexArrayList_1,maze.getUnvisitedNeighbors(5,5));//target function
    }

    /**
     * unit test for initializeMaze() function in maze class,
     * check whether a maze can be initialized successfully
     * */
    @Test
    void testinitializeMaze(){
        maze maze=new maze(50,50,1,1,49,25);
        maze.initializeMaze();//target function
        assertNotNull(maze.getMaze()[10][10]);
        assertNotNull(maze.getMaze()[49][49]);
        maze maze_wrong=new maze(50,50,1,1,-49,25);
        maze_wrong.initializeMaze();//target function
        assertNull(maze_wrong.getMaze()[0][0]);
        assertNull(maze_wrong.getMaze()[20][20]);
    }

    /**
     * unit test for initializeMaze() function in maze class,
     * check whether the required number of walls can be removed randomly and successfully
     * */
    @Test
    void testrandomly_remove_walls(){
        maze maze=new maze(100,100,1,1,99,99);
        maze maze_1=new maze(100,100,1,1,99,99);
        assertEquals(-1,maze.randomly_remove_walls(10000));//target function
        assertEquals(40,maze.randomly_remove_walls(40));//target function
        assertEquals(-1,maze.randomly_remove_walls(-10));//target function
        assertEquals(500,maze_1.randomly_remove_walls(500));//target function
    }
    //maze
    //vertex

    /**
     * unit test for Vertex() constructor in maze class,
     * check whether a vertex is created successfully,
     * and check whether the value is assigned successfully
     * */
    @Test
    void testVertex(){
        Vertex vertex=new Vertex(1,1);//target function
        assertEquals(1,vertex.getRow());
        assertEquals(1,vertex.getCol());
        assertEquals(true,vertex.isWall());
        assertEquals(false,vertex.isShortestPath());
        assertEquals(false,vertex.isVisited());
    }

    /**
     * unit test for getRow() function in maze class,
     * check whether the corresponding row of a vertex can be gotten successfully
     * */
    @Test
    void testgetRow() {
        Vertex vertex=new Vertex(213,345);
        assertEquals(213,vertex.getRow());//target function
        Vertex vertex_1=new Vertex(10,42);
        assertEquals(10,vertex_1.getRow());//target function
    }

    /**
     * unit test for getCol() function in maze class,
     * check whether the corresponding col of a vertex can be gotten successfully
     * */
    @Test
    void testgetCol() {
        Vertex vertex=new Vertex(213,345);
        assertEquals(345,vertex.getCol());//target function
        Vertex vertex_1=new Vertex(10,42);
        assertEquals(42,vertex_1.getCol());//target function
    }

    /**
     * unit test for isVisited() function in maze class,
     * check whether a vertex has been visited
     * */
    @Test
    void testisVisited() {
        Vertex vertex=new Vertex(98,20);
        assertEquals(false,vertex.isVisited());//target function
        Vertex vertex_1=new Vertex(3,0);
        assertEquals(false,vertex_1.isVisited());//target function
    }

    /**
     * unit test for isShortestPath() function in maze class,
     * check whether a vertex belongs to the shortest path
     * */
    @Test
    void testisShortestPath() {
        Vertex vertex=new Vertex(150,300);
        assertEquals(false,vertex.isShortestPath());//target function
        Vertex vertex_1=new Vertex(30,20);
        assertEquals(false,vertex_1.isShortestPath());//target function
    }

    /**
     * unit test for setVisited() function in maze class,
     * check whether the state of isvisited of a vertex can be modified successfully
     * */
    @Test
    void testsetVisited() {
        Vertex vertex=new Vertex(98,20);
        vertex.setVisited(true);//target function
        assertEquals(true,vertex.isVisited());
        Vertex vertex_1=new Vertex(3,0);
        vertex_1.setVisited(true);//target function
        vertex_1.setVisited(false);//target function
        assertEquals(false,vertex_1.isVisited());//target function
    }

    /**
     * unit test for setShortestPath() function in maze class,
     * check whether the state of isShortestPath of a vertex can be modified successfully
     * */
    @Test
    void testsetShortestPath() {
        Vertex vertex=new Vertex(35,20);
        vertex.setShortestPath(true);//target function
        assertEquals(true,vertex.isShortestPath());
        Vertex vertex_1=new Vertex(29,29);
        vertex_1.setShortestPath(false);//target function
        assertEquals(false,vertex_1.isShortestPath());
    }

    /**
     * unit test for isWall() function in maze class,
     * check whether a vertex is wall or not
     * */
    @Test
    void testisWall() {
        Vertex vertex=new Vertex(12,2);
        assertEquals(true,vertex.isWall());//target function
        Vertex vertex_1=new Vertex(1,50);
        assertEquals(true,vertex_1.isWall());//target function
    }

    /**
     * unit test for setShortestPath() function in maze class,
     * check whether the state of isWall of a vertex can be modified successfully
     * */
    @Test
    void testsetWall() {
        Vertex vertex=new Vertex(12,2);
        vertex.setWall(false); //target function
        assertEquals(false,vertex.isWall());
        Vertex vertex_1=new Vertex(1,50);
        vertex_1.setWall(true); //target function
        assertEquals(true,vertex_1.isWall());
    }
    //vertex
    //mazetocsv

    /**
     * unit test for mazetocsv() constructor in maze class,
     * check whether a mazetocsv is created successfully,
     * and check whether the objects are assigned successfully
     * */
    @Test
    void testmazetocsv(){
        maze maze=new maze(10,10,1,1,9,9);
        ShortestPath shortestPath=new ShortestPath(maze.getMaze());
        mazetocsv mazetocsv=new mazetocsv(maze.getMaze(), maze,shortestPath);//target function
        assertEquals(mazetocsv.vertices_maze,maze.getMaze());
        assertEquals(mazetocsv.maze,maze);
        assertEquals(mazetocsv.shortestPath,shortestPath);
    }

    /**
     * unit test for output_maze() function in maze class,
     * check whether csv file of maze_data can be output correctly and successfully
     * */
    @Test
    void testoutput_maze(){
        maze maze=new maze(30,30,24,1,1,24);
        maze.generateMaze();
        ShortestPath shortestPath=new ShortestPath(maze.getMaze());
        mazetocsv mazetocsv=new mazetocsv(maze.getMaze(), maze,shortestPath);
        mazetocsv.output_maze();//target function

        File file = new File("maze_data.csv");
        assertTrue(file.exists());
        assertEquals(file.length(),(maze.getCols()*3-1)*maze.getRows());

        maze maze_1=new maze(300,200,1,1,299,199);
        maze_1.generateMaze();
        ShortestPath shortestPath_1=new ShortestPath(maze.getMaze());
        mazetocsv mazetocsv_1=new mazetocsv(maze_1.getMaze(), maze_1,shortestPath_1);

        mazetocsv_1.output_maze();//target function

        File file_1 = new File("maze_data.csv");
        assertTrue(file_1.exists());
        assertEquals(file_1.length(),(maze_1.getCols()*3-1)*maze_1.getRows());
    }
    //mazetocsv

    //main
    /**
     * unit test for Main() function in maze class,
     * check whether a maze is created successfully,
     * and check whether two csv files are output successfully
     * */
    @Test
    void testmain() {
        String[] args=null;
        Main.main(args);//target function
        maze maze=new maze(30,30,12,1,1,29);
        maze.generateMaze();
        ShortestPath shortestPath=new ShortestPath(maze.getMaze());
        shortestPath.createShortestPath(12,1,1,29);
        File file = new File("shortestPath.csv");
        assertTrue(file.exists());
        File file_1 = new File("maze_data.csv");
        assertTrue(file_1.exists());
        assertEquals(file_1.length(),(maze.getCols()*3-1)*maze.getRows());
    }


    // Helper method to create a test maze
    /**
     * helper function for unit testing
     * used to create a test maze for shortest path
     */
    private Vertex[][] createMaze() {
        // Create a 5x5 maze with a path from (0,0) to (4,4)
        Vertex[][] maze = new Vertex[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                maze[i][j] = new Vertex(i, j);
            }
        }
        maze[1][1].setWall(false);
        maze[1][3].setWall(false);
        maze[2][1].setWall(false);
        maze[2][3].setWall(false);
        maze[3][1].setWall(false);
        maze[3][3].setWall(false);
        maze[3][2].setWall(false);

        return maze;
    }

    /**
     *  unit test to test displayShortestPath() function in ShortestPath Class
     *  test if a csv file able to create when called
     */
    @Test
    void testDisplayShortestPath() {
        // Create a test maze
        Vertex[][] maze = createMaze();
        ShortestPath shortestPath = new ShortestPath(maze);


        shortestPath.displayShortestPath(1, 1, 1, 3);//target function
        File file = new File("shortestPath.csv");
        Assertions.assertTrue(file.exists(), "File 'shortestPath.csv' should exist");

    }

    /**
     *  unit test to test ShortestPath() function in ShortestPath Class
     *  test if deep copy perform correctly
     */
    @Test
    void testShortestPath() {
        Vertex[][] maze = createMaze();
        ShortestPath shortestPath = new ShortestPath(maze);//target function
        assertTrue(shortestPath.path.isEmpty());
    }

    /**
     *  unit test to test createShortestPath() function in ShortestPath Class
     *  test shortest path is found if maze is clean correctly
     */
    @Test
    void testCreateShortestPath() {
        Vertex[][] maze = createMaze();
        ShortestPath shortestPath = new ShortestPath(maze);
        assertTrue(shortestPath.createShortestPath(1,1,1,3));//target function
    }

    /**
     *  unit test to test findShortestPath() function in ShortestPath Class
     *  test if the path created the same as the expected Path
     */
    @Test
    void testFindShortestPath() {
        Vertex[][] maze = createMaze(); // Create a test maze
        ShortestPath shortestPath = new ShortestPath(maze);
        LinkedList<Vertex> expectedPath = new LinkedList<>();

        expectedPath.add(new Vertex(1, 1));
        expectedPath.add(new Vertex(2, 1));
        expectedPath.add(new Vertex(3, 1));
        expectedPath.add(new Vertex(3, 2));
        expectedPath.add(new Vertex(3, 3));
        expectedPath.add(new Vertex(2, 3));
        expectedPath.add(new Vertex(1, 3));

        LinkedList<Vertex> actualPath = new LinkedList<>();
        shortestPath.findShortestPath(1,1,1,3, actualPath);//target function

        assertEquals(actualPath.size(), expectedPath.size());
        for (int i = 0; i < actualPath.size(); i++) {
            Vertex vertex1 = actualPath.get(i);
            Vertex vertex2 = expectedPath.get(i);

            assertEquals(vertex1.getRow(), vertex2.getRow());
            assertEquals(vertex1.getCol(), vertex2.getCol());
        }
    }

}