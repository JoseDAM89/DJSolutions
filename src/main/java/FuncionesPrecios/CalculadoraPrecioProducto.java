package FuncionesPrecios;

import modelos.PrecioProducto;

public class CalculadoraPrecioProducto {

    // Costes fijos exactos según la hoja Excel
    private static final double COSTE_OPERARIO_POR_HORA = 13.05; // €/h
    private static final double COSTE_OPERARIO_POR_MIN = COSTE_OPERARIO_POR_HORA / 60.0;
    private static final double COSTE_ENERGIA_POR_MIN = 0.03; // €/min
    private static final double PORCENTAJE_HERRAMIENTA = 0.024; // 2.4%

    public static double calcularCosteTotalSinEstructura(PrecioProducto p) {
        // Costes por pieza
        double costeOperario = p.getTiempoOperarioMin() * COSTE_OPERARIO_POR_MIN;
        double costeEnergia = p.getTiempoMaquinaMin() * COSTE_ENERGIA_POR_MIN;
        double costePreparacion = (p.getTiempoPreparacionMin() * COSTE_OPERARIO_POR_MIN) / p.getCantidad();

        double costeMaterial = p.getCosteBarra() / p.getPiezasPorBarra();
        double costeHerramienta = costeMaterial * PORCENTAJE_HERRAMIENTA;

        double costeZincado = p.isZincado() ? (p.getCosteZincadoManual() / p.getCantidad()) : 0.0;

        return costeOperario + costeEnergia + costePreparacion + costeMaterial + costeHerramienta + costeZincado;
    }

    public static double calcularCosteConEstructura(PrecioProducto p) {
        double base = calcularCosteTotalSinEstructura(p);
        return base * (1 + (p.getEstructura() / 100.0));
    }

    public static double calcularPrecioPorUnidad(PrecioProducto p) {
        double base = calcularCosteConEstructura(p);
        return base * (1 + (p.getMargen() / 100.0));
    }
}
