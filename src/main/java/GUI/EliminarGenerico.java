package GUI;

import Datos.ClienteDAO;
import Datos.ProductoDAO;

import javax.swing.*;

public class EliminarGenerico {

    /**
     * Elimina un registro de cualquier tabla por ID.
     * Si existe un DAO específico, se usará en lugar de SQL directo.
     */
    public static void eliminarRegistro(String tabla, String columnaID, Object idValor, String tipoID,
                                        JTable tablaSwing, int filaTabla) {

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Estás seguro de que deseas eliminar este registro?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) return;

        boolean eliminado = false;

        try {
            int id = Integer.parseInt(idValor.toString());

            switch (tabla.toLowerCase()) {
                case "clientes" -> eliminado = ClienteDAO.eliminarPorID(id);
                case "productos" -> eliminado = ProductoDAO.eliminarPorID(id);
                default -> {
                    // Opción por defecto si no hay DAO específico (opcional)
                    JOptionPane.showMessageDialog(null,
                            "❌ No hay DAO definido para eliminar de la tabla '" + tabla + "'");
                    return;
                }
            }

            if (eliminado) {
                ((javax.swing.table.DefaultTableModel) tablaSwing.getModel()).removeRow(filaTabla);
                JOptionPane.showMessageDialog(null, "✅ Registro eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "❌ No se pudo eliminar el registro.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al eliminar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
