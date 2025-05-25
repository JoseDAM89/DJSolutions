package FuncionesInventario;

import Controladores.ListarGenerico;
import Datos.ProductoDAO;
import GUI.EditarGenerico;
import GUI.EliminarGenerico;
import Modelos.Producto;

import java.util.List;

public class ListarProductos {

    public void mostrarVentana() {
        // 1. Obtener todos los productos desde el DAO
        List<Producto> productos = ProductoDAO.listarTodos();

        // 2. Definir los nombres de columnas visibles en pantalla
        String[] columnas = {
                "ID", "Nombre", "Precio", "Descripción", "Stock", "Materia Prima", "ID Materia"
        };

        // 3. Convertir la lista a una matriz para la JTable
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

        // 4. Mostrar usando el controlador genérico
        ListarGenerico.mostrarListado(
                "productos",
                datos,
                columnas,

                // Acción Editar
                e -> {
                    var tabla = (javax.swing.JTable) ((javax.swing.JButton) e.getSource())
                            .getParent().getParent().getComponent(1).getComponentAt(1, 1);
                    int fila = tabla.getSelectedRow();
                    if (fila < 0) return;

                    var modelo = (javax.swing.table.DefaultTableModel) tabla.getModel();
                    Object[] filaSeleccionada = new Object[modelo.getColumnCount()];
                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        filaSeleccionada[i] = modelo.getValueAt(fila, i);
                    }

                    EditarGenerico.mostrarFormularioDeEdicion(
                            "productos",
                            columnas,
                            filaSeleccionada,
                            "codproduct",
                            filaSeleccionada[0],
                            "INTEGER",
                            tabla,
                            fila
                    );
                },

                // Acción Eliminar
                e -> {
                    var tabla = (javax.swing.JTable) ((javax.swing.JButton) e.getSource())
                            .getParent().getParent().getComponent(1).getComponentAt(1, 1);
                    int fila = tabla.getSelectedRow();
                    if (fila < 0) return;

                    var modelo = (javax.swing.table.DefaultTableModel) tabla.getModel();
                    Object[] filaSeleccionada = new Object[modelo.getColumnCount()];
                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        filaSeleccionada[i] = modelo.getValueAt(fila, i);
                    }

                    EliminarGenerico.eliminarRegistro(
                            "productos",
                            "codproduct",
                            filaSeleccionada[0],
                            "INTEGER",
                            tabla,
                            fila
                    );
                }
        );
    }
}
