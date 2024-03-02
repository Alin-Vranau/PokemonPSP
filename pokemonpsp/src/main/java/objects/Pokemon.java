package objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;

import handlers.PokeAPIHandler;

public class Pokemon {
    private int id;
    private String name;
    private int health;
    private int actualHealth;
    private List<Integer> typesIDs;
    private HashSet<Move> moves;
    private String pokemonSpriteURL;

    // Todos los pokemons tienen nivel 50
    private int pokemonLevel = 50;

    public Pokemon(String name) {

        setAtrributes(-1, name);
    }

    public Pokemon(int id) {

        setAtrributes(id, null);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getActualHealth() {
        return actualHealth;
    }

    public void setActualHealth(int actualHealth) {
        if (actualHealth < 0) {
            this.actualHealth = 0;
        } else {
            this.actualHealth = actualHealth;
        }
    }

    public HashSet<Move> getMoves() {
        return moves;
    }

    public void setMoves(HashSet<Move> moves) {
        this.moves = moves;
    }

    public List<Integer> getTypesIDs() {
        return typesIDs;
    }

    public String getPokemonSpriteURL() {
        return pokemonSpriteURL;
    }

    /**
     * Método que establece los atributos del pokemon (nombre, URL sprite, movimientos, vida)
     * @param id - El id del pokemon será -1 si el constructor que se ha usado es el del nombre
     * @param name - El nombre del pokemon, será null si el constructor que se ha usado es el del id
     */
    private void setAtrributes(int id, String name) {

        // Obtencion de los datos de el pokemon elegido
        String responsePokemon = PokeAPIHandler.callPokeAPI("https://pokeapi.co/api/v2/pokemon/" + ((id == -1) ? name.toLowerCase() : id));

        // Se obtiene la id del pokemon
        this.id = new JSONObject(responsePokemon).getInt("id");

        // Se obtiene el nombre
        String apiName = new JSONObject(responsePokemon).getString("name");

        // Se cambia la primera letra del nombre a mayuscula
        this.name = apiName.substring(0, 1).toUpperCase() + apiName.substring(1);

        // La url del sprite frontal del pokemon
        pokemonSpriteURL = new JSONObject(responsePokemon). getJSONObject("sprites").getString("front_default");

        // Llamada a la función que devuelve un set de 4 movimientos para el pokemon
        moves = setRandomMoves(responsePokemon);

        // Llamada al método que hace el cálculo de la vida del pokemon
        setHealth(responsePokemon);

    }

    /**
     * Método que obtiene la vida base del pokemon y aplica una fórmula para calcular la vida total
     * 
     * @param responsePokemon - Una cadena con la respuesta de la API con los datos del pokemon
     */
    private void setHealth(String responsePokemon) {
        
        String baseHPText = JsonPath.read(responsePokemon, "$.stats[?(@.stat.name=='hp')].base_stat").toString().replaceAll("[\\[\\\"\\]\\\"]", "");
        
        int baseHP = Integer.parseInt(baseHPText);

        int totalHP = (int) Math.floor(0.01*(2*baseHP)*pokemonLevel) + pokemonLevel + 10;

        this.health = totalHP;

        this.actualHealth = totalHP;

        //System.out.println(this.health);
    }

    /**
     * Función que devuelve un set de 4 ataques para el pokemon, si el pokemon no tiene 4 movimientos el set será de menor tamaño
     * 
     * @param responsePokemon - Una cadena con la respuesta de la API con los datos del pokemon
     * @return - Set con los movimientos asignados al pokemon
     */
    private HashSet<Move> setRandomMoves(String responsePokemon) {
        HashSet<Move> outputMoves = new HashSet<>();


        // Obtencion de las url de los tipos para obtener el id de los tipos del pokemon
        List<String> typesURLList = JsonPath.read(responsePokemon.toString(), "$.types[*].type.url");

        this.typesIDs = new ArrayList<>();

        for (String url : typesURLList) {
            this.typesIDs.add(Integer.parseInt(url.substring(url.length()-3).replaceAll("/", "")));
        }

        // Creacion de una lista con todos sus ataques (contiene nombre y url a los datos del ataque)
        List<JSONObject> movesList = JsonPath.read(responsePokemon.toString(), "$.moves");
        
        // System.out.println(ataques.size());

        int iteracion = 0;

        // Bucle que se repite hasta que el pokemon tiene 4 ataques no repetidos o hasta que se llega a 45
        // iteraciones (como no se añaden ataques de poder 0 se evita un bucle infinito si el pokemon no tiene ataques suficientes)
        while (outputMoves.size() < 4 && iteracion != 45) {

            iteracion++;
            
            // Obtención de un ataque aleatorio de la lista
            int x = (int) (Math.random() * movesList.size());

            String moveURL = JsonPath.read(responsePokemon.toString(), "$.moves[" + x + "].move.url");

            //System.out.println(moveURL);

            // Obtencion de datos del movimiento
            String responseMove = PokeAPIHandler.callPokeAPI(moveURL);

            int id = JsonPath.read(responseMove.toString(), "$.id");

            // Comprobacion de que el ataque tenga poder, en caso de no tener el valor el null por lo que salta excepcion y pasa de iteracion
            int power = -1;
            try {
                power = JsonPath.read(responseMove.toString(), "$.power");
            } catch (NullPointerException poderCero) {
                continue;
            }

            // Obtencion de datos del ataque
            String name = JsonPath.read(responseMove.toString(), "$.names[?(@.language.name == 'es')].name")
                    .toString();
            // Se eliminan los [] y "" de la cadena porque era un array
            name = name.replaceAll("[\\[\\\"\\]\\\"]", "");
            //System.out.println(name);

            if (name == "") {
                continue;
            }

            int accuracy = 100;
            try {
                accuracy = JsonPath.read(responseMove, "$.accuracy");
            } catch (NullPointerException noPuedeFallar) {}
            //System.out.println(accuracy);
            
            // Como el tipo viene en ingles se obtiene la URL que lleva a sus datos
            String typeURL = JsonPath.read(responseMove.toString(), "$.type.url");
            String typeData = PokeAPIHandler.callPokeAPI(typeURL);

            // Obtención del nombre del ataque en español
            int typeID = JsonPath.read(typeData, "$.id");
            //System.out.println(type);

            // Se crea el objeto ataque y se añade al set de ataques
            Move move = new Move(id, name, typeID, accuracy, power);
            outputMoves.add(move);
            //System.out.println(moves.size());
            

        }

        
        for (Move m : outputMoves) {
            System.out.println(m.getId() + " - " + m.getName() + " - " + m.getAccuracy() + " - " + m.getPower());
        }
/* 
        for (int i : types) {
            System.out.println(i);
        }
        */
        return outputMoves;
    }


    /**
     * Función que simula el ataque de un pokemon sobre otro
     * 
     * @param move - El ataque a realizar
     * @param enemy - El pokemon sobre el que se realiza el ataque
     * @return - 0 si ataca y acierta, 1 si falla el ataque, 2 si el ataque no afecta al pokemon enemigo
     */
    public int attack(Move move, Pokemon enemy) {
        
        // Se calcula si acierta o falla
        if (Math.random()*100 + 1 > move.getAccuracy()) {
            return 1; // Ataque falla
        }

        // Obtemción del poder del ataque
        int movePower = move.getPower();

        // Si el tipo del pokemon coincide con el del ataque se aumenta el daño
        double stab = (typesIDs.contains(move.getTypeID())) ? 1.5 : 1;


        double effectiveness = 1;
        // Se calcula la efectividad del ataque
        for (int typeID : enemy.getTypesIDs()) {
            effectiveness *= (Type.typesList.get(move.getTypeID() - 1).getDouble_damage_to().contains(typeID)) ? 2 : Type.typesList.get(move.getTypeID() - 1).getHalf_damage_to().contains(typeID) ? 0.5 : 
                Type.typesList.get(move.getTypeID() - 1).getNo_damage_to().contains(typeID) ? 0 : 1;
        }

        if (effectiveness == 0) {
            return 2; // No afecta al enemigo
        }

        // Se calcula el daño total del ataque
        double damage = ((((2*pokemonLevel)/5 + 2) * movePower)/50 + 2) * stab * effectiveness;

        // Se modifica la vida del pokemon enemigo
        enemy.setActualHealth(enemy.getActualHealth() - (int) damage);

        return 0; // Ataca
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        Pokemon a = new Pokemon("snorlax");
        System.out.println(a.getName());
        new Pokemon("magikarp");
        Pokemon b = new Pokemon(1);
        System.out.println(b.name);
        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);   
        long elapsedTime = stopTime - startTime;
        long convert = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
        System.out.println(convert);
    }

}
