package FuncionesInventario;

import Controladores.AltaGenerico;
import Controladores.ValidadorGenerico;
import gui.FormularioGenericoAlta;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AltaMateriaPrima {

    /**
     * Construye el formulario de alta para materias primas.
     *
     * @return JPanel con los campos y botón para guardar.
     */
    public JPanel construirFormulario() {
        // 1. Definir campos y tipos
        Map<String, String> camposConTipos = new LinkedHashMap<>();
        camposConTipos.put("Descripción", "String");
        camposConTipos.put("Stock", "double");

        final FormularioGenericoAlta[] formulario = new FormularioGenericoAlta[1];

        // 2. Acción del botón Guardar
        ActionListener accionGuardar = e -> {
            HashMap<String, String> valores = formulario[0].getValores();

            // 3. Validación de tipos
            if (!ValidadorGenerico.validar(valores, camposConTipos)) return;

            // 4. Proceso de alta
            boolean exito = AltaGenerico.procesarAlta("materiasprimas", valores);
            if (exito) {
                formulario[0].limpiarCampos();
            }
        };

        // 5. Crear y devolver formulario
        formulario[0] = new FormularioGenericoAlta(camposConTipos, accionGuardar, "Alta Materia Prima");
        return formulario[0];
    }
}
