package domain;

import java.util.ArrayList;
import java.util.List;

public class MapParser {
    
    public static BadIceCream parseMap(String mapaTexto, GameConfig config) {
        
        String[] lineas = mapaTexto.split("\n");
        int rows = lineas.length;
        int cols = lineas[0].length();
        
        GameMap gameMap = new GameMap(rows, cols);
        
        List<IceCream> jugadores = new ArrayList<>();
        List<Fruit> frutas = new ArrayList<>();
        List<Enemy> enemigos = new ArrayList<>();
        List<Character> tiposJugadores = new ArrayList<>();
        
        // Recorrer cada celda
        for (int r = 0; r < rows; r++) {
            String linea = lineas[r];
            for (int c = 0; c < linea.length() && c < cols; c++) {
                char ch = linea.charAt(c);
                Position pos = new Position(r, c);

                if (ch == 'C' || ch == 'S' || ch == 'V' || ch == 'R' || ch == 'J' || ch == 'E') {
                    tiposJugadores.add(ch);
                }
                
                parseCell(ch, pos, gameMap, jugadores, frutas, enemigos);
            }
        }
        
        gameMap.saveInitialBlockStates();
        
        BadIceCream game = new BadIceCream(gameMap);

        for (int i = 0; i < jugadores.size(); i++) {
            IceCream p = jugadores.get(i);
            String flavor = (i == 0) ? config.getCharacter1() : config.getCharacter2();
            // Puede ser null si no hay segundo jugador; es aceptable
            p.setFlavor(flavor);
        }

        for (int i = 0; i < jugadores.size(); i++) {

            IceCream p = jugadores.get(i);
            char tipo = tiposJugadores.get(i);

            // Jugadores humanos (C, S, V)
            if (tipo == 'C' || tipo == 'S' || tipo == 'V') {
                String flavor = (i == 0) 
                                ? config.getCharacter1() 
                                : config.getCharacter2();

                if (flavor == null) flavor = "Vanilla";

                p.setFlavor(flavor);
            } 
            else {
                // Bots siempre usan default
                p.setFlavor("Vanilla");
            }
        }

        for (int i = 0; i < jugadores.size(); i++) {
            IceCream player = jugadores.get(i);
            char tipo = tiposJugadores.get(i);
        
            player.setGameMap(gameMap);
            ControllerCream controller = null;

            // Asignar controller según tipo
            switch (tipo) {
                case 'C': case 'S': case 'V':
                    controller = new Player();
                    break;
                case 'R':
                    controller = new Hungry(gameMap, game);
                    break;
                case 'J':
                    controller = new Fearful(gameMap, game);
                    break;
                case 'E':
                    controller = new Expert(gameMap, game);
                    break;
                }
            controller.setPlayer(player);
            game.addController(controller);
            game.addPlayer(player);
            player.setController(controller);
            gameMap.addPlayer(player);
        }

        for (Fruit fruit : frutas) {
            game.addFruit(fruit);
            gameMap.addFruit(fruit);
        }
        for (Enemy enemy : enemigos) {
            enemy.setGameMap(gameMap);
            game.addEnemy(enemy);
            gameMap.addEnemy(enemy);
        }

        game.initializeAfterMapLoad();   
        return game;
    }
    
    private static void parseCell(char ch, Position pos, GameMap map, 
                                   List<IceCream> jugadores, List<Fruit> frutas, 
                                   List<Enemy> enemigos) {
        
        map.setBlock(pos, new Floor(pos, BoxState.inactive));

        switch (ch) {
            // ============ BLOQUES ============
            case 'H': // Hierro
                map.setBlock(pos, new Iron(pos, BoxState.indestructible));
                break;
                
            case '1': // Hielo
                map.setBlock(pos, new Ice(pos, BoxState.created));
                break;
                
            case 'K': // Fuego
                Fire f = new Fire(pos, BoxState.on);
                f.setGameMap(map);
                map.setBlock(pos, f);
                break;
                
            case 'L': // Fogata
                Bonfire b = new Bonfire(pos, BoxState.on);
                b.setGameMap(map);
                map.setBlock(pos, b);
                break;
                
            // ============ JUGADORES ============
            case 'C': 
            case 'S': 
            case 'V':
                IceCream jugador = new IceCream(pos);
                jugador.setGameMap(map);
                jugadores.add(jugador);
                break;

            case 'R': 
                IceCream hungry = new IceCream(pos);
                hungry.setGameMap(map);
                hungry.setFlavor("Vanilla");
                jugadores.add(hungry);
                break;
            case 'J': 
                IceCream fearful = new IceCream(pos);
                fearful.setGameMap(map);
                fearful.setFlavor("Vanilla");
                jugadores.add(fearful);
                break;
            case 'E':
                IceCream expert = new IceCream(pos);
                expert.setGameMap(map);
                expert.setFlavor("Vanilla");
                jugadores.add(expert);
                break;

            // ============ FRUTAS ============
            case 'B': frutas.add(new Banana(pos)); break;
            case 'A': frutas.add(new Grape(pos)); break;
            case 'D': frutas.add(new Cherry(pos)); break;
            case 'Z': frutas.add(new Pineapple(pos)); break;
            case 'Q': frutas.add(new Cactus(pos)); break;
                
            // ============ ENEMIGOS ============
            case 'O': enemigos.add(new Troll(pos, 0.0001)); break;
            case 'W': enemigos.add(new Flowerpot(pos, 0.0001)); break;
            case 'P': enemigos.add(new Narval(pos, 0.00001)); break;
            case 'U': enemigos.add(new YellowSquid(pos, 0.00001)); break;
                
            // ============ VACÍO ============
            case '0': 
                // Mantener como Ice inactivo (ya viene por defecto)
                break;
        }
    }
}