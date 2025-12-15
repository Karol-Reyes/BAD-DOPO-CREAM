package domain;

public class Fire extends Boxy {

    /**
     * Crea un bloque de fuego en una posición y estado específicos.
     * @param position posición inicial del bloque
     * @param state estado inicial del bloque
     */
    public Fire(Position position, BoxState state) {
        super(BoxType.fire, position, state);
    }

    /**
     * Indica si el jugador puede caminar sobre este bloque.
     * @return true siempre, ya que el fuego es transitable
     */
    @Override
    public boolean canWalk() {
        return true;
    }

    /**
     * Indica si el bloque puede ser destruido.
     * @return false, ya que el fuego no es destruible
     */
    @Override
    public boolean canBeDestroyed() {
        return false;
    }

    /**
     * Indica si el bloque puede ser creado dinámicamente.
     * @return false, ya que el fuego no puede ser creado
     */
    @Override
    public boolean canBeCreated() {
        return false;
    }

    /**
     * Define el comportamiento del bloque cuando es congelado.
     */
    @Override
    public void onFreeze() {
    }
    
    /**
     * Actualiza el estado del bloque de fuego.
     */
    @Override
    public void update() {
    }
}