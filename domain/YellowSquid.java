package domain;

/**
 * Calamar Naranja: Persigue al jugador y destruye bloques de hielo en su camino.
 * Se detiene 3 ticks antes de romper un bloque.
 */
public class YellowSquid extends Enemy {
    
    // Estados del calamar
    private enum Estado {
        PERSIGUIENDO,   // Persiguiendo al jugador
        DETENIDO,       // Detenido frente a un bloque
        ROMPIENDO       // Rompiendo el bloque
    }
    
    private Estado estadoActual;
    private int ticksEsperando;
    private static final int TICKS_PARA_ROMPER = 3; // Espera 3 ticks antes de romper
    private Position bloqueObjetivo; // Bloque que va a romper
    
    public YellowSquid(Position position, double speed) {
        super(EnemyType.yellowSquid, position, speed);
        this.estadoActual = Estado.PERSIGUIENDO;
        this.ticksEsperando = 0;
        this.bloqueObjetivo = null;
        this.currentDirection = Direction.DOWN;
    }
    
    @Override
    public Direction getCurrentDirection() {
        return currentDirection;
    }
    
    @Override
    public void doUpdate() {
        switch (estadoActual) {
            case PERSIGUIENDO:
                actualizarPersecucion();
                break;
                
            case DETENIDO:
                actualizarEspera();
                break;
                
            case ROMPIENDO:
                actualizarRompiendo();
                break;
        }
    }
    
    /**
     * Estado: PERSIGUIENDO - Busca y sigue al jugador más cercano
     */
    private void actualizarPersecucion() {
        IceCream jugadorCercano = encontrarJugadorCercano();
        
        if (jugadorCercano == null || !jugadorCercano.isAlive()) {
            // No hay jugador, quedarse quieto
            return;
        }
        
        // Calcular dirección hacia el jugador
        Direction direccion = calcularDireccionHacia(jugadorCercano.getPosition());
        
        if (direccion == null) return;
        
        // Verificar si hay un bloque en el camino
        Position siguiente = new Position(
            position.getRow() + direccion.getRowDelta(),
            position.getCol() + direccion.getColDelta()
        );
        
        if (!gameMap.isValid(siguiente)) return;
        
        Boxy bloque = gameMap.getBlock(siguiente);
        
        // Si hay un bloque de hielo que puede destruir, detenerse
        if (bloque != null && bloque.isCreated() && bloque.canBeDestroyed()) {
            estadoActual = Estado.DETENIDO;
            ticksEsperando = 0;
            bloqueObjetivo = siguiente;
            currentDirection = direccion;
            return;
        }
        
        // Si no hay obstáculo, moverse normalmente
        if (moveInDirection(direccion)) {
            currentDirection = direccion;
            move(direccion);
        }
    }
    
    /**
     * Estado: DETENIDO - Espera antes de romper el bloque
     */
    private void actualizarEspera() {
        ticksEsperando++;
        
        if (ticksEsperando >= TICKS_PARA_ROMPER) {
            // Ya esperó suficiente, cambiar a estado ROMPIENDO
            estadoActual = Estado.ROMPIENDO;
        }
    }
    
    /**
     * Estado: ROMPIENDO - Rompe el bloque y vuelve a perseguir
     */
    private void actualizarRompiendo() {
        if (bloqueObjetivo == null) {
            estadoActual = Estado.PERSIGUIENDO;
            return;
        }
        
        // Verificar que el bloque todavía existe y se puede destruir
        if (gameMap.isValid(bloqueObjetivo)) {
            Boxy bloque = gameMap.getBlock(bloqueObjetivo);
            
            if (bloque != null && bloque.canBeDestroyed()) {
                // ¡ROMPER EL BLOQUE!
                gameMap.clearBlock(bloqueObjetivo);
            }
        }
        
        // Resetear y volver a perseguir
        bloqueObjetivo = null;
        ticksEsperando = 0;
        estadoActual = Estado.PERSIGUIENDO;
    }
    
    /**
     * Encuentra al jugador vivo más cercano
     */
    private IceCream encontrarJugadorCercano() {
        IceCream cercano = null;
        int menorDistancia = Integer.MAX_VALUE;
        
        // Buscar en todas las posiciones del mapa
        for (int r = 0; r < gameMap.getRows(); r++) {
            for (int c = 0; c < gameMap.getCols(); c++) {
                Position pos = new Position(r, c);
                IceCream jugador = gameMap.getPlayer(pos);
                
                if (jugador != null && jugador.isAlive()) {
                    int distancia = calcularDistancia(position, pos);
                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        cercano = jugador;
                    }
                }
            }
        }
        
        return cercano;
    }
    
    /**
     * Calcula la dirección hacia un objetivo
     */
    private Direction calcularDireccionHacia(Position objetivo) {
        int deltaRow = objetivo.getRow() - position.getRow();
        int deltaCol = objetivo.getCol() - position.getCol();
        
        // Priorizar movimiento vertical u horizontal según cuál sea mayor
        if (Math.abs(deltaRow) > Math.abs(deltaCol)) {
            return deltaRow > 0 ? Direction.DOWN : Direction.UP;
        } else if (deltaCol != 0) {
            return deltaCol > 0 ? Direction.RIGHT : Direction.LEFT;
        }
        
        return null; // Ya está en la misma posición
    }
    
    /**
     * Calcula distancia Manhattan entre dos posiciones
     */
    private int calcularDistancia(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
    
    @Override
    public String getSpriteKey() {
        switch (estadoActual) {
            case DETENIDO:
                return "yellowSquid_waiting";
            case ROMPIENDO:
                return "yellowSquid_breaking";
            default:
                return "yellowSquid_chasing";
        }
    }
    
    @Override
    public boolean isAnimated() {
        return estadoActual == Estado.PERSIGUIENDO;
    }
    
    // Getters para debugging o visualización
    public Estado getEstadoActual() {
        return estadoActual;
    }
    
    public int getTicksEsperando() {
        return ticksEsperando;
    }
    
    public boolean isWaiting() {
        return estadoActual == Estado.DETENIDO;
    }
    
    public boolean isBreaking() {
        return estadoActual == Estado.ROMPIENDO;
    }
}