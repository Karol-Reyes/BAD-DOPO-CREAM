package domain;

public class Banana extends Fruit{
    
    /**
     * Constructor de Banana.
     * @param position la posición inicial de la banana
     */
    public Banana(Position position) {
        super(FruitType.banana, position, 0, true);
    }
}
