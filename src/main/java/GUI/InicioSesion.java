package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Datos.ConexionBD;
import Modelos.Sesion;

public class InicioSesion extends JPanel {
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JButton btnEntrar;
    private Vprin ventana;

    public InicioSesion(Vprin ventana) {
        this.ventana = ventana;
        configurarPanel();
    }

    private void configurarPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel lblCorreo = new JLabel("Correo electrónico:");
        txtCorreo = new JTextField(20);

        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(20);

        btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(this::verificarCredenciales);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(lblTitulo, gbc);

        gbc.gridy++;
        add(lblCorreo, gbc);
        gbc.gridy++;
        add(txtCorreo, gbc);

        gbc.gridy++;
        add(lblPassword, gbc);
        gbc.gridy++;
        add(txtPassword, gbc);

        gbc.gridy++;
        add(btnEntrar, gbc);
    }

    private void verificarCredenciales(ActionEvent e) {
        String correo = txtCorreo.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (correo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes llenar todos los campos.");
            return;
        }

        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT * FROM usuarios WHERE correo_electronico = ? AND contraseña = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            stmt.setString(2, pass); // En producción deberías usar hashes de contraseña

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                boolean esAdmin = rs.getBoolean("admin");
                Sesion.iniciarSesion(correo, esAdmin);  // <--- Aquí guardas la sesión actual
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.");
                ventana.ponPanel(new OpcionesPrincipales(ventana));
            }else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.");
        }
    }
}
