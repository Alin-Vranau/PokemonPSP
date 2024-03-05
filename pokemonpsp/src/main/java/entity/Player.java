package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import handlers.GameHandler;
import main.GamePanel;
import main.KeyHandler;
import main.PokemonSelection;
import objects.Pokemon;
import screens.screenPokedex;

public class Player extends Entity {
	KeyHandler keyH;
	public static String gender;
	public final int screenX;
	public final int screenY;
	public List<String> pokemonNames;
	private boolean battlePanelOpened = false;
	private boolean pokedexPanelOpened = false;	
	private screenPokedex pokedex;
	private String infoMessage = "";
	private boolean showInfoMessage = false;
	private final int PROXIMITY_RANGE = 50; // Este valor es en píxeles. Ajusta según la necesidad.


	

	public Player(GamePanel gp, KeyHandler keyH, PokemonSelection pokemonSelection) {
		super(gp);

		this.keyH = keyH;
		this.pokemonNames = pokemonSelection.getSelectedPokemonNames();

		//super.createPokemonTeam(pokemonNames);

		screenX = gp.screenWidth / 2 - (gp.tileSize/2);
		screenY = gp.screenHeight / 2 - (gp.tileSize/2);

		solidArea = new Rectangle();
		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 32;
		solidArea.height = 32;

		setDefaultValues();
		getPlayerImage();
		pokedex = new screenPokedex();
    	screenPokedex.llenarListaPokemon();
		pokedex.setLocationRelativeTo(null);

		initPokedex();
	}

	private void initPokedex() {
		pokedex = new screenPokedex();
		screenPokedex.llenarListaPokemon();
		pokedex.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				pokedexPanelOpened = false;
			}
		});
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

	public void healPokemons() {
		for (Pokemon pokemon : pokemonTeam) {
			pokemon.setActualHealth(pokemon.getHealth());
		}
	}

	public void update() {
		showInfoMessage = false;
		for (NPC_Personaje1 npc : gp.npcList) {
			if (isPlayerNearNPC(npc)) { // Verifica si el jugador está cerca del NPC
				if (!npc.getDefeated()) {
					// Si el NPC no está vencido, muestra el mensaje para iniciar la batalla
					infoMessage = "Pulsa \"E\" para comenzar batalla";
				} else {
					// Si el NPC está vencido, muestra el mensaje "Derrotado"
					infoMessage = "Derrotado";
				}
				showInfoMessage = true; // Indica que debe mostrarse el mensaje
				break; // Sale del bucle después de encontrar el primer NPC cercano
			}
		}
		NPC_Personaje1 nearNPC = getNearNPC(); // Obtén el NPC cercano, si existe
		if (keyH.ePressed && !battlePanelOpened && nearNPC != null) {
			// Aquí asumimos que getNearNPC ya no solo verifica la colisión, sino la proximidad
			GameHandler.hideGamePanel();
			GameHandler.showBattlePanel(nearNPC);
			battlePanelOpened = true;
			keyH.ePressed = false; // Asegúrate de restablecer el estado de la tecla "e"
		}
		if (keyH.pPressed) {
			if (pokedex == null) {
				// Crear la ventana de la Pokedex si no existe.
				pokedex = new screenPokedex();
				pokedex.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						// Cambiar el estado cuando la ventana se cierra.
						pokedexPanelOpened = false;
					}
				});
			}
			screenPokedex.pokemonSeen = GameHandler.sqliteHandler.getPokemonsSeen();
			screenPokedex.pokemonDefeacted = GameHandler.sqliteHandler.getPokemonsDefeated();
			// Alternar la visibilidad de la Pokedex.
			togglePokedexVisibility();
			keyH.pPressed = false; // Asegurar que la pulsación se maneje una sola vez.
		}
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
	public NPC_Personaje1 getNearNPC() {
		for (NPC_Personaje1 npc : gp.npcList) {
			double distance = Math.sqrt(Math.pow(worldX - npc.worldX, 2) + Math.pow(worldY - npc.worldY, 2));
			if (distance < PROXIMITY_RANGE && !npc.getDefeated()) {
				return npc;
			}
		}
		return null; // Ningún NPC está suficientemente cerca
	}
	

	public boolean isPlayerNearNPC(NPC_Personaje1 npc) {
		// Calcula la distancia entre el jugador y el NPC
		int deltaX = worldX - npc.worldX; // Diferencia en X
		int deltaY = worldY - npc.worldY; // Diferencia en Y
		double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY); // Teorema de Pitágoras para distancia
	
		// Verifica si la distancia es menor que el rango de proximidad
		return distance < PROXIMITY_RANGE;
	}
	

	private void togglePokedexVisibility() {
		SwingUtilities.invokeLater(() -> {
			if (!pokedexPanelOpened) {
				pokedex.setLocationRelativeTo(null); // Esto centrará la ventana
				pokedex.setVisible(true);
				pokedexPanelOpened = true;
				keyH.reset();
			} else {
				pokedex.setVisible(false);
				pokedexPanelOpened = false;
			}
		});
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
		if (showInfoMessage) {
			g2.setColor(Color.WHITE); // Establece el color del texto
			g2.setFont(new Font("Arial", Font.BOLD, 14)); // Establece la fuente del texto
			// Dibuja el mensaje en la pantalla. Ajusta las coordenadas (x, y) según sea necesario.
			g2.drawString(infoMessage, screenX - 150, screenY - 20);
		}
	}

	public void pokemonBattleFinished() {
		healPokemons();
		battlePanelOpened = false;
	}
}
