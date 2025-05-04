package test.SistemaPedidos.modelo;

import SistemaPedidos.modelo.Pedido;
import SistemaPedidos.modelo.Producto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PedidoTest {

    @Test
    public void testCreacionPedido() {
        // Arrange & Act
        Pedido pedido = new Pedido();
        
        // Assert
        assertNotEquals(0, pedido.getId(), "El ID del pedido no debe ser 0");
        assertEquals(0, pedido.getItems().size(), "El pedido debe iniciar sin items");
        assertEquals(0.0, pedido.getTotal(), 0.001, "El total inicial debe ser 0");
        assertEquals(Pedido.EstadoPedido.PENDIENTE, pedido.getEstado(), "El estado inicial debe ser PENDIENTE");
    }
    
    @Test
    public void testAgregarItem() {
        // Arrange
        Pedido pedido = new Pedido();
        Producto producto = new Producto("001", "Laptop Test", 1500.0, 10);
        int cantidad = 2;
        
        // Act
        pedido.agregarOActualizarItem(producto, cantidad);
        
        // Assert
        assertEquals(1, pedido.getItems().size(), "El pedido debe tener 1 item");
        assertEquals(producto.getId(), pedido.getItems().get(0).getProducto().getId(), "El producto debe coincidir");
        assertEquals(cantidad, pedido.getItems().get(0).getCantidadSolicitada(), "La cantidad debe coincidir");
        assertEquals(producto.getPrecio() * cantidad, pedido.getTotal(), 0.001, "El total debe ser precio * cantidad");
    }
    
    @Test
    public void testActualizarItem() {
        // Arrange
        Pedido pedido = new Pedido();
        Producto producto = new Producto("001", "Laptop Test", 1500.0, 10);
        int cantidadInicial = 2;
        int cantidadAdicional = 3;
        
        // Act
        pedido.agregarOActualizarItem(producto, cantidadInicial);
        pedido.agregarOActualizarItem(producto, cantidadAdicional);
        
        // Assert
        assertEquals(1, pedido.getItems().size(), "El pedido debe mantener 1 item");
        assertEquals(cantidadInicial + cantidadAdicional, pedido.getItems().get(0).getCantidadSolicitada(), 
                "La cantidad debe ser la suma de ambas cantidades");
        assertEquals(producto.getPrecio() * (cantidadInicial + cantidadAdicional), pedido.getTotal(), 0.001, 
                "El total debe actualizarse correctamente");
    }
    
    @Test
    public void testObtenerCantidadActualDeProducto() {
        // Arrange
        Pedido pedido = new Pedido();
        Producto producto1 = new Producto("001", "Laptop Test", 1500.0, 10);
        Producto producto2 = new Producto("002", "Mouse Test", 50.0, 20);
        int cantidad1 = 2;
        int cantidad2 = 5;
        
        // Act
        pedido.agregarOActualizarItem(producto1, cantidad1);
        pedido.agregarOActualizarItem(producto2, cantidad2);
        
        // Assert
        assertEquals(cantidad1, pedido.obtenerCantidadActualDeProducto(producto1.getId()), 
                "Debe devolver la cantidad correcta para producto1");
        assertEquals(cantidad2, pedido.obtenerCantidadActualDeProducto(producto2.getId()), 
                "Debe devolver la cantidad correcta para producto2");
        assertEquals(0, pedido.obtenerCantidadActualDeProducto("999"), 
                "Debe devolver 0 para un producto que no existe en el pedido");
    }
    
    @Test
    public void testCalcularTotal() {
        // Arrange
        Pedido pedido = new Pedido();
        Producto producto1 = new Producto("001", "Laptop Test", 1500.0, 10);
        Producto producto2 = new Producto("002", "Mouse Test", 50.0, 20);
        int cantidad1 = 2;
        int cantidad2 = 5;
        double totalEsperado = (producto1.getPrecio() * cantidad1) + (producto2.getPrecio() * cantidad2);
        
        // Act
        pedido.agregarOActualizarItem(producto1, cantidad1);
        pedido.agregarOActualizarItem(producto2, cantidad2);
        
        // Assert
        assertEquals(totalEsperado, pedido.getTotal(), 0.001, 
                "El total debe ser la suma de los subtotales de cada item");
    }
    
    @Test
    public void testCambiarEstado() {
        // Arrange
        Pedido pedido = new Pedido();
        
        // Act
        pedido.setEstado(Pedido.EstadoPedido.PROCESANDO);
        
        // Assert
        assertEquals(Pedido.EstadoPedido.PROCESANDO, pedido.getEstado(), 
                "El estado debe actualizarse correctamente");
    }
}