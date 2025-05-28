package FuncionesDeUsuarios;

import Modelos.Usuario;
import datos.UsuarioDAO;
import gui.FormularioGenericoAlta;
import Controladores.ValidadorGenerico;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AltaUser {

    public JPanel construirFormulario() {
        Map<String, String> camposConTipos = new LinkedHashMap<>();
        camposConTipos.put("Nombre", "String");
        camposConTipos.put("Apellido", "String");
        camposConTipos.put("Correo electrónico", "String");
        camposConTipos.put("Contraseña", "String");
        camposConTipos.put("Administrador", "boolean");

        final FormularioGenericoAlta[] formulario = new FormularioGenericoAlta[1];

        ActionListener accionGuardar = e -> {
            HashMap<String, String> valores = formulario[0].getValores();

            if (!ValidadorGenerico.validar(valores, camposConTipos)) return;

            String nombre = valores.get("Nombre").trim();
            String apellido = valores.get("Apellido").trim();
            String correo = valores.get("Correo electrónico").trim();
            String contrasena = valores.get("Contraseña").trim();
            boolean admin = "Sí".equalsIgnoreCase(valores.get("Administrador"));

            if (UsuarioDAO.correoYaExiste(correo)) {
                JOptionPane.showMessageDialog(null, "Ese correo ya está registrado.", "Correo duplicado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UsuarioDAO.sincronizarSecuenciaId();

            Usuario usuario = new Usuario(nombre, apellido, correo, contrasena, admin);
            boolean exito = UsuarioDAO.registrar(usuario);

            if (exito) {
                JOptionPane.showMessageDialog(null, "Usuario registrado con éxito.");
                formulario[0].limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar usuario.");
            }
        };

        formulario[0] = new FormularioGenericoAlta(camposConTipos, accionGuardar, "Alta Usuario");
        return formulario[0];
    }
}
