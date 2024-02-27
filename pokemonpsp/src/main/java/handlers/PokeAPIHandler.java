package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PokeAPIHandler {
    
    public static String callPokeAPI(String url) {

        StringBuilder responsePokemon = new StringBuilder();

        try {
            URL urlPokemon = new URL(url);
            HttpURLConnection conPokemon = (HttpURLConnection) urlPokemon.openConnection();
            conPokemon.setRequestMethod("GET");

            String inputLine;

            // Obtengo el resultado de la API con una codificacion UTF-8 para poder mostrar caracteres especiales
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conPokemon.getInputStream(), StandardCharsets.UTF_8));

            while ((inputLine = in.readLine()) != null) {
                responsePokemon.append(inputLine);
            }
            in.close();

        } catch (IOException e) {
            return null;
        }

        return responsePokemon.toString();

    }

}
