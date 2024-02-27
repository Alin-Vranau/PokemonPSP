package handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqliteHandler {

    static Connection connection;

    private String pokedexBD = "pokedex";

    public SqliteHandler() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            } catch (SQLException connectionError) {
                System.out.println("Se ha producido un error al conectar a la base de datos");
                System.exit(0);
            }
        }
    }


    private boolean pokemonExists(int pokemonID) {
        boolean exists = false;
        
        String query = "select count(*) from ? where id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pokedexBD);
            statement.setInt(2, pokemonID);

            ResultSet rs = statement.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                exists = true;
            }
        } catch (SQLException SQLError) {  }

        return exists;
    }

    private void insertPokemon(int pokemonID) {
        String query = "insert into ?(pokemon_id, visto, derrotado) values (?, 0, 0)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pokedexBD);
            statement.setInt(2, pokemonID);

            statement.executeUpdate();
        } catch (SQLException SQLError) {  }
    }

    public void setPokemonAsSeen(int pokemonID) {

        if (! pokemonExists(pokemonID)) {
            insertPokemon(pokemonID);
        }

        String query = "update ? set visto = 1 where pokemon_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pokedexBD);
            statement.setInt(2, pokemonID);

            statement.executeUpdate();

        } catch (SQLException SQLError) {  }
    }

    public void setPokemonAsDefeated(int pokemonID) {

        if (! pokemonExists(pokemonID)) {
            insertPokemon(pokemonID);
        }

        String query = "update ? set derrotado = 1 where pokemon_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pokedexBD);
            statement.setInt(2, pokemonID);

            statement.executeUpdate();

        } catch (SQLException SQLError) {  }
    }

    public ArrayList<Integer> getPokemonsSeen() {
        ArrayList<Integer> output = new ArrayList<>();

        String query = "select id from ? where visto = 1";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pokedexBD);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                output.add(rs.getInt(1));
            }
        } catch (SQLException SQLError) { }
        
        return output;
    }


    public ArrayList<Integer> getPokemonsDefeated() {
        ArrayList<Integer> output = new ArrayList<>();

        String query = "select id from ? where derrotado = 1";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pokedexBD);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                output.add(rs.getInt(1));
            }
        } catch (SQLException SQLError) { }
        
        return output;
    }
    
}
