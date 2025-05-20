package SistemaPedidos.modelo;

public class Producto {
    // Enumeración para las categorías de productos
    public enum Categoria {
        CPUS("CPUs"),
        LAPTOPS("Laptops"),
        MOUSE("Mouse"),
        TECLADO("Teclado"),
        MONITOR("Monitor"),
        GPU("GPU"),
        SOFTWARE("Software"),
        PERIFERICOS("Perifericos");

        private final String nombre;

        Categoria(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    // Enumeración para los tamaños de productos
    public enum Tamano {
        PEQUENO("Pequeño"),
        MEDIANO("Mediano"),
        GRANDE("Grande");

        private final String nombre;

        Tamano(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    private String id;
    private String nombre;
    private double precio;
    private int cantidadEnStock;
    private Categoria categoria;
    private Tamano tamano;

    public Producto(String id, String nombre, double precio, int cantidadEnStock, Categoria categoria, Tamano tamano) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidadEnStock = cantidadEnStock;
        this.categoria = categoria;
        this.tamano = tamano;
    }

    public Producto(String id, String nombre, double precio, int cantidadEnStock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidadEnStock = cantidadEnStock;
        this.categoria = Categoria.PERIFERICOS; // Default category
        this.tamano = Tamano.MEDIANO; // Default size
    }

    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public double getPrecio() {
        return precio;
    }
    public int getCantidadEnStock() {
        return cantidadEnStock;
    }
    public void setCantidadEnStock(int cantidadEnStock) {
        this.cantidadEnStock = cantidadEnStock;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    public Tamano getTamano() {
        return tamano;
    }
    public void setTamano(Tamano tamano) {
        this.tamano = tamano;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nombre: " + nombre + ", Precio: S/ " + String.format("%.2f", precio) + 
               ", Stock: " + cantidadEnStock + ", Categoría: " + categoria + ", Tamaño: " + tamano;
    }
}
