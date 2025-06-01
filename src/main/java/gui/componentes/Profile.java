package gui.componentes;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel; // Asegúrate de importar JLabel
import javax.swing.JPanel; // Asegúrate de importar JPanel
import java.awt.Font; // Importar Font
import java.awt.Color; // Importar Color

// Si quieres añadir la funcionalidad de volverAHome al hacer clic en el logo
// necesitarías una forma de que jLabel1 sea accesible o pasar un listener al Profile.
// Por ahora, lo dejaremos como en el original para replicar el layout.
// Si necesitas el clic, lo integramos de otra forma.

public class Profile extends javax.swing.JPanel {

    private JLabel jLabel1; // Declarar aquí para que sea un campo de la clase

    public Profile() {
        initComponents();
        setOpaque(false);

    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(30, 30, 30));

// Cargar la imagen original
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/JSWINGICONS/icon/Logo-removebg-preview.png"));

// Escalar la imagen (48x48 en este ejemplo)
        Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);

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