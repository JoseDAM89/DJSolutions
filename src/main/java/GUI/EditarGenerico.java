package GUI;

import Datos.ConexionBD;
import Modelos.Cliente;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditarGenerico {

    /**
     * Muestra un formulario para editar los datos de una fila seleccionada de una tabla.
     *
     * @param tabla    Nombre de la tabla en la base de datos.
     * @param columnas Nombres visibles de las columnas (los que ve el usuario).
     * @param fila     Datos actuales de la fila seleccionada.
     */
    public static void mostrarFormularioDeEdicion(String tabla, String[] columnas, Object[] fila,
                                                  String columnaID, Object idValor, String tipoID,
                                                  JTable tablaSwing, int filaTabla) {
        JTextField[] campos = new JTextField[fila.length];
        JPanel panel = new JPanel(new GridLayout(fila.length, 2));

        for (int i = 0; i < fila.length; i++) {
            panel.add(new JLabel(columnas[i]));
            if (columnas[i].equalsIgnoreCase("Materia Prima")) {
                JComboBox<String> combo = new JComboBox<>(new String[]{"Sí", "No"});
                combo.setSelectedItem(fila[i].toString());
                campos[i] = new JTextField(); // usamos esto como marcador
                campos[i].setName("comboMateriaPrima");
                panel.add(combo);
            } else {
                campos[i] = new JTextField(fila[i].toString());
                panel.add(campos[i]);
            }
        }

        int resultado = JOptionPane.showConfirmDialog(null, panel, "Editar Registro",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {

            if (tabla.equalsIgnoreCase("clientes")) {
                try {
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
                    } else {
                        JOptionPane.showMessageDialog(null, "❌ Error al actualizar el cliente.");
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "❌ Error al procesar los datos del formulario.");
                    e.printStackTrace();
                }

            } else if (tabla.equalsIgnoreCase("productos")) {
                try {
                    String nombre = "";
                    double precio = 0.0;
                    String descripcion = "";
                    int stock = 0;
                    boolean materiaPrima = true;
                    int idMateriaPrima = 0;

                    for (int i = 0; i < columnas.length; i++) {
                        String col = columnas[i].toLowerCase();
                        String valor;
                        if (campos[i].getName() != null && campos[i].getName().equals("comboMateriaPrima")) {
                            JComboBox combo = (JComboBox) panel.getComponent(i * 2 + 1);
                            valor = combo.getSelectedItem().toString();
                        } else {
                            valor = campos[i].getText();
                        }

                        switch (col) {
                            case "nombre" -> nombre = valor;
                            case "precio" -> precio = Double.parseDouble(valor);
                            case "descripción", "descripcion" -> descripcion = valor;
                            case "stock", "cantidad" -> stock = Integer.parseInt(valor);
                            case "materia prima" -> materiaPrima = valor.equalsIgnoreCase("sí");
                            case "id materia", "idmateriaprima" -> idMateriaPrima = Integer.parseInt(valor);
                        }
                    }

                    Modelos.Producto producto = new Modelos.Producto(
                            (int) idValor,
                            nombre,
                            precio,
                            descripcion,
                            stock,
                            materiaPrima,
                            idMateriaPrima
                    );

                    boolean actualizado = Datos.ProductoDAO.actualizarPorID(producto);

                    if (actualizado) {
                        tablaSwing.setValueAt(nombre, filaTabla, 1);
                        tablaSwing.setValueAt(precio, filaTabla, 2);
                        tablaSwing.setValueAt(descripcion, filaTabla, 3);
                        tablaSwing.setValueAt(stock, filaTabla, 4);
                        tablaSwing.setValueAt(materiaPrima ? "Sí" : "No", filaTabla, 5);
                        tablaSwing.setValueAt(idMateriaPrima, filaTabla, 6);

                        JOptionPane.showMessageDialog(null, "✅ Producto actualizado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "❌ Error al actualizar el producto.");
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "❌ Error al procesar los datos del producto.");
                    e.printStackTrace();
                }

            } else {
                Map<String, String> mapeo = generarMapeoColumnas(tabla, columnas);

                for (int i = 0; i < columnas.length; i++) {
                    String columnaBD = mapeo.getOrDefault(columnas[i], columnas[i]);
                    String nuevoValor = campos[i].getText();
                    actualizarBD(tabla, columnaBD, nuevoValor, columnaID, idValor, tipoID);
                }

                JOptionPane.showMessageDialog(null, "✅ Registro actualizado correctamente.");
            }

        }
    }


    /**
     * Ejecuta un UPDATE en la base de datos con el nuevo valor para una columna.
     *
     * @param tabla      Nombre de la tabla.
     * @param columna    Nombre real de la columna en la base de datos.
     * @param nuevoValor Nuevo valor que se va a guardar.
     * @param columnaID  Nombre de la columna clave primaria (ej: idcliente).
     * @param idValor    Valor del ID del registro a actualizar.
     */
    private static void actualizarBD(String tabla, String columna, String nuevoValor,
                                     String columnaID, Object idValor, String tipoID) {
        String sql = "UPDATE " + tabla + " SET " + columna + " = ? WHERE " + columnaID + " = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, nuevoValor);

            // Convertir correctamente el tipo de clave
            if (tipoID.equalsIgnoreCase("INTEGER")) {
                stmt.setInt(2, Integer.parseInt(idValor.toString()));
            } else {
                stmt.setObject(2, idValor);
            }

            stmt.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Genera un mapa entre los nombres visibles de las columnas y sus nombres reales en la base de datos.
     * Este método asume que el orden de las columnas visibles y reales coincide.
     *
     * @param tabla    Nombre de la tabla.
     * @param visibles Array de nombres visibles (los que se muestran en pantalla).
     * @return Mapa con clave = nombre visible, valor = nombre real de columna.
     */
    private static Map<String, String> generarMapeoColumnas(String tabla, String[] visibles) {
        String[] reales = obtenerNombresReales(tabla); // Extrae desde la BD
        Map<String, String> mapeo = new HashMap<>();

        // Asocia cada nombre visible con el nombre real correspondiente
        for (int i = 0; i < visibles.length && i < reales.length; i++) {
            mapeo.put(visibles[i], reales[i]);
        }

        return mapeo;
    }

    /**
     * Obtiene los nombres reales de las columnas de una tabla en orden.
     * Usa `information_schema.columns`, que es una tabla interna de PostgreSQL.
     *
     * @param tabla Nombre de la tabla en minúsculas.
     * @return Array con los nombres reales de columna.
     */
    private static String[] obtenerNombresReales(String tabla) {
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT column_name FROM information_schema.columns WHERE table_name = ? ORDER BY ordinal_position")) {

            stmt.setString(1, tabla.toLowerCase()); // PostgreSQL guarda los nombres en minúsculas
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> columnas = new ArrayList<>();
            while (rs.next()) {
                columnas.add(rs.getString("column_name"));
            }

            return columnas.toArray(new String[0]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener columnas reales: " + e.getMessage());
            return new String[0];
        }
    }
}
