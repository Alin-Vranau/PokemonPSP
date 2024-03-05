package screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Clase que crea un panel de diálogo usado para indicar que es lo que ocurre durante las batallas
 */
public class DialogPanel extends JPanel implements Runnable{

    private String text; // El texto a mostrar
    private JLabel dialog; // La etiqueta usada para mostrar el texto
    private BattlePanel fightPanel; // El panel de lucha, usado para hacer llamadas a sus métodos
    private DialogType dialogType; // El tipo de dialogo que se esta mostrando, utilizado para saber que hacer una vez se ha terminado el diálogo

    private String INICIO_ETIQUETA = "<html><p style=\"width:100px\">";
    private String FIN_ETIQUETA = "</p></html>";

    enum DialogType {
        PLAYER_ATTACK, // El jugador ha realizado un ataque
        ENEMY_ATTACK, // El enemigo ha realizado un ataque
        PLAYER_POKEMON_DEFEATED, // El pokemon del jugador ha sido derrotado
        ENEMY_POKEMON_DEFEATED, // El pokemon del enemigo ha sido derrotado
        PLAYER_POKEMON_CHANGED, // El jugador ha cambiado de pokemon
        ENEMY_POKEMON_CHANGED // El enemigo ha cambiado de pokemon
    }

    public DialogPanel(int width, int height,String text, BattlePanel fightPanel, DialogType dialogType) {
        this.fightPanel = fightPanel;
        this.text = text;
        this.dialogType = dialogType;
        setLayout(new BorderLayout());

        // Se establece un tamaño de fuente en función del alto de la pantalla
        int fontSize = (height <= 150) ? 25 : (int) Math.round(20.0 *(height/1000.0 + 1.3));
       
        // Codigo HTML utilizado en la etiqueta para poder dividir el texto en distintos parrafos cuando un diálogo es muy largo
        INICIO_ETIQUETA = "<html><p style=\"width:" + Math.round(width*0.74) +"px\">";

        // Creación de la etiqueta que contendra el texto del diálogo
        dialog = new JLabel(INICIO_ETIQUETA + FIN_ETIQUETA);
        dialog.setBorder(new EmptyBorder(10, 10, 10, 10));
        dialog.setPreferredSize(new Dimension(width, height));
        dialog.setVerticalAlignment(JLabel.TOP);
        dialog.setFont(new Font("dialog", Font.PLAIN, fontSize));
        add(dialog, BorderLayout.CENTER);

        // Hilo que irá mostrando el texto poco a poco
        Thread t = new Thread(this);

        // Se asigna un escuchador para el ratón
        this.addMouseListener(new MouseListener() {

            // Al hacer click, si el hilo sigue activo, lo para y muestra todo el texto en la etiqueta
            // si no está activo (ya se ha mostrado todo el texto) se finaliza el diálogo
            @Override
            public void mouseClicked(MouseEvent e) {
                if (t.isAlive()) {
                    t.interrupt();
                    dialog.setText(INICIO_ETIQUETA + text + FIN_ETIQUETA);
                } else {
                    endDialog();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
            
        });

        t.start();

    }

    /**
     * Método que se ejecuta al finalizar el dialogo y ejecuta las acciones siguientes en el panel de lucha
     */
    private void endDialog() {
        fightPanel.dialogPanel.setVisible(false);
        fightPanel.postDialogAction(dialogType);
    }


    @Override
    public void run() {
        
        // Se van añadiendo letras poco a poco a la etiqueta
        for (int index = 0; index < text.length(); index++) {
            
            dialog.setText(dialog.getText().substring(0, dialog.getText().length() - FIN_ETIQUETA.length()) + text.charAt(index) + FIN_ETIQUETA);
            try {
                Thread.sleep(25);
            } catch (InterruptedException threadInterrupted) {
                break;
            }
        }

        // Una vez esta todo el texto en la etiqueta se deja el dialogo en pantalla 2 segundos mas
        try {
            Thread.sleep(2000);
        } catch (InterruptedException threadInterrupted) { 
            // Excepcion producida por el escuchador al hacer click (Se termina el dialogo)
            endDialog();
            return;
        }
        endDialog();
    }



    
}
