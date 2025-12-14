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

    private int tick = 0;
    private int normalSpeed = 2;
    private int chargeSpeed = 0;

    public Narval(Position position) {
        super(EnemyType.narval, position);
        this.charging = false;
        this.random = new Random();
        this.chargeDirection = null;
        this.currentDirection = Direction.DOWN;
    }

    @Override
    public void setGame(BadIceCream game) {
        // No necesita game, pero implementamos el método
    }

    @Override
    protected boolean usesAutoMovement() {
        return false;  // Maneja su propio movimiento
    }

    @Override
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    @Override
    public void doUpdate() {
        // Ajustar velocidad según el estado
        int speed = charging ? chargeSpeed : normalSpeed;

        tick++;
        if (tick < speed) return;
        tick = 0;
        
        // Si está cargando, continuar hasta que se detenga
        if (charging) {
            if (canContinueCharging()) {
                performChargeStep();
                return;
            } else {
                // Se detuvo la carga
                charging = false;
                chargeDirection = null;
            }
        }

        // Buscar si hay un jugador alineado
        Direction aligned = findAlignedPlayer();
        if (aligned != null) {
            // ¡Iniciar carga!
            charging = true;
            chargeDirection = aligned;
            currentDirection = aligned;
            performChargeStep();
            return;
        }

        // Movimiento de patrulla normal
        patrolMovement();
    }

    /** 
     * Verifica si puede continuar la carga.
     * Se detiene solo si encuentra un bloque indestructible o sale del mapa.
     */
    private boolean canContinueCharging() {
        Position next = new Position(
            position.getRow() + chargeDirection.getRowDelta(),
            position.getCol() + chargeDirection.getColDelta()
        );

        // Salió del mapa
        if (!gameMap.isValid(next)) return false;

        // Hay otro enemigo
        if (gameMap.hasEnemy(next)) return false;

        Boxy b = gameMap.getBlock(next);
        
        // Si hay un bloque
        if (b != null) {
            // Si es hielo, puede seguir (lo romperá)
            if (b.getType() == BoxType.ice && b.canBeDestroyed()) {
                return true;
            }
            
            // Si es un bloque indestructible o no es hielo, se detiene
            if (b.isCreated() && !b.canBeDestroyed()) {
                return false;
            }
            
            // Si el bloque no se puede atravesar
            if (!b.canWalk()) {
                return false;
            }
        }

        // Puede continuar
        return true;
    }

    /** Realiza un paso de la carga, destruyendo hielo si es necesario */
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

        // Destruir hielo durante la carga
        if (b != null && b.getType() == BoxType.ice && b.canBeDestroyed()) {
            gameMap.clearBlock(next);
        }

        // Intentar moverse
        if (moveInDirection(chargeDirection)) {
            move(chargeDirection);
        } else {
            // No pudo moverse, detener carga
            charging = false;
        }
    }

    /** Busca si hay algún jugador alineado en alguna dirección */
    private Direction findAlignedPlayer() {
        for (Direction d : Direction.values()) {
            if (playerInDirection(d)) {
                return d;
            }
        }
        return null;
    }

    /** 
     * Recorre una línea recta en una dirección buscando un jugador vivo.
     * El hielo NO bloquea la vista, pero otros bloques sí.
     */
    private boolean playerInDirection(Direction d) {
        Position cur = new Position(position.getRow(), position.getCol());
        
        while (true) {
            // Avanzar una posición
            cur = new Position(
                cur.getRow() + d.getRowDelta(),
                cur.getCol() + d.getColDelta()
            );

            // Salió del mapa
            if (!gameMap.isValid(cur)) return false;

            // ¿Hay un jugador vivo aquí?
            IceCream pl = gameMap.getPlayer(cur);
            if (pl != null && pl.isAlive()) {
                return true;
            }

            // Verificar si hay un bloque que bloquea la vista
            Boxy b = gameMap.getBlock(cur);
            if (b != null && b.isCreated()) {
                // El hielo NO bloquea la vista (puede verlo a través del hielo)
                if (b.getType() == BoxType.ice) {
                    continue; // Sigue buscando
                }
                // Cualquier otro bloque bloquea la vista
                return false;
            }
        }
    }

    /** Movimiento de patrulla cuando no está cargando */
    private void patrolMovement() {
        // Intentar seguir en la misma dirección
        if (moveInDirection(currentDirection)) {
            move(currentDirection);
            return;
        }

        // Intentar laterales según el eje
        Direction[] laterals = (currentDirection == Direction.UP || currentDirection == Direction.DOWN)
                ? new Direction[]{Direction.LEFT, Direction.RIGHT}
                : new Direction[]{Direction.UP, Direction.DOWN};

        // Aleatorizar el orden de las laterales
        if (random.nextBoolean() && laterals.length > 1) {
            Direction temp = laterals[0];
            laterals[0] = laterals[1];
            laterals[1] = temp;
        }

        for (Direction d : laterals) {
            if (moveInDirection(d)) {
                currentDirection = d;
                move(d);
                return;
            }
        }

        // Intentar la dirección opuesta
        Direction opp = currentDirection.getOpposite();
        if (moveInDirection(opp)) {
            currentDirection = opp;
            move(opp);
            return;
        }

        // Última opción: cualquier dirección disponible
        for (Direction d : Direction.values()) {
            if (moveInDirection(d)) {
                currentDirection = d;
                move(d);
                return;
            }
        }
    }

    @Override
    public String getSpriteKey() {
        if (charging) {
            return "narval_charging";
        }
        return "narval_";
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    public boolean isCharging() {
        return charging;
    }
}