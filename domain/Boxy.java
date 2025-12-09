package domain;

/**
 * Representa un bloque tipo caja dentro del mapa del juego. Maneja su tipo,
 * estado y posición, así como las operaciones básicas de creación y destrucción.
 */
public abstract class Boxy {

    protected BoxType boxType;
    protected BoxState state;
    protected Position position;

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
    public boolean canBeCreated() { 
        return false; 
    }

    public boolean canBeDestroyed() {
        return false;
    }

    public boolean canWalk() {
        return false;
    }

    public void on() {
        this.state = BoxState.on;
    }

    public void off() {
        this.state = BoxState.off;
    }

    /**
     * Obtiene la clave del sprite correspondiente al estado actual del bloque.
     * @return clave del sprite
     */
    public String getSpriteKey() {
        if (state == BoxState.created) {
            return boxType.name() + "_created";
        }
        return boxType.name() + "_inactive";
    }

    /**
     * Indica si el bloque posee animación.
     * @return false por defecto
     */
    public boolean isAnimated() {
        return false;
    }
}