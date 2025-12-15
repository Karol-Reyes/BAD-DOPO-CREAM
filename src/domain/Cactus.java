package domain;

/**
 * Representa una fruta tipo cactus dentro del juego.
 * El cactus alterna periódicamente entre un estado seguro y uno peligroso.
 * Cuando se encuentra en estado peligroso, elimina al jugador que lo toca.
 */
public class Cactus extends Fruit {

    private static final int STATE_CHANGE_INTERVAL = 30000;
    private long lastStateChangeTime;

    /**
     * Crea un cactus en una posición específica del mapa.
     * @param position posición inicial del cactus
     */
    public Cactus(Position position) {
        super(FruitType.cactus, position, 0, true);
        this.scoreValue = 250;
        this.state = FruitState.active;
        this.lastStateChangeTime = System.currentTimeMillis();
    }

    /**
     * Obtiene la puntuación que otorga el cactus al ser recolectado.
     * @return valor de puntuación del cactus
     */
    @Override
    public int getScore() {
        return scoreValue;
    }

    /**
     * Actualiza el estado del cactus y gestiona el cambio periódico
     * entre estado seguro y estado peligroso.
     */
    @Override
    public void update() {
        if (!active) return;
        if (state == FruitState.eaten) return;
        if (frozen) return;

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastStateChangeTime >= STATE_CHANGE_INTERVAL) {
            if (state == FruitState.active) {
                state = FruitState.dangerous;
            } else if (state == FruitState.dangerous) {
                state = FruitState.active;
            }
            lastStateChangeTime = currentTime;
        }
    }

    /**
     * Obtiene la clave del sprite correspondiente al estado actual del cactus.
     * @return clave del sprite a utilizar
     */
    @Override
    public String getSpriteKey() {
        if (state == FruitState.dangerous) {
            return "cactus_thorns";
        }
        return "cactus";
    }
}