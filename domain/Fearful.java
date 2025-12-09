package domain;

import java.util.*;

public class Fearful implements ControllerCream {
    private IceCream player;
    private GameMap map;
    private BadIceCream game;

    private static final int DISTANCIA_PELIGRO = 6;

    private int tick = 0;
    private int speed = 5;

    public Fearful(GameMap map, BadIceCream game) {
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

        Enemy enemigoCercano = encontrarEnemigoCercano();

        if (enemigoCercano != null) {
            huir(enemigoCercano);
        } else {
            buscarFrutaSegura();
        }
    }

    // ============================================================
    //    LÓGICA DE HUIDA INTELIGENTE
    // ============================================================

    private void huir(Enemy enemigo) {
        Position enemyPos = enemigo.getPosition();
        Position actual = player.getPosition();

        // 1. evaluar todas las direcciones posibles
        Direction mejor = null;
        int mejorDistancia = -1;

        for (Direction d : Direction.values()) {
            Position futuro = actual.translated(d);

            // verificar que esté dentro del mapa
            if (!map.isValid(futuro)) continue;

            // distancia que tendré después de moverme
            int distancia = calcularDistancia(futuro, enemyPos);

            // elegir la mejor (la que aleja más del enemigo)
            if (distancia > mejorDistancia) {
                mejorDistancia = distancia;
                mejor = d;
            }
        }

        if (mejor == null) return;

        // 2. intentar huir hacia la mejor dirección
        boolean movido = player.move(mejor);

        if (!movido) {
            // 3. si no puede, intenta crear hielo para bloquear al enemigo
            player.createIce(calcularDireccionHacia(enemyPos));

            // 4. si sigue atrapado, intenta destruir hielo para abrir paso
            player.destroyIce(mejor);
        }
    }

    // ============================================================
    //    FRUTAS SEGURAS
    // ============================================================

    private void buscarFrutaSegura() {
        for (Fruit f : game.getFruits()) {
            if (f.isEaten()) continue;

            if (!esFrutaSegura(f)) continue;

            Direction dir = calcularDireccionHacia(f.getPosition());
            if (dir != null) {
                player.move(dir);
                return;
            }
        }
    }

    private boolean esFrutaSegura(Fruit fruta) {
        for (Enemy e : game.getEnemies()) {
            if (calcularDistancia(fruta.getPosition(), e.getPosition()) < DISTANCIA_PELIGRO)
                return false;
        }
        return true;
    }

    // ============================================================
    //    UTILIDADES
    // ============================================================

    private Enemy encontrarEnemigoCercano() {
        Enemy cercano = null;
        int menorDist = DISTANCIA_PELIGRO + 1;

        for (Enemy e : game.getEnemies()) {
            int d = calcularDistancia(player.getPosition(), e.getPosition());
            if (d <= DISTANCIA_PELIGRO && d < menorDist) {
                menorDist = d;
                cercano = e;
            }
        }
        return cercano;
    }

    private Direction calcularDireccionHacia(Position destino) {
        Position actual = player.getPosition();
        int dr = destino.getRow() - actual.getRow();
        int dc = destino.getCol() - actual.getCol();

        if (Math.abs(dr) > Math.abs(dc))
            return dr > 0 ? Direction.DOWN : Direction.UP;
        if (dc != 0)
            return dc > 0 ? Direction.RIGHT : Direction.LEFT;
        return null;
    }

    private int calcularDistancia(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
}