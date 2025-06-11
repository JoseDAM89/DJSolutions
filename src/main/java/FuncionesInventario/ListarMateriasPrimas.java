package FuncionesInventario;

import datos.MateriaPrimaDAO;
import gui.EditarGenerico;
import gui.EliminarGenerico;
import gui.ListadosGenerico;
import modelos.MateriaPrima;

import javax.swing.*;
import java.util.List;

public class ListarMateriasPrimas {

    public JPanel mostrarVentana() {
        List<MateriaPrima> materias = MateriaPrimaDAO.obtenerTodasComoLista();

        String[] columnas = {"ID", "Descripción", "Stock"};

        Object[][] datos = new Object[materias.size()][columnas.length];
        for (int i = 0; i < materias.size(); i++) {
            MateriaPrima m = materias.get(i);
            datos[i][0] = m.getIdMaterial();
            datos[i][1] = m.getDescripcionMaterial();
            datos[i][2] = m.getStockMaterial();
        }

        return new ListadosGenerico("Materias Primas", columnas, datos,
                // Formulario de edición genérico
                (fila, tabla) -> EditarGenerico.crearFormularioEdicion(
                        "materiasprimas", columnas, fila, "idmateriaprima", fila[0], "INTEGER",
                        tabla, tabla.getSelectedRow(),
                        filaActualizada -> ((ListadosGenerico) SwingUtilities.getAncestorOfClass(ListadosGenerico.class, tabla))
                                .actualizarFila(filaActualizada)
                ),
                // Eliminación genérica sin validación extra
                fila -> {
                    int id = Integer.parseInt(fila[0].toString());
                    EliminarGenerico.eliminarRegistro("MateriasPrimas", id);
                }
        );
    }
}
