package GUI;

import javax.swing.*;
import java.awt.*;

public class GestionUsuarios extends JPanel {

    private JButton btnIniciarSesion;

    private Vprin ventana; // Para poder hacer ponPanel()

    public GestionUsuarios(Vprin ventana) {
        this.ventana = ventana;
        inicializarComponentes();
        configurarPanel();
        agregarEventos();
    }

    private void inicializarComponentes() {
        btnIniciarSesion = crearBoton("Iniciar Sesión");
    }

    private void configurarPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255)); // Color de fondo suave

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        gbc.gridy = fila++;
        add(btnIniciarSesion, gbc);
    }

    private void agregarEventos() {
        btnIniciarSesion.addActionListener(e -> iniciarSesion());
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(200, 50));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(30, 144, 255));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void iniciarSesion() {
        InicioSesion panelInicio = new InicioSesion(ventana);
        ventana.ponPanel(panelInicio);
    }

}
