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

    private final List<IceCream> players;
    private final List<Enemy> enemies;
    private final List<Fruit> fruits;
    private final GameMap gameMap;
    private final List<ControllerCream> controllers;

    private int score;
    private boolean gameWon;
    private boolean gameLost;

    private final List<Position> initialPlayerPositions;
    private final List<Position> initialEnemyPositions;
    private final List<Position> initialFruitPositions;

    private boolean paused = false;

    private int currentWave = 0;
    private final List<Class<? extends Fruit>> fruitWaves = new ArrayList<>();

    private static final long MAX_TIME_MS = 180000;
    private long startTime;
    private long remainingTime;
    private long pauseStartTime;
    private boolean timeExpired = false;

    /**
     * Crea el controlador principal del juego utilizando un mapa específico.
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
     * Construye las oleadas de frutas a partir de los tipos registrados.
     */
    private void buildFruitWaves() {
        fruitWaves.clear();
        List<Class<? extends Fruit>> seen = new ArrayList<>();

        for (Fruit f : fruits) {
            Class<? extends Fruit> type = f.getClass();
            if (!seen.contains(type)) {
                seen.add(type);
                fruitWaves.add(type);
            }
        }
    }

    /**
     * Activa la oleada actual de frutas en el mapa.
     */
    private void spawnCurrentWave() {
        gameMap.removeAllFruits();
        if (currentWave >= fruitWaves.size()) return;

        Class<? extends Fruit> currentType = fruitWaves.get(currentWave);

        for (Fruit f : fruits) {
            if (f.getClass() == currentType && !f.isEaten()) {
                f.activate();
                gameMap.addFruit(f);
            } else {
                f.deactivate();
            }
        }
    }

    /**
     * Inicializa el juego después de que el mapa ha sido cargado.
     */
    public void initializeAfterMapLoad() {
        buildFruitWaves();
        currentWave = 0;
        startTime = System.currentTimeMillis();
        remainingTime = MAX_TIME_MS;

        if (!fruitWaves.isEmpty()) {
            spawnCurrentWave();
        }
    }

    /**
     * Guarda el estado inicial de los bloques del mapa.
     */
    public void initializeLevel() {
        gameMap.saveInitialBlockStates();
    }

    /**
     * Registra un controlador de entrada para el juego.
     * @param c controlador a agregar
     */
    public void addController(ControllerCream c) {
        controllers.add(c);
    }

    /**
     * Registra un jugador y almacena su posición inicial.
     * @param p jugador a registrar
     */
    public void addPlayer(IceCream p) {
        p.setGameMap(gameMap);
        players.add(p);
        initialPlayerPositions.add(new Position(
                p.getPosition().getRow(),
                p.getPosition().getCol())
        );
        gameMap.addPlayer(p);
        p.update();
    }

    /**
     * Registra un enemigo y almacena su posición inicial.
     * @param e enemigo a registrar
     */
    public void addEnemy(Enemy e) {
        e.setGameMap(gameMap);
        e.setGame(this);
        enemies.add(e);
        initialEnemyPositions.add(new Position(
                e.getPosition().getRow(),
                e.getPosition().getCol())
        );
        gameMap.addEnemy(e);
        e.update();
    }

    /**
     * Registra una fruta y guarda su posición inicial.
     * @param f fruta a registrar
     */
    public void addFruit(Fruit f) {
        fruits.add(f);
        initialFruitPositions.add(new Position(
                f.getPosition().getRow(),
                f.getPosition().getCol())
        );
        gameMap.addFruit(f);
        f.upd(gameMap);
    }

    /**
     * Intenta mover un jugador en una dirección determinada.
     * @param playerIndex índice del jugador
     * @param d dirección de movimiento
     * @return true si el movimiento fue exitoso
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
     * Hace que un jugador genere hielo frente a él.
     * @param playerIndex índice del jugador
     * @return cantidad de bloques creados
     */
    public int playerCreateIce(int playerIndex) {
        if (gameLost || gameWon) return 0;
        if (playerIndex < 0 || playerIndex >= players.size()) return 0;

        IceCream p = players.get(playerIndex);
        if (!p.isAlive()) return 0;

        return p.createIce(p.getFacingDirection());
    }

    /**
     * Hace que un jugador destruya hielo frente a él.
     * @param playerIndex índice del jugador
     * @return cantidad de bloques destruidos
     */
    public int playerDestroyIce(int playerIndex) {
        if (gameLost || gameWon) return 0;
        if (playerIndex < 0 || playerIndex >= players.size()) return 0;

        IceCream p = players.get(playerIndex);
        if (!p.isAlive()) return 0;

        return p.destroyIce(p.getFacingDirection());
    }

    /**
     * Finaliza la partida por tiempo agotado.
     */
    private void timeOver() {
        for (IceCream p : players) {
            p.die();
        }
        gameLost = true;
    }

    /**
     * Actualiza el estado completo del juego.
     */
    public void updateGame() {
        if (gameLost || gameWon) return;

        if (paused) return;

        long now = System.currentTimeMillis();
        remainingTime = MAX_TIME_MS - (now - startTime);

        if (remainingTime <= 0 && !timeExpired) {
            timeExpired = true;
            timeOver();
            return;
        }

        if (fruitWaves.isEmpty()) return;

        for (ControllerCream c : controllers) {
            c.update();
        }

        for (Enemy e : enemies) {
            if (e.usesAutoMovement()) {
                Direction d = e.getNextDirection();
                if (d != null) {
                    e.move(d);
                    e.update();
                }
            } else {
                e.update();
            }
        }

        for (Enemy e : enemies) {
            Position ePos = e.getPosition();

            for (IceCream p : players) {
                if (!p.isAlive()) continue;

                if (p.getPosition().equals(ePos)) {
                    p.die();
                }
            }
        }

        boolean allDead = true;
        for (IceCream ic : players) {
            if (ic.isAlive()) {
                allDead = false;
                break;
            }
        }

        if (allDead) {
            gameLost = true;
            return;
        }

        for (IceCream p : players) {
            if (p.isAlive()) {
                checkCollisionsFor(p);
                if (gameLost) return;
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
        totalScore();
        checkWinCondition();
    }

    /**
     * Procesa colisiones de un jugador con enemigos, frutas o bloques.
     * @param p jugador a evaluar
     */
    public void checkCollisionsFor(IceCream p) {
        Position pos = p.getPosition();

        if (gameMap.hasEnemy(pos)) {
            p.die();
        }

        Fruit f = gameMap.getFruit(pos);
        if (f != null && !f.isEaten()) {
            if (f.isDangerous()) {
                p.die();
            } else {
                f.eat();
                p.setScorePlayer(f.getScore());
                gameMap.removeFruit(pos);
                score += f.getScoreValue();
            }
        }

        Boxy b = gameMap.getBlock(pos);
        if (b != null && b.getType() == BoxType.bonfire && b.getState() == BoxState.on) {
            p.die();
        }

        boolean allDead = true;
        for (IceCream ic : players) {
            if (ic.isAlive()) {
                allDead = false;
                break;
            }
        }

        if (allDead) {
            gameLost = true;
        }
    }

    /**
     * Calcula la puntuación total de todos los jugadores.
     * @return puntuación total
     */
    public int totalScore() {
        int total = 0;
        for (IceCream c : players) {
            total += c.getScorePlayer();
        }
        return total;
    }

    /**
     * Verifica si la condición de victoria ha sido alcanzada.
     */
    private void checkWinCondition() {
        if (gameWon) return;

        Class<? extends Fruit> currentType = fruitWaves.get(currentWave);

        for (Fruit f : fruits) {
            if (f.getClass() == currentType && !f.isEaten()) {
                return;
            }
        }

        currentWave++;

        if (currentWave >= fruitWaves.size()) {
            gameWon = true;
            return;
        }

        spawnCurrentWave();
    }

    /**
     * Restaura el juego a su estado inicial.
     */
    public void resetGame() {
        score = 0;
        gameWon = false;
        gameLost = false;

        startTime = System.currentTimeMillis();
        remainingTime = MAX_TIME_MS;
        timeExpired = false;

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
    public List<IceCream> getPlayers() {
        return players;
    }

    /**
     * @return lista de enemigos
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * @return lista de frutas
     */
    public List<Fruit> getFruits() {
        return fruits;
    }

    /**
     * @return puntuación actual
     */
    public int getScore() {
        return score;
    }

    /**
     * @return mapa del juego
     */
    public GameMap getMap() {
        return gameMap;
    }

    /**
     * @return true si el juego fue ganado
     */
    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * @return true si el juego fue perdido
     */
    public boolean isGameLost() {
        return gameLost;
    }

    /**
     * Cambia el estado de pausa del juego.
     * @param p nuevo estado de pausa
     */
    public void setPaused(boolean p) {
        if (!paused && p) {
            pauseStartTime = System.currentTimeMillis();
        }
        if (paused && !p) {
            long pauseDuration = System.currentTimeMillis() - pauseStartTime;
            startTime += pauseDuration;
        }
        paused = p;
    }

    /**
     * @return true si el juego está en pausa
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * @return tiempo restante en milisegundos
     */
    public long getRemainingTimeMs() {
        return Math.max(0, remainingTime);
    }

    /**
     * @return tiempo total de juego transcurrido
     */
    public long getGameTime() {
        return System.currentTimeMillis() - startTime;
    }
}