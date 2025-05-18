package FuncionesPresupuesto;

import Modelos.Presupuesto;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GenerarPresupuesto extends JPanel {

    private final ArrayList<Presupuesto> productos = new ArrayList<>();
    private final JPanel panelLista = new JPanel(new GridLayout(0, 1));
    private final JButton btnAgregar = new JButton("Agregar Producto");
    private final JButton btnGenerar = new JButton("Generar PDF");

    public GenerarPresupuesto() {
        setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnGenerar);

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setPreferredSize(new Dimension(500, 300));
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarProductoDesdeFormulario());
        btnGenerar.addActionListener(e -> generarPDF());
    }

    private void agregarProductoDesdeFormulario() {
        JTextField campoID = new JTextField();
        JTextField campoNombre = new JTextField();
        JTextField campoCantidad = new JTextField();
        JTextField campoPrecio = new JTextField();

        JPanel formulario = new JPanel(new GridLayout(0, 2));
        formulario.add(new JLabel("ID:"));
        formulario.add(campoID);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Cantidad:"));
        formulario.add(campoCantidad);
        formulario.add(new JLabel("Precio unitario:"));
        formulario.add(campoPrecio);

        int result = JOptionPane.showConfirmDialog(this, formulario, "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Presupuesto p = new Presupuesto(
                        Integer.parseInt(campoID.getText()),
                        campoNombre.getText(),
                        Integer.parseInt(campoCantidad.getText()),
                        Double.parseDouble(campoPrecio.getText())
                );
                productos.add(p);
                panelLista.add(new JLabel(p.nombre + " x" + p.cantidad + " (" + p.precio + " €/u)"));
                panelLista.revalidate();
                panelLista.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Campos numéricos inválidos.");
            }
        }
    }

    private void generarPDF() {
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos en el presupuesto.");
            return;
        }

        try {
            // Crear nombre único con fecha y hora
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);
            String nombreArchivo = "presupuesto_" + timestamp + ".pdf";

            // Ruta de destino
            String destino = System.getProperty("user.home") + "/Downloads/" + nombreArchivo;

            Document documento = new Document(PageSize.A4, 50, 50, 50, 50); // márgenes básicos
            PdfWriter.getInstance(documento, new FileOutputStream(destino));
            documento.open();

            documento.add(new Paragraph("Presupuesto de Venta", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD)));
            documento.add(new Paragraph(" ")); // Espacio

            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{2, 4, 2, 2});

            for (String col : new String[]{"ID", "Producto", "Cantidad", "Precio"}) {
                PdfPCell celda = new PdfPCell(new Phrase(col, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
            }

            double total = 0;
            for (Presupuesto p : productos) {
                tabla.addCell(String.valueOf(p.id));
                tabla.addCell(p.nombre);
                tabla.addCell(String.valueOf(p.cantidad));
                tabla.addCell(String.format("%.2f €", p.precio));
                total += p.cantidad * p.precio;
            }

            documento.add(tabla);
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph("Total: " + String.format("%.2f", total) + " €",
                    new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

            documento.close();
            JOptionPane.showMessageDialog(this, "Presupuesto generado en: " + destino);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage());
        }

    }
}
