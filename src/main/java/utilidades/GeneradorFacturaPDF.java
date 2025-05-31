package utilidades;

import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import modelos.Cliente;
import modelos.Factura;
import modelos.LineaFactura;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image; // ✅ Este es el Image de iText

import java.awt.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class GeneradorFacturaPDF {

    public static void generarYMostrar(Factura factura, Cliente cliente) {
        try {
            // 📄 Crear documento y flujo
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 50, 50, 60, 40);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            // 🔹 Fuente
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font fontNormal = new Font(Font.FontFamily.HELVETICA, 12);
            Font fontNegrita = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            // 🔹 Cabecera
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new int[]{3, 1});

            // Datos empresa
            PdfPCell empresaCell = new PdfPCell();
            empresaCell.setBorder(Rectangle.NO_BORDER);
            empresaCell.addElement(new Paragraph("DJSOLUTIONS S.A.", fontNegrita));
            empresaCell.addElement(new Paragraph("Calle Ejemplo 123", fontNormal));
            empresaCell.addElement(new Paragraph("MADRID, España", fontNormal));
            empresaCell.addElement(new Paragraph("djsolutionssa@gmail.com", fontNormal));
            empresaCell.addElement(new Paragraph("+34 000 000 000", fontNormal));
            headerTable.addCell(empresaCell);

            // Logo con el imputStream deberia funcionr incluso con .jar
            InputStream is = GeneradorFacturaPDF.class.getClassLoader().getResourceAsStream("Logo.jpg");
            if (is == null) {
                throw new FileNotFoundException("Logo.jpg no encontrado en /resources");
            }
            Image logo = Image.getInstance(is.readAllBytes());
            logo.scaleToFit(160, 160);
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            logoCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(logoCell);

            document.add(headerTable);
            document.add(new Paragraph(" "));

            // 🔹 Título
            Paragraph titulo = new Paragraph("Factura", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));

            // 🔹 Datos cliente
            PdfPTable datosCliente = new PdfPTable(2);
            datosCliente.setWidthPercentage(60);
            datosCliente.setHorizontalAlignment(Element.ALIGN_LEFT);
            datosCliente.addCell(getCelda("Para:", fontNegrita));
            datosCliente.addCell(getCelda(cliente.getCampoNombre(), fontNormal));
            datosCliente.addCell(getCelda("CIF:", fontNegrita));
            datosCliente.addCell(getCelda(cliente.getCampoCIF(), fontNormal));
            datosCliente.addCell(getCelda("Fecha:", fontNegrita));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFormateada = factura.getFecha().format(formatter);
            datosCliente.addCell(getCelda(fechaFormateada, fontNormal));
            datosCliente.addCell(getCelda("Factura ID:", fontNegrita));
            datosCliente.addCell(getCelda("F" + factura.getIdFactura(), fontNormal));
            document.add(datosCliente);
            document.add(new Paragraph(" "));

            // 🔹 Tabla productos
            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new int[]{1, 3, 2, 2, 2});

            tabla.addCell(getCeldaCabecera("ID"));
            tabla.addCell(getCeldaCabecera("Descripción"));
            tabla.addCell(getCeldaCabecera("Cantidad"));
            tabla.addCell(getCeldaCabecera("Precio Unitario"));
            tabla.addCell(getCeldaCabecera("Total"));

            double total = 0;
            for (LineaFactura linea : factura.getLineas()) {
                double subtotal = linea.getCantidad() * linea.getPrecioUnitario();
                total += subtotal;

                tabla.addCell(String.valueOf(linea.getIdProducto()));
                tabla.addCell(linea.getDescripcion());
                tabla.addCell(String.valueOf(linea.getCantidad()));
                tabla.addCell(String.format("%.2f €", linea.getPrecioUnitario()));
                tabla.addCell(String.format("%.2f €", subtotal));
            }

            document.add(tabla);
            document.add(new Paragraph(" "));

            // Calcular IVA y total final
            double iva = total * 0.21;
            double totalConIva = total + iva;

            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(40);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(getCelda("TOTAL (IVA incluido)", fontNegrita));
            totalTable.addCell(getCelda(String.format("%.2f €", totalConIva), fontNormal));
            document.add(totalTable);

            // Añadir anotación informativa
            Paragraph anotacion = new Paragraph("* Total con IVA incluido.", fontNormal);
            anotacion.setAlignment(Element.ALIGN_RIGHT);
            document.add(anotacion);


            document.add(new Paragraph(" "));

            // 🔹 Pie
            Paragraph pie = new Paragraph("Gracias por su confianza.", fontNormal);
            pie.setAlignment(Element.ALIGN_CENTER);
            document.add(pie);

            // Marca de agua (si se quiere)
            PdfContentByte canvas = writer.getDirectContentUnder();
            Font marcaAguaFont = new Font(Font.FontFamily.HELVETICA, 60, Font.BOLD, new BaseColor(220, 220, 220));
            Phrase watermark = new Phrase("DJSOLUTIONS", marcaAguaFont);
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 297.5f, 421, 45);

            document.close();

            // Abrir
            File temp = File.createTempFile("factura_" + factura.getIdFactura(), ".pdf");
            temp.deleteOnExit();
            try (FileOutputStream fos = new FileOutputStream(temp)) {
                baos.writeTo(fos);
            }

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 Celdas reutilizables
    private static PdfPCell getCelda(String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBorder(Rectangle.BOX);
        return celda;
    }

    private static PdfPCell getCeldaCabecera(String texto) {
        Font fontCabecera = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        PdfPCell celda = new PdfPCell(new Phrase(texto, fontCabecera));
        celda.setBackgroundColor(new BaseColor(230, 230, 230));
        return celda;
    }

    public static void generarYMostrar(Factura factura) {
        Cliente cliente = new Cliente("Desconocido", "", "", "", "", "");
        generarYMostrar(factura, cliente);
    }

}
