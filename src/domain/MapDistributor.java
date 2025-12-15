package domain;

import java.util.*;

/**
 * Clase responsable de distribuir elementos del juego
 * (frutas, enemigos y obstáculos) dentro del mapa de forma
 * balanceada y, cuando es posible, simétrica.
 */
public class MapDistributor {

    private final Random random;

    /**
     * Crea un distribuidor de mapa con un generador de números aleatorios.
     */
    public MapDistributor() {
        this.random = new Random();
    }

    /**
     * Encuentra todas las posiciones vacías representadas por el carácter '0'
     * dentro del mapa textual.
     * @param lines arreglo de strings que representan el mapa
     * @return lista de posiciones vacías encontradas
     */
    public List<Position> findEmptyPositions(String[] lines) {
        List<Position> emptyPositions = new ArrayList<>();

        for (int r = 0; r < lines.length; r++) {
            String line = lines[r];
            for (int c = 0; c < line.length(); c++) {
                if (line.charAt(c) == '0') {
                    emptyPositions.add(new Position(r, c));
                }
            }
        }
        return emptyPositions;
    }

    /**
     * Divide el mapa en diferentes zonas para permitir una distribución
     * equilibrada de entidades.
     * @param positions lista de posiciones disponibles
     * @param rows número total de filas del mapa
     * @param cols número total de columnas del mapa
     * @return mapa que asocia nombres de zonas con listas de posiciones
     */
    public Map<String, List<Position>> divideIntoZones(List<Position> positions, int rows, int cols) {
        Map<String, List<Position>> zones = new HashMap<>();

        zones.put("TOP", new ArrayList<>());
        zones.put("CENTER", new ArrayList<>());
        zones.put("BOTTOM", new ArrayList<>());
        zones.put("LEFT", new ArrayList<>());
        zones.put("RIGHT", new ArrayList<>());

        int topLimit = rows / 3;
        int bottomLimit = (rows * 2) / 3;
        int leftLimit = cols / 3;
        int rightLimit = (cols * 2) / 3;

        for (Position pos : positions) {
            int r = pos.getRow();
            int c = pos.getCol();

            if (r < topLimit) {
                zones.get("TOP").add(pos);
            } else if (r > bottomLimit) {
                zones.get("BOTTOM").add(pos);
            } else {
                zones.get("CENTER").add(pos);
            }

            if (c < leftLimit) {
                zones.get("LEFT").add(pos);
            } else if (c > rightLimit) {
                zones.get("RIGHT").add(pos);
            }
        }
        return zones;
    }

    /**
     * Distribuye las frutas en el mapa de forma balanceada y simétrica
     * según la cantidad total configurada.
     * @param config configuración del juego
     * @param emptyPositions posiciones disponibles
     * @param rows número de filas del mapa
     * @param cols número de columnas del mapa
     * @param playerPositions posiciones iniciales de los jugadores
     * @return mapa que asocia posiciones con tipos de frutas
     */
    public Map<Position, String> distributeFruits(GameConfig config, List<Position> emptyPositions, int rows,
        int cols, List<Position> playerPositions) {

        Map<Position, String> distribution = new HashMap<>();
        Map<String, List<Position>> zones = divideIntoZones(emptyPositions, rows, cols);

        Map<String, Integer> fruits = config.getFruits();
        int totalFruits = config.getTotalFruits();

        List<String> fruitList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : fruits.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                fruitList.add(entry.getKey());
            }
        }

        Collections.shuffle(fruitList, random);
        List<Position> selectedPositions;

        if (totalFruits <= 4) {
            selectedPositions = distributeCorners(zones, totalFruits);
        } else if (totalFruits <= 12) {
            selectedPositions = distributeBalanced(zones, totalFruits);
        } else {
            selectedPositions = distributeAcrossMap(emptyPositions, totalFruits, playerPositions);
        }
        for (int i = 0; i < Math.min(fruitList.size(), selectedPositions.size()); i++) {
            distribution.put(selectedPositions.get(i), fruitList.get(i));
        }
        return distribution;
    }

    /**
     * Distribuye enemigos en posiciones alejadas de los jugadores.
     * @param config configuración del juego
     * @param emptyPositions posiciones disponibles
     * @param rows número de filas del mapa
     * @param cols número de columnas del mapa
     * @param playerPositions posiciones de los jugadores
     * @param placedFruits frutas ya colocadas
     * @return mapa que asocia posiciones con tipos de enemigos
     */
    public Map<Position, String> distributeEnemies(GameConfig config, List<Position> emptyPositions, int rows,
        int cols, List<Position> playerPositions, Map<Position, String> placedFruits) {

        Map<Position, String> distribution = new HashMap<>();

        List<Position> available = new ArrayList<>(emptyPositions);
        available.removeAll(placedFruits.keySet());

        Map<String, Integer> enemies = config.getEnemies();
        int totalEnemies = config.getTotalEnemies();

        List<String> enemyList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : enemies.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                enemyList.add(entry.getKey());
            }
        }

        List<Position> farPositions = filterFarPositions(available, playerPositions, 5);
        Map<String, List<Position>> zones = divideIntoZones(farPositions, rows, cols);
        List<Position> selectedPositions = distributeBalanced(zones, totalEnemies);

        for (int i = 0; i < Math.min(enemyList.size(), selectedPositions.size()); i++) {
            distribution.put(selectedPositions.get(i), enemyList.get(i));
        }
        return distribution;
    }

    /**
     * Distribuye obstáculos de forma aleatoria evitando posiciones ocupadas.
     * @param config configuración del juego
     * @param emptyPositions posiciones disponibles
     * @param placedFruits frutas ya colocadas
     * @param placedEnemies enemigos ya colocados
     * @return mapa que asocia posiciones con tipos de obstáculos
     */
    public Map<Position, String> distributeObstacles(GameConfig config, List<Position> emptyPositions,
        Map<Position, String> placedFruits, Map<Position, String> placedEnemies) {

        Map<Position, String> distribution = new HashMap<>();

        List<Position> available = new ArrayList<>(emptyPositions);
        available.removeAll(placedFruits.keySet());
        available.removeAll(placedEnemies.keySet());

        Map<String, Integer> obstacles = config.getObstacles();

        List<String> obstacleList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : obstacles.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                obstacleList.add(entry.getKey());
            }
        }

        Collections.shuffle(available, random);

        for (int i = 0; i < Math.min(obstacleList.size(), available.size()); i++) {
            distribution.put(available.get(i), obstacleList.get(i));
        }

        return distribution;
    }

    /**
     * Selecciona posiciones alternando entre zonas superiores e inferiores.
     */
    private List<Position> distributeCorners(Map<String, List<Position>> zones, int amount) {
        List<Position> selected = new ArrayList<>();

        List<Position> top = new ArrayList<>(zones.get("TOP"));
        List<Position> bottom = new ArrayList<>(zones.get("BOTTOM"));

        Collections.shuffle(top, random);
        Collections.shuffle(bottom, random);

        for (int i = 0; i < amount; i++) {
            if (i % 2 == 0 && !top.isEmpty()) {
                selected.add(top.remove(0));
            } else if (!bottom.isEmpty()) {
                selected.add(bottom.remove(0));
            } else if (!top.isEmpty()) {
                selected.add(top.remove(0));
            }
        }
        return selected;
    }

    /**
     * Distribuye posiciones equitativamente entre zonas del mapa.
     */
    private List<Position> distributeBalanced(Map<String, List<Position>> zones, int amount) {
        List<Position> selected = new ArrayList<>();

        List<Position> top = new ArrayList<>(zones.get("TOP"));
        List<Position> center = new ArrayList<>(zones.get("CENTER"));
        List<Position> bottom = new ArrayList<>(zones.get("BOTTOM"));

        Collections.shuffle(top, random);
        Collections.shuffle(center, random);
        Collections.shuffle(bottom, random);

        int perZone = amount / 3;
        int remainder = amount % 3;

        selected.addAll(top.subList(0, Math.min(perZone + (remainder > 0 ? 1 : 0), top.size())));
        selected.addAll(center.subList(0, Math.min(perZone + (remainder > 1 ? 1 : 0), center.size())));
        selected.addAll(bottom.subList(0, Math.min(perZone, bottom.size())));

        return selected;
    }

    /**
     * Distribuye posiciones a lo largo de todo el mapa sin restricciones.
     */
    private List<Position> distributeAcrossMap(List<Position> positions, 
        int amount, @SuppressWarnings("unused") List<Position> players) {

        List<Position> available = new ArrayList<>(positions);
        Collections.shuffle(available, random);
        return available.subList(0, Math.min(amount, available.size()));
    }

    /**
     * Filtra posiciones que se encuentren a una distancia mínima
     * de los jugadores.
     */
    private List<Position> filterFarPositions(List<Position> positions, List<Position> players, int minDistance) {

        List<Position> farPositions = new ArrayList<>();
        for (Position pos : positions) {
            boolean isFar = true;
            for (Position player : players) {
                int distance = Math.abs(pos.getRow() - player.getRow())
                             + Math.abs(pos.getCol() - player.getCol());

                if (distance < minDistance) {
                    isFar = false;
                    break;
                }
            }
            if (isFar) {
                farPositions.add(pos);
            }
        }
        return farPositions;
    }
}