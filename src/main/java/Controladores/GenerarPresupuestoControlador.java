package Controladores;

import modelos.Cliente;
import modelos.LineaPresupuesto;
import modelos.Presupuesto;
import datos.PresupuestoDAO;
import gui.GenerarPresupuestoPanel;
import FuncionesPresupuesto.PresupuestoServicio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GenerarPresupuestoControlador {

    private List<LineaPresupuesto> listaProductos = new ArrayList<>();
    private GenerarPresupuestoPanel panel;
    private PresupuestoDAO dao = new PresupuestoDAO();

    public GenerarPresupuestoControlador(GenerarPresupuestoPanel panel) {
        this.panel = panel;
    }

    public GenerarPresupuestoControlador() {
    }

    public void setPanel(GenerarPresupuestoPanel panel) {
        this.panel = panel;
    }

    // ✅ Agregar una línea al presupuesto
    public void agregarProducto(LineaPresupuesto linea) {
        listaProductos.add(linea);
        actualizarTabla();
    }

    // ✅ Obtener productos añadidos (para el panel)
    public List<LineaPresupuesto> getProductos() {
        return listaProductos;
    }

    // ✅ Eliminar una línea por índice
    public void eliminarProducto(int index) {
        if (index >= 0 && index < listaProductos.size()) {
            listaProductos.remove(index);
            actualizarTabla();
        }
    }

    // ✅ Mostrar productos por consola (puede enlazarse con tabla)
    public void actualizarTabla() {
        System.out.println("Productos actuales en el presupuesto:");
        for (LineaPresupuesto lp : listaProductos) {
            System.out.println("- " + lp.getNombreProducto() + ", cantidad: " + lp.getCantidad());
        }
    }

    // ✅ Guardar en base de datos
    public Presupuesto guardarPresupuestoEnBD(Cliente cliente) {
        double total = listaProductos.stream()
                .mapToDouble(lp -> lp.getCantidad() * lp.getPrecioUnitario())
                .sum();

        Presupuesto presupuesto = new Presupuesto(
                cliente.getIdcliente(),
                LocalDate.now(),
                new ArrayList<>(listaProductos),
                total
        );

        dao.insertar(presupuesto);  // Este método debe rellenar el ID
        return presupuesto;
    }

    // ✅ Generar PDF con datos
    public void generarPDF(Cliente cliente) {
        try {
            guardarPresupuestoEnBD(cliente);
            List<Presupuesto> lista = new ArrayList<>();
            double total = listaProductos.stream()
                    .mapToDouble(lp -> lp.getCantidad() * lp.getPrecioUnitario())
                    .sum();

            Presupuesto p = new Presupuesto(
                    cliente.getIdcliente(),
                    LocalDate.now(),
                    new ArrayList<>(listaProductos),
                    total
            );

            lista.add(p);

            PresupuestoServicio service = new PresupuestoServicio();
            service.generarPDF(lista, cliente);
        } catch (Exception e) {
            e.printStackTrace();
            // Aquí puedes mostrar un mensaje en el panel si quieres
        }
    }
}
