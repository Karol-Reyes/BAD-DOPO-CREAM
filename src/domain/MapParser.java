package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase encargada de analizar una representación textual de un mapa
 * y construir completamente una instancia del juego a partir de ella.
 */
public class MapParser {

    /**
     * Construye una instancia completa del juego a partir de un mapa en texto
     * y una configuración inicial.
     * @param mapText representación textual del mapa del nivel
     * @param config configuración inicial del juego (jugadores, sabores, enemigos, etc.)
     * @return instancia completamente inicializada del juego
     */
    public static BadIceCream parseMap(String mapText, GameConfig config) {

        String[] lines = mapText.split("\n");
        int rows = lines.length;
        int cols = lines[0].length();

        GameMap map = new GameMap(rows, cols);
        MapDistributor distributor = new MapDistributor();

        List<IceCream> players = new ArrayList<>();
        List<Character> playerTypes = new ArrayList<>();
        List<Position> playerPositions = new ArrayList<>();
        List<Position> emptyPositions = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            String line = lines[r];
            for (int c = 0; c < line.length() && c < cols; c++) {
                char ch = line.charAt(c);
                Position pos = new Position(r, c);

                if ("CSVRJE".indexOf(ch) >= 0) {
                    playerTypes.add(ch);
                    playerPositions.add(pos);
                }

                if (ch == '0' || ch == 'F' || ch == 'G') {
                    emptyPositions.add(pos);
                }
                parseCell(ch, pos, map, players);
            }
        }

        Map<Position, String> distributedFruits = distributor.distributeFruits(
                config, emptyPositions, rows, cols, playerPositions
        );

        Map<Position, String> distributedEnemies = distributor.distributeEnemies(
                config, emptyPositions, rows, cols, playerPositions, distributedFruits
        );

        Map<Position, String> distributedObstacles = distributor.distributeObstacles(
                config, emptyPositions, distributedFruits, distributedEnemies
        );

        List<Fruit> fruits = createFruits(distributedFruits);
        List<Enemy> enemies = createEnemies(distributedEnemies);
        createObstacles(distributedObstacles, map);

        map.saveInitialBlockStates();
        BadIceCream game = new BadIceCream(map);

        for (int i = 0; i < players.size(); i++) {
            IceCream player = players.get(i);
            String flavor = (i == 0) ? config.getCharacter1() : config.getCharacter2();
            player.setFlavor(flavor);
        }

        for (int i = 0; i < players.size(); i++) {
            IceCream player = players.get(i);

            String flavor = (i == 0)
                    ? config.getCharacter1()
                    : config.getCharacter2();

            if (flavor == null) {
                flavor = "Vanilla";
            }
            player.setFlavor(flavor);
        }

        for (int i = 0; i < players.size(); i++) {
            IceCream player = players.get(i);
            char type = playerTypes.get(i);

            player.setGameMap(map);
            ControllerCream controller;

            switch (type) {
                case 'C', 'S', 'V' -> controller = new Player();
                case 'R' -> controller = new Hungry(map, game);
                case 'J' -> controller = new Fearful(map, game);
                case 'E' -> controller = new Expert(map, game);
                default -> throw new IllegalArgumentException("Unknown player type: " + type);
            }

            controller.setPlayer(player);
            player.setController(controller);
            game.addController(controller);
            game.addPlayer(player);
            map.addPlayer(player);
        }

        for (Fruit fruit : fruits) {
            game.addFruit(fruit);
            map.addFruit(fruit);
        }

        for (Enemy enemy : enemies) {
            enemy.setGameMap(map);
            game.addEnemy(enemy);
            map.addEnemy(enemy);
        }
        game.initializeAfterMapLoad();
        return game;
    }

    /**
     * Interpreta un carácter del mapa y crea el bloque o entidad
     * correspondiente en la posición indicada.
     * @param ch carácter leído del mapa
     * @param pos posición dentro del mapa
     * @param map mapa del juego
     * @param players lista donde se almacenan los jugadores detectados
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
                Fire fire = new Fire(pos, BoxState.on);
                fire.setGameMap(map);
                map.setBlock(pos, fire);
            }
            case 'L' -> {
                Bonfire bonfire = new Bonfire(pos, BoxState.on);
                bonfire.setGameMap(map);
                map.setBlock(pos, bonfire);
            }
            case 'C', 'S', 'V' -> {
                IceCream player = new IceCream(pos);
                player.setGameMap(map);
                players.add(player);
            }
            case 'R' -> {
                IceCream player = new IceCream(pos);
                player.setGameMap(map);
                player.setFlavor("hungry");
                players.add(player);
            }
            case 'J' -> {
                IceCream player = new IceCream(pos);
                player.setGameMap(map);
                player.setFlavor("fearful");
                players.add(player);
            }
            case 'E' -> {
                IceCream player = new IceCream(pos);
                player.setGameMap(map);
                player.setFlavor("expert");
                players.add(player);
            }
        }
    }

    /**
     * Crea las frutas a partir de una distribución previamente calculada.
     * @param distribution mapa que asocia posiciones con tipos de frutas
     * @return lista de frutas creadas
     */
    private static List<Fruit> createFruits(Map<Position, String> distribution) {
        List<Fruit> fruits = new ArrayList<>();

        for (Map.Entry<Position, String> entry : distribution.entrySet()) {
            Position pos = entry.getKey();
            String type = entry.getValue();

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
     * Crea los enemigos a partir de una distribución previamente calculada.
     * @param distribution mapa que asocia posiciones con tipos de enemigos
     * @return lista de enemigos creados
     */
    private static List<Enemy> createEnemies(Map<Position, String> distribution) {
        List<Enemy> enemies = new ArrayList<>();

        for (Map.Entry<Position, String> entry : distribution.entrySet()) {
            Position pos = entry.getKey();
            String type = entry.getValue();

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
     * Crea y coloca los obstáculos en el mapa según la distribución indicada.
     * @param distribution mapa que asocia posiciones con tipos de obstáculos
     * @param map mapa del juego donde se colocarán
     */
    private static void createObstacles(Map<Position, String> distribution, GameMap map) {
        for (Map.Entry<Position, String> entry : distribution.entrySet()) {
            Position pos = entry.getKey();
            String type = entry.getValue();

            Boxy obstacle = null;
            switch (type) {
                case "Fire" -> obstacle = new Fire(pos, BoxState.created);
                case "Bonfire" -> obstacle = new Bonfire(pos, BoxState.on);
            }

            if (obstacle != null) {
                obstacle.setGameMap(map);
                map.setBlock(pos, obstacle);
            }
        }
    }
}