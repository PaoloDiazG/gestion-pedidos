package SistemaPedidos.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import SistemaPedidos.modelo.Producto;
import SistemaPedidos.modulos.ModuloInventario;
import SistemaPedidos.modulos.ModuloPagos;
import SistemaPedidos.modulos.ModuloGestionPedidos;

/**
 * Interfaz gráfica principal para el Sistema de Gestión de Pedidos.
 * Proporciona acceso a las funcionalidades del sistema a través de una GUI.
 */
public class SistemaGestionPedidosGUI extends JFrame {

    private ModuloInventario inventario;
    private ModuloPagos pagos;
    private ModuloGestionPedidos gestionPedidos;

    /**
     * Constructor que inicializa la interfaz gráfica y los módulos del sistema.
     */
    public SistemaGestionPedidosGUI() {
        // Configuración de la ventana principal
        setTitle("Sistema de Gestión de Pedidos");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Inicialización de los módulos del sistema
        inventario = new ModuloInventario();
        pagos = new ModuloPagos();
        gestionPedidos = new ModuloGestionPedidos(inventario, pagos, null); // No necesitamos Scanner aquí

        // Cargar inventario inicial (ejemplo)
        cargarInventarioInicial();

        // Configurar el panel principal con un layout de cuadrícula
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Sistema de Gestión de Pedidos", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        mainPanel.add(titleLabel, gbc);

        // Botón de Ingreso Cliente
        JButton clienteButton = new JButton("1. Simular Pedido");
        clienteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        mainPanel.add(clienteButton, gbc);

        // Botón de Ingreso Administrador
        JButton adminButton = new JButton("2. Admin Inventario");
        adminButton.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 2;
        mainPanel.add(adminButton, gbc);

        // Botón de Salir
        JButton salirButton = new JButton("3. Salir");
        salirButton.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 3;
        mainPanel.add(salirButton, gbc);

        // Agregar el panel principal a la ventana
        add(mainPanel);

        // Configurar acciones de los botones
        clienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInterfazCliente();
            }
        });

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarAdmin();
            }
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Carga el inventario inicial con productos de ejemplo.
     */
    private void cargarInventarioInicial() {
        inventario.agregarProducto(new Producto("001", "Laptop Gamer X", 4500.00, 10, Producto.Categoria.LAPTOPS, Producto.Tamano.MEDIANO));
        inventario.agregarProducto(new Producto("002", "Mouse Óptico", 55.50, 50, Producto.Categoria.MOUSE, Producto.Tamano.PEQUENO));
        inventario.agregarProducto(new Producto("003", "Teclado Mecánico", 350.00, 25, Producto.Categoria.TECLADO, Producto.Tamano.MEDIANO));
        inventario.agregarProducto(new Producto("004", "Monitor 27 pulgadas", 1200.00, 15, Producto.Categoria.MONITOR, Producto.Tamano.GRANDE));
        inventario.agregarProducto(new Producto("005", "Webcam HD", 150.00, 5, Producto.Categoria.PERIFERICOS, Producto.Tamano.PEQUENO));
        inventario.agregarProducto(new Producto("006", "Auriculares Gamer", 250.00, 20, Producto.Categoria.PERIFERICOS, Producto.Tamano.MEDIANO));
        inventario.agregarProducto(new Producto("007", "Tarjeta Gráfica NVidia", 1000.00, 5,Producto.Categoria.GPU, Producto.Tamano.PEQUENO));
        inventario.agregarProducto(new Producto("008", "Licencia Antivirus NORTON", 500.00, 10, Producto.Categoria.SOFTWARE, Producto.Tamano.PEQUENO));
        inventario.agregarProducto(new Producto("009", "Windows 11 Key", 375.00, 50, Producto.Categoria.SOFTWARE, Producto.Tamano.PEQUENO));
    }

    /**
     * Abre la interfaz de cliente.
     */
    private void abrirInterfazCliente() {
        // Ocultar la ventana principal
        setVisible(false);

        // Crear y mostrar la interfaz de cliente
        ClienteGUI clienteGUI = new ClienteGUI(this, inventario, pagos, gestionPedidos);
        clienteGUI.setVisible(true);
    }

    /**
     * Verifica las credenciales de administrador.
     */
    private void verificarAdmin() {
        String password = JOptionPane.showInputDialog(this, 
                "Ingrese la contraseña de administrador:", 
                "Verificación de Administrador", 
                JOptionPane.QUESTION_MESSAGE);

        if (password != null) {
            if (password.equals("password")) { // Contraseña hardcodeada para simplificar
                // Ocultar la ventana principal
                setVisible(false);

                // Crear y mostrar la interfaz de administrador
                AdminGUI adminGUI = new AdminGUI(this, inventario, gestionPedidos);
                adminGUI.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Contraseña incorrecta. Acceso denegado.", 
                        "Error de Autenticación", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Metodo principal que inicia la aplicación.
     */
    public static void main(String[] args) {
        // Usar SwingUtilities para asegurar que la GUI se crea en el hilo de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SistemaGestionPedidosGUI gui = new SistemaGestionPedidosGUI();
                gui.setVisible(true);
            }
        });
    }
}
