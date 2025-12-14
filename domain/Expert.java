package domain;

import java.util.*;

public class Expert implements ControllerCream {
    private IceCream player;
    private GameMap map;
    private BadIceCream game;

    private static final int DISTANCIA_PELIGRO = 8;
    private static final int DISTANCIA_CRITICA = 4;

    private int tick = 0;
    private int speed = 2; // Más rápido para reaccionar mejor

    private Position ultimoDestino = null;
    private int ticksSinAvance = 0;
    private static final int MAX_TICKS_ATASCADO = 5; // Reducido para reaccionar más rápido

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

        // Detectar si está atascado
        Position actual = player.getPosition();
        if (actual.equals(ultimoDestino)) {
            ticksSinAvance++;
            if (ticksSinAvance >= MAX_TICKS_ATASCADO) {
                desbloquearAgresivo();
                ticksSinAvance = 0;
            }
        } else {
            ticksSinAvance = 0;
        }
        ultimoDestino = new Position(actual.getRow(), actual.getCol());

        // 1. PRIORIDAD MÁXIMA: Peligro inminente
        Enemy enemigoInminente = detectarPeligroInmediato();
        if (enemigoInminente != null) {
            reaccionEmergencia(enemigoInminente);
            return;
        }

        // 2. Enemigos cercanos
        List<Enemy> peligros = enemigosCercanos();
        if (!peligros.isEmpty()) {
            huirInteligente(peligros);
            return;
        }

        // 3. Buscar fruta segura
        Fruit fruta = frutaMejorPuntuada();
        if (fruta != null) {
            irHaciaConSeguridad(fruta.getPosition());
            return;
        }

        // 4. Explorar de forma segura
        explorarSeguro();
    }

    private Enemy detectarPeligroInmediato() {
        Position pos = player.getPosition();
        Enemy masCercano = null;
        int menorDist = Integer.MAX_VALUE;
        
        for (Enemy e : game.getEnemies()) {
            int dist = distancia(pos, e.getPosition());
            
            if (dist <= DISTANCIA_CRITICA && dist < menorDist) {
                menorDist = dist;
                masCercano = e;
            }
        }
        return masCercano;
    }

    private void reaccionEmergencia(Enemy enemigo) {
        Position posJugador = player.getPosition();
        Position posEnemigo = enemigo.getPosition();
        
        // Si está alineado, crear barrera
        boolean mismaFila = posJugador.getRow() == posEnemigo.getRow();
        boolean mismaColumna = posJugador.getCol() == posEnemigo.getCol();
        
        if (mismaFila || mismaColumna) {
            Direction dirHaciaEnemigo = direccionHacia(posEnemigo);
            if (dirHaciaEnemigo != null) {
                Position bloquePos = new Position(
                    posJugador.getRow() + dirHaciaEnemigo.getRowDelta(),
                    posJugador.getCol() + dirHaciaEnemigo.getColDelta()
                );
                
                if (map.isValid(bloquePos) && !map.hasEnemy(bloquePos) && !map.isBlocked(bloquePos)) {
                    player.createIce(dirHaciaEnemigo);
                }
            }
        }
        
        // Huir en la dirección MÁS segura, destruyendo hielo si es necesario
        Direction dirSegura = direccionMasSegura();
        if (dirSegura != null) {
            if (!moverConDestruccion(dirSegura)) {
                // Si no pudo moverse, intentar otra dirección
                for (Direction dir : Direction.values()) {
                    if (moverConDestruccion(dir)) {
                        return;
                    }
                }
            }
        }
    }

    private List<Enemy> enemigosCercanos() {
        List<Enemy> peligros = new ArrayList<>();
        Position pos = player.getPosition();

        for (Enemy e : game.getEnemies()) {
            int dist = distancia(pos, e.getPosition());
            if (dist <= DISTANCIA_PELIGRO) {
                peligros.add(e);
            }
        }
        return peligros;
    }

    private int calcularPeligroPosicion(Position pos) {
        if (!map.isValid(pos)) return 9999;
        if (map.hasEnemy(pos)) return 10000;
        
        // Los bloques de hielo ya no son tan penalizantes
        if (map.isBlocked(pos)) return 100; // Reducido de 8888

        int nivelPeligro = 0;

        for (Enemy e : game.getEnemies()) {
            int dist = distancia(pos, e.getPosition());
            
            if (dist == 0) nivelPeligro += 10000;
            else if (dist == 1) nivelPeligro += 5000;
            else if (dist == 2) nivelPeligro += 1000;
            else if (dist == 3) nivelPeligro += 500;
            else if (dist == 4) nivelPeligro += 200;
            else if (dist <= 6) nivelPeligro += 50;
            else if (dist <= DISTANCIA_PELIGRO) nivelPeligro += 10;
            
            if (estaAlineado(pos, e.getPosition())) {
                nivelPeligro += 300 / Math.max(dist, 1);
            }
        }

        return nivelPeligro;
    }

    private boolean estaAlineado(Position a, Position b) {
        return a.getRow() == b.getRow() || a.getCol() == b.getCol();
    }

    private Direction direccionMasSegura() {
        Direction mejorDir = null;
        int menorPeligro = Integer.MAX_VALUE;
        Position actual = player.getPosition();

        for (Direction dir : Direction.values()) {
            Position siguiente = new Position(
                actual.getRow() + dir.getRowDelta(),
                actual.getCol() + dir.getColDelta()
            );

            int peligro = calcularPeligroPosicion(siguiente);

            if (peligro < menorPeligro) {
                menorPeligro = peligro;
                mejorDir = dir;
            }
        }

        return mejorDir;
    }

    private void huirInteligente(List<Enemy> enemigos) {
        Position actual = player.getPosition();

        // Crear barreras contra enemigos muy cercanos y alineados
        for (Enemy e : enemigos) {
            int dist = distancia(actual, e.getPosition());
            if (dist <= 3 && estaAlineado(actual, e.getPosition())) {
                Direction dirHaciaEnemigo = direccionHacia(e.getPosition());
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

        // Moverse a lugar seguro, destruyendo hielo si es necesario
        Direction dirSegura = direccionMasSegura();
        if (dirSegura != null) {
            moverConDestruccion(dirSegura);
        }
    }
    
    private Fruit frutaMejorPuntuada() {
        Fruit mejor = null;
        int mejorScore = Integer.MIN_VALUE;
        Position actual = player.getPosition();

        for (Fruit f : game.getFruits()) {
            if (f.isEaten() || !f.isActive()) continue;

            int dist = distancia(actual, f.getPosition());
            int peligro = calcularPeligroPosicion(f.getPosition());

            // Score: priorizar cercanía y seguridad
            int score = (100 - dist * 2) - (peligro / 10);
            
            if (peligro < 100) score += 50;
            if (dist < 5) score += 30;
            
            if (score > mejorScore) {
                mejorScore = score;
                mejor = f;
            }
        }
        
        // Umbral de seguridad reducido
        if (mejor != null) {
            int peligroFruta = calcularPeligroPosicion(mejor.getPosition());
            if (peligroFruta > 1000) return null; // Aumentado de 500 para ser más tolerante
        }
        
        return mejor;
    }

    private void irHaciaConSeguridad(Position destino) {
        Position actual = player.getPosition();
        
        int peligroDestino = calcularPeligroPosicion(destino);
        if (peligroDestino > 1000) {
            explorarSeguro();
            return;
        }

        Direction dirOptima = direccionOptima(actual, destino);
        if (dirOptima == null) {
            explorarSeguro();
            return;
        }

        Position siguiente = new Position(
            actual.getRow() + dirOptima.getRowDelta(),
            actual.getCol() + dirOptima.getColDelta()
        );

        int peligroSiguiente = calcularPeligroPosicion(siguiente);

        // Si es peligroso, buscar alternativa
        if (peligroSiguiente > 500) {
            Direction segura = direccionMasSegura();
            if (segura != null) {
                dirOptima = segura;
            }
        }

        // Moverse destruyendo hielo si es necesario
        moverConDestruccion(dirOptima);
    }

    /**
     * Intenta moverse en una dirección, destruyendo hielo automáticamente si bloquea
     */
    private boolean moverConDestruccion(Direction dir) {
        if (dir == null) return false;
        
        Position siguiente = new Position(
            player.getPosition().getRow() + dir.getRowDelta(),
            player.getPosition().getCol() + dir.getColDelta()
        );
        
        // Verificar si hay enemigo (no destruir si hay enemigo)
        if (map.isValid(siguiente) && map.hasEnemy(siguiente)) {
            return false;
        }
        
        // Intentar moverse
        boolean movido = player.move(dir);
        
        if (!movido && map.isValid(siguiente)) {
            // Si hay hielo, destruirlo
            if (map.isBlocked(siguiente)) {
                player.destroyIce(dir);
                // Intentar moverse de nuevo después de destruir
                return player.move(dir);
            }
        }
        
        return movido;
    }

    private Direction direccionOptima(Position origen, Position destino) {
        int dr = destino.getRow() - origen.getRow();
        int dc = destino.getCol() - origen.getCol();

        Direction[] opciones;
        
        if (Math.abs(dr) > Math.abs(dc)) {
            Direction vertical = dr > 0 ? Direction.DOWN : Direction.UP;
            Direction horizontal = dc > 0 ? Direction.RIGHT : Direction.LEFT;
            opciones = new Direction[]{vertical, horizontal, horizontal.getOpposite(), vertical.getOpposite()};
        } else if (dc != 0) {
            Direction horizontal = dc > 0 ? Direction.RIGHT : Direction.LEFT;
            Direction vertical = dr > 0 ? Direction.DOWN : Direction.UP;
            opciones = new Direction[]{horizontal, vertical, vertical.getOpposite(), horizontal.getOpposite()};
        } else {
            return null;
        }

        Direction mejor = null;
        int menorPeligro = Integer.MAX_VALUE;

        for (Direction dir : opciones) {
            Position pos = new Position(
                origen.getRow() + dir.getRowDelta(),
                origen.getCol() + dir.getColDelta()
            );
            
            int peligro = calcularPeligroPosicion(pos);
            if (peligro < menorPeligro) {
                menorPeligro = peligro;
                mejor = dir;
            }
        }

        return mejor;
    }

    private void explorarSeguro() {
        List<DireccionSeguridad> opciones = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            Position siguiente = new Position(
                player.getPosition().getRow() + dir.getRowDelta(),
                player.getPosition().getCol() + dir.getColDelta()
            );

            int peligro = calcularPeligroPosicion(siguiente);
            opciones.add(new DireccionSeguridad(dir, peligro));
        }

        opciones.sort(Comparator.comparingInt(ds -> ds.peligro));

        // Intentar moverse en la dirección más segura, destruyendo si es necesario
        for (DireccionSeguridad ds : opciones) {
            if (ds.peligro < 9999 && moverConDestruccion(ds.direccion)) {
                return;
            }
        }
    }

    /**
     * Desbloquear agresivamente destruyendo hielo en múltiples direcciones
     */
    private void desbloquearAgresivo() {
        Position actual = player.getPosition();
        
        // Contar cuántos bloques hay alrededor
        int bloquesAlrededor = 0;
        for (Direction dir : Direction.values()) {
            Position pos = new Position(
                actual.getRow() + dir.getRowDelta(),
                actual.getCol() + dir.getColDelta()
            );
            
            if (map.isValid(pos) && map.isBlocked(pos)) {
                bloquesAlrededor++;
            }
        }
        
        // Si está muy encerrado, destruir en múltiples direcciones
        if (bloquesAlrededor >= 3) {
            for (Direction dir : Direction.values()) {
                Position pos = new Position(
                    actual.getRow() + dir.getRowDelta(),
                    actual.getCol() + dir.getColDelta()
                );
                
                if (map.isValid(pos) && map.isBlocked(pos)) {
                    player.destroyIce(dir);
                }
            }
        } else {
            // Destruir en la dirección más prometedora
            Direction mejorDir = direccionParaDesbloquear();
            if (mejorDir != null) {
                player.destroyIce(mejorDir);
                player.move(mejorDir);
            }
        }
    }
    
    /**
     * Encuentra la mejor dirección para desbloquear
     */
    private Direction direccionParaDesbloquear() {
        Direction mejor = null;
        int mejorScore = Integer.MIN_VALUE;
        Position actual = player.getPosition();
        
        for (Direction dir : Direction.values()) {
            Position pos = new Position(
                actual.getRow() + dir.getRowDelta(),
                actual.getCol() + dir.getColDelta()
            );
            
            if (!map.isValid(pos)) continue;
            
            int score = 0;
            
            // Preferir direcciones con hielo
            if (map.isBlocked(pos)) score += 100;
            
            // Evaluar qué hay más allá
            Position masMas = new Position(
                pos.getRow() + dir.getRowDelta(),
                pos.getCol() + dir.getColDelta()
            );
            
            if (map.isValid(masMas)) {
                if (!map.isBlocked(masMas)) score += 50; // Hay espacio libre más allá
                if (!map.hasEnemy(masMas)) score += 30;
            }
            
            if (score > mejorScore) {
                mejorScore = score;
                mejor = dir;
            }
        }
        
        return mejor;
    }

    private static class DireccionSeguridad {
        Direction direccion;
        int peligro;

        DireccionSeguridad(Direction direccion, int peligro) {
            this.direccion = direccion;
            this.peligro = peligro;
        }
    }

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

    private int distancia(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
}