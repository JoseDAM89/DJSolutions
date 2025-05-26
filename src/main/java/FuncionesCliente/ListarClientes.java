package FuncionesCliente;

import Modelos.Cliente;
import datos.ClienteDAO;
import gui.EditarGenerico;
import gui.EliminarGenerico;
import gui.ListadosGenerico;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListarClientes {

    public JPanel mostrarVentana() {
        List<Cliente> clientes = ClienteDAO.listarTodos();

        String[] columnas = {
                "ID", "Nombre", "CIF", "Email", "Persona de Contacto", "Dirección", "Descripción"
        };

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

        return new ListadosGenerico("Clientes", columnas, datos,
                (fila, tabla) -> EditarGenerico.crearFormularioEdicion(
                        "clientes", columnas, fila, "idcliente", fila[0], "INTEGER",
                        tabla, tabla.getSelectedRow(),
                        filaActualizada -> ((ListadosGenerico) SwingUtilities.getAncestorOfClass(ListadosGenerico.class, tabla))
                                .actualizarFila(filaActualizada)
                ),
                fila -> EliminarGenerico.eliminarRegistro("clientes", fila[0])
        );


    }
}
