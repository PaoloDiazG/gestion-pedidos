package test.SistemaPedidos.modelo;

import SistemaPedidos.modelo.ItemPedido;
import SistemaPedidos.modelo.Producto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemPedidoTest {

    @Test
    public void testCreacionItemPedido() {
        // Arrange
        Producto producto = new Producto("001", "Laptop Test", 1500.0, 10);
        int cantidad = 2;
        
        // Act
        ItemPedido item = new ItemPedido(producto, cantidad);
        
        // Assert
        assertEquals(producto, item.getProducto(), "El producto debe ser el mismo");
        assertEquals(cantidad, item.getCantidadSolicitada(), "La cantidad debe coincidir");
    }
    
    @Test
    public void testSetCantidadSolicitada() {
        // Arrange
        Producto producto = new Producto("001", "Laptop Test", 1500.0, 10);
        ItemPedido item = new ItemPedido(producto, 2);
        int nuevaCantidad = 5;
        
        // Act
        item.setCantidadSolicitada(nuevaCantidad);
        
        // Assert
        assertEquals(nuevaCantidad, item.getCantidadSolicitada(), "La cantidad debe actualizarse correctamente");
    }
    
    @Test
    public void testCalcularSubtotal() {
        // Arrange
        double precio = 1500.0;
        int cantidad = 2;
        Producto producto = new Producto("001", "Laptop Test", precio, 10);
        ItemPedido item = new ItemPedido(producto, cantidad);
        double subtotalEsperado = precio * cantidad;
        
        // Act
        double subtotalCalculado = item.calcularSubtotal();
        
        // Assert
        assertEquals(subtotalEsperado, subtotalCalculado, 0.001, "El subtotal debe ser precio * cantidad");
    }
    
    @Test
    public void testCalcularSubtotalConCambioDeCantidad() {
        // Arrange
        double precio = 1500.0;
        int cantidadInicial = 2;
        int cantidadNueva = 3;
        Producto producto = new Producto("001", "Laptop Test", precio, 10);
        ItemPedido item = new ItemPedido(producto, cantidadInicial);
        
        // Act
        double subtotalInicial = item.calcularSubtotal();
        item.setCantidadSolicitada(cantidadNueva);
        double subtotalNuevo = item.calcularSubtotal();
        
        // Assert
        assertEquals(precio * cantidadInicial, subtotalInicial, 0.001, "El subtotal inicial debe ser correcto");
        assertEquals(precio * cantidadNueva, subtotalNuevo, 0.001, "El subtotal debe actualizarse con la nueva cantidad");
    }
}