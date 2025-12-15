package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enemigo tipo Troll. Se mueve de forma semialeatoria siguiendo una dirección
 * principal, cambiándola cuando encuentra obstáculos o está atrapado.
 */
public class Troll extends Enemy {

    private Direction trollDirection;
    private final Random random;
    @SuppressWarnings("unused")
    private BadIceCream game;

    private int tick = 0;
    private final int speed = 3;

    /** 
     * Construye un Troll con posición y velocidad específicas. 
     */
    public Troll(Position position) {
        super(EnemyType.troll, position);
        this.random = new Random();
        this.trollDirection = Direction.DOWN;
    }

    /**
     * Asigna el juego al que pertenece este troll.
     * @param game instancia del juego BadIceCream
     */
    @Override
    public void setGame(BadIceCream game) {
        this.game = game;
    }

    /**
     * @return false; los trolls no usan movimiento automático.
     */
    @Override
    protected boolean usesAutoMovement() {
        return false;
    }

    /** 
     * @return la dirección actual del troll.
     */
    @Override
    public Direction getCurrentDirection() {
        return trollDirection;
    }

    /** 
     * Actualiza el comportamiento del troll según su lógica de movimiento. 
     */
    @Override
    public void doUpdate() { 
        
        tick++;
        if (tick < speed) return;
        tick = 0;

        checkTrapped();
        if (trapped) return;

        Position oldPos = position;    
        Position target = new Position(
            oldPos.getRow() + trollDirection.getRowDelta(),
            oldPos.getCol() + trollDirection.getColDelta()
        );

        if (gameMap.hasEnemy(target)) {
            changeDirection();
            return;    
        }

        if (moveInDirection(trollDirection)) {
            move(trollDirection);
        } else {
            changeDirection();
        }
    }

    /** 
     * Detecta si el troll no puede moverse en ninguna dirección. 
     */
    private void checkTrapped() {
        trapped = true;
        for (Direction d : Direction.values()) {
            if (moveInDirection(d)) {
                trapped = false;
                return;
            }
        }
    }

    /** 
     * Cambia la dirección del troll cuando su camino está bloqueado.
     */
    private void changeDirection() {
        List<Direction> laterals = getLateral(trollDirection);
        for (Direction d : laterals) {
            if (moveInDirection(d)) {
                trollDirection = d;
                move(d);
                return;
            }
        }
        
        Direction opposite = trollDirection.getOpposite();
        if (moveInDirection(opposite)) {
            trollDirection = opposite;
            move(opposite);
            return;
        }
        
        for (Direction d : Direction.values()) {
            if (moveInDirection(d)) {
                trollDirection = d;
                move(d);
                return;
            }
        }
    }

    /** 
     * Genera las direcciones laterales según la dirección principal. 
     * @param current dirección principal
     */
    private List<Direction> getLateral(Direction current) {
        List<Direction> list = new ArrayList<>();
        if (current == Direction.UP || current == Direction.DOWN) {
            list.add(Direction.LEFT);
            list.add(Direction.RIGHT);
        } else {
            list.add(Direction.UP);
            list.add(Direction.DOWN);
        }

        if (random.nextBoolean()) {
            Direction t = list.get(0);
            list.set(0, list.get(1));
            list.set(1, t);
        }
        return list;
    }

    /** 
     * @return una dirección aleatoria. 
     */
    @SuppressWarnings("unused")
    private Direction getRandomDirection() {
        return Direction.values()[random.nextInt(Direction.values().length)];
    }

    /** 
     * @return clave del sprite según la dirección y si está atrapado. 
     */
    @Override
    public String getSpriteKey() {
        return trapped ? "troll_trapped" : "troll_" + trollDirection.name().toLowerCase();
    }

    /** 
     * @return siempre false; los trolls no tienen animación.
     * */
    @Override
    public boolean isAnimated() {
        return false;
    }
}