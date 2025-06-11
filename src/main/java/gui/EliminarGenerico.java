package gui;

import datos.ClienteDAO;
import datos.MateriaPrimaDAO;
import datos.ProductoDAO;
import datos.UsuarioDAO;

public class EliminarGenerico {

    /**
     * Elimina un registro de una tabla utilizando el DAO correspondiente.
     * @param tabla Nombre de la tabla (e.g. "clientes", "productos").
     * @param idValor ID del registro a eliminar.
     * @return true si se eliminó correctamente, false si falló o no existe DAO.
     * @throws IllegalArgumentException si no hay un DAO definido para esa tabla.
     */
    public static boolean eliminarRegistro(String tabla, Object idValor) throws IllegalArgumentException {
        boolean eliminado;

        try {
            int id = Integer.parseInt(idValor.toString());

            switch (tabla) {
                case "clientes" -> eliminado = ClienteDAO.eliminarPorID(id);
                case "productos" -> eliminado = ProductoDAO.eliminarPorID(id);
                case "usuarios" -> eliminado = UsuarioDAO.eliminarPorID(id);
                case "MateriasPrimas" -> eliminado = MateriaPrimaDAO.eliminarPorID(id);
                default -> throw new IllegalArgumentException("No hay DAO definido para la tabla: " + tabla);
            }


        } catch (Exception e) {
            throw new RuntimeException("Error eliminando registro: " + e.getMessage(), e);
        }

        return eliminado;
    }
}
