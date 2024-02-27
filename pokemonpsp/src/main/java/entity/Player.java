package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.PokemonSelection;

public class Player extends Entity {
	KeyHandler keyH;
	public static String gender;
	public final int screenX;
	public final int screenY;
	private List<String> pokemonNames;
	private boolean battlePanelOpened = false;

	public Player(GamePanel gp, KeyHandler keyH, PokemonSelection pokemonSelection) {
		super(gp);

		this.keyH = keyH;
		this.pokemonNames = pokemonSelection.getSelectedPokemonNames();

		screenX = gp.screenWidth / 2 - (gp.tileSize/2);
		screenY = gp.screenHeight / 2 - (gp.tileSize/2);

		solidArea = new Rectangle();
		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 32;
		solidArea.height = 32;

		setDefaultValues();
		getPlayerImage();
	}

	public void setDefaultValues() {
		worldX = gp.tileSize * 10;
		worldY = gp.tileSize * 9;
		speed = 4;
		direction = "down";
	}

	public void getPlayerImage() {
		try {
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/" + gender + "_up_1.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream("/player/" + gender + "_up_2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/" + gender + "_down_1.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/player/" + gender + "_down_2.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/" + gender + "_left_1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/" + gender + "_left_2.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/" + gender + "_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/player/" + gender + "_right_2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		collisionOn = false;
		if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
			int nextX = worldX;
			int nextY = worldY;
	
			if (keyH.upPressed) {
				direction = "up";            
				nextY -= speed;
			} else if (keyH.downPressed) {
				direction = "down";            
				nextY += speed;
			} else if (keyH.leftPressed) {
				direction = "left";                
				nextX -= speed;
			} else if (keyH.rightPressed) {
				direction = "right";
				nextX += speed;
			}
	
			// Verificación de colisión usando la nueva función
			collisionOn = gp.cChecker.checkEntityCollision(this, nextX, nextY);
			// Verificación adicional de colisión con el NPC solo si no hay colisión con tiles 
			if (!collisionOn) {
				for (NPC_Personaje1 npc : gp.npcList) {
					if (checkCollisionWithNPC(nextX, nextY, npc)) {
						collisionOn = true;
						if (keyH.ePressed && !battlePanelOpened) {
							gp.setVisible(false);
							gp.openBattlePanel();
							battlePanelOpened = true;
						}
						break;
					}
				}
			}
	
			// Mover al jugador si no hay colisión
			if (!collisionOn) {
				worldX = nextX;
				worldY = nextY;
			}
	
			// Administración de la animación del sprite
			spriteCounter++;
			if (spriteCounter > 12) {
				spriteNum = spriteNum == 1 ? 2 : 1;
				spriteCounter = 0;
			}
		}
	}

	public boolean checkCollisionWithNPC(int nextX, int nextY, NPC_Personaje1 npc) {
        Rectangle playerFutureRect = new Rectangle(nextX + this.solidArea.x, nextY + this.solidArea.y, this.solidArea.width, this.solidArea.height);
        Rectangle npcRect = new Rectangle(npc.worldX + npc.solidArea.x, npc.worldY + npc.solidArea.y, npc.solidArea.width, npc.solidArea.height);
    
        return playerFutureRect.intersects(npcRect);
    } 
	

	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		switch (direction) {
		case "up":
			if (spriteNum == 1) {
				image = up1;
			}
			if (spriteNum == 2) {
				image = up2;
			}
			break;
		case "down":
			if (spriteNum == 1) {
				image = down1;
			}
			if (spriteNum == 2) {
				image = down2;
			}
			break;
		case "left":
			if (spriteNum == 1) {
				image = left1;
			}
			if (spriteNum == 2) {
				image = left2;
			}
			break;
		case "right":
			if (spriteNum == 1) {
				image = right1;
			}
			if (spriteNum == 2) {
				image = right2;
			}
			break;
		}
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
	}
}
