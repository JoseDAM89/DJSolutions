package FuncionesInventario;

import Datos.ProductoDAO;
import Modelos.Producto;
import GUI.ListadosGenerico;
import GUI.EditarGenerico;
import GUI.EliminarGenerico;

import javax.swing.*;
import java.util.List;

public class ListarProductos {

    public void mostrarVentana() {
        // 1. Obtener todos los productos desde la base de datos usando DAO
        List<Producto> productos = ProductoDAO.listarTodos();

        // 2. Definir nombres de columnas (coinciden con lo mostrado en la tabla)
        String[] columnas = {
                "ID", "Nombre", "Precio", "Descripción", "Stock", "Materia Prima", "ID Materia"
        };

        // 3. Convertir la lista de productos en una matriz para JTable
        Object[][] datos = new Object[productos.size()][columnas.length];
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            datos[i][0] = p.getCodproduct();
            datos[i][1] = p.getNombreproduct();
            datos[i][2] = p.getPrecioproduct();
            datos[i][3] = p.getDescripcionproduct();
            datos[i][4] = p.getStockproduct();
            datos[i][5] = p.isMateriaprima() ? "Sí" : "No";
            datos[i][6] = p.getIdmateriaprima();
        }

        // 4. Crear tabla reutilizable con botón "Editar"
        final ListadosGenerico[] tablaProductos = new ListadosGenerico[1];

        ListadosGenerico tabla = new ListadosGenerico(
                "Listado de Productos",
                columnas,
                datos,
                "Editar",
                e -> {
                    Object[] fila = tablaProductos[0].getFilaSeleccionada();
                    int filaSeleccionada = tablaProductos[0].getTabla().getSelectedRow();

                    if (fila != null && filaSeleccionada != -1) {
                        int id = (int) fila[0];
                        String nombre = (String) fila[1];
                        System.out.println("Editar Producto con ID: " + id + ", Nombre: " + nombre);

                        // Llamar al formulario genérico de edición
                        EditarGenerico.mostrarFormularioDeEdicion(
                                "productos",
                                columnas,
                                fila,
                                "codproduct",
                                fila[0],
                                "INTEGER",
                                tablaProductos[0].getTabla(),
                                filaSeleccionada
                        );
                    }
                }
        );

        // 5. Crear botón "Eliminar"
        JButton botonEliminar = new JButton("Eliminar");
        botonEliminar.addActionListener(e -> {
            int filaSeleccionada = tabla.getTabla().getSelectedRow();
            if (filaSeleccionada != -1) {
                Object[] fila = tabla.getFilaSeleccionada();
                EliminarGenerico.eliminarRegistro(
                        "productos",
                        "codproduct",
                        fila[0],
                        "INTEGER",
                        tabla.getTabla(),
                        filaSeleccionada
                );
            } else {
                JOptionPane.showMessageDialog(tabla, "Selecciona un producto para eliminar.");
            }
        });

        // 6. Añadir ambos botones en el panel inferior
        JPanel panelBotones = new JPanel();
        panelBotones.add(tabla.getBotonAccion());  // Botón Editar
        panelBotones.add(botonEliminar);           // Botón Eliminar
        tabla.add(panelBotones, java.awt.BorderLayout.SOUTH);

        // 7. Mostrar tabla
        tablaProductos[0] = tabla;
        tabla.setVisible(true);
    }
}
