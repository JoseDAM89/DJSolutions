package modelos;

import java.time.LocalDateTime;
import java.util.List;

public class Factura {
    private int idFactura;
    private int idCliente;
    private LocalDateTime fecha;
    private List<LineaFactura> lineas;
    private boolean pagada;

    // Constructor completo (usado al recuperar desde BD)
    public Factura(int idFactura, int idCliente, LocalDateTime fecha, List<LineaFactura> lineas, boolean pagada) {
        this.idFactura = idFactura;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.lineas = lineas;
        this.pagada = pagada;
    }

    // Constructor parcial (usado al crear nueva factura)
    public Factura(int idCliente, List<LineaFactura> lineas) {
        this(0, idCliente, LocalDateTime.now(), lineas, false); // ⬅ pagada por defecto: false
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

    public boolean isPagada() {
        return pagada;
    }

    public void setPagada(boolean pagada) {
        this.pagada = pagada;
    }
}
