package FuncionesFacturas;

import datos.ClienteDAO;
import datos.FacturaDAO;
import modelos.Cliente;
import modelos.Factura;

import java.util.List;

public class HistorialDeFacturas {

    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public List<Factura> obtenerTodas() {
        return facturaDAO.listarTodas();
    }

    public Factura obtenerPorId(int id) {
        return facturaDAO.obtenerFacturaPorID(id);
    }

    public Cliente obtenerCliente(int idCliente) {
        return clienteDAO.obtenerPorID(idCliente);
    }

    public void actualizarEstadoPagada(int idFactura, boolean pagada) {
        facturaDAO.actualizarEstadoPagada(idFactura, pagada);
    }
}
