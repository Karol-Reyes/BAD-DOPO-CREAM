package domain;

import java.util.Random;

/**
 * Flowerpot: alterna 6s de movimiento aleatorio y 6s persiguiendo al jugador más cercano.
 * Nunca rompe bloques.
 */
public class Flowerpot extends Enemy {

    private static final long MODE_DURATION = 6000; // 6 segundos

    private boolean chasingMode = false;
    private long modeTimer;
    private BadIceCream game; // ← AGREGAR REFERENCIA AL GAME

    private final Random random;
    private Direction currentDirection;

    public Flowerpot(Position position, double speed) {
        super(EnemyType.flowerpot, position, speed);
        this.random = new Random();
        this.currentDirection = getRandomDirection();
        this.modeTimer = System.currentTimeMillis();
    }

    public void setGame(BadIceCream game) {
        this.game = game;
    }

    @Override
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    @Override
    public void doUpdate() {
        long now = System.currentTimeMillis();
        long elapsed = now - modeTimer;

        // Alternar modo cada 6 segundos
        if (elapsed >= MODE_DURATION) {
            chasingMode = !chasingMode;
            modeTimer = now;
        }

        // ---------------- RANDOM MODE ----------------
        if (!chasingMode) {
            tryMoveOrTurn();
            return;
        }

        // ---------------- CHASING MODE ----------------
        IceCream target = findClosestPlayer();
        if (target == null) {
            tryMoveOrTurn();
            return;
        }

        // Calcular diferencia
        Position tp = target.getPosition();
        int dr = tp.getRow() - position.getRow();
        int dc = tp.getCol() - position.getCol();

        // Dirección preferida
        Direction preferred = null;
        if (Math.abs(dr) > Math.abs(dc)) {
            preferred = dr > 0 ? Direction.DOWN : Direction.UP;
        } else if (dc != 0) {
            preferred = dc > 0 ? Direction.RIGHT : Direction.LEFT;
        }

        // Intentar moverse en dirección preferida
        if (preferred != null && moveInDirection(preferred)) {
            setAndMove(preferred);
            return;
        }

        // Si no puede ir directo, intentar rutas alternativas
        if (intentarRutaAlternativa(tp)) {
            return;
        }

        // Si todo falla, moverse en cualquier dirección
        moverseEnCualquierDireccion();
    }

    /**
     * Encuentra al jugador vivo más cercano usando la lista del game
     */
    private IceCream findClosestPlayer() {
        IceCream closest = null;
        int bestDist = Integer.MAX_VALUE;

        // Buscar en TODO el mapa
        for (int r = 0; r < gameMap.getRows(); r++) {
            for (int c = 0; c < gameMap.getCols(); c++) {
                Position pos = new Position(r, c);
            
                if (gameMap.hasPlayer(pos)) {
                    IceCream player = gameMap.getPlayer(pos);
                    // Verificar que esté vivo
                    if (player != null && player.isAlive()) {
                        int dist = Math.abs(r - position.getRow()) + Math.abs(c - position.getCol());
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
     * Intenta encontrar una ruta alternativa hacia el objetivo
     */
    private boolean intentarRutaAlternativa(Position objetivo) {
        int dr = objetivo.getRow() - position.getRow();
        int dc = objetivo.getCol() - position.getCol();

        Direction[] prioridades = calcularPrioridades(dr, dc);

        for (Direction dir : prioridades) {
            if (moveInDirection(dir)) {
                setAndMove(dir);
                return true;
            }
        }

        return false;
    }

    /**
     * Calcula el orden de prioridad de direcciones
     */
    private Direction[] calcularPrioridades(int deltaRow, int deltaCol) {
        Direction vertical = deltaRow > 0 ? Direction.DOWN : Direction.UP;
        Direction horizontal = deltaCol > 0 ? Direction.RIGHT : Direction.LEFT;

        if (Math.abs(deltaRow) > Math.abs(deltaCol)) {
            return new Direction[]{
                vertical,
                horizontal,
                horizontal.getOpposite(),
                vertical.getOpposite()
            };
        } else {
            return new Direction[]{
                horizontal,
                vertical,
                vertical.getOpposite(),
                horizontal.getOpposite()
            };
        }
    }

    /**
     * Intenta moverse en CUALQUIER dirección disponible
     */
    private void moverseEnCualquierDireccion() {
        for (Direction dir : Direction.values()) {
            if (moveInDirection(dir)) {
                setAndMove(dir);
                return;
            }
        }
    }

    private void tryMoveOrTurn() {
        if (moveInDirection(currentDirection)) {
            setAndMove(currentDirection);
        } else {
            changeRandomDirection();
        }
    }

    private void setAndMove(Direction d) {
        currentDirection = d;
        move(d);
    }

    private void changeRandomDirection() {
        Direction[] laterals = (currentDirection == Direction.UP || currentDirection == Direction.DOWN)
                ? new Direction[]{Direction.LEFT, Direction.RIGHT}
                : new Direction[]{Direction.UP, Direction.DOWN};

        for (Direction d : laterals) {
            if (moveInDirection(d)) {
                setAndMove(d);
                return;
            }
        }

        Direction opposite = currentDirection.getOpposite();
        if (moveInDirection(opposite)) {
            setAndMove(opposite);
            return;
        }

        Direction rand = getRandomDirection();
        if (moveInDirection(rand)) {
            setAndMove(rand);
        }
    }

    private Direction getRandomDirection() {
        Direction[] vals = Direction.values();
        return vals[random.nextInt(vals.length)];
    }

    @Override
    public String getSpriteKey() {
        return chasingMode ? "flowerpot_on" : "flowerpot";
    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    public boolean isCharging() {
        return chasingMode;
    }
}