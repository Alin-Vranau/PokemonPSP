package main;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PokemonSelection {
    private List<String> selectedPokemonNames = new ArrayList<>();
    private List<String> selectedPokemonImages = new ArrayList<>();

    public void selectPokemon() {
        try {
            // Realizar una solicitud GET a la PokeAPI para obtener información sobre los Pokémon
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/?limit=100");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Analizar la respuesta JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray results = jsonResponse.getJSONArray("results");

            // Crear una lista de nombres y URL de imágenes de frente de Pokémon
            List<String> pokemonNames = new ArrayList<>();
            List<String> pokemonFrontImages = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject pokemon = results.getJSONObject(i);
                String name = pokemon.getString("name");
                String imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + (i + 1) + ".png";
                pokemonNames.add(name);
                pokemonFrontImages.add(imageUrl);
            }

            // Seleccionar tres Pokémon aleatorios
            for (int i = 0; i < 3; i++) {
                int randomIndex = (int) (Math.random() * pokemonNames.size());
                selectedPokemonNames.add(pokemonNames.get(randomIndex));
                selectedPokemonImages.add(pokemonFrontImages.get(randomIndex));
                pokemonNames.remove(randomIndex);
                pokemonFrontImages.remove(randomIndex);
            }

            // Crear y mostrar la ventana de selección de Pokémon
            PokemonSelectionWindow selectionWindow = new PokemonSelectionWindow(selectedPokemonNames, selectedPokemonImages);
            selectedPokemonNames.clear(); // Limpiar la lista después de mostrar la ventana

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSelectedPokemon() {
        return selectedPokemonNames.isEmpty() ? null : selectedPokemonNames.get(0);
    }

    public static void main(String[] args) {
        PokemonSelection pokemonSelection = new PokemonSelection();
        pokemonSelection.selectPokemon();

        String selectedPokemon = pokemonSelection.getSelectedPokemon();
        if (selectedPokemon != null) {
            System.out.println("Has seleccionado el Pokémon: " + selectedPokemon);
        }
    }
}
