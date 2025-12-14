package domain;

public class Ice extends Boxy {

    private Boxy underlyingBlock;

    /**
     * Constructor segun una posicion dada
     * @param position del hielo
     */
    public Ice(Position position, BoxState state) {
        super(BoxType.ice, position, state);
        this.underlyingBlock = null;
    }

    public Ice(Position position, BoxState state, Boxy underlying) {
        super(BoxType.ice, position, state);
        this.underlyingBlock = underlying;
    }

    public Boxy getUnderlyingBlock() {
        return underlyingBlock;
    }

    @Override
    public void onDestroy(GameMap map) {
        // Si había una bonfire debajo, restaurarla
        if (underlyingBlock != null && underlyingBlock.getType() == BoxType.bonfire) {
            map.setBlock(position, underlyingBlock);
            underlyingBlock.onUnfreeze(); // Nuevo método para reiniciar el timer
        } else {
            // No había nada especial, eliminar normalmente
            map.clearBlock(position);
        }
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
