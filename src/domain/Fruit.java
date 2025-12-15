package domain;

/**
 * Representa una fruta dentro del mapa del juego.
 * Define su tipo, estado, posición y comportamiento básico.
 */
public abstract class Fruit {

    protected FruitType type;
    protected FruitState state;
    protected Position position;
    protected int speed;
    protected boolean isStatic;
    protected int scoreValue;
    protected boolean frozen = false;
    protected boolean active = false;

    /**
     * Crea una fruta con sus atributos básicos.
     * @param type tipo de fruta
     * @param pos posición inicial
     * @param speed velocidad de movimiento
     * @param isStatic indica si la fruta es estática
     */
    public Fruit(FruitType type, Position pos, int speed, boolean isStatic) {
        this.type = type;
        this.state = FruitState.active;
        this.position = pos;
        this.speed = speed;
        this.isStatic = isStatic;
    }

    /**
     * Obtiene el puntaje otorgado por la fruta.
     * @return valor de puntaje
     */
    public abstract int getScore();

    /**
     * Congela la fruta impidiendo su comportamiento normal.
     */
    public void freeze() {
        frozen = true;
    }

    /**
     * Descongela la fruta restaurando su comportamiento normal.
     */
    public void unfreeze() {
        frozen = false;
    }

    /**
     * Indica si la fruta está congelada.
     * @return true si está congelada
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Indica si la fruta está activa.
     * @return true si está activa
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Activa la fruta en el juego.
     */
    public void activate() {
        active = true;
    }

    /**
     * Desactiva la fruta del juego.
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Indica si la fruta es peligrosa al contacto.
     * @return true si su estado es peligroso
     */
    public boolean isDangerous() {
        return state == FruitState.dangerous;
    }

    /**
     * Obtiene el valor de puntaje asignado.
     * @return valor de puntaje
     */
    public int getScoreValue() {
        return scoreValue;
    }

    /**
     * Actualiza el comportamiento interno de la fruta.
     */
    public void update() {
    }

    /**
     * Actualiza la fruta permitiendo el uso del mapa.
     * @param map mapa del juego
     */
    public void upd(GameMap map) {
        update();
    }

    /**
     * Obtiene el tipo de fruta.
     * @return tipo de fruta
     */
    public FruitType getType() {
        return type;
    }

    /**
     * Obtiene el estado actual de la fruta.
     * @return estado de la fruta
     */
    public FruitState getState() {
        return state;
    }

    /**
     * Obtiene la posición actual de la fruta.
     * @return posición de la fruta
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Establece una nueva posición para la fruta.
     * @param position nueva posición
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Obtiene la velocidad de movimiento.
     * @return velocidad de la fruta
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Indica si la fruta no se mueve.
     * @return true si es estática
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * Indica si la fruta ya fue consumida.
     * @return true si fue comida
     */
    public boolean isEaten() {
        return state == FruitState.eaten;
    }

    /**
     * Marca la fruta como comida.
     */
    public void eat() {
        this.state = FruitState.eaten;
    }

    /**
     * Restablece el estado activo de la fruta.
     */
    public void reset() {
        this.state = FruitState.active;
    }

    /**
     * Obtiene la clave del sprite asociado.
     * @return clave del sprite
     */
    public String getSpriteKey() {
        return type.name();
    }

    /**
     * Indica si la fruta posee animación.
     * @return false por defecto
     */
    public boolean isAnimated() {
        return false;
    }
}