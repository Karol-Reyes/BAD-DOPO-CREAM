package domain;

public class Ice extends Boxy {

    /**
     * Constructor segun una posicion dada
     * @param position del hielo
     */
    public Ice(Position position, BoxState state) {
        super(BoxType.ice, position, state);
    }

    /**
     * Indica si se puede caminar sobre el bloque.
     * @return true si se puede caminar, false en caso contrario
     */
    @Override
    public boolean canWalk() {
        return state != BoxState.created;
    }

    public void onFreeze() {}

    public void update() {}

    /**
     * Indica si el bloque se puede destruir.
     * @return true si se puede destruir, false en caso contrario
     */
    @Override
    public boolean canBeDestroyed() {
        return state == BoxState.created;
    }

    /**
     * Indica si el bloque se puede crear.
     * @return true si se puede crear, false en caso contrario
     */
    @Override
    public boolean canBeCreated() {
        return state != BoxState.created;
    }

    /**
     * Crea el bloque.
     */
    @Override
    public void create() {
        if (state != BoxState.created) {
            super.create();
        }
    }

    /**
     * Destruye el bloque.
     */
    @Override
    public void destroy(){
        if (canBeDestroyed()) {
            super.destroy();
        }
    }
}
