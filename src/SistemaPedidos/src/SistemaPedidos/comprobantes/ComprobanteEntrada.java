package SistemaPedidos.comprobantes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import SistemaPedidos.modelo.Producto;

/**
 * Clase que representa un comprobante de entrada de productos al inventario.
 * Almacena la información de los productos agregados, la fecha y hora de la entrada.
 */
public class ComprobanteEntrada {
    private static int contadorId = 0;
    private int id;
    private List<Producto> productosAgregados;
    private LocalDateTime fechaHora;

    /**
     * Constructor que inicializa un nuevo comprobante de entrada.
     */
    public ComprobanteEntrada() {
        this.id = ++contadorId;
        this.productosAgregados = new ArrayList<>();
        this.fechaHora = LocalDateTime.now();
    }

    /**
     * Agrega un producto al comprobante de entrada.
     * @param producto El producto a agregar
     */
    public void agregarProducto(Producto producto) {
        productosAgregados.add(producto);
    }

    /**
     * Obtiene el ID del comprobante.
     * @return El ID del comprobante
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene la lista de productos agregados.
     * @return Lista de productos agregados
     */
    public List<Producto> getProductosAgregados() {
        return productosAgregados;
    }

    /**
     * Obtiene la fecha y hora de la entrada.
     * @return Fecha y hora de la entrada
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Genera una representación en texto del comprobante de entrada.
     * @return Texto del comprobante
     */
    public String generarComprobante() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();

        sb.append("=== COMPROBANTE DE ENTRADA #").append(id).append(" ===\n");
        sb.append("Fecha y Hora: ").append(fechaHora.format(formatter)).append("\n\n");
        sb.append("Productos Agregados:\n");

        for (int i = 0; i < productosAgregados.size(); i++) {
            Producto p = productosAgregados.get(i);
            sb.append(i + 1).append(". ").append(p.toString()).append("\n");
        }

        sb.append("\nTotal de Productos: ").append(productosAgregados.size());
        sb.append("\n=====================================");

        return sb.toString();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "Comprobante #" + id + " - Fecha: " + fechaHora.format(formatter) + 
               " - Productos: " + productosAgregados.size();
    }
}
