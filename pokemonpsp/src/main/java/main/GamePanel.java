package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Tile.TileManager;
import entity.NPC_Personaje1;
import entity.Player;
import screens.BattlePanel;

public class GamePanel extends JPanel implements Runnable {
	public List<NPC_Personaje1> npcList = new ArrayList<>();
	// ajustes de la pantalla
	final int originalTileSize = 16; // 16x16
	final int scale = 3;

	public final int tileSize = originalTileSize * scale; // 48x48
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; // 768px
	public final int screenHeight = tileSize * maxScreenRow; // 576px
	
	// World settings
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	private JFrame window;
	BattlePanel battlePanel;
	// FPS
	int FPS = 60;
	public TileManager tileM = new TileManager(this); 
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	public CollisionChecker cChecker = new CollisionChecker(this);
	public Player player;

	private boolean runThread = true;
	
	public GamePanel(JFrame window) {
		this.window = window;
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		PokemonSelection pokemonSelection = new PokemonSelection();
    	pokemonSelection.selectPokemon();
		this.player = new Player(this, keyH, pokemonSelection);
		addRandomNPCs();
	}

	private void addRandomNPCs() {
        List<Integer> imageNumbers = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            imageNumbers.add(i);
        }
        Collections.shuffle(imageNumbers); // Mezclar los números para obtener un orden aleatorio
        for (int i = 0; i < 5; i++) { // Añadir 5 NPCs a la lista
            int imageNumber = imageNumbers.get(i);
            NPC_Personaje1 npc = new NPC_Personaje1(this, "personaje" + imageNumber, "/npcs/npc_" + imageNumber + ".png");
            npcList.add(npc);
        }
    }

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		while (gameThread != null && runThread) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if (timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}

		runThread = true;
	}

	public void stopThread(){
		runThread = false;
		
	}

	public void update() {
		for (NPC_Personaje1 npc : npcList) {
			if (npc != null) {
				npc.update();
			}
		}
		player.update();
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;

		tileM.draw(g2);
		
		for (NPC_Personaje1 npc : npcList) {
			if (npc != null) {
				npc.draw(g2);
			}
		}

		player.draw(g2);

		g2.dispose();
	}

	public TileManager getTileManager() {
		return tileM;
	}	


	public void openBattlePanel(NPC_Personaje1 npc) {
        // Crear el panel de batalla // TODO modificar
        battlePanel = new BattlePanel(window.getWidth(), window.getHeight(), player, npc);
        // Añadir el panel de batalla a la ventana
        window.add(battlePanel);
        window.pack();
        // Ocultar GamePanel
        this.setVisible(false);
        // Mostrar el panel de batalla
        battlePanel.setVisible(true);
    }

	public void closeBattlePanel() {
        // Verificar si el panel de batalla existe
        if (battlePanel != null) {
            // Cerrar el panel de batalla
            battlePanel.setVisible(false);
            battlePanel = null;
            // Mostrar GamePanel
            this.setVisible(true);
        }
    }

}
