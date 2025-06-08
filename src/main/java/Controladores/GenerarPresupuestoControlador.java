package Controladores;

import modelos.Cliente;
import modelos.LineaPresupuesto;
import modelos.Presupuesto;
import datos.PresupuestoDAO;
import FuncionesPresupuesto.PresupuestoServicio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GenerarPresupuestoControlador {

    private List<LineaPresupuesto> listaProductos = new ArrayList<>();
    private final PresupuestoDAO dao = new PresupuestoDAO();

    // ✅ Agregar una línea al presupuesto
    public void agregarProducto(LineaPresupuesto linea) {
        listaProductos.add(linea);
    }

    // ✅ Obtener productos añadidos
    public List<LineaPresupuesto> getProductos() {
        return listaProductos;
    }

    // ✅ Eliminar una línea por índice
    public void eliminarProducto(int index) {
        if (index >= 0 && index < listaProductos.size()) {
            listaProductos.remove(index);
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

        dao.insertar(presupuesto);
        return presupuesto;
    }

    // ✅ Generar PDF con datos
    public void generarPDF(Cliente cliente) {
        try {
            guardarPresupuestoEnBD(cliente);
            List<Presupuesto> lista = new ArrayList<>();

            Presupuesto p = new Presupuesto(
                    cliente.getIdcliente(),
                    LocalDate.now(),
                    new ArrayList<>(listaProductos),
                    listaProductos.stream().mapToDouble(lp -> lp.getCantidad() * lp.getPrecioUnitario()).sum()
            );

            lista.add(p);

            PresupuestoServicio service = new PresupuestoServicio();
            service.generarPDF(lista, cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
