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
        return false; 
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
        }
    }

    @Override
    public void off() {
        if (state != BoxState.off) {
            super.off();
        }
    }

    /**
     * Se llama cuando el hielo lo congela.
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
     * Se llama desde GameMap.update() para ver si ya debe reencenderse.
     */
    @Override
    public void update() {
        if (state == BoxState.off) {
            long elapsed = System.currentTimeMillis() - freezeTimestamp;
            if (elapsed >= FREEZE_DURATION) {
                on(); // se vuelve a encender
            }
        }
    }
}
