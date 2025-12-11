package domain;

import java.util.*;

public class Expert implements ControllerCream {
    private IceCream player;
    private GameMap map;
    private BadIceCream game;

    private static final int DISTANCIA_PELIGRO = 6;
    private static final int DISTANCIA_CRITICA = 3; 

    private int tick = 0;
    private int speed = 3;

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

        Enemy enemigoInminente = detectarPeligroInmediato();
        if (enemigoInminente != null) {
                distancia(player.getPosition(), enemigoInminente.getPosition());
            reaccionEmergencia(enemigoInminente);
            return;
        }

        List<Enemy> peligros = enemigosCercanos();
        if (!peligros.isEmpty()) {
            huirInteligente(peligros);
            return;
        }

        Fruit fruta = frutaMejorPuntuada();
        if (fruta != null) {
            game.checkCollisionsFor(player);
            irHacia(fruta.getPosition());
            return;
        }

        explorar();
    }


    /**
     * Detecta enemigos MUY CERCANOS en cualquier dirección (visión 360°)
     */
    private Enemy detectarPeligroInmediato() {
        Position pos = player.getPosition();
        
        for (Enemy e : game.getEnemies()) {
            int dist = distancia(pos, e.getPosition());
            
            if (dist <= DISTANCIA_CRITICA) {
                return e;
            }
        }
        return null;
    }

    /**
     * Reacción de emergencia cuando un enemigo está MUY cerca
     */
    private void reaccionEmergencia(Enemy enemigo) {
        Position posJugador = player.getPosition();
        Position posEnemigo = enemigo.getPosition();
        
        boolean mismaFila = posJugador.getRow() == posEnemigo.getRow();
        boolean mismaColumna = posJugador.getCol() == posEnemigo.getCol();
        
        if (mismaFila || mismaColumna) {            
            Direction dirHaciaEnemigo = direccionHacia(posEnemigo);
            if (dirHaciaEnemigo != null) {
                player.createIce(dirHaciaEnemigo);
            }
        }
        
        Direction dirSegura = direccionMasSegura();
        if (dirSegura != null) {
            boolean movido = player.move(dirSegura);
            
            if (!movido) {
                Position siguiente = new Position(
                    posJugador.getRow() + dirSegura.getRowDelta(),
                    posJugador.getCol() + dirSegura.getColDelta()
                );
                
                if (map.isValid(siguiente) && map.isBlocked(siguiente)) {
                    player.destroyIce(dirSegura);
                }
            }
        }
    }

    /**
     * Encuentra TODOS los enemigos cercanos (visión 360°)
     */
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

    /**
     * Verifica si hay un enemigo en una dirección específica
     */
    private boolean hayEnemigoEnDireccion(Direction dir) {
        Position pos = player.getPosition();
        
        // Mirar hasta 5 casillas en esa dirección
        for (int i = 1; i <= 5; i++) {
            Position check = new Position(
                pos.getRow() + (dir.getRowDelta() * i),
                pos.getCol() + (dir.getColDelta() * i)
            );
            
            if (!map.isValid(check)) break;
            if (map.isBlocked(check)) break;
            
            if (map.hasEnemy(check)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calcula qué tan peligrosa es una posición (considera 360°)
     */
    private int calcularPeligroPosicion(Position pos) {
        if (!map.isValid(pos)) return 999;
        if (map.isBlocked(pos)) return 999;
        if (map.hasEnemy(pos)) return 1000; // ← Enemigo directo = muerte segura

        int nivelPeligro = 0;

        for (Enemy e : game.getEnemies()) {
            int dist = distancia(pos, e.getPosition());
            
            if (dist == 0) nivelPeligro += 1000; // Mismo lugar
            else if (dist == 1) nivelPeligro += 100; // Adyacente
            else if (dist == 2) nivelPeligro += 50;
            else if (dist == 3) nivelPeligro += 25;
            else if (dist <= 5) nivelPeligro += 10;
            else if (dist <= DISTANCIA_PELIGRO) nivelPeligro += 5;
        }

        return nivelPeligro;
    }

    /**
     * Evalúa todas las direcciones y elige la más segura (360°)
     */
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

        for (Enemy e : enemigos) {
            int dist = distancia(actual, e.getPosition());
            if (dist <= 2) {
                Direction dirHaciaEnemigo = direccionHacia(e.getPosition());
                if (dirHaciaEnemigo != null) {
                    player.createIce(dirHaciaEnemigo);
                }
            }
        }

        Direction dirSegura = direccionMasSegura();
        if (dirSegura != null) {
            boolean movido = player.move(dirSegura);

            if (!movido) {
                Position siguiente = new Position(
                    actual.getRow() + dirSegura.getRowDelta(),
                    actual.getCol() + dirSegura.getColDelta()
                );

                if (map.isValid(siguiente) && map.isBlocked(siguiente)) {
                    player.destroyIce(dirSegura);
                }
            }
        }
    }
    
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

    private void irHacia(Position destino) {
        int peligroDestino = calcularPeligroPosicion(destino);
        
        if (peligroDestino > 30) {
            explorar();
            return;
        }

        Direction dir = direccionHacia(destino);
        if (dir == null) return;

        Position siguiente = new Position(
            player.getPosition().getRow() + dir.getRowDelta(),
            player.getPosition().getCol() + dir.getColDelta()
        );

        int peligroSiguiente = calcularPeligroPosicion(siguiente);

        if (peligroSiguiente > 20) {
            Direction segura = direccionMasSegura();
            if (segura != null) dir = segura;
        }

        boolean movido = player.move(dir);

        if (!movido && map.isValid(siguiente)) {
            if (map.isBlocked(siguiente)) {
                player.destroyIce(dir);
            } else {
                player.createIce(dir);
            }
        }
    }

    private void explorar() {
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

        for (DireccionSeguridad ds : opciones) {
            if (player.move(ds.direccion)) return;
        }

        if (!opciones.isEmpty()) {
            player.destroyIce(opciones.get(0).direccion);
        }
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