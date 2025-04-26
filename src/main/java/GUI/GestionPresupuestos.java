package GUI;

import javax.swing.*;
import java.awt.*;

public class GestionPresupuestos extends JPanel {

    private JButton btnGenerarPresupuesto;
    private JButton btnSeleccionarMateriales;
    private JButton btnHistorialPresupuestos;
    private JButton btnEnviarPresupuesto;

    public GestionPresupuestos() {
        inicializarComponentes();
        configurarPanel();
        agregarEventos();
    }

    private void inicializarComponentes() {
        btnGenerarPresupuesto = crearBoton("Generar Presupuesto");
        btnSeleccionarMateriales = crearBoton("Seleccionar Materiales");
        btnHistorialPresupuestos = crearBoton("Historial de Presupuestos");
        btnEnviarPresupuesto = crearBoton("Enviar Presupuesto");
    }

    private void configurarPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245)); // Color de fondo clarito

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        add(btnGenerarPresupuesto, gbc);

        gbc.gridy = 1;
        add(btnSeleccionarMateriales, gbc);

        gbc.gridy = 2;
        add(btnHistorialPresupuestos, gbc);

        gbc.gridy = 3;
        add(btnEnviarPresupuesto, gbc);
    }

    private void agregarEventos() {
        btnGenerarPresupuesto.addActionListener(e -> mostrarMensaje("Generando presupuesto..."));
        btnSeleccionarMateriales.addActionListener(e -> mostrarMensaje("Seleccionando materiales..."));
        btnHistorialPresupuestos.addActionListener(e -> mostrarMensaje("Mostrando historial de presupuestos..."));
        btnEnviarPresupuesto.addActionListener(e -> mostrarMensaje("Enviando presupuesto..."));
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(250, 50));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(70, 130, 180)); // Azul bonito
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
