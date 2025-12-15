package presentation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import domain.GameConfig;

/**
 * Clase que controla la configuración del juego.
 * Permite establecer y obtener las selecciones realizadas por el usuario
 * en cuanto a modo de juego, personajes, nivel, frutas, enemigos y objetos.
 */
public class GameControl {
    private String gameMode;
    private String character1;
    private String character2;
    private int selectedLevel;

    /**
     * Constructor de la clase GameControl.
     * Inicializa los atributos con valores predeterminados.
     */
    public String getGameMode() {
        return gameMode;
    } 

    /**
     * Establece el modo de juego seleccionado.
     */
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Obtiene el personaje 1 seleccionado.
     */
    public String getCharacter1() {
        return character1;
    }

    /**
     * Establece el personaje 1 seleccionado.
     */
    public void setCharacter1(String character1) {
        this.character1 = character1;
    }

    /**
     * Obtiene el personaje 2 seleccionado.
     */
    public String getCharacter2() {
        return character2;
    }

    /**
     * Establece el personaje 2 seleccionado.
     */
    public void setCharacter2(String character2) {
        this.character2 = character2;
    }

    /**
     * Obtiene el nivel seleccionado.
     */
    public int getSelectedLevel() {
        return selectedLevel;
    }

    /**
     * Establece el nivel seleccionado.
     */
    public void setSelectedLevel(int selectedLevel) {
        this.selectedLevel = selectedLevel;
    }

//frutas elegidas
     /**
     * Clase interna que representa una selección de fruta con su nombre y cantidad.
     */
    public static class FruitSelection {
        public final String name;
        public final int amount;

        public FruitSelection(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }
    }

    /**
     * Lista de frutas seleccionadas por el usuario.
     */
    private final List<FruitSelection> selectedFruits = new ArrayList<>();

    /**
     * Agrega una fruta seleccionada con su cantidad.
     * @param name nombre de la fruta
     * @param amount cantidad de la fruta
     */
    public void addFruit(String name, int amount) {
        selectedFruits.add(new FruitSelection(name, amount));
    }

    /**
     * Verifica si se han seleccionado frutas.
     */
    public boolean hasFruits() {
        return !selectedFruits.isEmpty();
    }

    /**
     * Verifica si una fruta específica ha sido seleccionada.
     */
    public boolean isFruitSelected(String name) {
        return selectedFruits.stream()
                .anyMatch(f -> f.name.equals(name));
    }

    /**
     * Obtiene la cantidad de frutas seleccionadas.
     */
    public int getFruitCount() {
        return selectedFruits.size();
    }

    /**
     * Obtiene la lista de frutas seleccionadas.
     */
    public List<FruitSelection> getSelectedFruits() {
        return selectedFruits;
    }
//--------------------------------------------------------------------
//Enemigos seleccionados con cantidad
    /*  
    * Enemigos seleccionados con cantidad
    */
    private final Map<String, Integer> selectedEnemies = new LinkedHashMap<>();

    /* 
    * Agrega un enemigo seleccionado con cantidad
    */
    public void addEnemy(String enemy, int amount) {
        selectedEnemies.put(enemy, amount);
    }

    /* 
    * Obtiene los enemigos seleccionados
    */
    public Map<String, Integer> getSelectedEnemies() {
        return selectedEnemies;
    }

    /*
    * Verifica si hay enemigos seleccionados
    */
    public boolean hasEnemies() {
        return !selectedEnemies.isEmpty();
    }

    /**
     * Verifica si un enemigo específico ha sido seleccionado.
     * @param enemy nombre del enemigo
     * @return true si el enemigo ha sido seleccionado, false en caso contrario
     */
    public boolean isEnemySelected(String enemy) {
        return selectedEnemies.containsKey(enemy);
    }
//---------------------------------------------------------------------
// Objeto seleccionado con cantidad

    /**
     * Establece el objeto seleccionado con su cantidad.
     * @param object nombre del objeto seleccionado
     */
    private final Map<String, Integer> selectedObjects = new LinkedHashMap<>();

    /**
     * Agrega un objeto seleccionado con su cantidad.
     * @param name nombre del objeto
     * @param amount cantidad del objeto
     */
    public void addObject(String name, int amount) {
        if (selectedObjects.size() >= 2) return;
        if (selectedObjects.containsKey(name)) return;

        selectedObjects.put(name, amount);
    }

    /**
     * Verifica si hay objetos seleccionados.
     * @return true si hay objetos seleccionados, false en caso contrario
     */
    public boolean hasObjects() {
        return !selectedObjects.isEmpty();
    }

    /**
     * Verifica si un objeto específico ha sido seleccionado.
     * @param name nombre del objeto
     * @return true si el objeto ha sido seleccionado, false en caso contrario
     */
    public boolean isObjectSelected(String name) {
        return selectedObjects.containsKey(name);
    }

    /**
     * Obtiene los objetos seleccionados con sus cantidades.
     * @return mapa de objetos seleccionados con sus cantidades
     */
    public Map<String, Integer> getSelectedObjects() {
        return selectedObjects;
    }

    /**
     * Convierte la configuración de presentation a domain
     */
    public GameConfig toGameConfig() {
        Map<String, Integer> fruitMap = new HashMap<>();
        for (FruitSelection fruit : selectedFruits) {
            fruitMap.put(fruit.name, fruit.amount);
        }

        Map<String, Integer> enemyMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : selectedEnemies.entrySet()) {
            enemyMap.put(entry.getKey(), entry.getValue());
        }

        Map<String, Integer> obstacleMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : selectedObjects.entrySet()) {
            obstacleMap.put(entry.getKey(), entry.getValue());
        }

        return new GameConfig(
            this.gameMode,
            this.character1,
            this.character2,
            this.selectedLevel,
            fruitMap,
            enemyMap,
            obstacleMap
        );
    }

    /**
     * Resetea todas las selecciones realizadas.
     */
    public void resetSelections() {
        selectedFruits.clear();
        selectedEnemies.clear();
        selectedObjects.clear();
        selectedLevel = 0;
        gameMode = null;
        character1 = null;
        character2 = null;
    }
}