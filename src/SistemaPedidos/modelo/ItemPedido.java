package SistemaPedidos.modelo;

import SistemaPedidos.modelo.Producto;

public class ItemPedido {
    private Producto producto;
    private int cantidadSolicitada;

    public ItemPedido(Producto producto, int cantidadSolicitada) {
        this.producto = producto;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Producto getProducto() { return producto; }
    public int getCantidadSolicitada() { return cantidadSolicitada; }

    public void setCantidadSolicitada(int cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public double calcularSubtotal() {
        return producto.getPrecio() * cantidadSolicitada;
    }

    @Override
    public String toString() {
        return producto.getNombre() + " (x" + cantidadSolicitada + ") - Subtotal: S/" + String.format("%.2f", calcularSubtotal());
    }
}
