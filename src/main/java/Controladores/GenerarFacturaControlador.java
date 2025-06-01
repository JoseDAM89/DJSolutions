package Controladores;

import datos.ClienteDAO;
import datos.FacturaDAO;
import datos.ProductoDAO;
import modelos.Cliente;
import modelos.Factura;
import modelos.LineaFactura;
import modelos.Producto;

import java.util.List;

public class GenerarFacturaControlador {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final FacturaDAO facturaDAO = new FacturaDAO();

    public List<Cliente> obtenerClientes() {
        return clienteDAO.listarTodos();
    }

    public List<Producto> obtenerProductos() {
        return productoDAO.listarTodos();
    }

    public int guardarFactura(Factura factura) {
        return facturaDAO.insertarFactura(factura);
    }

    public Cliente obtenerClientePorID(int id) {
        return clienteDAO.obtenerPorID(id);
    }

    public Producto obtenerProductoPorID(int id) {
        return productoDAO.obtenerPorID(id);
    }
}
