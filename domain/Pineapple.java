package domain;

/**
 * Representa una fruta Piña dentro del mapa del juego.
 * La piña se mueve aleatoriamente por el mapa.
 */
public class Pineapple extends Fruit {

    private GameMap map;

    /**
     * Crea una fruta Piña en la posición indicada.
     * @param pos posición inicial de la fruta
     */
    public Pineapple(Position pos) {
        super(FruitType.pineapple, pos, 1, false);
        this.scoreValue = 200;
    }

    /**
     * Devuelve la cantidad de puntos que otorga la fruta.
     * @return valor en puntos
     */
    @Override
    public int getScore() {
        return scoreValue;
    }

    /**
     * Asigna el mapa del juego a la fruta.
     * @param map mapa actual del juego
     */
    public void setGameMap(GameMap map) {
        this.map = map;
    }

    /**
     * Actualiza la referencia del mapa y ejecuta la lógica de actualización.
     * @param map mapa actual del juego
     */
    @Override
    public void upd(GameMap map) {
        this.map = map;
        update();
    }

    /**
     * Ejecuta el comportamiento de movimiento de la fruta.
     */
    @Override
    public void update() {
        if (!active || frozen || isEaten() || map == null) return;

        Direction[] dirs = Direction.values();
        Direction dir = dirs[(int) (Math.random() * dirs.length)];

        move(dir);
    }

    /**
     * Intenta mover la fruta en una dirección válida.
     * @param dir dirección a intentar
     */
    private void move(Direction dir) {
        Position next = new Position(
                position.getRow() + dir.getRowDelta(),
                position.getCol() + dir.getColDelta()
        );

        if (map.isValid(next)
                && !map.isBlocked(next)
                && !map.hasEnemy(next)
                && map.getFruit(next) == null) {

            map.removeFruit(position);
            position = next;
            map.addFruit(this);
        }
    }
}