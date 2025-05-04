package Modelos;

public class Producto {

    int CodProduct;
    String NombreProduct;
    double PrecioProduct;
    String DescripcionProduct;
    int StockProduct;
    boolean MateriaPrima = false;

    public Producto(int codProduct, String nombreProduct, double precioProduct, String descripcionProduct, int stockProduct, boolean materiaPrima) {
        CodProduct = codProduct;
        NombreProduct = nombreProduct;
        PrecioProduct = precioProduct;
        DescripcionProduct = descripcionProduct;
        StockProduct = stockProduct;
        MateriaPrima = materiaPrima;
    }


}
