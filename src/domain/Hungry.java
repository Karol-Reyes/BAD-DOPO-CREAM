package domain;
import java.util.*;

/**
 * IA agresiva enfocada en recolectar frutas.
 */
public class Hungry implements ControllerCream {

    private IceCream player;
    private final GameMap map;
    private final BadIceCream game;

    private int tick;
    private final int speed = 3;

    private Position lastPos;
    private int stuckTicks;
    private Position targetFruit;

    /**
     * Crea una IA agresiva enfocada en recolectar frutas.
     * @param map mapa del juego
     * @param game instancia principal del juego
     */
    public Hungry(GameMap map, BadIceCream game) {
        this.map = map;
        this.game = game;
    }

    /**
     * Asigna el jugador controlado por la IA.
     * @param player jugador a controlar
     */
    @Override
    public void setPlayer(IceCream player) {
        this.player = player;
    }

    /**
     * Actualiza el comportamiento de la IA en cada ciclo.
     */
    @Override
    public void update() {
        tick++;
        if (tick < speed) return;
        tick = 0;

        if (player == null || !player.isAlive()) return;
        Position current = player.getPosition();

        if (current.equals(lastPos)) {
            stuckTicks++;
            if (stuckTicks > 6) {
                unstuck();
                stuckTicks = 0;
                targetFruit = null;
            }
        } else {
            stuckTicks = 0;
        }

        lastPos = new Position(current.getRow(), current.getCol());
        game.checkCollisionsFor(player);
        Fruit fruit = chooseFruit();

        if (fruit != null) {
            targetFruit = fruit.getPosition();
            moveToFruit(fruit.getPosition());
        } else {
            explore();
        }
    }

    /**
     * Selecciona la fruta más conveniente según distancia y obstáculos.
     * @return fruta objetivo o null si no hay frutas válidas
     */
    private Fruit chooseFruit() {
        Fruit best = null;
        int bestScore = Integer.MIN_VALUE;
        Position cur = player.getPosition();

        for (Fruit f : game.getFruits()) {
            if (f.isEaten() || !f.isActive()) continue;

            int dist = distance(cur, f.getPosition());
            int blocks = countBlocks(cur, f.getPosition());
            int score = (100 - dist * 2) - (blocks * 5);

            if (targetFruit != null && f.getPosition().equals(targetFruit)) {
                score += 30;
            }

            if (score > bestScore) {
                bestScore = score;
                best = f;
            }
        }
        return best;
    }

    /**
     * Cuenta obstáculos aproximados entre dos posiciones.
     * @param from posición inicial
     * @param to posición destino
     * @return cantidad estimada de obstáculos
     */
    private int countBlocks(Position from, Position to) {
        int blocks = 0;
        int steps = Math.max(
                Math.abs(to.getRow() - from.getRow()),
                Math.abs(to.getCol() - from.getCol())
        );

        for (int i = 1; i <= steps; i++) {
            float t = (float) i / steps;
            int r = (int) (from.getRow() + t * (to.getRow() - from.getRow()));
            int c = (int) (from.getCol() + t * (to.getCol() - from.getCol()));
            Position p = new Position(r, c);

            if (map.isValid(p) && map.isBlocked(p)) {
                blocks++;
            }
        }
        return blocks;
    }

    /**
     * Avanza agresivamente hacia una fruta objetivo.
     * @param dest posición destino
     */
    private void moveToFruit(Position dest) {
        Position cur = player.getPosition();
        Direction dir = bestDir(cur, dest);

        if (dir == null) {
            explore();
            return;
        }

        Position next = new Position(
                cur.getRow() + dir.getRowDelta(),
                cur.getCol() + dir.getColDelta()
        );

        if (map.isValid(next) && map.hasEnemy(next)) {
            player.createIce(dir);

            for (Direction alt : altDirs(dest)) {
                Position p = new Position(
                        cur.getRow() + alt.getRowDelta(),
                        cur.getCol() + alt.getColDelta()
                );

                if (map.isValid(p) && !map.hasEnemy(p)) {
                    if (tryMove(alt)) return;
                }
            }
            return;
        }
        tryMove(dir);
    }

    /**
     * Intenta moverse en una dirección destruyendo obstáculos si es necesario.
     * @param dir dirección a intentar
     * @return true si logró moverse
     */
    private boolean tryMove(Direction dir) {
        boolean moved = player.move(dir);

        if (!moved) {
            Position next = new Position(
                    player.getPosition().getRow() + dir.getRowDelta(),
                    player.getPosition().getCol() + dir.getColDelta()
            );

            if (map.isValid(next) && map.isBlocked(next)) {
                player.destroyIce(dir);
                return player.move(dir);
            }
        }
        return moved;
    }

    /**
     * Calcula la mejor dirección hacia un destino.
     * @param from posición actual
     * @param to posición destino
     * @return dirección óptima o null
     */
    private Direction bestDir(Position from, Position to) {
        int dr = to.getRow() - from.getRow();
        int dc = to.getCol() - from.getCol();

        if (dr == 0 && dc == 0) return null;

        Direction[] dirs;

        if (Math.abs(dr) > Math.abs(dc)) {
            Direction v = dr > 0 ? Direction.DOWN : Direction.UP;
            Direction h = dc != 0 ? (dc > 0 ? Direction.RIGHT : Direction.LEFT) : null;
            dirs = h != null ? new Direction[]{v, h} : new Direction[]{v};
        } else {
            Direction h = dc > 0 ? Direction.RIGHT : Direction.LEFT;
            Direction v = dr != 0 ? (dr > 0 ? Direction.DOWN : Direction.UP) : null;
            dirs = v != null ? new Direction[]{h, v} : new Direction[]{h};
        }

        for (Direction d : dirs) {
            Position p = new Position(
                    from.getRow() + d.getRowDelta(),
                    from.getCol() + d.getColDelta()
            );
            if (map.isValid(p) && !map.hasEnemy(p)) return d;
        }

        return dirs.length > 0 ? dirs[0] : null;
    }

    /**
     * Calcula direcciones alternativas hacia un destino.
     * @param dest posición destino
     * @return arreglo de direcciones alternativas
     */
    private Direction[] altDirs(Position dest) {
        Position cur = player.getPosition();
        int dr = dest.getRow() - cur.getRow();
        int dc = dest.getCol() - cur.getCol();

        Direction v = dr > 0 ? Direction.DOWN : Direction.UP;
        Direction h = dc > 0 ? Direction.RIGHT : Direction.LEFT;

        return Math.abs(dr) > Math.abs(dc)
                ? new Direction[]{h, v.getOpposite(), h.getOpposite()}
                : new Direction[]{v, h.getOpposite(), v.getOpposite()};
    }

    /**
     * Explora el mapa de forma agresiva cuando no hay frutas.
     */
    private void explore() {
        List<DirScore> options = new ArrayList<>();
        Position cur = player.getPosition();

        for (Direction d : Direction.values()) {
            Position next = new Position(
                    cur.getRow() + d.getRowDelta(),
                    cur.getCol() + d.getColDelta()
            );

            if (!map.isValid(next)) continue;
            int score = 0;
            if (!map.isBlocked(next)) score += 100;
            if (map.hasEnemy(next)) score -= 1000;

            if (lastPos != null) {
                score += distance(next, lastPos) * 10;
            }

            options.add(new DirScore(d, score));
        }

        options.sort((a, b) -> Integer.compare(b.score, a.score));

        for (DirScore ds : options) {
            if (tryMove(ds.dir)) return;
        }
    }

    /**
     * Libera al jugador cuando queda completamente bloqueado.
     */
    private void unstuck() {
        for (Direction d : Direction.values()) {
            Position p = new Position(
                    player.getPosition().getRow() + d.getRowDelta(),
                    player.getPosition().getCol() + d.getColDelta()
            );

            if (map.isValid(p) && map.isBlocked(p)) {
                player.destroyIce(d);
                player.move(d);
                return;
            }
        }

        Direction d = Direction.values()[new Random().nextInt(4)];
        player.createIce(d);
    }

    /**
     * Calcula la distancia Manhattan entre dos posiciones.
     * @param a primera posición
     * @param b segunda posición
     * @return distancia calculada
     */
    private int distance(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }

    private static class DirScore {
        Direction dir;
        int score;

        DirScore(Direction dir, int score) {
            this.dir = dir;
            this.score = score;
        }
    }
}