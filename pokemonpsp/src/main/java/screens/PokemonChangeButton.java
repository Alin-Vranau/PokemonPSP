package screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import objects.Pokemon;
import screens.DialogPanel.DialogType;


//* Clase uada para crear un botón personalizado para el panel de selección de pokemon */
public class PokemonChangeButton extends JPanel{


    public static void main(String[] args) {
        JFrame win = new JFrame();
        win.setResizable(false);
        win.setSize(500, 133); // 700, 500
        win.setTitle("Cambiar Pokemon");


        JPanel  panel= new PokemonChangeButton(500, 133, new Pokemon("Charmander"), null);

        win.add(panel);

        win.setVisible(true);

        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public PokemonChangeButton(int width, int height, Pokemon pokemon, BattlePanel battlePanel) {
        
        setLayout(new GridBagLayout());
        setBackground(new Color(21, 64, 97));
        setBorder(new LineBorder(new Color(13, 39, 59), 3));

        // Asignación de las restricciones de posición para la imagen del pokemon
        Insets pokemonImageInsets = new Insets(5, -50, 5, 5);
        GridBagConstraints pokemonImageConstraints = new GridBagConstraints();
        pokemonImageConstraints.fill = GridBagConstraints.BOTH;
        pokemonImageConstraints.weightx = 0.05;
        pokemonImageConstraints.weighty = 0.01;
        pokemonImageConstraints.gridy = 0;
        pokemonImageConstraints.gridx = 0;
        pokemonImageConstraints.gridheight = 2;
        pokemonImageConstraints.insets = pokemonImageInsets;
        
        // Obtención de la imagen
        ImageIcon image= null;
        try {
            image = new ImageIcon(new URL(pokemon.getPokemonSpriteURL()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        image = new ImageIcon(image.getImage().getScaledInstance((int) (width*0.25), (int) (height*0.8), Image.SCALE_DEFAULT));
        
        // Creación de la etiqueta que contendrá la imagen
        JLabel pokemonImage = new JLabel(image);
        
        add(pokemonImage, pokemonImageConstraints);

        // Asignación de las restricciones de posición para el nombre del pokemon
        Insets pokemonNameInsets = new Insets(2, 5, 5, 5);
        GridBagConstraints pokemonNameConstraints = new GridBagConstraints();
        pokemonNameConstraints.fill = GridBagConstraints.HORIZONTAL;
        pokemonNameConstraints.ipadx = 150;
        pokemonNameConstraints.weightx = 0.1;
        pokemonNameConstraints.weighty = 0.95;
        pokemonNameConstraints.gridy = 0;
        pokemonNameConstraints.gridx = 2;
        pokemonNameConstraints.gridheight = 1;
        pokemonNameConstraints.insets = pokemonNameInsets;

        JLabel pokemonName = new JLabel(pokemon.getName());
        pokemonName.setFont(new Font(pokemonName.getFont().getName(), Font.PLAIN, 17));
        pokemonName.setForeground(Color.WHITE);
        pokemonName.setVerticalAlignment(JLabel.TOP);
        pokemonName.setHorizontalAlignment(JLabel.LEFT);
        add(pokemonName, pokemonNameConstraints);

        // Asignación de las restricciones de posición para la barra de vida del pokemon
        Insets pokemonHealthBarInsets = new Insets(-30, 5, 10, 15);
        GridBagConstraints pokemonHealthBarConstraints = new GridBagConstraints();
        pokemonHealthBarConstraints.fill = GridBagConstraints.HORIZONTAL;
        pokemonHealthBarConstraints.weightx = 0.33;
        pokemonHealthBarConstraints.weighty = 0.95;
        pokemonHealthBarConstraints.gridy = 1;
        pokemonHealthBarConstraints.gridx = 2;
        pokemonHealthBarConstraints.gridheight = 1;
        pokemonHealthBarConstraints.insets = pokemonHealthBarInsets;

        // Cración de la barra de vida del pokemon
        JProgressBar pokemonHealthBar = new JProgressBar();
        pokemonHealthBar.setMaximum(pokemon.getHealth());
        pokemonHealthBar.setMinimum(0);
        pokemonHealthBar.setValue(pokemon.getActualHealth());
        pokemonHealthBar.setPreferredSize(new Dimension((int)(width*0.6), height));
        pokemonHealthBar.setForeground((pokemon.getActualHealth() < pokemon.getHealth()*0.2) ? Color.RED : Color.GREEN);

        add(pokemonHealthBar, pokemonHealthBarConstraints);

        // Asignación de las restricciones de posición para el indicador numérico de vida del pokemon
        Insets pokemonHealthIndicatorInsets = new Insets(-20, 5, 15, 15);
        GridBagConstraints pokemonHealthIndicatorConstraints = new GridBagConstraints();
        pokemonHealthIndicatorConstraints.fill = GridBagConstraints.HORIZONTAL;
        pokemonHealthIndicatorConstraints.anchor = GridBagConstraints.PAGE_END;
        pokemonHealthIndicatorConstraints.weightx = 0.33;
        pokemonHealthIndicatorConstraints.weighty = 0.95;
        pokemonHealthIndicatorConstraints.gridy = 1;
        pokemonHealthIndicatorConstraints.gridx = 2;
        pokemonHealthIndicatorConstraints.gridheight = 1;
        pokemonHealthIndicatorConstraints.insets = pokemonHealthIndicatorInsets;

        // Creación del indicador numérico del pokemon
        JLabel pokemonHealthIndicator = new JLabel();
        pokemonHealthIndicator.setFont(new Font(pokemonHealthIndicator.getFont().getName(), Font.PLAIN, 15));
        pokemonHealthIndicator.setVerticalAlignment(JLabel.TOP);
        pokemonHealthIndicator.setHorizontalAlignment(JLabel.RIGHT);
        pokemonHealthIndicator.setText(String.format("%d/%d", pokemon.getActualHealth(),pokemon.getHealth()));
        pokemonHealthIndicator.setForeground(Color.WHITE);

        add(pokemonHealthIndicator, pokemonHealthIndicatorConstraints);


        // Se añade un escuchador para detectar las distintas acciones del ratón sobre el componente
        addMouseListener(new MouseListener() {

            // Al pulsar sobre él se cierra el panel de elección y se cambia el pokemon del jugador, también se muestra un diálogo
			@Override
			public void mouseClicked(MouseEvent e) {
                if (pokemon.getActualHealth() > 0) {
                    battlePanel.resetBattlePanel();
                    battlePanel.playerPokemon = pokemon;
                    battlePanel.placePlayerPokemon(pokemon);
                    String text = String.format("Has sacado a %s", pokemon.getName());
                    battlePanel.showDialog(text, DialogType.PLAYER_POKEMON_CHANGED);
                }
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

            // Cambio del color de fondo del componente al entrar o salir de este con el ratón para dar a entender que se puede hacer click sobre él
			@Override
			public void mouseEntered(MouseEvent e) {
                if (pokemon.getActualHealth() > 0)
                    setBackground(new Color(36, 98, 145));
			}

			@Override
			public void mouseExited(MouseEvent e) {
                if (pokemon.getActualHealth() > 0) 
				    setBackground(new Color(21, 64, 97));
			}
            
        });

    }

}
