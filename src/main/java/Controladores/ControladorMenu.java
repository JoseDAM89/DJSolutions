package Controladores;

import FuncionesCliente.AltaCliente;
import FuncionesCliente.ListarClientes;
import FuncionesInventario.AltaProducto;
import FuncionesInventario.ListarProductos;
import GestionDeUsuarios.RegistrarUser;
import GestionDeUsuarios.VerUsuarios;
import gui.Vprin;

import javax.swing.*;

public class ControladorMenu {

    private final Vprin ventana;

    public ControladorMenu(Vprin ventana) {
        this.ventana = ventana;
    }

    public void menuSeleccionado(int menuIndex, int subMenuIndex) {
        // Funciones Cliente
        if (menuIndex == 0) {
            switch (subMenuIndex) {
                case 0 -> ventana.ponPanel(new AltaCliente().construirFormulario());
                case 1 -> JOptionPane.showMessageDialog(ventana, "Editar Clientes aún no implementado.");
                case 2 -> new ListarClientes().mostrarVentana();
            }
        }

        // Funciones Inventario
        else if (menuIndex == 1) {
            switch (subMenuIndex) {
                case 0 -> ventana.ponPanel(new AltaProducto().construirFormulario());
                case 1 -> JOptionPane.showMessageDialog(ventana, "Consultar Stock aún no implementado.");
                case 2 -> new ListarProductos().mostrarVentana();
                case 3 -> JOptionPane.showMessageDialog(ventana, "Ver Alerta Stock aún no implementado.");
            }
        }

        // Funciones Presupuesto
        else if (menuIndex == 2) {
            JOptionPane.showMessageDialog(ventana, "⚠️ Funciones de presupuesto aún no integradas.");
        }

        // Gestión de Usuarios
        else if (menuIndex == 3) {
            switch (subMenuIndex) {
                case 0 -> ventana.ponPanel(new RegistrarUser());
                case 1 -> ventana.ponPanel(new VerUsuarios());
            }
        }
    }
}
