package domain;

import java.util.*;

public class Fearful implements ControllerCream {
    private IceCream player;
    private GameMap map;
    private BadIceCream game;

    private static final int DISTANCIA_PELIGRO = 8;
    private static final int DISTANCIA_CRITICA = 4;
    private static final int DISTANCIA_MINIMA_FRUTA = 10; // Solo busca frutas si enemigos están lejos

    private int tick = 0;
    private int speed = 3; // Más rápido para reaccionar mejor

    private Position ultimaPosicion = null;
    private int ticksAtascado = 0;

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

        // Detectar si está atascado
        Position actual = player.getPosition();
        if (actual.equals(ultimaPosicion)) {
            ticksAtascado++;
            if (ticksAtascado > 8) {
                intentarDesatascar();
                ticksAtascado = 0;
            }
        } else {
            ticksAtascado = 0;
        }
        ultimaPosicion = new Position(actual.getRow(), actual.getCol());

        // PRIORIDAD 1: Peligro inmediato
        List<Enemy> enemigosCriticos = encontrarEnemigosCriticos();
        if (!enemigosCriticos.isEmpty()) {
            huirDesesperadamente(enemigosCriticos);
            return;
        }

        // PRIORIDAD 2: Enemigos cercanos
        List<Enemy> enemigosCercanos = encontrarEnemigosCercanos();
        if (!enemigosCercanos.isEmpty()) {
            esconderse(enemigosCercanos);
            return;
        }

        // PRIORIDAD 3: Buscar fruta MUY segura
        Fruit frutaSegura = encontrarFrutaMuySegura();
        if (frutaSegura != null) {
            irConCuidado(frutaSegura.getPosition());
            return;
        }

        // PRIORIDAD 4: Mantenerse alejado y patrullar
        patrullarSeguro();
    }

    // ============================================================
    //    HUIDA DESESPERADA (Peligro inmediato)
    // ============================================================

    private List<Enemy> encontrarEnemigosCriticos() {
        List<Enemy> criticos = new ArrayList<>();
        Position pos = player.getPosition();

        for (Enemy e : game.getEnemies()) {
            int dist = calcularDistancia(pos, e.getPosition());
            if (dist <= DISTANCIA_CRITICA) {
                criticos.add(e);
            }
        }
        return criticos;
    }

    private void huirDesesperadamente(List<Enemy> enemigos) {
        Position actual = player.getPosition();

        // Crear barreras defensivas inmediatamente
        for (Enemy e : enemigos) {
            if (calcularDistancia(actual, e.getPosition()) <= 2) {
                Direction dirHaciaEnemigo = calcularDireccionHacia(e.getPosition());
                if (dirHaciaEnemigo != null) {
                    Position bloquePos = new Position(
                        actual.getRow() + dirHaciaEnemigo.getRowDelta(),
                        actual.getCol() + dirHaciaEnemigo.getColDelta()
                    );
                    
                    if (map.isValid(bloquePos) && !map.hasEnemy(bloquePos) && !map.isBlocked(bloquePos)) {
                        player.createIce(dirHaciaEnemigo);
                    }
                }
            }
        }

        // Huir en la dirección MÁS alejada
        Direction dirHuida = encontrarDireccionMasAlejada(enemigos);
        if (dirHuida != null) {
            boolean movido = player.move(dirHuida);
            
            if (!movido) {
                Position siguiente = new Position(
                    actual.getRow() + dirHuida.getRowDelta(),
                    actual.getCol() + dirHuida.getColDelta()
                );
                
                if (map.isValid(siguiente) && map.isBlocked(siguiente)) {
                    player.destroyIce(dirHuida);
                    player.move(dirHuida);
                }
            }
        }
    }

    // ============================================================
    //    ESCONDERSE (Enemigos cercanos pero no inmediatos)
    // ============================================================

    private List<Enemy> encontrarEnemigosCercanos() {
        List<Enemy> cercanos = new ArrayList<>();
        Position pos = player.getPosition();

        for (Enemy e : game.getEnemies()) {
            int dist = calcularDistancia(pos, e.getPosition());
            if (dist > DISTANCIA_CRITICA && dist <= DISTANCIA_PELIGRO) {
                cercanos.add(e);
            }
        }
        return cercanos;
    }

    private void esconderse(List<Enemy> enemigos) {
        Position actual = player.getPosition();

        // Crear barreras estratégicas si enemigos están alineados
        for (Enemy e : enemigos) {
            if (estaAlineado(actual, e.getPosition())) {
                Direction dirHaciaEnemigo = calcularDireccionHacia(e.getPosition());
                if (dirHaciaEnemigo != null) {
                    Position bloquePos = new Position(
                        actual.getRow() + dirHaciaEnemigo.getRowDelta(),
                        actual.getCol() + dirHaciaEnemigo.getColDelta()
                    );
                    
                    if (map.isValid(bloquePos) && !map.hasEnemy(bloquePos) && !map.isBlocked(bloquePos)) {
                        player.createIce(dirHaciaEnemigo);
                    }
                }
            }
        }

        // Moverse hacia la posición más segura
        Direction dirSegura = encontrarDireccionMasSegura(enemigos);
        if (dirSegura != null) {
            boolean movido = player.move(dirSegura);
            
            if (!movido) {
                Position siguiente = new Position(
                    actual.getRow() + dirSegura.getRowDelta(),
                    actual.getCol() + dirSegura.getColDelta()
                );
                
                if (map.isValid(siguiente) && map.isBlocked(siguiente)) {
                    player.destroyIce(dirSegura);
                    player.move(dirSegura);
                }
            }
        }
    }

    // ============================================================
    //    BUSCAR FRUTAS MUY SEGURAS
    // ============================================================

    private Fruit encontrarFrutaMuySegura() {
        Fruit mejorFruta = null;
        int mejorScore = Integer.MIN_VALUE;
        Position actual = player.getPosition();

        for (Fruit f : game.getFruits()) {
            if (f.isEaten() || !f.isActive()) continue;

            // Verificar que TODOS los enemigos estén lejos de la fruta
            int distanciaEnemigoCercano = Integer.MAX_VALUE;
            for (Enemy e : game.getEnemies()) {
                int distEnemigo = calcularDistancia(f.getPosition(), e.getPosition());
                distanciaEnemigoCercano = Math.min(distanciaEnemigoCercano, distEnemigo);
            }

            // Solo considerar frutas si enemigos están MUY lejos
            if (distanciaEnemigoCercano < DISTANCIA_MINIMA_FRUTA) continue;

            // Evaluar qué tan seguro es el camino
            int distanciaJugador = calcularDistancia(actual, f.getPosition());
            int peligroCamino = evaluarPeligroCamino(actual, f.getPosition());

            // Score: priorizar cercanía y seguridad del camino
            int score = (50 - distanciaJugador) - (peligroCamino * 10) + (distanciaEnemigoCercano * 2);

            if (score > mejorScore) {
                mejorScore = score;
                mejorFruta = f;
            }
        }

        // Solo retornar si es REALMENTE seguro
        if (mejorFruta != null && mejorScore < 0) {
            return null;
        }

        return mejorFruta;
    }

    private int evaluarPeligroCamino(Position origen, Position destino) {
        int peligro = 0;
        
        // Evaluar posiciones intermedias aproximadas
        int pasos = Math.max(Math.abs(destino.getRow() - origen.getRow()), 
                           Math.abs(destino.getCol() - origen.getCol()));
        
        for (int i = 0; i <= pasos; i++) {
            float t = pasos > 0 ? (float)i / pasos : 0;
            int r = (int)(origen.getRow() + t * (destino.getRow() - origen.getRow()));
            int c = (int)(origen.getCol() + t * (destino.getCol() - origen.getCol()));
            Position pos = new Position(r, c);
            
            for (Enemy e : game.getEnemies()) {
                int dist = calcularDistancia(pos, e.getPosition());
                if (dist <= 3) peligro += (4 - dist);
            }
        }
        
        return peligro;
    }

    // ============================================================
    //    IR CON CUIDADO HACIA FRUTA
    // ============================================================

    private void irConCuidado(Position destino) {
        Position actual = player.getPosition();

        // Verificar que sigue siendo seguro
        for (Enemy e : game.getEnemies()) {
            if (calcularDistancia(destino, e.getPosition()) < DISTANCIA_PELIGRO) {
                patrullarSeguro(); // Abortar misión
                return;
            }
        }

        Direction dir = calcularDireccionHacia(destino);
        if (dir == null) return;

        Position siguiente = new Position(
            actual.getRow() + dir.getRowDelta(),
            actual.getCol() + dir.getColDelta()
        );

        // Verificar que el siguiente paso sea seguro
        int peligroSiguiente = 0;
        for (Enemy e : game.getEnemies()) {
            int dist = calcularDistancia(siguiente, e.getPosition());
            if (dist < DISTANCIA_PELIGRO) {
                peligroSiguiente += (DISTANCIA_PELIGRO - dist);
            }
        }

        if (peligroSiguiente > 10) {
            patrullarSeguro(); // Demasiado peligroso
            return;
        }

        boolean movido = player.move(dir);
        if (!movido && map.isValid(siguiente) && map.isBlocked(siguiente)) {
            player.destroyIce(dir);
            player.move(dir);
        }
    }

    // ============================================================
    //    PATRULLAR SEGURO (sin objetivo claro)
    // ============================================================

    private void patrullarSeguro() {
        List<Enemy> enemigos = new ArrayList<>(game.getEnemies());
        Direction dirSegura = encontrarDireccionMasSegura(enemigos);
        
        if (dirSegura != null) {
            boolean movido = player.move(dirSegura);
            
            if (!movido) {
                Position siguiente = new Position(
                    player.getPosition().getRow() + dirSegura.getRowDelta(),
                    player.getPosition().getCol() + dirSegura.getColDelta()
                );
                
                if (map.isValid(siguiente) && map.isBlocked(siguiente)) {
                    player.destroyIce(dirSegura);
                }
            }
        }
    }

    // ============================================================
    //    CÁLCULOS DE DIRECCIONES SEGURAS
    // ============================================================

    private Direction encontrarDireccionMasAlejada(List<Enemy> enemigos) {
        Direction mejor = null;
        int mejorDistanciaTotal = -1;
        Position actual = player.getPosition();

        for (Direction d : Direction.values()) {
            Position siguiente = new Position(
                actual.getRow() + d.getRowDelta(),
                actual.getCol() + d.getColDelta()
            );

            if (!map.isValid(siguiente)) continue;
            if (map.hasEnemy(siguiente)) continue;

            // Calcular distancia total a TODOS los enemigos
            int distanciaTotal = 0;
            for (Enemy e : enemigos) {
                distanciaTotal += calcularDistancia(siguiente, e.getPosition());
            }

            if (distanciaTotal > mejorDistanciaTotal) {
                mejorDistanciaTotal = distanciaTotal;
                mejor = d;
            }
        }

        return mejor;
    }

    private Direction encontrarDireccionMasSegura(List<Enemy> enemigos) {
        Direction mejor = null;
        int menorPeligro = Integer.MAX_VALUE;
        Position actual = player.getPosition();

        for (Direction d : Direction.values()) {
            Position siguiente = new Position(
                actual.getRow() + d.getRowDelta(),
                actual.getCol() + d.getColDelta()
            );

            if (!map.isValid(siguiente)) continue;
            if (map.hasEnemy(siguiente)) continue;

            int peligro = calcularPeligroPosicion(siguiente, enemigos);

            if (peligro < menorPeligro) {
                menorPeligro = peligro;
                mejor = d;
            }
        }

        return mejor;
    }

    private int calcularPeligroPosicion(Position pos, List<Enemy> enemigos) {
        if (map.isBlocked(pos)) return 9999;
        
        int peligro = 0;

        for (Enemy e : enemigos) {
            int dist = calcularDistancia(pos, e.getPosition());
            
            if (dist == 0) peligro += 10000;
            else if (dist == 1) peligro += 5000;
            else if (dist == 2) peligro += 1000;
            else if (dist == 3) peligro += 500;
            else if (dist <= 5) peligro += 100;
            else if (dist <= 8) peligro += 20;
            
            // Penalizar alineamiento
            if (estaAlineado(pos, e.getPosition())) {
                peligro += 200 / Math.max(dist, 1);
            }
        }

        return peligro;
    }

    // ============================================================
    //    DESATASCAR
    // ============================================================

    private void intentarDesatascar() {
        // Intentar destruir hielo en todas direcciones
        for (Direction dir : Direction.values()) {
            Position pos = new Position(
                player.getPosition().getRow() + dir.getRowDelta(),
                player.getPosition().getCol() + dir.getColDelta()
            );
            
            if (map.isValid(pos) && map.isBlocked(pos)) {
                player.destroyIce(dir);
                player.move(dir);
                return;
            }
        }
    }

    // ============================================================
    //    UTILIDADES
    // ============================================================

    private boolean estaAlineado(Position a, Position b) {
        return a.getRow() == b.getRow() || a.getCol() == b.getCol();
    }

    private Direction calcularDireccionHacia(Position destino) {
        Position actual = player.getPosition();
        int dr = destino.getRow() - actual.getRow();
        int dc = destino.getCol() - actual.getCol();

        if (Math.abs(dr) >= Math.abs(dc)) {
            return dr > 0 ? Direction.DOWN : Direction.UP;
        } else {
            return dc > 0 ? Direction.RIGHT : Direction.LEFT;
        }
    }

    private int calcularDistancia(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
}