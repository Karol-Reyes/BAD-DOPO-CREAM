package domain;

public class Iron  extends Boxy {

    /**
     * Constructor segun una posicion dada
     * @param position del hierro
     */
    public Iron(Position position, BoxState state) {
        super(BoxType.iron, position, state);
    }

        /**
     * Indica si se puede caminar sobre el bloque.
     * @return true si se puede caminar, false en caso contrario
     */
    @Override
    public boolean canWalk() {
        return false;
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
        return true;
    }

    /**
     * Crea el bloque.
     */
    @Override
    public void create() {
        if (state != BoxState.indestructible) {
            super.indestructible();
        }
    }

    public void onFreeze() {}

    public void update() {}
}