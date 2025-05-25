package Controladores;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ValidadorGenerico {

    /**
     * Valida que los campos no estén vacíos y tengan el tipo esperado.
     *
     * @param valores HashMap con etiqueta → valor introducido
     * @param tipos   HashMap con etiqueta → tipo esperado ("int", "double", "boolean", etc.)
     * @return true si todo está correcto, false si hay errores
     */
    public static boolean validar(HashMap<String, String> valores, Map<String, String> tipos) {
        for (String campo : valores.keySet()) {
            String valor = valores.get(campo).trim();
            String tipo = tipos.getOrDefault(campo, "string").toLowerCase();

            // Validación de campo vacío
            if (valor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo \"" + campo + "\" no puede estar vacío.");
                return false;
            }

            // Validación de tipo
            try {
                switch (tipo) {
                    case "int" -> Integer.parseInt(valor);
                    case "double", "float" -> {
                        Double.parseDouble(valor.replace(",", "."));  // normaliza antes de parsear
                    }
                    case "boolean" -> {
                        if (!valor.equalsIgnoreCase("sí") && !valor.equalsIgnoreCase("no")) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "❌ El campo \"" + campo + "\" debe ser de tipo " + tipo + ".");
                return false;
            }
        }

        return true; // Todo correcto
    }
}
