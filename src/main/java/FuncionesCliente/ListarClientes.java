package FuncionesCliente;

import Controladores.ListarGenerico;
import Datos.ClienteDAO;
import GUI.EditarGenerico;
import GUI.EliminarGenerico;
import Modelos.Cliente;

import java.util.List;

public class ListarClientes {

    public void mostrarVentana() {
        // 1. Obtener todos los clientes desde la base de datos
        List<Cliente> clientes = ClienteDAO.listarTodos();

        // 2. Definir columnas visibles
        String[] columnas = {
                "ID", "Nombre", "CIF", "Email", "Persona de Contacto", "Dirección", "Descripción"
        };

        // 3. Convertir la lista a una matriz de datos
        Object[][] datos = new Object[clientes.size()][columnas.length];
        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            datos[i][0] = c.getIdcliente();
            datos[i][1] = c.getCampoNombre();
            datos[i][2] = c.getCampoCIF();
            datos[i][3] = c.getCampoEmail();
            datos[i][4] = c.getCampoPersonaDeContacto();
            datos[i][5] = c.getCampoDireccion();
            datos[i][6] = c.getCampoDescripcion();
        }

        // 4. Usar el controlador genérico para mostrar el listado
        ListarGenerico.mostrarListado(
                "clientes",
                datos,
                columnas,
                // Acción Editar
                e -> {
                    var tabla = (javax.swing.JTable) ((javax.swing.JButton) e.getSource()).getParent().getParent().getComponent(1).getComponentAt(1, 1);
                    int fila = tabla.getSelectedRow();
                    if (fila < 0) return;

                    var modelo = (javax.swing.table.DefaultTableModel) tabla.getModel();
                    Object[] filaSeleccionada = new Object[modelo.getColumnCount()];
                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        filaSeleccionada[i] = modelo.getValueAt(fila, i);
                    }

                    EditarGenerico.mostrarFormularioDeEdicion(
                            "clientes",
                            columnas,
                            filaSeleccionada,
                            "idcliente",
                            filaSeleccionada[0],
                            "INTEGER",
                            tabla,
                            fila
                    );
                },
                // Acción Eliminar
                e -> {
                    var tabla = (javax.swing.JTable) ((javax.swing.JButton) e.getSource()).getParent().getParent().getComponent(1).getComponentAt(1, 1);
                    int fila = tabla.getSelectedRow();
                    if (fila < 0) return;

                    var modelo = (javax.swing.table.DefaultTableModel) tabla.getModel();
                    Object[] filaSeleccionada = new Object[modelo.getColumnCount()];
                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        filaSeleccionada[i] = modelo.getValueAt(fila, i);
                    }

                    EliminarGenerico.eliminarRegistro(
                            "clientes",
                            "idcliente",
                            filaSeleccionada[0],
                            "INTEGER",
                            tabla,
                            fila
                    );
                }
        );
    }
}
