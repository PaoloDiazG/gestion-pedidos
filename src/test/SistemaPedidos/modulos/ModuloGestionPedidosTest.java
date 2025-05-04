package test.SistemaPedidos.modulos;

import SistemaPedidos.modelo.Pedido;
import SistemaPedidos.modelo.Producto;
import SistemaPedidos.modulos.ModuloGestionPedidos;
import SistemaPedidos.modulos.ModuloInventario;
import SistemaPedidos.modulos.ModuloPagos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModuloGestionPedidosTest {
    
    private ModuloInventario moduloInventario;
    private ModuloPagos moduloPagos;
    private ModuloGestionPedidos moduloGestionPedidos;
    private Producto producto1;
    private Producto producto2;
    
    @BeforeEach
    public void setUp() {
        moduloInventario = new ModuloInventario();
        moduloPagos = new ModuloPagos();
        // Pasamos null como scanner ya que no usaremos métodos interactivos en las pruebas
        moduloGestionPedidos = new ModuloGestionPedidos(moduloInventario, moduloPagos, null);
        
        // Configurar productos de prueba
        producto1 = new Producto("001", "Laptop Test", 1500.0, 10);
        producto2 = new Producto("002", "Mouse Test", 50.0, 5);
        
        // Agregar productos al inventario
        moduloInventario.agregarProducto(producto1);
        moduloInventario.agregarProducto(producto2);
    }
    
    @Test
    public void testConsultarPedidoPorId_PedidoExistente() {
        // Arrange
        // Crear un pedido manualmente y agregarlo a los pedidos registrados
        Pedido pedido = new Pedido();
        pedido.agregarOActualizarItem(producto1, 2);
        
        // Forzar el pago exitoso para que el pedido se registre
        moduloPagos.setForzarResultado(true);
        
        // Procesar el pedido (esto lo registrará en el mapa de pedidos)
        if (moduloInventario.verificarDisponibilidadPedido(pedido)) {
            if (moduloPagos.procesarPago(pedido)) {
                moduloInventario.reducirStockPedido(pedido);
                pedido.setEstado(Pedido.EstadoPedido.PAGADO);
                moduloGestionPedidos.getPedidosRegistrados().put(pedido.getId(), pedido);
            }
        }
        
        // Act
        Pedido pedidoConsultado = moduloGestionPedidos.consultarPedidoPorId(pedido.getId());
        
        // Assert
        assertNotNull(pedidoConsultado, "Debe encontrar el pedido registrado");
        assertEquals(pedido.getId(), pedidoConsultado.getId(), "El ID del pedido debe coincidir");
        assertEquals(Pedido.EstadoPedido.PAGADO, pedidoConsultado.getEstado(), "El estado debe ser PAGADO");
    }
    
    @Test
    public void testConsultarPedidoPorId_PedidoInexistente() {
        // Act
        Pedido pedidoConsultado = moduloGestionPedidos.consultarPedidoPorId(999); // ID que no existe
        
        // Assert
        assertNull(pedidoConsultado, "Debe devolver null para un pedido inexistente");
    }
    
    @Test
    public void testPedidosRegistradosInicialmenteVacio() {
        // Act & Assert
        assertTrue(moduloGestionPedidos.getPedidosRegistrados().isEmpty(), 
                "La lista de pedidos debe estar vacía inicialmente");
    }
}