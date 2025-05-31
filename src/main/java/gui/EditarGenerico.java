package gui;

import modelos.Cliente;
import modelos.Producto;
import datos.ProductoDAO;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EditarGenerico {

    public static JPanel crearFormularioEdicion(String tabla, String[] columnas, Object[] fila,
                                                String columnaID, Object idValor, String tipoID,
                                                JTable tablaSwing, int filaTabla,
                                                Consumer<Object[]> onSuccess) {

        JTextField[] campos = new JTextField[fila.length];
        JPanel panel = new JPanel(new GridBagLayout());
        Map<Integer, JComboBox<String>> combos = new HashMap<>();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < fila.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(columnas[i]), gbc);

            gbc.gridx = 1;
            if (columnas[i].equalsIgnoreCase("Materia Prima")) {
                JComboBox<String> combo = new JComboBox<>(new String[]{"Sí", "No"});
                combo.setSelectedItem(fila[i].toString());
                panel.add(combo, gbc);
                combos.put(i, combo);
            } else if (columnas[i].equalsIgnoreCase("ID Materia")) {
                JComboBox<String> comboMateria = new JComboBox<>();
                boolean esMateriaPrima = false;
                for (int k = 0; k < columnas.length; k++) {
                    if (columnas[k].equalsIgnoreCase("Materia Prima")) {
                        esMateriaPrima = fila[k].toString().equalsIgnoreCase("Sí");
                        break;
                    }
                }

                if (!esMateriaPrima) {
                    comboMateria.addItem("0 - Ninguna");
                }

                datos.MateriaPrimaDAO.obtenerTodas().forEach((id, nombre) ->
                        comboMateria.addItem(id + " - " + nombre));


                String valorActual = fila[i].toString();
                for (int j = 0; j < comboMateria.getItemCount(); j++) {
                    if (comboMateria.getItemAt(j).startsWith(valorActual + " -") || comboMateria.getItemAt(j).equals(valorActual)) {
                        comboMateria.setSelectedIndex(j);
                        break;
                    }
                }

                panel.add(comboMateria, gbc);
                combos.put(i, comboMateria);
            } else {
                campos[i] = new JTextField(fila[i].toString());
                panel.add(campos[i], gbc);
            }
        }

        // 🔄 Vincular comportamiento entre "Materia Prima" y "ID Materia"
        Integer idxMateriaPrima = null, idxIDMateria = null;

        for (int i = 0; i < columnas.length; i++) {
            if (columnas[i].equalsIgnoreCase("Materia Prima")) idxMateriaPrima = i;
            if (columnas[i].equalsIgnoreCase("ID Materia")) idxIDMateria = i;
        }

        if (idxMateriaPrima != null && idxIDMateria != null) {
            JComboBox<String> comboMP = combos.get(idxMateriaPrima);
            JComboBox<String> comboID = combos.get(idxIDMateria);

            // Estado inicial
            boolean esSi = comboMP.getSelectedItem().toString().equalsIgnoreCase("Sí");
            comboID.setEnabled(esSi);
            if (!esSi) comboID.setSelectedIndex(0);

            // Comportamiento al cambiar
            comboMP.addActionListener(e -> {
                boolean esSiValor = comboMP.getSelectedItem().toString().equalsIgnoreCase("Sí");

                if (!esSiValor) {
                    DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboID.getModel();
                    if (model.getIndexOf("0 - Ninguna") == -1) {
                        model.insertElementAt("0 - Ninguna", 0);
                    }
                    comboID.setSelectedItem("0 - Ninguna");
                    comboID.setEnabled(false);
                } else {
                    DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboID.getModel();
                    int index = model.getIndexOf("0 - Ninguna");
                    if (index != -1) {
                        model.removeElementAt(index);
                    }
                    comboID.setEnabled(true);
                    if (comboID.getItemCount() > 0 && comboID.getSelectedIndex() == -1) {
                        comboID.setSelectedIndex(0);
                    }
                }
            });
        }

        JButton btnGuardar = new JButton("Guardar cambios");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            try {
                Object[] filaActualizada = new Object[fila.length];

                if (tabla.equalsIgnoreCase("clientes")) {
                    String nombre = "", cif = "", email = "", contacto = "", direccion = "", descripcion = "";

                    for (int i = 0; i < columnas.length; i++) {
                        String col = columnas[i].toLowerCase();
                        String valor = campos[i] != null ? campos[i].getText() : "";
                        filaActualizada[i] = valor; // ⚠️ Para refrescar fila completa
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
                    boolean actualizado = datos.ClienteDAO.actualizarPorID(cliente);

                    if (actualizado) {
                        JOptionPane.showMessageDialog(null, "✅ Cliente actualizado correctamente.");
                        if (onSuccess != null) onSuccess.accept(filaActualizada);
                    }

                } else if (tabla.equalsIgnoreCase("productos")) {
                    String nombre = "", descripcion = "";
                    double precio = 0.0;
                    int stock = 0, idmateria = 0;
                    boolean esMateriaPrima = false;

                    for (int i = 0; i < columnas.length; i++) {
                        String col = columnas[i].toLowerCase();
                        if (col.equals("materia prima")) {
                            JComboBox<String> combo = combos.get(i);
                            esMateriaPrima = combo.getSelectedItem().toString().equalsIgnoreCase("Sí");
                            filaActualizada[i] = esMateriaPrima ? "Sí" : "No";
                        } else if (col.equals("id materia")) {
                            JComboBox<String> comboMateria = combos.get(i);
                            String seleccion = comboMateria.getSelectedItem().toString();
                            idmateria = (seleccion.equals("0 - Ninguna") || !esMateriaPrima)
                                    ? 0
                                    : Integer.parseInt(seleccion.split(" - ")[0].trim());
                            filaActualizada[i] = idmateria;
                        } else {
                            String valor = campos[i].getText();
                            filaActualizada[i] = valor;
                            switch (col) {
                                case "nombre" -> nombre = valor;
                                case "precio" -> precio = Double.parseDouble(valor);
                                case "descripción", "descripcion" -> descripcion = valor;
                                case "stock" -> stock = Integer.parseInt(valor);
                            }
                        }
                    }

                    Producto p = new Producto((int) idValor, nombre, precio, descripcion, stock, esMateriaPrima, idmateria);
                    boolean actualizado = ProductoDAO.actualizarPorID(p);

                    if (actualizado) {
                        JOptionPane.showMessageDialog(null, "✅ Producto actualizado correctamente.");
                        if (onSuccess != null) onSuccess.accept(filaActualizada);
                    } else {
                        JOptionPane.showMessageDialog(null, "❌ Error al actualizar producto.");
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Error al guardar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnCancelar.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());

        gbc.gridy = fila.length;
        gbc.gridx = 0;
        panel.add(btnGuardar, gbc);
        gbc.gridx = 1;
        panel.add(btnCancelar, gbc);

        return panel;
    }
}
