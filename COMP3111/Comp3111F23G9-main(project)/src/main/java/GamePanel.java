import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 *  a Class of game environment
 */
public class GamePanel extends JPanel implements Runnable{
    final int ogTileSize = 10;
    final int scale = 1;
    final int tileSize = ogTileSize * scale;
    private int maxScreenCol;
    private int maxScreenRow;
    int screenWidth;
    int screenHeight;
    int FPS = 60;
    final int titleState = -1;
    final int functionAState = 0;
    final int functionBState = 1;
    final int functionCState = 2;
    final int endState = 3;

    final int quitState = 4;
    int gameState = titleState;
    boolean jerryWin = false;
    boolean isEnded = false;
    KeyHandler kH = new KeyHandler(this);
    Thread gameThread;
    Player jerry;
    Enemy tom;
    MapManager map;
    maze maze;
    ShortestPath path;
    UI ui;
    public CollisionChecker cChecker;
    public GamePanel(maze maze, int maxCol, int maxRow){

        this.maze = maze;
        this.maxScreenCol = maxCol;
        this.maxScreenRow = maxRow;
        this.screenWidth = tileSize * this.maxScreenCol;
        this.screenHeight = tileSize * this.maxScreenRow + 50;
        this.screenHeight += 25; // Add 25 pixels for the line
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(kH);
        this.setFocusable(true);
        this.map = new MapManager(this.maze.getMaze(), this);
        this.cChecker = new CollisionChecker(this);
        this.path = new ShortestPath(maze.getMaze());
        this.ui = new UI(this);
        jerry = new Player(this, kH);
        jerry.setSpeed(1);
        jerry.setPos((maze.getEntryCol())*tileSize,(maze.getEntryRow())*tileSize);
        tom = new Enemy(this, path);
        tom.setSpeed(1);
        tom.setPos(maze.getExitCol()*tileSize, maze.getExitRow()*tileSize);

    }

    /**
     *  create a thread for this class to run
     */
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * this function will be called repeatedly in the thread
     * this will be called 60 times per seconds
     */
    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null){

            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                if(remainingTime < 0){
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * show the maze
     */
    private void functionA(Graphics2D g2){
        map.draw(g2);
    }

    /**
     * show the maze and the shortest path
     */
    private void functionB(Graphics2D g2){
        map.draw(g2);
        path.createShortestPath(maze.getEntryRow(),maze.getEntryCol(),maze.getExitRow(),maze.getExitCol());
        path.draw(g2);
    }

    /**
     * the game starts
     */
    private void functionC(Graphics2D g2){
        map.draw(g2);
//        path.draw(g2);
        jerry.draw(g2);
        tom.draw(g2);
        String text = "Press WASD to Play";
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = (screenWidth - textWidth) / 2;
        int textY = screenHeight - 25;
        g2.setColor(Color.WHITE);
        g2.drawString(text, textX, textY);
    }

    /**
     * function for updating game component like Player, Enemy, ShortestPath class
     */
    public void update(){
        if(gameState == quitState){
            System.exit(0);
        }

        if(gameState == functionCState){
            path.createShortestPath(tom.y/10, tom.x/10, (jerry.y+jerry.solidArea.y)/10, (jerry.x+jerry.solidArea.x)/10);
            jerry.update();
            tom.update();
            cChecker.checkEntity(jerry, tom);
            if(isEnded){
                gameState = endState;
            }
        }
    }

    /**
     * function for displaying the game component on the screen
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if(gameState == titleState){
            ui.draw(g2);
        }else if(gameState == functionAState){
            functionA(g2);
        }else if(gameState == functionBState){
            functionB(g2);
        }else if(gameState == functionCState){
            functionC(g2);
        }else{
            ui.draw(g2);
        }

        if (gameState == functionAState || gameState == functionBState){
            String text = "Press B to return to title";
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textX = (screenWidth - textWidth) / 2;
            int textY = screenHeight - 25;
            g2.setColor(Color.WHITE);
            g2.drawString(text, textX, textY);
        }

        g2.dispose();
    }

}
