package modelos;

import java.time.LocalDateTime;
import java.util.List;

public class Factura {
    private int idFactura;
    private int idCliente;
    private LocalDateTime fecha;
    private List<LineaFactura> lineas;

    public Factura(int idFactura, int idCliente, LocalDateTime fecha, List<LineaFactura> lineas) {
        this.idFactura = idFactura;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.lineas = lineas;
    }

    public Factura(int idCliente, List<LineaFactura> lineas) {
        this(0, idCliente, LocalDateTime.now(), lineas);
    }

    // Getters y setters
    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<LineaFactura> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaFactura> lineas) {
        this.lineas = lineas;
    }
}
