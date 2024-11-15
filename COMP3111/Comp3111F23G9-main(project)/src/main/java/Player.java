import java.awt.*;

/**
 * Player Class, user can control this class
 */
public class Player extends Entity{
    GamePanel gp;
    KeyHandler kH;
    public Player(GamePanel gp, KeyHandler kH){
        this.gp = gp;
        this.kH = kH;
        solidArea = new Rectangle(2,2, gp.tileSize-6, gp.tileSize-6);
    }

    /**
     *  updating player position base on key pressed
     */
    public void update(){
//        System.out.println(gp.maze.getMaze()[1][12].isWall());
        if(kH.upPressed || kH.downPressed || kH.leftPressed || kH.rightPressed){
            if(kH.upPressed){
                direction = "up";
            }
            if(kH.downPressed){
                direction = "down";
            }
            if(kH.leftPressed){
                direction = "left";
            }
            if(kH.rightPressed){
                direction = "right";
            }
        }else{
            direction = "null";
        }


        collisionOn = false;
        gp.cChecker.checkTile(this);
        if(!collisionOn){
            switch (direction){
                case "up":
                    y -= speed;
                    break;
                case "down":
                    y += speed;
                    break;
                case "left":
                    x -= speed;
                    break;
                case "right":
                    x += speed;
                    break;
            }
        }
    }

    /**
     * function for displaying the Player class on the screen
     */
    public void draw(Graphics2D g2){
        g2.setColor(Color.orange);
        g2.fillRect(x,y, gp.tileSize, gp.tileSize);
    }
}
