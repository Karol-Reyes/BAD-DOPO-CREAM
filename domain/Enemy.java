package domain;

/**
 * Enemigo base con lógica de movimiento, colisión y estado.
 */
public abstract class Enemy implements SpriteProvider {

    protected EnemyType type;
    protected Position position;
    protected double speed;
    protected boolean trapped;
    protected GameMap gameMap;
    protected Direction currentDirection;
    //protected int skipCounter = 0;
    private double speedAccumulator = 0;

    /**
     * Inicializa un enemigo con tipo, posición y velocidad.
     * @param type     tipo de enemigo
     * @param position posición inicial
     * @param speed    velocidad de movimiento
     */
    public Enemy(EnemyType type, Position position, double speed) {
        this.type = type;
        this.position = position;
        this.speed = speed;
        this.trapped = false;
        this.currentDirection = Direction.DOWN;
    }

    /** Retorna el tipo de enemigo. */
    public EnemyType getType() { return type; }

    /** Retorna la posición actual del enemigo. */
    public Position getPosition() { return position; }

    /**
     * Actualiza la posición del enemigo.
     * @param position nueva posición
     */
    public void setPosition(Position position) { this.position = position; }

    /** Asigna el mapa donde se mueve el enemigo. */
    public void setGameMap(GameMap gameMap) { this.gameMap = gameMap; }

    /** Retorna la velocidad del enemigo. */
    public double getSpeed() { return speed; }

    /** Retorna la dirección actual del enemigo. */
    public Direction getCurrentDirection() { return currentDirection; }

    /** Indica si el enemigo está atrapado. */
    public boolean isTrapped() { return trapped; }

    public void update() {

        speedAccumulator += speed;
        
        if (speedAccumulator >= 1.0) {
            speedAccumulator -= 1.0;
            doUpdate();
        }
    }

    protected abstract void doUpdate();
    
    /**
     * Verifica colisión con otra posición.
     * @param otherPosition posición a comparar
     */
    public boolean collidesWith(Position otherPosition) {
        return position.equals(otherPosition);
    }

    /**
     * Verifica si el enemigo puede moverse a una posición.
     * @param pos posición destino
     */
    public boolean canMove(Position pos) {
        if (!gameMap.isValid(pos)) return false;
        if (gameMap.isBlocked(pos)) return false;
        if (gameMap.getEnemy(pos) != null) return false;
        return true;
    }

    /**
     * Verifica si el enemigo puede moverse en una dirección dada.
     * @param d dirección a evaluar
     */
    protected boolean moveInDirection(Direction d) {
        Position oldPos = position;
        Position newPos = new Position(
            oldPos.getRow() + d.getRowDelta(),
            oldPos.getCol() + d.getColDelta()
        );
        return canMove(newPos);
    }

    /**
     * Determina la siguiente dirección según prioridad.
     * Intenta mantener la dirección actual, luego alternativas.
     * @return siguiente dirección válida o null
     */
    public Direction getNextDirection() {
        if (moveInDirection(currentDirection)) {
            return currentDirection;
        }

        Direction[] priorityDirections = getPriorityDirections(currentDirection);
        for (Direction d : priorityDirections) {
            if (moveInDirection(d)) {
                return d;
            }
        }

        for (Direction d : Direction.values()) {
            if (moveInDirection(d)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Devuelve direcciones prioritarias según la dirección actual.
     * @param current dirección actual
     */
    private Direction[] getPriorityDirections(Direction current) {
        switch (current) {
            case UP: case DOWN: return new Direction[]{Direction.LEFT, Direction.RIGHT};
            case LEFT: case RIGHT: return new Direction[]{Direction.UP, Direction.DOWN};
            default: return new Direction[]{};
        }
    }

    /**
     * Mueve al enemigo en una dirección, actualizando el mapa.
     * @param direction dirección de movimiento
     */
    public void move(Direction direction) {
        if (moveInDirection(direction)) {
            gameMap.moveEnemy(this, direction);
            this.currentDirection = direction;
        }
    }

    /** Mueve al enemigo hacia arriba. */
    public int moveUp() {
        move(Direction.UP);
        return position.getRow();
    }

    /** Mueve al enemigo hacia abajo. */
    public int moveDown() {
        move(Direction.DOWN);
        return position.getRow();
    }

    /** Mueve al enemigo hacia la izquierda. */
    public int moveLeft() {
        move(Direction.LEFT);
        return position.getCol();
    }

    /** Mueve al enemigo hacia la derecha. */
    public int moveRight() {
        move(Direction.RIGHT);
        return position.getCol();
    }
}