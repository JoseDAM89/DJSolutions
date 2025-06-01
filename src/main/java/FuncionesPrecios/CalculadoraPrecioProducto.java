package FuncionesPrecios;

import modelos.PrecioProducto;

public class CalculadoraPrecioProducto {

    // Constantes según el Excel
    private static final double COSTE_OPERARIO_HORA = 13.05;   // K7
    private static final double COSTE_ENERGIA_MIN = 0.03;      // K13
    private static final double COSTE_TRANSPORTE = 27.48;      // C8

    public static double calcularPrecioVentaUnidad(PrecioProducto p) {

        // C3: Coste operario por unidad = (coste hora * tiempo operario) / 60
        double costeOperario = (p.getTiempoOperarioMin() * COSTE_OPERARIO_HORA) / 60.0;

        // C4: Coste material por unidad = coste barra / piezas por barra
        double costeMaterial = p.getCosteBarra() / p.getPiezasPorBarra();

        // C5: Coste energía por unidad = tiempo máquina * coste energía por minuto
        double costeEnergia = p.getTiempoMaquinaMin() * COSTE_ENERGIA_MIN;

        // C6: Zincado por unidad (si aplica)
        double costeZincado = p.isZincado() ? p.getCosteZincadoManual() / p.getCantidad() : 0.0;

        // C7: Coste estructura = (C3+C4+C5+C6) * % estructura
        double baseSinEstructura = costeOperario + costeMaterial + costeEnergia + costeZincado;
        double costeEstructura = baseSinEstructura * p.getEstructura();

        // C9: Preparación por unidad = (tiempo preparación * coste hora) / 60
        double costePreparacion = (p.getTiempoPreparacionMin() * COSTE_OPERARIO_HORA) / 60.0;

        // C8: Transporte por unidad
        double costeTransporte = COSTE_TRANSPORTE / p.getCantidad();

        // C12: Coste total por unidad con todo incluido
        double costeTotalUnidad = baseSinEstructura + costeEstructura + costePreparacion + costeTransporte;

        // H4: Precio de venta por unidad con margen
        return costeTotalUnidad / (1 - p.getMargen()); // ✅ Fórmula correcta
    }
}
