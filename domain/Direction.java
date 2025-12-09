package domain;

public enum Direction {
    
    /**
     * Representa las direccipones en una cuadrícula.
     */
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);
    
    private final int rowDelta;
    private final int colDelta;
    
    /**
     * Crea una dirección con los cambios de fila y columna asociados.
     * @param rowDelta cambio en fila
     * @param colDelta cambio en columna
     */
    Direction(int rowDelta, int colDelta) {
        this.rowDelta = rowDelta;
        this.colDelta = colDelta;
    }
    
    /**
     * Obtiene el cambio en fila asociado a la dirección.
     */
    public int getRowDelta() {
        return rowDelta;
    }
    
    /**
     * Obtiene el cambio en columna asociado a la dirección.
     */
    public int getColDelta() {
        return colDelta;
    }
    
    /**
     * Obtiene la dirección opuesta a esta.
     */
    public Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            default -> throw new IllegalArgumentException("Direction not avilable");
        };
    }
}
