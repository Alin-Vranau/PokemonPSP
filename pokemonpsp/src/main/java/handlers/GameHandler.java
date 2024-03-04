package handlers;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Threads.PreparePokemons;
import entity.NPC_Personaje1;
import main.GamePanel;
import objects.Type;
import screens.BattlePanel;
import screens.LoadingScreen;
import screens.screenStart;

public class GameHandler {

    private static int width = 700;
    private static int height = 500;
    private static JFrame mainWindow;
    private static GamePanel gamePanel;
    private static BattlePanel battlePanel;
    private static LoadingScreen loadingScreen;

    private static JDialog endGameDialog;

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

        Thread t = new PreparePokemons((ArrayList)gamePanel.npcList, gamePanel.player);
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
        gamePanel.startGameThread();
        gamePanel.setVisible(true);
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        // Para que el personaje se mueva al volver de un combate
        gamePanel.grabFocus();
        
        mainWindow.revalidate();

        // Comprobar el estado de la partida
        if (gamePanel.player.getDefeated()) {
            //Perdido
            System.out.println("Derrota");
            gamePanel.stopThread();
            
            showEndGameDialog("Has perdido :(.");
        } else if (gamePanel.checkAllNPCsDefeated()) {
            //Ganado
            gamePanel.stopThread();
            showEndGameDialog("Has ganado :).");
            
        }

    }

    private static void showEndGameDialog(String dialogText) {

        endGameDialog = new JDialog(mainWindow, "La partida ha terminado");
            endGameDialog.setSize(100,100);
            endGameDialog.setLocationRelativeTo(null);
            JPanel p = new JPanel();

            JLabel dialogTextLabel = new JLabel(dialogText);

            JButton dialogButton = new JButton("Continuar");
            dialogButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    mainWindow.dispose();
                    new screenStart().setVisible(true);
                }
                
            });

            endGameDialog.addWindowListener(new WindowListener() {

                @Override
                public void windowOpened(WindowEvent e) {
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    dialogButton.doClick();
                }

                @Override
                public void windowClosed(WindowEvent e) {
                }

                @Override
                public void windowIconified(WindowEvent e) {
                }

                @Override
                public void windowDeiconified(WindowEvent e) {
                }

                @Override
                public void windowActivated(WindowEvent e) {
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                }
                
            });



            p.add(dialogTextLabel);
            p.add(dialogButton);
            endGameDialog.add(p);
            endGameDialog.setModal(true);
            endGameDialog.setVisible(true);
    }


    public static void showBattlePanel(NPC_Personaje1 npc){
        
        battlePanel = new BattlePanel(width, height, gamePanel.player, npc);
        

        
        mainWindow.add(battlePanel, BorderLayout.CENTER);
        
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
