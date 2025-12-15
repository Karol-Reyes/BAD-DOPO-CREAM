package domain;
import java.util.Random;

/**
 * Representa un enemigo Flowerpot dentro del mapa del juego.
 * El Flowerpot alterna entre movimiento aleatorio y persecución del jugador.
 */
public class Flowerpot extends Enemy {

    private static final long MODE_DURATION = 6000;

    private boolean chasingMode;
    private long modeTimer;
    @SuppressWarnings("unused")
    private BadIceCream game;

    private int tick = 2;
    private int speed = 2;

    private final Random random;
    private Direction flowerpotDirection;

    /**
     * Crea un enemigo Flowerpot en una posición inicial.
     * @param position posición inicial del enemigo
     */
    public Flowerpot(Position position) {
        super(EnemyType.flowerpot, position);
        this.random = new Random();
        this.flowerpotDirection = Direction.UP;
        this.modeTimer = System.currentTimeMillis();
    }

    /**
     * Asigna la instancia del juego para acceder a jugadores y mapa.
     * @param game instancia principal del juego
     */
    @Override
    public void setGame(BadIceCream game) {
        this.game = game;
    }

    /**
     * Indica si el enemigo utiliza el movimiento automático por defecto.
     * @return false, ya que maneja su propio movimiento
     */
    @Override
    protected boolean usesAutoMovement() {
        return false;
    }

    /**
     * Obtiene la dirección actual del enemigo.
     * @return dirección actual de movimiento
     */
    @Override
    public Direction getCurrentDirection() {
        return flowerpotDirection;
    }

    /**
     * Establece la dirección actual del enemigo.
     * @param dir nueva dirección a asignar
     */
    public void setCurrentDirection(Direction dir) {
        this.flowerpotDirection = dir;
    }

    /**
     * Actualiza el comportamiento del enemigo alternando entre
     * movimiento aleatorio y persecución.
     */
    @Override
    public void doUpdate() {

        tick++;
        if (tick < speed) {
            return;
        }
        tick = 0;

        long now = System.currentTimeMillis();
        long elapsed = now - modeTimer;

        if (elapsed >= MODE_DURATION) {
            chasingMode = !chasingMode;
            modeTimer = now;
            speed = chasingMode ? 1 : 2;
        }

        if (!chasingMode) {
            tryMoveOrTurn();
            return;
        }

        IceCream target = findClosestPlayer();
        if (target == null) {
            tryMoveOrTurn();
            return;
        }

        Position tp = target.getPosition();
        int dr = tp.getRow() - position.getRow();
        int dc = tp.getCol() - position.getCol();

        Direction preferred = null;
        if (Math.abs(dr) > Math.abs(dc)) {
            preferred = dr > 0 ? Direction.DOWN : Direction.UP;
        } else if (dc != 0) {
            preferred = dc > 0 ? Direction.RIGHT : Direction.LEFT;
        }

        if (preferred != null && moveInDirection(preferred)) {
            setAndMove(preferred);
            return;
        }

        if (tryAltRoute(tp)) {
            return;
        }

        moveAny();
    }

    /**
     * Busca el jugador vivo más cercano en el mapa.
     * @return jugador más cercano o null si no hay jugadores vivos
     */
    private IceCream findClosestPlayer() {
        IceCream closest = null;
        int bestDist = Integer.MAX_VALUE;

        for (int r = 0; r < gameMap.getRows(); r++) {
            for (int c = 0; c < gameMap.getCols(); c++) {
                Position pos = new Position(r, c);

                if (gameMap.hasPlayer(pos)) {
                    IceCream player = gameMap.getPlayer(pos);
                    if (player != null && player.isAlive()) {
                        int dist = Math.abs(r - position.getRow())
                                + Math.abs(c - position.getCol());
                        if (dist < bestDist) {
                            bestDist = dist;
                            closest = player;
                        }
                    }
                }
            }
        }
        return closest;
    }

    /**
     * Intenta moverse hacia el objetivo usando rutas alternativas.
     * @param target posición del objetivo
     * @return true si se logró mover, false en caso contrario
     */
    private boolean tryAltRoute(Position target) {
        int dr = target.getRow() - position.getRow();
        int dc = target.getCol() - position.getCol();

        Direction[] priority = calcPriority(dr, dc);

        for (Direction dir : priority) {
            if (moveInDirection(dir)) {
                setAndMove(dir);
                return true;
            }
        }
        return false;
    }

    /**
     * Calcula el orden de prioridad de direcciones según la distancia.
     * @param dr diferencia en filas
     * @param dc diferencia en columnas
     * @return arreglo de direcciones ordenadas por prioridad
     */
    private Direction[] calcPriority(int dr, int dc) {
        Direction vertical = dr > 0 ? Direction.DOWN : Direction.UP;
        Direction horizontal = dc > 0 ? Direction.RIGHT : Direction.LEFT;

        if (Math.abs(dr) > Math.abs(dc)) {
            return new Direction[]{
                vertical,
                horizontal,
                horizontal.getOpposite(),
                vertical.getOpposite()
            };
        }

        return new Direction[]{
            horizontal,
            vertical,
            vertical.getOpposite(),
            horizontal.getOpposite()
        };
    }

    /**
     * Intenta moverse en cualquier dirección disponible.
     */
    private void moveAny() {
        for (Direction dir : Direction.values()) {
            if (moveInDirection(dir)) {
                setAndMove(dir);
                return;
            }
        }
    }

    /**
     * Intenta continuar en la dirección actual o cambiarla.
     */
    private void tryMoveOrTurn() {
        if (moveInDirection(flowerpotDirection)) {
            setAndMove(flowerpotDirection);
        } else {
            changeRandomDir();
        }
    }

    /**
     * Actualiza la dirección y realiza el movimiento.
     * @param dir dirección de movimiento
     */
    private void setAndMove(Direction dir) {
        flowerpotDirection = dir;
        move(dir);
    }

    /**
     * Cambia la dirección actual a una alternativa válida.
     */
    private void changeRandomDir() {
        Direction[] lateral = (flowerpotDirection == Direction.UP || flowerpotDirection == Direction.DOWN)
                ? new Direction[]{Direction.LEFT, Direction.RIGHT}
                : new Direction[]{Direction.UP, Direction.DOWN};

        for (Direction dir : lateral) {
            if (moveInDirection(dir)) {
                setAndMove(dir);
                return;
            }
        }

        Direction opposite = flowerpotDirection.getOpposite();
        if (moveInDirection(opposite)) {
            setAndMove(opposite);
            return;
        }

        Direction rand = getRandomDir();
        if (moveInDirection(rand)) {
            setAndMove(rand);
        }
    }

    /**
     * Obtiene una dirección aleatoria.
     * @return dirección aleatoria
     */
    private Direction getRandomDir() {
        Direction[] dirs = Direction.values();
        return dirs[random.nextInt(dirs.length)];
    }

    /**
     * Obtiene la clave del sprite según el estado actual.
     * @return clave del sprite
     */
    @Override
    public String getSpriteKey() {
        return chasingMode ? "flowerpot_on" : "flowerpot";
    }

    /**
     * Indica si el enemigo usa animación.
     * @return false, ya que no es animado
     */
    @Override
    public boolean isAnimated() {
        return false;
    }

    /**
     * Indica si el enemigo está en modo persecución.
     * @return true si está persiguiendo al jugador
     */
    public boolean isCharging() {
        return chasingMode;
    }
}