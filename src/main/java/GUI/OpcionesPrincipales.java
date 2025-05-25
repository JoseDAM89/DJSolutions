package GUI;

import GestionDeUsuarios.RegistrarUser;
import GestionDeUsuarios.VerUsuarios;
import Modelos.Sesion;

import javax.swing.*;
import java.awt.*;

public class OpcionesPrincipales extends JPanel {

    private JButton btnVerUsuarios;
    private JButton btnRegistrarUsuarios;
    private JButton btnGestionInventario;
    private JButton btnGestionPresupuestos;
    private JButton btnGestionClientes;
    private boolean esAdmin;

    private Vprin ventana; // Referencia a la ventana principal para poder usar ponPanel()

    public OpcionesPrincipales(Vprin ventana) {
        this.ventana = ventana;
        this.esAdmin = Sesion.esAdmin();

        inicializarComponentes();
        configurarPanel();
        agregarEventos();

    }

    private void inicializarComponentes() {

        btnVerUsuarios = crearBoton("Ver Usuarios");
        btnRegistrarUsuarios = crearBoton("Registrar Usuario");
        btnGestionInventario = crearBoton("Gestión de Inventario");
        btnGestionPresupuestos = crearBoton("Gestión de Presupuestos");
        btnGestionClientes = crearBoton("Gestión de Clientes");

    }

    private void configurarPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255)); // Color clarito tipo profesional

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int fila = 0;

        if (esAdmin) {

            gbc.gridy = fila++;
            add(btnVerUsuarios, gbc);

            gbc.gridy = fila++;
            add(btnRegistrarUsuarios, gbc);
        }

        gbc.gridy = fila++;
        add(btnGestionInventario, gbc);

        gbc.gridy = fila++;
        add(btnGestionPresupuestos, gbc);

        gbc.gridy = fila++;
        add(btnGestionClientes, gbc);
    }

    private void agregarEventos() {

        btnRegistrarUsuarios.addActionListener(e -> {
            ventana.ponPanel(new RegistrarUser());
        });

        btnVerUsuarios.addActionListener(e -> {
           ventana.ponPanel(new VerUsuarios());
        });



        btnGestionInventario.addActionListener(e -> {
            GestionInventario gestionInventario = new GestionInventario(ventana);
            ventana.ponPanel(gestionInventario);
        });

        btnGestionPresupuestos.addActionListener(e -> {
            GestionPresupuestos gestionPresupuestos = new GestionPresupuestos(ventana);
            ventana.ponPanel(new GestionPresupuestos(ventana));
        });

        btnGestionClientes.addActionListener(e -> {
            GestionClientes gestionClientes = new GestionClientes(ventana);
            ventana.ponPanel(new GestionClientes(ventana));
        });
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(300, 60));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(60, 120, 180));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }
}
