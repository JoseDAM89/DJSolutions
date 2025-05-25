package gui;

import Modelos.Cliente;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EditarGenerico {

    public static JPanel crearFormularioEdicion(String tabla, String[] columnas, Object[] fila,
                                                String columnaID, Object idValor, String tipoID,
                                                JTable tablaSwing, int filaTabla, Runnable onSuccess) {

        JTextField[] campos = new JTextField[fila.length];
        JPanel panel = new JPanel(new GridLayout(fila.length + 1, 2)); // +1 para el botón
        Map<Integer, JComboBox<String>> combos = new HashMap<>();

        for (int i = 0; i < fila.length; i++) {
            panel.add(new JLabel(columnas[i]));

            if (columnas[i].equalsIgnoreCase("Materia Prima")) {
                JComboBox<String> combo = new JComboBox<>(new String[]{"Sí", "No"});
                combo.setSelectedItem(fila[i].toString());
                panel.add(combo);
                combos.put(i, combo);
            } else {
                campos[i] = new JTextField(fila[i].toString());
                panel.add(campos[i]);
            }
        }

        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.addActionListener(e -> {
            try {
                if (tabla.equalsIgnoreCase("clientes")) {
                    String nombre = "", cif = "", email = "", contacto = "", direccion = "", descripcion = "";

                    for (int i = 0; i < columnas.length; i++) {
                        String col = columnas[i].toLowerCase();
                        String valor = campos[i].getText();
                        switch (col) {
                            case "nombre" -> nombre = valor;
                            case "cif" -> cif = valor;
                            case "email" -> email = valor;
                            case "persona de contacto" -> contacto = valor;
                            case "dirección", "direccion" -> direccion = valor;
                            case "descripción", "descripcion" -> descripcion = valor;
                        }
                    }

                    if (cif.length() > 15) {
                        JOptionPane.showMessageDialog(null, "❌ El CIF no puede tener más de 15 caracteres.");
                        return;
                    }

                    Cliente cliente = new Cliente((int) idValor, nombre, cif, email, contacto, direccion, descripcion);
                    boolean actualizado = Datos.ClienteDAO.actualizarPorID(cliente);

                    if (actualizado) {
                        tablaSwing.setValueAt(nombre, filaTabla, 1);
                        tablaSwing.setValueAt(cif, filaTabla, 2);
                        tablaSwing.setValueAt(email, filaTabla, 3);
                        tablaSwing.setValueAt(contacto, filaTabla, 4);
                        tablaSwing.setValueAt(direccion, filaTabla, 5);
                        tablaSwing.setValueAt(descripcion, filaTabla, 6);
                        JOptionPane.showMessageDialog(null, "✅ Cliente actualizado correctamente.");
                        if (onSuccess != null) onSuccess.run();
                    }

                } else if (tabla.equalsIgnoreCase("productos")) {
                    // Mismo patrón: obtener campos, validar, actualizar
                    // Si quieres también esto como JPanel completo avísame, lo separo igual que clientes
                }

                // Otros tipos genéricos se manejarían aquí...

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Error al guardar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        panel.add(new JLabel());
        panel.add(btnGuardar);

        return panel;
    }
}
