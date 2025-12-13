package domain;

/**
 * Cereza: Se teletransporta a una posición aleatoria cada 20 segundos.
 * Otorga 150 puntos.
 */
public class Cherry extends Fruit {
    
    private static final int TELEPORT_INTERVAL = 20000; // 20 segundos
    private long lastTeleportTime;
    private GameMap gameMap;
    
    public Cherry(Position position) {
        super(FruitType.cherry, position, 0, false);
        this.lastTeleportTime = System.currentTimeMillis();
        this.scoreValue = 150;
    }
    
    public int getScore() {
        return scoreValue;
    }

    public void setGameMap(GameMap map) {
        this.gameMap = map;
    }
    
    @Override
    public void update() {
        if (!active) return;
        if (state == FruitState.eaten || gameMap == null) return;
        if (frozen) return;

        long currentTime = System.currentTimeMillis();
        
        // Si pasaron 20 segundos, teletransportarse
        if (currentTime - lastTeleportTime >= TELEPORT_INTERVAL) {
            teleport();
            lastTeleportTime = currentTime;
        }
    }
    
    @Override
    public void upd(GameMap map) {
        this.gameMap = map;
        update();
    }
    
    /**
     * Teletransporta la cereza a una posición aleatoria libre.
     */
    private void teleport() {
        state = FruitState.teleporting;
        
        Position newPos = findRandomFreePosition();
        if (newPos != null) {
            // Remover de posición actual
            gameMap.removeFruit(this.position);
            
            // Mover a nueva posición
            this.position = newPos;
            gameMap.addFruit(this);
        }
        
        state = FruitState.active;
    }
    
    /**
     * Encuentra una posición libre aleatoria en el mapa.
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
    
    @Override
    public String getSpriteKey() {
        if (state == FruitState.teleporting) {
            return "cherry_teleporting"; // Sprite especial durante teletransporte
        }
        return "cherry";
    }
}