package domain;

import java.util.Random;

/**
 * Representa un enemigo Narval dentro del mapa del juego.
 * El Narval puede patrullar y cargar contra los jugadores alineados.
 */
public class Narval extends Enemy {

    private boolean charging;
    private Direction chargeDir;
    private final Random rng;

    private int tick;
    private final int walkDelay;
    private final int chargeDelay;

    /**
     * Crea un enemigo Narval en la posición indicada.
     * @param pos posición inicial del enemigo
     */
    public Narval(Position pos) {
        super(EnemyType.narval, pos);
        this.charging = false;
        this.chargeDir = null;
        this.rng = new Random();
        this.tick = 0;
        this.walkDelay = 2;
        this.chargeDelay = 0;
        this.currentDirection = Direction.DOWN;
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
     * Actualiza el comportamiento del enemigo en cada ciclo del juego.
     */
    @Override
    public void doUpdate() {
        int delay = charging ? chargeDelay : walkDelay;

        tick++;
        if (tick < delay) return;
        tick = 0;

        if (charging) {
            if (canCharge()) {
                chargeStep();
                return;
            }
            charging = false;
            chargeDir = null;
        }

        Direction aligned = findPlayer();
        if (aligned != null) {
            charging = true;
            chargeDir = aligned;
            currentDirection = aligned;
            chargeStep();
            return;
        }
        patrol();
    }

    /**
     * Verifica si la carga puede continuar en la dirección actual.
     * @return true si puede seguir cargando, false en caso contrario
     */
    private boolean canCharge() {
        Position next = new Position(
                position.getRow() + chargeDir.getRowDelta(),
                position.getCol() + chargeDir.getColDelta()
        );

        if (!gameMap.isValid(next)) return false;
        if (gameMap.hasEnemy(next)) return false;
        Boxy b = gameMap.getBlock(next);

        if (b != null) {
            if (b.getType() == BoxType.ice && b.canBeDestroyed()) return true;
            if (b.isCreated() && !b.canBeDestroyed()) return false;
            if (!b.canWalk()) return false;
        }
        return true;
    }

    /**
     * Ejecuta un paso de la carga, destruyendo hielo y avanzando.
     */
    private void chargeStep() {
        Position next = new Position(
                position.getRow() + chargeDir.getRowDelta(),
                position.getCol() + chargeDir.getColDelta()
        );

        if (!gameMap.isValid(next)) {
            charging = false;
            return;
        }

        Boxy b = gameMap.getBlock(next);
        if (b != null && b.getType() == BoxType.ice && b.canBeDestroyed()) {
            gameMap.clearBlock(next);
        }

        if (moveInDirection(chargeDir)) {
            move(chargeDir);
        } else {
            charging = false;
        }
    }

    /**
     * Busca un jugador alineado en cualquier dirección.
     * @return dirección del jugador alineado o null si no existe
     */
    private Direction findPlayer() {
        for (Direction d : Direction.values()) {
            if (playerAhead(d)) return d;
        }
        return null;
    }

    /**
     * Verifica si hay un jugador vivo en línea recta en una dirección.
     * @param dir dirección a evaluar
     * @return true si hay un jugador visible, false en caso contrario
     */
    private boolean playerAhead(Direction dir) {
        Position cur = new Position(position.getRow(), position.getCol());

        while (true) {
            cur = new Position(
                    cur.getRow() + dir.getRowDelta(),
                    cur.getCol() + dir.getColDelta()
            );

            if (!gameMap.isValid(cur)) return false;

            IceCream p = gameMap.getPlayer(cur);
            if (p != null && p.isAlive()) return true;

            Boxy b = gameMap.getBlock(cur);
            if (b != null && b.isCreated() && b.getType() != BoxType.ice) {
                return false;
            }
        }
    }

    /**
     * Realiza el movimiento de patrulla cuando no está cargando.
     */
    private void patrol() {
        if (moveInDirection(currentDirection)) {
            move(currentDirection);
            return;
        }

        Direction[] sides = (currentDirection == Direction.UP || currentDirection == Direction.DOWN)
                ? new Direction[]{Direction.LEFT, Direction.RIGHT}
                : new Direction[]{Direction.UP, Direction.DOWN};

        if (rng.nextBoolean() && sides.length == 2) {
            Direction tmp = sides[0];
            sides[0] = sides[1];
            sides[1] = tmp;
        }

        for (Direction d : sides) {
            if (moveInDirection(d)) {
                currentDirection = d;
                move(d);
                return;
            }
        }

        Direction back = currentDirection.getOpposite();
        if (moveInDirection(back)) {
            currentDirection = back;
            move(back);
            return;
        }

        for (Direction d : Direction.values()) {
            if (moveInDirection(d)) {
                currentDirection = d;
                move(d);
                return;
            }
        }
    }

    /**
     * Devuelve la clave del sprite según el estado actual.
     * @return clave del sprite
     */
    @Override
    public String getSpriteKey() {
        return charging ? "narval_charging" : "narval_";
    }

    /**
     * Indica si el enemigo tiene animación.
     * @return true siempre
     */
    @Override
    public boolean isAnimated() {
        return true;
    }

    /**
     * Indica si el Narval está cargando.
     * @return true si está en estado de carga
     */
    public boolean isCharging() {
        return charging;
    }
}