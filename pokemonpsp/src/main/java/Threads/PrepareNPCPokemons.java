package Threads;

import java.util.ArrayList;

import entity.NPC_Personaje1;
import handlers.GameHandler;

public class PrepareNPCPokemons extends Thread {

    public ArrayList<NPC_Personaje1> npcList;

    public PrepareNPCPokemons(ArrayList<NPC_Personaje1> npcList) {
        this.npcList = npcList;
    }

    @Override
    public void run() {
        
        for (NPC_Personaje1 npc : npcList) {
            npc.createNPCPokemonTeam();
        }

        GameHandler.hideLoadingScreen();
        GameHandler.showGamePanel();

    }
    
}
