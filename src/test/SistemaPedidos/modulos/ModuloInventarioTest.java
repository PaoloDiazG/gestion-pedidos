package test.SistemaPedidos.modulos;

import SistemaPedidos.modelo.Producto;
import SistemaPedidos.modelo.Pedido;
import SistemaPedidos.modelo.ItemPedido;
import SistemaPedidos.modulos.ModuloInventario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModuloInventarioTest {
    
    private ModuloInventario inventario;
    private Producto producto1;
    private Producto producto2;
    
    @BeforeEach
    public void setUp() {
        inventario = new ModuloInventario();
        producto1 = new Producto("001", "Laptop Test", 1500.0, 10, Producto.Categoria.LAPTOPS, Producto.Tamano.MEDIANO);
        producto2 = new Producto("002", "Mouse Test", 50.0, 5, Producto.Categoria.MOUSE, Producto.Tamano.PEQUENO);
        
        inventario.agregarProducto(producto1);
        inventario.agregarProducto(producto2);
    }
    
    @Test
    public void testAgregarProducto() {
        // Arrange
        Producto nuevoProducto = new Producto("003", "Teclado Test", 100.0, 15);
        
        // Act
        inventario.agregarProducto(nuevoProducto);
        Producto productoRecuperado = inventario.obtenerProducto("003");
        
        // Assert
        assertNotNull(productoRecuperado, "El producto debe ser agregado y recuperable");
        assertEquals(nuevoProducto.getId(), productoRecuperado.getId(), "El ID debe coincidir");
        assertEquals(nuevoProducto.getNombre(), productoRecuperado.getNombre(), "El nombre debe coincidir");
    }
    
    @Test
    public void testObtenerProducto() {
        // Act
        Producto productoRecuperado = inventario.obtenerProducto("001");
        Producto productoInexistente = inventario.obtenerProducto("999");
        
        // Assert
        assertNotNull(productoRecuperado, "Debe recuperar un producto existente");
        assertEquals(producto1.getId(), productoRecuperado.getId(), "El ID debe coincidir");
        assertEquals(producto1.getNombre(), productoRecuperado.getNombre(), "El nombre debe coincidir");
        assertNull(productoInexistente, "Debe devolver null para un producto inexistente");
    }
    
    @Test
    public void testVerificarDisponibilidadPedido_StockSuficiente() {
        // Arrange
        Pedido pedido = new Pedido();
        pedido.agregarOActualizarItem(producto1, 5); // Solicitar 5 de 10 disponibles
        pedido.agregarOActualizarItem(producto2, 3); // Solicitar 3 de 5 disponibles
        
        // Act
        boolean disponible = inventario.verificarDisponibilidadPedido(pedido);
        
        // Assert
        assertTrue(disponible, "Debe confirmar disponibilidad cuando hay stock suficiente");
    }
    
    @Test
    public void testVerificarDisponibilidadPedido_StockInsuficiente() {
        // Arrange
        Pedido pedido = new Pedido();
        pedido.agregarOActualizarItem(producto1, 5); // Solicitar 5 de 10 disponibles
        pedido.agregarOActualizarItem(producto2, 6); // Solicitar 6 de 5 disponibles (insuficiente)
        
        // Act
        boolean disponible = inventario.verificarDisponibilidadPedido(pedido);
        
        // Assert
        assertFalse(disponible, "Debe rechazar disponibilidad cuando no hay stock suficiente");
    }
    
    @Test
    public void testReducirStockPedido() {
        // Arrange
        Pedido pedido = new Pedido();
        pedido.agregarOActualizarItem(producto1, 3);
        pedido.agregarOActualizarItem(producto2, 2);
        int stockInicialProducto1 = producto1.getCantidadEnStock();
        int stockInicialProducto2 = producto2.getCantidadEnStock();
        
        // Act
        inventario.reducirStockPedido(pedido);
        
        // Assert
        assertEquals(stockInicialProducto1 - 3, producto1.getCantidadEnStock(), 
                "El stock del producto1 debe reducirse en 3");
        assertEquals(stockInicialProducto2 - 2, producto2.getCantidadEnStock(), 
                "El stock del producto2 debe reducirse en 2");
    }
    
    @Test
    public void testBuscarProducto() {
        // Act
        Producto productoEncontrado = inventario.buscarProducto("001");
        Producto productoNoEncontrado = inventario.buscarProducto("999");
        
        // Assert
        assertNotNull(productoEncontrado, "Debe encontrar un producto existente");
        assertEquals(producto1.getId(), productoEncontrado.getId(), "El ID debe coincidir");
        assertNull(productoNoEncontrado, "Debe devolver null para un producto inexistente");
    }
}