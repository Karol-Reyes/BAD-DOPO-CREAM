package domain;

public class Ice extends Boxy {

    private final Boxy baseBlock;

    /**
     * Crea un bloque de hielo en una posición específica.
     * @param pos posición del bloque
     * @param state estado inicial del bloque
     */
    public Ice(Position pos, BoxState state) {
        super(BoxType.ice, pos, state);
        this.baseBlock = null;
    }

    /**
     * Crea un bloque de hielo sobre otro bloque existente.
     * @param pos posición del bloque
     * @param state estado inicial del bloque
     * @param base bloque subyacente
     */
    public Ice(Position pos, BoxState state, Boxy base) {
        super(BoxType.ice, pos, state);
        this.baseBlock = base;
    }

    /**
     * Retorna el bloque que se encuentra debajo del hielo.
     * @return bloque subyacente o null
     */
    public Boxy getBaseBlock() {
        return baseBlock;
    }

    /**
     * Maneja la destrucción del hielo y restaura el bloque subyacente si existe.
     * @param map mapa del juego
     */
    @Override
    public void onDestroy(GameMap map) {
        if (baseBlock != null && baseBlock.getType() == BoxType.bonfire) {
            map.setBlock(position, baseBlock);
            baseBlock.onUnfreeze();
        } else {
            map.clearBlock(position);
        }
    }

    /**
     * Indica si el jugador puede caminar sobre el bloque.
     * @return true si se puede caminar
     */
    @Override
    public boolean canWalk() {
        return state != BoxState.created;
    }

    /**
     * Ejecuta acciones al congelarse el bloque.
     */
    @Override
    public void onFreeze() {}

    /**
     * Actualiza el estado del bloque en cada ciclo del juego.
     */
    @Override
    public void update() {}

    /**
     * Indica si el bloque puede ser destruido.
     * @return true si puede destruirse
     */
    @Override
    public boolean canBeDestroyed() {
        return state == BoxState.created;
    }

    /**
     * Indica si el bloque puede ser creado.
     * @return true si puede crearse
     */
    @Override
    public boolean canBeCreated() {
        return state != BoxState.created;
    }

    /**
     * Crea el bloque de hielo si aún no existe.
     */
    @Override
    public void create() {
        if (state != BoxState.created) {
            super.create();
        }
    }

    /**
     * Destruye el bloque de hielo si es posible.
     */
    @Override
    public void destroy() {
        if (canBeDestroyed()) {
            super.destroy();
        }
    }
}