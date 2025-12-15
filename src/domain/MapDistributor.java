package domain;

import java.util.*;

/**
 * Clase responsable de distribuir elementos (frutas, enemigos, obstáculos) 
 * en el mapa de manera equilibrada y estratégica.
 */
public class MapDistributor {

    private final Random rnd;

    /**
     * Constructor de la clase MapDistributor.
     */
    public MapDistributor() {
        this.rnd = new Random();
    }

    /**
     * Encuentra todas las posiciones vacías en el mapa.
     * @param lines líneas del mapa representadas como cadenas
     * @return lista de posiciones vacías
     */
    public List<Position> findEmpty(String[] lines) {
        List<Position> empty = new ArrayList<>();

        for (int r = 0; r < lines.length; r++) {
            String line = lines[r];
            for (int c = 0; c < line.length(); c++) {
                if (line.charAt(c) == '0') {
                    empty.add(new Position(r, c));
                }
            }
        }
        return empty;
    }

    /**
     * Divide las posiciones en zonas del mapa.
     * @param pos lista de posiciones a dividir
     * @param rows número de filas del mapa
     * @param cols número de columnas del mapa
     * @return mapa de zonas con sus respectivas posiciones
     */
    public Map<String, List<Position>> splitZones(List<Position> pos, int rows, int cols) {
        Map<String, List<Position>> zones = new HashMap<>();

        zones.put("TOP", new ArrayList<>());
        zones.put("MID", new ArrayList<>());
        zones.put("BOT", new ArrayList<>());
        zones.put("LEFT", new ArrayList<>());
        zones.put("RIGHT", new ArrayList<>());

        int topLim = rows / 3;
        int botLim = (rows * 2) / 3;
        int leftLim = cols / 3;
        int rightLim = (cols * 2) / 3;

        for (Position p : pos) {
            int r = p.getRow();
            int c = p.getCol();

            if (r < topLim) zones.get("TOP").add(p);
            else if (r > botLim) zones.get("BOT").add(p);
            else zones.get("MID").add(p);

            if (c < leftLim) zones.get("LEFT").add(p);
            else if (c > rightLim) zones.get("RIGHT").add(p);
        }
        return zones;
    }

    /**
     * Distribuye las frutas en el mapa.
     * @param cfg configuración del juego
     * @param empty posiciones vacías disponibles
     * @param rows número de filas del mapa
     * @param cols número de columnas del mapa
     * @param players posiciones de los jugadores
     * @return mapa de posiciones a tipos de frutas distribuidas
     */
    public Map<Position, String> placeFruits(GameConfig cfg, List<Position> empty,
                                             int rows, int cols, List<Position> players) {
        Map<Position, String> out = new HashMap<>();
        Map<String, List<Position>> zones = splitZones(empty, rows, cols);

        Map<String, Integer> fruits = cfg.getFruits();
        int total = cfg.getTotalFruits();

        List<String> fruitList = new ArrayList<>();
        for (Map.Entry<String, Integer> e : fruits.entrySet()) {
            for (int i = 0; i < e.getValue(); i++) {
                fruitList.add(e.getKey());
            }
        }

        Collections.shuffle(fruitList, rnd);
        List<Position> picked;

        if (total <= 4) picked = cornerPick(zones, total);
        else if (total <= 12) picked = balancedPick(zones, total);
        else picked = fullPick(empty, total, players);

        for (int i = 0; i < Math.min(fruitList.size(), picked.size()); i++) {
            out.put(picked.get(i), fruitList.get(i));
        }
        return out;
    }

    /**
     * Distribuye los enemigos en el mapa.
     * @param cfg configuración del juego
     * @param empty posiciones vacías disponibles
     * @param rows número de filas del mapa
     * @param cols número de columnas del mapa
     * @param players posiciones de los jugadores
     * @param fruitsPlaced frutas ya colocadas en el mapa
     * @return mapa de posiciones a tipos de enemigos distribuidos
     */
    public Map<Position, String> placeEnemies(GameConfig cfg, List<Position> empty,
                                              int rows, int cols, List<Position> players,
                                              Map<Position, String> fruitsPlaced) {
        Map<Position, String> out = new HashMap<>();

        List<Position> free = new ArrayList<>(empty);
        free.removeAll(fruitsPlaced.keySet());

        Map<String, Integer> enemies = cfg.getEnemies();
        int total = cfg.getTotalEnemies();

        List<String> enemyList = new ArrayList<>();
        for (Map.Entry<String, Integer> e : enemies.entrySet()) {
            for (int i = 0; i < e.getValue(); i++) {
                enemyList.add(e.getKey());
            }
        }

        List<Position> far = filterFar(free, players, 5);
        Map<String, List<Position>> zones = splitZones(far, rows, cols);
        List<Position> picked = balancedPick(zones, total);

        for (int i = 0; i < Math.min(enemyList.size(), picked.size()); i++) {
            out.put(picked.get(i), enemyList.get(i));
        }
        return out;
    }

    /**
     * Distribuye los obstáculos en el mapa.
     * @param cfg configuración del juego
     * @param empty posiciones vacías disponibles
     * @param fruitsPlaced frutas ya colocadas en el mapa
     * @param enemiesPlaced enemigos ya colocados en el mapa
     * @return mapa de posiciones a tipos de obstáculos distribuidos
     */
    public Map<Position, String> placeObstacles(GameConfig cfg, List<Position> empty,
                                                Map<Position, String> fruitsPlaced,
                                                Map<Position, String> enemiesPlaced) {
        Map<Position, String> out = new HashMap<>();

        List<Position> free = new ArrayList<>(empty);
        free.removeAll(fruitsPlaced.keySet());
        free.removeAll(enemiesPlaced.keySet());

        Map<String, Integer> obs = cfg.getObstacles();
        List<String> obsList = new ArrayList<>();

        for (Map.Entry<String, Integer> e : obs.entrySet()) {
            for (int i = 0; i < e.getValue(); i++) {
                obsList.add(e.getKey());
            }
        }

        Collections.shuffle(free, rnd);

        for (int i = 0; i < Math.min(obsList.size(), free.size()); i++) {
            out.put(free.get(i), obsList.get(i));
        }
        return out;
    }

    // ==================== HELPERS ====================

    /**
     * Selecciona posiciones en las esquinas del mapa.
     * @param zones zonas del mapa divididas
     * @param count número de posiciones a seleccionar
     * @return lista de posiciones seleccionadas
     */
    private List<Position> cornerPick(Map<String, List<Position>> zones, int count) {
        List<Position> out = new ArrayList<>();
        List<Position> top = new ArrayList<>(zones.get("TOP"));
        List<Position> bot = new ArrayList<>(zones.get("BOT"));

        Collections.shuffle(top, rnd);
        Collections.shuffle(bot, rnd);

        for (int i = 0; i < count; i++) {
            if (i % 2 == 0 && !top.isEmpty()) out.add(top.remove(0));
            else if (!bot.isEmpty()) out.add(bot.remove(0));
            else if (!top.isEmpty()) out.add(top.remove(0));
        }
        return out;
    }

    /**
     * Selecciona posiciones de manera equilibrada en el mapa.
     * @param zones zonas del mapa divididas
     * @param count número de posiciones a seleccionar
     * @return lista de posiciones seleccionadas
     */
    private List<Position> balancedPick(Map<String, List<Position>> zones, int count) {
        List<Position> out = new ArrayList<>();

        List<Position> top = new ArrayList<>(zones.get("TOP"));
        List<Position> mid = new ArrayList<>(zones.get("MID"));
        List<Position> bot = new ArrayList<>(zones.get("BOT"));

        Collections.shuffle(top, rnd);
        Collections.shuffle(mid, rnd);
        Collections.shuffle(bot, rnd);

        int per = count / 3;
        int rem = count % 3;

        out.addAll(top.subList(0, Math.min(per + (rem > 0 ? 1 : 0), top.size())));
        out.addAll(mid.subList(0, Math.min(per + (rem > 1 ? 1 : 0), mid.size())));
        out.addAll(bot.subList(0, Math.min(per, bot.size())));
        return out;
    }

    /**
     * Selecciona posiciones libremente en el mapa.
     * @param pos lista de posiciones a seleccionar
     * @param count número de posiciones a seleccionar
     * @param players posiciones de los jugadores
     * @return lista de posiciones seleccionadas
     */
    private List<Position> fullPick(List<Position> pos, int count,
                                    @SuppressWarnings("unused") List<Position> players) {
        List<Position> free = new ArrayList<>(pos);
        Collections.shuffle(free, rnd);
        return free.subList(0, Math.min(count, free.size()));
    }

    /**
     * Filtra posiciones que estén lejos de los jugadores.
     * @param pos lista de posiciones a filtrar
     * @param players posiciones de los jugadores
     * @param minDist distancia mínima requerida
     * @return lista de posiciones filtradas
     */
    private List<Position> filterFar(List<Position> pos, List<Position> players, int minDist) {
        List<Position> out = new ArrayList<>();

        for (Position p : pos) {
            boolean ok = true;

            for (Position pl : players) {
                int d = Math.abs(p.getRow() - pl.getRow())
                      + Math.abs(p.getCol() - pl.getCol());
                if (d < minDist) {
                    ok = false;
                    break;
                }
            }
            if (ok) out.add(p);
        }
        return out;
    }
}
