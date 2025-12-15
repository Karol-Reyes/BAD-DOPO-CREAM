package domain;

/**
 * Representa una fogata dentro del mapa del juego.
 * La fogata puede apagarse temporalmente al ser congelada
 * y vuelve a encenderse automáticamente tras un tiempo definido.
 */
public class Bonfire extends Boxy {

    private long freezeTimestamp;
    private static final long FREEZE_DURATION = 10000;

    /**
     * Crea una fogata en una posición específica con un estado inicial.
     * @param position posición donde se ubicará la fogata
     * @param state estado inicial de la fogata
     */
    public Bonfire(Position position, BoxState state) {
        super(BoxType.bonfire, position, state);
        this.freezeTimestamp = 0;
    }

    /**
     * Indica si una entidad puede caminar sobre la fogata.
     * @return true si la fogata está apagada, false en caso contrario
     */
    @Override
    public boolean canWalk() {
        return state == BoxState.off;
    }

    /**
     * Indica si la fogata puede ser creada dinámicamente.
     * @return false, ya que la fogata no puede ser creada
     */
    @Override
    public boolean canBeCreated() {
        return false;
    }

    /**
     * Indica si la fogata puede ser destruida.
     * @return false, ya que la fogata no puede ser destruida
     */
    @Override
    public boolean canBeDestroyed() {
        return false;
    }

    /**
     * Enciende la fogata y reinicia el temporizador de congelación.
     */
    @Override
    public void on() {
        if (state != BoxState.on) {
            super.on();
            freezeTimestamp = 0;
        }
    }

    /**
     * Apaga la fogata si se encuentra encendida.
     */
    @Override
    public void off() {
        if (state != BoxState.off) {
            super.off();
        }
    }

    /**
     * Se ejecuta cuando la fogata es congelada por hielo.
     * Apaga la fogata y registra el momento de la congelación.
     */
    @Override
    public void onFreeze() {
        off();
        freezeTimestamp = System.currentTimeMillis();
    }

    /**
     * Reinicia el temporizador que controla el tiempo apagado de la fogata.
     */
    @Override
    public void iniciarTimer() {
        freezeTimestamp = System.currentTimeMillis();
    }

    /**
     * Actualiza el estado de la fogata y verifica si debe volver a encenderse
     * después de haber sido congelada.
     */
    @Override
    public void update() {
        if (state == BoxState.off && freezeTimestamp > 0) {
            long elapsed = System.currentTimeMillis() - freezeTimestamp;
            if (elapsed >= FREEZE_DURATION) {
                on();
            }
        }
    }

    /**
     * Se ejecuta cuando el hielo que congelaba la fogata es destruido
     * y reinicia el temporizador de apagado.
     */
    @Override
    public void onUnfreeze() {
        freezeTimestamp = System.currentTimeMillis();
    }
}