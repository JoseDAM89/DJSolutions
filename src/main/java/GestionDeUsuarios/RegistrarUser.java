package GestionDeUsuarios;

import Datos.ConexionBD;
import Modelos.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegistrarUser extends JPanel {

    public RegistrarUser() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Registro de nuevo usuario", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos del usuario"));

        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtCorreo = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JCheckBox chkAdmin = new JCheckBox("¿Es administrador?");

        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Apellido:"));
        panelCampos.add(txtApellido);
        panelCampos.add(new JLabel("Correo electrónico:"));
        panelCampos.add(txtCorreo);
        panelCampos.add(new JLabel("Contraseña:"));
        panelCampos.add(txtPassword);
        panelCampos.add(new JLabel());
        panelCampos.add(chkAdmin);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnRegistrar = new JButton("Registrar");
        panelBotones.add(btnRegistrar);
        add(panelBotones, BorderLayout.SOUTH);

        // Acción del botón
        btnRegistrar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String correo = txtCorreo.getText().trim();
            String password = new String(txtPassword.getPassword());
            boolean esAdmin = chkAdmin.isSelected();

            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Rellena todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (correoYaExiste(correo)) {
                JOptionPane.showMessageDialog(this, "Ese correo ya está registrado.", "Correo duplicado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            sincronizarSecuenciaId();
            Usuario nuevoUsuario = new Usuario(nombre, apellido, correo, password, esAdmin);

            if (registrar(nuevoUsuario)) {
                JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.");
                // Aquí podrías limpiar los campos si lo deseas
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario.");
            }
        });
    }

    public static boolean registrar(Usuario usuario) {
        String hashed = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "INSERT INTO usuarios (nombre, apellido, correo_electronico, contraseña, admin) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getCorreoElectronico());
            stmt.setString(4, hashed);
            stmt.setBoolean(5, usuario.isAdmin());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean correoYaExiste(String correo) {
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT 1 FROM usuarios WHERE correo_electronico = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static void sincronizarSecuenciaId() {
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT setval('usuarios_id_seq', (SELECT COALESCE(MAX(id), 1) FROM usuarios))";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
