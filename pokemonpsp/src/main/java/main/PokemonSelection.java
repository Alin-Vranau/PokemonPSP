package main;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;

import entity.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PokemonSelection {
    private List<String> selectedPokemonNames = new ArrayList<>();
    private List<String> selectedPokemonImages = new ArrayList<>();

    public void selectPokemon() {
        try {
            // Usar un conjunto para almacenar los IDs únicos de Pokémon
            Set<Integer> pokemonIds = new HashSet<>();
            Random random = new Random();

            // Generar 3 IDs aleatorios únicos de Pokémon
            while (pokemonIds.size() < 3) {
                int id = random.nextInt(151) + 1; // Pokémon ID entre 1 y 151
                pokemonIds.add(id); // El conjunto asegura que todos los IDs sean únicos
            }

            for (int id : pokemonIds) {
                // Realizar una solicitud GET a la PokeAPI para obtener información sobre el Pokémon
                URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + id);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Analizar la respuesta JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                String name = jsonResponse.getString("name");
                String imageUrl = jsonResponse.getJSONObject("sprites").getString("front_default");
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                // Agregar el nombre y la imagen del Pokémon a las listas
                selectedPokemonNames.add(name);
                selectedPokemonImages.add(imageUrl);
            }

            // Crear y mostrar la ventana de selección de Pokémon
            new PokemonSelectionWindow(selectedPokemonNames, selectedPokemonImages);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<String> getSelectedPokemonNames() {
        return selectedPokemonNames;
    }
}

class PokemonSelectionWindow extends JDialog {
    Font labelFont = new Font("Arial", Font.BOLD, 20);
    Font Titulo = new Font("Arial", Font.BOLD, 25);
    public PokemonSelectionWindow(List<String> pokemonNames, List<String> pokemonFrontImages) {
        // Configuración básica de la ventana
        setTitle("Pokemon");
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setSize(800, 650);
        setUndecorated(true);
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        // Crear un panel principal con BoxLayout vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Añadir un margen

        // Panel para el género
        JPanel panelgenero = new JPanel();
        panelgenero.setLayout(new BoxLayout(panelgenero, BoxLayout.X_AXIS));
        
        JLabel selectGender = new JLabel("Selecciona el género");
        selectGender.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectGender.setFont(Titulo);
        mainPanel.add(selectGender);
        
        mainPanel.add(panelgenero);
        JRadioButton masculino = new JRadioButton();
        masculino.setText("Masculino");
        panelgenero.add(masculino);
        masculino.setFont(labelFont);
        masculino.setSelected(true);
        
        JRadioButton femenino = new JRadioButton();
        femenino.setText("Femenino");
        panelgenero.add(femenino);
        femenino.setFont(labelFont);
        
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(femenino);
        grupo.add(masculino);

        // Panel para las imágenes de los géneros
        JPanel panelImageGender = new JPanel();
        panelImageGender.setLayout(new BoxLayout(panelImageGender, BoxLayout.X_AXIS));
        mainPanel.add(panelImageGender);
        try {
            BufferedImage boyImage = ImageIO.read(getClass().getResource("/player/boy_down_2.png"));
            Image scaledBoyImage = boyImage.getScaledInstance(85, 85, Image.SCALE_SMOOTH); // Ajustar el tamaño a 100x100
            JLabel boyImageLabel = new JLabel(new ImageIcon(scaledBoyImage));
            panelImageGender.add(boyImageLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Añade un espacio fijo entre las imágenes
        Dimension gapSize = new Dimension(10, 0); // Puedes ajustar el 10 al tamaño deseado del espacio
        panelImageGender.add(Box.createRigidArea(gapSize));
        try {
            BufferedImage girlImage = ImageIO.read(getClass().getResource("/player/girl_down_1.png"));
            Image scaledGirlImage = girlImage.getScaledInstance(85, 85, Image.SCALE_SMOOTH); // Ajustar el tamaño a 100x100
            JLabel girlImageLabel = new JLabel(new ImageIcon(scaledGirlImage));
            panelImageGender.add(girlImageLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Agregar JLabel "Estos son tus pokemons"
        JLabel selectLabel = new JLabel("Estos son tus pokemons");
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        selectLabel.setFont(Titulo);
        mainPanel.add(selectLabel);

        // Panel para las etiquetas de los Pokémon y sus imágenes
        JPanel pokemonPanel = new JPanel();
        pokemonPanel.setLayout(new GridLayout(3, 2, 5, 5)); // Reducir el espaciado a 10 horizontal y 5 vertical
        pokemonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregar etiquetas e imágenes para cada Pokémon
        for (int i = 0; i < pokemonNames.size(); i++) {
            String pokemonName = pokemonNames.get(i);
            String imageUrl = pokemonFrontImages.get(i);

            // Crear una etiqueta para el Pokémon
            JLabel pokemonLabel = new JLabel(pokemonName);
            pokemonLabel.setHorizontalAlignment(JLabel.RIGHT); // Alinea el texto a la izquierda
            pokemonLabel.setFont(labelFont);

            // Crear un panel para la imagen del Pokémon
            JPanel imagePanel = new JPanel();
            imagePanel.setLayout(new BorderLayout());

            // Descargar la imagen del Pokémon y mostrarla en un JLabel
            try {
                URL url = new URL(imageUrl);
                BufferedImage image = ImageIO.read(url);
                int newWidth = 150; // Nuevo ancho
                int newHeight = 150; // Nueva altura
                Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imagePanel.add(imageLabel, BorderLayout.LINE_START);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Agregar la etiqueta y la imagen al panel de Pokémon
            pokemonPanel.add(pokemonLabel);
            pokemonPanel.add(imagePanel);
        }

        // Agregar el panel de Pokémon al panel principal
        mainPanel.add(Box.createVerticalStrut(5)); // Añadir espacio
        mainPanel.add(pokemonPanel);

        // Crear un panel para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Crear el botón "Aceptar"
        JButton acceptButton = new JButton("Aceptar");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.gender = femenino.isSelected() ? "girl" : "boy";
                PokemonSelectionWindow.this.dispose();
                System.out.println("Has aceptado. El juego continuará.");
            }
        });
        buttonPanel.add(acceptButton);

        // Crear un espacio entre los botones
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        // Crear el botón "Cancelar"
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí puedes poner el código para salir del juego
                System.exit(0);
            }
        });
        buttonPanel.add(cancelButton);

        // Agregar el panel de botones al panel principal
        mainPanel.add(buttonPanel);

        // Agregar el panel principal a la ventana
        add(mainPanel);

        // Ajustar el tamaño de la ventana automáticamente
        pack();

        // Centrar la ventana en la pantalla
        setLocationRelativeTo(null);

        // Hacer visible la ventana
        setVisible(true);
    }

}
