package domain;

public class Grape extends Fruit{
    
    /**
     * Constructor de Grape.
     * @param position la posición inicial de la uva
     */
    public Grape(Position position) {
        super(FruitType.grape, position, 0, true);
        this.scoreValue = 50;
    }

    public int getScore() {
        return scoreValue;
    }
}