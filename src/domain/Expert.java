package domain;
/**
 * Controlador experto para el jugador IceCream.
 * Implementa una IA avanzada para la toma de decisiones en el juego.
 */
public class Expert implements ControllerCream {

    private IceCream player;
    private final GameMap map;
    private final BadIceCream game;

    private static final int CRIT_DIST = 3;
    private static final int DANGER_DIST = 7;

    private int tick;
    private final int speed = 2;

    private Position lastPos;
    private int stuckTicks;

    /**
     * Crea un controlador experto con acceso al mapa y al juego.
     * @param map mapa del juego
     * @param game instancia principal del juego
     */
    public Expert(GameMap map, BadIceCream game) {
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
     * Ejecuta la lógica principal de decisión del controlador experto.
     */
    @Override
    public void update() {
        if (player == null || !player.isAlive()) return;

        Position pos = player.getPosition();

        for (Enemy e : game.getEnemies()) {
            if (dist(pos, e.getPosition()) <= CRIT_DIST &&
                aligned(pos, e.getPosition())) {

                Direction d = dirTo(e.getPosition());
                player.createIce(d);
                player.createIce(d);
            }
        }

        tick++;
        boolean canMove = tick >= speed;
        if (canMove) tick = 0;

        stuckTicks = pos.equals(lastPos) ? stuckTicks + 1 : 0;
        if (stuckTicks > 3) unstuck();
        lastPos = new Position(pos.getRow(), pos.getCol());

        if (!canMove) return;

        Enemy near = closestEnemy();
        if (near != null) {
            int d = dist(pos, near.getPosition());
            if (d <= CRIT_DIST || danger(pos) > 1200) {
                flee(near);
                return;
            }
        }

        Fruit fruit = bestFruit();
        if (fruit != null) {
            goTo(fruit.getPosition());
            return;
        }
        explore();
    }

    /**
     * Obtiene el enemigo más cercano dentro del rango de peligro.
     * @return enemigo cercano o null si no hay amenaza
     */
    private Enemy closestEnemy() {
        Position pos = player.getPosition();
        Enemy best = null;
        int min = DANGER_DIST + 1;

        for (Enemy e : game.getEnemies()) {
            int d = dist(pos, e.getPosition());
            if (d < min) {
                min = d;
                best = e;
            }
        }
        return min <= DANGER_DIST ? best : null;
    }

    /**
     * Ejecuta una maniobra de huida respecto a un enemigo.
     * @param e enemigo del que se huye
     */
    private void flee(Enemy e) {
        Position pos = player.getPosition();

        if (aligned(pos, e.getPosition())) {
            player.createIce(dirTo(e.getPosition()));
        }
        move(safeDir());
    }

    /**
     * Selecciona la fruta con mayor beneficio y menor riesgo.
     * @return fruta óptima o null si no conviene recoger ninguna
     */
    private Fruit bestFruit() {
        Position pos = player.getPosition();
        Fruit best = null;
        int bestScore = Integer.MIN_VALUE;

        for (Fruit f : game.getFruits()) {
            if (!f.isActive() || f.isEaten()) continue;

            int d = dist(pos, f.getPosition());
            int risk = danger(f.getPosition());

            int score = 200 - d * 4 - (risk / 15);
            if (d <= 3) score += 80;
            if (risk < 300) score += 60;

            if (score > bestScore && risk < 2500) {
                bestScore = score;
                best = f;
            }
        }
        return best;
    }

    /**
     * Desplaza al jugador hacia una posición objetivo.
     * @param target posición destino
     */
    private void goTo(Position target) {
        move(bestDir(player.getPosition(), target));
    }

    /**
     * Realiza un movimiento exploratorio seguro.
     */
    private void explore() {
        move(safeDir());
    }

    /**
     * Ejecuta un movimiento en una dirección válida.
     * @param dir dirección de movimiento
     * @return true si el movimiento fue exitoso
     */
    private boolean move(Direction dir) {
        if (dir == null) return false;

        Position next = nextPos(player.getPosition(), dir);
        if (!map.isValid(next) || map.hasEnemy(next)) return false;

        Boxy box = map.getBlock(next);
        if (box != null &&
            box.getType() == BoxType.bonfire &&
            box.getState() == BoxState.on) return false;

        boolean ok = player.move(dir);

        if (!ok && map.isValid(next) && map.isBlocked(next)) {
            player.destroyIce(dir);
            ok = player.move(dir);
        }
        return ok;
    }

    /**
     * Libera al jugador cuando queda bloqueado repetidamente.
     */
    private void unstuck() {
        for (Direction d : Direction.values()) {
            Position p = nextPos(player.getPosition(), d);
            if (map.isValid(p) && map.isBlocked(p)) {
                player.destroyIce(d);
            }
        }
        move(safeDir());
        stuckTicks = 0;
    }

    /**
     * Obtiene la dirección con menor nivel de peligro.
     * @return dirección más segura
     */
    private Direction safeDir() {
        return bestDir(Direction.values());
    }

    /**
     * Selecciona la dirección con menor riesgo.
     * @param dirs direcciones evaluadas
     * @return mejor dirección
     */
    private Direction bestDir(Direction[] dirs) {
        Direction best = null;
        int min = Integer.MAX_VALUE;

        for (Direction d : dirs) {
            Position p = nextPos(player.getPosition(), d);
            int risk = danger(p);

            if (risk < min) {
                min = risk;
                best = d;
            }
        }
        return best;
    }

    /**
     * Calcula el nivel de peligro de una posición.
     * @param p posición evaluada
     * @return valor numérico de peligro
     */
    private int danger(Position p) {
        if (!map.isValid(p)) return 9999;
        if (map.hasEnemy(p)) return 10000;
        if (map.isBlocked(p)) return 80;

        int level = 0;
        for (Enemy e : game.getEnemies()) {
            int d = dist(p, e.getPosition());
            switch (d) {
                case 1 -> level += 3000;
                case 2 -> level += 1200;
                case 3 -> level += 400;
                default -> {
                }
            }
        }
        return level;
    }

    /**
     * Obtiene la mejor dirección para acercarse a un objetivo.
     * @param from posición origen
     * @param to posición destino
     * @return dirección óptima
     */
    private Direction bestDir(Position from, Position to) {
        int dr = to.getRow() - from.getRow();
        int dc = to.getCol() - from.getCol();

        if (Math.abs(dr) > Math.abs(dc))
            return dr > 0 ? Direction.DOWN : Direction.UP;
        if (dc != 0)
            return dc > 0 ? Direction.RIGHT : Direction.LEFT;
        return null;
    }

    /**
     * Calcula la siguiente posición en una dirección.
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

    /**
     * Obtiene la dirección directa hacia una posición.
     * @param target posición objetivo
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
     * Indica si dos posiciones están alineadas en fila o columna.
     * @param a primera posición
     * @param b segunda posición
     * @return true si están alineadas
     */
    private boolean aligned(Position a, Position b) {
        return a.getRow() == b.getRow() || a.getCol() == b.getCol();
    }

    /**
     * Calcula la distancia Manhattan entre dos posiciones.
     * @param a primera posición
     * @param b segunda posición
     * @return distancia Manhattan
     */
    private int dist(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
}