package domain;

import java.util.*;

/**
 * IA que prioriza recolectar frutas por el camino más corto
 */
public class Hungry implements ControllerCream {
    private IceCream player;
    private GameMap map;
    private BadIceCream game;

    private int tick = 0;
    private int speed = 5; //+ grande + lento
    
    public Hungry(GameMap map, BadIceCream game) {
        this.map = map;
        this.game = game;
    }
    
    @Override
    public void setPlayer(IceCream player) {
        this.player = player;
    }
    
    @Override
    public void update() {

        tick++;
        if (tick < speed) return;
        tick = 0;

        if (player == null || !player.isAlive()) return;
        
        Fruit frutaCercana = encontrarFrutaMasCercana();
        
        if (frutaCercana != null) {
            // Moverse hacia la fruta
            Direction direccion = calcularDireccionHacia(frutaCercana.getPosition());
            
            if (direccion != null) {
                boolean movido = player.move(direccion);
                
                // Si no pudo moverse, intentar crear hielo para abrir camino
                if (!movido) {
                    intentarCrearCaminoHaciaFruta(direccion);
                }
            }
        } else {
            // No hay frutas, moverse aleatoriamente
            moverseAleatoriamente();
        }
        game.checkCollisionsFor(player);
    }

    
    private Fruit encontrarFrutaMasCercana() {
        Fruit cercana = null;
        int menorDistancia = Integer.MAX_VALUE;
        
        for (Fruit f : game.getFruits()) {
            if (!f.isEaten()) {
                int distancia = calcularDistancia(player.getPosition(), f.getPosition());
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    cercana = f;
                }
            }
        }
        return cercana;
    }
    
    private Direction calcularDireccionHacia(Position destino) {
        Position actual = player.getPosition();
        
        int deltaRow = destino.getRow() - actual.getRow();
        int deltaCol = destino.getCol() - actual.getCol();
        
        // Priorizar movimiento vertical u horizontal según cuál sea mayor
        if (Math.abs(deltaRow) > Math.abs(deltaCol)) {
            return deltaRow > 0 ? Direction.DOWN : Direction.UP;
        } else if (deltaCol != 0) {
            return deltaCol > 0 ? Direction.RIGHT : Direction.LEFT;
        }
        return null;
    }
    
    private void intentarCrearCaminoHaciaFruta(Direction direccion) {
        // Intentar crear hielo en esa dirección para abrir camino
        int creados = player.createIce(direccion);
        if (creados == 0) {
            // Si no pudo crear, intentar destruir
            player.destroyIce(direccion);
        }
    }
    
    private void moverseAleatoriamente() {
        Direction[] direcciones = Direction.values();
        Direction dir = direcciones[new Random().nextInt(direcciones.length)];
        player.move(dir);
    }
    
    private int calcularDistancia(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
}