package GUI;

import FuncionesCliente.*;

import javax.swing.*;
import java.awt.*;

public class GestionClientes extends JPanel {

    private JButton btnAltaCliente;
    private JButton btnListadoClientes;
    private JButton btnAtras;
    private Vprin ventana;

    public GestionClientes(Vprin ventana) {
        this.ventana = ventana;
        inicializarComponentes();
        configurarPanel();
        agregarEventos();
    }

    private void inicializarComponentes() {
        btnAltaCliente = crearBoton("Alta Cliente");
        btnListadoClientes = crearBoton("Listado Clientes");
        btnAtras = crearBoton("Atras");
    }

    private void configurarPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(255, 250, 250)); // Color claro para un estilo limpio

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        gbc.gridy = fila++;
        add(btnAltaCliente, gbc);

        gbc.gridy = fila++;
        add(btnListadoClientes, gbc);

        gbc.gridy = fila++;
        add(btnAtras, gbc);
    }

    private void agregarEventos() {

        /* cambiando el panel
       btnAltaCliente.addActionListener(e -> {
            AltaCliente altaCliente = new AltaCliente();
            ventana.ponPanel(new AltaCliente());
        });
        */
        btnAltaCliente.addActionListener(e -> {
            JFrame ventanaAlta = new JFrame("Alta Cliente");
            ventanaAlta.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventanaAlta.setSize(1000, 500);
            ventanaAlta.setLocationRelativeTo(null);

            // Creamos la instancia lógica de AltaCliente
            AltaCliente altaCliente = new AltaCliente();

            // Obtenemos el formulario (que es un JPanel)
            JPanel formulario = altaCliente.construirFormulario();

            // Lo insertamos en la ventana
            ventanaAlta.setContentPane(formulario);
            ventanaAlta.setVisible(true);
        });


        btnListadoClientes.addActionListener(e -> {
            ListarClientes listar = new ListarClientes();
            listar.mostrarVentana();
        });


        btnAtras.addActionListener(e -> {
            GestionInventario gestionInventario = new GestionInventario(ventana);
            ventana.ponPanel(new OpcionesPrincipales(ventana));
        });
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(250, 50));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(60, 120, 180));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
