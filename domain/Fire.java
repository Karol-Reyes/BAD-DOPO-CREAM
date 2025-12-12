package domain;

public class Fire extends Boxy {
    
    /**
     * Constructor segun una posicion dada
     * @param position del hielo
     */
    public Fire(Position position, BoxState state) {
        super(BoxType.fire, position, state);
    }

    /**
     * Indica si se puede caminar sobre el bloque.
     * @return true si se puede caminar, false en caso contrario
     */
    @Override
    public boolean canWalk() {
        return true;
    }

    /**
     * Indica si el bloque se puede destruir.
     * @return true si se puede destruir, false en caso contrario
     */
    @Override
    public boolean canBeDestroyed() {
        return false;
    }

    /**
     * Indica si el bloque se puede crear.
     * @return true si se puede crear, false en caso contrario
     */
    @Override
    public boolean canBeCreated() {
        return false;
    }

    public void onFreeze() {}

    public void update() {}
}