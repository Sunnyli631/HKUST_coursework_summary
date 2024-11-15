import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * class for handling Key Events
 */
public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    GamePanel gp;
    public KeyHandler(GamePanel gp){
        upPressed = downPressed = leftPressed = rightPressed = false;
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * function for handling WASD, B and Enter press
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if(gp.gameState == gp.titleState){
            if(code == KeyEvent.VK_W){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0)
                    gp.ui.commandNum = 0;
            }else if(code == KeyEvent.VK_S){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 3)
                    gp.ui.commandNum = 3;
            }else if(code == KeyEvent.VK_ENTER){
                handleCommand();
            }
        }

        if (gp.gameState == gp.functionAState || gp.gameState == gp.functionBState) {
            if (code == KeyEvent.VK_B) {
                gp.gameState = gp.titleState;
            }
        }

        if(gp.gameState == gp.functionCState){
            if(code == KeyEvent.VK_W){
                upPressed = true;
                leftPressed = false;
                rightPressed = false;
                downPressed = false;
            }
            if(code == KeyEvent.VK_A){
                leftPressed = true;
                rightPressed = false;
                upPressed = false;
                downPressed = false;
            }
            if(code == KeyEvent.VK_S){
                downPressed = true;
                upPressed = false;
                rightPressed = false;
                leftPressed = false;
            }
            if(code == KeyEvent.VK_D){
                rightPressed = true;
                leftPressed = false;
                upPressed = false;
                downPressed = false;
            }
        }

        if(gp.gameState == gp.endState){
            if(code == KeyEvent.VK_W){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0)
                    gp.ui.commandNum = 0;
            }else if(code == KeyEvent.VK_S){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 1)
                    gp.ui.commandNum = 1;
            }else if(code == KeyEvent.VK_ENTER){
                handleCommand();
            }
        }
    }

    /**
     * function for handling WASD release
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
    }

    /**
     * function for handling selection on the GUI
     */
    protected void handleCommand(){
        switch (gp.ui.selectedCommand){
            case "function A":
                gp.gameState = gp.functionAState;
                break;
            case "function B":
                gp.gameState = gp.functionBState;
                break;
            case "function C":
                gp.gameState = gp.functionCState;
                break;
            case "quit":
                gp.gameState = gp.quitState;
                break;
            case "title":
                gp.gameState = gp.titleState;
                gp.maze = new maze(gp.maze.getRows(),gp.maze.getCols(),gp.maze.getEntryRow(),gp.maze.getEntryCol(),gp.maze.getExitRow(),gp.maze.getExitCol());
                gp.maze.generateMaze();
                gp.map = new MapManager(gp.maze.getMaze(), gp);
                gp.path = new ShortestPath(gp.maze.getMaze());
                gp.jerry.setPos((gp.maze.getEntryCol())*gp.tileSize,(gp.maze.getEntryRow())*gp.tileSize);
                gp.tom.setPos(gp.maze.getExitCol()*gp.tileSize, gp.maze.getExitRow()*gp.tileSize);
                gp.tom.path = gp.path;
                gp.isEnded = false;
                gp.jerryWin = false;

                mazetocsv output= new mazetocsv(gp.maze.getMaze(),gp.maze,gp.path);
                output.output_maze();
                break;
        }
        gp.ui.commandNum = 0;
    }
}
