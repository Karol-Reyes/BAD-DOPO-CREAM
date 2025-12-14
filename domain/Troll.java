package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enemigo tipo Troll. Se mueve de forma semialeatoria siguiendo una dirección
 * principal, cambiándola cuando encuentra obstáculos o está atrapado.
 */
public class Troll extends Enemy {

    private Direction currentDirection;
    private final Random random;
    private BadIceCream game;

    private int tick = 0;
    private int speed = 3;

    /** Construye un Troll con posición y velocidad específicas. */
    public Troll(Position position) {
        super(EnemyType.troll, position);
        this.random = new Random();
        this.currentDirection = Direction.DOWN;
    }

    @Override
    public void setGame(BadIceCream game) {
        this.game = game;
    }

    @Override
    protected boolean usesAutoMovement() {
        return false;
    }

    /** @return la dirección actual del troll. */
    @Override
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /** Actualiza el comportamiento del troll según su lógica de movimiento. */
    @Override
    public void doUpdate() { 
        
        tick++;
        if (tick < speed) return;
        tick = 0;

        checkTrapped();
        if (trapped) return;

        Position oldPos = position;    
        Position target = new Position(
            oldPos.getRow() + currentDirection.getRowDelta(),
            oldPos.getCol() + currentDirection.getColDelta()
        );

        // Si hay enemigo → cambiar dirección
        if (gameMap.hasEnemy(target)) {
            changeDirection();
            return;    
        }

        // Intenta moverse en la dirección actual
        if (moveInDirection(currentDirection)) {
            move(currentDirection);
        } else {
            // Si no puede, cambia de dirección
            changeDirection();
        }
    }

    /** Detecta si el troll no puede moverse en ninguna dirección. */
    private void checkTrapped() {
        trapped = true;
        for (Direction d : Direction.values()) {
            if (moveInDirection(d)) {
                trapped = false;
                return;
            }
        }
    }

    /** Cambia la dirección del troll cuando su camino está bloqueado. */
    private void changeDirection() {
        // Primero intenta las direcciones laterales
        List<Direction> laterals = getLateral(currentDirection);
        for (Direction d : laterals) {
            if (moveInDirection(d)) {
                currentDirection = d;
                move(d);
                return;
            }
        }
        
        // Si las laterales están bloqueadas, intenta la dirección opuesta
        Direction opposite = currentDirection.getOpposite();
        if (moveInDirection(opposite)) {
            currentDirection = opposite;
            move(opposite);
            return;
        }
        
        // Si todo está bloqueado, intenta CUALQUIER dirección disponible
        for (Direction d : Direction.values()) {
            if (moveInDirection(d)) {
                currentDirection = d;
                move(d);
                return;
            }
        }
    }

    /** Genera las direcciones laterales según la dirección principal. */
    private List<Direction> getLateral(Direction current) {
        List<Direction> list = new ArrayList<>();
        if (current == Direction.UP || current == Direction.DOWN) {
            list.add(Direction.LEFT);
            list.add(Direction.RIGHT);
        } else {
            list.add(Direction.UP);
            list.add(Direction.DOWN);
        }

        // Aleatoriza el orden
        if (random.nextBoolean()) {
            Direction t = list.get(0);
            list.set(0, list.get(1));
            list.set(1, t);
        }
        return list;
    }

    /** @return una dirección aleatoria. */
    private Direction getRandomDirection() {
        return Direction.values()[random.nextInt(Direction.values().length)];
    }

    /** @return clave del sprite según la dirección y si está atrapado. */
    @Override
    public String getSpriteKey() {
        return trapped ? "troll_trapped" : "troll_" + currentDirection.name().toLowerCase();
    }

    /** @return siempre false; los trolls no tienen animación. */
    @Override
    public boolean isAnimated() {
        return false;
    }
}