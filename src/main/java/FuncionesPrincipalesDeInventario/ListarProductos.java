package FuncionesPrincipalesDeInventario;

import Datos.ConexionBD;
import GUI.ListadosGenerico;
import GUI.Vprin;
import GUI.EditarGenerico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ListarProductos {

    private final String nombreTabla = "productos";
    private final Vprin ventana;

    public ListarProductos(Vprin ventana) {
        this.ventana = ventana;
        mostrarListado();
    }

    private void mostrarListado() {
        Object[][] datos = obtenerDatosDeTabla();
        if (datos == null) return;

        String[] columnas = obtenerNombresDeColumnas();

        // Creamos el ActionListener con acceso a las variables necesarias
        ActionListener accionEditar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListadosGenerico origen = (ListadosGenerico) SwingUtilities.getWindowAncestor((Component) e.getSource());
                Object[] fila = origen.getFilaSeleccionada();
                if (fila == null) return;

                // Llamamos al EditarGenerico para manejar la edición
                EditarGenerico.mostrarFormularioDeEdicion(nombreTabla, columnas, fila);

                origen.dispose(); // Cerramos la ventana actual
                mostrarListado(); // Recargamos la lista
            }
        };

        // Creamos la ventana de listado y le pasamos la acción
        ListadosGenerico listado = new ListadosGenerico(
                "Listado de Productos",
                columnas,
                datos,
                "Editar",
                accionEditar
        );

        listado.setVisible(true);
    }

    private Object[][] obtenerDatosDeTabla() {
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY
             );
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + nombreTabla)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnas = metaData.getColumnCount();

            rs.last();
            int filas = rs.getRow();
            rs.beforeFirst();

            Object[][] datos = new Object[filas][columnas];
            int filaActual = 0;

            while (rs.next()) {
                for (int i = 0; i < columnas; i++) {
                    datos[filaActual][i] = rs.getObject(i + 1);
                }
                filaActual++;
            }

            return datos;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar productos: " + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private String[] obtenerNombresDeColumnas() {
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + nombreTabla + " LIMIT 1")) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnas = metaData.getColumnCount();
            String[] nombres = new String[columnas];

            for (int i = 0; i < columnas; i++) {
                nombres[i] = metaData.getColumnName(i + 1);
            }

            return nombres;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener columnas: " + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            return new String[0];
        }
    }
}
