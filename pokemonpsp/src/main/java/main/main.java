package main;

import javax.swing.*;

import objects.Type;

public class main {
    public static void main(String[] args) {
        // Crear la ventana principal del juego
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Pokemon");
        Type.initializeTypes();
        // Crear el panel del juego y pasarlo a la ventana
        GamePanel gamePanel = new GamePanel(window);
        window.add(gamePanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Iniciar el hilo de juego
        gamePanel.startGameThread();
    }
}
