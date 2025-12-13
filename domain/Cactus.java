package domain;

/**
 * Cactus: Alterna entre estado seguro (active) y peligroso (dangerous) cada 30 segundos.
 * Cuando está en estado dangerous, elimina al jugador que lo toca.
 * Otorga 250 puntos.
 */
public class Cactus extends Fruit {
    
    private static final int STATE_CHANGE_INTERVAL = 30000; // 30 segundos
    private long lastStateChangeTime;
    
    public Cactus(Position position) {
        super(FruitType.cactus, position, 0, true);
        this.scoreValue = 250;
        this.state = FruitState.active; // Empieza seguro
        this.lastStateChangeTime = System.currentTimeMillis();
    }
    
    public int getScore() {
        return scoreValue;
    }

    @Override
    public void update() {
        if (!active) return;
        if (state == FruitState.eaten) return;
        if (frozen) return;

        long currentTime = System.currentTimeMillis();
        
        // Alternar entre active y dangerous cada 30 segundos
        if (currentTime - lastStateChangeTime >= STATE_CHANGE_INTERVAL) {
            if (state == FruitState.active) {
                state = FruitState.dangerous;
            } else if (state == FruitState.dangerous) {
                state = FruitState.active;
            }
            lastStateChangeTime = currentTime;
        }
    }
    
    @Override
    public String getSpriteKey() {
        if (state == FruitState.dangerous) {
            return "cactus_thorns";
        }
        return "cactus";
    }
}