package Threads;

import java.util.ArrayList;

import entity.NPC_Personaje1;
import entity.Player;
import handlers.GameHandler;

public class PreparePokemons extends Thread {

    public ArrayList<NPC_Personaje1> npcList;
    public Player player;

    public PreparePokemons(ArrayList<NPC_Personaje1> npcList, Player player) {
        this.npcList = npcList;
        this.player = player;
    }

    @Override
    public void run() {

        player.createPokemonTeam(player.pokemonNames);
        
        for (NPC_Personaje1 npc : npcList) {
            npc.createNPCPokemonTeam();
        }

        GameHandler.hideLoadingScreen();
        GameHandler.showGamePanel();

    }
    
}
