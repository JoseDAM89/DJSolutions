package Controladores;

import javax.swing.*;

import java.util.HashMap;
import java.util.Map;

public class ValidadorGenerico {

    public static boolean validar(HashMap<String, String> valores, Map<String, String> tipos) {
        for (String campo : valores.keySet()) {
            String valor = valores.get(campo).trim();
            String tipo = tipos.getOrDefault(campo, "string").toLowerCase();

            if (valor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo \"" + campo + "\" no puede estar vacío.");
                return false;
            }

            try {
                switch (tipo) {
                    case "int" -> Integer.parseInt(valor);
                    case "double", "float" -> Double.parseDouble(valor.replace(",", "."));
                    case "boolean" -> {
                        if (!valor.equalsIgnoreCase("sí") && !valor.equalsIgnoreCase("no")) {
                            throw new IllegalArgumentException();
                        }
                    }
                    case "cif" -> {
                        if (!validarCIF(valor)) {
                            mostrarError("El documento debe cumplir los requisitos de CIF/NIF y en mayuscula\nEjemplo: A1234567Z");
                            return false;
                        }
                    }
                    case "email" -> {
                        if (!validarEmail(valor)) {
                            mostrarError("El correo debe tener el formato correcto y terminar en '.com'.\nEjemplo: usuario@dominio.com");
                            return false;
                        }
                    }
                }
            } catch (Exception e) {
                mostrarError("❌ El campo \"" + campo + "\" debe ser de tipo " + tipo + ".");
                return false;
            }
        }

        return true;
    }

    public static boolean validarCIF(String texto) {
        return texto.matches("^[A-Z]\\d{7}[A-Z0-9]$|^\\d{8}[A-Z]$|^[A-Z]{1,3}\\d{6,9}$");
    }

    public static boolean validarEmail(String texto) {
        return texto.matches("^[^@\\s]+@[^@\\s]+\\.com$");
    }

    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Formato inválido", JOptionPane.WARNING_MESSAGE);
    }
}
