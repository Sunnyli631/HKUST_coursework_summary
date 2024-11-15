import java.awt.*;

public class Entity {
    /**
     * position of the Entity
     */
    public int x,y;
    /**
     * speed
     */
    public int speed;
    /**
     * hit box
     */
    public Rectangle solidArea;
    public boolean collisionOn = false;
    public String direction = "null";

    /**
     *  set function to set coordinate
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setPos(int x , int y){
        this.x = x;
        this.y = y;
    }

    /**
     * set function to set speed
     * @param speed speed
     */
    public void setSpeed(int speed){
        this.speed = speed;
    }
}

