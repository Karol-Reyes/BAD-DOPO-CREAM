package domain;

/**
 * Configuración del juego, incluyendo modo, personajes, nivel, frutas, enemigos y objetos.
 */
public class GameConfig {

    private String mode;
    private String charA;
    private String charB;
    private int level;
    private String fruitA;
    private String fruitB;
    private String enemyA;
    private String enemyB;
    private String item;

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
                      int level, String fruitA, String fruitB,
                      String enemyA, String enemyB, String item) {
        this.mode = mode;
        this.charA = charA;
        this.charB = charB;
        this.level = level;
        this.fruitA = fruitA;
        this.fruitB = fruitB;
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
     * Obtiene la primera fruta.
     * @return fruta A
     */
    public String getFruit1() {
        return fruitA;
    }

    /**
     * Obtiene la segunda fruta.
     * @return fruta B
     */
    public String getFruit2() {
        return fruitB;
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
}