package SistemaPedidos.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import SistemaPedidos.modelo.Producto;
import SistemaPedidos.modelo.Pedido;
import SistemaPedidos.modulos.ModuloInventario;
import SistemaPedidos.modulos.ModuloGestionPedidos;
import SistemaPedidos.comprobantes.ComprobanteEntrada;
import SistemaPedidos.comprobantes.ComprobanteSalida;
import SistemaPedidos.comprobantes.GestorComprobantes;

/**
 * Interfaz gráfica para el administrador del Sistema de Gestión de Pedidos.
 * Permite agregar nuevos productos, editar productos, ver reportes y gestionar comprobantes.
 */
public class AdminGUI extends JFrame {

    private JFrame parentFrame;
    private ModuloInventario inventario;
    private ModuloGestionPedidos gestionPedidos;
    private GestorComprobantes gestorComprobantes;

    private JTabbedPane tabbedPane;
    private JPanel agregarProductoPanel;
    private JPanel editarProductoPanel;
    private JPanel verReportesPanel;

    // Lista para almacenar productos temporales durante la creación de un comprobante de entrada
    private List<Producto> productosTemporales;
    private ComprobanteEntrada comprobanteActual;

    /**
     * Constructor que inicializa la interfaz gráfica del administrador.
     */
    public AdminGUI(JFrame parentFrame, ModuloInventario inventario, ModuloGestionPedidos gestionPedidos) {
        this.parentFrame = parentFrame;
        this.inventario = inventario;
        this.gestionPedidos = gestionPedidos;
        this.gestorComprobantes = new GestorComprobantes();
        this.productosTemporales = new ArrayList<>();

        // Configuración de la ventana
        setTitle("Sistema de Gestión de Pedidos - Administrador");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Crear el panel con pestañas
        tabbedPane = new JTabbedPane();

        // Inicializar los paneles
        agregarProductoPanel = crearPanelAgregarProducto();
        editarProductoPanel = crearPanelEditarProducto();
        verReportesPanel = crearPanelVerReportes();

        // Agregar los paneles al tabbedPane
        tabbedPane.addTab("Agregar Nuevo Producto", agregarProductoPanel);
        tabbedPane.addTab("Editar Producto", editarProductoPanel);
        tabbedPane.addTab("Ver Reportes", verReportesPanel);

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
     * Crea el panel para agregar un nuevo producto.
     */
    private JPanel crearPanelAgregarProducto() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        JLabel idLabel = new JLabel("ID del Producto:");
        JTextField idField = new JTextField(20);
        idField.setEditable(false); // ID autogenerado
        idField.setText(generarNuevoId());

        JLabel nombreLabel = new JLabel("Nombre del Producto:");
        JTextField nombreField = new JTextField(20);

        JLabel precioLabel = new JLabel("Precio del Producto:");
        JTextField precioField = new JTextField(20);
        precioField.setDocument(new javax.swing.text.PlainDocument() { 
            @Override 
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException { 
                if (str == null) return; 
        
                StringBuilder currentText = new StringBuilder(getText(0, getLength())); 
                currentText.insert(offs, str);  // Inserta el nuevo texto en la posición correcta 
        
                String newStr = currentText.toString(); 
        
                if (newStr.matches("^\\d{0,6}(\\.\\d{0,2})?$")) {  // Máx 6 dígitos enteros, 2 decimales 
                    try { 
                        double value = Double.parseDouble(newStr); 
                        if (value <= 10000) { 
                            super.insertString(offs, str, a); 
                        } 
                    } catch (NumberFormatException e) { 
                        // Ignorar si no es número válido 
                    } 
                } 
            } 
        }); 

        JLabel stockLabel = new JLabel("Cantidad en Stock:");
        JTextField stockField = new JTextField(20);
        stockField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String newStr = getText(0, getLength()) + str;
                try {
                    int value = Integer.parseInt(newStr);
                    if (value <= 100) {
                        super.insertString(offs, str, a);
                    }
                } catch (NumberFormatException e) {
                    // Ignorar entrada no numérica
                }
            }
        });

        JLabel categoriaLabel = new JLabel("Categoría:");
        JComboBox<Producto.Categoria> categoriaCombo = new JComboBox<>(Producto.Categoria.values());

        JLabel tamanoLabel = new JLabel("Tamaño:");
        JComboBox<Producto.Tamano> tamanoCombo = new JComboBox<>(Producto.Tamano.values());

        // Agregar componentes al panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nombreLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(precioLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(precioField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(stockLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(stockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(categoriaLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(categoriaCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(tamanoLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(tamanoCombo, gbc);

        // Panel para mostrar productos agregados en esta sesión
        JPanel productosAgregadosPanel = new JPanel(new BorderLayout());
        productosAgregadosPanel.setBorder(BorderFactory.createTitledBorder("Productos Agregados en esta Entrada"));

        DefaultTableModel modeloProductosAgregados = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Precio", "Stock", "Categoría", "Tamaño"}, 0);
        JTable tablaProductosAgregados = new JTable(modeloProductosAgregados);
        JScrollPane scrollPane = new JScrollPane(tablaProductosAgregados);
        productosAgregadosPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel principal con formulario y productos agregados
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(productosAgregadosPanel, BorderLayout.CENTER);

        panel.add(mainPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        JButton agregarButton = new JButton("Agregar Producto");
        JButton generarComprobanteButton = new JButton("Generar Comprobante de Entrada");
        generarComprobanteButton.setEnabled(false); // Deshabilitado hasta que se agregue al menos un producto

        buttonPanel.add(agregarButton);
        buttonPanel.add(generarComprobanteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Inicializar lista de productos temporales
        productosTemporales.clear();

        // Solo crear un nuevo comprobante si no existe uno o si el actual ya tiene productos
        if (comprobanteActual == null || !comprobanteActual.getProductosAgregados().isEmpty()) {
            comprobanteActual = gestorComprobantes.crearComprobanteEntrada();
        }

        // Configurar acciones de los botones
        agregarButton.addActionListener(e -> {
            try {
                // Validar campos
                String id = idField.getText().trim();
                String nombre = nombreField.getText().trim();
                String precioStr = precioField.getText().trim();
                String stockStr = stockField.getText().trim();
                Producto.Categoria categoria = (Producto.Categoria) categoriaCombo.getSelectedItem();
                Producto.Tamano tamano = (Producto.Tamano) tamanoCombo.getSelectedItem();

                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(panel,
                            "El nombre del producto es obligatorio.",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (precioStr.isEmpty() || stockStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel,
                            "Todos los campos son obligatorios.",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double precio = Double.parseDouble(precioStr);
                int stock = Integer.parseInt(stockStr);

                if (precio <= 0) {
                    JOptionPane.showMessageDialog(panel,
                            "El precio debe ser mayor que 0.",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (stock < 0) {
                    JOptionPane.showMessageDialog(panel,
                            "El stock no puede ser negativo.",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear y agregar el producto
                Producto nuevoProducto = new Producto(id, nombre, precio, stock, categoria, tamano);
                // Verificar si ya existe un producto con el mismo nombre
                if (inventario.existeProductoConNombre(nombre)) {
                    JOptionPane.showMessageDialog(panel,
                            "Ya existe un producto con el nombre de \"" + nombre + "\".\nNo se pueden agregar duplicados.",
                            "Nombre Duplicado",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Verificar si ya existe un producto con el mismo nombre en el inventario o en la lista temporal
                boolean existeEnTemporal = productosTemporales.stream()
                        .anyMatch(p -> p.getNombre().equalsIgnoreCase(nombre));

                if (inventario.existeProductoConNombre(nombre) || existeEnTemporal) {
                    JOptionPane.showMessageDialog(panel,
                            "Ya existe un producto con el nombre de \"" + nombre + "\".\nNo se pueden agregar duplicados.",
                            "Nombre Duplicado",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Agregar a la lista temporal y al comprobante
                productosTemporales.add(nuevoProducto);
                comprobanteActual.agregarProducto(nuevoProducto);

                // Agregar a la tabla de productos agregados
                modeloProductosAgregados.addRow(new Object[]{
                        nuevoProducto.getId(),
                        nuevoProducto.getNombre(),
                        String.format("S/ %.2f", nuevoProducto.getPrecio()),
                        nuevoProducto.getCantidadEnStock(),
                        nuevoProducto.getCategoria(),
                        nuevoProducto.getTamano()
                });

                // Habilitar el botón de generar comprobante
                generarComprobanteButton.setEnabled(true);

                // Preguntar si desea agregar otro producto
                int opcion = JOptionPane.showConfirmDialog(panel,
                        "¿Desea agregar otro producto a este registro?",
                        "Agregar Otro Producto",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (opcion == JOptionPane.YES_OPTION) {
                    // Limpiar campos y generar nuevo ID
                    // Asegurarse de generar un nuevo ID que sea mayor que el último utilizado
                    idField.setText(generarNuevoId());
                    nombreField.setText("");
                    precioField.setText("");
                    stockField.setText("");
                    categoriaCombo.setSelectedIndex(0);
                    tamanoCombo.setSelectedIndex(0);
                } else {
                    // Generar comprobante automáticamente
                    generarComprobante();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel,
                        "Error en los datos numéricos. Verifique el precio y el stock.",
                        "Error de Formato",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        generarComprobanteButton.addActionListener(e -> generarComprobante());

        return panel;
    }

    /**
     * Genera un comprobante de entrada con los productos agregados.
     */
    private void generarComprobante() {
        if (productosTemporales.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos para generar un comprobante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Agregar todos los productos al inventario
        for (Producto producto : productosTemporales) {
            inventario.agregarProducto(producto);
        }

        // Mostrar el comprobante generado
        JTextArea comprobanteArea = new JTextArea(comprobanteActual.generarComprobante());
        comprobanteArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(comprobanteArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Comprobante de Entrada Generado",
                JOptionPane.INFORMATION_MESSAGE);

        // Reiniciar para una nueva entrada
        productosTemporales.clear();
        comprobanteActual = null; // Set to null so a new one will be created when needed

        // Actualizar la interfaz
        tabbedPane.setSelectedIndex(0); // Volver a la pestaña de agregar producto

        // Refrescar el panel
        JPanel nuevoPanel = crearPanelAgregarProducto();
        tabbedPane.setComponentAt(0, nuevoPanel);
    }

    /**
     * Genera un nuevo ID para un producto basado en los IDs existentes y los productos temporales.
     * @return Nuevo ID generado
     */
    private String generarNuevoId() {
        int maxId = 0;

        // Buscar el ID numérico más alto en el inventario
        for (int i = 1; i <= 999; i++) {
            String idStr = String.format("%03d", i);
            if (inventario.buscarProducto(idStr) != null) {
                maxId = i;
            }
        }

        // También considerar los IDs de los productos temporales (los que se acaban de agregar)
        for (Producto producto : productosTemporales) {
            try {
                int id = Integer.parseInt(producto.getId());
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Ignorar IDs que no sean numéricos
            }
        }

        // Generar el siguiente ID
        return String.format("%03d", maxId + 1);
    }

    /**
     * Crea el panel para editar un producto existente.
     */
    private JPanel crearPanelEditarProducto() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel superior con instrucciones
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.add(new JLabel("Seleccione un producto del stock para editarlo"));
        panel.add(instructionsPanel, BorderLayout.NORTH);

        // Panel central con cuadrícula de productos
        JPanel productosGridPanel = new JPanel();
        productosGridPanel.setBorder(BorderFactory.createTitledBorder("Productos Disponibles"));
        productosGridPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columnas, filas automáticas
        JScrollPane scrollPane = new JScrollPane(productosGridPanel);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botón de refrescar
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refrescar Lista de Productos");
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Crear una clase anónima para manejar la actualización de la UI
        class UIUpdater {
            void actualizarProductosGrid() {
                // Limpiar el panel
                productosGridPanel.removeAll();

                // Recorrer el inventario y obtener todos los productos
                for (int i = 1; i <= 999; i++) {
                    String id = String.format("%03d", i);
                    Producto producto = inventario.buscarProducto(id);
                    if (producto != null) {
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

                        // Categoría
                        JLabel categoriaLabel = new JLabel("Categoría: " + producto.getCategoria());
                        categoriaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                        // Tamaño
                        JLabel tamanoLabel = new JLabel("Tamaño: " + producto.getTamano());
                        tamanoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
                        productoPanel.add(categoriaLabel);
                        productoPanel.add(tamanoLabel);
                        productoPanel.add(Box.createVerticalStrut(5));
                        productoPanel.add(idLabel);
                        productoPanel.add(Box.createVerticalStrut(5));

                        // Hacer que el panel sea clickeable
                        final Producto productoFinal = producto; // Para usar en el listener
                        final UIUpdater updater = this; // Referencia a esta instancia

                        productoPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                mostrarDialogoEditarProducto(productoFinal, () -> updater.actualizarProductosGrid());
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
                        productosGridPanel.add(productoPanel);
                    }
                }

                // Actualizar la vista
                productosGridPanel.revalidate();
                productosGridPanel.repaint();
            }
        }

        // Crear una instancia del actualizador
        UIUpdater updater = new UIUpdater();

        // Configurar acción del botón refrescar
        refreshButton.addActionListener(e -> updater.actualizarProductosGrid());

        // Cargar productos inicialmente
        updater.actualizarProductosGrid();

        return panel;
    }

    /**
     * Muestra un diálogo para editar un producto existente.
     * @param producto El producto a editar
     * @param actualizarUI Runnable para actualizar la UI después de editar
     */
    private void mostrarDialogoEditarProducto(Producto producto, Runnable actualizarUI) {
        // Crear un panel personalizado para el diálogo
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        JLabel idLabel = new JLabel("ID del Producto:");
        JTextField idField = new JTextField(producto.getId());
        idField.setEditable(false); // No permitir editar el ID

        JLabel nombreLabel = new JLabel("Nombre del Producto:");
        JTextField nombreField = new JTextField(producto.getNombre());

        JLabel precioLabel = new JLabel("Precio del Producto:");
        JTextField precioField = new JTextField(String.valueOf(producto.getPrecio()));
        precioField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String newStr = getText(0, getLength()) + str;
                try {
                    double value = Double.parseDouble(newStr);
                    if (value <= 10000) {
                        super.insertString(offs, str, a);
                    }
                } catch (NumberFormatException e) {
                    // Ignorar entrada no numérica
                }
            }
        });

        JLabel stockLabel = new JLabel("Cantidad en Stock:");
        JTextField stockField = new JTextField(String.valueOf(producto.getCantidadEnStock()));
        stockField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String newStr = getText(0, getLength()) + str;
                try {
                    int value = Integer.parseInt(newStr);
                    if (value <= 100) {
                        super.insertString(offs, str, a);
                    }
                } catch (NumberFormatException e) {
                    // Ignorar entrada no numérica
                }
            }
        });

        JLabel categoriaLabel = new JLabel("Categoría:");
        JComboBox<Producto.Categoria> categoriaCombo = new JComboBox<>(Producto.Categoria.values());
        categoriaCombo.setSelectedItem(producto.getCategoria());

        JLabel tamanoLabel = new JLabel("Tamaño:");
        JComboBox<Producto.Tamano> tamanoCombo = new JComboBox<>(Producto.Tamano.values());
        tamanoCombo.setSelectedItem(producto.getTamano());

        // Agregar componentes al panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nombreLabel, gbc);

        gbc.gridx = 1;
        panel.add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(precioLabel, gbc);

        gbc.gridx = 1;
        panel.add(precioField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(stockLabel, gbc);

        gbc.gridx = 1;
        panel.add(stockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(categoriaLabel, gbc);

        gbc.gridx = 1;
        panel.add(categoriaCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(tamanoLabel, gbc);

        gbc.gridx = 1;
        panel.add(tamanoCombo, gbc);


        // Mostrar el diálogo
        int result = JOptionPane.showConfirmDialog(
                this, 
                panel, 
                "Editar Producto", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE);

        // Procesar la respuesta
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = nombreField.getText().trim();
                double precio = Double.parseDouble(precioField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                Producto.Categoria categoria = (Producto.Categoria) categoriaCombo.getSelectedItem();
                Producto.Tamano tamano = (Producto.Tamano) tamanoCombo.getSelectedItem();

                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "El nombre del producto no puede estar vacío.",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validacion agregada de nombres iguales
                if (!producto.getNombre().equalsIgnoreCase(nombre) && inventario.existeProductoConNombre(nombre)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Ya existe otro producto con el nombre: " + nombre,
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (precio <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "El precio debe ser mayor que 0.",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (stock < 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "El stock no puede ser negativo.",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Actualizar el producto
                producto.setNombre(nombre);
                producto.setPrecio(precio);
                producto.setCantidadEnStock(stock);
                producto.setCategoria(categoria);
                producto.setTamano(tamano);

                JOptionPane.showMessageDialog(
                        this,
                        "Producto actualizado exitosamente.",
                        "Producto Actualizado",
                        JOptionPane.INFORMATION_MESSAGE);

                // Actualizar la UI
                actualizarUI.run();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Por favor, ingrese valores numéricos válidos para precio y stock.",
                        "Error de Formato",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * Crea el panel para ver reportes.
     */
    private JPanel crearPanelVerReportes() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel superior con pestañas para los diferentes tipos de reportes
        JTabbedPane reportesTabbedPane = new JTabbedPane();

        // Crear los paneles para cada tipo de reporte
        JPanel reporteStockPanel = crearPanelReporteStock();
        JPanel reporteComprobantesEntradaPanel = crearPanelReporteComprobantesEntrada();
        JPanel reporteComprobantesSalidaPanel = crearPanelReporteComprobantesSalida();

        // Agregar los paneles al tabbedPane
        reportesTabbedPane.addTab("Reporte de Stock General", reporteStockPanel);
        reportesTabbedPane.addTab("Reporte de Comprobantes de Entrada", reporteComprobantesEntradaPanel);
        reportesTabbedPane.addTab("Reporte de Comprobantes de Salida", reporteComprobantesSalidaPanel);

        panel.add(reportesTabbedPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel para el reporte de stock general.
     */
    private JPanel crearPanelReporteStock() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel superior con filtros
        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtrosPanel.setBorder(BorderFactory.createTitledBorder("Filtros"));

        // Filtro por categoría
        filtrosPanel.add(new JLabel("Categoría:"));
        JComboBox<String> categoriaCombo = new JComboBox<>();
        categoriaCombo.addItem("Todas");
        for (Producto.Categoria categoria : Producto.Categoria.values()) {
            categoriaCombo.addItem(categoria.toString());
        }
        filtrosPanel.add(categoriaCombo);

        // Filtro por tamaño
        filtrosPanel.add(new JLabel("Tamaño:"));
        JComboBox<String> tamanoCombo = new JComboBox<>();
        tamanoCombo.addItem("Todos");
        for (Producto.Tamano tamano : Producto.Tamano.values()) {
            tamanoCombo.addItem(tamano.toString());
        }
        filtrosPanel.add(tamanoCombo);

        // Filtro por rango de precio
        filtrosPanel.add(new JLabel("Precio desde:"));
        JTextField precioDesdeField = new JTextField(5);
        precioDesdeField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String newStr = getText(0, getLength()) + str;
                try {
                    double value = Double.parseDouble(newStr);
                    if (value >= 0 && value <= 10000) {
                        super.insertString(offs, str, a);
                    }
                } catch (NumberFormatException e) {
                    // Ignorar entrada no numérica
                }
            }
        });
        filtrosPanel.add(precioDesdeField);

        filtrosPanel.add(new JLabel("hasta:"));
        JTextField precioHastaField = new JTextField(5);
        precioHastaField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String newStr = getText(0, getLength()) + str;
                try {
                    double value = Double.parseDouble(newStr);
                    if (value >= 0 && value <= 10000) {
                        super.insertString(offs, str, a);
                    }
                } catch (NumberFormatException e) {
                    // Ignorar entrada no numérica
                }
            }
        });
        filtrosPanel.add(precioHastaField);

        // Filtro por rango de stock
        filtrosPanel.add(new JLabel("Stock desde:"));
        JTextField stockDesdeField = new JTextField(5);
        stockDesdeField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String newStr = getText(0, getLength()) + str;
                try {
                    int value = Integer.parseInt(newStr);
                    if (value >= 0 && value <= 100) {
                        super.insertString(offs, str, a);
                    }
                } catch (NumberFormatException e) {
                    // Ignorar entrada no numérica
                }
            }
        });
        filtrosPanel.add(stockDesdeField);

        filtrosPanel.add(new JLabel("hasta:"));
        JTextField stockHastaField = new JTextField(5);
        stockHastaField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String newStr = getText(0, getLength()) + str;
                try {
                    int value = Integer.parseInt(newStr);
                    if (value >= 0 && value <= 100) {
                        super.insertString(offs, str, a);
                    }
                } catch (NumberFormatException e) {
                    // Ignorar entrada no numérica
                }
            }
        });
        filtrosPanel.add(stockHastaField);

        // Botón para aplicar filtros
        JButton aplicarFiltrosButton = new JButton("Aplicar Filtros");
        filtrosPanel.add(aplicarFiltrosButton);

        panel.add(filtrosPanel, BorderLayout.NORTH);

        // Panel central con tabla de productos
        DefaultTableModel modeloProductos = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Precio", "Stock", "Categoría", "Tamaño"}, 0);
        JTable tablaProductos = new JTable(modeloProductos);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Función para cargar productos con filtros
        Runnable cargarProductos = () -> {
            modeloProductos.setRowCount(0);

            String categoriaFiltro = categoriaCombo.getSelectedItem().toString();
            String tamanoFiltro = tamanoCombo.getSelectedItem().toString();

            double precioDesde = -1;
            double precioHasta = Double.MAX_VALUE;
            int stockDesde = -1;
            int stockHasta = Integer.MAX_VALUE;

            try {
                if (!precioDesdeField.getText().trim().isEmpty()) {
                    precioDesde = Double.parseDouble(precioDesdeField.getText().trim());
                }
                if (!precioHastaField.getText().trim().isEmpty()) {
                    precioHasta = Double.parseDouble(precioHastaField.getText().trim());
                }
                if (!stockDesdeField.getText().trim().isEmpty()) {
                    stockDesde = Integer.parseInt(stockDesdeField.getText().trim());
                }
                if (!stockHastaField.getText().trim().isEmpty()) {
                    stockHasta = Integer.parseInt(stockHastaField.getText().trim());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese valores numéricos válidos para los filtros.",
                        "Error de Formato",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Recorrer el inventario y aplicar filtros
            for (int i = 1; i <= 999; i++) {
                String id = String.format("%03d", i);
                Producto producto = inventario.buscarProducto(id);
                if (producto != null) {
                    // Aplicar filtros
                    boolean pasaFiltroCategoria = categoriaFiltro.equals("Todas") || 
                            producto.getCategoria().toString().equals(categoriaFiltro);
                    boolean pasaFiltroTamano = tamanoFiltro.equals("Todos") || 
                            producto.getTamano().toString().equals(tamanoFiltro);
                    boolean pasaFiltroPrecio = producto.getPrecio() >= precioDesde && 
                            producto.getPrecio() <= precioHasta;
                    boolean pasaFiltroStock = producto.getCantidadEnStock() >= stockDesde && 
                            producto.getCantidadEnStock() <= stockHasta;

                    if (pasaFiltroCategoria && pasaFiltroTamano && pasaFiltroPrecio && pasaFiltroStock) {
                        modeloProductos.addRow(new Object[]{
                                producto.getId(),
                                producto.getNombre(),
                                String.format("S/ %.2f", producto.getPrecio()),
                                producto.getCantidadEnStock(),
                                producto.getCategoria(),
                                producto.getTamano()
                        });
                    }
                }
            }
        };

        // Configurar acción del botón aplicar filtros
        aplicarFiltrosButton.addActionListener(e -> cargarProductos.run());

        // Cargar productos inicialmente sin filtros
        cargarProductos.run();

        return panel;
    }

    /**
     * Crea el panel para el reporte de comprobantes de entrada.
     */
    private JPanel crearPanelReporteComprobantesEntrada() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel superior con opciones de ordenamiento
        JPanel opcionesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        opcionesPanel.setBorder(BorderFactory.createTitledBorder("Opciones"));

        opcionesPanel.add(new JLabel("Ordenar por fecha:"));
        JRadioButton ascendenteRadio = new JRadioButton("Ascendente");
        JRadioButton descendenteRadio = new JRadioButton("Descendente", true);
        ButtonGroup ordenGroup = new ButtonGroup();
        ordenGroup.add(ascendenteRadio);
        ordenGroup.add(descendenteRadio);
        opcionesPanel.add(ascendenteRadio);
        opcionesPanel.add(descendenteRadio);

        JButton actualizarButton = new JButton("Actualizar Reporte de Comprobante de entrada");
        opcionesPanel.add(actualizarButton);

        panel.add(opcionesPanel, BorderLayout.NORTH);

        // Panel central con lista de comprobantes
        DefaultListModel<ComprobanteEntrada> modeloComprobantes = new DefaultListModel<>();
        JList<ComprobanteEntrada> listaComprobantes = new JList<>(modeloComprobantes);
        JScrollPane scrollPane = new JScrollPane(listaComprobantes);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con detalles del comprobante seleccionado
        JTextArea detallesArea = new JTextArea();
        detallesArea.setEditable(false);
        JScrollPane detallesScrollPane = new JScrollPane(detallesArea);
        detallesScrollPane.setBorder(BorderFactory.createTitledBorder("Detalles del Comprobante"));
        detallesScrollPane.setPreferredSize(new Dimension(600, 200));
        panel.add(detallesScrollPane, BorderLayout.SOUTH);

        // Configurar selección de comprobante
        listaComprobantes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ComprobanteEntrada comprobante = listaComprobantes.getSelectedValue();
                if (comprobante != null) {
                    detallesArea.setText(comprobante.generarComprobante());
                } else {
                    detallesArea.setText("");
                }
            }
        });

        // Función para cargar comprobantes
        Runnable cargarComprobantes = () -> {
            modeloComprobantes.clear();

            List<ComprobanteEntrada> comprobantes = gestorComprobantes.getComprobantesEntradaOrdenadosPorFecha(
                    ascendenteRadio.isSelected());

            for (ComprobanteEntrada comprobante : comprobantes) {
                modeloComprobantes.addElement(comprobante);
            }
        };

        // Configurar acción del botón actualizar
        actualizarButton.addActionListener(e -> cargarComprobantes.run());

        // Cargar comprobantes inicialmente
        cargarComprobantes.run();

        return panel;
    }

    /**
     * Crea el panel para el reporte de comprobantes de salida.
     */
    private JPanel crearPanelReporteComprobantesSalida() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel superior con opciones de ordenamiento y sincronización
        JPanel opcionesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        opcionesPanel.setBorder(BorderFactory.createTitledBorder("Opciones"));

        opcionesPanel.add(new JLabel("Ordenar por fecha:"));
        JRadioButton ascendenteRadio = new JRadioButton("Ascendente");
        JRadioButton descendenteRadio = new JRadioButton("Descendente", true);
        ButtonGroup ordenGroup = new ButtonGroup();
        ordenGroup.add(ascendenteRadio);
        ordenGroup.add(descendenteRadio);
        opcionesPanel.add(ascendenteRadio);
        opcionesPanel.add(descendenteRadio);

        JButton sincronizarButton = new JButton("Sincronizar Pedidos");
        opcionesPanel.add(sincronizarButton);

        JButton actualizarButton = new JButton("Actualizar Reporte de Comprobantes de salida");
        opcionesPanel.add(actualizarButton);

        panel.add(opcionesPanel, BorderLayout.NORTH);

        // Panel central con lista de comprobantes
        DefaultListModel<ComprobanteSalida> modeloComprobantes = new DefaultListModel<>();
        JList<ComprobanteSalida> listaComprobantes = new JList<>(modeloComprobantes);
        JScrollPane scrollPane = new JScrollPane(listaComprobantes);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con detalles del comprobante seleccionado
        JTextArea detallesArea = new JTextArea();
        detallesArea.setEditable(false);
        JScrollPane detallesScrollPane = new JScrollPane(detallesArea);
        detallesScrollPane.setBorder(BorderFactory.createTitledBorder("Detalles del Comprobante"));
        detallesScrollPane.setPreferredSize(new Dimension(600, 200));
        panel.add(detallesScrollPane, BorderLayout.SOUTH);

        // Configurar selección de comprobante
        listaComprobantes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ComprobanteSalida comprobante = listaComprobantes.getSelectedValue();
                if (comprobante != null) {
                    detallesArea.setText(comprobante.generarComprobante());
                } else {
                    detallesArea.setText("");
                }
            }
        });

        // Función para cargar comprobantes
        Runnable cargarComprobantes = () -> {
            modeloComprobantes.clear();

            List<ComprobanteSalida> comprobantes = gestorComprobantes.getComprobantesSalidaOrdenadosPorFecha(
                    ascendenteRadio.isSelected());

            for (ComprobanteSalida comprobante : comprobantes) {
                modeloComprobantes.addElement(comprobante);
            }
        };

        // Configurar acción del botón sincronizar
        sincronizarButton.addActionListener(e -> {
            // Convertir el mapa de pedidos a una lista
            List<Pedido> pedidos = new ArrayList<>(gestionPedidos.getPedidosRegistrados().values());

            // Sincronizar pedidos con comprobantes de salida
            gestorComprobantes.sincronizarPedidos(pedidos);

            JOptionPane.showMessageDialog(panel,
                    "Pedidos sincronizados exitosamente.",
                    "Sincronización Completada",
                    JOptionPane.INFORMATION_MESSAGE);

            // Actualizar la lista de comprobantes
            cargarComprobantes.run();
        });

        // Configurar acción del botón actualizar
        actualizarButton.addActionListener(e -> cargarComprobantes.run());

        // Cargar comprobantes inicialmente
        cargarComprobantes.run();

        return panel;
    }

    /**
     * Crea el panel para modificar el stock de un producto.
     */
    private JPanel crearPanelModificarStock() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel superior con instrucciones
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.add(new JLabel("Haga clic en un producto para modificar su stock"));
        panel.add(instructionsPanel, BorderLayout.NORTH);

        // Panel central con cuadrícula de productos
        final JPanel productosGridPanel = new JPanel();
        productosGridPanel.setBorder(BorderFactory.createTitledBorder("Inventario Actual"));
        productosGridPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columnas, filas automáticas
        JScrollPane scrollPane = new JScrollPane(productosGridPanel);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botón de refrescar
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refrescar Inventario");
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Crear una clase anónima para manejar la actualización de la UI
        class UIUpdater {
            void actualizarProductosGrid() {
                // Limpiar el panel
                productosGridPanel.removeAll();

                // Recorrer el inventario y obtener todos los productos
                for (int i = 1; i <= 20; i++) { // Ampliado a 20 para incluir posibles nuevos productos
                    String id = String.format("%03d", i);
                    Producto producto = inventario.buscarProducto(id);
                    if (producto != null) {
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

                        // Stock (destacado)
                        JLabel stockLabel = new JLabel("Stock: " + producto.getCantidadEnStock());
                        stockLabel.setFont(new Font("Arial", Font.BOLD, 14));
                        stockLabel.setForeground(Color.BLUE);
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
                        final Producto productoFinal = producto; // Para usar en el listener
                        final UIUpdater updater = this; // Referencia a esta instancia

                        productoPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                mostrarDialogoModificarStock(productoFinal, () -> updater.actualizarProductosGrid());
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
                        productosGridPanel.add(productoPanel);
                    }
                }

                // Actualizar la vista
                productosGridPanel.revalidate();
                productosGridPanel.repaint();
            }
        }

        // Crear una instancia del actualizador
        final UIUpdater updater = new UIUpdater();

        // Configurar acción del botón refrescar
        refreshButton.addActionListener(e -> updater.actualizarProductosGrid());

        // Cargar productos inicialmente
        updater.actualizarProductosGrid();

        return panel;
    }

    /**
     * Muestra un diálogo para modificar el stock de un producto.
     * @param producto El producto seleccionado
     * @param actualizarUI Runnable para actualizar la UI después de modificar el stock
     */
    private void mostrarDialogoModificarStock(Producto producto, Runnable actualizarUI) {
        // Crear un panel personalizado para el diálogo
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

        // Información del producto
        panel.add(new JLabel("Producto: " + producto.getNombre()));
        panel.add(new JLabel("Stock Actual: " + producto.getCantidadEnStock()));
        panel.add(new JLabel("Ingrese el nuevo stock:"));

        // Campo para ingresar el nuevo stock
        JTextField stockField = new JTextField(10);
        stockField.setText(String.valueOf(producto.getCantidadEnStock())); // Valor actual como default
        panel.add(stockField);

        // Mostrar el diálogo
        int result = JOptionPane.showConfirmDialog(
                this, 
                panel, 
                "Modificar Stock", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE);

        // Procesar la respuesta
        if (result == JOptionPane.OK_OPTION) {
            try {
                int nuevoStock = Integer.parseInt(stockField.getText().trim());

                if (nuevoStock < 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "El stock no puede ser negativo.",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Actualizar el stock
                producto.setCantidadEnStock(nuevoStock);

                JOptionPane.showMessageDialog(
                        this,
                        "Stock actualizado exitosamente para " + producto.getNombre(),
                        "Stock Actualizado",
                        JOptionPane.INFORMATION_MESSAGE);

                // Actualizar la UI
                actualizarUI.run();

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
     * Crea el panel para ver todos los pedidos.
     */
    private JPanel crearPanelVerPedidos() {
        JPanel panel = new JPanel(new BorderLayout());

        // Área de texto para mostrar los pedidos
        JTextArea pedidosArea = new JTextArea();
        pedidosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(pedidosArea);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Botón para refrescar la lista de pedidos
        JButton refreshButton = new JButton("Refrescar Lista de Pedidos");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pedidosArea.setText("");

                Map<Integer, Pedido> pedidos = gestionPedidos.getPedidosRegistrados();
                if (pedidos.isEmpty()) {
                    pedidosArea.setText("(No hay pedidos registrados)");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Pedido pedido : pedidos.values()) {
                        sb.append(pedido.toString()).append("\n");
                    }
                    pedidosArea.setText(sb.toString());
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Cargar pedidos inicialmente
        refreshButton.doClick();

        return panel;
    }
}
