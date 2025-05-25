package Controladores;

import GUI.ListadosGenerico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ListarGenerico {

    /**
     * Crea un JPanel que contiene un listado reutilizable en una tabla con acciones opcionales de editar/eliminar.
     *
     * @param tablaNombre    Nombre de la tabla (ej: "clientes", "productos").
     * @param datos          Datos a mostrar en formato bidimensional (incluyendo ID).
     * @param columnas       Nombres de columnas visibles (incluyendo "ID" si aplica).
     * @param accionEditar   Lógica del botón "Editar" (puede ser null si no se desea).
     * @param accionEliminar Lógica del botón "Eliminar" (puede ser null si no se desea).
     * @return JPanel con el listado y los botones necesarios.
     */
    public static JPanel crearListadoPanel(String tablaNombre, Object[][] datos, String[] columnas,
                                           ActionListener accionEditar, ActionListener accionEliminar) {

        // Crear tabla genérica visual
        ListadosGenerico tabla = new ListadosGenerico(
                "Listado de " + tablaNombre.substring(0, 1).toUpperCase() + tablaNombre.substring(1),
                columnas,
                datos,
                (accionEditar != null) ? "Editar" : null,
                accionEditar
        );

        // Panel principal para devolver
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(tabla, BorderLayout.CENTER);

        // Panel de botones adicional si se quiere añadir "Eliminar"
        if (accionEliminar != null) {
            JPanel panelInferior = new JPanel();
            if (tabla.getBotonAccion() != null) {
                panelInferior.add(tabla.getBotonAccion()); // Botón Editar
            }

            JButton botonEliminar = new JButton("Eliminar");
            botonEliminar.addActionListener(accionEliminar);
            panelInferior.add(botonEliminar);

            panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        }

        return panelPrincipal;
    }
}
