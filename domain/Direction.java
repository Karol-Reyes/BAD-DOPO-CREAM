package domain;
/**
 * Representa las direcciones posibles de movimiento en una cuadrícula,
 * definiendo el desplazamiento en filas y columnas asociado a cada una.
 */
public enum Direction {

    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int rowDelta;
    private final int colDelta;

    /**
     * Crea una dirección con los desplazamientos de fila y columna asociados.
     * @param rowDelta cambio aplicado a la fila
     * @param colDelta cambio aplicado a la columna
     */
    Direction(int rowDelta, int colDelta) {
        this.rowDelta = rowDelta;
        this.colDelta = colDelta;
    }

    /**
     * Obtiene el desplazamiento en filas asociado a la dirección.
     * @return cambio en la fila
     */
    public int getRowDelta() {
        return rowDelta;
    }

    /**
     * Obtiene el desplazamiento en columnas asociado a la dirección.
     * @return cambio en la columna
     */
    public int getColDelta() {
        return colDelta;
    }

    /**
     * Obtiene la dirección opuesta a la actual.
     * @return dirección contraria
     */
    public Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}