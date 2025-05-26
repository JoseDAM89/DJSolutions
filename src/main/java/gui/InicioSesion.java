package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import datos.ConexionBD;
import Modelos.Sesion;

public class InicioSesion extends JPanel {
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JButton btnEntrar;
    private LoginDialog loginDialog;

    public InicioSesion(LoginDialog loginDialog) {
        this.loginDialog = loginDialog;
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

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(lblCorreo, gbc);
        gbc.gridx = 1;
        add(txtCorreo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(lblPassword, gbc);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
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
            stmt.setString(2, pass); // Nota: en producción deberías usar hashes

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                boolean esAdmin = rs.getBoolean("admin");
                Sesion.iniciarSesion(correo, esAdmin);
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.");
                loginDialog.onLoginSuccess(correo, esAdmin);
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.");
        }
    }
}