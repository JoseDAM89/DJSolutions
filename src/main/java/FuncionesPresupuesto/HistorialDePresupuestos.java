package FuncionesPresupuesto;

import Correo.EnviarCorreo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class HistorialDePresupuestos extends JPanel {

    private File carpetaPresupuestos;

    public HistorialDePresupuestos() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Título
        JLabel titulo = new JLabel("Historial de Presupuestos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(titulo, BorderLayout.NORTH);

        // Ruta donde se guardan los PDFs
        String userHome = System.getProperty("user.home");
        carpetaPresupuestos = new File(userHome + "/Downloads");

        // Panel contenedor
        JPanel panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        // Estilo para la vista previa emergente
        JWindow previewWindow = new JWindow();
        previewWindow.setBackground(new Color(255, 255, 255, 220));

        // Cargar PDFs que empiecen por "presupuesto_" y terminen en ".pdf"
        File[] archivos = carpetaPresupuestos.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".pdf") && name.toLowerCase().startsWith("presupuesto_")
        );

        if (archivos != null && archivos.length > 0) {
            // Ordenar por fecha descendente (más reciente primero)
            Arrays.sort(archivos, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));

            for (File archivo : archivos) {
                JPanel item = crearItemDePresupuesto(archivo, previewWindow);
                panelLista.add(item);
                panelLista.add(Box.createVerticalStrut(10));
            }
        } else {
            JLabel vacio = new JLabel("No hay presupuestos generados aún.");
            vacio.setFont(new Font("SansSerif", Font.ITALIC, 16));
            vacio.setForeground(Color.GRAY);
            vacio.setHorizontalAlignment(SwingConstants.CENTER);
            add(vacio, BorderLayout.CENTER);
        }
    }

    private JPanel crearItemDePresupuesto(File archivo, JWindow previewWindow) {
        JPanel item = new JPanel(new BorderLayout());
        item.setPreferredSize(new Dimension(500, 50));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        item.setBackground(new Color(245, 245, 245));
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        JLabel nombreArchivo = new JLabel(archivo.getName());
        nombreArchivo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel fecha = new JLabel(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(archivo.lastModified()));
        fecha.setFont(new Font("SansSerif", Font.PLAIN, 12));
        fecha.setForeground(Color.GRAY);

        JPanel texto = new JPanel(new BorderLayout());
        texto.setOpaque(false);
        texto.add(nombreArchivo, BorderLayout.NORTH);
        texto.add(fecha, BorderLayout.SOUTH);

        item.add(texto, BorderLayout.CENTER);

        // Panel botones ocultos
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panelBotones.setOpaque(false);
        panelBotones.setVisible(false);  // Oculto por defecto

        JButton btnVer = new JButton("Ver PDF");
        btnVer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnVer.setFocusable(false);
        btnVer.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(archivo);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(item, "No se pudo abrir el archivo.");
            }
        });

        JButton btnEnviar = new JButton("Enviar PDF");
        btnEnviar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnEnviar.setFocusable(false);
        btnEnviar.addActionListener(e -> {
            String correo = JOptionPane.showInputDialog(item, "Introduce el correo destino:");
            if (correo != null && !correo.trim().isEmpty()) {
                try {
                    EnviarCorreo.enviarArchivoPorCorreo(
                            correo,
                            archivo,
                            "Presupuesto generado",
                            "Adjunto le enviamos su presupuesto generado."
                    );
                    JOptionPane.showMessageDialog(item, "Correo enviado correctamente.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(item, "Error enviando el correo: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(item, "Correo no válido.");
            }
        });

        panelBotones.add(btnVer);
        panelBotones.add(btnEnviar);

        item.add(panelBotones, BorderLayout.EAST);

        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panelBotones.setVisible(true);
                mostrarPreview(previewWindow, archivo, e.getLocationOnScreen());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Comprobar si el mouse está dentro del item o del panelBotones
                Point p = e.getLocationOnScreen();
                Rectangle boundsItem = item.getBounds();
                Point locItem = item.getLocationOnScreen();
                Rectangle boundsPanelBotones = panelBotones.getBounds();
                Point locPanelBotones = panelBotones.getLocationOnScreen();

                boundsItem.setLocation(locItem);
                boundsPanelBotones.setLocation(locPanelBotones);

                if (!boundsItem.contains(p) && !boundsPanelBotones.contains(p)) {
                    panelBotones.setVisible(false);
                    previewWindow.setVisible(false);
                }
            }
        };

        item.addMouseListener(mouseListener);
        panelBotones.addMouseListener(mouseListener);

        return item;
    }

    private void mostrarPreview(JWindow previewWindow, File archivo, Point ubicacionPantalla) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel nombre = new JLabel("📄 " + archivo.getName());
        nombre.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel tamano = new JLabel("Tamaño: " + (archivo.length() / 1024) + " KB");
        tamano.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JLabel fecha = new JLabel("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(archivo.lastModified()));
        fecha.setFont(new Font("SansSerif", Font.PLAIN, 12));

        panel.add(nombre);
        panel.add(Box.createVerticalStrut(5));
        panel.add(tamano);
        panel.add(fecha);

        previewWindow.getContentPane().removeAll();
        previewWindow.getContentPane().add(panel);
        previewWindow.pack();
        previewWindow.setLocation(ubicacionPantalla.x + 15, ubicacionPantalla.y);
        previewWindow.setVisible(true);
    }
}
