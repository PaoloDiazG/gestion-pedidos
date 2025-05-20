package SistemaPedidos.modulos;

import java.util.HashMap;
import java.util.Map;
import SistemaPedidos.modelo.Producto;
import SistemaPedidos.modelo.Pedido;
import SistemaPedidos.modelo.ItemPedido;

public class ModuloInventario {
    private Map<String, Producto> inventario; // Key: Id del producto

    public ModuloInventario() {
        this.inventario = new HashMap<>();
    }
    public String generarNuevoId() {
        int maxId = 0;
        for (String id : inventario.keySet()) {
            try {
                int numero = Integer.parseInt(id);
                if (numero > maxId) {
                    maxId = numero;
                }
            } catch (NumberFormatException e) {
            }
        }
        return String.format("%03d", maxId + 1);
    }

    
    public void agregarProducto(Producto producto) {
        String nombreNuevo = producto.getNombre();
        // Verificar si ya existe un producto con el mismo nombre
        for (Producto p : inventario.values()) {
            if (p.getNombre().equalsIgnoreCase(nombreNuevo)) {
                System.out.println("[Inventario] ERROR: Ya existe un producto con el nombre: " + nombreNuevo);
                return; // No se agrega el producto
            }
        }
        inventario.put(producto.getId(), producto);
        System.out.println("[Inventario] Producto agregado/actualizado: " + producto.getNombre());
    }


    public Producto obtenerProducto(String idProducto) {
        return inventario.get(idProducto);
    }

    public boolean verificarDisponibilidadPedido(Pedido pedido) {
        System.out.println("[Inventario] Verificando stock para Pedido ID: " + pedido.getId());
        for (ItemPedido item : pedido.getItems()) {
            Producto productoEnStock = inventario.get(item.getProducto().getId());
            if (productoEnStock == null || productoEnStock.getCantidadEnStock() < item.getCantidadSolicitada()) {
                System.out.println("[Inventario] Stock insuficiente para: " + item.getProducto().getNombre()
                        + " (Necesita: " + item.getCantidadSolicitada()
                        + ", Disponible: " + (productoEnStock == null ? 0 : productoEnStock.getCantidadEnStock()) + ")");
                return false; // Stock insuficiente para al menos un item
            }
        }
        System.out.println("[Inventario] Stock disponible confirmado para el Pedido con ID: " + pedido.getId());
        return true; // Hay stock para todos los items
    }

    public void reducirStockPedido(Pedido pedido) {
        System.out.println("[Inventario] Reduciendo stock para el Pedido con ID: " + pedido.getId());
        for (ItemPedido item : pedido.getItems()) {
            Producto productoEnStock = inventario.get(item.getProducto().getId());
            if (productoEnStock != null) { // Debería existir si la verificación pasó
                int nuevaCantidad = productoEnStock.getCantidadEnStock() - item.getCantidadSolicitada();
                productoEnStock.setCantidadEnStock(nuevaCantidad);
                System.out.println("[Inventario] Stock actualizado para " + productoEnStock.getNombre() + ": " + nuevaCantidad);
            } else {
                // Esto no debería ocurrir si la lógica es correcta
                System.err.println("[Inventario] ERROR: Producto no encontrado al intentar reducir stock: " + item.getProducto().getId());
            }
        }
    }

    public void mostrarInventario() {
        System.out.println("\n--- Inventario Actual ---");
        if (inventario.isEmpty()) {
            System.out.println("(Vacío)");
        } else {
            for (Producto p : inventario.values()) {
                System.out.println(p);
            }
        }
        System.out.println("-------------------------\n");
    }

    public Producto buscarProducto(String codigo) {
        return inventario.get(codigo);
    }
    public boolean existeProductoConNombre(String nombre) {
    for (Producto producto : inventario.values()) {
        if (producto.getNombre().equalsIgnoreCase(nombre)) {
            return true;
        }
    }
    return false;
}
}
