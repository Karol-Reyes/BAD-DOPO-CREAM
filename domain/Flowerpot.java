package domain;

import java.util.Random;

/**
 * Flowerpot: alterna 6s de movimiento aleatorio y 6s persiguiendo al jugador más cercano.
 * Nunca rompe bloques. Usa moveInDirection / move del Enemy real.
 */
public class Flowerpot extends Enemy {

    private static final long MODE_DURATION = 6000; // 6 segundos

    private boolean chasingMode = false;
    private long modeTimer;

    private final Random random;
    private Direction currentDirection;

    public Flowerpot(Position position, double speed) {
        super(EnemyType.flowerpot, position, speed);
        this.random = new Random();
        this.currentDirection = getRandomDirection();
        this.modeTimer = System.currentTimeMillis();
    }

    @Override
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    @Override
    public void doUpdate() {

        long now = System.currentTimeMillis();

        // Alternar modo cada 6 segundos
        if (now - modeTimer >= MODE_DURATION) {
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

        Direction preferred =
                Math.abs(dr) > Math.abs(dc)
                        ? (dr > 0 ? Direction.DOWN : Direction.UP)
                        : (dc > 0 ? Direction.RIGHT : Direction.LEFT);

        // 1° Preferred
        if (moveInDirection(preferred)) {
            setAndMove(preferred);
            return;
        }

        // 2° Opuesta
        Direction opposite = preferred.getOpposite();
        if (moveInDirection(opposite)) {
            setAndMove(opposite);
            return;
        }

        // 3° fallback
        changeRandomDirection();
    }

    // ----------------------------------------------------
    // ---------------------- HELPERS ----------------------
    // ----------------------------------------------------

    /** Intenta seguir recto; si no puede, gira. */
    private void tryMoveOrTurn() {
        if (moveInDirection(currentDirection)) {
            setAndMove(currentDirection);
        } else {
            changeRandomDirection();
        }
    }

    /** Cambia la dirección interna y mueve */
    private void setAndMove(Direction d) {
        currentDirection = d;
        move(d);
    }

    /**
     * Igual lógica que Troll:
     * - prueba laterales
     * - luego opuesta
     * - luego random
     */
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

        // finalmente random
        Direction rand = getRandomDirection();
        if (moveInDirection(rand)) {
            setAndMove(rand);
        }
    }

    /** Búsqueda manual del jugador más cercano */
    private IceCream findClosestPlayer() {

        IceCream closest = null;
        int bestDist = Integer.MAX_VALUE;

        for (int r = 0; r < gameMap.getRows(); r++) {
            for (int c = 0; c < gameMap.getCols(); c++) {

                IceCream p = gameMap.getPlayer(new Position(r, c));
                if (p == null || !p.isAlive()) continue;

                int dist = Math.abs(p.getPosition().getRow() - position.getRow())
                        + Math.abs(p.getPosition().getCol() - position.getCol());

                if (dist < bestDist) {
                    bestDist = dist;
                    closest = p;
                }
            }
        }

        return closest;
    }

    /** Random direction */
    private Direction getRandomDirection() {
        Direction[] vals = Direction.values();
        return vals[random.nextInt(vals.length)];
    }

    @Override
    public String getSpriteKey() {
        return "flowerpot_" + currentDirection.name().toLowerCase();
    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    public boolean isCharging() {
        return chasingMode;
    }
}
