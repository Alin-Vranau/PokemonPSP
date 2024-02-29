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

/**
 * Clase utilizada para crear un componente que muestre los datos de la vida del pokemon en combate
 */
public class PokemonStatus extends JPanel{

    JLabel name; // El nombre del pokemon
    JProgressBar healthBar; // Barra de vida del pokemon
    JLabel health; // Indicador numérico de la vida del pokemon
    Pokemon pokemon; // El pokemon del que se está dando la información
    boolean enemy; // Flag utilizada para indicar si este componente es para el pokemon del jugador o para el del enemigo

    
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

        // Cálculo de la altura de los componentes, ya que si el indicador es del jugador se indica de forma numérica la vida y si es del enemigo no
        int componentsHeight = (int) (height/((enemy) ? 2 : 3));

        // Restricciones de posición del nombre del pokemon
        Insets i = new Insets(0, 35, 0, 0);
        GridBagConstraints nameConstraints = new GridBagConstraints();
        nameConstraints.gridy= 0;
        nameConstraints.insets = i;

        // Se ajustan los parámetros de la etiqueta del nombre
        name.setText(pokemon.getName());
        name.setPreferredSize(new Dimension(width, componentsHeight));
        name.setMinimumSize(new Dimension(width, componentsHeight));
        name.setFont(new Font(name.getFont().getName(), Font.PLAIN, 20));
        //adjustFontSize(name, 0.3);
        
        // Restricciones de posición de la barra de vida del pokemon
        Insets i2 = new Insets(0, 0, 0, 35);
        GridBagConstraints healthBarConstraints = new GridBagConstraints();
        healthBarConstraints.gridy= 1;
        healthBarConstraints.insets = i2;
        healthBarConstraints.anchor = GridBagConstraints.LINE_END;

        // Parametrización de la barra de vida
        healthBar.setForeground(Color.GREEN);
        healthBar.setMinimum(0);
        healthBar.setMaximum(pokemon.getHealth()); // Cambiar por el maximo de vida del pokemon (hecho)
        healthBar.setValue(pokemon.getHealth());
        healthBar.setPreferredSize(new Dimension((int) (width*0.7), (int)(componentsHeight*((enemy) ? 0.25: 0.4))));
        healthBar.setMinimumSize(new Dimension((int) (width*0.7), (int)(componentsHeight*((enemy) ? 0.25: 0.4))));

        // Si el pokemon no es enemigo se añade el indicdor númerico de la vida
        if (!enemy) {
            //Restricciones de posición del indicador de vida del pokemon
            Insets i3 = new Insets(5, 0, 5, 10);
            GridBagConstraints healthConstraints = new GridBagConstraints();
            healthConstraints.gridy= 2;
            healthConstraints.insets = i3;
            healthConstraints.anchor = GridBagConstraints.LINE_END;

            // Parametrización del indicador de vida
            health.setText(String.format("%d/%d", pokemon.getActualHealth(),pokemon.getHealth())); // Modificar por los valores del pokemon
            health.setPreferredSize(new Dimension((int) (width*0.3), (int)(componentsHeight*0.7)));
            health.setMinimumSize(new Dimension((int) (width*0.3), (int)(componentsHeight*0.7)));
            adjustFontSize(health, 0.7);

            add(health, healthConstraints);
        }
        add(name, nameConstraints);
        add(healthBar, healthBarConstraints);
        
        

    }

    /**
     * Método que ajusta el tamaño del texto de las etiquetas del componente
     * 
     * @param label - La etiqueta a modificar
     * @param multiplier - El multiplicador para escalar el texto
     */
    private void adjustFontSize(JLabel label, double multiplier) {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = label.getPreferredSize().width;

        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio * multiplier);
        int componentHeight = label.getPreferredSize().height;

        int fontSizeToUse = Math.min(newFontSize, componentHeight);

        label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
    }

    /**
     * Método que cambia la vida indicada por la barra de vida y el indicador numérico de vida
     */
    public void updateHealth() {
        
        // Sin uso, no es visible en la batalla (Utilizado para hacer una animación para reducir el valor de la barra de vida)
        for (int i = healthBar.getValue(); i >= pokemon.getActualHealth(); i-- ) {
            healthBar.setValue(i);

            // Si la vida esta por debajo del 20% la barra de vida se cambia de color a rojo
            if (pokemon.getActualHealth() < pokemon.getHealth()*0.2) {
                healthBar.setForeground(Color.red);
            } else {
                healthBar.setForeground(Color.green);
            }
        }

        // Si no es el indicador del enemigo se actualiza el indicador numérico para indicar la vida actual
        if (!enemy) {
        health.setText(String.format("%d/%d", pokemon.getActualHealth(),pokemon.getHealth()));
        }
    }

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setSize(350, 200);
        window.setTitle("Batalla");

        //window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));


        PokemonStatus statusPanel = new PokemonStatus(window.getWidth(), window.getHeight(), new Pokemon("Raichu"), false);
        window.add(statusPanel);
        
        window.setVisible(true);
    }


}


