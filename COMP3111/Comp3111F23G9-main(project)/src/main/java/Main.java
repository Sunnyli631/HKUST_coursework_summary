import javax.swing.*;

/**
 * main function, run to start the maze game
 */
public class Main {
    public static void main(String[] args) {
        int width = 30;
        int height = 30;
        int entryX = 0;
        int entryY = 12;
        int exitX = 29;
        int exitY = 1;

        maze maze = new maze(width, height, entryY, entryX, exitY, exitX);
        maze.generateMaze();
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D");
        GamePanel gamePanel = new GamePanel(maze, width,height);
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.startGameThread();

        ShortestPath shortestPath =new ShortestPath(maze.getMaze());
        mazetocsv output= new mazetocsv(maze.getMaze(),maze,shortestPath);
        output.output_maze();


    }
}
