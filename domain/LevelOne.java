/*
package domain;

public class LevelOne extends Level {

    public LevelOne() {
        super(18, 18); // tamaño del mapa
    }

    @Override
    protected void buildStructure() {
        createBorders();
        addBoxesCenter();
    }

    private void createBorders() {
        for (int col = 0; col < cols; col++) {
            map.getBlock(new Position(0, col)).create();
            map.getBlock(new Position(rows - 1, col)).create();
        }
        for (int row = 0; row < rows; row++) {
            map.getBlock(new Position(row, 0)).create();
            map.getBlock(new Position(row, cols - 1)).create();
        }
    }

    private void addBoxesCenter() {
        Position[] boxPositions = {
            new Position(4,4), new Position(4,5), new Position(4,6),
            new Position(4,9), new Position(4,10), new Position(4,11),
            new Position(5,4), new Position(5,11),
            new Position(6,4), new Position(6,11),
            new Position(7,4), new Position(7,11),
            new Position(8,4), new Position(8,11),
            new Position(9,4), new Position(9,11),
            new Position(10,4), new Position(10,11),
            new Position(11,4), new Position(11,11),
            new Position(11,5), new Position(11,6),
            new Position(11,9), new Position(11,10)
        };

        for (Position p : boxPositions) {
            map.getBlock(p).create();
        }
    }
}
*/
package domain;

/**
 * Nivel 1: Mapa 18x18 con bordes de hielo, frutas, trolls,
 * y ahora también un Narval y un Flowerpot.
 */
public class LevelOne {

    private static final int MAP_SIZE = 18;

    /**
     * Crea y configura el nivel 1.
     */
    public static BadIceCream createLevel() {

        GameMap map = new GameMap(MAP_SIZE, MAP_SIZE);
        createBorders(map);
        addBoxesCentro(map);

        BadIceCream game = new BadIceCream(map);

        // Jugador en el centro
        game.addPlayer(new IceCream(new Position(8, 8)));

        // Enemigos base (2 trolls lentos)
    
        //game.addEnemy(EnemyFactory.createEnemy(EnemyType.troll, new Position(1, 1), 0.05));
        game.addEnemy(new Troll(new Position(14, 14),0.0001));

        // -----------------------------------------
        //   NUEVOS ENEMIGOS
        // -----------------------------------------
/*
        // Flowerpot — deambula 6s y luego persigue 6s
        game.addEnemy(
            EnemyFactory.createEnemy(
                EnemyType.flowerpot,
                new Position(14, 3),
                0.3       // velocidad ligeramente mayor
            )
        );

        // Narval — patrulla y embiste en línea recta
        game.addEnemy(
            EnemyFactory.createEnemy(
                EnemyType.narval,
                new Position(14, 14),
                0.5       // velocidad rápida (embestida se sentirá)
            )
        );
*/
        // -----------------------------------------
        //   FRUTAS DEL NIVEL
        // -----------------------------------------

        addBananas(game);
        addGrapes(game);

        // Cereza — teletransporta
        Cherry cherry = new Cherry(new Position(3, 8));
        cherry.setGameMap(map);
        game.addFruit(cherry);

        // Piña — se mueve aleatoriamente
        Pineapple pineapple = new Pineapple(new Position(10, 10));
        pineapple.setGameMap(map);
        game.addFruit(pineapple);

        // Cactus — alterna entre seguro y peligroso
        Cactus cactus = new Cactus(new Position(8, 13));
        game.addFruit(cactus);

        // Iniciar comportamientos especiales
        game.initializeLevel();

        return game;
    }

    // --------------------------
    // CREACIÓN DE BORDES
    // --------------------------
    private static void createBorders(GameMap map) {
        for (int col = 0; col < MAP_SIZE; col++) {
            map.getBlock(new Position(0, col)).create();
            map.getBlock(new Position(MAP_SIZE - 1, col)).create();
        }
        for (int row = 0; row < MAP_SIZE; row++) {
            map.getBlock(new Position(row, 0)).create();
            map.getBlock(new Position(row, MAP_SIZE - 1)).create();
        }
    }

    // --------------------------
    // BANANAS
    // --------------------------
    private static void addBananas(BadIceCream game) {
        Position[] bananaPositions = {
            new Position(2, 4), new Position(2, 11),
            new Position(7, 2), new Position(7, 13),
            new Position(13, 4), new Position(13, 11),
            new Position(4, 8), new Position(11, 8)
        };

        for (Position pos : bananaPositions) {
            game.addFruit(new Banana(pos));
        }
    }

    // --------------------------
    // UVAS
    // --------------------------
    private static void addGrapes(BadIceCream game) {
        Position[] grapePositions = {
            new Position(4, 3), new Position(4, 12),
            new Position(8, 3), new Position(8, 12),
            new Position(11, 3), new Position(11, 12),
            new Position(6, 8), new Position(9, 8)
        };

        for (Position pos : grapePositions) {
            game.addFruit(new Grape(pos));
        }
    }

    // --------------------------
    // BLOQUES CENTRO
    // --------------------------
    private static void addBoxesCentro(GameMap map) {
        Position[] boxPositions = {
            new Position(4,4), new Position(4,5), new Position(4,6),
            new Position(4,9), new Position(4,10), new Position(4,11),
            new Position(5,4), new Position(5,11),
            new Position(6,4), new Position(6,11),
            new Position(7,4), new Position(7,11),
            new Position(8,4), new Position(8,11),
            new Position(9,4), new Position(9,11),
            new Position(10,4), new Position(10,11),
            new Position(11,4), new Position(11,11),
            new Position(11,5), new Position(11,6),
            new Position(11,9), new Position(11,10)
        };

        for (Position pos : boxPositions) {
            map.getBlock(pos).create();
        }
    }
}
