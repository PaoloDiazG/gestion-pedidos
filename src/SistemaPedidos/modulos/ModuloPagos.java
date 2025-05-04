package SistemaPedidos.modulos;

import java.util.Random;
import SistemaPedidos.modelo.Pedido;

public class ModuloPagos {

    // --- Para Control en Tests ---
    private Boolean forzarResultado = null; // null = comportamiento normal, true = forzar éxito, false = forzar fallo

    public void setForzarResultado(Boolean forzar) {
        this.forzarResultado = forzar;
    }

    public void resetearComportamiento() {
        this.forzarResultado = null;
    }
    // --- Fin Control en Tests ---


    public boolean procesarPago(Pedido pedido) {
        System.out.println("[Pagos] Iniciando procesamiento de pago para Pedido ID: " + pedido.getId()
                + " - Monto: S/" + String.format("%.2f", pedido.getTotal()));

        // --- Lógica de Resultado ---
        boolean exito;
        if (forzarResultado != null) {
            exito = forzarResultado;
            System.out.println("[Pagos] Resultado FORZADO para test: " + (exito ? "APROBADO" : "RECHAZADO"));
        } else {
            // Comportamiento original aleatorio
            exito = new Random().nextInt(100) < 95; // 75% de probabilidad de éxito
            System.out.println("[Pagos] Resultado aleatorio: " + (exito ? "APROBADO" : "RECHAZADO"));
        }

        if (exito) {
            System.out.println("[Pagos] Pago procesado para el Pedido con ID: " + pedido.getId() + " - RESULTADO: APROBADO");
            return true;
        } else {
            System.out.println("[Pagos] Pago procesado para el Pedido con ID: " + pedido.getId() + " - RESULTADO: RECHAZADO");
            return false;
        }
    }
}
