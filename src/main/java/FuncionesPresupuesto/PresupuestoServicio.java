package FuncionesPresupuesto;

import Modelos.Cliente;
import Modelos.Presupuesto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PresupuestoServicio {

    private static final String NOMBRE_EMPRESA = "DJSOLUTIONS S.A.";
    private static final String EMAIL_EMPRESA = "djsolutionssa@gmail.com";
    private static final String TELEFONO_EMPRESA = "+34 000 000 000";
    private static final String CALLE = "Calle Ejemplo 123";
    private static final String CIUDAD = "MADRID, España";

    public void generarPDF(List<Presupuesto> productos, Cliente cliente) throws Exception {
        if (productos == null || productos.isEmpty()) {
            throw new Exception("No hay productos en el presupuesto.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String nombreArchivo = "Presupuesto_" + timestamp + ".pdf";
        String destino = System.getProperty("user.home") + "/Downloads/" + nombreArchivo;

        Document doc = new Document(PageSize.A4, 50, 50, 70, 50);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(destino));
        writer.setPageEvent(new HeaderFooter(NOMBRE_EMPRESA, EMAIL_EMPRESA, TELEFONO_EMPRESA));
        doc.open();

        // Cabecera con logo y datos de empresa
        PdfPTable cabecera = new PdfPTable(2);
        cabecera.setWidths(new float[]{2f, 1f});
        cabecera.setWidthPercentage(100);

        PdfPCell datosEmpresa = new PdfPCell();
        datosEmpresa.setBorder(Rectangle.NO_BORDER);
        datosEmpresa.addElement(new Paragraph(NOMBRE_EMPRESA, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
        datosEmpresa.addElement(new Paragraph(CALLE, new Font(Font.FontFamily.HELVETICA, 10)));
        datosEmpresa.addElement(new Paragraph(CIUDAD, new Font(Font.FontFamily.HELVETICA, 10)));
        datosEmpresa.addElement(new Paragraph(EMAIL_EMPRESA, new Font(Font.FontFamily.HELVETICA, 10)));
        datosEmpresa.addElement(new Paragraph(TELEFONO_EMPRESA, new Font(Font.FontFamily.HELVETICA, 10)));
        cabecera.addCell(datosEmpresa);

        try {
            Image logo = Image.getInstance("src/main/resources/JSWINGICONS/icon/Logo.png");
            logo.scaleToFit(120, 60);
            PdfPCell logoCell = new PdfPCell(logo, false);
            logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            logoCell.setVerticalAlignment(Element.ALIGN_TOP);
            logoCell.setBorder(Rectangle.NO_BORDER);
            cabecera.addCell(logoCell);
        } catch (Exception e) {
            PdfPCell vacia = new PdfPCell(new Phrase(""));
            vacia.setBorder(Rectangle.NO_BORDER);
            cabecera.addCell(vacia);
        }

        doc.add(cabecera);
        doc.add(Chunk.NEWLINE);

        // Título
        Paragraph titulo = new Paragraph("Presupuesto", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);
        doc.add(Chunk.NEWLINE);

        // Datos del cliente
        PdfPTable datosCliente = new PdfPTable(2);
        datosCliente.setWidths(new float[]{1f, 2f});
        datosCliente.setWidthPercentage(80);
        datosCliente.setHorizontalAlignment(Element.ALIGN_LEFT);

        datosCliente.addCell(getCell("Para:", Font.BOLD));
        datosCliente.addCell(getCell(cliente.getCampoNombre(), Font.NORMAL));
        datosCliente.addCell(getCell("Dirección:", Font.BOLD));
        datosCliente.addCell(getCell(cliente.getCampoDireccion(), Font.NORMAL));
        datosCliente.addCell(getCell("Fecha:", Font.BOLD));
        datosCliente.addCell(getCell(LocalDate.now().toString(), Font.NORMAL));
        datosCliente.addCell(getCell("Presupuesto ID:", Font.BOLD));
        datosCliente.addCell(getCell(timestamp, Font.NORMAL));

        doc.add(datosCliente);
        doc.add(Chunk.NEWLINE);

        // Tabla de productos
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1.5f, 3f, 1f, 1.5f, 1.5f});
        String[] columnas = {"ID", "Descripción", "Cantidad", "Precio Unitario", "Total"};

        for (String col : columnas) {
            PdfPCell celda = new PdfPCell(new Phrase(col, new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(5);
            tabla.addCell(celda);
        }

        double total = 0;
        for (Presupuesto p : productos) {
            double subtotal = p.getCantidad() * p.getPrecio();
            total += subtotal;

            tabla.addCell(getCell(String.valueOf(p.getId()), Font.NORMAL));
            tabla.addCell(getCell(p.getNombre(), Font.NORMAL));
            tabla.addCell(getCell(String.valueOf(p.getCantidad()), Font.NORMAL));
            tabla.addCell(getCell(String.format("%.2f €", p.getPrecio()), Font.NORMAL));
            tabla.addCell(getCell(String.format("%.2f €", subtotal), Font.NORMAL));
        }

        doc.add(tabla);
        doc.add(Chunk.NEWLINE);

        // Total
        PdfPTable totalTabla = new PdfPTable(2);
        totalTabla.setWidths(new float[]{6, 2});
        totalTabla.setWidthPercentage(50);
        totalTabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalTabla.addCell(getCell("TOTAL", Font.BOLD, Element.ALIGN_RIGHT));
        totalTabla.addCell(getCell(String.format("%.2f €", total), Font.BOLD, Element.ALIGN_RIGHT));
        doc.add(totalTabla);

        doc.add(Chunk.NEWLINE);
        doc.add(new Paragraph("Este presupuesto es válido durante 15 días.", new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC)));
        doc.add(new Paragraph("Gracias por su confianza.", new Font(Font.FontFamily.HELVETICA, 10)));

        doc.close();
    }

    private PdfPCell getCell(String texto, int estilo) {
        return getCell(texto, estilo, Element.ALIGN_LEFT);
    }

    private PdfPCell getCell(String texto, int estilo, int alineacion) {
        Font font = new Font(Font.FontFamily.HELVETICA, 11, estilo);
        PdfPCell celda = new PdfPCell(new Phrase(texto, font));
        celda.setHorizontalAlignment(alineacion);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        return celda;
    }

    public static class HeaderFooter extends PdfPageEventHelper {
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
}
