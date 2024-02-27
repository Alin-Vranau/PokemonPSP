package objects;

import java.util.ArrayList;
import java.util.List;

import com.jayway.jsonpath.JsonPath;

import handlers.PokeAPIHandler;

public class Type {
    
    private int id;
    private String name;
    // Listas con los IDs de los tipos sobre los que tiene distintos daños
    private List<Integer> double_damage_to;
    private List<Integer> double_damage_from;
    private List<Integer> half_damage_to;
    private List<Integer> half_damage_from;
    private List<Integer> no_damage_to;
    private List<Integer> no_damage_from;

    // Lista que contendra todos los tipos que se van creando, asi se pueden acceder a todos los tipos desde esta clase
    public static List<Type> typesList;


    public Type(String typeURL) {

        String responseType = PokeAPIHandler.callPokeAPI(typeURL);

        this.id = JsonPath.read(responseType, "$.id");

        String name = JsonPath.read(responseType.toString(), "$.names[?(@.language.name == 'es')].name")
                    .toString();
        // Se eliminan los [] y "" de la cadena porque era un array
        this.name = name.replaceAll("[\\[\\\"\\]\\\"]", "");

        // Se obtienen las listas con los datos sobre los daños que inflingen sobre los distintos tipos
        this.double_damage_from = getDamageRelations("double_damage_from", responseType);

        this.double_damage_to = getDamageRelations("double_damage_to", responseType);

        this.half_damage_from = getDamageRelations("half_damage_from", responseType);

        this.half_damage_to = getDamageRelations("half_damage_to", responseType);

        this.no_damage_from = getDamageRelations("no_damage_from", responseType);

        this.no_damage_to = getDamageRelations("no_damage_to", responseType);
        
        if (Type.typesList == null) {
            Type.typesList = new ArrayList<>();
        }

        Type.typesList.add(this);
        
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getDouble_damage_from() {
        return double_damage_from;
    }

    public List<Integer> getDouble_damage_to() {
        return double_damage_to;
    }

    public List<Integer> getHalf_damage_from() {
        return half_damage_from;
    }

    public List<Integer> getHalf_damage_to() {
        return half_damage_to;
    }

    public List<Integer> getNo_damage_from() {
        return no_damage_from;
    }

    public List<Integer> getNo_damage_to() {
        return no_damage_to;
    }

    /**
     * Función que devuelve una lista con los tipos sobre los que tiene mas efecto, efecto normal o menos efecto un movimiento
     * 
     * @param type - El tipo de relación que estamos buscando (doble daño, mitad de daño, no afecta)
     * @param responseType - Una cadena con los datos de la API del ataque
     * @return - La lista con las relaciones de daño hacia diferentes tipos de pokemon
     */
    private List<Integer> getDamageRelations(String type, String responseType) {
        List<String> damageRelations = JsonPath.read(responseType, "$.damage_relations." + type +"[*].url");
        List<Integer> output = new ArrayList<>();

        for (String url : damageRelations) {
            output.add(Integer.parseInt(url.substring(url.length()-3).replaceAll("/", "")));
        }
        return output;

    }

    /**
     * Método que inicializa los 18 tipos existenetes en pokemon y los almacena en una lista que es estatica (Type.typesList)
     */
    public static void initializeTypes() {

        String responseType = PokeAPIHandler.callPokeAPI("https://pokeapi.co/api/v2/type?limit=18");

        List<String> typesURLs = JsonPath.read(responseType, "$.results[*].url");

        for (String url : typesURLs) {
            new Type(url);
        }

        for (Type tipo : Type.typesList) {
            System.out.println(tipo.getName());
        }

    }


    public static void main(String[] args) {
        
        //new Type("https://pokeapi.co/api/v2/type/1");

        Type.initializeTypes();

        

    }

}
