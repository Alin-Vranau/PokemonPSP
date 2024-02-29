package handlers;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import entity.NPC_Personaje1;
import main.GamePanel;
import objects.Type;
import screens.BattlePanel;

public class GameHandler {

    private static int width = 700;
    private static int height = 500;
    private static JFrame mainWindow;
    private static GamePanel gamePanel;
    private static BattlePanel battlePanel;

    public GameHandler() {

        mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setResizable(false);
        mainWindow.setTitle("Pokemon");
        Type.initializeTypes();
        // Crear el panel del juego y pasarlo a la ventana
        gamePanel = new GamePanel(mainWindow);
        mainWindow.add(gamePanel);

        gamePanel.startGameThread();

        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        

        mainWindow.setVisible(true);
    }

    public static void hideGamePanel() {
        mainWindow.remove(gamePanel);
        gamePanel.stopThread();
        gamePanel.setVisible(false);
    }

    public static void showGamePanel() {
        mainWindow.add(gamePanel);
        gamePanel.startGameThread();
        gamePanel.setVisible(true);
        // Para que el personaje se mueva al volver de un combate
        gamePanel.grabFocus();
        
        mainWindow.revalidate();
    }

    public static void showBattlePanel(NPC_Personaje1 npc){
        
        battlePanel = new BattlePanel(width, height, gamePanel.player, npc);
        

        
        mainWindow.add(battlePanel, BorderLayout.CENTER);
        
        System.out.println("a");
        mainWindow.revalidate();
    }

    public static void hideBattlePanel() {
        mainWindow.remove(battlePanel);
        battlePanel.setVisible(false);
    }


    public static void main(String[] args) {
        new GameHandler();
    }
    
}
