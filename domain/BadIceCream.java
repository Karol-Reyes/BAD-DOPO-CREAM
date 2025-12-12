package domain;


import java.util.ArrayList;
import java.util.List;

/**
 * Controla el estado general del juego Bad Ice Cream, incluyendo jugadores,
 * enemigos, frutas, mapa, puntuación y condiciones de victoria o derrota.
 * Permite registrar entidades, actualizar el juego, procesar movimientos y
 * restaurar el nivel a su estado inicial.
 */
public class BadIceCream {
    
    private List<IceCream> players;
    private List<Enemy> enemies;
    private List<Fruit> fruits;
    private GameMap gameMap;
    private List<ControllerCream> controllers;

    private int score;
    private boolean gameWon;
    private boolean gameLost;
    
    private List<Position> initialPlayerPositions;
    private List<Position> initialEnemyPositions;
    private List<Position> initialFruitPositions;

    private boolean paused = false;
    
    /**
     * Crea un controlador de juego para un mapa dado.
     * @param map mapa donde se desarrollará la partida
     */
    public BadIceCream(GameMap map) {
        this.gameMap = map;
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.fruits = new ArrayList<>();
        this.controllers = new ArrayList<>();
        this.initialPlayerPositions = new ArrayList<>();
        this.initialEnemyPositions = new ArrayList<>();
        this.initialFruitPositions = new ArrayList<>();
        this.score = 0;
        this.gameWon = false;
        this.gameLost = false;
    }

    /**
     * Finaliza la configuración inicial del nivel guardando el estado de los bloques.
     * Debe llamarse después de registrar jugadores, enemigos y frutas.
     */
    public void initializeLevel() {
        gameMap.saveInitialBlockStates();
    }

    public void addController(ControllerCream c) {
        controllers.add(c);
    }

    /**
     * Registra un jugador en el juego y guarda su posición inicial.
     * @param p jugador a agregar
     */
    public void addPlayer(IceCream p) {
        p.setGameMap(gameMap);
        players.add(p);
        initialPlayerPositions.add(new Position(
            p.getPosition().getRow(),
            p.getPosition().getCol())
        );
        gameMap.addPlayer(p);

        try {
            p.update();
        } catch (Exception ex) {    
            System.err.println("addPlayer -> error inicializando player: " + ex);
        }
    }

    /**
     * Registra un enemigo en el juego y guarda su posición inicial.
     * @param e enemigo a agregar
     */
    public void addEnemy(Enemy e) {
        e.setGameMap(gameMap);
        enemies.add(e);
        initialEnemyPositions.add(new Position(
            e.getPosition().getRow(),
            e.getPosition().getCol())
        );
        gameMap.addEnemy(e);

        try {
            e.update();
        } catch (Exception ex) {    
            System.err.println("addEnemy -> error inicializando enemigo: " + ex);
        }
    }

    /**
     * Registra una fruta y guarda su ubicación inicial.
     * @param f fruta a agregar
     */
    public void addFruit(Fruit f) {
        fruits.add(f);
        initialFruitPositions.add(new Position(
            f.getPosition().getRow(),
            f.getPosition().getCol())
        );
        gameMap.addFruit(f);

        try {
            f.upd(gameMap); // esto asigna gameMap cuando la fruta lo requiera
        } catch (Exception ex) {    
            System.err.println("addFruit -> error inicializando fruta: " + ex);
        }
    }

    /**
     * Intenta mover un jugador en una dirección específica.
     * @param playerIndex índice del jugador
     * @param d dirección de movimiento
     * @return true si el jugador se movió, false en caso contrario
     */
    public boolean movePlayer(int playerIndex, Direction d) {
        if (gameLost || gameWon) return false;
        if (playerIndex < 0 || playerIndex >= players.size()) return false;

        IceCream p = players.get(playerIndex);
        if (!p.isAlive()) return false;

        boolean moved = p.move(d);
        
        if (moved) {
            checkCollisionsFor(p);
        }

        return moved;
    }

    /**
     * Hace que un jugador genere un bloque de hielo frente a él.
     * @param playerIndex índice del jugador
     * @return número de bloques creados (0 si no se creó nada)
     */
    public int playerCreateIce(int playerIndex) {
        if (gameLost || gameWon) return 0;
        if (playerIndex < 0 || playerIndex >= players.size()) return 0;

        IceCream p = players.get(playerIndex);
        if (!p.isAlive()) return 0;

        return p.createIce(p.getFacingDirection());
    }

    /**
     * Hace que un jugador destruya un bloque de hielo frente a él.
     * @param playerIndex índice del jugador
     * @return número de bloques destruidos
     */
    public int playerDestroyIce(int playerIndex) {
        if (gameLost || gameWon) return 0;
        if (playerIndex < 0 || playerIndex >= players.size()) return 0;

        IceCream p = players.get(playerIndex);
        if (!p.isAlive()) return 0;

        return p.destroyIce(p.getFacingDirection());
    }

    /**
     * Actualiza el estado del juego moviendo enemigos,
     * procesando colisiones y comprobando condiciones de victoria.
     */
    public void updateGame() {
        if (gameLost || gameWon) return;

        for (ControllerCream c : controllers) {
            c.update();
        }

        for (Enemy e : enemies) {
            Direction d = e.getNextDirection();
            if (d != null) {
                e.move(d);
                e.update();
            }
        }

        for (Fruit f: fruits) {
            if (f != null) {
                try {
                    f.upd(gameMap);
                } catch (Exception ex) {
                    System.err.println("Error actualizando fruta " + f + ": " + ex);
                }
            }
        }

        for (int r = 0; r < gameMap.getRows(); r++) {
            for (int c = 0; c < gameMap.getCols(); c++) {
                Boxy bloque = gameMap.getBlock(new Position(r, c));
                if (bloque != null && bloque.getType() == BoxType.bonfire) {
                    ((Bonfire) bloque).update(); // Aquí SÍ usamos casting pero es inevitable
                }
            }
        }

        for (IceCream p : players) {
            if (p.isAlive()) {
                p.update();
                checkCollisionsFor(p);
            }
        }
        
        checkWinCondition();
    }

    /**
     * Procesa colisiones de un jugador con enemigos o frutas.
     * @param p jugador a verificar
     */
    public void checkCollisionsFor(IceCream p) {
        Position pos = p.getPosition();
        if (gameMap.hasEnemy(pos)) {
            p.die();
            gameLost = true;
        }

        boolean todosMuertos = true;
    
        for (IceCream ic : players) {
            if (ic.isAlive()) {
                todosMuertos = false;
                break; // Si uno sigue vivo, no se perdió la partida
            }
        }
    
        // Si todos los jugadores están muertos, entonces sí se pierde el juego
        if (todosMuertos) {
            gameLost = true;
        } else {
            gameLost = false; // Si al menos uno vive, no es game over
        }

        // Recolecta fruta o colisión con fruta peligrosa
        Fruit f = gameMap.getFruit(pos);
        if (f != null && !f.isEaten()) {
            if (f.isDangerous()) {
                p.die();
                gameLost = true;
                return;
            }
            f.eat();
            gameMap.removeFruit(pos);
            score += f.getScoreValue();
        }
        
        Boxy b = gameMap.getBlock(pos);
        if (b == null) return;
        if (b.getType() == BoxType.bonfire && b.getState() == BoxState.on) {
            p.die();
            gameLost = true;
            return;
        }
    }

    /**
     * Verifica si todas las frutas han sido recogidas para determinar si se ganó el nivel.
     */
    private void checkWinCondition() {
        for (Fruit f : fruits) {
            if (!f.isEaten()) return;
        }
        gameWon = true;
    }

    /**
     * Restaura el nivel a su estado original: posiciones, frutas, enemigos y bloques.
     */
    public void resetGame() {
        score = 0;
        gameWon = false;
        gameLost = false;

        gameMap.clearEntities();
        gameMap.resetBlocks();

        for (int i = 0; i < players.size(); i++) {
            IceCream p = players.get(i);
            Position ini = initialPlayerPositions.get(i);
            p.setPosition(new Position(ini.getRow(), ini.getCol()));
            p.alive();
            gameMap.addPlayer(p);
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            Position ini = initialEnemyPositions.get(i);
            e.setPosition(new Position(ini.getRow(), ini.getCol()));
            gameMap.addEnemy(e);
        }

        for (int i = 0; i < fruits.size(); i++) {
            Fruit f = fruits.get(i);
            Position ini = initialFruitPositions.get(i);
            f.setPosition(new Position(ini.getRow(), ini.getCol()));
            f.reset();
            gameMap.addFruit(f);
        }
    }

    /**
     * @return lista de jugadores
     */
    public List<IceCream> getPlayers() { return players; }

    /**
     * @return lista de enemigos
     */
    public List<Enemy> getEnemies() { return enemies; }

    /**
     * @return lista de frutas
     */
    public List<Fruit> getFruits() { return fruits; }

    /**
     * @return puntuación actual
     */
    public int getScore() { return score; }

    /**
     * @return mapa del juego
     */
    public GameMap getMap() { return gameMap; }

    /**
     * @return true si el juego ha sido ganado
     */
    public boolean isGameWon() { return gameWon; }

    /**
     * @return true si el jugador ha perdido
     */
    public boolean isGameLost() { return gameLost; }

    /**
     * Cambia el estado de pausa del juego
     */
    public void setPaused(boolean p) { paused = p; }
    
    public boolean isPaused() { return paused; }
}
