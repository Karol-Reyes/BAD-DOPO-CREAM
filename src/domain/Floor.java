package domain;

/**
 * Representa una superficie del mapa del juego.
 */
public class Floor extends Boxy {

    /**
     * Crea una superficie del mapa en una posición y estado específicos.
     * @param position posición inicial del bloque
     * @param state estado inicial del bloque
     */
    public Floor(Position position, BoxState state) {
        super(BoxType.floor, position, state);
    }

    /**
     * Indica si el jugador puede caminar sobre este bloque.
     * @return true siempre, ya que el piso es transitable
     */
    @Override
    public boolean canWalk() {
        return true;
    }

    /**
     * Indica si el bloque puede ser destruido.
     * @return false, ya que el piso no es destruible
     */
    @Override
    public boolean canBeDestroyed() {
        return false;
    }

    /**
     * Indica si el bloque puede ser creado dinámicamente.
     * @return false, ya que el piso no puede ser creado
     */
    @Override
    public boolean canBeCreated() {
        return false;
    }

    /**
     * Establece el bloque como indestructible si aún no lo es.
     */
    @Override
    public void create() {
        if (state != BoxState.indestructible) {
            super.indestructible();
        }
    }

    /**
     * Define el comportamiento del bloque cuando es congelado.
     */
    @Override
    public void onFreeze() {
    }

    /**
     * Actualiza el estado del bloque en cada ciclo del juego.
     */
    @Override
    public void update() {
    }
}