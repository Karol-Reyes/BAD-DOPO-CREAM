package domain;

public class Bonfire extends Boxy {

    private long freezeTimestamp; 
    private static final long FREEZE_DURATION = 10000; // 10 segundos

    public Bonfire(Position position, BoxState state) {
        super(BoxType.bonfire, position, state);
        this.freezeTimestamp = 0;
    }

    @Override
    public boolean canWalk() { 
        return state == BoxState.off; // Solo se puede caminar si está apagada
    }

    @Override
    public boolean canBeCreated() { 
        return false; 
    }

    @Override
    public boolean canBeDestroyed() { 
        return false; 
    }

    @Override
    public void on() {
        if (state != BoxState.on) {
            super.on();
            freezeTimestamp = 0; // Resetear timestamp al encenderse
        }
    }

    @Override
    public void off() {
        if (state != BoxState.off) {
            super.off();
        }
    }

    /**
     * Se llama cuando el hielo la congela.
     */
    @Override
    public void onFreeze() {
        off(); // se apaga
        freezeTimestamp = System.currentTimeMillis();
    }

    /**
     * Reinicia el temporizador de estar apagado.
     */
    public void iniciarTimer() {
        freezeTimestamp = System.currentTimeMillis();
    }

    /**
     * Se llama desde BadIceCream.updateGame() para ver si ya debe reencenderse.
     */
    @Override
    public void update() {
        if (state == BoxState.off && freezeTimestamp > 0) {
            long elapsed = System.currentTimeMillis() - freezeTimestamp;
            if (elapsed >= FREEZE_DURATION) {
                on(); // se vuelve a encender
            }
        }
    }

    /**
    * Se llama cuando se destruye el hielo que la congelaba.
    */
    public void onUnfreeze() {
        // Reiniciar el timer desde este momento
        freezeTimestamp = System.currentTimeMillis();
    }
}