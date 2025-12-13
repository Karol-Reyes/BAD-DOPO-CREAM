package domain;

/**
 * Piña: Se mueve intentando alejarse del jugador más cercano.
 * Otorga 200 puntos.
 */
public class Pineapple extends Fruit {
    
    private GameMap gameMap;
    
    public Pineapple(Position position) {
        super(FruitType.pineapple, position, 1, false);
        this.scoreValue = 200;
    }

    public int getScore() {
        return scoreValue;
    }
    
    /**
     * Establece la referencia al mapa del juego.
     */
    public void setGameMap(GameMap map) {
        this.gameMap = map;
    }
    
    @Override
    public void upd(GameMap map) {
        this.gameMap = map;
        update();
    }
    
    @Override
    public void update() {
        if (!active) return;
        if (isEaten() || gameMap == null) return;

        if (frozen) return;
        
        Direction[] directions = Direction.values();
        Direction randomDir = directions[(int)(Math.random() * directions.length)];
        
        tryMove(randomDir);
    }
    
    /**
     * Intenta moverse en una dirección específica.
     */
    private void tryMove(Direction d) {
        Position newPos = new Position(
            position.getRow() + d.getRowDelta(),
            position.getCol() + d.getColDelta()
        );
        
        if (gameMap.isValid(newPos) && 
            !gameMap.isBlocked(newPos) &&
            !gameMap.hasEnemy(newPos) &&
            gameMap.getFruit(newPos) == null) {
            
            // Remover de posición actual
            gameMap.removeFruit(this.position);
            
            // Mover a nueva posición
            this.position = newPos;
            gameMap.addFruit(this);
        }
    }
}