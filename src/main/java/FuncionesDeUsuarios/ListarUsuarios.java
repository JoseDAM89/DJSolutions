package FuncionesDeUsuarios;

import modelos.Usuario;
import datos.UsuarioDAO;
import gui.EditarGenerico;
import gui.EliminarGenerico;
import gui.ListadosGenerico;

import javax.swing.*;
import java.util.List;

public class ListarUsuarios {

    public JPanel mostrarVentana() {
        List<Usuario> usuarios = UsuarioDAO.listarTodos();

        String[] columnas = {
                "ID", "Nombre", "Apellido", "Correo Electrónico", "Administrador"
        };

        Object[][] datos = new Object[usuarios.size()][columnas.length];
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            datos[i][0] = u.getId();
            datos[i][1] = u.getNombre();
            datos[i][2] = u.getApellido();
            datos[i][3] = u.getCampoEmail();
            datos[i][4] = u.isAdmin() ? "Sí" : "No";
        }

        return new ListadosGenerico(
                "Usuarios",
                columnas,
                datos,
                (fila, tabla) -> EditarGenerico.crearFormularioEdicion(
                        "usuarios", columnas, fila, "id", fila[0], "INTEGER",
                        tabla, tabla.getSelectedRow(),
                        filaActualizada -> ((ListadosGenerico) SwingUtilities.getAncestorOfClass(ListadosGenerico.class, tabla))
                                .actualizarFila(filaActualizada)
                ),
                fila -> EliminarGenerico.eliminarRegistro("usuarios", fila[0])
        );
    }
}
