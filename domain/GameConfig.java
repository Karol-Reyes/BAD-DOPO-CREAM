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
    private String enemyA;
    private String enemyB;
    private String item;

    private Map<String, Integer> fruits;

    /**
     * Crea una configuración vacía del juego.
     */
    public GameConfig() {
    }

    /**
     * Crea una configuración completa del juego.
     * @param mode modo de juego
     * @param charA primer personaje
     * @param charB segundo personaje
     * @param level nivel seleccionado
     * @param fruitA primera fruta
     * @param fruitB segunda fruta
     * @param enemyA primer enemigo
     * @param enemyB segundo enemigo
     * @param item objeto especial
     */
    public GameConfig(String mode, String charA, String charB,
                      int level, Map<String, Integer> fruits,
                      String enemyA, String enemyB, String item) {
        this.mode = mode;
        this.charA = charA;
        this.charB = charB;
        this.level = level;
        this.fruits = fruits != null ? fruits : new HashMap<>();
        this.enemyA = enemyA;
        this.enemyB = enemyB;
        this.item = item;
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
     * Obtiene el primer enemigo.
     * @return enemigo A
     */
    public String getEnemy1() {
        return enemyA;
    }

    /**
     * Obtiene el segundo enemigo.
     * @return enemigo B
     */
    public String getEnemy2() {
        return enemyB;
    }

    /**
     * Obtiene el objeto especial.
     * @return objeto del juego
     */
    public String getObject() {
        return item;
    }

    /**
     * obtiene el total de las frutas a colocar
     */
    public int getTotalFruits() {
        return fruits.values().stream().mapToInt(Integer::intValue).sum();
    }
}