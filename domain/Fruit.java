package domain;

/**
 * Fruta base con estado, posición, movimiento y valor de puntaje.
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
     * Inicializa una fruta con tipo, posición, velocidad y comportamiento.
     * @param type     tipo de fruta
     * @param pos      posición inicial
     * @param speed    velocidad de movimiento
     * @param isStatic indica si la fruta es estática
     */
    public Fruit(FruitType type, Position pos, int speed, boolean isStatic) {
        this.type = type;
        this.state = FruitState.active;
        this.position = pos;
        this.speed = speed;
        this.isStatic = isStatic;
    }

    public abstract int getScore();
    
    public void freeze() {
        frozen = true;
    }

    public void unfreeze() {
        frozen = false;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    /**
    * Indica si la fruta es peligrosa al tocarla.
    * Por defecto, verifica el estado dangerous.
    */
    public boolean isDangerous() {
        return state == FruitState.dangerous;
    }

    /** Devuelve el puntaje otorgado por esta fruta. */
    public int getScoreValue() { return scoreValue; }

    /** Actualiza el comportamiento de la fruta. */
    public void update() {}

    /** Llama a update() permitiendo pasar un mapa si se requiere. */
    public void upd(GameMap mao) { update(); }

    /** Retorna el tipo de fruta. */
    public FruitType getType() { return type; }

    /** Retorna el estado de la fruta. */
    public FruitState getState() { return state; }

    /** Retorna la posición actual. */
    public Position getPosition() { return position; }

    /** Establece la posición de la fruta. */
    public void setPosition(Position position) { this.position = position; }

    /** Retorna la velocidad de movimiento. */
    public int getSpeed() { return speed; }

    /** Indica si la fruta es estática. */
    public boolean isStatic() { return isStatic; }

    /** Indica si la fruta ya fue comida. */
    public boolean isEaten() { return state == FruitState.eaten; }

    /** Marca la fruta como comida. */
    public void eat() { this.state = FruitState.eaten; }

    /** Restablece la fruta a estado activo. */
    public void reset() { this.state = FruitState.active; }

    /** Retorna la clave del sprite. */
    public String getSpriteKey() { return type.name(); }

    /** Indica si la fruta posee animación. */
    public boolean isAnimated() { return false; }
}