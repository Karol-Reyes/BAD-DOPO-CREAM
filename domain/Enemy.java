package domain;

/**
 * Representa un enemigo genérico del juego.
 * Define la lógica base de movimiento, colisión y estado.
 */
public abstract class Enemy implements SpriteProvider {

    protected EnemyType type;
    protected Position position;
    protected boolean trapped;
    protected GameMap gameMap;
    protected Direction currentDirection;

    /**
     * Crea un enemigo con un tipo y una posición inicial.
     * @param type tipo de enemigo
     * @param position posición inicial del enemigo
     */
    public Enemy(EnemyType type, Position position) {
        this.type = type;
        this.position = position;
        this.trapped = false;
        this.currentDirection = Direction.DOWN;
    }

    /**
     * Obtiene el tipo del enemigo.
     * @return tipo de enemigo
     */
    public EnemyType getType() {
        return type;
    }

    /**
     * Obtiene la posición actual del enemigo.
     * @return posición del enemigo
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Establece una nueva posición para el enemigo.
     * @param position nueva posición a asignar
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Asigna el mapa de juego donde se mueve el enemigo.
     * @param gameMap mapa del juego
     */
    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    /**
     * Obtiene la dirección actual de movimiento del enemigo.
     * @return dirección actual
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Indica si el enemigo se encuentra atrapado.
     * @return true si está atrapado
     */
    public boolean isTrapped() {
        return trapped;
    }

    /**
     * Actualiza el estado del enemigo.
     */
    public void update() {
        doUpdate();
    }

    /**
     * Ejecuta la lógica específica de actualización del enemigo.
     */
    protected abstract void doUpdate();

    /**
     * Verifica si el enemigo colisiona con una posición dada.
     * @param otherPosition posición a comparar
     * @return true si colisiona con dicha posición
     */
    public boolean collidesWith(Position otherPosition) {
        return position.equals(otherPosition);
    }

    /**
     * Verifica si el enemigo puede moverse a una posición determinada.
     * @param pos posición destino
     * @return true si el movimiento es válido
     */
    public boolean canMove(Position pos) {
        return gameMap.isValid(pos)
                && !gameMap.isBlocked(pos)
                && gameMap.getEnemy(pos) == null;
    }

    /**
     * Verifica si el enemigo puede moverse en una dirección específica.
     * @param d dirección a evaluar
     * @return true si puede moverse en esa dirección
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
     * Determina la siguiente dirección válida de movimiento del enemigo
     * según prioridades internas.
     * @return siguiente dirección válida o null si no hay movimientos posibles
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
     * Obtiene las direcciones prioritarias según la dirección actual.
     * @param current dirección actual
     * @return arreglo de direcciones prioritarias
     */
    private Direction[] getPriorityDirections(Direction current) {
        return switch (current) {
            case UP, DOWN -> new Direction[]{Direction.LEFT, Direction.RIGHT};
            case LEFT, RIGHT -> new Direction[]{Direction.UP, Direction.DOWN};
            default -> new Direction[]{};
        };
    }

    /**
     * Mueve al enemigo en una dirección específica y actualiza el mapa.
     * @param direction dirección de movimiento
     */
    public void move(Direction direction) {
        if (moveInDirection(direction)) {
            gameMap.moveEnemy(this, direction);
            this.currentDirection = direction;
        }
    }

    /**
     * Mueve al enemigo hacia arriba.
     * @return nueva fila del enemigo
     */
    public int moveUp() {
        move(Direction.UP);
        return position.getRow();
    }

    /**
     * Mueve al enemigo hacia abajo.
     * @return nueva fila del enemigo
     */
    public int moveDown() {
        move(Direction.DOWN);
        return position.getRow();
    }

    /**
     * Mueve al enemigo hacia la izquierda.
     * @return nueva columna del enemigo
     */
    public int moveLeft() {
        move(Direction.LEFT);
        return position.getCol();
    }

    /**
     * Mueve al enemigo hacia la derecha.
     * @return nueva columna del enemigo
     */
    public int moveRight() {
        move(Direction.RIGHT);
        return position.getCol();
    }

    /**
     * Asigna la instancia principal del juego al enemigo.
     * @param game instancia del juego
     */
    public void setGame(BadIceCream game) {
    }

    /**
     * Indica si el enemigo utiliza movimiento automático.
     * @return true si usa movimiento automático
     */
    protected boolean usesAutoMovement() {
        return true;
    }
}