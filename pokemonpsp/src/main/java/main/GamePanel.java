package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.HashSet;
import java.util.Set;
import Tile.TileManager;
import entity.NPC_Personaje1;
import entity.Player;
import handlers.GameHandler;
import screens.BattlePanel;
import screens.screenStart;

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
	public Set<Point> npcPositions = new HashSet<>();

	private JPanel pauseMenu;

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

    public boolean isPositionOccupied(int x, int y) {
        return npcPositions.contains(new Point(x, y));
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


	public boolean checkAllNPCsDefeated() {

		boolean win = true;

		for (NPC_Personaje1 npc : npcList) {
			if (! npc.getDefeated()) {
				win = false;
				break;
			}
		}

		return win;

	}

	public void showPauseMenu(){

		if (pauseMenu == null) {
			pauseMenu = new JPanel();
			pauseMenu.setOpaque(false);
			pauseMenu.setSize(screenWidth, screenHeight);
			pauseMenu.setLayout(new GridBagLayout());
			pauseMenu.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						hidePauseMenu();
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}
				
			});
			Insets i1 = new Insets(50, 50, 25, 50);

			GridBagConstraints resumeConstraints = new GridBagConstraints();
			resumeConstraints.insets = i1;
			resumeConstraints.gridy = 0;
			JButton resumeButton = new JButton("Reanudar");
			resumeButton.setMinimumSize(new Dimension((int)(screenWidth*0.5),(int)(screenHeight*0.2)));
			resumeButton.setPreferredSize(new Dimension((int)(screenWidth*0.5),(int)(screenHeight*0.2)));

			resumeButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					hidePauseMenu();
				}
				
			});


			Insets i2 = new Insets(50, 50, 25, 50);

			GridBagConstraints pauseConstraints = new GridBagConstraints();
			pauseConstraints.insets = i2;
			pauseConstraints.gridy = 1;

			JButton exitButton = new JButton("Salir");

			exitButton.setMinimumSize(new Dimension((int)(screenWidth*0.5),(int)(screenHeight*0.2)));
			exitButton.setPreferredSize(new Dimension((int)(screenWidth*0.5),(int)(screenHeight*0.2)));

			exitButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					stopThread();
					GameHandler.mainWindow.dispose();
                    new screenStart().setVisible(true);
				}
			});

			pauseMenu.add(resumeButton, resumeConstraints);
			pauseMenu.add(exitButton, pauseConstraints);
			GameHandler.mainWindow.add(pauseMenu, BorderLayout.CENTER);
		}

		pauseMenu.setVisible(true);
		GameHandler.hideGamePanel();
		//setVisible(false);
		pauseMenu.revalidate();
		pauseMenu.repaint();
		pauseMenu.grabFocus();
	}

	public void hidePauseMenu() {
		pauseMenu.setVisible(false);
		GameHandler.showGamePanel();
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
