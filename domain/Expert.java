package domain;

import java.util.*;

public class Expert implements ControllerCream {
    private IceCream player;
    private GameMap map;
    private BadIceCream game;

    private static final int DISTANCIA_PELIGRO = 6;

    private int tick = 0;
    private int speed = 5;

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

        tick++;
        if (tick < speed) return;
        tick = 0;

        if (player == null || !player.isAlive()) return;

        Enemy peligro = enemigoMasCercano();
        Fruit fruta = frutaMejorPuntuada();

        // Prioridad 1: sobrevivir
        if (peligro != null) {
            huirInteligente(peligro);
            return;
        }

        // Prioridad 2: recolectar frutas
        if (fruta != null) {
            irHacia(fruta.getPosition());
            return;
        }

        // Prioridad 3: explorar
        explorar();
    }

    // ------------------------
    // ENEMIGOS
    // ------------------------

    private Enemy enemigoMasCercano() {
        Enemy cercano = null;
        int min = Integer.MAX_VALUE;

        for (Enemy e : game.getEnemies()) {
            int dist = distancia(player.getPosition(), e.getPosition());
            if (dist < min && dist <= DISTANCIA_PELIGRO) {
                min = dist;
                cercano = e;
            }
        }
        return cercano;
    }

    // ------------------------
    // FRUTAS
    // ------------------------

    private Fruit frutaMejorPuntuada() {
        Fruit mejor = null;
        int mejorScore = Integer.MIN_VALUE;

        for (Fruit f : game.getFruits()) {
            if (f.isEaten()) continue;

            int dist = distancia(player.getPosition(), f.getPosition());
            int peligro = evaluarPeligro(f.getPosition());

            int score = (50 - dist) - (peligro * 25);
            if (score > mejorScore) {
                mejorScore = score;
                mejor = f;
            }
        }
        return mejor;
    }

    private int evaluarPeligro(Position pos) {
        int nivel = 0;
        for (Enemy e : game.getEnemies()) {
            int d = distancia(pos, e.getPosition());
            if (d <= 2) nivel += 2;
            else if (d <= 4) nivel += 1;
        }
        return nivel;
    }

    // ------------------------
    // MOVIMIENTO INTELIGENTE
    // ------------------------

    private void huirInteligente(Enemy e) {

        Direction haciaEnemigo = direccionHacia(e.getPosition());
        Direction opuesta = opuesta(haciaEnemigo);

        // 1. Crear barrera si el enemigo está muy cerca
        if (distancia(player.getPosition(), e.getPosition()) <= 2) {
            if (haciaEnemigo != null) {
                player.createIce(haciaEnemigo);
            }
        }

        // 2. Intentar huir en la dirección opuesta
        if (opuesta != null && player.move(opuesta)) return;

        // 3. Intentar alternativas laterales
        for (Direction d : direccionesAleatorias()) {
            if (player.move(d)) return;
        }

        // 4. Si no puede moverse, destruir o crear hielo
        if (opuesta != null) {
            player.destroyIce(opuesta);
            player.createIce(opuesta);
        }
    }

    private void irHacia(Position destino) {
        Direction dir = direccionHacia(destino);
        if (dir == null) return;

        boolean movido = player.move(dir);

        if (!movido) {
            // Si hay un bloque, intentar abrir camino
            Position frente = new Position(
                player.getPosition().getRow() + dir.getRowDelta(),
                player.getPosition().getCol() + dir.getColDelta()
            );

            if (map.isValid(frente)) {
                if (map.isBlocked(frente)) player.destroyIce(dir);
                else player.createIce(dir);
            }
        }
    }

    private void explorar() {
        for (Direction d : direccionesAleatorias()) {
            if (player.move(d)) return;
        }
    }

    // ------------------------
    // UTILIDADES
    // ------------------------

    private Direction direccionHacia(Position destino) {
        Position actual = player.getPosition();
        int dr = destino.getRow() - actual.getRow();
        int dc = destino.getCol() - actual.getCol();

        if (Math.abs(dr) >= Math.abs(dc)) {
            return dr > 0 ? Direction.DOWN : Direction.UP;
        } else {
            return dc > 0 ? Direction.RIGHT : Direction.LEFT;
        }
    }

    private Direction opuesta(Direction d) {
        if (d == null) return null;
        switch (d) {
            case UP: return Direction.DOWN;
            case DOWN: return Direction.UP;
            case LEFT: return Direction.RIGHT;
            case RIGHT: return Direction.LEFT;
        }
        return null;
    }

    private int distancia(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }

    private Direction[] direccionesAleatorias() {
        Direction[] dirs = Direction.values();
        Collections.shuffle(Arrays.asList(dirs));
        return dirs;
    }
}