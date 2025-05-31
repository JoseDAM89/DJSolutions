package FuncionesFacturas;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import datos.ClienteDAO;
import datos.FacturaDAO;
import modelos.Cliente;
import modelos.Factura;
import modelos.LineaFactura;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FacturaServicio {

    public static byte[] generarFacturaPDF(int idFactura) {
        FacturaDAO dao = new FacturaDAO();
        Factura factura = dao.obtenerFacturaPorID(idFactura);
        if (factura == null) return null;

        Cliente cliente = new ClienteDAO().obtenerPorID(factura.getIdCliente());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, baos);
            doc.open();

            // Cabecera
            agregarCabecera(doc);

            // Datos cliente y factura
            agregarDatosCliente(doc, cliente, factura);

            // Tabla de productos
            agregarTablaProductos(doc, factura.getLineas());

            // Totales
            agregarTotales(doc, factura.getLineas());

            // Footer
            agregarFooter(doc);

            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    private static void agregarCabecera(Document doc) throws DocumentException {
        Paragraph header = new Paragraph("DJSOLUTIONS S.A.\nCalle Ejemplo 123\nMADRID, España\n" +
                "djsolutionssa@gmail.com\n+34 000 000 000\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        header.setAlignment(Element.ALIGN_LEFT);
        doc.add(header);

        Paragraph titulo = new Paragraph("FACTURA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);
        doc.add(Chunk.NEWLINE);
    }

    private static void agregarDatosCliente(Document doc, Cliente cliente, Factura factura) throws DocumentException {
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);

        tabla.addCell(getCell("Cliente:", PdfPCell.ALIGN_LEFT));
        tabla.addCell(getCell(cliente.getCampoNombre(), PdfPCell.ALIGN_LEFT));
        tabla.addCell(getCell("Fecha:", PdfPCell.ALIGN_LEFT));
        tabla.addCell(getCell(factura.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), PdfPCell.ALIGN_LEFT));
        tabla.addCell(getCell("Factura ID:", PdfPCell.ALIGN_LEFT));
        tabla.addCell(getCell(String.valueOf(factura.getIdFactura()), PdfPCell.ALIGN_LEFT));

        doc.add(tabla);
        doc.add(Chunk.NEWLINE);
    }

    private static void agregarTablaProductos(Document doc, List<LineaFactura> lineas) throws DocumentException {
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1.5f, 3f, 1.5f, 1.5f, 1.5f});

        String[] headers = {"ID", "Descripción", "Cantidad", "Precio Unitario", "Subtotal"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tabla.addCell(cell);
        }

        for (LineaFactura linea : lineas) {
            tabla.addCell(String.valueOf(linea.getIdProducto()));
            tabla.addCell(linea.getDescripcion());
            tabla.addCell(String.valueOf(linea.getCantidad()));
            tabla.addCell(String.format("%.2f €", linea.getPrecioUnitario()));
            tabla.addCell(String.format("%.2f €", linea.getSubtotal()));
        }

        doc.add(tabla);
        doc.add(Chunk.NEWLINE);
    }

    private static void agregarTotales(Document doc, List<LineaFactura> lineas) throws DocumentException {
        double subtotal = lineas.stream().mapToDouble(LineaFactura::getSubtotal).sum();
        double iva = subtotal * 0.21;
        double total = subtotal + iva;

        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(30);
        tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);

        tabla.addCell(getCell("Subtotal:", PdfPCell.ALIGN_RIGHT));
        tabla.addCell(getCell(String.format("%.2f €", subtotal), PdfPCell.ALIGN_RIGHT));
        tabla.addCell(getCell("IVA (21%):", PdfPCell.ALIGN_RIGHT));
        tabla.addCell(getCell(String.format("%.2f €", iva), PdfPCell.ALIGN_RIGHT));
        tabla.addCell(getCell("TOTAL:", PdfPCell.ALIGN_RIGHT));
        tabla.addCell(getCell(String.format("%.2f €", total), PdfPCell.ALIGN_RIGHT));

        doc.add(tabla);
        doc.add(Chunk.NEWLINE);
    }

    private static void agregarFooter(Document doc) throws DocumentException {
        Paragraph p = new Paragraph("Gracias por su confianza.\n\n" +
                "DJSOLUTIONS S.A. - djsolutionssa@gmail.com | Tel. +34 000 000 000",
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9));
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
    }

    private static PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
}
