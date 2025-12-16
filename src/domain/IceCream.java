package domain;

/**
 * Representa al jugador principal del juego, un helado.
 */
public class IceCream implements SpriteProvider {

    private Position pos;
    private boolean alive;
    private ControllerCream ctrl;
    private GameMap map;
    private Direction dir;
    private boolean moving;
    private int score;
    private String flavor;

    /**
     * Crea un jugador en una posición inicial.
     * @param pos posición inicial
     */
    public IceCream(Position pos) {
        this.pos = pos;
        this.alive = true;
        this.score = 0;
        this.dir = Direction.DOWN;
        this.moving = false;
    }

    /**
     * Retorna el puntaje actual del jugador.
     * @return puntaje acumulado
     */
    public int getScore() {
        return score;
    }

    /**
     * Suma puntaje al jugador.
     * @param value valor a agregar
     */
    public void addScore(int value) {
        this.score += value;
    }

    /**
     * Asigna el controlador de comportamiento del jugador.
     * @param ctrl controlador a usar
     */
    public void setController(ControllerCream ctrl) {
        this.ctrl = ctrl;
    }

    /**
     * Retorna el controlador asignado.
     * @return controlador actual
     */
    public ControllerCream getController() {
        return ctrl;
    }

    /** Asocia el jugador con el mapa donde se mueve. */
    public void setGameMap(GameMap gameMap) { 
        this.map = gameMap; 
    }

    /**
     * Actualiza el comportamiento del jugador.
     */
    public void update() {
        if (ctrl != null && alive) {
            ctrl.update();
        }
    }

    /**
     * Asocia el jugador con el mapa del juego.
     * @param map mapa del juego
     */
    public void setMap(GameMap map) {
        this.map = map;
    }

    /**
     * Retorna la posición actual del jugador.
     * @return posición actual
     */
    public Position getPosition() {
        return pos;
    }

    /**
     * Actualiza la posición del jugador.
     * @param pos nueva posición
     */
    public void setPosition(Position pos) {
        this.pos = pos;
    }

    /**
     * Indica si el jugador está vivo.
     * @return true si está vivo
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Marca al jugador como vivo.
     */
    public void revive() {
        alive = true;
    }

    /**
     * Marca al jugador como muerto.
     */
    public void die() {
        alive = false;
    }

    /**
     * Retorna la dirección actual del jugador.
     * @return dirección actual
     */
    public Direction getDir() {
        return dir;
    }

    /**
     * Cambia la dirección del jugador.
     * @param dir nueva dirección
     */
    public void setDir(Direction dir) {
        this.dir = dir;
    }

    /**
     * Define si el jugador está en movimiento.
     * @param moving estado de movimiento
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     * Indica si el jugador se está moviendo.
     * @return true si se mueve
     */
    public boolean isMoving() {
        return moving;
    }

    /**
     * Intenta mover al jugador en una dirección.
     * @param d dirección de movimiento
     * @return true si se movió
     */
    public boolean move(Direction d) {
        if (map == null || !alive) return false;
        this.dir = d;
        return map.movePlayer(this, d);
    }

    /**
     * Mueve al jugador hacia arriba.
     * @return fila actual
     */
    public int moveUp() {
        move(Direction.UP);
        return pos.getRow();
    }

    /**
     * Mueve al jugador hacia abajo.
     * @return fila actual
     */
    public int moveDown() {
        move(Direction.DOWN);
        return pos.getRow();
    }

    /**
     * Mueve al jugador hacia la izquierda.
     * @return columna actual
     */
    public int moveLeft() {
        move(Direction.LEFT);
        return pos.getCol();
    }

    /**
     * Mueve al jugador hacia la derecha.
     * @return columna actual
     */
    public int moveRight() {
        move(Direction.RIGHT);
        return pos.getCol();
    }

    /**
     * Crea hielo en la dirección indicada.
     * @param d dirección de creación
     * @return cantidad de bloques creados
     */
    public int createIce(Direction d) {
        if (map == null) return 0;

        int count = 0;
        Position p = pos;

        while (true) {
            Position next = new Position(
                p.getRow() + d.getRowDelta(),
                p.getCol() + d.getColDelta()
            );

            if (!map.isValid(next)) break;
            if (map.hasEnemy(next) || map.hasPlayer(next)) break;

            Boxy b = map.getBlock(next);

            if (b != null && b.getType() == BoxType.fire) {
                p = next;
                continue;
            }

            if (b != null && b.getType() == BoxType.bonfire && b.getState() == BoxState.on) {
                map.setBlock(next, new Ice(next, BoxState.created, b));
                b.onFreeze();
                count++;
                p = next;
                continue;
            }

            if ((b != null && b.isCreated()) || b.getType() == BoxType.iron) break;

            if (b == null || b.getType() == BoxType.floor || b.canBeCreated() ) {
                if (map.hasFruit(next)) {
                    map.getFruit(next).freeze();
                }
                map.setBlock(next, new Ice(next, BoxState.created));
                count++;
            } else {
                break;
            }

            p = next;
        }

        return count;
    }

    /**
     * Destruye hielo en la dirección indicada.
     * @param d dirección de destrucción
     * @return cantidad de bloques destruidos
     */
    public int destroyIce(Direction d) {
        if (map == null) return 0;

        int count = 0;
        Position p = pos;

        while (true) {
            Position next = new Position(
                p.getRow() + d.getRowDelta(),
                p.getCol() + d.getColDelta()
            );

            if (!map.isValid(next)) break;

            Boxy b = map.getBlock(next);
            if (b == null || !b.canBeDestroyed() || b.getType() == BoxType.iron) break;

            if (map.hasFruit(next)) {
                map.getFruit(next).unfreeze();
            }

            b.onDestroy(map);
            map.clearBlock(next);
            count++;
            p = next;
        }

        return count;
    }

    /**
     * Retorna la clave del sprite del jugador.
     * @return clave del sprite
     */
    @Override
    public String getSpriteKey() {
        return "player_" + dir.name().toLowerCase();
    }

    /**
     * Indica si el sprite debe animarse.
     * @return true si está animado
     */
    @Override
    public boolean isAnimated() {
        return moving;
    }

    /**
     * Retorna el sabor del jugador.
     * @return sabor actual
     */
    public String getFlavor() {
        return flavor;
    }

    /**
     * Asigna el sabor del jugador.
     * @param flavor nuevo sabor
     */
    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    /** 
     * @return dirección hacia donde mira el jugador. 
     */
    public Direction getFacingDirection() { 
        return dir; 
    }

    /**
     * @return puntaje del jugador
     */
    public int getScorePlayer() { 
        return score; 
    } 
    
    /**
     * Sets el puntaje del jugador.
     * @param s puntaje a asignar
     */
    public void setScorePlayer (int s) { 
        this.score += s; 
    }

    /** Marca al jugador como vivo. */ 
    public void alive() { 
        this.alive = true; 
    }
}