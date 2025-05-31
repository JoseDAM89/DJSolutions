package Controladores;

import modelos.Cliente;
import modelos.Presupuesto;
import datos.PresupuestoDAO;
import gui.GenerarPresupuestoPanel;
import FuncionesPresupuesto.PresupuestoServicio;

import java.util.ArrayList;
import java.util.List;

public class GenerarPresupuestoControlador {

    private List<Presupuesto> productos = new ArrayList<>();
    private GenerarPresupuestoPanel panel;  // Referencia al panel, si necesitas notificarle

    // Constructor que recibe el panel
    public GenerarPresupuestoControlador(GenerarPresupuestoPanel panel) {
        this.panel = panel;
    }

    // Constructor por defecto, por si se quiere usar sin panel
    public GenerarPresupuestoControlador() {
    }

    public void setPanel(GenerarPresupuestoPanel panel) {
        this.panel = panel;
    }

    public void agregarProducto(Presupuesto p) {
        productos.add(p);
        // Aquí puedes actualizar el panel si fuera necesario
        // if (panel != null) panel.actualizarTabla(productos);
    }

    public void guardarPresupuestoEnBD() {
        for (Presupuesto p : productos) {
            PresupuestoDAO.insertar(p);
        }
    }

    public void generarPDF(List<Presupuesto> productos, Cliente cliente) {

        PresupuestoServicio service = new PresupuestoServicio();
        try {
            guardarPresupuestoEnBD();
            service.generarPDF(productos, cliente);
        } catch (Exception e) {
            e.printStackTrace();
            // if (panel != null) panel.mostrarError("Error al generar el PDF");
        }
    }

    public List<Presupuesto> getProductos() {
        return productos;
    }
}
