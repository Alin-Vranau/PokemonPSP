package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import java.util.Random;

public class NPC_Personaje1 extends Entity {
    
    private String imagePath;
    private boolean defeated = false;

    public NPC_Personaje1(GamePanel gp, String characterName, String imagePath) {
        super(gp);
        this.imagePath = imagePath;
        direction = "down";
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        speed = 0;
        super.createNPCPokemonTeam();
        getImage();
        setPosition();
    }

    public boolean getDefeated() {
        return defeated;
    }

    public void getImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void setPosition() {
        Random random = new Random();
        boolean validPosition = false;

        while (!validPosition) {
            // Genera coordenadas aleatorias dentro de los límites del mapa
            int x = random.nextInt(gp.maxWorldCol);
            int y = random.nextInt(gp.maxWorldRow);

            // Convierte coordenadas de tile a coordenadas del mundo
            int worldX = x * gp.tileSize;
            int worldY = y * gp.tileSize;

            // Comprueba si el tile en esa posición no tiene colisión
            if (!gp.getTileManager().tile[gp.getTileManager().mapTileNum[x][y]].collision) {
                // Si el tile es transitable, actualiza la posición del NPC
                this.worldX = worldX;
                this.worldY = worldY;
                validPosition = true;
            }
        }
    }

    public void defeated() {
        defeated = true;
    }
    
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        g2.drawImage(up1, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
