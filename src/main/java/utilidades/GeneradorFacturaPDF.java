package utilidades;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import modelos.Factura;
import modelos.LineaFactura;

import java.io.FileOutputStream;

public class GeneradorFacturaPDF {

    public static void generar(Factura factura, String nombreArchivo) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            document.open();

            document.add(new Paragraph("Factura #" + factura.getIdFactura()));
            document.add(new Paragraph("Cliente ID: " + factura.getIdCliente()));
            document.add(new Paragraph("Fecha: " + factura.getFecha()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.addCell("Producto");
            table.addCell("Cantidad");
            table.addCell("Precio Unitario");
            table.addCell("Subtotal");

            double total = 0;
            for (LineaFactura linea : factura.getLineas()) {
                double subtotal = linea.getCantidad() * linea.getPrecioUnitario();
                total += subtotal;

                table.addCell(linea.getDescripcion());
                table.addCell(String.valueOf(linea.getCantidad()));
                table.addCell(String.format("%.2f", linea.getPrecioUnitario()));
                table.addCell(String.format("%.2f", subtotal));
            }

            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total: " + String.format("%.2f €", total)));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
