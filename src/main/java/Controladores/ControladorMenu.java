package Controladores;

import FuncionesCliente.AltaCliente;
import FuncionesCliente.ListarClientes;
import FuncionesInventario.AltaProducto;
import FuncionesInventario.ConsultarStock;
import FuncionesInventario.ListarProductos;
import FuncionesPresupuesto.GenerarPresupuesto;
import FuncionesPresupuesto.HistorialDePresupuestos;
import FuncionesPresupuesto.SeleccionarMateriales;
import GestionDeUsuarios.RegistrarUser;
import GestionDeUsuarios.VerUsuarios;
import gui.GenerarPresupuestoPanel;
import gui.formularios.Form1;
import gui.formularios.Form_Home;

import javax.swing.*;

public class ControladorMenu {

    public static JPanel obtenerFormulario(int menuIndex, int subMenuIndex) {
        switch (menuIndex) {
            case 0: // Funciones Cliente
                return switch (subMenuIndex) {
                    case 0 -> new AltaCliente().construirFormulario();
                    case 1 -> new Form1(); // Placeholder para "Editar Clientes"
                    case 2 -> new ListarClientes().mostrarVentana();
                    default -> new AltaCliente().construirFormulario();
//                  default -> panelVacio();

                };
            case 1: // Funciones Inventario
                return switch (subMenuIndex) {
                    case 0 -> new AltaProducto().construirFormulario();
                    case 1 -> new ConsultarStock();
                    case 2 -> new ListarProductos().mostrarVentana();
                    case 3 -> new ListarProductos().mostrarVentana(); // Ver Alerta Stock (reutilizado por ahora)
                    default -> new AltaProducto().construirFormulario();
//                  default -> panelVacio();
                };
            case 2: // Funciones Presupuesto
                return switch (subMenuIndex) {
                    case 0 -> FabricaPresupuesto.crearPanel();

                    case 1 -> new HistorialDePresupuestos();
                    case 2 -> new SeleccionarMateriales();
                    default -> new GenerarPresupuesto();
//                  default -> panelVacio();
                };
            case 3: // Gestión de Usuarios (solo visible si es Admin)
                return switch (subMenuIndex) {
                    case 0 -> new RegistrarUser();
                    case 1 -> new VerUsuarios();
                    default -> new RegistrarUser();

//                  default -> panelVacio();
                };
            default:
                return new Form_Home();
        }
    }

    private static JPanel panelVacio() {
        JPanel panel = new JPanel();

        panel.add(new JLabel("Funcionalidad no implementada"));
        return panel;
    }

    public class FabricaPresupuesto {
        public static GenerarPresupuestoPanel crearPanel() {
            GenerarPresupuestoPanel panel = new GenerarPresupuestoPanel();
            GenerarPresupuestoControlador controlador = new GenerarPresupuestoControlador(panel);
            panel.setControlador(controlador);
            return panel;
        }
    }

}