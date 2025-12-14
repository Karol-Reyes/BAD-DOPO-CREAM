package domain;

import java.util.*;

public class Expert implements ControllerCream {

    private IceCream player;
    private GameMap map;
    private BadIceCream game;

    private static final int DIST_CRITICA = 3;
    private static final int DIST_PELIGRO = 7;

    private int tick = 0;
    private int speed = 2;

    private Position ultimaPos = null;
    private int ticksAtascado = 0;

    public Expert(GameMap map, BadIceCream game) {
        this.map = map;
        this.game = game;
    }

    @Override
    public void setPlayer(IceCream player) {
        this.player = player;
    }

    @Override
    public void update() {

        if (player == null || !player.isAlive()) return;

        Position pos = player.getPosition();

        /* ==================================================
           🔥 REFLEJO PURO: HIELO INMEDIATO (SIN PENSAR)
           ================================================== */

        for (Enemy e : game.getEnemies()) {
            if (dist(pos, e.getPosition()) <= DIST_CRITICA
                    && alineado(pos, e.getPosition())) {

                Direction d = dirHacia(e.getPosition());

                // HIELO DIRECTO, SIN CONDICIONES
                player.createIce(d);
                player.createIce(d);
            }
        }

        /* ================= CONTROL DE MOVIMIENTO ================= */

        tick++;
        boolean puedeMover = tick >= speed;
        if (puedeMover) tick = 0;

        /* ================= ANTIBUCLES ================= */

        ticksAtascado = pos.equals(ultimaPos) ? ticksAtascado + 1 : 0;
        if (ticksAtascado > 3) desatascar();
        ultimaPos = new Position(pos.getRow(), pos.getCol());

        if (!puedeMover) return;

        /* ================= DECISIÓN ================= */

        Enemy cercano = enemigoCercano();

        if (cercano != null) {
            int d = dist(pos, cercano.getPosition());

            if (d <= DIST_CRITICA || peligro(pos) > 1200) {
                huir(cercano);
                return;
            }
        }

        Fruit fruta = mejorFruta();
        if (fruta != null) {
            irA(fruta.getPosition());
            return;
        }

        explorar();
    }

    /* ================= ENEMIGOS ================= */

    private Enemy enemigoCercano() {
        Position pos = player.getPosition();
        Enemy mejor = null;
        int min = DIST_PELIGRO + 1;

        for (Enemy e : game.getEnemies()) {
            int d = dist(pos, e.getPosition());
            if (d < min) {
                min = d;
                mejor = e;
            }
        }
        return min <= DIST_PELIGRO ? mejor : null;
    }

    private void huir(Enemy e) {
        Position pos = player.getPosition();

        if (alineado(pos, e.getPosition())) {
            player.createIce(dirHacia(e.getPosition())); // refuerzo
        }

        mover(dirSegura());
    }

    /* ================= FRUTAS ================= */

    private Fruit mejorFruta() {
        Position pos = player.getPosition();
        Fruit mejor = null;
        int mejorScore = Integer.MIN_VALUE;

        for (Fruit f : game.getFruits()) {
            if (!f.isActive() || f.isEaten()) continue;

            int d = dist(pos, f.getPosition());
            int p = peligro(f.getPosition());

            int score = 200 - d * 4 - (p / 15);
            if (d <= 3) score += 80;
            if (p < 300) score += 60;

            if (score > mejorScore && p < 2500) {
                mejorScore = score;
                mejor = f;
            }
        }
        return mejor;
    }

    private void irA(Position dest) {
        Direction d = dirOptima(player.getPosition(), dest);
        mover(d);
    }

    /* ================= MOVIMIENTO ================= */

    private void explorar() {
        mover(dirSegura());
    }

    private boolean mover(Direction dir) {
        if (dir == null) return false;

        Position next = nueva(player.getPosition(), dir);
        if (!map.isValid(next) || map.hasEnemy(next)) return false;

        Boxy b = map.getBlock(next);
        if (b != null && b.getType() == BoxType.bonfire && b.getState() == BoxState.on)
            return false;

        boolean ok = player.move(dir);

        if (!ok && map.isValid(next) && map.isBlocked(next)) {
            player.destroyIce(dir);
            ok = player.move(dir);
        }
        return ok;
    }

    /* ================= ANTIBLOQUEO ================= */

    private void desatascar() {
        for (Direction d : Direction.values()) {
            Position p = nueva(player.getPosition(), d);
            if (map.isValid(p) && map.isBlocked(p)) {
                player.destroyIce(d);
            }
        }
        mover(dirSegura());
        ticksAtascado = 0;
    }

    /* ================= IA ================= */

    private Direction dirSegura() {
        return mejorDir(Direction.values());
    }

    private Direction mejorDir(Direction[] dirs) {
        Direction mejor = null;
        int menor = Integer.MAX_VALUE;

        for (Direction d : dirs) {
            Position p = nueva(player.getPosition(), d);
            int pel = peligro(p);

            if (pel < menor) {
                menor = pel;
                mejor = d;
            }
        }
        return mejor;
    }

    private int peligro(Position p) {
        if (!map.isValid(p)) return 9999;
        if (map.hasEnemy(p)) return 10000;
        if (map.isBlocked(p)) return 80;

        int nivel = 0;
        for (Enemy e : game.getEnemies()) {
            int d = dist(p, e.getPosition());
            if (d == 1) nivel += 3000;
            else if (d == 2) nivel += 1200;
            else if (d == 3) nivel += 400;
        }
        return nivel;
    }

    private Direction dirOptima(Position o, Position d) {
        int dr = d.getRow() - o.getRow();
        int dc = d.getCol() - o.getCol();

        if (Math.abs(dr) > Math.abs(dc))
            return dr > 0 ? Direction.DOWN : Direction.UP;
        if (dc != 0)
            return dc > 0 ? Direction.RIGHT : Direction.LEFT;
        return null;
    }

    /* ================= UTILS ================= */

    private Position nueva(Position p, Direction d) {
        return new Position(
                p.getRow() + d.getRowDelta(),
                p.getCol() + d.getColDelta()
        );
    }

    private Direction dirHacia(Position dest) {
        Position pos = player.getPosition();
        int dr = dest.getRow() - pos.getRow();
        int dc = dest.getCol() - pos.getCol();

        return Math.abs(dr) >= Math.abs(dc)
                ? (dr > 0 ? Direction.DOWN : Direction.UP)
                : (dc > 0 ? Direction.RIGHT : Direction.LEFT);
    }

    private boolean alineado(Position a, Position b) {
        return a.getRow() == b.getRow() || a.getCol() == b.getCol();
    }

    private int dist(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow())
                + Math.abs(a.getCol() - b.getCol());
    }
}