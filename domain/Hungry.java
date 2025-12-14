package domain;

import java.util.*;

/**
 * IA que prioriza recolectar frutas de la manera más eficiente posible.
 * Agresivo, directo, y obsesionado con comer.
 */
public class Hungry implements ControllerCream {
    private IceCream player;
    private GameMap map;
    private BadIceCream game;

    private int tick = 0;
    private int speed = 3; // Rápido y agresivo

    private Position ultimaPosicion = null;
    private int ticksAtascado = 0;
    private Position frutaObjetivo = null;
    
    public Hungry(GameMap map, BadIceCream game) {
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

        Position actual = player.getPosition();

        // Detectar si está atascado
        if (actual.equals(ultimaPosicion)) {
            ticksAtascado++;
            if (ticksAtascado > 6) {
                desatascar();
                ticksAtascado = 0;
                frutaObjetivo = null; // Cambiar de objetivo
            }
        } else {
            ticksAtascado = 0;
        }
        ultimaPosicion = new Position(actual.getRow(), actual.getCol());

        // Verificar colisiones
        game.checkCollisionsFor(player);

        // Buscar fruta objetivo
        Fruit fruta = seleccionarMejorFruta();
        
        if (fruta != null) {
            frutaObjetivo = fruta.getPosition();
            irAgresivamenteHacia(fruta.getPosition());
        } else {
            // No hay frutas, explorar agresivamente
            explorarAgresivo();
        }
    }

    /**
     * Selecciona la mejor fruta considerando distancia y accesibilidad
     */
    private Fruit seleccionarMejorFruta() {
        Fruit mejor = null;
        int mejorScore = Integer.MIN_VALUE;
        Position actual = player.getPosition();

        for (Fruit f : game.getFruits()) {
            if (f.isEaten() || !f.isActive()) continue;

            int distancia = calcularDistancia(actual, f.getPosition());
            int obstaculos = contarObstaculosEnCamino(actual, f.getPosition());

            // Score: priorizar cercanía y menos obstáculos
            int score = (100 - distancia * 2) - (obstaculos * 5);

            // Bonificación si es la misma fruta objetivo (persistencia)
            if (frutaObjetivo != null && f.getPosition().equals(frutaObjetivo)) {
                score += 30;
            }

            if (score > mejorScore) {
                mejorScore = score;
                mejor = f;
            }
        }

        return mejor;
    }

    /**
     * Cuenta obstáculos aproximados en el camino
     */
    private int contarObstaculosEnCamino(Position origen, Position destino) {
        int obstaculos = 0;
        int pasos = Math.max(Math.abs(destino.getRow() - origen.getRow()), 
                           Math.abs(destino.getCol() - origen.getCol()));
        
        for (int i = 1; i <= pasos; i++) {
            float t = (float)i / pasos;
            int r = (int)(origen.getRow() + t * (destino.getRow() - origen.getRow()));
            int c = (int)(origen.getCol() + t * (destino.getCol() - origen.getCol()));
            Position pos = new Position(r, c);
            
            if (map.isValid(pos) && map.isBlocked(pos)) {
                obstaculos++;
            }
        }
        
        return obstaculos;
    }

    /**
     * Se mueve agresivamente hacia la fruta, destruyendo todo en el camino
     */
    private void irAgresivamenteHacia(Position destino) {
        Position actual = player.getPosition();

        // Calcular la mejor dirección
        Direction dirOptima = calcularDireccionOptima(actual, destino);
        
        if (dirOptima == null) {
            explorarAgresivo();
            return;
        }

        Position siguiente = new Position(
            actual.getRow() + dirOptima.getRowDelta(),
            actual.getCol() + dirOptima.getColDelta()
        );

        // Verificar si hay un enemigo en la siguiente posición
        if (map.isValid(siguiente) && map.hasEnemy(siguiente)) {
            // Intentar crear barrera y buscar ruta alternativa
            player.createIce(dirOptima);
            
            // Probar direcciones alternativas
            Direction[] alternativas = calcularDireccionesAlternativas(destino);
            for (Direction alt : alternativas) {
                Position posAlt = new Position(
                    actual.getRow() + alt.getRowDelta(),
                    actual.getCol() + alt.getColDelta()
                );
                
                if (map.isValid(posAlt) && !map.hasEnemy(posAlt)) {
                    if (intentarMoverYDestruir(alt)) {
                        return;
                    }
                }
            }
            return;
        }

        // Intentar moverse en la dirección óptima
        intentarMoverYDestruir(dirOptima);
    }

    /**
     * Intenta moverse en una dirección, destruyendo hielo si es necesario
     */
    private boolean intentarMoverYDestruir(Direction dir) {
        boolean movido = player.move(dir);
        
        if (!movido) {
            Position siguiente = new Position(
                player.getPosition().getRow() + dir.getRowDelta(),
                player.getPosition().getCol() + dir.getColDelta()
            );
            
            if (map.isValid(siguiente) && map.isBlocked(siguiente)) {
                // Destruir el obstáculo
                player.destroyIce(dir);
                // Intentar moverse de nuevo
                return player.move(dir);
            }
        }
        
        return movido;
    }

    /**
     * Calcula la dirección óptima considerando múltiples factores
     */
    private Direction calcularDireccionOptima(Position origen, Position destino) {
        int dr = destino.getRow() - origen.getRow();
        int dc = destino.getCol() - origen.getCol();

        if (dr == 0 && dc == 0) return null;

        Direction[] candidatos;
        
        if (Math.abs(dr) > Math.abs(dc)) {
            // Priorizar vertical
            Direction vertical = dr > 0 ? Direction.DOWN : Direction.UP;
            Direction horizontal = dc > 0 ? Direction.RIGHT : (dc < 0 ? Direction.LEFT : null);
            
            if (horizontal != null) {
                candidatos = new Direction[]{vertical, horizontal};
            } else {
                candidatos = new Direction[]{vertical};
            }
        } else {
            // Priorizar horizontal
            Direction horizontal = dc > 0 ? Direction.RIGHT : Direction.LEFT;
            Direction vertical = dr > 0 ? Direction.DOWN : (dr < 0 ? Direction.UP : null);
            
            if (vertical != null) {
                candidatos = new Direction[]{horizontal, vertical};
            } else {
                candidatos = new Direction[]{horizontal};
            }
        }

        // Elegir la mejor de las candidatas que no tenga enemigo
        for (Direction dir : candidatos) {
            Position pos = new Position(
                origen.getRow() + dir.getRowDelta(),
                origen.getCol() + dir.getColDelta()
            );
            
            if (map.isValid(pos) && !map.hasEnemy(pos)) {
                return dir;
            }
        }

        // Si todas tienen enemigos, retornar la primera
        return candidatos.length > 0 ? candidatos[0] : null;
    }

    /**
     * Calcula direcciones alternativas hacia el destino
     */
    private Direction[] calcularDireccionesAlternativas(Position destino) {
        Position actual = player.getPosition();
        int dr = destino.getRow() - actual.getRow();
        int dc = destino.getCol() - actual.getCol();

        Direction vertical = dr > 0 ? Direction.DOWN : Direction.UP;
        Direction horizontal = dc > 0 ? Direction.RIGHT : Direction.LEFT;

        if (Math.abs(dr) > Math.abs(dc)) {
            return new Direction[]{
                horizontal,
                vertical.getOpposite(),
                horizontal.getOpposite()
            };
        } else {
            return new Direction[]{
                vertical,
                horizontal.getOpposite(),
                vertical.getOpposite()
            };
        }
    }

    /**
     * Exploración agresiva cuando no hay fruta objetivo
     */
    private void explorarAgresivo() {
        // Evaluar todas las direcciones
        List<DireccionScore> opciones = new ArrayList<>();
        Position actual = player.getPosition();

        for (Direction dir : Direction.values()) {
            Position siguiente = new Position(
                actual.getRow() + dir.getRowDelta(),
                actual.getCol() + dir.getColDelta()
            );

            if (!map.isValid(siguiente)) continue;

            int score = 0;

            // Preferir espacios libres
            if (!map.isBlocked(siguiente)) score += 100;
            
            // Evitar enemigos directos
            if (map.hasEnemy(siguiente)) score -= 1000;

            // Preferir direcciones que se alejen de la última posición
            if (ultimaPosicion != null) {
                int distOrigen = calcularDistancia(siguiente, ultimaPosicion);
                score += distOrigen * 10;
            }

            opciones.add(new DireccionScore(dir, score));
        }

        // Ordenar por score
        opciones.sort((a, b) -> Integer.compare(b.score, a.score));

        // Intentar moverse en la mejor dirección
        for (DireccionScore ds : opciones) {
            if (intentarMoverYDestruir(ds.direccion)) {
                return;
            }
        }
    }

    /**
     * Desatascar cuando está completamente bloqueado
     */
    private void desatascar() {
        // Intentar destruir en todas las direcciones
        for (Direction dir : Direction.values()) {
            Position pos = new Position(
                player.getPosition().getRow() + dir.getRowDelta(),
                player.getPosition().getCol() + dir.getColDelta()
            );
            
            if (map.isValid(pos)) {
                if (map.isBlocked(pos)) {
                    player.destroyIce(dir);
                    player.move(dir);
                    return;
                }
            }
        }

        // Si nada funciona, intentar crear hielo para cambiar el entorno
        Direction randomDir = Direction.values()[new Random().nextInt(4)];
        player.createIce(randomDir);
    }

    private static class DireccionScore {
        Direction direccion;
        int score;

        DireccionScore(Direction direccion, int score) {
            this.direccion = direccion;
            this.score = score;
        }
    }
    
    private int calcularDistancia(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
}