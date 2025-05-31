package Controladores;

import datos.ClienteDAO;
import datos.ProductoDAO;
import modelos.Cliente;
import modelos.Producto;

import javax.swing.*;
import java.util.HashMap;

public class AltaGenerico {

    /**
     * Procesa los datos del formulario y llama al DAO correspondiente para insertar.
     *
     * @param tabla      Nombre de la tabla (ej: "clientes", "productos")
     * @param valores    HashMap con clave = etiqueta del campo, valor = contenido escrito
     */
    public static boolean procesarAlta(String tabla, HashMap<String, String> valores) {
        boolean insertado = false;

        try {
            switch (tabla.toLowerCase()) {

                case "clientes" -> {
                    // Extraer valores
                    String nombre = valores.get("Nombre");
                    String cif = valores.get("CIF");
                    String email = valores.get("Email");
                    String contacto = valores.get("Persona de Contacto");
                    String direccion = valores.get("Dirección");
                    String descripcion = valores.get("Descripción");

                    Cliente cliente = new Cliente(nombre, cif, email, contacto, direccion, descripcion);
                    insertado = ClienteDAO.insertar(cliente);
                }

                case "productos" -> {
                    int codproduct = Integer.parseInt(valores.get("COD"));
                    String nombre = valores.get("Nombre");
                    double precio = Double.parseDouble(valores.get("Precio").replace(",", "."));
                    String descripcion = valores.get("Descripción");
                    int stock = Integer.parseInt(valores.get("Stock"));
                    boolean materiaPrima = valores.get("Materia Prima").equalsIgnoreCase("Sí");
                    int idMateriaPrima = Integer.parseInt(valores.get("ID Materia"));

                    Producto producto = new Producto(codproduct, nombre, precio, descripcion, stock, materiaPrima, idMateriaPrima);
                    insertado = ProductoDAO.insertar(producto);
                }

                default -> {
                    JOptionPane.showMessageDialog(null, "❌ Tabla no soportada: " + tabla);
                    return false;
                }
            }

            if (insertado) {
                JOptionPane.showMessageDialog(null, "✅ Registro insertado correctamente.");
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "❌ Error al procesar datos: " + e.getMessage());
                 e.printStackTrace();
            return false;
    }

}
}
