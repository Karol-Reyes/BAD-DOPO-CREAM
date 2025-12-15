package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Analiza una representación textual del mapa y crea la instancia del juego correspondiente.
 */
public class MapParser {

    /**
     * Construye una instancia del juego a partir de un mapa en texto y la configuración inicial.
     * Interpreta cada carácter del mapa, crea los objetos correspondientes y los registra en el juego.
     * @param mapText representación textual del mapa del nivel
     * @param config configuración del juego (sabores, jugadores, etc.)
     * @return instancia completamente inicializada del juego
     */
    public static BadIceCream parseMap(String mapText, GameConfig config) {

        String[] lines = mapText.split("\n");
        int rows = lines.length;
        int cols = lines[0].length();

        GameMap map = new GameMap(rows, cols);
        MapDistributor dist = new MapDistributor();

        List<IceCream> players = new ArrayList<>();
        List<Character> playerTypes = new ArrayList<>();
        List<Position> playerPos = new ArrayList<>();
        List<Position> emptyPos = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            String line = lines[r];
            for (int c = 0; c < line.length() && c < cols; c++) {
                char ch = line.charAt(c);
                Position pos = new Position(r, c);

                if ("CSVRJE".indexOf(ch) >= 0) {
                    playerTypes.add(ch);
                    playerPos.add(pos);
                }

                if (ch == '0' || ch == 'F' || ch == 'G') {
                    emptyPos.add(pos);
                }

                parseCell(ch, pos, map, players);
            }
        }

        Map<Position, String> fruitMap = dist.placeFruits(
                config, emptyPos, rows, cols, playerPos
        );

        Map<Position, String> enemyMap = dist.placeEnemies(
                config, emptyPos, rows, cols, playerPos, fruitMap
        );

        Map<Position, String> obsMap = dist.placeObstacles(
                config, emptyPos, fruitMap, enemyMap
        );

        List<Fruit> fruits = buildFruits(fruitMap);
        List<Enemy> enemies = buildEnemies(enemyMap);
        buildObstacles(obsMap, map);

        map.saveInitialBlockStates();
        BadIceCream game = new BadIceCream(map);

        for (int i = 0; i < players.size(); i++) {
            IceCream p = players.get(i);
            String flavor = (i == 0) ? config.getCharacter1() : config.getCharacter2();
            p.setFlavor(flavor);
        }

        for (int i = 0; i < players.size(); i++) {
            IceCream p = players.get(i);

            String flavor = (i == 0)
                    ? config.getCharacter1()
                    : config.getCharacter2();

            if (flavor == null) {
                flavor = "Vanilla";
            }

            p.setFlavor(flavor);
        }

        for (int i = 0; i < players.size(); i++) {
            IceCream p = players.get(i);
            char type = playerTypes.get(i);

            p.setGameMap(map);
            ControllerCream ctrl;

            switch (type) {
                case 'C', 'S', 'V' -> ctrl = new Player();
                case 'R' -> ctrl = new Hungry(map, game);
                case 'J' -> ctrl = new Fearful(map, game);
                case 'E' -> ctrl = new Expert(map, game);
                default -> throw new IllegalArgumentException("Unknown player type: " + type);
            }

            ctrl.setPlayer(p);
            p.setController(ctrl);

            game.addController(ctrl);
            game.addPlayer(p);
            map.addPlayer(p);
        }

        for (Fruit f : fruits) {
            game.addFruit(f);
            map.addFruit(f);
        }

        for (Enemy e : enemies) {
            e.setGameMap(map);
            game.addEnemy(e);
            map.addEnemy(e);
        }

        game.initializeAfterMapLoad();
        return game;
    }

    /**
     * Interpreta un carácter del mapa y crea el objeto correspondiente en la posición indicada.
     * @param ch carácter leído del mapa
     * @param pos posición dentro del mapa
     * @param map mapa del juego
     * @param players lista de jugadores detectados
     */
    private static void parseCell(
            char ch,
            Position pos,
            GameMap map,
            List<IceCream> players) {

        map.setBlock(pos, new Floor(pos, BoxState.inactive));

        switch (ch) {
            case 'H' -> map.setBlock(pos, new Iron(pos, BoxState.indestructible));
            case '1' -> map.setBlock(pos, new Ice(pos, BoxState.created));
            case 'K' -> {
                Fire f = new Fire(pos, BoxState.on);
                f.setGameMap(map);
                map.setBlock(pos, f);
            }
            case 'L' -> {
                Bonfire b = new Bonfire(pos, BoxState.on);
                b.setGameMap(map);
                map.setBlock(pos, b);
            }
            case 'C', 'S', 'V' -> {
                IceCream p = new IceCream(pos);
                p.setGameMap(map);
                players.add(p);
            }
            case 'R' -> {
                IceCream p = new IceCream(pos);
                p.setGameMap(map);
                p.setFlavor("hungry");
                players.add(p);
            }
            case 'J' -> {
                IceCream p = new IceCream(pos);
                p.setGameMap(map);
                p.setFlavor("fearful");
                players.add(p);
            }
            case 'E' -> {
                IceCream p = new IceCream(pos);
                p.setGameMap(map);
                p.setFlavor("expert");
                players.add(p);
            }
        }
    }

    /**
     * Crea las frutas según la distribución proporcionada.
     * @param distribucion mapa de posiciones a tipos de frutas
     * @return lista de frutas creadas
     */
    private static List<Fruit> buildFruits(Map<Position, String> distribucion) {
        List<Fruit> fruits = new ArrayList<>();

        for (Map.Entry<Position, String> e : distribucion.entrySet()) {
            Position pos = e.getKey();
            String type = e.getValue();

            Fruit fruit = null;
            switch (type) {
                case "Banana" -> fruit = new Banana(pos);
                case "Grape" -> fruit = new Grape(pos);
                case "Cherry" -> fruit = new Cherry(pos);
                case "Pineapple" -> fruit = new Pineapple(pos);
                case "Cactus" -> fruit = new Cactus(pos);
            }

            if (fruit != null) {
                fruits.add(fruit);
            }
        }
        return fruits;
    }

    /**
     * Crea los enemigos según la distribución proporcionada.
     * @param distribucion mapa de posiciones a tipos de enemigos
     * @return lista de enemigos creados
     */
    private static List<Enemy> buildEnemies(Map<Position, String> distribucion) {
        List<Enemy> enemies = new ArrayList<>();

        for (Map.Entry<Position, String> e : distribucion.entrySet()) {
            Position pos = e.getKey();
            String type = e.getValue();

            Enemy enemy = null;
            switch (type) {
                case "Troll" -> enemy = new Troll(pos);
                case "Flowerpot" -> enemy = new Flowerpot(pos);
                case "Narwhal" -> enemy = new Narval(pos);
                case "YellowSquid" -> enemy = new YellowSquid(pos);
            }

            if (enemy != null) {
                enemies.add(enemy);
            }
        }
        return enemies;
    }

    /**
     * Crea los obstáculos según la distribución proporcionada y los añade al mapa.
     * @param distribucion mapa de posiciones a tipos de obstáculos
     * @param map mapa del juego donde se añadirán los obstáculos
     */
    private static void buildObstacles(Map<Position, String> distribucion, GameMap map) {
        for (Map.Entry<Position, String> e : distribucion.entrySet()) {
            Position pos = e.getKey();
            String type = e.getValue();

            Boxy obs = null;
            switch (type) {
                case "Fire" -> obs = new Fire(pos, BoxState.created);
                case "Bonfire" -> obs = new Bonfire(pos, BoxState.on);
            }

            if (obs != null) {
                obs.setGameMap(map);
                map.setBlock(pos, obs);
            }
        }
    }
}
