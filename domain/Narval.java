package domain;


import java.util.Random;
/**
 * Narval: Patrulla pero si un jugador se alinea
 * horizontal o verticalmente, embiste en esa dirección destruyendo
 * hielo en su camino.
 */
public class Narval extends Enemy {

    private boolean charging;
    private Direction chargeDirection;
    private final Random random;

    public Narval(Position position, double speed) {
        super(EnemyType.narval, position, speed);
        this.charging = false;
        this.random = new Random();
        this.chargeDirection = null;
        this.currentDirection = Direction.DOWN;
    }

    @Override
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    @Override
    public void doUpdate() {
        if (charging) {
            if (canContinueCharging()) {
                performChargeStep();
                return;
            } else {
                charging = false;
                chargeDirection = null;
            }
        }

        Direction aligned = findAlignedPlayer();
        if (aligned != null) {
            charging = true;
            chargeDirection = aligned;
            currentDirection = aligned;
            performChargeStep();
            return;
        }
        patrolMovement();
    }

    /** Sigue en embestida si no hay obstáculo. */
    private boolean canContinueCharging() {

        Position next = new Position(
                position.getRow() + chargeDirection.getRowDelta(),
                position.getCol() + chargeDirection.getColDelta()
        );

        if (!gameMap.isValid(next)) return false;
        if (gameMap.getEnemy(next) != null) return false;
        return true;
    }

    private void performChargeStep() {

        Position next = new Position(
                position.getRow() + chargeDirection.getRowDelta(),
                position.getCol() + chargeDirection.getColDelta()
        );

        if (!gameMap.isValid(next)) {
            charging = false;
            return;
        }

        Boxy b = gameMap.getBlock(next);

        // destruir hielo durante la carga
        if (b != null && b.getType() == BoxType.ice && b.canBeDestroyed()) {
            gameMap.clearBlock(next);
        }

        // mover normal
        if (moveInDirection(chargeDirection)) {
            move(chargeDirection);
        } else {
            charging = false;
        }
    }

    private Direction findAlignedPlayer() {
        for (Direction d : Direction.values()) {
            if (playerInDirection(d)) return d;
        }
        return null;
    }

    /** Recorre una línea recta buscando un jugador vivo. */
    private boolean playerInDirection(Direction d) {
        Position cur = position;
        while (true) {
            cur = new Position(
                    cur.getRow() + d.getRowDelta(),
                    cur.getCol() + d.getColDelta()
            );

            if (!gameMap.isValid(cur)) return false;

            IceCream pl = gameMap.getPlayer(cur);
            if (pl != null && pl.isAlive()) return true;

            // bloque no-ice bloquea la vista
            Boxy b = gameMap.getBlock(cur);
            if (b != null && b.isCreated() && b.getType() != BoxType.ice) {
                return false;
            }
        }
    }

    private void patrolMovement() {

        if (moveInDirection(currentDirection)) {
            move(currentDirection);
            return;
        }

        // laterales según el eje
        Direction[] laterals = (currentDirection == Direction.UP || currentDirection == Direction.DOWN)
                ? new Direction[]{Direction.LEFT, Direction.RIGHT}
                : new Direction[]{Direction.UP, Direction.DOWN};

        for (Direction d : laterals) {
            if (moveInDirection(d)) {
                currentDirection = d;
                move(d);
                return;
            }
        }

        // opuesta
        Direction opp = currentDirection.getOpposite();
        if (moveInDirection(opp)) {
            currentDirection = opp;
            move(opp);
            return;
        }
    }

    @Override
    public String getSpriteKey() {
        if (charging) {
            return "narval_charging_" + currentDirection.name().toLowerCase();
        }
        return "narval_" + currentDirection.name().toLowerCase();
    }

    @Override
    public boolean isAnimated() {
        return true;
    }
}
