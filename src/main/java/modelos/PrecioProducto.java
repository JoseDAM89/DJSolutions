package modelos;

public class PrecioProducto {
    private int cantidad;
    private double tiempoOperarioMin;
    private double tiempoMaquinaMin;
    private double tiempoPreparacionMin;
    private double costeBarra;
    private int piezasPorBarra;
    private boolean zincado;
    private double costeZincadoManual; // This will be the total zincado cost (e.g., 30)
    private double estructura; // Already a decimal (e.g., 0.10 for 10%)
    private double margen;     // Already a decimal (e.g., 0.30 for 30%)

    public PrecioProducto(int cantidad, double tiempoOperarioMin, double tiempoMaquinaMin,
                          double tiempoPreparacionMin, double costeBarra, int piezasPorBarra,
                          boolean zincado, double costeZincadoManual, double estructura, double margen) {
        this.cantidad = cantidad;
        this.tiempoOperarioMin = tiempoOperarioMin;
        this.tiempoMaquinaMin = tiempoMaquinaMin;
        this.tiempoPreparacionMin = tiempoPreparacionMin;
        this.costeBarra = costeBarra;
        this.piezasPorBarra = piezasPorBarra;
        this.zincado = zincado;
        this.costeZincadoManual = costeZincadoManual;
        this.estructura = estructura;
        this.margen = margen;
    }

    // Getters for all fields
    public int getCantidad() {
        return cantidad;
    }

    public double getTiempoOperarioMin() {
        return tiempoOperarioMin;
    }

    public double getTiempoMaquinaMin() {
        return tiempoMaquinaMin;
    }

    public double getTiempoPreparacionMin() {
        return tiempoPreparacionMin;
    }

    public double getCosteBarra() {
        return costeBarra;
    }

    public int getPiezasPorBarra() {
        return piezasPorBarra;
    }

    public boolean isZincado() {
        return zincado;
    }

    public double getCosteZincadoManual() {
        return costeZincadoManual;
    }

    public double getEstructura() {
        return estructura;
    }

    public double getMargen() {
        return margen;
    }
}