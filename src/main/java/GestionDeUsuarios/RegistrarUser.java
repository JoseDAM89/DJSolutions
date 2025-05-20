package GestionDeUsuarios;

import Datos.ConexionBD;
import GUI.OpcionesPrincipales;
import GUI.Vprin;
import Modelos.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegistrarUser extends JPanel {

    public RegistrarUser(Vprin ventana) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // padding

        // Título superior
        JLabel titulo = new JLabel("Registro de nuevo usuario", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // Panel central con los campos
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(5, 2, 10, 10)); // 5 filas
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos del usuario"));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField();

        JLabel lblCorreo = new JLabel("Correo electrónico:");
        JTextField txtCorreo = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField();

        JCheckBox chkAdmin = new JCheckBox("¿Es administrador?");

        panelCampos.add(lblNombre);
        panelCampos.add(txtNombre);

        panelCampos.add(lblApellido);
        panelCampos.add(txtApellido);

        panelCampos.add(lblCorreo);
        panelCampos.add(txtCorreo);

        panelCampos.add(lblPassword);
        panelCampos.add(txtPassword);

        panelCampos.add(new JLabel()); // vacío
        panelCampos.add(chkAdmin);

        add(panelCampos, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnVolver = new JButton("Volver");
        panelBotones.add(btnVolver);
        panelBotones.add(btnRegistrar);

        add(panelBotones, BorderLayout.SOUTH);

        // Lógica de botones
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
                ventana.ponPanel(new OpcionesPrincipales(ventana));
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario.");
            }
        });

        btnVolver.addActionListener(e -> ventana.ponPanel(new OpcionesPrincipales(ventana)));
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
            return true; // asumimos que sí existe si hay error
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
