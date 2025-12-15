package domain;

/**
 * Enemigo tipo Calamar Amarillo. Persigue al jugador más cercano,
 * esperando para romper bloques destructibles en su camino.
 */
public class YellowSquid extends Enemy {

    private enum State {
        CHASE,
        WAIT,
        BREAK
    }

    private State state;
    private int waitTicks;
    private static final int BREAK_DELAY = 2;
    private Position targetBlock;

    private int tick;
    private final int delay;

    /**
     * Crea un Calamar Amarillo en la posición indicada.
     * @param pos posición inicial del enemigo
     */
    public YellowSquid(Position pos) {
        super(EnemyType.yellowSquid, pos);
        this.state = State.CHASE;
        this.waitTicks = 0;
        this.targetBlock = null;
        this.currentDirection = Direction.DOWN;
        this.tick = 0;
        this.delay = 2;
    }

    /**
     * Asigna el juego al enemigo.
     * @param game instancia del juego
     */
    @Override
    public void setGame(BadIceCream game) {
    }

    /**
     * Indica si el enemigo usa movimiento automático.
     * @return false, ya que controla su propio movimiento
     */
    @Override
    protected boolean usesAutoMovement() {
        return false;
    }

    /**
     * Devuelve la dirección actual del enemigo.
     * @return dirección actual
     */
    @Override
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Actualiza el comportamiento del enemigo según su estado.
     */
    @Override
    public void doUpdate() {
        tick++;
        if (tick < delay) return;
        tick = 0;

        switch (state) {
            case CHASE -> chase();
            case WAIT -> waitBlock();
            case BREAK -> breakBlock();
        }
    }

    /**
     * Estado de persecución del jugador más cercano.
     */
    private void chase() {
        IceCream player = findPlayer();
        if (player == null || !player.isAlive()) return;

        Direction main = dirTo(player.getPosition());
        if (main != null && tryMove(main)) return;

        for (Direction d : altDirs(player.getPosition())) {
            if (tryMove(d)) return;
        }

        for (Direction d : Direction.values()) {
            if (tryMove(d)) return;
        }
    }

    /**
     * Intenta moverse en una dirección determinada.
     * @param dir dirección a intentar
     * @return true si realizó alguna acción
     */
    private boolean tryMove(Direction dir) {
        Position next = new Position(
                position.getRow() + dir.getRowDelta(),
                position.getCol() + dir.getColDelta()
        );

        if (!gameMap.isValid(next)) return false;
        if (gameMap.hasEnemy(next)) return false;
        Boxy block = gameMap.getBlock(next);

        if (block != null && block.isCreated() && block.canBeDestroyed()) {
            state = State.WAIT;
            waitTicks = 0;
            targetBlock = next;
            currentDirection = dir;
            return true;
        }

        if (gameMap.isBlocked(next)) return false;
        currentDirection = dir;
        move(dir);
        return true;
    }

    /**
     * Estado de espera antes de romper un bloque.
     */
    private void waitBlock() {
        waitTicks++;
        if (waitTicks >= BREAK_DELAY) {
            state = State.BREAK;
        }
    }

    /**
     * Estado de ruptura del bloque objetivo.
     */
    private void breakBlock() {
        if (targetBlock == null) {
            state = State.CHASE;
            return;
        }

        if (gameMap.isValid(targetBlock)) {
            Boxy block = gameMap.getBlock(targetBlock);
            if (block != null && block.canBeDestroyed()) {
                gameMap.clearBlock(targetBlock);
            }
        }
        targetBlock = null;
        waitTicks = 0;
        state = State.CHASE;
    }

    /**
     * Encuentra el jugador vivo más cercano.
     * @return jugador más cercano o null si no existe
     */
    private IceCream findPlayer() {
        IceCream closest = null;
        int minDist = Integer.MAX_VALUE;

        for (int r = 0; r < gameMap.getRows(); r++) {
            for (int c = 0; c < gameMap.getCols(); c++) {
                Position p = new Position(r, c);
                IceCream pl = gameMap.getPlayer(p);

                if (pl != null && pl.isAlive()) {
                    int d = dist(position, p);
                    if (d < minDist) {
                        minDist = d;
                        closest = pl;
                    }
                }
            }
        }
        return closest;
    }

    /**
     * Calcula la dirección principal hacia una posición objetivo.
     * @param target posición objetivo
     * @return dirección óptima o null si no hay movimiento
     */
    private Direction dirTo(Position target) {
        int dr = target.getRow() - position.getRow();
        int dc = target.getCol() - position.getCol();

        if (Math.abs(dr) > Math.abs(dc)) {
            return dr > 0 ? Direction.DOWN : Direction.UP;
        }
        if (dc != 0) {
            return dc > 0 ? Direction.RIGHT : Direction.LEFT;
        }
        return null;
    }

    /**
     * Calcula direcciones alternativas hacia el objetivo.
     * @param target posición objetivo
     * @return arreglo de direcciones alternativas
     */
    private Direction[] altDirs(Position target) {
        int dr = target.getRow() - position.getRow();
        int dc = target.getCol() - position.getCol();

        Direction v = dr > 0 ? Direction.DOWN : Direction.UP;
        Direction h = dc > 0 ? Direction.RIGHT : Direction.LEFT;

        return Math.abs(dr) > Math.abs(dc)
                ? new Direction[]{h, h.getOpposite(), v.getOpposite()}
                : new Direction[]{v, v.getOpposite(), h.getOpposite()};
    }

    /**
     * Calcula la distancia Manhattan entre dos posiciones.
     * @param a primera posición
     * @param b segunda posición
     * @return distancia Manhattan
     */
    private int dist(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }

    /**
     * Devuelve la clave del sprite según el estado actual.
     * @return clave del sprite
     */
    @Override
    public String getSpriteKey() {
        return switch (state) {
            case WAIT -> "yellowSquid_waiting";
            case BREAK -> "yellowSquid_breaking";
            default -> "yellowSquid_chasing";
        };
    }

    /**
     * Indica si el enemigo tiene animación activa.
     * @return true si está persiguiendo
     */
    @Override
    public boolean isAnimated() {
        return state == State.CHASE;
    }

    /**
     * Devuelve los ticks de espera acumulados.
     * @return cantidad de ticks de espera
     */
    public int getWaitTicks() {
        return waitTicks;
    }

    /**
     * Indica si el enemigo está esperando.
     * @return true si está en estado WAIT
     */
    public boolean isWaiting() {
        return state == State.WAIT;
    }

    /**
     * Indica si el enemigo está rompiendo un bloque.
     * @return true si está en estado BREAK
     */
    public boolean isBreaking() {
        return state == State.BREAK;
    }
}