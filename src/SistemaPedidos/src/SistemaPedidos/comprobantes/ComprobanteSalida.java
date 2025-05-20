package SistemaPedidos.comprobantes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import SistemaPedidos.modelo.Pedido;
import SistemaPedidos.modelo.ItemPedido;

/**
 * Clase que representa un comprobante de salida de productos del inventario.
 * Almacena la información del pedido asociado, la fecha y hora de la salida.
 */
public class ComprobanteSalida {
    private static int contadorId = 0;
    private int id;
    private Pedido pedidoAsociado;
    private LocalDateTime fechaHora;

    /**
     * Constructor que inicializa un nuevo comprobante de salida.
     * @param pedido El pedido asociado a este comprobante
     */
    public ComprobanteSalida(Pedido pedido) {
        this.id = ++contadorId;
        this.pedidoAsociado = pedido;
        this.fechaHora = LocalDateTime.now();
    }

    /**
     * Obtiene el ID del comprobante.
     * @return El ID del comprobante
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el pedido asociado al comprobante.
     * @return El pedido asociado
     */
    public Pedido getPedidoAsociado() {
        return pedidoAsociado;
    }

    /**
     * Obtiene la fecha y hora de la salida.
     * @return Fecha y hora de la salida
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Genera una representación en texto del comprobante de salida.
     * @return Texto del comprobante
     */
    public String generarComprobante() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();

        sb.append("=== COMPROBANTE DE SALIDA #").append(id).append(" ===\n");
        sb.append("Fecha y Hora: ").append(fechaHora.format(formatter)).append("\n\n");
        sb.append("Pedido ID: ").append(pedidoAsociado.getId()).append("\n");
        sb.append("Estado: ").append(pedidoAsociado.getEstado()).append("\n\n");
        sb.append("Productos:\n");

        for (int i = 0; i < pedidoAsociado.getItems().size(); i++) {
            ItemPedido item = pedidoAsociado.getItems().get(i);
            sb.append(i + 1).append(". ")
              .append(item.getProducto().getNombre())
              .append(" (x").append(item.getCantidadSolicitada()).append(")")
              .append(" - S/ ").append(String.format("%.2f", item.calcularSubtotal()))
              .append("\n");
        }

        sb.append("\nTotal del Pedido: S/ ").append(String.format("%.2f", pedidoAsociado.getTotal()));
        sb.append("\n=====================================");

        return sb.toString();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "Comprobante de Salida #" + id + " - Pedido #" + pedidoAsociado.getId() + 
               " - Fecha: " + fechaHora.format(formatter) + 
               " - Total: S/ " + String.format("%.2f", pedidoAsociado.getTotal());
    }
}
