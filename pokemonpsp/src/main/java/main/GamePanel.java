package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Tile.TileManager;
import entity.Player;
import objects.Pokemon;
import objects.Type;
import screens.BattlePanel;

public class GamePanel extends JPanel implements Runnable {
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


	// FPS
	int FPS = 60;

	TileManager tileM = new TileManager(this); 
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	public CollisionChecker cChecker = new CollisionChecker(this);
	public Player player = new Player(this, keyH);

	JFrame window;
	
	
	public GamePanel(JFrame window) {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		this.window = window;

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_E) {
					setEnabled(false);
					
					window.setVisible(false);
					JFrame win = new JFrame();
       				win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					win.setBounds(window.getX(), window.getY(), window.getWidth(), window.getHeight());
        			win.setResizable(false);
        			win.setTitle("Batalla");

        			Type.initializeTypes();

					// Crear el panel del juego y pasarlo a la ventana
					JPanel gamePanel = new BattlePanel(window.getWidth(), window.getHeight(), new Pokemon("Charizard"),
							new Pokemon("Vaporeon") );
					win.add(gamePanel);
        
        
					//window.pack();
					//window.setLocationRelativeTo(null);
					win.setVisible(true);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
			
		});
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
		while (gameThread != null) {
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
	}

	public void update() {
		player.update();
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;

		tileM.draw(g2);

		player.draw(g2);

		g2.dispose();
	}

}
