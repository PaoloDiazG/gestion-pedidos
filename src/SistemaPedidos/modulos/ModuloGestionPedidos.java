package SistemaPedidos.modulos;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner; // Para la interacción en consola
import SistemaPedidos.modelo.Pedido;
import SistemaPedidos.modelo.Producto;
import SistemaPedidos.modulos.ModuloInventario;
import SistemaPedidos.modulos.ModuloPagos;

public class ModuloGestionPedidos {
    private ModuloInventario moduloInventario;
    private ModuloPagos moduloPagos;
    private Map<Integer, Pedido> pedidosRegistrados; // Key: ID del Pedido
    private Scanner scanner; // Para leer la entrada del usuario

    public ModuloGestionPedidos(ModuloInventario moduloInventario, ModuloPagos moduloPagos, Scanner scanner) {
        this.moduloInventario = moduloInventario;
        this.moduloPagos = moduloPagos;
        this.pedidosRegistrados = new HashMap<>();
        this.scanner = scanner;
    }

    public Map<Integer,Pedido> getPedidosRegistrados() {
        return pedidosRegistrados;
    }
    /**
     * Guía al usuario para crear un nuevo pedido interactivamente.
     */
    public void crearYProcesarPedidoInteractivo() {
        System.out.println("\n--- Creando Nuevo Pedido ---");
        // Crear un pedido vacío al inicio
        Pedido nuevoPedido = new Pedido();

        String idProducto;
        while (true) {
            System.out.println("\n--- Inventario Disponible ---");
            moduloInventario.mostrarInventario();
            System.out.println("--- Fin Inventario ---\n");

            System.out.println("--- Pedido Actual (ID: " + nuevoPedido.getId() + ") ---");
            System.out.println(nuevoPedido); // Mostrar estado actual del pedido
            System.out.println("----------------------\n");


            System.out.print("Ingrese ID del producto a agregar/actualizar (o 'fin' para terminar): ");
            idProducto = scanner.nextLine();

            if (idProducto.equalsIgnoreCase("fin")) {
                if (nuevoPedido.getItems().isEmpty()) {
                    System.out.println("No se agregaron productos válidos. Pedido cancelado.");
                    return; // No procesar pedido vacío
                }
                break; // Terminar de agregar productos
            }

            Producto producto = moduloInventario.obtenerProducto(idProducto);
            if (producto == null) {
                System.out.println("Error: Producto con ID '" + idProducto + "' no encontrado.");
                continue;
            }

            // --- Verificación de stock CERO ---
            if (producto.getCantidadEnStock() <= 0) {
                System.out.println("Error: El producto '" + producto.getNombre() + "' está AGOTADO (Stock: 0). No se puede agregar.");
                continue; // Volver a pedir otro producto
            }

            System.out.print("Ingrese la cantidad A AGREGAR para '" + producto.getNombre() + "' (Stock disponible: " + producto.getCantidadEnStock() + "): ");
            int cantidadAAgregar; // Cambiado el nombre para claridad
            try {
                cantidadAAgregar = Integer.parseInt(scanner.nextLine());
                if (cantidadAAgregar <= 0) {
                    System.out.println("Error: La cantidad a agregar debe ser positiva.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido para la cantidad.");
                continue;
            }

            // 1. Obtener cuánto de este producto YA está en el pedido actual
            int cantidadYaEnPedido = nuevoPedido.obtenerCantidadActualDeProducto(producto.getId());

            // 2. Calcular cuánto se tendría en total si se agrega la nueva cantidad
            int cantidadTotalSolicitada = cantidadYaEnPedido + cantidadAAgregar;

            // 3. Comparar el total solicitado con el stock FÍSICO disponible
            if (producto.getCantidadEnStock() < cantidadTotalSolicitada) {
                System.out.println("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("Error: STOCK INSUFICIENTE PARA '" + producto.getNombre() + "'. ");
                System.out.println("  - Stock Físico Disponible: " + producto.getCantidadEnStock());
                System.out.println("  - Cantidad ya en este pedido: " + cantidadYaEnPedido);
                System.out.println("  - Cantidad que intenta agregar: " + cantidadAAgregar);
                System.out.println("  - Total Solicitado (" + cantidadTotalSolicitada + ") excede el stock disponible.");
                System.out.println("No se agregó ni actualizó la cantidad para este item.");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
                // No se agrega/actualiza, el bucle continúa
            } else {
                // Hay stock suficiente para el total solicitado (lo que ya estaba + lo nuevo)
                // Usa el metodo que agrega o actualiza la cantidad
                nuevoPedido.agregarOActualizarItem(producto, cantidadAAgregar);
                System.out.println("--> OK: Producto '" + producto.getNombre() + "' agregado/actualizado en el pedido.");

            }

        }

        // --- Iniciar el procesamiento del pedido (esta parte no necesita cambios) ---
        System.out.println("\n--- Revisión Final y Procesamiento del Pedido con ID: " + nuevoPedido.getId() + " ---");
        System.out.println(nuevoPedido);

        if (nuevoPedido.getItems().isEmpty()){
            System.out.println("El pedido está vacío. No se procesará.");
            return;
        }

        if (moduloInventario.verificarDisponibilidadPedido(nuevoPedido)) {
            nuevoPedido.setEstado(Pedido.EstadoPedido.PROCESANDO);
            System.out.println("Estado del Pedido " + nuevoPedido.getId() + " actualizado a: " + nuevoPedido.getEstado());

            if (moduloPagos.procesarPago(nuevoPedido)) {
                moduloInventario.reducirStockPedido(nuevoPedido);
                nuevoPedido.setEstado(Pedido.EstadoPedido.PAGADO);
                pedidosRegistrados.put(nuevoPedido.getId(), nuevoPedido);
                System.out.println("¡Pedido con ID: " + nuevoPedido.getId() + " completado y pagado exitosamente!");
                System.out.println("Estado final del Pedido: " + nuevoPedido.getEstado());
            } else {
                nuevoPedido.setEstado(Pedido.EstadoPedido.CANCELADOxPAGO);
                System.out.println("Pedido ID: " + nuevoPedido.getId() + " cancelado debido a fallo en el pago.");
                System.out.println("Estado final del Pedido: " + nuevoPedido.getEstado());
                pedidosRegistrados.put(nuevoPedido.getId(), nuevoPedido);
            }
        } else {

            nuevoPedido.setEstado(Pedido.EstadoPedido.CANCELADOxSTOCK);
            System.out.println("Pedido ID: " + nuevoPedido.getId() + " cancelado por falta de stock (detectado en verificación final).");
            System.out.println("Estado final del Pedido: " + nuevoPedido.getEstado());
            pedidosRegistrados.put(nuevoPedido.getId(), nuevoPedido);
        }
        System.out.println("--- Fin Procesamiento Pedido ID: " + nuevoPedido.getId() + " ---");
    }


    public void consultarPedidoInteractivo() {
        System.out.println("\n--- Consultar Pedido ---");
        System.out.print("Ingrese el ID del pedido a consultar: ");
        int idPedido;
        try {
            idPedido = Integer.parseInt(scanner.nextLine());
            Pedido pedido = consultarPedidoPorId(idPedido); // Llama al método testable
            if (pedido != null) {
                System.out.println("Detalles del Pedido encontrado:");
                System.out.println(pedido);
            } else {
                System.out.println("No se encontró ningún pedido registrado con el ID: " + idPedido);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: ID inválido. Debe ser un número.");
        }
    }
    public Pedido consultarPedidoPorId(int idPedido) {
        return this.pedidosRegistrados.get(idPedido); // Acceso directo al mapa
    }

    public void mostrarTodosLosPedidos() {
        System.out.println("\n--- Todos los Pedidos Registrados ---");
        if (pedidosRegistrados.isEmpty()) {
            System.out.println("(No hay pedidos registrados)");
        } else {
            for (Pedido p : pedidosRegistrados.values()) {
                System.out.println(p);
            }
        }
        System.out.println("-------------------------------------\n");
    }
}
