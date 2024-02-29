package handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqliteHandler {

    static Connection connection;

    private String pokedexBD = "pokedex";

    public static void main(String[] args) {
        new SqliteHandler();
    }

    public SqliteHandler() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:database.db");
                createPokedexTable();
            } catch (SQLException connectionError) {
                System.out.println("Se ha producido un error al conectar a la base de datos");
                System.exit(0);
            }
        }
    }

    public void resetPokedex() {
        String query = "delete from " + pokedexBD;

        try {
            Statement statement = connection.createStatement();

            statement.execute(query);
            statement.close();
        } catch (SQLException errorSQL) {
            System.out.println("Error al borrar los datos de la pokedex. " + errorSQL.getMessage());
        }
    }


    private void createPokedexTable() {

        String query = "create table " + pokedexBD + " ("+
        "pokemon_id integer primary key," +
        "visto boolean,"+
        "derrotado boolean)";

        try {
            Statement statement = connection.createStatement();

            statement.execute(query);

            statement.close();
        } catch (SQLException errorTablaExistente) {
            System.out.println("La tabla " + pokedexBD + " ya existe, no se creará.");
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

            rs.close();
            statement.close();
        } catch (SQLException SQLError) {
            System.out.println("Error al comprobar si existe el pokemon " + pokemonID + ". " + SQLError.getMessage());
          }
            
        return exists;
    }

    private void insertPokemon(int pokemonID) {
        String query = "insert into ?(pokemon_id, visto, derrotado) values (?, 0, 0)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pokedexBD);
            statement.setInt(2, pokemonID);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException SQLError) {
            System.out.println("Error al insertar el pokemon " + pokemonID + ". " + SQLError.getMessage());
          }
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
            statement.close();

        } catch (SQLException SQLError) {
            System.out.println("Error al actualizar el estado del pokemon " + pokemonID + " a visto. " + SQLError.getMessage());
          }
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
            statement.close();

        } catch (SQLException SQLError) { 
            System.out.println("Error al actualizar el estado del pokemon " + pokemonID + " a derrotado. " + SQLError.getMessage());
         }
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
            rs.close();
            statement.close();
        } catch (SQLException SQLError) {
            System.out.println("Error al obtener la lista de pokemons vistos. " + SQLError);
         }
            
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
            rs.close();
            statement.close();
        } catch (SQLException SQLError) {
            System.out.println("Error al obtener la lista de pokemons derrotados. " + SQLError);
         }
        
        return output;
    }
    
}
