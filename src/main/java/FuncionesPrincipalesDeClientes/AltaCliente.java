package FuncionesPrincipalesDeClientes;

import Datos.ClienteDAO;
import GUI.FormularioGenericoAlta;
import Modelos.Cliente;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class AltaCliente {

    /**
     * Método que construye el formulario genérico para dar de alta un cliente.
     * @return JPanel listo para insertar en una ventana
     */
    public JPanel construirFormulario() {
        // Campos que queremos que aparezcan en el formulario
        String[] campos = {
                "Nombre", "CIF", "Email", "Persona de Contacto", "Dirección", "Descripción"
        };

        // Usamos un array para poder usar la variable dentro del ActionListener
        final FormularioGenericoAlta[] formulario = new FormularioGenericoAlta[1];

        // Definimos la acción del botón guardar
        ActionListener accionGuardar = e -> {
            HashMap<String, String> valores = formulario[0].getValores();

            // Validamos que no haya campos vacíos
            if (valores.values().stream().anyMatch(String::isEmpty)) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                return;
            }

            // Creamos el objeto Cliente con los valores
            Cliente cliente = new Cliente(
                    valores.get("Nombre"),
                    valores.get("CIF"),
                    valores.get("Email"),
                    valores.get("Persona de Contacto"),
                    valores.get("Dirección"),
                    valores.get("Descripción")
            );

            // Insertamos el cliente en la base de datos
            boolean guardado = ClienteDAO.insertar(cliente);

            if (guardado) {
                JOptionPane.showMessageDialog(null, "Cliente guardado en la base de datos correctamente.");
                formulario[0].limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar el cliente en la base de datos.");
            }
        };

        // Creamos el formulario genérico pasándole los campos y la acción del botón
        formulario[0] = new FormularioGenericoAlta(campos, accionGuardar);

        return formulario[0];
    }
}
