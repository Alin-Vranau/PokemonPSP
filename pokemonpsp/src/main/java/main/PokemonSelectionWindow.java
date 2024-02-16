package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PokemonSelectionWindow extends JDialog {
    private String selectedPokemon;

    public PokemonSelectionWindow(List<String> pokemonNames, List<String> pokemonFrontImages) {
        // Configuración básica de la ventana
        setTitle("Pokemon");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(800, 650);
        setModal(true);
        

        // Crear un panel principal con BoxLayout vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Añadir un margen

        JPanel panelgenero = new JPanel();
        panelgenero.setLayout(new BoxLayout(panelgenero, BoxLayout.X_AXIS));
        
        JLabel selectGender = new JLabel("Selecciona el género");
        selectGender.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(selectGender);
        
        mainPanel.add(panelgenero);
        JRadioButton masculino = new JRadioButton();
        masculino.setText("Masculino");
        panelgenero.add(masculino);
        masculino.setSelected(true);
        
        JRadioButton femenino = new JRadioButton();
        femenino.setText("Femenino");
        panelgenero.add(femenino);
        
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(femenino);
        grupo.add(masculino);
        
        
        // Agregar JLabel "Selecciona el Pokémon con el que quieres jugar"
        JLabel selectLabel = new JLabel("Selecciona el Pokémon con el que quieres jugar");
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(selectLabel);

        // Panel para los botones de los Pokémon y sus imágenes
        JPanel pokemonPanel = new JPanel();
        pokemonPanel.setLayout(new GridLayout(0, 2, 20, 20)); // Dos columnas con espaciado
        pokemonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregar botones e imágenes para cada Pokémon
        for (int i = 0; i < pokemonNames.size(); i++) {
            String pokemonName = pokemonNames.get(i);
            String imageUrl = pokemonFrontImages.get(i);

            // Crear un botón para el Pokémon
            JButton pokemonButton = new JButton(pokemonName);
            pokemonButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton selectedButton = (JButton) e.getSource();
                    selectedPokemon = selectedButton.getText();
                    JOptionPane.showMessageDialog(PokemonSelectionWindow.this,
                            "Has seleccionado a " + selectedPokemon,
                            "Pokémon seleccionado",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Cerrar la ventana después de seleccionar un Pokémon
                }
            });

            // Crear un panel para la imagen del Pokémon
            JPanel imagePanel = new JPanel();
            imagePanel.setLayout(new BorderLayout());

            // Descargar la imagen del Pokémon y mostrarla en un JLabel
            try {
                URL url = new URL(imageUrl);
                BufferedImage image = ImageIO.read(url);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                imagePanel.add(imageLabel, BorderLayout.CENTER);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Agregar el botón y la imagen al panel de Pokémon
            pokemonPanel.add(pokemonButton);
            pokemonPanel.add(imagePanel);
        }

        // Agregar el panel de Pokémon al panel principal
        mainPanel.add(Box.createVerticalStrut(20)); // Añadir espacio
        mainPanel.add(pokemonPanel);

        // Agregar el panel principal a la ventana
        add(mainPanel);

        // Ajustar el tamaño de la ventana automáticamente
        pack();

        // Centrar la ventana en la pantalla
        setLocationRelativeTo(null);

        // Hacer visible la ventana
        setVisible(true);
    }

    public String getSelectedPokemon() {
        return selectedPokemon;
    }

}
