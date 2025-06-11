package FuncionesCliente;

import Controladores.AltaGenerico;
import Controladores.ValidadorGenerico;
import datos.ClienteDAO;
import gui.FormularioGenericoAlta;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AltaCliente {

    /**
     * Construye el formulario de alta para clientes usando componentes genéricos.
     *
     * @return JPanel listo para insertar en una ventana
     */
    public JPanel construirFormulario() {
        // 1. Definir los campos con su tipo (todos tipo texto en este caso)
        Map<String, String> camposConTipos = new LinkedHashMap<>();
        camposConTipos.put("Nombre", "String");
        camposConTipos.put("CIF", "cif");
        camposConTipos.put("Email", "email");
        camposConTipos.put("Persona de Contacto", "String");
        camposConTipos.put("Dirección", "String");
        camposConTipos.put("Descripción", "String");

        final FormularioGenericoAlta[] formulario = new FormularioGenericoAlta[1];

        // 2. Acción del botón Guardar
        ActionListener accionGuardar = e -> {
            HashMap<String, String> valores = formulario[0].getValores();

            //validar si el cif existe
            String cif = valores.get("CIF");
            if (ClienteDAO.existeCIF(cif)) {
                JOptionPane.showMessageDialog(null,
                        "Ya existe un cliente con el CIF: " + cif,
                        "CIF duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3. Validación básica por tipo
            if (!ValidadorGenerico.validar(valores, camposConTipos)) return;

            // 4. Insertar si todo es válido
            AltaGenerico.procesarAlta("clientes", valores);
            formulario[0].limpiarCampos();
        };


        // 5. Crear formulario con tipos y acción
        formulario[0] = new FormularioGenericoAlta(camposConTipos, accionGuardar,"Alta Cliente");
        return formulario[0];
    }
}