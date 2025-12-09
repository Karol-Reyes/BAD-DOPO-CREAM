package test;

import domain.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class FruitFactoryTest {

    // Stub para Position
    private Position mockPos = new Position(2, 2);

    @Test
    public void testCreateBananaFruit() {
        // 1. Act
        Fruit fruta = FruitFactory.createFruit(FruitType.banana, mockPos);

        // 2. Assert (Verifica clase exacta)
        assertNotNull(fruta);
        assertEquals("La clase concreta debe ser Banana", Banana.class, fruta.getClass());
        assertEquals(FruitType.banana, fruta.getType());
    }
    
    @Test
    public void testCreateGrapeFruit() {
        // 1. Act
        Fruit fruta = FruitFactory.createFruit(FruitType.grape, mockPos);

        // 2. Assert (Verifica clase exacta)
        assertNotNull(fruta);
        assertEquals("La clase concreta debe ser Grape", Grape.class, fruta.getClass());
        assertEquals(FruitType.grape, fruta.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFruitNullType() {
        FruitFactory.createFruit(null, mockPos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFruitNullPosition() {
        FruitFactory.createFruit(FruitType.banana, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFruitUnavailableType() {
        // Pineapple y Cherry no están implementados en el switch de Factory, deben fallar.
        FruitFactory.createFruit(FruitType.pineapple, mockPos); 
    }
}
