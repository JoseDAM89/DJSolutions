package gui.componentes;

import gui.VentanaPrincipal;  // IMPORTANTE: para reconocer la clase padre

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Profile extends JPanel {

    public Profile() {
        initComponents();
        setOpaque(false);

        // Listener para que si haces clic en el logo, llame a volverAHome de VentanaPrincipal
        jLabelLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                llamarVolverAHome();
            }
        });
    }

    private void llamarVolverAHome() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof VentanaPrincipal) {
            ((VentanaPrincipal) topFrame).volverAHome();
        } else {
            System.out.println("El JFrame padre no es VentanaPrincipal o no encontrado.");
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Usamos BoxLayout vertical para apilar logo y texto
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Label para el logo
        jLabelLogo = new JLabel();

        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/JSWINGICONS/icon/Logo-removebg-preview.png"));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(120, -1, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
        jLabelLogo.setIcon(iconoEscalado);

        // Centrar logo horizontalmente
        jLabelLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label para el texto de la empresa
        jLabelTexto = new JLabel("DJSOLUTIONS");
        jLabelTexto.setFont(new Font("SansSerif", Font.BOLD, 20));
        jLabelTexto.setForeground(new Color(30, 30, 30));
        jLabelTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Añadir un pequeño espacio entre logo y texto
        add(Box.createVerticalStrut(10));
        add(jLabelLogo);
        add(Box.createVerticalStrut(1));
        add(jLabelTexto);
        add(Box.createVerticalGlue()); // para que se quede arriba y empuje hacia abajo
    }

    // Variables declaration
    private JLabel jLabelLogo;
    private JLabel jLabelTexto;
}
