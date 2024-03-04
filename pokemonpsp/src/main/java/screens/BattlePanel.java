package screens;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

import entity.NPC_Personaje1;
import entity.Player;
import handlers.GameHandler;
import objects.Move;
import objects.Pokemon;
import objects.Type;
import screens.DialogPanel.DialogType;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class BattlePanel extends JPanel {

    private BattlePanel battlePanel = this;

    JPanel interactivePanel; // Panel con la zona inferior de la pantalla
    JPanel fightPanel; // Panel con el campo de batalla
    JPanel dialogPanel; // Panel con dialogos
    JPanel optionPanel; // Panel de opciones (Luchar, huir)
    JPanel attacksPanel; // Panel con los ataques
    PokemonChangePanel pokemonChangePanel; // Panel para el cambio de pokemon
    PokemonStatus enemyStatus; // Estado del pokemon enemigo
    PokemonStatus playerStatus; // Estado del pokemon del jugador
    static BattlePanel gamePanel; // Panel que contiene todos los elementos la pantalla
    JLabel playersPokemonImage; // Imagen del pokemon del jugador
    JLabel enemysPokemonImage; // Imagen del pokemon enemigo
    int optionsSize = 0; // Tamaño que tiene la seleccion de accion
    int healthStatusWidth; // Ancho del cuadro de vida
    int healthStatusHeigth; // Alto del cuadro de vida

    NPC_Personaje1 enemyNPC;
    Player player;
    Pokemon playerPokemon; // El pokemon actual del jugador
    Pokemon enemyPokemon; // El pokemon actual del enemigo
    int enemyPokemonsDefeated = 0; // Contador utilizado para saber cuando termina la pelea


    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setSize(700, 500); // 700, 500
        window.setTitle("Batalla");

        Type.initializeTypes();

        // Crear el panel del juego y pasarlo a la ventana
        //gamePanel = new BattlePanel(window.getWidth(), window.getHeight(), new Pokemon("Drifloon"),
        //        new Pokemon("Tarountula") );
        
        
        window.add(gamePanel);
        
        window.setVisible(true);
    }

    public BattlePanel(int width, int height, Player player, NPC_Personaje1 enemyNPC) {
        // Ajustes del panel
        this.setSize(new Dimension(width, height));
        this.setDoubleBuffered(true);
		this.setFocusable(true);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        this.player = player;
        this.enemyNPC = enemyNPC;

        playerPokemon = player.getPokemonTeam().get(0);
        enemyPokemon = enemyNPC.getPokemonTeam().get(0);

        // Se calcula el tamaño del panel de opciones
        optionsSize = Math.round(height * 0.30f);
        
        // Se crea el panel que tiene la zona de batalla
        fightPanel = new JPanel();
        fightPanel.setLayout(null);
        fightPanel.setPreferredSize(new Dimension(width, height - optionsSize));
        fightPanel.setBackground(new Color(0,0,0,0));


        // Calculo del ancho y alto de los paneles de vida
        healthStatusWidth = (int) (fightPanel.getPreferredSize().width * 0.4);
        healthStatusHeigth = (int) (fightPanel.getPreferredSize().height * 0.3);
        
        // Se añaden los pokemons y sus barras de vida a la pantalla
        placeEnemyPokemon(enemyPokemon);
         
        placePlayerPokemon(playerPokemon);
       
        // Creacion del panel que contendra los paneles que se utilicen en la parte inferior de la pantalla
        interactivePanel = new JPanel();
        interactivePanel.setPreferredSize(new Dimension(width, optionsSize));
        interactivePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true), BorderFactory.createLineBorder(Color.BLACK, 3, true)));
        interactivePanel.setBackground(Color.WHITE);
        interactivePanel.setLayout(new BorderLayout());

        // Creacion del panel de acciones (Atacar, huir, ¿capturar?, ¿Pokemons?)
        optionPanel = new JPanel();
        optionPanel.setPreferredSize(interactivePanel.getPreferredSize());
        //optionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true), BorderFactory.createLineBorder(Color.BLACK, 3, true)));
        optionPanel.setBackground(new Color(0,0,0,0));
        optionPanel.setOpaque(false);
        optionPanel.setLayout(new GridBagLayout());

        interactivePanel.add(optionPanel);
        

        add(fightPanel, BorderLayout.CENTER);
        add(interactivePanel, BorderLayout.SOUTH);
        
        // Constraints temporales para el boton de ataque
        GridBagConstraints consAttackButton = new GridBagConstraints();
        consAttackButton.fill = GridBagConstraints.HORIZONTAL;
        consAttackButton.weightx= 1;
        consAttackButton.gridwidth = 1;
        consAttackButton.weighty = 1;
        consAttackButton.gridwidth = 2;
        consAttackButton.ipady=30;
        consAttackButton.ipadx = 80;
        consAttackButton.insets = new Insets(10, 10, 10, 10);
        
        // Creacion de los botones del panel de opciones
        JButton boton = new JButton("Atacar");
        boton.setMinimumSize(new Dimension((int)(optionPanel.getPreferredSize().width*0.8), (int)(optionPanel.getPreferredSize().height*0.8)));
        boton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                optionPanel.setVisible(false);
                createAttacksPanel();
            }
            
        });
        optionPanel.add(boton,consAttackButton);


        // Constraints temporales para el boton de ataque
        GridBagConstraints consChangeButton = new GridBagConstraints();
        consChangeButton.fill = GridBagConstraints.HORIZONTAL;
        consChangeButton.weightx= 0.5;
        consChangeButton.gridwidth = 1;
        consChangeButton.weighty = 1;
        consChangeButton.ipady=30;
        consChangeButton.ipadx = 80;
        consChangeButton.insets = new Insets(10, 10, 10, 10);
        JButton boton2 = new JButton("Cambiar Pokemon");
        boton2.setMinimumSize(new Dimension((int)(optionPanel.getPreferredSize().width*0.8), (int)(optionPanel.getPreferredSize().height*0.8)));
        boton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openPokemonChangePanel(false);
                
            }
            
        });

        optionPanel.add(boton2,consChangeButton);

        
    }


    /**
     * Método que abre el panel para cambiar de pokemon
     * 
     * @param defeated - Si se abre el panel por haber muerto el pokemon o si ha sido elección del jugador
     */
    private void openPokemonChangePanel(boolean defeated){
        pokemonChangePanel = new PokemonChangePanel(getSize().width, getSize().height, player, defeated, battlePanel);
        optionPanel.setVisible(false);
        fightPanel.setVisible(false);
        interactivePanel.setVisible(false);
        fightPanel.setVisible(false);
        interactivePanel.setVisible(false);
        add(pokemonChangePanel, BorderLayout.CENTER);
    }


    /**
     * Método que muestra el panel de combate como al inicio de una batalla, usado al volver de algun panel distinto (como el de selección de pokemon)
     */
    public void resetBattlePanel() {

        if (pokemonChangePanel != null) {
            pokemonChangePanel.setVisible(false);
        }
        fightPanel.setVisible(true);
        interactivePanel.setVisible(true);

        revalidate();
    }

    /**
     * Metodo que crea el panel con los ataques del pokemon del jugador y lo muestra en la pantalla
     */
    private void createAttacksPanel() {
        
        // Se comprueba que el panel no exista ya un panel de atauqes, de ser asi se elimina de la interfaz
        if (attacksPanel != null) {
            interactivePanel.remove(attacksPanel);
        }

        // Creación del panel de combate
        attacksPanel = new JPanel();
        attacksPanel.setPreferredSize(new Dimension(interactivePanel.getWidth(), interactivePanel.getHeight()));
        attacksPanel.setLayout(new GridBagLayout());

        // Restricciones que ajustan la posición de los botones de ataque superiores
        Insets i1 = new Insets(10, 15, 15, 15);
        GridBagConstraints topButtonsConstraints = new GridBagConstraints();
        topButtonsConstraints.fill = GridBagConstraints.BOTH;
        topButtonsConstraints.weightx = 0.4;
        topButtonsConstraints.weighty = 1;
        topButtonsConstraints.gridy = 0;
        topButtonsConstraints.gridwidth = 2;
        topButtonsConstraints.insets= i1;

        // Restricciones que ajustan la posición de los botones de ataque inferiores
        Insets i2 = new Insets(0, 15, 15, 15);
        GridBagConstraints bottomButtonsConstraints = new GridBagConstraints();
        bottomButtonsConstraints.fill = GridBagConstraints.BOTH;
        bottomButtonsConstraints.weightx = 0.4;
        bottomButtonsConstraints.weighty = 1;
        bottomButtonsConstraints.gridy = 1;
        bottomButtonsConstraints.gridwidth = 2;
        bottomButtonsConstraints.insets = i2;

        // Restricciones que ajustan la posición del botón para volver al panel de opciones
        Insets i3 = new Insets(0, 15, 5, 15);
        GridBagConstraints backButtonConstraints = new GridBagConstraints();
        backButtonConstraints.fill = GridBagConstraints.BOTH;
        backButtonConstraints.weighty = 0.3;
        backButtonConstraints.anchor = GridBagConstraints.PAGE_END; // Se ajusta la posicion en la esquina inferior derecha
        backButtonConstraints.insets = i3;
        backButtonConstraints.gridx = 2;
        backButtonConstraints.gridwidth = 2;
        backButtonConstraints.gridy = 2;
        backButtonConstraints.gridheight = 1;

        // Array que contendrá los botones de ataque
        JButton[] attackButtons = new JButton[4];
        int index = 0;
        HashSet<Move> moves = null;

        //Para evitar que haya pokemons sin ningun ataque (Puesto por Ditto que solo tiene un ataque que es transformarse en el pokemon enemigo)
        if (playerPokemon.getMoves().size() == 0) {
            moves = enemyPokemon.getMoves();
        } else {
            moves = playerPokemon.getMoves();
        }

        // Se crea un boton por cada movimiento del pokemon
        for (Move move : moves) {
            JButton attack = new JButton(move.getName());
            attack.setFont(new Font(attack.getFont().getName(), Font.PLAIN, 18));
            attack.setToolTipText("Tipo: " + Type.typesList.get(move.getTypeID()-1).getName());
            attack.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int attackOutput = playerPokemon.attack(move, enemyPokemon);
                    enemyStatus.updateHealth();
                    attacksPanel.setVisible(false);
                    String attackStatus = (attackOutput == 1) ? "Ha fallado" : (attackOutput == 2) ? "El ataque no afecta a " + enemyPokemon.getName() : "";
                    showDialog(String.format("Tu %s ha usado %s. %s", playerPokemon.getName(), move.getName(), attackStatus), DialogType.PLAYER_ATTACK);
                    
                    
                }
                
            });

            attackButtons[index] = attack;
            index++;
        }

        // Si el pokemon tiene menos de 4 ataques se añaden botones inactivos
        for (int i = 4; i > moves.size(); i--) {

            JButton attack = new JButton("-----");
            attack.setFont(new Font(attack.getFont().getName(), Font.PLAIN, 18));
            attack.setEnabled(false);
            attackButtons[i - 1] = attack;

        }

        // Creación del boton para volver al panel de opciones
        JButton back = new JButton("Volver");
        back.setFont(new Font(back.getFont().getName(), Font.PLAIN, 18));
        back.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                attacksPanel.setVisible(false);
                optionPanel.setVisible(true);
            }
            
        });


        attacksPanel.add(attackButtons[0], topButtonsConstraints);
        attacksPanel.add(attackButtons[1], topButtonsConstraints);
        attacksPanel.add(attackButtons[2], bottomButtonsConstraints);
        attacksPanel.add(attackButtons[3], bottomButtonsConstraints);
        attacksPanel.add(back, backButtonConstraints);

        interactivePanel.add(attacksPanel);

    }

    /**
     * Método que muestra un diálogo despues de cada accion del combate (Atacar, cambiar de pokemon, pokemon eliminado)
     * 
     * @param dialogText - El texto que contendra el dialogo
     * @param dialogType - El tipo de dialogo, sera una constante de el enum DialogType de la clase DialogPanel, indica la accion que invoca al dialogo
     */
    public void showDialog(String dialogText, DialogType dialogType) {
        
        if (dialogPanel != null) {
            interactivePanel.remove(dialogPanel);
        }

        dialogPanel = new screens.DialogPanel(interactivePanel.getWidth(), interactivePanel.getHeight(),dialogText, battlePanel, dialogType);
        interactivePanel.add(dialogPanel);
        dialogPanel.setVisible(true);

    }

    /**
     * Método que elige un ataque aleatorio del enemigo y ataca con el al jugador
     */
    public void enemyAttack() {

        // Variable que indica el ataque a realizar
        int randomAttack = (int) (Math.random()*enemyPokemon.getMoves().size());

        HashSet<Move> moves = null;

        if (enemyPokemon.getMoves().size() == 0) {
            moves = playerPokemon.getMoves();
        } else {
            moves = enemyPokemon.getMoves();
        }

        // Se recorre la lista de ataques
        for (Move enemyMove : moves) {
            // Como el conjunto de ataques es un set se va recorriendo hasta que la variable de randomAttack llega a 0
            if (randomAttack == 0) {
                int attackOutput = enemyPokemon.attack(enemyMove, playerPokemon);
                playerStatus.updateHealth();

                String attackStatus = (attackOutput == 1) ? "Ha fallado" : (attackOutput == 2) ? "El ataque no afecta a " + playerPokemon.getName() : "";
                showDialog(String.format("El %s enemigo ha usado %s. %s", enemyPokemon.getName(), enemyMove.getName(), attackStatus), DialogType.ENEMY_ATTACK);
                break;
            }
            // Se resta uno a la variable randomAttack para que cuando llegue a 0 se realice el ataque que toca
            randomAttack--;
            }

    }

    /**
     * Método que muestra el panel de opciones despues de un diálogo
     */
    public void showOptionsPanel() {
        if (dialogPanel != null){
            dialogPanel.setVisible(false);
        }
        optionPanel.setVisible(true);
    }

    /**
     * Metodo que se invoca desde la clase DialogPanel al acabar de mostrar el dialogo, se encaraga de realizar la siguiente accion del combate
     * 
     * @param dialogTpye - Constante que se utiliza para saber que tipo de dialog se mostro y que hacer a continuacion
     */
    public void postDialogAction(DialogType dialogTpye) {

        // Se comprueba el tipo de dialogo que se acaba de mostrar y se realiza la siguiente accion
        switch (dialogTpye) {
            // Si el dialogo es de un ataque enemigo se comprueba que el pokemon del jugador no haya sido derrotado, si es asi se muestran las opciones de combate,
            // si no se muestra un dialogo para indicar que el pokemon ha sido derrotado
            case ENEMY_ATTACK -> {if (playerPokemon.getActualHealth() == 0) {
                                    showDialog(String.format("Tu %s se ha debilitado", playerPokemon.getName()), DialogType.PLAYER_POKEMON_DEFEATED);
                                } else {
                                    dialogPanel.setVisible(false);
                                    showOptionsPanel();}}
            // Si el dialogo es de un ataque del jugador se comprueba que el pokemon del enemigo no haya sido derrotado, si es asi se llama a la funcion que hace que el enemigo ataque,
            // si no se muestra un dialogo para indicar que el pokemon ha sido derrotado
            case PLAYER_ATTACK -> {
                                    if (enemyPokemon.getActualHealth() == 0) {
                                        // Modificar la base de datos para añadir el pokemon como derrotado
                                        GameHandler.sqliteHandler.setPokemonAsDefeated(enemyPokemon.getId());
                                        
                                        enemyPokemonsDefeated++;
                                        showDialog(String.format("El %s enemigo se ha debilitado", enemyPokemon.getName()), DialogType.ENEMY_POKEMON_DEFEATED);
                                    } else{
                                        dialogPanel.setVisible(false);
                                        enemyAttack();}}
            // Si el dialogo es para indicar que el pokemon del jugador ha sido derrotado se llama a la funcion que muestra la pantalla para cambiar de pokemon
            case PLAYER_POKEMON_DEFEATED -> {   if (playersPokemonsDefeated()){ 
                                                    // TODO modificar para que salga la pantalla del titulo
                                                    player.defeated();
                                                    GameHandler.hideBattlePanel();
                                                    GameHandler.showGamePanel();
                                                } else
                                                    openPokemonChangePanel(true);}
            // Si el dialogo es para indicar que el pokemon del enemigo ha sido derrotado se llama a la funcion que cambia al siguiente pokemon del enemigo
            case ENEMY_POKEMON_DEFEATED -> {    if (enemyPokemonsDefeated == 3) {
                                                    enemyNPC.defeated(); // Se establece que el npc esta derrotado
                                                    player.pokemonBattleFinished();
                                                    GameHandler.hideBattlePanel();
                                                    GameHandler.showGamePanel();
                                                } else {
                                                    enemyPokemon = enemyNPC.getPokemonTeam().get(enemyPokemonsDefeated);
                                                    String text = String.format("El enemigo ha sacado a %s", enemyPokemon.getName());
                                                    battlePanel.showDialog(text, DialogType.PLAYER_POKEMON_CHANGED);
                                                    placeEnemyPokemon(enemyPokemon);
                                                }

            }
            // Si el dialogo se produce despues de un cambio de pokemon se muestra el panel de opciones
            case PLAYER_POKEMON_CHANGED, ENEMY_POKEMON_CHANGED -> {dialogPanel.setVisible(false);
                                                                    showOptionsPanel();}
            }


    }


    /**
     * Función que comprueba si todos los pokemons del juagdor han sido derrotados
     * 
     * @return - True si todos han sido derrotados o false si no.
     */
    private boolean playersPokemonsDefeated() {

        boolean defeated = false;

        int pokemonsDefeated = 0;
        for (Pokemon pokemon : player.getPokemonTeam()) {
            if (pokemon.getActualHealth() == 0)
                pokemonsDefeated++;
        }

        if (pokemonsDefeated == player.getPokemonTeam().size()) {
            defeated = true;
        }
        

        return defeated;

    }


    /**
     * Método que coloca la imagen del pokemon enemigo y su vida en la zona de batalla
     * 
     * @param pokemon - El pokemon enemigo
     */
    private void placeEnemyPokemon(Pokemon pokemon) {

        // Modificar la base de datos para añadir el pokemon como visto
        GameHandler.sqliteHandler.setPokemonAsSeen(pokemon.getId());
        
        // Si ya habia un pokemon se elimina la imagen de este y su vida
        if (enemyStatus != null) {
            fightPanel.remove(enemyStatus);
        }

        if (enemysPokemonImage != null) {
            fightPanel.remove(enemysPokemonImage);
        }

        // Se añade la vida del pokemon
        enemyStatus = new PokemonStatus(healthStatusWidth, healthStatusHeigth, pokemon, true);
        enemyStatus.setLocation((int) (fightPanel.getPreferredSize().width * 0.05), (int) (fightPanel.getPreferredSize().height * 0.1));
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
        poke = new ImageIcon(imgPokemon.getImage().getScaledInstance((int)(fightPanel.getPreferredSize().width*0.35),
                    (int)(fightPanel.getPreferredSize().height*0.55), Image.SCALE_DEFAULT));

        // Creacion de la etiqueta con la imagen del pokemon ajustandose su tamaño y posicion
        enemysPokemonImage = new JLabel(poke);
        enemysPokemonImage.setSize(new Dimension((int)(fightPanel.getPreferredSize().width*0.35),
                                        (int)(fightPanel.getPreferredSize().height*0.55)));
        enemysPokemonImage.setLocation(fightPanel.getPreferredSize().width - (int)(fightPanel.getPreferredSize().width*0.37),
                                            (int)(fightPanel.getPreferredSize().height*0.23));
        fightPanel.add(enemysPokemonImage);

        repaint();
    }

    /**
     * Método que coloca la imagen del pokemon del jugador y su vida en la pantalla
     * 
     * @param pokemon - El pokemon a colocar
     */
    public void placePlayerPokemon(Pokemon pokemon) {

        if (playerStatus != null) {
            fightPanel.remove(playerStatus);
        }

        if (playersPokemonImage != null) {
            fightPanel.remove(playersPokemonImage);
        }

        playerStatus = new PokemonStatus(healthStatusWidth, healthStatusHeigth, pokemon, false);
        playerStatus.setLocation((int) (fightPanel.getPreferredSize().width - fightPanel.getPreferredSize().width*0.40),
             (int) (fightPanel.getPreferredSize().height - fightPanel.getPreferredSize().height * 0.25));
        fightPanel.add(playerStatus);

        ImageIcon poke = null;
		
        ImageIcon imgPokemon = null;
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

						imgPokemon = new ImageIcon(backDefaultPngUrl);

                    } catch (IOException e1) {
						e1.printStackTrace();
					}

        poke = new ImageIcon(imgPokemon.getImage().getScaledInstance((int)(fightPanel.getPreferredSize().width*0.4),
            (int)(fightPanel.getPreferredSize().height*0.7), Image.SCALE_DEFAULT));

        playersPokemonImage = new JLabel(poke);
        playersPokemonImage.setSize(new Dimension((int)(fightPanel.getPreferredSize().width*0.6),
            (int)(fightPanel.getPreferredSize().height*0.8)));
        playersPokemonImage.setLocation((int)(-fightPanel.getPreferredSize().width*0.02) ,
            fightPanel.getPreferredSize().height - (int)(fightPanel.getPreferredSize().height*0.28));
        fightPanel.add(playersPokemonImage);
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
        g.drawImage(imagen, 0, 0, getWidth(), getHeight() - optionsSize, null); // Se escala la imagen
    }

    
}
