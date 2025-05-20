# Tests para el Sistema de Gestión de Pedidos

Este directorio contiene pruebas unitarias para las funciones más vitales del sistema de gestión de pedidos.

## Estructura de los Tests

Los tests están organizados siguiendo la misma estructura de paquetes que el código fuente:

- `SistemaPedidos.modelo`: Tests para las clases del modelo de datos
  - `ProductoTest.java`: Prueba la creación y manipulación de productos
  - `PedidoTest.java`: Prueba la creación y gestión de pedidos
  - `ItemPedidoTest.java`: Prueba la creación y cálculos de ítems de pedido

- `SistemaPedidos.modulos`: Tests para los módulos funcionales
  - `ModuloInventarioTest.java`: Prueba la gestión de inventario
  - `ModuloPagosTest.java`: Prueba el procesamiento de pagos
  - `ModuloGestionPedidosTest.java`: Prueba la gestión de pedidos

## Cómo ejecutar los tests

Para ejecutar estos tests, necesitas tener JUnit 5 en tu classpath. Puedes ejecutar los tests de varias maneras:

### Desde un IDE (como IntelliJ IDEA o Eclipse)

1. Asegúrate de que el directorio `test` esté configurado como directorio de tests en tu IDE
2. Agrega JUnit 5 a las dependencias del proyecto
3. Haz clic derecho en el directorio `test` y selecciona "Run All Tests"

### Desde la línea de comandos

Si tienes Maven configurado:

```bash
mvn test
```

Si usas Gradle:

```bash
gradle test
```

## Funciones probadas

### Modelo de datos
- Creación y manipulación de productos
- Creación y gestión de pedidos
- Cálculo de subtotales y totales
- Actualización de estados de pedidos

### Módulos funcionales
- Agregar y buscar productos en inventario
- Verificar disponibilidad de stock
- Reducir stock al procesar pedidos
- Procesar pagos (con posibilidad de forzar resultados para testing)
- Consultar pedidos por ID