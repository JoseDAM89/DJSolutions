package FuncionesPrincipalesDePresupuestos;

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

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mostrarPreview(previewWindow, archivo, e.getLocationOnScreen());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                previewWindow.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().open(archivo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(item, "No se pudo abrir el archivo.");
                }
            }
        });

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
