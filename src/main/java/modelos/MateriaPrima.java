package modelos;

public class MateriaPrima {

    int idMaterial;
    String descripcionMaterial;
    double stockMaterial;

    public MateriaPrima(int idMaterial, String descripcionMaterial, double stockMaterial) {
        this.idMaterial = idMaterial;
        this.descripcionMaterial = descripcionMaterial;
        this.stockMaterial = stockMaterial;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public String getDescripcionMaterial() {
        return descripcionMaterial;
    }

    public double getStockMaterial() {
        return stockMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public void setDescripcionMaterial(String descripcionMaterial) {
        this.descripcionMaterial = descripcionMaterial;
    }

    public void setStockMaterial(double stockMaterial) {
        this.stockMaterial = stockMaterial;
    }
}
