package SistemaPedidos.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import SistemaPedidos.modelo.Producto;
import SistemaPedidos.modelo.Pedido;
import SistemaPedidos.modelo.ItemPedido;
import SistemaPedidos.modulos.ModuloInventario;
import SistemaPedidos.modulos.ModuloPagos;
import SistemaPedidos.modulos.ModuloGestionPedidos;

/**
 * Interfaz gráfica para el cliente del Sistema de Gestión de Pedidos.
 * Permite crear pedidos, consultar pedidos y ver el inventario.
 */
public class ClienteGUI extends JFrame {

    private JFrame parentFrame;
    private ModuloInventario inventario;
    private ModuloPagos pagos;
    private ModuloGestionPedidos gestionPedidos;

    private JTabbedPane tabbedPane;
    private JPanel crearPedidoPanel;
    private JPanel consultarPedidoPanel;
    private JPanel verInventarioPanel;

    // Componentes para crear pedido
    private JTable productosTable;
    private DefaultTableModel productosTableModel;
    private JTable pedidoActualTable;
    private DefaultTableModel pedidoActualTableModel;
    private JLabel totalLabel;
    private Pedido pedidoActual;
    private Map<String, Integer> cantidadesSeleccionadas;

    /**
     * Constructor que inicializa la interfaz gráfica del cliente.
     */
    public ClienteGUI(JFrame parentFrame, ModuloInventario inventario, ModuloPagos pagos, ModuloGestionPedidos gestionPedidos) {
        this.parentFrame = parentFrame;
        this.inventario = inventario;
        this.pagos = pagos;
        this.gestionPedidos = gestionPedidos;
        this.cantidadesSeleccionadas = new HashMap<>();

        // Configuración de la ventana
        setTitle("Sistema de Gestión de Pedidos - Cliente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel con pestañas
        tabbedPane = new JTabbedPane();

        // Inicializar los paneles
        crearPedidoPanel = crearPanelCrearPedido();
        consultarPedidoPanel = crearPanelConsultarPedido();
        verInventarioPanel = crearPanelVerInventario();

        // Agregar los paneles al tabbedPane
        tabbedPane.addTab("Crear Pedido", crearPedidoPanel);
        tabbedPane.addTab("Consultar Pedido", consultarPedidoPanel);
        tabbedPane.addTab("Ver Inventario", verInventarioPanel);

        // Agregar el tabbedPane a la ventana
        add(tabbedPane);

        // Agregar botón para volver al menú principal
        JButton volverButton = new JButton("Volver al Menú Principal");
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                parentFrame.setVisible(true);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(volverButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Crea el panel para crear un nuevo pedido.
     */
    private JPanel crearPanelCrearPedido() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel superior con instrucciones
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.add(new JLabel("Haga clic en un producto para agregarlo al pedido"));
        panel.add(instructionsPanel, BorderLayout.NORTH);

        // Panel central con cuadrícula de productos y pedido actual
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Panel de productos en cuadrícula
        JPanel productosGridPanel = new JPanel();
        productosGridPanel.setBorder(BorderFactory.createTitledBorder("Productos Disponibles"));
        productosGridPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columnas, filas automáticas
        JScrollPane productosScrollPane = new JScrollPane(productosGridPanel);
        mainPanel.add(productosScrollPane);

        // Tabla del pedido actual
        pedidoActualTableModel = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Precio", "Cantidad", "Subtotal"}, 0);
        pedidoActualTable = new JTable(pedidoActualTableModel);
        JScrollPane pedidoScrollPane = new JScrollPane(pedidoActualTable);
        pedidoScrollPane.setBorder(BorderFactory.createTitledBorder("Pedido Actual"));
        mainPanel.add(pedidoScrollPane);

        panel.add(mainPanel, BorderLayout.CENTER);

        // Panel inferior con botones y total
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel buttonsPanel = new JPanel();
        JButton procesarButton = new JButton("Procesar Pedido");
        JButton nuevoButton = new JButton("Nuevo Pedido");

        buttonsPanel.add(procesarButton);
        buttonsPanel.add(nuevoButton);

        bottomPanel.add(buttonsPanel, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel();
        totalLabel = new JLabel("Total: S/ 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);

        bottomPanel.add(totalPanel, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Inicializar un nuevo pedido
        iniciarNuevoPedido();

        // Cargar productos en la cuadrícula
        cargarProductosEnCuadricula(productosGridPanel);

        // Configurar acciones de los botones
        procesarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarPedido();
            }
        });

        nuevoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarNuevoPedido();
                cargarProductosEnCuadricula(productosGridPanel);
            }
        });

        return panel;
    }

    /**
     * Crea el panel para consultar un pedido existente.
     */
    private JPanel crearPanelConsultarPedido() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel superior con campo de búsqueda
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("ID del Pedido:"));
        JTextField idField = new JTextField(10);
        searchPanel.add(idField);
        JButton buscarButton = new JButton("Buscar");
        searchPanel.add(buscarButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Panel central con resultados
        JTextArea resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Configurar acción del botón buscar
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int idPedido = Integer.parseInt(idField.getText());
                    Pedido pedido = gestionPedidos.consultarPedidoPorId(idPedido);

                    if (pedido != null) {
                        resultadoArea.setText(pedido.toString());
                    } else {
                        resultadoArea.setText("No se encontró ningún pedido con el ID: " + idPedido);
                    }
                } catch (NumberFormatException ex) {
                    resultadoArea.setText("Error: ID inválido. Debe ser un número.");
                }
            }
        });

        return panel;
    }

    /**
     * Crea el panel para ver el inventario actual.
     */
    private JPanel crearPanelVerInventario() {
        JPanel panel = new JPanel(new BorderLayout());

        // Crear modelo de tabla para el inventario
        DefaultTableModel inventarioTableModel = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Precio", "Stock"}, 0);
        JTable inventarioTable = new JTable(inventarioTableModel);
        JScrollPane scrollPane = new JScrollPane(inventarioTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Botón para refrescar inventario
        JButton refreshButton = new JButton("Refrescar Inventario");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Limpiar tabla
                inventarioTableModel.setRowCount(0);

                // Obtener todos los productos del inventario
                for (Producto producto : obtenerTodosLosProductos()) {
                    inventarioTableModel.addRow(new Object[]{
                            producto.getId(),
                            producto.getNombre(),
                            String.format("S/ %.2f", producto.getPrecio()),
                            producto.getCantidadEnStock()
                    });
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Cargar inventario inicial
        refreshButton.doClick();

        return panel;
    }

    /**
     * Inicia un nuevo pedido y limpia la tabla del pedido actual.
     */
    private void iniciarNuevoPedido() {
        pedidoActual = new Pedido();
        pedidoActualTableModel.setRowCount(0);
        cantidadesSeleccionadas.clear();
        actualizarTotal();
    }

    /**
     * Carga los productos del inventario en la tabla de productos.
     */
    private void cargarProductosEnTabla() {
        productosTableModel.setRowCount(0);

        for (Producto producto : obtenerTodosLosProductos()) {
            productosTableModel.addRow(new Object[]{
                    producto.getId(),
                    producto.getNombre(),
                    String.format("S/ %.2f", producto.getPrecio()),
                    producto.getCantidadEnStock(),
                    0
            });
        }
    }

    /**
     * Carga los productos del inventario en una cuadrícula visual.
     * @param gridPanel El panel donde se mostrarán los productos en cuadrícula
     */
    private void cargarProductosEnCuadricula(JPanel gridPanel) {
        // Limpiar el panel
        gridPanel.removeAll();

        // Obtener todos los productos
        List<Producto> productos = obtenerTodosLosProductos();

        // Crear un panel para cada producto
        for (Producto producto : productos) {
            // Crear un panel para el producto con borde
            JPanel productoPanel = new JPanel();
            productoPanel.setLayout(new BoxLayout(productoPanel, BoxLayout.Y_AXIS));
            productoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            productoPanel.setBackground(Color.WHITE);

            // Nombre del producto (en negrita)
            JLabel nombreLabel = new JLabel(producto.getNombre());
            nombreLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Precio
            JLabel precioLabel = new JLabel("Precio: S/ " + String.format("%.2f", producto.getPrecio()));
            precioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Stock
            JLabel stockLabel = new JLabel("Stock: " + producto.getCantidadEnStock());
            stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // ID (pequeño, para referencia)
            JLabel idLabel = new JLabel("ID: " + producto.getId());
            idLabel.setFont(new Font("Arial", Font.ITALIC, 10));
            idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Agregar componentes al panel del producto
            productoPanel.add(Box.createVerticalStrut(5));
            productoPanel.add(nombreLabel);
            productoPanel.add(Box.createVerticalStrut(5));
            productoPanel.add(precioLabel);
            productoPanel.add(stockLabel);
            productoPanel.add(Box.createVerticalStrut(5));
            productoPanel.add(idLabel);
            productoPanel.add(Box.createVerticalStrut(5));

            // Hacer que el panel sea clickeable
            productoPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    mostrarDialogoCantidad(producto);
                }

                // Cambiar el color cuando el mouse está sobre el panel
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    productoPanel.setBackground(new Color(230, 230, 250)); // Lavender
                    productoPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    productoPanel.setBackground(Color.WHITE);
                    productoPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

            // Agregar el panel del producto a la cuadrícula
            gridPanel.add(productoPanel);
        }

        // Actualizar la vista
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /**
     * Muestra un diálogo para ingresar la cantidad de un producto a agregar al pedido.
     * @param producto El producto seleccionado
     */
    private void mostrarDialogoCantidad(Producto producto) {
        // Crear un panel personalizado para el diálogo
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

        // Información del producto
        panel.add(new JLabel("Producto: " + producto.getNombre()));
        panel.add(new JLabel("Stock: " + producto.getCantidadEnStock()));
        panel.add(new JLabel("¿Cuántos deseas pedir?"));

        // Campo para ingresar la cantidad
        JTextField cantidadField = new JTextField(10);
        panel.add(cantidadField);

        // Mostrar el diálogo
        int result = JOptionPane.showConfirmDialog(
                this, 
                panel, 
                "Agregar Producto", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE);

        // Procesar la respuesta
        if (result == JOptionPane.OK_OPTION) {
            try {
                int cantidad = Integer.parseInt(cantidadField.getText().trim());

                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "La cantidad debe ser mayor que cero.",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verificar stock
                int cantidadYaEnPedido = pedidoActual.obtenerCantidadActualDeProducto(producto.getId());
                int cantidadTotal = cantidadYaEnPedido + cantidad;

                if (producto.getCantidadEnStock() >= cantidadTotal) {
                    pedidoActual.agregarOActualizarItem(producto, cantidad);
                    cantidadesSeleccionadas.put(producto.getId(), cantidadTotal);

                    // Actualizar la tabla del pedido actual
                    actualizarTablaPedidoActual();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Stock insuficiente para " + producto.getNombre() + "\n" +
                                    "Stock disponible: " + producto.getCantidadEnStock() + "\n" +
                                    "Cantidad ya en pedido: " + cantidadYaEnPedido + "\n" +
                                    "Cantidad solicitada: " + cantidad,
                            "Error de Stock",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Por favor, ingrese un número válido.",
                        "Error de Formato",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Obtiene todos los productos del inventario.
     */
    private List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();

        // Recorrer el inventario y obtener todos los productos
        for (int i = 1; i <= 999; i++) {
            String id = String.format("%03d", i);
            Producto producto = inventario.buscarProducto(id);
            if (producto != null) {
                productos.add(producto);
            }
        }

        return productos;
    }

    /**
     * Agrega los productos seleccionados al pedido actual.
     */
    private void agregarProductosAlPedido() {
        for (int i = 0; i < productosTableModel.getRowCount(); i++) {
            String id = (String) productosTableModel.getValueAt(i, 0);
            int cantidad = 0;

            try {
                cantidad = Integer.parseInt(productosTableModel.getValueAt(i, 4).toString());
            } catch (NumberFormatException e) {
                // Ignorar si no es un número válido
            }

            if (cantidad > 0) {
                Producto producto = inventario.buscarProducto(id);

                if (producto != null) {
                    // Verificar stock
                    int cantidadYaEnPedido = pedidoActual.obtenerCantidadActualDeProducto(id);
                    int cantidadTotal = cantidadYaEnPedido + cantidad;

                    if (producto.getCantidadEnStock() >= cantidadTotal) {
                        pedidoActual.agregarOActualizarItem(producto, cantidad);
                        cantidadesSeleccionadas.put(id, cantidadTotal);

                        // Resetear la cantidad en la tabla de productos
                        productosTableModel.setValueAt(0, i, 4);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Stock insuficiente para " + producto.getNombre() + "\n" +
                                        "Stock disponible: " + producto.getCantidadEnStock() + "\n" +
                                        "Cantidad ya en pedido: " + cantidadYaEnPedido + "\n" +
                                        "Cantidad solicitada: " + cantidad,
                                "Error de Stock",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        // Actualizar la tabla del pedido actual
        actualizarTablaPedidoActual();
    }

    /**
     * Actualiza la tabla del pedido actual con los items del pedido.
     */
    private void actualizarTablaPedidoActual() {
        pedidoActualTableModel.setRowCount(0);

        for (ItemPedido item : pedidoActual.getItems()) {
            Producto producto = item.getProducto();
            pedidoActualTableModel.addRow(new Object[]{
                    producto.getId(),
                    producto.getNombre(),
                    String.format("S/ %.2f", producto.getPrecio()),
                    item.getCantidadSolicitada(),
                    String.format("S/ %.2f", item.calcularSubtotal())
            });
        }

        actualizarTotal();
    }

    /**
     * Actualiza el label del total del pedido.
     */
    private void actualizarTotal() {
        totalLabel.setText("Total: S/ " + String.format("%.2f", pedidoActual.getTotal()));
    }

    /**
     * Procesa el pedido actual.
     */
    private void procesarPedido() {
        if (pedidoActual.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El pedido está vacío. Agregue productos antes de procesar.",
                    "Pedido Vacío",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar disponibilidad
        if (inventario.verificarDisponibilidadPedido(pedidoActual)) {
            pedidoActual.setEstado(Pedido.EstadoPedido.PROCESANDO);

            // Procesar pago
            if (pagos.procesarPago(pedidoActual)) {
                inventario.reducirStockPedido(pedidoActual);
                pedidoActual.setEstado(Pedido.EstadoPedido.PAGADO);
                gestionPedidos.getPedidosRegistrados().put(pedidoActual.getId(), pedidoActual);

                JOptionPane.showMessageDialog(this,
                        "¡Pedido con ID: " + pedidoActual.getId() + " completado y pagado exitosamente!\n" +
                                "Estado final del Pedido: " + pedidoActual.getEstado(),
                        "Pedido Completado",
                        JOptionPane.INFORMATION_MESSAGE);

                // Iniciar nuevo pedido
                iniciarNuevoPedido();

                // Refrescar la cuadrícula de productos
                JPanel productosGridPanel = null;
                for (Component comp : ((JPanel)crearPedidoPanel.getComponent(1)).getComponents()) {
                    if (comp instanceof JScrollPane) {
                        Component view = ((JScrollPane)comp).getViewport().getView();
                        if (view instanceof JPanel) {
                            productosGridPanel = (JPanel)view;
                            break;
                        }
                    }
                }

                if (productosGridPanel != null) {
                    cargarProductosEnCuadricula(productosGridPanel);
                }
            } else {
                pedidoActual.setEstado(Pedido.EstadoPedido.CANCELADOxPAGO);
                gestionPedidos.getPedidosRegistrados().put(pedidoActual.getId(), pedidoActual);

                JOptionPane.showMessageDialog(this,
                        "Pedido ID: " + pedidoActual.getId() + " cancelado debido a fallo en el pago.\n" +
                                "Estado final del Pedido: " + pedidoActual.getEstado(),
                        "Pago Fallido",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            pedidoActual.setEstado(Pedido.EstadoPedido.CANCELADOxSTOCK);
            gestionPedidos.getPedidosRegistrados().put(pedidoActual.getId(), pedidoActual);

            JOptionPane.showMessageDialog(this,
                    "Pedido ID: " + pedidoActual.getId() + " cancelado por falta de stock.\n" +
                            "Estado final del Pedido: " + pedidoActual.getEstado(),
                    "Stock Insuficiente",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
