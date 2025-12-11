package test;

import domain.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class BoxTest {

    // Clase interna 'dummy' para probar la clase abstracta Box
    private class BoxConcreto extends Box {
        public BoxConcreto(BoxType type, Position pos) {
            super(type, pos);
        }
        // Métodos abstractos/adicionales que no afectan esta prueba base
        @Override public boolean canWalk() { return true; }
        @Override public boolean canBeDestroyed() { return true; }
    }

    @Test
    public void testBoxBaseFunctionality() {
        Position pos = new Position(5, 5);
        Box box = new BoxConcreto(BoxType.ice, pos);

        // Test Getters iniciales
        assertEquals(BoxType.ice, box.getType());
        assertEquals(BoxState.inactive, box.getState());
        assertEquals(pos, box.getPosition());
    }

    @Test
    public void testSetPosition() {
        Position startPos = new Position(0, 0);
        Position newPos = new Position(10, 10);
        Box box = new BoxConcreto(BoxType.ice, startPos);

        box.setPosition(newPos);
        assertEquals("La posición debería haberse actualizado", newPos, box.getPosition());
    }

    @Test
    public void testSpriteKeyLogic() {
        Position pos = new Position(0, 0);
        Box box = new BoxConcreto(BoxType.ice, pos);

        // Estado inactivo
        assertEquals("ice_inactive", box.getSpriteKey());

        // Estado creado
        box.create();
        assertEquals("ice_created", box.getSpriteKey());
    }
}