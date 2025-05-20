package SistemaPedidos.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pedido {
    public enum EstadoPedido {
        PENDIENTE, PROCESANDO, PAGADO, CANCELADOxSTOCK, CANCELADOxPAGO
    }

    private static int contadorId = 0;
    private int id;
    private List<ItemPedido> items;
    private double total;
    private EstadoPedido estado;

    public Pedido() {
        this.id = ++contadorId;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.estado = EstadoPedido.PENDIENTE;
    }

    public void agregarOActualizarItem(Producto producto, int cantidad) {
        // Buscar si ya existe un SistemaPedidos.ItemPedido para este producto
        for (ItemPedido item : items) {
            // Comparar por ID de producto
            if (Objects.equals(item.getProducto().getId(), producto.getId())) {
                // SistemaPedidos.Producto encontrado, actualizar cantidad (sumar la nueva cantidad)
                item.setCantidadSolicitada(item.getCantidadSolicitada() + cantidad);
                calcularTotal(); // Recalcular total
                System.out.println("   [SistemaPedidos.Pedido] Cantidad actualizada para " + producto.getNombre() + " a " + item.getCantidadSolicitada());
                return; // Salir del método, ya se actualizó
            }
        }

        ItemPedido nuevoItem = new ItemPedido(producto, cantidad);
        this.items.add(nuevoItem);
        System.out.println("   [Pedido] Nuevo item agregado: " + producto.getNombre() + " (x" + cantidad + ")");
        calcularTotal(); // Recalcular total
    }


    public int obtenerCantidadActualDeProducto(String productoId) {
        for (ItemPedido item : items) {
            if (Objects.equals(item.getProducto().getId(), productoId)) {
                return item.getCantidadSolicitada();
            }
        }
        return 0;
    }


    public void calcularTotal() {
        this.total = 0.0;
        for (ItemPedido item : items) {
            this.total += item.calcularSubtotal();
        }
    }

    public int getId() { return id; }
    public List<ItemPedido> getItems() { return items; }
    public double getTotal() { return total; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Pedido ID: ").append(id).append(" ---\n");
        sb.append("Estado: ").append(estado).append("\n");
        sb.append("Items:\n");
        if (items.isEmpty()) {
            sb.append("  (Vacío)\n");
        } else {
            for (ItemPedido item : items) {
                sb.append("  - ").append(item).append("\n");
            }
        }
        sb.append("Total Pedido: S/").append(String.format("%.2f", total)).append("\n");
        sb.append("----------------------\n");
        return sb.toString();
    }
}
