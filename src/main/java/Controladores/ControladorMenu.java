package Controladores;

import FuncionesCliente.AltaCliente;
import FuncionesCliente.ListarClientes;
import FuncionesInventario.*;
import gui.*;
import FuncionesPresupuesto.SeleccionarMateriales;
import FuncionesDeUsuarios.AltaUser;
import FuncionesDeUsuarios.ListarUsuarios;
import gui.formularios.Form_Home;


import javax.swing.*;

public class ControladorMenu {

    public static JPanel obtenerFormulario(int menuIndex, int subMenuIndex) {
        switch (menuIndex) {
            case 0: // Funciones Cliente
                return switch (subMenuIndex) {
                    case 0 -> new AltaCliente().construirFormulario();
                    case 1 -> new ListarClientes().mostrarVentana();
                    default -> null;

                };
            case 1: // Funciones Inventario
                return switch (subMenuIndex) {
                    case 0 -> new AltaProducto().construirFormulario();
                    case 1 -> new ListarProductos().mostrarVentana();
                    case 2 -> new AltaMateriaPrima().construirFormulario();
                    case 3 -> new ListarMateriasPrimas().mostrarVentana();
                    case 4 -> new ConsultarStock();
                    default -> null;
                };
            case 2: // Funciones Presupuesto
                return switch (subMenuIndex) {
                    case 0 -> new GenerarDocumentoPanel(GenerarDocumentoPanel.TipoDocumento.PRESUPUESTO);
                    case 1 -> new HistorialDocumentosPanel(HistorialDocumentosPanel.TipoDocumento.PRESUPUESTO);
                    case 2 -> new SeleccionarMateriales();
                    default -> null;
                };
            case 3: // Funciones Factura
            return switch (subMenuIndex) {
                case 0 -> new GenerarDocumentoPanel(GenerarDocumentoPanel.TipoDocumento.FACTURA);
                case 1 -> new HistorialDocumentosPanel(HistorialDocumentosPanel.TipoDocumento.FACTURA);
                default -> null;
            };
            case 4: // Funciones de Precios
                return switch (subMenuIndex) {
                    case 0 -> new PrecioProductoPanel(); // Mostrará el panel de cálculo de precio y alta
                    default -> null;
                };

            case 5: // Gestión de Usuarios (solo visible si es Admin)
                return switch (subMenuIndex) {
                    case 0 -> new AltaUser().construirFormulario();
                    case 1 -> new ListarUsuarios().mostrarVentana();
                    default -> null;
                };
            default:
                return new Form_Home();
        }
    }

}