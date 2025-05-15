package Modelos;

public class Presupuesto {
    public int id;
    public String nombre;
    public int cantidad;
    public double precio;

    public Presupuesto(int id, String nombre, int cantidad, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }
}
