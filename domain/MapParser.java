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
        MapDistributor distributor = new MapDistributor();

        List<IceCream> players = new ArrayList<>();
        //List<Fruit> fruits = new ArrayList<>();
        //List<Enemy> enemies = new ArrayList<>();
        List<Character> playerTypes = new ArrayList<>();
        List<Position> posicionesJugadores = new ArrayList<>();
        List<Position> posicionesVacias = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            String line = lines[r];
            for (int c = 0; c < line.length() && c < cols; c++) {
                char ch = line.charAt(c);
                Position pos = new Position(r, c);

                if ("CSVRJE".indexOf(ch) >= 0) {
                    playerTypes.add(ch);
                    posicionesJugadores.add(pos);
                }

                if (ch == '0' || ch == 'F' || ch == 'G') {
                    posicionesVacias.add(pos);
                }

                parseCell(ch, pos, map, players);
            }
        }

        // ========== PASO 2: Distribuir FRUTAS dinámicamente ==========
        Map<Position, String> frutasDistribuidas = distributor.distribuirFrutas(
            config, posicionesVacias, rows, cols, posicionesJugadores
        );
        
        // ⭐ PASO 3: Distribuir enemigos alejados de jugadores
        Map<Position, String> enemigosDistribuidos = distributor.distribuirEnemigos(
            config, posicionesVacias, rows, cols, posicionesJugadores, frutasDistribuidas
        );
        
        // ⭐ PASO 4: Distribuir obstáculos
        Map<Position, String> obstaculosDistribuidos = distributor.distribuirObstaculos(
            config, posicionesVacias, frutasDistribuidas, enemigosDistribuidos
        );
        
        // ⭐ PASO 5: Crear entidades
        List<Fruit> frutas = crearFrutas(frutasDistribuidas);
        List<Enemy> enemigos = crearEnemigos(enemigosDistribuidos, map);
        crearObstaculos(obstaculosDistribuidos, map);

        map.saveInitialBlockStates();
        BadIceCream game = new BadIceCream(map);

        for (int i = 0; i < players.size(); i++) {
            IceCream p = players.get(i);
            String flavor = (i == 0) ? config.getCharacter1() : config.getCharacter2();
            p.setFlavor(flavor);
        }

        for (int i = 0; i < players.size(); i++) {
            IceCream p = players.get(i);
            //char type = playerTypes.get(i);

            String flavor = (i == 0)
                    ? config.getCharacter1()
                    : config.getCharacter2();

            if (flavor == null) {
                flavor = "Vanilla";
            }

            p.setFlavor(flavor);
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

        for (Fruit f : frutas) {
            game.addFruit(f);
            map.addFruit(f);
        }

        for (Enemy e : enemigos) {
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

    private static List<Fruit> crearFrutas(Map<Position, String> distribucion) {
        List<Fruit> frutas = new ArrayList<>();
        
        for (Map.Entry<Position, String> entry : distribucion.entrySet()) {
            Position pos = entry.getKey();
            String tipo = entry.getValue();
            
            Fruit fruta = null;
            switch (tipo) {
                case "Banana":
                    fruta = new Banana(pos);
                    break;
                case "Grape":
                    fruta = new Grape(pos);
                    break;
                case "Cherry":
                    fruta = new Cherry(pos);
                    break;
                case "Pineapple":
                    fruta = new Pineapple(pos);
                    break;
                case "Cactus":
                    fruta = new Cactus(pos);
                    break;
            }
            
            if (fruta != null) {
                frutas.add(fruta);
            }
        }
        
        return frutas;
    }
    
    private static List<Enemy> crearEnemigos(Map<Position, String> distribucion, GameMap map) {
        List<Enemy> enemigos = new ArrayList<>();
        
        for (Map.Entry<Position, String> entry : distribucion.entrySet()) {
            Position pos = entry.getKey();
            String tipo = entry.getValue();
            
            Enemy enemigo = null;
            switch (tipo) {
                case "Troll":
                    enemigo = new Troll(pos);
                    break;
                case "Flowerpot":
                    enemigo = new Flowerpot(pos);
                    break;
                case "Narwhal":
                    enemigo = new Narval(pos);
                    break;
                case "YellowSquid":
                    enemigo = new YellowSquid(pos);
                    break;
            }
            
            if (enemigo != null) {
                enemigos.add(enemigo);
            }
        }
        
        return enemigos;
    }
    
    private static void crearObstaculos(Map<Position, String> distribucion, GameMap map) {
        for (Map.Entry<Position, String> entry : distribucion.entrySet()) {
            Position pos = entry.getKey();
            String tipo = entry.getValue();
            
            Boxy obstaculo = null;
            switch (tipo) {
                case "Fire":
                    obstaculo = new Fire(pos, BoxState.created);
                    break;
                case "Bonfire":
                    obstaculo = new Bonfire(pos, BoxState.on);
                    break;
            }
            
            if (obstaculo != null) {
                obstaculo.setGameMap(map);
                map.setBlock(pos, obstaculo);
            }
        }
    }
}