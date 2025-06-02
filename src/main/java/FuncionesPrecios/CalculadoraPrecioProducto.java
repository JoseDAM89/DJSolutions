package FuncionesPrecios;

import modelos.PrecioProducto;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculadoraPrecioProducto {

    // Constantes (valores desde Excel)
    private static final BigDecimal COSTE_OPERARIO_HORA = new BigDecimal("13.05");      // K7
    private static final BigDecimal COSTE_ENERGIA_MIN = new BigDecimal("0.03");         // K13
    private static final BigDecimal COSTE_TRANSPORTE_TOTAL = new BigDecimal("27.48");   // C8
    private static final BigDecimal FACTOR_COSTE_HERRAMIENTA = new BigDecimal("0.024"); // K6 (2.40%)
    private static final BigDecimal COSTE_FINANCIACION_POR_PIEZA = BigDecimal.ZERO;     // C6 (Asumimos 0.0)

    public static double calcularPrecioVentaUnidad(PrecioProducto p) {
        if (p == null || p.getCantidad() <= 0 || p.getPiezasPorBarra() <= 0) {
            throw new IllegalArgumentException("Datos inválidos: cantidad o piezas por barra no pueden ser <= 0.");
        }

        BigDecimal cantidad = BigDecimal.valueOf(p.getCantidad());           // E4
        BigDecimal piezasPorBarra = BigDecimal.valueOf(p.getPiezasPorBarra());
        BigDecimal tiempoOperario = BigDecimal.valueOf(p.getTiempoOperarioMin());    // C3
        BigDecimal tiempoMaquina = BigDecimal.valueOf(p.getTiempoMaquinaMin());      // C4
        BigDecimal tiempoPreparacion = BigDecimal.valueOf(p.getTiempoPreparacionMin()); // C9
        BigDecimal costeBarra = BigDecimal.valueOf(p.getCosteBarra());         // C5
        BigDecimal costeZincado = p.isZincado() ? BigDecimal.valueOf(p.getCosteZincadoManual()) : BigDecimal.ZERO; // C10
        BigDecimal estructura = BigDecimal.valueOf(p.getEstructura());         // K1 (estructura)
        BigDecimal margen = BigDecimal.valueOf(p.getMargen());                 // K2 (margen)

        if (margen.compareTo(BigDecimal.ONE) >= 0) {
            throw new IllegalArgumentException("El margen debe ser menor que 1 para evitar división por cero.");
        }

        // Costes variables unitarios (CVU)
        BigDecimal c3 = tiempoOperario.multiply(COSTE_OPERARIO_HORA).divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP);
        BigDecimal c4 = costeBarra.divide(piezasPorBarra, 10, RoundingMode.HALF_UP);
        BigDecimal c5 = tiempoMaquina.multiply(COSTE_ENERGIA_MIN).setScale(10, RoundingMode.HALF_UP);
        BigDecimal c6 = COSTE_FINANCIACION_POR_PIEZA;  // 0 por ahora
        BigDecimal c7 = c3.add(c4).add(c5).add(c6).multiply(FACTOR_COSTE_HERRAMIENTA).setScale(10, RoundingMode.HALF_UP);

        BigDecimal costeVariableUnidad = c3.add(c4).add(c5).add(c6).add(c7);

        // Costes fijos totales (CFT)
        BigDecimal c8 = COSTE_TRANSPORTE_TOTAL;
        BigDecimal c9 = tiempoPreparacion.multiply(COSTE_OPERARIO_HORA).divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP);
        BigDecimal c10 = costeZincado;

        BigDecimal costesFijosTotales = c8.add(c9).add(c10);

        // Coste total por unidad
        BigDecimal coste = costeVariableUnidad.add(costesFijosTotales.divide(cantidad, 10, RoundingMode.HALF_UP));

        // Aplicar estructura y margen
        BigDecimal costeMasEstructura = coste.divide(BigDecimal.ONE.subtract(estructura), 10, RoundingMode.HALF_UP);
        BigDecimal precioFinal = costeMasEstructura.divide(BigDecimal.ONE.subtract(margen), 2, RoundingMode.HALF_UP);

        return precioFinal.doubleValue();
    }

}
