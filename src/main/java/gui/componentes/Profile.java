package gui.componentes;

import gui.VentanaPrincipal;  // IMPORTANTE: para reconocer la clase padre

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Profile extends JPanel {

    private JLabel jLabel1;

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
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof VentanaPrincipal) {
            ((VentanaPrincipal) topFrame).volverAHome();
        } else {
            System.out.println("El JFrame padre no es VentanaPrincipal o no encontrado.");
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new JLabel();

        jLabel1.setFont(new Font("sansserif", 1, 24));
        jLabel1.setForeground(new Color(30, 30, 30));

        // Cargar la imagen original
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/JSWINGICONS/icon/Logo-removebg-preview.png"));

        // Escalar la imagen (40x40 en este ejemplo)
        Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        // Crear un nuevo ImageIcon con la imagen escalada
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Asignar el icono escalado al JLabel
        jLabel1.setIcon(scaledIcon);

        jLabel1.setText("DJSOLUTIONS");

        // Layout (sin cambios)
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }
}
