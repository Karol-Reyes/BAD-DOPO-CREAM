package domain;
import java.util.*;

/**
 * Controlador de IA que implementa un comportamiento temeroso para el jugador IceCream.
 * El jugador evita enemigos y busca frutas seguras.
 */
public class Fearful implements ControllerCream {

    private IceCream player;
    private final GameMap map;
    private final BadIceCream game;

    private static final int DANGER_DIST = 8;
    private static final int CRIT_DIST = 4;
    private static final int SAFE_FRUIT_DIST = 10;

    private int tick;
    private final int speed = 3;

    private Position lastPos;
    private int stuckTicks;

    /**
     * Crea un controlador temeroso con acceso al mapa y al juego.
     * @param map mapa del juego
     * @param game instancia principal del juego
     */
    public Fearful(GameMap map, BadIceCream game) {
        this.map = map;
        this.game = game;
    }

    /**
     * Asigna el jugador controlado por la IA.
     * @param player jugador IceCream
     */
    @Override
    public void setPlayer(IceCream player) {
        this.player = player;
    }

    /**
     * Ejecuta la lógica principal del comportamiento temeroso.
     */
    @Override
    public void update() {
        tick++;
        if (tick < speed) return;
        tick = 0;

        if (player == null || !player.isAlive()) return;
        Position pos = player.getPosition();

        stuckTicks = pos.equals(lastPos) ? stuckTicks + 1 : 0;
        if (stuckTicks > 8) {
            unstuck();
            stuckTicks = 0;
        }
        lastPos = new Position(pos.getRow(), pos.getCol());

        List<Enemy> critical = criticalEnemies();
        if (!critical.isEmpty()) {
            panicEscape(critical);
            return;
        }

        List<Enemy> nearby = nearbyEnemies();
        if (!nearby.isEmpty()) {
            hideFrom(nearby);
            return;
        }

        Fruit fruit = safeFruit();
        if (fruit != null) {
            carefulMove(fruit.getPosition());
            return;
        }
        safePatrol();
    }

    /**
     * Obtiene los enemigos en distancia crítica.
     * @return lista de enemigos críticos
     */
    private List<Enemy> criticalEnemies() {
        List<Enemy> list = new ArrayList<>();
        Position pos = player.getPosition();

        for (Enemy e : game.getEnemies()) {
            if (dist(pos, e.getPosition()) <= CRIT_DIST) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Ejecuta una huida inmediata y defensiva.
     * @param enemies enemigos críticos
     */
    private void panicEscape(List<Enemy> enemies) {
        Position pos = player.getPosition();

        for (Enemy e : enemies) {
            if (dist(pos, e.getPosition()) <= 2) {
                Direction d = dirTo(e.getPosition());
                if (d != null) player.createIce(d);
            }
        }

        Direction fleeDir = farDir(enemies);
        tryMove(fleeDir);
    }

    /**
     * Obtiene los enemigos cercanos pero no críticos.
     * @return lista de enemigos cercanos
     */
    private List<Enemy> nearbyEnemies() {
        List<Enemy> list = new ArrayList<>();
        Position pos = player.getPosition();

        for (Enemy e : game.getEnemies()) {
            int d = dist(pos, e.getPosition());
            if (d > CRIT_DIST && d <= DANGER_DIST) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Ejecuta una maniobra defensiva contra enemigos cercanos.
     * @param enemies enemigos cercanos
     */
    private void hideFrom(List<Enemy> enemies) {
        Position pos = player.getPosition();

        for (Enemy e : enemies) {
            if (aligned(pos, e.getPosition())) {
                Direction d = dirTo(e.getPosition());
                if (d != null) player.createIce(d);
            }
        }

        Direction safe = safeDir(enemies);
        tryMove(safe);
    }

    /**
     * Busca una fruta extremadamente segura.
     * @return fruta segura o null
     */
    private Fruit safeFruit() {
        Fruit best = null;
        int bestScore = Integer.MIN_VALUE;
        Position pos = player.getPosition();

        for (Fruit f : game.getFruits()) {
            if (!f.isActive() || f.isEaten()) continue;

            int enemyDist = Integer.MAX_VALUE;
            for (Enemy e : game.getEnemies()) {
                enemyDist = Math.min(enemyDist, dist(f.getPosition(), e.getPosition()));
            }

            if (enemyDist < SAFE_FRUIT_DIST) continue;

            int d = dist(pos, f.getPosition());
            int risk = pathRisk(pos, f.getPosition());

            int score = (50 - d) - (risk * 10) + (enemyDist * 2);
            if (score > bestScore) {
                bestScore = score;
                best = f;
            }
        }

        return bestScore >= 0 ? best : null;
    }

    /**
     * Evalúa el riesgo aproximado del camino hacia un destino.
     * @param from posición inicial
     * @param to posición destino
     * @return nivel de peligro del camino
     */
    private int pathRisk(Position from, Position to) {
        int risk = 0;
        int steps = Math.max(
                Math.abs(to.getRow() - from.getRow()),
                Math.abs(to.getCol() - from.getCol())
        );

        for (int i = 0; i <= steps; i++) {
            float t = steps > 0 ? (float) i / steps : 0;
            Position p = new Position(
                    (int) (from.getRow() + t * (to.getRow() - from.getRow())),
                    (int) (from.getCol() + t * (to.getCol() - from.getCol()))
            );

            for (Enemy e : game.getEnemies()) {
                int d = dist(p, e.getPosition());
                if (d <= 3) risk += (4 - d);
            }
        }
        return risk;
    }

    /**
     * Se desplaza cuidadosamente hacia un destino.
     * @param target posición objetivo
     */
    private void carefulMove(Position target) {
        for (Enemy e : game.getEnemies()) {
            if (dist(target, e.getPosition()) < DANGER_DIST) {
                safePatrol();
                return;
            }
        }

        Direction d = dirTo(target);
        tryMove(d);
    }

    /**
     * Ejecuta una patrulla segura sin objetivo definido.
     */
    private void safePatrol() {
        Direction d = safeDir(game.getEnemies());
        tryMove(d);
    }

    /**
     * Obtiene la dirección más alejada de los enemigos.
     * @param enemies enemigos evaluados
     * @return dirección óptima de huida
     */
    private Direction farDir(List<Enemy> enemies) {
        Direction best = null;
        int max = -1;
        Position pos = player.getPosition();

        for (Direction d : Direction.values()) {
            Position next = nextPos(pos, d);
            if (!map.isValid(next) || map.hasEnemy(next)) continue;

            int total = 0;
            for (Enemy e : enemies) {
                total += dist(next, e.getPosition());
            }

            if (total > max) {
                max = total;
                best = d;
            }
        }
        return best;
    }

    /**
     * Obtiene la dirección con menor peligro.
     * @param enemies enemigos evaluados
     * @return dirección más segura
     */
    private Direction safeDir(Collection<Enemy> enemies) {
        Direction best = null;
        int min = Integer.MAX_VALUE;
        Position pos = player.getPosition();

        for (Direction d : Direction.values()) {
            Position next = nextPos(pos, d);
            if (!map.isValid(next) || map.hasEnemy(next)) continue;

            int risk = posRisk(next, enemies);
            if (risk < min) {
                min = risk;
                best = d;
            }
        }
        return best;
    }

    /**
     * Calcula el peligro de una posición específica.
     * @param pos posición evaluada
     * @param enemies enemigos cercanos
     * @return nivel de peligro
     */
    private int posRisk(Position pos, Collection<Enemy> enemies) {
        if (map.isBlocked(pos)) return 9999;

        int risk = 0;
        for (Enemy e : enemies) {
            int d = dist(pos, e.getPosition());

            if (d == 0) risk += 10000;
            else if (d == 1) risk += 5000;
            else if (d == 2) risk += 1000;
            else if (d == 3) risk += 500;
            else if (d <= 5) risk += 100;
            else if (d <= 8) risk += 20;

            if (aligned(pos, e.getPosition())) {
                risk += 200 / Math.max(d, 1);
            }
        }
        return risk;
    }

    /**
     * Intenta liberar al jugador cuando queda bloqueado.
     */
    private void unstuck() {
        Position pos = player.getPosition();

        for (Direction d : Direction.values()) {
            Position p = nextPos(pos, d);
            if (map.isValid(p) && map.isBlocked(p)) {
                player.destroyIce(d);
                player.move(d);
                return;
            }
        }
    }

    /**
     * Intenta mover al jugador en una dirección.
     * @param d dirección de movimiento
     * @return true si se pudo mover
     */
    private boolean tryMove(Direction d) {
        if (d == null) return false;

        Position next = nextPos(player.getPosition(), d);
        boolean ok = player.move(d);

        if (!ok && map.isValid(next) && map.isBlocked(next)) {
            player.destroyIce(d);
            return player.move(d);
        }
        return ok;
    }

    /**
     * Indica si dos posiciones están alineadas.
     * @param a primera posición
     * @param b segunda posición
     * @return true si comparten fila o columna
     */
    private boolean aligned(Position a, Position b) {
        return a.getRow() == b.getRow() || a.getCol() == b.getCol();
    }

    /**
     * Calcula la dirección hacia una posición objetivo.
     * @param target posición destino
     * @return dirección hacia el objetivo
     */
    private Direction dirTo(Position target) {
        Position pos = player.getPosition();
        int dr = target.getRow() - pos.getRow();
        int dc = target.getCol() - pos.getCol();

        return Math.abs(dr) >= Math.abs(dc)
                ? (dr > 0 ? Direction.DOWN : Direction.UP)
                : (dc > 0 ? Direction.RIGHT : Direction.LEFT);
    }

    /**
     * Calcula la distancia Manhattan entre dos posiciones.
     * @param a primera posición
     * @param b segunda posición
     * @return distancia Manhattan
     */
    private int dist(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow())
             + Math.abs(a.getCol() - b.getCol());
    }

    /**
     * Calcula la posición resultante al moverse en una dirección.
     * @param p posición base
     * @param d dirección
     * @return nueva posición
     */
    private Position nextPos(Position p, Direction d) {
        return new Position(
                p.getRow() + d.getRowDelta(),
                p.getCol() + d.getColDelta()
        );
    }
}