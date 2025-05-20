package SistemaPedidos.comprobantes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import SistemaPedidos.modelo.Pedido;

/**
 * Clase que gestiona los comprobantes de entrada y salida del sistema.
 * Proporciona métodos para crear, almacenar y consultar comprobantes.
 */
public class GestorComprobantes {
    private List<ComprobanteEntrada> comprobantesEntrada;
    private List<ComprobanteSalida> comprobantesSalida;

    /**
     * Constructor que inicializa las listas de comprobantes.
     */
    public GestorComprobantes() {
        this.comprobantesEntrada = new ArrayList<>();
        this.comprobantesSalida = new ArrayList<>();
    }

    /**
     * Crea un nuevo comprobante de entrada.
     * @return El comprobante de entrada creado
     */
    public ComprobanteEntrada crearComprobanteEntrada() {
        ComprobanteEntrada comprobante = new ComprobanteEntrada();
        comprobantesEntrada.add(comprobante);
        return comprobante;
    }

    /**
     * Crea un nuevo comprobante de salida asociado a un pedido.
     * @param pedido El pedido asociado al comprobante
     * @return El comprobante de salida creado
     */
    public ComprobanteSalida crearComprobanteSalida(Pedido pedido) {
        ComprobanteSalida comprobante = new ComprobanteSalida(pedido);
        comprobantesSalida.add(comprobante);
        return comprobante;
    }

    /**
     * Obtiene todos los comprobantes de entrada.
     * @return Lista de comprobantes de entrada
     */
    public List<ComprobanteEntrada> getComprobantesEntrada() {
        return comprobantesEntrada;
    }

    /**
     * Obtiene todos los comprobantes de salida.
     * @return Lista de comprobantes de salida
     */
    public List<ComprobanteSalida> getComprobantesSalida() {
        return comprobantesSalida;
    }

    /**
     * Obtiene los comprobantes de entrada ordenados por fecha.
     * @param ascendente true para orden ascendente, false para descendente
     * @return Lista ordenada de comprobantes de entrada
     */
    public List<ComprobanteEntrada> getComprobantesEntradaOrdenadosPorFecha(boolean ascendente) {
        Comparator<ComprobanteEntrada> comparador = Comparator.comparing(ComprobanteEntrada::getFechaHora);
        if (!ascendente) {
            comparador = comparador.reversed();
        }
        return comprobantesEntrada.stream()
                .sorted(comparador)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los comprobantes de salida ordenados por fecha.
     * @param ascendente true para orden ascendente, false para descendente
     * @return Lista ordenada de comprobantes de salida
     */
    public List<ComprobanteSalida> getComprobantesSalidaOrdenadosPorFecha(boolean ascendente) {
        Comparator<ComprobanteSalida> comparador = Comparator.comparing(ComprobanteSalida::getFechaHora);
        if (!ascendente) {
            comparador = comparador.reversed();
        }
        return comprobantesSalida.stream()
                .sorted(comparador)
                .collect(Collectors.toList());
    }

    /**
     * Sincroniza los pedidos con los comprobantes de salida.
     * Crea comprobantes de salida para los pedidos que no tienen uno asociado.
     * @param pedidos Lista de pedidos a sincronizar
     */
    public void sincronizarPedidos(List<Pedido> pedidos) {
        // Obtener los IDs de los pedidos que ya tienen comprobante
        List<Integer> pedidosConComprobante = comprobantesSalida.stream()
                .map(c -> c.getPedidoAsociado().getId())
                .collect(Collectors.toList());

        // Crear comprobantes para los pedidos que no tienen
        for (Pedido pedido : pedidos) {
            if (!pedidosConComprobante.contains(pedido.getId()) && 
                pedido.getEstado() == Pedido.EstadoPedido.PAGADO) {
                crearComprobanteSalida(pedido);
            }
        }
    }

    /**
     * Busca un comprobante de entrada por su ID.
     * @param id ID del comprobante a buscar
     * @return El comprobante encontrado o null si no existe
     */
    public ComprobanteEntrada buscarComprobanteEntrada(int id) {
        return comprobantesEntrada.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca un comprobante de salida por su ID.
     * @param id ID del comprobante a buscar
     * @return El comprobante encontrado o null si no existe
     */
    public ComprobanteSalida buscarComprobanteSalida(int id) {
        return comprobantesSalida.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
