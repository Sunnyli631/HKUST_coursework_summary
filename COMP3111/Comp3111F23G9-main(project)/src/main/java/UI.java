import java.awt.*;

/**
 * class for UI component
 */
public class UI {
    GamePanel gp;
    Graphics2D g2;
    int commandNum = 0;
    String selectedCommand = "null";
    UI(GamePanel gp){
        this.gp = gp;
    }
    private int getXForCenterText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }

    /**
     * function for Title Screen display
     */
    private void drawTitleScreen(){
        String title = "Tom and Jerry";
        String text1="Press W and S to switch between each function,";
        String text2=" and press Enter to select.";
        String[] selections = {"Function A(Generate Maze)","Function B(Find Shortest Path)","Function C(Tom catches Jerry)","quit"};
        int selectionsGap = 3*gp.tileSize;
        int y = 4*gp.tileSize;
        int texty1= 10*gp.tileSize;
        int texty2= 12*gp.tileSize;
        int x = getXForCenterText(title);
        int textx1 = getXForCenterText(text1);
        int textx2 = getXForCenterText(text2);
        g2.setColor(Color.white);
        g2.drawString(title, x, y);
        g2.drawString(text1, textx1, texty1);
        g2.drawString(text2, textx2, texty2);
        y += 13*gp.tileSize;

        int cursorY = y;
        int cursorX;

        for(int i = 0; i < selections.length; i++){
            x = getXForCenterText(selections[i]);
            g2.drawString(selections[i], x, y);
            y += selectionsGap;
        }

        cursorY += commandNum*selectionsGap;
        cursorX = getXForCenterText(selections[commandNum]);

        g2.drawString(">", cursorX-2*gp.tileSize, cursorY);
        switch (commandNum){
            case 0:
                selectedCommand = "function A";
                break;
            case 1:
                selectedCommand = "function B";
                break;
            case 2:
                selectedCommand = "function C";
                break;
            case 3:
                selectedCommand = "quit";
        }
    }

    /**
     * function for End Game screen displaying aka jerry wins or tom loses
     */
    private void drawEndGameScreen(){
        String declaration;
        String[] selections = {"Go back to title screen", "quit"};
        int selectionsGap = 3*gp.tileSize;
        if(gp.jerryWin)
            declaration = "You Escaped and won !";
        else
            declaration = "Tom caught you :(";
        int x = getXForCenterText(declaration);
        int y = gp.screenHeight/2 - selectionsGap;
        g2.setColor(Color.white);
        g2.drawString(declaration, x, y);
        y += selectionsGap;
        int cursorY = y;
        int cursorX;
        for(int i = 0; i < selections.length; i++){
            x = getXForCenterText(selections[i]);
            g2.drawString(selections[i], x, y);
            y += selectionsGap;
        }

        cursorY += commandNum*selectionsGap;
        cursorX = getXForCenterText(selections[commandNum]);
        g2.drawString(">", cursorX-2*gp.tileSize, cursorY);

        switch (commandNum){
            case 0:
                selectedCommand = "title";
                break;
            case 1:
                selectedCommand = "quit";
                break;
        }
    }

    /**
     *  function for displaying the Title screen or endgame screen base on the state of the game
     */
    public void draw(Graphics2D g2){
        this.g2 = g2;
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }else if(gp.gameState == gp.endState){
            drawEndGameScreen();
        }
    }

}
