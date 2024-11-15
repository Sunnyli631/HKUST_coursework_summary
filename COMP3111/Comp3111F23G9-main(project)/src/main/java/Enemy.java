import java.awt.*;

/**
 * Enemy Class which will be Tom
 */
public class Enemy extends Entity{
    GamePanel gp;
    /**
     * for tom to chase jerry
     */
    ShortestPath path;
    public Enemy(GamePanel gp, ShortestPath path){
        this.gp = gp;
        this.path = path;
        solidArea = new Rectangle(2,2, gp.tileSize-6, gp.tileSize-6);
    }

    /**
     * for updating tom position base on the content of variable path
     */
    public void update(){
        if(!path.path.isEmpty()){
            Vertex cell = path.path.pop();
            int deltaX = cell.getCol() - x/10;
            int deltaY = cell.getRow() - y/10;
            if(x == cell.getCol()*10 && y == cell.getRow()*10){
                if(path.path.isEmpty()) {
                    direction = "null";
                    return;
                }
                cell = path.path.pop();
                deltaX = cell.getCol()*10 - x;
                deltaY = cell.getRow()*10 - y;
                if(deltaY < 0){
                    direction = "up";
                }else if(deltaY > 0){
                    direction = "down";
                }else if(deltaX < 0){
                    direction = "left";
                }else if(deltaX > 0){
                    direction = "right";
                }
            }else{
                if(deltaY > 0){
                    direction = "up";
                    System.out.println(direction);
                }else if(deltaY < 0){
                    direction = "down";
                    System.out.println(direction);
                }else if(deltaX < 0){
                    direction = "left";
                    System.out.println(direction);
                }else if(deltaX > 0){
                    direction = "right";
                    System.out.println(direction);
                }
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


    }

    /**
     * function for displaying Tom on the screen
     */
    public void draw(Graphics2D g2){
        g2.setColor(Color.green);
        g2.setColor(Color.blue);
        g2.fillRect(x,y, gp.tileSize, gp.tileSize);
    }
}
