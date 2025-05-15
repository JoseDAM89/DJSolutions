package Modelos;

public class Producto {

    private int codproduct;
    private String nombreproduct;
    private double precioproduct;
    private String descripcionproduct;
    private int stockproduct;
    private boolean materiaprima;
    private int idmateriaprima;

    // Constructor completo con ID
    public Producto(int codproduct, String nombreproduct, double precioproduct,
                    String descripcionproduct, int stockproduct,
                    boolean materiaprima, int idmateriaprima) {
        this.codproduct = codproduct;
        this.nombreproduct = nombreproduct;
        this.precioproduct = precioproduct;
        this.descripcionproduct = descripcionproduct;
        this.stockproduct = stockproduct;
        this.materiaprima = materiaprima;
        this.idmateriaprima = idmateriaprima;
    }

    // Getters
    public int getCodproduct() {
        return codproduct;
    }

    public String getNombreproduct() {
        return nombreproduct;
    }

    public double getPrecioproduct() {
        return precioproduct;
    }

    public String getDescripcionproduct() {
        return descripcionproduct;
    }

    public int getStockproduct() {
        return stockproduct;
    }

    public boolean isMateriaprima() {
        return materiaprima;
    }

    public int getIdmateriaprima() {
        return idmateriaprima;
    }

    // Setters
    public void setCodproduct(int codproduct) {
        this.codproduct = codproduct;
    }

    public void setNombreproduct(String nombreproduct) {
        this.nombreproduct = nombreproduct;
    }

    public void setPrecioproduct(double precioproduct) {
        this.precioproduct = precioproduct;
    }

    public void setDescripcionproduct(String descripcionproduct) {
        this.descripcionproduct = descripcionproduct;
    }

    public void setStockproduct(int stockproduct) {
        this.stockproduct = stockproduct;
    }

    public void setMateriaprima(boolean materiaprima) {
        this.materiaprima = materiaprima;
    }

    public void setIdmateriaprima(int idmateriaprima) {
        this.idmateriaprima = idmateriaprima;
    }
}
