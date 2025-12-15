package domain;

/**
 * Representa una fruta tipo cereza dentro del juego.
 * La cereza se teletransporta a una posición aleatoria cada 20 segundos.
 * Otorga 150 puntos al ser recolectada.
 */
public class Cherry extends Fruit {

    private static final int TELEPORT_INTERVAL = 20000;
    private long lastTeleportTime;
    private GameMap gameMap;

    /**
     * Crea una cereza en una posición específica del mapa.
     * @param position posición inicial de la cereza
     */
    public Cherry(Position position) {
        super(FruitType.cherry, position, 0, false);
        this.lastTeleportTime = System.currentTimeMillis();
        this.scoreValue = 150;
    }

    /**
     * Obtiene la puntuación que otorga la cereza al ser recolectada.
     * @return valor de puntuación de la cereza
     */
    @Override
    public int getScore() {
        return scoreValue;
    }

    /**
     * Establece el mapa de juego donde se encuentra la cereza.
     * @param map mapa donde se encuentra la cereza
     */
    public void setGameMap(GameMap map) {
        this.gameMap = map;
    }

    /**
     * Actualiza el estado de la cereza y gestiona su teletransportación
     * a una posición aleatoria cada 20 segundos.
     */
    @Override
    public void update() {
        if (!active) return;
        if (state == FruitState.eaten || gameMap == null) return;
        if (frozen) return;

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastTeleportTime >= TELEPORT_INTERVAL) {
            teleport();
            lastTeleportTime = currentTime;
        }
    }

    /**
     * Actualiza el estado de la cereza usando un mapa específico.
     * @param map mapa donde se encuentra la cereza
     */
    @Override
    public void upd(GameMap map) {
        this.gameMap = map;
        update();
    }

    /**
     * Teletransporta la cereza a una posición aleatoria libre en el mapa.
     */
    private void teleport() {
        state = FruitState.teleporting;

        Position newPos = findRandomFreePosition();
        if (newPos != null) {
            gameMap.removeFruit(this.position);
            this.position = newPos;
            gameMap.addFruit(this);
        }

        state = FruitState.active;
    }

    /**
     * Encuentra una posición libre aleatoria en el mapa.
     * @return posición libre aleatoria en el mapa, o null si no se encuentra ninguna
     */
    private Position findRandomFreePosition() {
        int rows = gameMap.getRows();
        int cols = gameMap.getCols();
        int maxAttempts = 100;

        for (int i = 0; i < maxAttempts; i++) {
            int r = 1 + (int)(Math.random() * (rows - 2));
            int c = 1 + (int)(Math.random() * (cols - 2));
            Position pos = new Position(r, c);

            if (!gameMap.isBlocked(pos) &&
                !gameMap.hasEnemy(pos) &&
                gameMap.getFruit(pos) == null &&
                gameMap.getPlayer(pos) == null) {
                return pos;
            }
        }
        return null;
    }

    /**
     * Obtiene la clave del sprite correspondiente al estado actual de la cereza.
     * @return clave del sprite de la cereza, dependiendo de su estado
     */
    @Override
    public String getSpriteKey() {
        if (state == FruitState.teleporting) {
            return "cherry_teleporting";
        }
        return "cherry";
    }
}
