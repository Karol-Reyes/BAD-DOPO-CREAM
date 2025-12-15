package domain;
import java.util.*;
/**
 * Configuración del juego, incluyendo modo, personajes, nivel, frutas, enemigos y objetos.
 */
public class GameConfig {

    private String mode;
    private String charA;
    private String charB;
    private int level;

    private Map<String, Integer> fruits;
    private Map<String, Integer> enemies;
    private Map<String, Integer> obstacles;

    /**
     * Crea una configuración vacía del juego.
     */
    public GameConfig() {
    }

    /**
     * Crea una configuración completa del juego.
     * @param mode modo de juego
     * @param charA personaje 1
     * @param charB personaje 2
     * @param level nivel del juego
     * @param fruits frutas seleccionadas con sus cantidades
     * @param enemies enemigos seleccionados con sus cantidades
     * @param obstacles obstáculos seleccionados con sus cantidades
     */
    public GameConfig(String mode, String charA, String charB,
                      int level, Map<String, Integer> fruits,
                      Map<String, Integer> enemies, Map<String, Integer> obstacles) {
        this.mode = mode;
        this.charA = charA;
        this.charB = charB;
        this.level = level;
        this.fruits = fruits != null ? fruits : new HashMap<>();
        this.enemies = enemies != null ? enemies : new HashMap<>();
        this.obstacles = obstacles != null ? obstacles : new HashMap<>();
    }

    /**
     * Obtiene el modo de juego.
     * @return modo de juego
     */
    public String getMode() {
        return mode;
    }

    /**
     * Obtiene el primer personaje.
     * @return personaje A
     */
    public String getCharacter1() {
        return charA;
    }

    /**
     * Obtiene el segundo personaje.
     * @return personaje B
     */
    public String getCharacter2() {
        return charB;
    }

    /**
     * Obtiene el nivel configurado.
     * @return nivel del juego
     */
    public int getLevel() {
        return level;
    }

    /**
     * Obtiene las frutas seleccionadas.
     * @return fruta seleccionada
     */
    public Map<String, Integer> getFruits() {
        return fruits;
    }

    /**
     * Obtiene los enemigos seleccionados.
     * @return enemigo seleccionado
     */
    public Map<String, Integer> getEnemies() { 
        return enemies; 
    }

    /**
     * Obtiene los obstáculos seleccionados.
     * @return objeto seleccionado
     */
    public Map<String, Integer> getObstacles() { 
        return obstacles; }


    /**
     * obtiene el total de las frutas a colocar
     */
    public int getTotalFruits() {
        return fruits.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Obtiene el total de enemigos a colocar
     */
    public int getTotalEnemies() {
        return enemies.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Obtiene el total de obstáculos a colocar
     */
    public int getTotalObstacles() {
        return obstacles.values().stream().mapToInt(Integer::intValue).sum();
    }
}