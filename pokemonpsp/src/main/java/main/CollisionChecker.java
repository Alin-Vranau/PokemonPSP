package main;

import Tile.Tile;
import entity.Entity;
public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    } 

    public boolean checkEntityCollision(Entity entity, int x, int y) {
        boolean collision = false;
        
        int entityLeftWorldX = x + entity.solidArea.x;
        int entityRightWorldX = x + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = y + entity.solidArea.y;
        int entityBottomWorldY = y + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int[][] mapTileNum = gp.getTileManager().mapTileNum;
        Tile[] tiles = gp.getTileManager().tile;

        int tileNum1 = -1; 
        int tileNum2 = -1;
        
        // Revisa las colisiones en todas las direcciones
        if (entity.direction.equals("up")) {
            entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
            tileNum1 = mapTileNum[entityLeftCol][entityTopRow];
            tileNum2 = mapTileNum[entityRightCol][entityTopRow];
        } else if (entity.direction.equals("down")) {
            entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
            tileNum1 = mapTileNum[entityLeftCol][entityBottomRow];
            tileNum2 = mapTileNum[entityRightCol][entityBottomRow];
        } else if (entity.direction.equals("left")) {
            entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
            tileNum1 = mapTileNum[entityLeftCol][entityTopRow];
            tileNum2 = mapTileNum[entityLeftCol][entityBottomRow];
        } else if (entity.direction.equals("right")) {
            entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
            tileNum1 = mapTileNum[entityRightCol][entityTopRow];
            tileNum2 = mapTileNum[entityRightCol][entityBottomRow];
        }

        if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
            collision = true;
        }

        return collision;
    }
}

