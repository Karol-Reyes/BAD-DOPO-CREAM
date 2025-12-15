package domain;

/**
 * Representa un bloque genérico dentro del mapa del juego.
 * Define su tipo, estado, posición y comportamiento básico.
 */
public abstract class Boxy {

    protected BoxType boxType;
    protected BoxState state;
    protected Position position;
    protected GameMap map;

    /**
     * Crea un bloque con un tipo, estado y posición inicial.
     * @param boxType tipo de bloque
     * @param position posición inicial en el mapa
     * @param state estado inicial del bloque
     */
    public Boxy(BoxType boxType, Position position, BoxState state) {
        this.boxType = boxType;
        this.state = state;
        this.position = position;
    }

    /**
     * Asigna el mapa de juego al bloque.
     * @param m mapa donde se encuentra el bloque
     */
    public void setGameMap(GameMap m) {
        this.map = m;
    }

    /**
     * Obtiene el tipo del bloque.
     * @return tipo de bloque
     */
    public BoxType getType() {
        return boxType;
    }

    /**
     * Obtiene el estado actual del bloque.
     * @return estado del bloque
     */
    public BoxState getState() {
        return state;
    }

    /**
     * Obtiene la posición actual del bloque.
     * @return posición del bloque
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Establece una nueva posición para el bloque.
     * @param position nueva posición a asignar
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Indica si el bloque se encuentra en estado creado.
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

    /**
     * Cambia el estado del bloque a indestructible.
     */
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
     * Indica si el bloque puede ser creado dinámicamente.
     * @return true si puede ser creado, false en caso contrario
     */
    public abstract boolean canBeCreated();

    /**
     * Indica si el bloque puede ser destruido.
     * @return true si puede ser destruido, false en caso contrario
     */
    public abstract boolean canBeDestroyed();

    /**
     * Indica si una entidad puede caminar sobre el bloque.
     * @return true si se puede caminar sobre él
     */
    public abstract boolean canWalk();

    /**
     * Cambia el estado del bloque a encendido.
     */
    public void on() {
        this.state = BoxState.on;
    }

    /**
     * Cambia el estado del bloque a apagado.
     */
    public void off() {
        this.state = BoxState.off;
    }

    /**
     * Se ejecuta cuando el bloque es afectado por congelación.
     */
    public abstract void onFreeze();

    /**
     * Actualiza el estado interno del bloque.
     */
    public abstract void update();

    /**
     * Inicia o reinicia un temporizador interno del bloque.
     */
    public void iniciarTimer() {
    }

    /**
     * Se ejecuta cuando el bloque va a ser destruido y se elimina del mapa.
     * @param map mapa del cual se removerá el bloque
     */
    public void onDestroy(GameMap map) {
        map.clearBlock(position);
    }

    /**
     * Se ejecuta cuando el bloque deja de estar congelado.
     */
    public void onUnfreeze() {
    }

    /**
     * Obtiene la clave del sprite correspondiente al estado actual del bloque.
     * @return clave del sprite
     */
    public String getSpriteKey() {
        return switch (state) {
            case destroyed -> "floor_inactive";
            case created -> boxType.name().toLowerCase() + "_created";
            case on -> boxType.name().toLowerCase() + "_created";
            case off -> boxType.name().toLowerCase() + "_inactive";
            case indestructible -> boxType.name().toLowerCase() + "_inactive";
            default -> boxType.name().toLowerCase() + "_inactive";
        };
    }

    /**
     * Indica si el bloque posee animación.
     * @return true si el bloque es animado, false en caso contrario
     */
    public boolean isAnimated() {
        return false;
    }
}