package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel;
import objects.Pokemon;

public class Entity {
	GamePanel gp;
	public int worldX, worldY;
	public int speed;
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public String direction;
	public int spriteCounter = 0;
	public int spriteNum = 1; 
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public boolean collisionOn = false;
	public ArrayList<Pokemon> pokemonTeam = new ArrayList<>();

	public Entity (GamePanel gp){
		this.gp = gp;
	}

	public ArrayList<Pokemon> getPokemonTeam() {
		return pokemonTeam;
	}

	public void update() {
	}

	public void createPokemonTeam(List<String> pokemonNames) {

		for (String pokemonName : pokemonNames) {
			pokemonTeam.add(new Pokemon(pokemonName));
		}


	}

	public void createNPCPokemonTeam() {

		ArrayList<Integer> ids = new ArrayList<>();
		System.out.println("entidad");
		//151 pokemons
		while (ids.size() < 3) {
			int randomPokemonID =(int) (Math.random()*151 + 1);

			if (ids.contains(randomPokemonID)) {
				continue;
			}
			ids.add(randomPokemonID);
			pokemonTeam.add(new Pokemon(randomPokemonID));
		}
		


	}

}
