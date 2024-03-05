package handlers;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;

import Threads.PrepareNPCPokemons;
import entity.NPC_Personaje1;
import main.GamePanel;
import objects.Type;
import screens.BattlePanel;
import screens.LoadingScreen;

public class GameHandler {

    private static int width = 700;
    private static int height = 500;
    private static JFrame mainWindow;
    private static GamePanel gamePanel;
    private static BattlePanel battlePanel;
    private static LoadingScreen loadingScreen;

    public static SqliteHandler sqliteHandler;

    public GameHandler() {

        mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setResizable(false);
        mainWindow.setTitle("Pokemon");

        sqliteHandler = new SqliteHandler();

        showLoadingScreen();

        startGame();

        hideGamePanel();

        

        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        

        mainWindow.setVisible(true);

        if (Type.typesList == null){
            Type.initializeTypes();
        }
        
        
        // Inicializa el juego la primera vez
        //startGame();

        
        
    }


    public static void startGame() {

        if (gamePanel != null) {
            mainWindow.remove(gamePanel);
        }

        gamePanel = new GamePanel(mainWindow);

        gamePanel.startGameThread();

        Thread t = new PrepareNPCPokemons((ArrayList)gamePanel.npcList);
        t.start();
    }

    public static void showLoadingScreen() {
        loadingScreen = new LoadingScreen(width, height);

        mainWindow.add(loadingScreen);

        
    }

    public static void hideLoadingScreen() {

        loadingScreen.closeWindow();

        mainWindow.remove(loadingScreen);


    }


    public static void hideGamePanel() {
        mainWindow.remove(gamePanel);
        gamePanel.stopThread();
        gamePanel.setVisible(false);
    }

    public static void showGamePanel() {
        mainWindow.add(gamePanel);
        mainWindow.setLocationRelativeTo(null);
        gamePanel.startGameThread();
        gamePanel.setVisible(true);
        mainWindow.pack();
        
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
