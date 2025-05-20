package FuncionesPresupuesto;

import Modelos.Presupuesto;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GenerarPresupuesto extends JPanel {

    private static final String NOMBRE_EMPRESA = "DJSolutions S.A.";
    private static final String EMAIL_EMPRESA = "contacto@tuempresa.com";
    private static final String TELEFONO_EMPRESA = "123 456 789";

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
            // Ruta
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);
            String nombreArchivo = "Presupuesto_" + timestamp + ".pdf";
            String destino = System.getProperty("user.home") + "/Downloads/" + nombreArchivo;

            // Documento
            Document doc = new Document(PageSize.A4, 50, 50, 60, 50);
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(destino));
            writer.setPageEvent(new HeaderFooter(NOMBRE_EMPRESA, EMAIL_EMPRESA, TELEFONO_EMPRESA));
            doc.open();

            // LOGO
            try {
                Image logo = Image.getInstance("src/resources/logo.png");
                logo.scaleToFit(150, 90);
                logo.setAlignment(Image.ALIGN_RIGHT);
                doc.add(logo);
            } catch (Exception e) {
                System.out.println("No se pudo cargar el logo.");
            }

            // CABECERA
            Paragraph titulo = new Paragraph("Presupuesto Comercial", new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD));
            titulo.setAlignment(Element.ALIGN_LEFT);
            doc.add(titulo);
            doc.add(new Paragraph("Fecha: " + LocalDate.now()));
            doc.add(new Paragraph("Cliente: Empresa Cliente XYZ S.L."));
            doc.add(new Paragraph("Generado por: " + NOMBRE_EMPRESA));
            doc.add(Chunk.NEWLINE);

            // TABLA
            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{1.2f, 3, 1.2f, 1.5f, 1.5f});

            String[] columnas = {"ID", "Producto", "Cantidad", "Precio Unitario", "Subtotal"};
            for (String col : columnas) {
                PdfPCell celda = new PdfPCell(new Phrase(col, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(8);
                tabla.addCell(celda);
            }

            double total = 0;
            for (Presupuesto p : productos) {
                double subtotal = p.cantidad * p.precio;
                total += subtotal;

                tabla.addCell(String.valueOf(p.id));
                tabla.addCell(p.nombre);
                tabla.addCell(String.valueOf(p.cantidad));
                tabla.addCell(String.format("%.2f €", p.precio));
                tabla.addCell(String.format("%.2f €", subtotal));
            }

            doc.add(tabla);
            doc.add(Chunk.NEWLINE);

            // TOTALES
            Paragraph totalParrafo = new Paragraph("Total sin IVA: " + String.format("%.2f", total) + " €", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            Paragraph iva = new Paragraph("IVA (21%): " + String.format("%.2f", total * 0.21) + " €", new Font(Font.FontFamily.HELVETICA, 12));
            Paragraph totalConIVA = new Paragraph("Total con IVA: " + String.format("%.2f", total * 1.21) + " €", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLUE));

            doc.add(totalParrafo);
            doc.add(iva);
            doc.add(totalConIVA);

            // PIE
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("Este presupuesto es válido por 15 días.", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC)));
            doc.add(new Paragraph("Gracias por confiar en nosotros."));

            doc.close();

            JOptionPane.showMessageDialog(this, "Presupuesto generado en:\n" + destino);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage());
        }
    }

}



class HeaderFooter extends PdfPageEventHelper {
    private final String empresa;
    private final String email;
    private final String telefono;
    private final Font font = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);

    public HeaderFooter(String empresa, String email, String telefono) {
        this.empresa = empresa;
        this.email = email;
        this.telefono = telefono;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase(empresa + " - " + email + " | Tel. " + telefono, font);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() + document.left()) / 2,
                document.bottom() - 10, 0);
    }
}

