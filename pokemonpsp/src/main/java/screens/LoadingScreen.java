package screens;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class LoadingScreen extends JPanel implements Runnable{

    static JFrame win;

    JLabel loadingGif;
    JLabel loadingText;
    JLabel tipsLabel;

    private int width;
    private String INICIO_ETIQUETA = "<html><p style=\"width:" + Math.round(width*0.75) +"px; text-align:center\">";
    private String FIN_ETIQUETA = "</p></html>";

    private String[] tips;
    private static boolean stopThread = false;

    public LoadingScreen(int width, int height) {

        this.width = width;
        INICIO_ETIQUETA = "<html><p style=\"width:" + Math.round(width*0.70) +"px; text-align:center\">";
        this.setSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
        this.setLayout(new GridBagLayout());

        Icon imgIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/pokeball_gif/poke.gif")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));


        GridBagConstraints gc1 = new GridBagConstraints();
        gc1.gridy=0;
        gc1.gridheight=4;
        gc1.insets = new Insets(20, 10, 5, 10);
        loadingGif = new JLabel(imgIcon);
        loadingGif.setMinimumSize(new Dimension((int)(width*0.5), (int)(height*0.3)));

        add(loadingGif, gc1);

        GridBagConstraints gc2 = new GridBagConstraints();
        gc2.gridy=4;
        gc2.insets = new Insets(0, 10, 10, 10);
        loadingText = new JLabel("Cargando...");
        loadingText.setHorizontalAlignment(SwingConstants.CENTER);
        loadingText.setFont(new Font(loadingText.getFont().getName(), Font.BOLD, 25));
        loadingText.setMinimumSize(new Dimension((int)(width*0.5), (int)(height*0.2)));
        add(loadingText, gc2);

        GridBagConstraints gc3 = new GridBagConstraints();
        gc3.gridy=5;
        gc3.insets = new Insets(40, 10, 10, 10);

        JPanel a = new JPanel();
        a.setMinimumSize(new Dimension(width, (int)(height*0.2)));
        
        tipsLabel = new JLabel(INICIO_ETIQUETA + "Si mantienes el ratón sobre un ataque puedes ver su tipo." + FIN_ETIQUETA);
        tipsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tipsLabel.setPreferredSize(new Dimension((int)(a.getMinimumSize().width*0.95), (int) (a.getMinimumSize().height*0.95)));
        tipsLabel.setMinimumSize(new Dimension(a.getMinimumSize()));
        tipsLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true), BorderFactory.createEmptyBorder(5,5,5,5)));
        tipsLabel.setFont(new Font(tipsLabel.getFont().getName(), Font.BOLD, 20));
        a.add(tipsLabel, BorderLayout.CENTER);
        
        add(a, gc3);

        setTips();

        Thread t = new Thread(this);

        t.start();


    }

    private void setTips() {

        String[] tips = {"Prueba a atacar a tu enemigo :P",
                        "Saber la tabla de tipos debes",
                        "Si mantienes el ratón sobre un ataque puedes ver su tipo.",
                        "Para empezar un combate acercate a un NPC y pulsa la E",
                        "Para abrir la pokedex pulsa la P",
                        "La pokedex contiene información sobre los pokemons, ademas de indicar cuales has visto y cuales has derrotado"};

        this.tips = tips;

    }

    public static void main(String[] args) {
        win = new JFrame();
        //win.setSize(700,500);
        LoadingScreen ls = new LoadingScreen(700, 500);
        win.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                ls.closeWindow();
                win.dispose();
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
        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        win.add(ls);
        win.pack();
        win.setLocationRelativeTo(null);

        win.setVisible(true);
    }

    public void closeWindow() {
        stopThread = true;
    }

    @Override
    public void run() {
        
        while (!stopThread) {
            System.out.println(stopThread);
            int tipNumber = (int) (Math.random()*tips.length);

            tipsLabel.setText(INICIO_ETIQUETA + tips[tipNumber] + FIN_ETIQUETA);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return;

    }
    
}
