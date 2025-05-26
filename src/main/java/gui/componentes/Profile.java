package gui.componentes;

import gui.VentanaPrincipal;  // IMPORTANTE: para reconocer la clase padre
import gui.formularios.Form_Home;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Profile extends javax.swing.JPanel {

    public Profile() {
        initComponents();
        setOpaque(false);

        // Listener para que si haces clic en el logo, llame a volverAHome de VentanaPrincipal
        jLabel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                llamarVolverAHome();
            }
        });
    }

    private void llamarVolverAHome() {
        // Buscar el JFrame contenedor
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof VentanaPrincipal) {
            ((VentanaPrincipal) topFrame).volverAHome();
        } else {
            System.out.println("El JFrame padre no es VentanaPrincipal o no encontrado.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        jLabel1 = new JLabel();

        // Fuente y color del texto
        jLabel1.setFont(new Font("SansSerif", Font.BOLD, 20));
        jLabel1.setForeground(new Color(30, 30, 30));

        // Cargar el logo original
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/JSWINGICONS/icon/Logo-removebg-preview.png"));

        // Escalar con buena calidad manteniendo la relación de aspecto
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(80, -1, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

        // Asignar icono y texto
        jLabel1.setIcon(iconoEscalado);
        jLabel1.setText(" DJSolutions");
        jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
        jLabel1.setIconTextGap(10);

        // Añadir etiqueta al panel
        add(jLabel1);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
