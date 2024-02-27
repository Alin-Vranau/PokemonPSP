package screens;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

import main.GamePanel;
import objects.Pokemon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BattlePanel extends JPanel {

    static JPanel interactivePanel; // Panel con la zona inferior de la pantalla
    static JPanel fightPanel; // Panel con el campo de batalla
    static JPanel dialogPanel; // Panel con dialogos
    static JPanel optionPanel; // Panel de opciones (Luchar, huir)
    static JPanel attacksPanel; // Panel con los ataques
    static JLabel playerPokemon; // Etiqueta que contendra la imagen de los pokemons del jugador
    static JLabel enemyPokemon; // Etiqueta que contendra la imagen de los pokemons enemigos
    static PokemonStatus enemyStatus; // Estado del pokemon enemigo
    static PokemonStatus playerStatus; // Estado del pokemon del jugador
    static BattlePanel gamePanel; // Panel que contiene todos los elementos la pantalla
    static JLabel imagenPokemonJugador; // Imagen del pokemon del jugador
    static JLabel imagenPokemonEnemigo; // Imagen del pokemon enemigo
    static int optionsSize = 0; // Tamaño que tiene la seleccion de accion
    static int healthStatusWidth; // Ancho del cuadro de vida
    static int healthStatusHeigth; // Alto del cuadro de vida
    GamePanel gp;

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setSize(700, 500);
        window.setTitle("Batalla");

        // Crear el panel del juego y pasarlo a la ventana
        //gamePanel = new BattlePanel(window.getWidth(), window.getHeight());
        window.add(gamePanel);
        
        
        //window.pack();
        //window.setLocationRelativeTo(null);  
        window.setVisible(true);
    }

    public BattlePanel(int width, int height, GamePanel gp) {
        // Ajustes del panel
        this.gp = gp;
        this.setSize(new Dimension(width, height));
        this.setDoubleBuffered(true);
		this.setFocusable(true);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gamePanel = this;

        // Se calcula el tamaño del panel de opciones
        optionsSize = Math.round(getHeight() * 0.30f);
        
        // Se crea el panel que tiene la zona de batalla
        fightPanel = new JPanel();
        fightPanel.setLayout(null);
        fightPanel.setPreferredSize(new Dimension(getWidth(), getHeight() - optionsSize));
        fightPanel.setBackground(new Color(0,0,0,0));
        

        // Calculo del ancho y alto de los paneles de vida
        healthStatusWidth = (int) (fightPanel.getPreferredSize().width * 0.35);
        healthStatusHeigth = (int) (fightPanel.getPreferredSize().height * 0.25);
        
        // Se crean los pokemons y se añaden a la pantalla (En un futuro esto debe ser recibido como parametro)
        Pokemon enemyPokemon = new Pokemon("Pikachu", 300, new ArrayList<>());
        
        placeEnemyPokemon(enemyPokemon);

        Pokemon playerPokemon = new Pokemon("Venusaur", 600, new ArrayList<>());            
        
        placePlayerPokemon(playerPokemon);
       
        // Creacion del panel que contendra los paneles que se utilicen en la parte inferior de la pantalla
        interactivePanel = new JPanel();
        interactivePanel.setPreferredSize(new Dimension(getWidth(), optionsSize));
        interactivePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true), BorderFactory.createLineBorder(Color.BLACK, 3, true)));
        interactivePanel.setBackground(Color.WHITE);
        interactivePanel.setLayout(new BorderLayout());

        // Creacion del panel de acciones (Atacar, huir, ¿capturar?, ¿Pokemons?)
        optionPanel = new JPanel();
        optionPanel.setPreferredSize(interactivePanel.getPreferredSize());
        //optionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true), BorderFactory.createLineBorder(Color.BLACK, 3, true)));
        optionPanel.setBackground(new Color(0,0,0,0));
        optionPanel.setLayout(new GridBagLayout());

        interactivePanel.add(optionPanel);


        add(fightPanel, BorderLayout.CENTER);
        add(interactivePanel, BorderLayout.SOUTH);
        
        // Constraints temporales para los botones del panel de acciones
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx= 0.3;
        cons.weighty = 0.5;
        
        // Creacion de los botones del panes el opciones
        JButton boton = new JButton("Atacar");
        //boton.setPreferredSize(new Dimension((int) (optionPanel.getPreferredSize().width/2), optionPanel.getPreferredSize().height));
        boton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                enemyPokemon.setActualHealth(enemyPokemon.getActualHealth() - 50);
                enemyStatus.updateHealth();
            }
            
        });
        optionPanel.add(boton,cons);

        // TODO: Encontrar como cerrar el panel desde el propio panel
        JButton boton2 = new JButton("Huir");
        //boton2.setPreferredSize(new Dimension((int) (optionPanel.getPreferredSize().width/2), optionPanel.getPreferredSize().height));
        boton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                BattlePanel.this.setVisible(false);
                gp.closeBattlePanel();
            }
            
        });

        optionPanel.add(boton2,cons);

        
    }



    /**
     * Metodo que coloca la imagen del pokemon enemigo y su vida en la zona de batalla
     * @param pokemon - El pokemon enemigo
     */
    private void placeEnemyPokemon(Pokemon pokemon) {
        
        // Si ya habia un pokemon se elimina la imagen de este y su vida
        if (enemyStatus != null) {
            fightPanel.remove(enemyStatus);
        }

        if (imagenPokemonEnemigo != null) {
            fightPanel.remove(imagenPokemonEnemigo);
        }

        // Se añade la vida del pokemon
        enemyStatus = new PokemonStatus(healthStatusWidth, healthStatusHeigth, pokemon, true);
        enemyStatus.setLocation((int) (fightPanel.getPreferredSize().width * 0.05), (int) (fightPanel.getPreferredSize().height * 0.05));
        fightPanel.add(enemyStatus);

        // Se obtiene la imagen del pokemon de la PokeApi
        ImageIcon poke = null;
		
        // Imagen auxiliar para reescalado
        ImageIcon imgPokemon = null; 
        try {

						URL urlPokemon = new URL("https://pokeapi.co/api/v2/pokemon/"+ pokemon.getName().toLowerCase());
						HttpURLConnection conPokemon = (HttpURLConnection) urlPokemon.openConnection();
						conPokemon.setRequestMethod("GET");

						String inputLine;

						BufferedReader in = new BufferedReader(new InputStreamReader(conPokemon.getInputStream()));
						StringBuilder responsePokemon = new StringBuilder();
						while ((inputLine = in.readLine()) != null) {
							responsePokemon.append(inputLine);
						}
						in.close();

						// Imagen
						JSONObject jsonResponse = new JSONObject(responsePokemon.toString());
						String frontDefaultPng = jsonResponse.getJSONObject("sprites").getString("front_default");
						URL frontDefaultPngUrl = new URL(frontDefaultPng);

						imgPokemon = new ImageIcon(frontDefaultPngUrl);

                    } catch (IOException e1) {
						e1.printStackTrace();
					}
        // Se reescala la imagen
        poke = new ImageIcon(imgPokemon.getImage().getScaledInstance((int)(fightPanel.getPreferredSize().width*0.4),
                    (int)(fightPanel.getPreferredSize().height*0.5), Image.SCALE_DEFAULT));

        // Creacion de la etiqueta con la imagen del pokemon ajustandose su tamaño y posicion
        imagenPokemonEnemigo = new JLabel(poke);
        imagenPokemonEnemigo.setSize(new Dimension((int)(fightPanel.getPreferredSize().width*0.35),
                                        (int)(fightPanel.getPreferredSize().height*0.4)));
        imagenPokemonEnemigo.setLocation(fightPanel.getPreferredSize().width - (int)(imagenPokemonEnemigo.getWidth()*1.3),
                                            (int)(imagenPokemonEnemigo.getWidth()*0.21));
        fightPanel.add(imagenPokemonEnemigo);
    }

    /**
     * Metodo que coloca la imagen del pokemon y su vida del jugador en la pantalla
     * @param pokemon - El pokemon a colocar
     */
    private void placePlayerPokemon(Pokemon pokemon) {

        if (playerStatus != null) {
            fightPanel.remove(playerStatus);
        }

        playerStatus = new PokemonStatus(healthStatusWidth, healthStatusHeigth, pokemon, false);
        playerStatus.setLocation((int) (fightPanel.getPreferredSize().width - fightPanel.getPreferredSize().width*0.40),
             (int) (fightPanel.getPreferredSize().height - fightPanel.getPreferredSize().height * 0.40));
        fightPanel.add(playerStatus);

        ImageIcon charmander = null;
		
        ImageIcon imgPokemon2 = null;
        try {

						URL urlPokemon = new URL("https://pokeapi.co/api/v2/pokemon/" + pokemon.getName().toLowerCase());
						HttpURLConnection conPokemon = (HttpURLConnection) urlPokemon.openConnection();
						conPokemon.setRequestMethod("GET");

						String inputLine;

						BufferedReader in = new BufferedReader(new InputStreamReader(conPokemon.getInputStream()));
						StringBuilder responsePokemon = new StringBuilder();
						while ((inputLine = in.readLine()) != null) {
							responsePokemon.append(inputLine);
						}
						in.close();

						// Imagen
						JSONObject jsonResponse = new JSONObject(responsePokemon.toString());
						String backDefaultPng = jsonResponse.getJSONObject("sprites").getString("back_default");
						URL backDefaultPngUrl = new URL(backDefaultPng);

						imgPokemon2 = new ImageIcon(backDefaultPngUrl);

                    } catch (IOException e1) {
						e1.printStackTrace();
					}

        charmander = new ImageIcon(imgPokemon2.getImage().getScaledInstance((int)(fightPanel.getPreferredSize().width*0.4),
            (int)(fightPanel.getPreferredSize().height*0.5), Image.SCALE_DEFAULT));

        JLabel picLabel2 = new JLabel(charmander);
        picLabel2.setSize(new Dimension((int)(fightPanel.getPreferredSize().width*0.40),
            (int)(fightPanel.getPreferredSize().height*0.50)));
        picLabel2.setLocation((int)(fightPanel.getPreferredSize().width*0.05) ,
            fightPanel.getPreferredSize().height - optionsSize - (int)(fightPanel.getPreferredSize().height*0.01));
        fightPanel.add(picLabel2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Se añade la imagen de fondo de la pelea
        super.paintComponent(g);

        BufferedImage imagen = null;
        try {
            imagen = ImageIO.read(getClass().getResourceAsStream("/battle_screen/battle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(imagen, 0, 0, gamePanel.getWidth(), getHeight() - optionsSize, null); // Se escala la imagen
    }
    
}
