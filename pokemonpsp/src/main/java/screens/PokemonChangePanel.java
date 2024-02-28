package screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import objects.Pokemon;

/** Clase que genera la pantalla para poder cambiar de pokemon durante un combate */
public class PokemonChangePanel extends JPanel{
    
    public static void main(String[] args) {
        
        JFrame win = new JFrame();
        win.setResizable(false);
        win.setSize(700, 500); // 700, 500
        win.setTitle("Cambiar Pokemon");


        JPanel  panel= new PokemonChangePanel(700, 500, true, null);

        win.add(panel);

        win.setVisible(true);

        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public PokemonChangePanel(int width, int height, boolean defeated, BattlePanel battlePanel) {
        
        setLayout(new BorderLayout());

        // Creacion del panel que contiene los 3 botones de seleccion de pokemon
        JPanel pokemonPanel = new JPanel();
        pokemonPanel.setLayout(new GridBagLayout());
        pokemonPanel.setPreferredSize(new Dimension(width, (int)(height*0.85)));

        // Restricciones que ajustan la posición de los botones de seleccion de pokemon
        Insets i1 = new Insets(15, 40, 15, 40);
        GridBagConstraints buttonPokemon1Constraints = new GridBagConstraints();
        buttonPokemon1Constraints.fill = GridBagConstraints.BOTH; // Se extiende el boton el ambas direcciones para que ocupe todo el espacio disponible
        buttonPokemon1Constraints.weightx = 1; // Necesario para que se ajuste el alto
        buttonPokemon1Constraints.weighty = 1; // Necesario para que se ajuste el ancho
        buttonPokemon1Constraints.gridy = 0; // Posicion de la fila que ocupa
        buttonPokemon1Constraints.gridwidth = 1; // Cantidad de columnas que ocupa
        buttonPokemon1Constraints.insets= i1; // Ajuste de los espacios con el resto de elementos

        GridBagConstraints buttonPokemon2Constraints = new GridBagConstraints();
        buttonPokemon2Constraints.fill = GridBagConstraints.BOTH;
        buttonPokemon2Constraints.weightx = 1;
        buttonPokemon2Constraints.weighty = 1;
        buttonPokemon2Constraints.gridy = 1;
        buttonPokemon2Constraints.gridwidth = 1;
        buttonPokemon2Constraints.insets= i1;

        GridBagConstraints buttonPokemon3Constraints = new GridBagConstraints();
        buttonPokemon3Constraints.fill = GridBagConstraints.BOTH;
        buttonPokemon3Constraints.weightx = 1;
        buttonPokemon3Constraints.weighty = 1;
        buttonPokemon3Constraints.gridy = 2;
        buttonPokemon3Constraints.gridwidth = 1;
        buttonPokemon3Constraints.insets= i1;

        // TODO Modificar esto por los pokemons de la lista del jugador
        // Se crean los botones de seleccion de pokemon (Es un componente personalizado comprobar clase para mas detalles)
        Pokemon a = new Pokemon("Raichu");
        a.setActualHealth(0);
        PokemonChangeButton buttonPokemon1 = new PokemonChangeButton(width,(int) (height*0.8/3), a, battlePanel);
        PokemonChangeButton buttonPokemon2 = new PokemonChangeButton(width,(int) (height*0.8/3), new Pokemon("Charmander"), battlePanel);
        PokemonChangeButton buttonPokemon3 = new PokemonChangeButton(width,(int) (height*0.8/3), new Pokemon("Charizard"), battlePanel);
        
        pokemonPanel.add(buttonPokemon1, buttonPokemon1Constraints);
        pokemonPanel.add(buttonPokemon2, buttonPokemon2Constraints);
        pokemonPanel.add(buttonPokemon3, buttonPokemon3Constraints);

        // Se aplica un fondo transparente para que se vea la imagen de fondo del panel principal
        pokemonPanel.setOpaque(false);

        add(pokemonPanel, BorderLayout.CENTER);

        // Creación del panel que contiene una etiqueta indicando que elijas un pokemon, y un botón para volver a la pelea
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setPreferredSize(new Dimension(width, (int)(height*0.15)));
        bottomPanel.setBackground(new Color(255,0,0, 0));
        bottomPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Restricciones que ajustan la posición de la etiqueta en el panel inferior
        Insets insetsLabel = new Insets(0, 5, 5, 5);
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.fill = GridBagConstraints.BOTH;
        labelConstraints.weightx = 0.3;
        labelConstraints.weighty = 1;
        labelConstraints.gridy = 0;
        labelConstraints.gridwidth = 1;
        labelConstraints.gridheight = 1;
        labelConstraints.insets= insetsLabel;

        JLabel text = new JLabel("Elige un pokemon.");
        text.setFont(new Font(text.getFont().getName(), Font.PLAIN, 20));
        text.setPreferredSize(new Dimension((int)(width*0.7), (int)(height*0.15)));
        text.setMinimumSize(new Dimension((int)(width*0.7), (int)(height*0.15)));
        text.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 3, false), BorderFactory.createEmptyBorder(10,10,10,10)));
        text.setBackground(Color.WHITE);
        text.setOpaque(true);
        bottomPanel.add(text, labelConstraints);


        // Restricciones que ajustan la posición del botón de volver en el panel inferior
        Insets insetsButton = new Insets(0, 0, 0, 5);
        GridBagConstraints backButtoConstraints = new GridBagConstraints();
        backButtoConstraints.fill = GridBagConstraints.HORIZONTAL;
        backButtoConstraints.weightx = 0.7;
        backButtoConstraints.weighty = 1;
        backButtoConstraints.gridy = 0;
        backButtoConstraints.gridwidth = 1;
        backButtoConstraints.gridheight = 1;
        backButtoConstraints.ipady = 20;
        backButtoConstraints.insets= insetsButton;

        JButton backButton = new JButton("Volver");
        // Evita que salga un recuadro alrededor del texto
        backButton.setFocusPainted(false);
        backButton.setForeground(new Color(255,255,255));
        backButton.setBackground(new Color(21, 64, 97));
        backButton.setEnabled(false);

        // Solo se habilita el botón si está pantalla no ha salido por la derrota del pokemon del jugador
        if (! defeated) {
            backButton.addMouseListener(new MouseListener() {

                // Al pulsarlo se vulve al panel de combate
                @Override
                public void mouseClicked(MouseEvent e) {
                    battlePanel.resetBattlePanel();
                    battlePanel.showOptionsPanel();
                }

                @Override
                public void mousePressed(MouseEvent e) { }

                @Override
                public void mouseReleased(MouseEvent e) { }

                // Modficación del color de fondo del botón al entrar o salir de él con el ratón
                @Override
                public void mouseEntered(MouseEvent e) {
                    backButton.setBackground(new Color(36, 98, 145));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    backButton.setBackground(new Color(21, 64, 97));
                }
                
            });
            backButton.setEnabled(true);
        }

        bottomPanel.add(backButton, backButtoConstraints);


        add(bottomPanel, BorderLayout.SOUTH);
    }
 
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage imagen = null;
        try {
            imagen = ImageIO.read(getClass().getResourceAsStream("/pokemon_selector/pokemon_selector.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), null); // Se escala la imagen
    }
    }

