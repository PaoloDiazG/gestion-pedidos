package test.SistemaPedidos.modelo;

import SistemaPedidos.modelo.Producto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoTest {

    @Test
    public void testCreacionProducto() {
        // Arrange
        String id = "001";
        String nombre = "Laptop Test";
        double precio = 1500.0;
        int stock = 10;
        Producto.Categoria categoria = Producto.Categoria.LAPTOPS;
        Producto.Tamano tamano = Producto.Tamano.MEDIANO;
        
        // Act
        Producto producto = new Producto(id, nombre, precio, stock, categoria, tamano);
        
        // Assert
        assertEquals(id, producto.getId(), "El ID debe coincidir");
        assertEquals(nombre, producto.getNombre(), "El nombre debe coincidir");
        assertEquals(precio, producto.getPrecio(), 0.001, "El precio debe coincidir");
        assertEquals(stock, producto.getCantidadEnStock(), "El stock debe coincidir");
        assertEquals(categoria, producto.getCategoria(), "La categoría debe coincidir");
        assertEquals(tamano, producto.getTamano(), "El tamaño debe coincidir");
    }
    
    @Test
    public void testConstructorSimplificado() {
        // Arrange
        String id = "002";
        String nombre = "Mouse Test";
        double precio = 50.0;
        int stock = 20;
        
        // Act
        Producto producto = new Producto(id, nombre, precio, stock);
        
        // Assert
        assertEquals(id, producto.getId(), "El ID debe coincidir");
        assertEquals(nombre, producto.getNombre(), "El nombre debe coincidir");
        assertEquals(precio, producto.getPrecio(), 0.001, "El precio debe coincidir");
        assertEquals(stock, producto.getCantidadEnStock(), "El stock debe coincidir");
        assertEquals(Producto.Categoria.PERIFERICOS, producto.getCategoria(), "La categoría por defecto debe ser PERIFERICOS");
        assertEquals(Producto.Tamano.MEDIANO, producto.getTamano(), "El tamaño por defecto debe ser MEDIANO");
    }
    
    @Test
    public void testSetters() {
        // Arrange
        Producto producto = new Producto("003", "Teclado Test", 100.0, 15);
        
        // Act
        producto.setId("003-updated");
        producto.setNombre("Teclado Test Updated");
        producto.setPrecio(120.0);
        producto.setCantidadEnStock(25);
        producto.setCategoria(Producto.Categoria.TECLADO);
        producto.setTamano(Producto.Tamano.PEQUENO);
        
        // Assert
        assertEquals("003-updated", producto.getId(), "El ID actualizado debe coincidir");
        assertEquals("Teclado Test Updated", producto.getNombre(), "El nombre actualizado debe coincidir");
        assertEquals(120.0, producto.getPrecio(), 0.001, "El precio actualizado debe coincidir");
        assertEquals(25, producto.getCantidadEnStock(), "El stock actualizado debe coincidir");
        assertEquals(Producto.Categoria.TECLADO, producto.getCategoria(), "La categoría actualizada debe coincidir");
        assertEquals(Producto.Tamano.PEQUENO, producto.getTamano(), "El tamaño actualizado debe coincidir");
    }
}