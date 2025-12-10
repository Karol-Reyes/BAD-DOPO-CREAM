package domain;

/**
 * Representa al jugador principal del juego. Gestiona su posición, movimiento,
 * dirección, estado y la interacción con el mapa (creación y destrucción de hielo).
 */
public class IceCream implements SpriteProvider {

    private Position position;
    private int speed;
    private boolean alive;
    private ControllerCream controller;

    private GameMap gameMap;
    private Direction facingDirection;
    private boolean moving;
    
    /** Construye un IceCream en la posición dada. */
    public IceCream(Position position) {
        this.position = position;
        this.speed = 1;
        this.alive = true;
        this.facingDirection = Direction.DOWN;
        this.moving = false;
    }

    public void setController(ControllerCream controller) {
        this.controller = controller;
    }

    public ControllerCream getController() { 
        return this.controller; 
    }

    public void update() {
        if (controller != null && alive) {
            controller.update();
        }
    }
    
    /** Asocia el jugador con el mapa donde se mueve. */
    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    /** @return posición actual del jugador */
    public Position getPosition() { return position; }
    /** Establece una nueva posición. */
    public void setPosition(Position position) { this.position = position; }

    /** @return true si el jugador está vivo. */
    public boolean isAlive() { return alive; }
    /** Marca al jugador como vivo. */
    public void alive() { this.alive = true; }
    /** Marca al jugador como muerto. */
    public void die() { this.alive = false; }

    /** @return dirección hacia donde mira el jugador. */
    public Direction getFacingDirection() { return facingDirection; }
    /** Cambia la dirección del jugador. */
    public void setFacingDirection(Direction direction) { this.facingDirection = direction; }

    /** Cambia el estado de movimiento. */
    public void setMoving(boolean moving) { this.moving = moving; }
    /** @return true si el jugador está avanzando. */
    public boolean isMoving() { return moving; }
    
    /**
     * Intenta mover al jugador en la dirección indicada.
     * @return true si el movimiento fue exitoso.
     */
    public boolean move(Direction d) {
        if (gameMap == null || !alive) return false;
        this.facingDirection = d;
        return gameMap.movePlayer(this, d);
    }

    /** Mueve al jugador hacia arriba y devuelve la nueva fila. */
    public int moveUp() {
        move(Direction.UP);
        return position.getRow();
    }

    /** Mueve al jugador hacia abajo y devuelve la nueva fila. */
    public int moveDown() {
        move(Direction.DOWN);
        return position.getRow();
    }

    /** Mueve al jugador hacia la izquierda y devuelve la nueva columna. */
    public int moveLeft() {
        move(Direction.LEFT);
        return position.getCol();
    }

    /** Mueve al jugador hacia la derecha y devuelve la nueva columna. */
    public int moveRight() {
        move(Direction.RIGHT);
        return position.getCol();
    }

    /**
     * Crea bloques de hielo avanzando en la dirección dada.
     * @return cantidad de bloques creados.
     */
    public int createIce(Direction d) {
        if (gameMap == null) return 0;

        int count = 0;
        Position pos = this.position;

        while (true) {
            Position next = new Position(
                pos.getRow() + d.getRowDelta(),
                pos.getCol() + d.getColDelta()
            );

            // 1) Fuera del mapa -> parar
            if (!gameMap.isValid(next)) break;

            // 2) Si hay enemigo o jugador -> parar (no crear en esa celda)
            if (gameMap.hasEnemy(next) || gameMap.hasPlayer(next)) break;

            // 3) obtener el bloque actual (puede ser null o Floor u otro)
            Boxy b = gameMap.getBlock(next);

            // 4) Si ya hay un bloque creado (hielo ya creado) -> parar
            if (b != null && b.isCreated()) break;

            // 5) Si es celda vacía o suelo -> crear hielo
            if (b == null || b.getType() == BoxType.floor) {
                // si hay fruta, la removemos para que el hielo ocupe la celda
                if (gameMap.hasFruit(next)) {
                    gameMap.removeFruit(next);
                }
                // poner Ice explícitamente
                gameMap.setBlock(next, new Ice(next, BoxState.created));
                count++;
            }
            // 6) Si hay un bloque que puede convertirse -> convertirlo en hielo
            else if (b.canBeCreated()) {
                if (gameMap.hasFruit(next)) {
                    gameMap.removeFruit(next);
                }
                gameMap.setBlock(next, new Ice(next, BoxState.created));
                count++;
            }
            // 7) Bloque no convertible (iron / indestructible u otro) -> parar
            else {
                break;
            }

            // 8) avanza la referencia y sigue en línea
            pos = next;
        }

        return count;
    }



    /**
     * Destruye bloques de hielo en la dirección indicada.
     * @return cantidad de bloques destruidos.
     */
    public int destroyIce(Direction d) {
        if (gameMap == null) return 0;

        int count = 0;
        Position pos = this.position;

        while (true) {
            Position next = new Position(
                pos.getRow() + d.getRowDelta(),
                pos.getCol() + d.getColDelta()
            );
            if (!gameMap.isValid(next)) break;
            Boxy b = gameMap.getBlock(next);

            if (b == null || !b.canBeDestroyed()) break;
            gameMap.clearBlock(next);
            count++;
            pos = next;
        }
        return count;
    }

    /** @return clave del sprite según la dirección. */
    @Override
    public String getSpriteKey() {
         return "player_" + facingDirection.name().toLowerCase();
    }

    /** @return true si el sprite debe animarse. */
    @Override
    public boolean isAnimated() {
        return moving;
    }
}