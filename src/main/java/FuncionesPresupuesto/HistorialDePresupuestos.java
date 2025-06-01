package FuncionesPresupuesto;

import datos.ClienteDAO;
import datos.PresupuestoDAO;
import modelos.Cliente;
import modelos.Presupuesto;

import java.util.List;

public class HistorialDePresupuestos {

    private final PresupuestoDAO presupuestoDAO = new PresupuestoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public List<Presupuesto> obtenerTodos() {
        return presupuestoDAO.listarTodos();
    }

    public Presupuesto obtenerPorId(int id) {
        return presupuestoDAO.obtenerPorID(id);
    }

    public Cliente obtenerCliente(int idCliente) {
        return clienteDAO.obtenerPorID(idCliente);
    }
}
