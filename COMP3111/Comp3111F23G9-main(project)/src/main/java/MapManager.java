import java.awt.*;

/**
 * class for displaying the maze on screen
 */
public class MapManager {
    private Vertex[][] maze;
    GamePanel gp;
    MapManager(Vertex[][] maze, GamePanel gp){
        this.maze = maze;
        this.gp = gp;
    }

    /**
     * function for displaying the maze on screen
     */
    public void draw(Graphics2D g2){
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                int x = col * gp.tileSize;
                int y = row * gp.tileSize;

                if (maze[row][col].isWall()) {
                    g2.setColor(Color.DARK_GRAY);
                    g2.fillRect(x, y, gp.tileSize, gp.tileSize);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRect(x, y, gp.tileSize, gp.tileSize);
                }
            }
            g2.setColor(Color.lightGray);
            g2.fillRect((gp.maze.getEntryCol()-1)*gp.tileSize, gp.maze.getEntryRow()*gp.tileSize, gp.tileSize, gp.tileSize);
            g2.setColor(Color.MAGENTA);
            g2.fillRect((gp.maze.getExitCol()+1)*gp.tileSize, gp.maze.getExitRow()*gp.tileSize, gp.tileSize, gp.tileSize);
        }
    }
}
