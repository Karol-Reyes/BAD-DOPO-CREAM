package domain;

/**
 * Representa un bloque tipo caja dentro del mapa del juego. Maneja su tipo,
 * estado y posición, así como las operaciones básicas de creación y destrucción.
 */
public abstract class Boxy {

    protected BoxType boxType;
    protected BoxState state;
    protected Position position;
    protected GameMap map;

    /**
     * Crea una caja con un tipo específico y posición inicial.
     * @param boxType tipo de caja
     * @param position posición inicial en el mapa
     */
    public Boxy(BoxType boxType, Position position, BoxState state) {
        this.boxType = boxType;
        this.state = state;
        this.position = position;
    }

    public void setGameMap (GameMap m) {
        this.map = m;
    }

    /**
     * @return tipo de bloque (caja)
     */
    public BoxType getType() { 
        return boxType; 
    }

    /**
     * @return estado actual del bloque
     */
    public BoxState getState() { 
        return state; 
    }

    /**
     * @return posición actual del bloque en el mapa
     */
    public Position getPosition() { 
        return position; 
    }

    /**
     * Establece una nueva posición para el bloque.
     * @param position posición a asignar
     */
    public void setPosition(Position position) { 
        this.position = position; 
    }

    /**
     * Indica si el bloque está en estado creado.
     * @return true si el bloque está creado
     */
    public boolean isCreated() { 
        return state == BoxState.created; 
    }

    /**
     * Cambia el estado del bloque a creado.
     */
    public void create() { 
        this.state = BoxState.created; 
    }

    public void indestructible() {
        this.state = BoxState.indestructible;
    }

    /**
     * Cambia el estado del bloque a destruido.
     */
    public void destroy() { 
        this.state = BoxState.destroyed; 
    }

    /**
     * Indica si el bloque puede ser creado.
     * @return false por defecto
     */
    public abstract boolean canBeCreated();

    public abstract boolean canBeDestroyed();

    public abstract boolean canWalk();

    public void on() {
        this.state = BoxState.on;
    }

    public void off() {
        this.state = BoxState.off;
    }

    public abstract void onFreeze();

    public abstract void update();

    public void iniciarTimer() { }

    /**
    * Método llamado cuando el bloque va a ser destruido.
    * Por defecto, simplemente se elimina del mapa.
    */
    public void onDestroy(GameMap map) {
        map.clearBlock(position);
    }

    public void onUnfreeze() { }

    /**
     * Obtiene la clave del sprite correspondiente al estado actual del bloque.
     * @return clave del sprite
     */
    public String getSpriteKey() {
        switch (state) {
            case destroyed:
                return "floor_inactive";
            case created:
                return boxType.name().toLowerCase() + "_created";
            case on:
                return boxType.name().toLowerCase() + "_created";
            case off:
                return boxType.name().toLowerCase() + "_inactive";
            case indestructible:
                return boxType.name().toLowerCase() + "_inactive";
            default: // inactive
                return boxType.name().toLowerCase() + "_inactive";
        }
    }

    /**
     * Indica si el bloque posee animación.
     * @return false por defecto
     */
    public boolean isAnimated() {
        return false;
    }
}