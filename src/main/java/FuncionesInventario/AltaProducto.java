package FuncionesInventario;

import Controladores.AltaGenerico;
import Controladores.ValidadorGenerico;
import gui.FormularioGenericoAlta;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AltaProducto {

    /**
     * Construye el formulario de alta para productos.
     *
     * @return JPanel con los campos y botón para guardar.
     */
    public JPanel construirFormulario() {
        // 1. Mapa de campos con sus tipos
        Map<String, String> camposConTipos = new LinkedHashMap<>();
        camposConTipos.put("COD", "int");
        camposConTipos.put("Nombre", "String");
        camposConTipos.put("Precio", "double");
        camposConTipos.put("Descripción", "String");
        camposConTipos.put("Stock", "int");
        camposConTipos.put("Materia Prima", "boolean");
        camposConTipos.put("ID Materia", "int");

        final FormularioGenericoAlta[] formulario = new FormularioGenericoAlta[1];

        // 2. Acción del botón Guardar
        ActionListener accionGuardar = e -> {
            HashMap<String, String> valores = formulario[0].getValores();

            // 3. Validación de tipos
            if (!ValidadorGenerico.validar(valores, camposConTipos)) return;

            // 4. Alta solo si los datos son correctos
            boolean exito = AltaGenerico.procesarAlta("productos", valores);
            if (exito) {
                formulario[0].limpiarCampos(); // ✅ solo limpiamos si fue exitoso
            }
        };

        // 5. Crear el formulario con tipos y acción
        formulario[0] = new FormularioGenericoAlta(camposConTipos, accionGuardar, "Alta Productos");
        return formulario[0];
    }
}
