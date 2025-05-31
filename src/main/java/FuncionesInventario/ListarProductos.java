package FuncionesInventario;

import modelos.Producto;
import datos.ProductoDAO;
import gui.EditarGenerico;
import gui.EliminarGenerico;
import gui.ListadosGenerico;

import javax.swing.*;
import java.util.List;

public class ListarProductos {

    public JPanel mostrarVentana() {
        List<Producto> productos = ProductoDAO.listarTodos();

        String[] columnas = {
                "ID", "Nombre", "Precio", "Descripción", "Stock", "Materia Prima", "ID Materia"
        };

        Object[][] datos = new Object[productos.size()][columnas.length];
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            datos[i][0] = p.getCodproduct();
            datos[i][1] = p.getNombreproduct();
            datos[i][2] = p.getPrecioproduct();
            datos[i][3] = p.getDescripcionproduct();
            datos[i][4] = p.getStockproduct();
            datos[i][5] = p.isMateriaprima() ? "Sí" : "No";
            datos[i][6] = p.getIdmateriaprima();
        }

        return new ListadosGenerico("Productos", columnas, datos,
                (fila, tabla) -> EditarGenerico.crearFormularioEdicion(
                        "productos", columnas, fila, "codproduct", fila[0], "INTEGER",
                        tabla, tabla.getSelectedRow(),
                        filaActualizada -> ((ListadosGenerico) SwingUtilities.getAncestorOfClass(ListadosGenerico.class, tabla))
                                .actualizarFila(filaActualizada)
                ),
                fila -> EliminarGenerico.eliminarRegistro("productos", fila[0])
        );

    }
}
