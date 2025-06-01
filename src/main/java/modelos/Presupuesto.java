package modelos;

import java.time.LocalDate;
import java.util.List;

public class Presupuesto {
    private int id;
    private int idCliente;
    private LocalDate fecha;
    private double total;
    private List<LineaPresupuesto> lineas;
    private boolean aceptado;

    // ✅ Constructor completo con todos los campos (se usa al recuperar de la BD)
    public Presupuesto(int id, int idCliente, LocalDate fecha, List<LineaPresupuesto> lineas, double total, boolean aceptado) {
        this.id = id;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.lineas = lineas;
        this.total = total;
        this.aceptado = aceptado;
    }

    // ✅ Constructor con idCliente, fecha, líneas y total (para insertar nuevos)
    public Presupuesto(int idCliente, LocalDate fecha, List<LineaPresupuesto> lineas, double total) {
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.lineas = lineas;
        this.total = total;
        this.aceptado = false; // Por defecto NO aceptado
    }

    // ✅ Constructor alternativo para casos más simples (con fecha actual)
    public Presupuesto(int idCliente, List<LineaPresupuesto> lineas, double total) {
        this(0, idCliente, LocalDate.now(), lineas, total, false); // id = 0 por defecto
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public List<LineaPresupuesto> getLineas() { return lineas; }
    public void setLineas(List<LineaPresupuesto> lineas) { this.lineas = lineas; }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }
}
