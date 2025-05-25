package Controladores;

import GUI.EditarGenerico;
import GUI.EliminarGenerico;
import GUI.ListadosGenerico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ListarGenerico {

    /**
     * Muestra un listado reutilizable en una tabla con acciones opcionales de editar/eliminar.
     *
     * @param tablaNombre   Nombre de la tabla (ej: "clientes", "productos").
     * @param datos         Datos a mostrar en formato bidimensional (incluyendo ID).
     * @param columnas      Nombres de columnas visibles (incluyendo "ID" si aplica).
     * @param accionEditar  Lógica del botón "Editar" (puede ser null si no se desea).
     * @param accionEliminar Lógica del botón "Eliminar" (puede ser null si no se desea).
     */
    public static void mostrarListado(String tablaNombre, Object[][] datos, String[] columnas,
                                      ActionListener accionEditar, ActionListener accionEliminar) {

        // Crear tabla genérica visual
        ListadosGenerico tabla = new ListadosGenerico(
                "Listado de " + tablaNombre.substring(0, 1).toUpperCase() + tablaNombre.substring(1),
                columnas,
                datos,
                (accionEditar != null) ? "Editar" : null,
                accionEditar
        );

        // Panel de botones adicional si se quiere añadir "Eliminar"
        if (accionEliminar != null) {
            JPanel panelInferior = new JPanel();
            if (tabla.getBotonAccion() != null) {
                panelInferior.add(tabla.getBotonAccion()); // Botón Editar
            }

            JButton botonEliminar = new JButton("Eliminar");
            botonEliminar.addActionListener(accionEliminar);
            panelInferior.add(botonEliminar);

            tabla.add(panelInferior, BorderLayout.SOUTH);
        }

        tabla.setVisible(true);
    }
}
