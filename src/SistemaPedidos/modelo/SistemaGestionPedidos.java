package SistemaPedidos;

import SistemaPedidos.modelo.Producto;
import SistemaPedidos.modulos.ModuloGestionPedidos;
import SistemaPedidos.modulos.ModuloInventario;
import SistemaPedidos.modulos.ModuloPagos;

import java.util.Scanner;

public class SistemaGestionPedidos {
    private static final String ADMIN_PASSWORD = "password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // --- Inicialización de Módulos ---
        ModuloInventario inventario = new ModuloInventario();
        ModuloPagos pagos = new ModuloPagos();
        ModuloGestionPedidos gestionPedidos = new ModuloGestionPedidos(inventario, pagos, scanner);

        // --- Cargar Inventario Inicial (Ejemplo) ---
        inventario.agregarProducto(new Producto("001", "Laptop Gamer X", 4500.00, 10, Producto.Categoria.LAPTOPS, Producto.Tamano.MEDIANO));
        inventario.agregarProducto(new Producto("002", "Mouse Óptico", 55.50, 50, Producto.Categoria.MOUSE, Producto.Tamano.PEQUENO));
        inventario.agregarProducto(new Producto("003", "Teclado Mecánico", 350.00, 25, Producto.Categoria.TECLADO, Producto.Tamano.MEDIANO));
        inventario.agregarProducto(new Producto("004", "Monitor 27 pulgadas", 1200.00, 15, Producto.Categoria.MONITOR, Producto.Tamano.GRANDE));
        inventario.agregarProducto(new Producto("005", "Webcam HD", 150.00, 5, Producto.Categoria.PERIFERICOS, Producto.Tamano.PEQUENO));
        inventario.agregarProducto(new Producto("006", "Auriculares Gamer", 250.00, 20, Producto.Categoria.PERIFERICOS, Producto.Tamano.MEDIANO));
        inventario.agregarProducto(new Producto("007", "Tarjeta Gráfica NVidia", 1000.00, 5,Producto.Categoria.GPU, Producto.Tamano.PEQUENO));
        inventario.agregarProducto(new Producto("008", "Licencia Antivirus NORTON", 500.00, 10, Producto.Categoria.SOFTWARE, Producto.Tamano.PEQUENO));
        inventario.agregarProducto(new Producto("009", "Windows 11 Key", 375.00, 50, Producto.Categoria.SOFTWARE, Producto.Tamano.PEQUENO));

        // --- Menú Principal en Consola ---
        int opcion = 0;
        do {
            mostrarMenu();
            try {
                System.out.print("Seleccione una opción: ");
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        gestionPedidos.crearYProcesarPedidoInteractivo();
                        break;
                    case 2:
                        gestionPedidos.consultarPedidoInteractivo();
                        break;
                    case 3:
                        inventario.mostrarInventario();
                        break;
                    case 4:
                        gestionPedidos.mostrarTodosLosPedidos();
                        break;
                    case 5:
                        if (verificarAdmin(scanner)) {
                            menuAdministrador(scanner, inventario);
                        }
                        break;
                    case 6:
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número válido como opción.");
                opcion = 0;
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                e.printStackTrace(); // Útil para depuración
                opcion = 0;
            }
            if (opcion != 5) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }

        } while (opcion != 6);

        scanner.close(); // Cerrar el scanner al final
        System.out.println("Sistema cerrado.");
    }

    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("          Sistema de Gestión de Pedidos          ");
        System.out.println("========================================");
        System.out.println("  1. Crear y Procesar Nuevo Pedido      ");
        System.out.println("  2. Consultar Pedido por ID            ");
        System.out.println("  3. Ver Inventario Actual               ");
        System.out.println("  4. Ver Todos los Pedidos Registrados   ");
        System.out.println("  5. Ingreso Administrador              ");
        System.out.println("  6. Salir                               ");
        System.out.println("========================================");
    }

    private static boolean verificarAdmin(Scanner scanner) {
        System.out.print("Ingrese la contraseña de administrador: ");
        String password = scanner.nextLine();
        if (password.equals(ADMIN_PASSWORD)) {
            System.out.println("Acceso concedido!");
            return true;
        } else {
            System.out.println("Contraseña incorrecta. Acceso denegado.");
            return false;
        }
    }

    private static void menuAdministrador(Scanner scanner, ModuloInventario inventario) {
        int opcion;
        do {
            System.out.println("\n=== Menú de Administrador ===");
            System.out.println("1. Agregar nuevo producto");
            System.out.println("2. Modificar stock de producto");
            System.out.println("3. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        agregarNuevoProducto(scanner, inventario);
                        break;
                    case 2:
                        modificarStockProducto(scanner, inventario);
                        break;
                    case 3:
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
                opcion = 0;
            }
        } while (opcion != 3);
    }

    private static void agregarNuevoProducto(Scanner scanner, ModuloInventario inventario) {
        System.out.println("\n=== Agregar Nuevo Producto ===");
        
        System.out.print("Ingrese el código del producto: ");
        String codigo = scanner.nextLine();
        
        System.out.print("Ingrese el nombre del producto: ");
        String nombre = scanner.nextLine();
        
        double precio = 0;
        do {
            try {
                System.out.print("Ingrese el precio del producto: ");
                precio = Double.parseDouble(scanner.nextLine());
                if (precio <= 0) {
                    System.out.println("El precio debe ser mayor que 0");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido");
            }
        } while (precio <= 0);
        
        int stock = 0;
        do {
            try {
                System.out.print("Ingrese la cantidad en stock: ");
                stock = Integer.parseInt(scanner.nextLine());
                if (stock < 0) {
                    System.out.println("El stock no puede ser negativo");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido");
            }
        } while (stock < 0);

        Producto nuevoProducto = new Producto(codigo, nombre, precio, stock);
        inventario.agregarProducto(nuevoProducto);
        System.out.println("\nProducto agregado exitosamente!");
    }

    private static void modificarStockProducto(Scanner scanner, ModuloInventario inventario) {
        System.out.println("\n=== Modificar Stock de Producto ===");
        inventario.mostrarInventario();
        
        System.out.print("\nIngrese el código del producto a modificar: ");
        String codigo = scanner.nextLine();
        
        Producto producto = inventario.buscarProducto(codigo);
        if (producto != null) {
            System.out.println("Producto actual: " + producto.getNombre());
            System.out.println("Stock actual: " + producto.getCantidadEnStock());
            
            int nuevoStock = -1;
            do {
                try {
                    System.out.print("Ingrese el nuevo stock: ");
                    nuevoStock = Integer.parseInt(scanner.nextLine());
                    if (nuevoStock < 0) {
                        System.out.println("El stock no puede ser negativo");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un número válido");
                }
            } while (nuevoStock < 0);

            producto.setCantidadEnStock(nuevoStock);
            System.out.println("Stock actualizado exitosamente!");
        } else {
            System.out.println("No se encontró el producto con el código especificado.");
        }
    }
}
