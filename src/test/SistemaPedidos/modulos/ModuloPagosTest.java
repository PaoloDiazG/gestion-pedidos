package test.SistemaPedidos.modulos;

import SistemaPedidos.modelo.Pedido;
import SistemaPedidos.modelo.Producto;
import SistemaPedidos.modulos.ModuloPagos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModuloPagosTest {
    
    private ModuloPagos moduloPagos;
    private Pedido pedido;
    
    @BeforeEach
    public void setUp() {
        moduloPagos = new ModuloPagos();
        pedido = new Pedido();
        
        // Agregar algunos productos al pedido para las pruebas
        Producto producto = new Producto("001", "Laptop Test", 1500.0, 10);
        pedido.agregarOActualizarItem(producto, 2);
    }
    
    @Test
    public void testProcesarPagoComportamientoNormal() {
        // Arrange
        moduloPagos.resetearComportamiento(); // Asegurar comportamiento normal (aleatorio)
        
        // Act
        boolean resultado = moduloPagos.procesarPago(pedido);
        
        // Assert
        // No podemos afirmar un resultado específico ya que es aleatorio,
        // pero podemos verificar que el método no lance excepciones
        // y que el resultado sea un booleano
        assertNotNull(resultado, "El resultado no debe ser null");
    }
    
    @Test
    public void testProcesarPagoForzadoExitoso() {
        // Arrange
        moduloPagos.setForzarResultado(true); // Forzar éxito
        
        // Act
        boolean resultado = moduloPagos.procesarPago(pedido);
        
        // Assert
        assertTrue(resultado, "El pago debe ser exitoso cuando se fuerza a true");
    }
    
    @Test
    public void testProcesarPagoForzadoFallido() {
        // Arrange
        moduloPagos.setForzarResultado(false); // Forzar fallo
        
        // Act
        boolean resultado = moduloPagos.procesarPago(pedido);
        
        // Assert
        assertFalse(resultado, "El pago debe fallar cuando se fuerza a false");
    }
    
    @Test
    public void testResetearComportamiento() {
        // Arrange
        moduloPagos.setForzarResultado(true); // Primero forzamos un resultado
        
        // Act
        moduloPagos.resetearComportamiento(); // Luego reseteamos
        
        // No podemos probar directamente el estado interno,
        // pero podemos verificar que el comportamiento vuelve a ser aleatorio
        // indirectamente verificando que el método no lance excepciones
        
        // Assert (implícito)
        moduloPagos.procesarPago(pedido); // No debería lanzar excepciones
    }
}