package main;

import javax.swing.*;

public class main {

    public static void main(String[] args) {
        // Crear y mostrar la ventana de selección de Pokémon
        PokemonSelection pokemonSelection = new PokemonSelection();
        pokemonSelection.selectPokemon();

        // Continuar con la lógica del juego si se selecciona un Pokémon
        String selectedPokemon = pokemonSelection.getSelectedPokemon();
        if (selectedPokemon != null) {
            // Continuar con la lógica del juego aquí
            System.out.println("Has seleccionado el Pokémon: " + selectedPokemon);
        }

        // Crear la ventana principal del juego
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Pokemon");

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
