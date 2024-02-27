package screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import objects.Pokemon;

public class PokemonStatus extends JPanel{

    JLabel name;
    JProgressBar healthBar;
    JLabel health;
    Pokemon pokemon;
    boolean enemy;

    
    public PokemonStatus(int width, int height, Pokemon pokemon, boolean enemy) {
        setSize(width, height);
        setBackground(new Color(254,246,207,255));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true), BorderFactory.createLineBorder(Color.BLACK, 3, true)));
        setLayout(new GridBagLayout());
        
        this.pokemon = pokemon;
        this.enemy = enemy;
        name = new JLabel();
        healthBar = new JProgressBar();
        health = new JLabel();

        Insets i = new Insets(0, 35, 0, 0);
        
        int componentsHeight = (int) (height/((enemy) ? 2 : 3));

        GridBagConstraints nameConstraints = new GridBagConstraints();
        nameConstraints.gridy= 0;
        nameConstraints.insets = i;
        name.setText(pokemon.getName()); // Modificar por el nombre del pokemon cuando se añada el objeto a los parametros (hecho)
        
        name.setPreferredSize(new Dimension(width, componentsHeight));
        name.setMinimumSize(new Dimension(width, componentsHeight));
        adjustFontSize(name, 0.35);
        
        Insets i2 = new Insets(0, 0, 0, 35);
        GridBagConstraints healthBarConstraints = new GridBagConstraints();
        healthBarConstraints.gridy= 1;
        healthBarConstraints.insets = i2;
        healthBarConstraints.anchor = GridBagConstraints.LINE_END;
        healthBar.setForeground(Color.GREEN);
        healthBar.setMinimum(0);
        healthBar.setMaximum(pokemon.getHealth()); // Cambiar por el maximo de vida del pokemon (hecho)
        healthBar.setValue(pokemon.getHealth());
        healthBar.setPreferredSize(new Dimension((int) (width*0.7), (int)(componentsHeight*((enemy) ? 0.25: 0.4))));
        healthBar.setMinimumSize(new Dimension((int) (width*0.7), (int)(componentsHeight*((enemy) ? 0.25: 0.4))));
        if (!enemy) {
            Insets i3 = new Insets(5, 0, 5, 10);
            GridBagConstraints healthConstraints = new GridBagConstraints();
            healthConstraints.gridy= 2;
            healthConstraints.insets = i3;
            healthConstraints.anchor = GridBagConstraints.LINE_END;
            health.setText(String.format("%d/%d", pokemon.getActualHealth(),pokemon.getHealth())); // Modificar por los valores del pokemon
            health.setPreferredSize(new Dimension((int) (width*0.3), (int)(componentsHeight*0.7)));
            health.setMinimumSize(new Dimension((int) (width*0.3), (int)(componentsHeight*0.7)));
            
            adjustFontSize(health, 0.7);
            add(health, healthConstraints);
        }
        add(name, nameConstraints);
        add(healthBar, healthBarConstraints);
        
        

    }

    private void adjustFontSize(JLabel label, double multiplier) {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = label.getPreferredSize().width;

        // Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio * multiplier);
        int componentHeight = label.getPreferredSize().height;

        // Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);
        // Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
    }

    public void updateHealth() {
        
        for (int i = healthBar.getValue(); i >= pokemon.getActualHealth(); i-- ) {
            healthBar.setValue(i);
            if (pokemon.getActualHealth() < pokemon.getHealth()*0.2) {
                healthBar.setForeground(Color.red);
            } else {
                healthBar.setForeground(Color.green);
            }
        }
        if (!enemy) {
        health.setText(String.format("%d/%d", healthBar.getValue(),100));
        }
    }

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setSize(350, 200);
        window.setTitle("Batalla");

        //window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));


        // Crear el panel del juego y pasarlo a la ventana
        PokemonStatus gamePanel = new PokemonStatus(window.getWidth(), window.getHeight(), new Pokemon("Prueba", 100, null), false);
        window.add(gamePanel);
        

        //window.pack();
        //window.setLocationRelativeTo(null);
        window.setVisible(true);
    }


}


