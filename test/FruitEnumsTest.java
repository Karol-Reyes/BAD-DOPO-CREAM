package test;

import domain.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class FruitEnumsTest {

    // ==========================================================
    // TESTS PARA FRUITSTATE
    // ==========================================================
    
    @Test
    public void testFruitStateValues() {
        // 1. Verificar el número de estados
        assertEquals("Debe haber 3 estados para FruitState (active, eaten, teleporting)", 3, FruitState.values().length);
        
        // 2. Verificar la existencia y nombres de los estados
        assertEquals("active", FruitState.active.name());
        assertEquals("eaten", FruitState.eaten.name());
        assertEquals("teleporting", FruitState.teleporting.name());
    }

    // ==========================================================
    // TESTS PARA FRUITTYPE
    // ==========================================================

    @Test
    public void testFruitTypeValues() {
        // 1. Verificar el número de tipos
        assertEquals("Debe haber 4 tipos para FruitType (banana, grape, pineapple, cherry)", 4, FruitType.values().length);
        
        // 2. Verificar la existencia y nombres de los tipos
        assertEquals("banana", FruitType.banana.name());
        assertEquals("grape", FruitType.grape.name());
        assertEquals("pineapple", FruitType.pineapple.name());
        assertEquals("cherry", FruitType.cherry.name());
    }
}
