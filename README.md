# Sistema de Gestión de Pedidos

## Descripción General
El Sistema de Gestión de Pedidos es una aplicación Java que permite administrar inventario, procesar pedidos y generar comprobantes para una tienda de productos tecnológicos. La aplicación cuenta con una interfaz gráfica de usuario (GUI) que facilita la interacción tanto para clientes como para administradores.

## Arquitectura del Sistema
El sistema está organizado en cuatro paquetes principales:

1. **modelo**: Contiene las clases que representan las entidades del negocio.
2. **modulos**: Contiene la lógica de negocio del sistema.
3. **gui**: Contiene las interfaces gráficas de usuario.
4. **comprobantes**: Contiene las clases relacionadas con la generación y gestión de comprobantes.

## Componentes Principales

### Modelos (modelo)
- **Producto**: Representa un producto en el inventario con atributos como ID, nombre, precio, cantidad en stock, categoría y tamaño.
- **Pedido**: Representa un pedido realizado por un cliente, contiene una lista de ítems y tiene un estado (pendiente, procesando, pagado, cancelado).
- **ItemPedido**: Representa un ítem dentro de un pedido, asociando un producto con una cantidad solicitada.
- **SistemaGestionPedidos**: Clase principal que inicializa el sistema en modo consola (no GUI).

### Módulos (modulos)
- **ModuloInventario**: Gestiona el inventario de productos, permitiendo agregar, consultar y modificar productos.
- **ModuloGestionPedidos**: Gestiona los pedidos, permitiendo crear, procesar y consultar pedidos.
- **ModuloPagos**: Gestiona el procesamiento de pagos para los pedidos.

### Interfaces Gráficas (gui)
- **SistemaGestionPedidosGUI**: Interfaz principal que muestra las opciones de acceso para clientes y administradores.
- **ClienteGUI**: Interfaz para clientes que permite crear pedidos, consultar pedidos y ver el inventario.
- **AdminGUI**: Interfaz para administradores que permite gestionar el inventario, editar productos y ver reportes.

### Comprobantes (comprobantes)
- **ComprobanteEntrada**: Representa un comprobante de entrada de productos al inventario.
- **ComprobanteSalida**: Representa un comprobante de salida de productos del inventario (asociado a un pedido).
- **GestorComprobantes**: Gestiona la creación y consulta de comprobantes.

## Cómo Ejecutar la Aplicación
1. Asegúrese de tener Java instalado en su sistema.
2. Compile el proyecto usando su IDE favorito o mediante línea de comandos.
3. Ejecute la clase `SistemaGestionPedidosGUI` para iniciar la aplicación con interfaz gráfica.
4. Alternativamente, ejecute la clase `SistemaGestionPedidos` para iniciar la aplicación en modo consola.

## Características y Funcionalidades

### Funcionalidades para Clientes
1. **Crear Pedidos**: Los clientes pueden seleccionar productos del inventario y crear pedidos.
2. **Consultar Pedidos**: Los clientes pueden consultar el estado de sus pedidos.
3. **Ver Inventario**: Los clientes pueden ver los productos disponibles en el inventario.

### Funcionalidades para Administradores
1. **Gestión de Productos**:
   - Agregar nuevos productos al inventario
   - Editar productos existentes
   - Ver y filtrar el inventario

2. **Gestión de Comprobantes**:
   - Generar comprobantes de entrada al agregar productos
   - Ver comprobantes de entrada y salida
   - Sincronizar pedidos con comprobantes de salida

3. **Reportes**:
   - Reporte de stock general con filtros por categoría, tamaño, precio y stock
   - Reporte de comprobantes de entrada
   - Reporte de comprobantes de salida

## Descripción Detallada de Clases

### Producto
La clase `Producto` representa un producto en el inventario. Tiene los siguientes atributos:
- `id`: Identificador único del producto
- `nombre`: Nombre del producto
- `precio`: Precio del producto
- `cantidadEnStock`: Cantidad disponible en inventario
- `categoria`: Categoría del producto (CPUS, LAPTOPS, MOUSE, etc.)
- `tamano`: Tamaño del producto (PEQUENO, MEDIANO, GRANDE)

### Pedido
La clase `Pedido` representa un pedido realizado por un cliente. Tiene los siguientes atributos:
- `id`: Identificador único del pedido
- `items`: Lista de ítems del pedido
- `total`: Monto total del pedido
- `estado`: Estado del pedido (PENDIENTE, PROCESANDO, PAGADO, CANCELADOxSTOCK, CANCELADOxPAGO)

### ModuloInventario
Este módulo gestiona el inventario de productos. Sus principales funcionalidades son:
- Agregar productos al inventario
- Consultar productos por ID
- Verificar disponibilidad de stock para pedidos
- Reducir stock al procesar pedidos

### ModuloGestionPedidos
Este módulo gestiona los pedidos. Sus principales funcionalidades son:
- Crear y procesar pedidos
- Consultar pedidos por ID
- Mostrar todos los pedidos registrados

### GestorComprobantes
Esta clase gestiona los comprobantes de entrada y salida. Sus principales funcionalidades son:
- Crear comprobantes de entrada al agregar productos
- Crear comprobantes de salida al procesar pedidos
- Consultar comprobantes por ID
- Sincronizar pedidos con comprobantes de salida

## Flujos de Trabajo

### Flujo de Creación de Pedido (Cliente)
1. El cliente accede a la interfaz de cliente desde la pantalla principal.
2. Selecciona la opción "Crear Pedido".
3. Se muestra una cuadrícula con los productos disponibles.
4. El cliente hace clic en un producto para agregarlo al pedido.
5. Ingresa la cantidad deseada en el diálogo que aparece.
6. Repite los pasos 4-5 para agregar más productos.
7. Finaliza el pedido haciendo clic en "Procesar Pedido".
8. El sistema verifica el stock y procesa el pago.
9. Se muestra un resumen del pedido con su estado final.

### Flujo de Agregar Producto (Administrador)
1. El administrador accede a la interfaz de administrador desde la pantalla principal.
2. Selecciona la pestaña "Agregar Nuevo Producto".
3. Completa el formulario con los datos del producto (nombre, precio, stock, categoría, tamaño).
4. Hace clic en "Agregar Producto".
5. El sistema pregunta si desea agregar otro producto.
6. Si selecciona "Sí", se limpia el formulario para agregar otro producto.
7. Si selecciona "No", se genera un comprobante de entrada con los productos agregados.

### Flujo de Generación de Reportes (Administrador)
1. El administrador accede a la interfaz de administrador desde la pantalla principal.
2. Selecciona la pestaña "Ver Reportes".
3. Selecciona el tipo de reporte que desea ver (stock, comprobantes de entrada, comprobantes de salida).
4. Aplica filtros según sea necesario.
5. Visualiza el reporte generado.

## Seguridad
El acceso a la interfaz de administrador está protegido por una contraseña. La contraseña predeterminada es "password".

## Notas Adicionales
- El sistema utiliza Java Swing para la interfaz gráfica.
- Los datos se mantienen en memoria durante la ejecución del programa (no hay persistencia en base de datos).
- El sistema incluye datos de ejemplo que se cargan al iniciar la aplicación.