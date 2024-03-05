package handlers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.SwingConstants;

import Threads.PreparePokemons;
import entity.NPC_Personaje1;
import main.GamePanel;
import objects.Type;
import screens.BattlePanel;
import screens.LoadingScreen;
import screens.screenStart;

public class GameHandler {

    private static int width = 768;
    private static int height = 556;
    public static JFrame mainWindow;
    public static GamePanel gamePanel;
    private static BattlePanel battlePanel;
    private static LoadingScreen loadingScreen;

    private static JDialog endGameDialog;

    public static SqliteHandler sqliteHandler;

    public GameHandler() {

        mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setResizable(false);
        mainWindow.setTitle("Pokemon");
        mainWindow.setUndecorated(true);
        sqliteHandler = new SqliteHandler();

        startGame();

        hideGamePanel();

        showLoadingScreen();

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

        Thread t = new PreparePokemons((ArrayList<NPC_Personaje1>)gamePanel.npcList, gamePanel.player);
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
            
            showEndGameDialog("Has perdido 😢.");
        } else if (gamePanel.checkAllNPCsDefeated()) {
            //Ganado
            gamePanel.stopThread();
            showEndGameDialog("Has ganado 😁.");
            
        }

    }

    private static void showEndGameDialog(String dialogText) {

        endGameDialog = new JDialog(mainWindow, "La partida ha terminado");
            endGameDialog.setSize(400,200);
            endGameDialog.setLocationRelativeTo(null);

            JPanel p = new JPanel();
            p.setSize(400,200);
            p.setLayout(new GridBagLayout());

            GridBagConstraints constraintsLabel = new GridBagConstraints();
            constraintsLabel.gridy = 0;
            constraintsLabel.insets = new Insets(20, 40, 15, 40);
            JLabel dialogTextLabel = new JLabel(dialogText);
            dialogTextLabel.setFont(new Font(dialogTextLabel.getFont().getName(), Font.PLAIN, 20));
            dialogTextLabel.setVerticalAlignment(SwingConstants.CENTER);
            dialogTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dialogTextLabel.setPreferredSize(new Dimension(200, 50));
            dialogTextLabel.setMinimumSize(new Dimension(200, 50));

            GridBagConstraints constraintsButton = new GridBagConstraints();
            constraintsButton.gridy = 1;
            constraintsButton.insets = new Insets(15, 40, 20, 40);
            JButton dialogButton = new JButton("Continuar");
            dialogButton.setPreferredSize(new Dimension(200, 50));
            dialogButton.setMinimumSize(new Dimension(200, 50));
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



            p.add(dialogTextLabel, constraintsLabel);
            p.add(dialogButton, constraintsButton);
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
