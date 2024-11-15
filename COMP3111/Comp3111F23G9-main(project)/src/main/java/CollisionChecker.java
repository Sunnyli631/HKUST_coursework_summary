/**
 * class for Collision checking for Entity class
 */
public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp){ this.gp = gp; }

    /**
     *
     * @param e1 Entity class to be checked if collision happened
     * @param e2 Entity class to be checked if collision happened
     */
    public void checkEntity(Entity e1, Entity e2){
        int e1LeftX = e1.x + e1.solidArea.x;
        int e1TopY = e1.y + e1.solidArea.y;

        int e2LeftX = e2.x + e2.solidArea.x;
        int e2TopY = e2.y + e2.solidArea.y;


        int deltaX = Math.abs(e1LeftX - e2LeftX);
        int deltaY = Math.abs(e1TopY - e2TopY);
        if(deltaX <= e1.solidArea.width && deltaY <= e1.solidArea.height){
            gp.isEnded = true;
            gp.jerryWin = false;
        }
    }

    /**
     * check collision with walls
     * @param entity Entity class to be checked if collision with the wall happened
     */
    public void checkTile(Entity entity){
        int entityLeftX = entity.x + entity.solidArea.x;
        int entityRightX = entity.x + entity.solidArea.x + entity.solidArea.width;
        int entityTopY = entity.y + entity.solidArea.y;
        int entityBottomY = entity.y + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftX/gp.tileSize;
        int entityRightCol = entityRightX/gp.tileSize;
        int entityTopRow = entityTopY/gp.tileSize;
        int entityBottomRow = entityBottomY/gp.tileSize;

        Vertex v1, v2;

        switch (entity.direction){
            case "up":
                entityTopRow = (entityTopY - entity.speed)/gp.tileSize;
                v1 = gp.maze.getMaze()[entityTopRow][entityLeftCol];
                v2 = gp.maze.getMaze()[entityTopRow][entityRightCol];
                if(v1.isWall() || v2.isWall())
                    entity.collisionOn = true;
                break;
            case "down":
                entityBottomRow = (entityBottomY + entity.speed)/gp.tileSize;
                v1 = gp.maze.getMaze()[entityBottomRow][entityLeftCol];
                v2 = gp.maze.getMaze()[entityBottomRow][entityRightCol];
                if(v1.isWall() || v2.isWall())
                    entity.collisionOn = true;
                break;
            case "left":
                entityLeftCol = (entityLeftX - entity.speed)/gp.tileSize;
                if(entityLeftX < gp.maze.getEntryCol()){
                    entity.collisionOn = true;
                    break;
                }
                v1 = gp.maze.getMaze()[entityBottomRow][entityLeftCol];
                v2 = gp.maze.getMaze()[entityTopRow][entityLeftCol];
                if(v1.isWall() || v2.isWall())
                    entity.collisionOn = true;
                break;
            case "right":
                entityRightCol = (entityRightX + entity.speed)/gp.tileSize;
                if(entityRightCol > gp.maze.getExitCol()){
                    entity.collisionOn = true;
                    gp.jerryWin = true;
                    gp.isEnded = true;
                    break;
                }
                v1 = gp.maze.getMaze()[entityBottomRow][entityRightCol];
                v2 = gp.maze.getMaze()[entityTopRow][entityRightCol];
                if(v1.isWall() || v2.isWall())
                    entity.collisionOn = true;
                break;
        }
    }
}
