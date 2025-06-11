package utilidades;

import modelos.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.awt.Desktop;
import java.io.*;
import java.time.format.DateTimeFormatter;

public class GeneradorDocumentoPDF {

    private static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 12);
    private static final Font FONT_NEGRITA = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font FONT_TITULO = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);

    // ====================== FACTURA ==========================

    public static void generarYMostrarFactura(Factura factura, Cliente cliente) {
        mostrarPDF(crearPDF(factura, cliente, "Factura"));
    }

    public static File generarFactura(Factura factura, Cliente cliente) {
        return crearPDF(factura, cliente, "Factura");
    }

    // ====================== PRESUPUESTO ==========================

    public static void generarYMostrarPresupuesto(Presupuesto presupuesto, Cliente cliente) {
        mostrarPDF(crearPDF(presupuesto, cliente, "Presupuesto"));
    }

    public static File generarPresupuesto(Presupuesto presupuesto, Cliente cliente) {
        return crearPDF(presupuesto, cliente, "Presupuesto");
    }

    // ====================== CREAR PDF GENÉRICO ==========================

    private static File crearPDF(Object documento, Cliente cliente, String tipo) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4, 50, 50, 60, 40);
            PdfWriter writer = PdfWriter.getInstance(doc, baos);
            doc.open();

            // Datos empresa
            agregarCabeceraEmpresa(doc, tipo);


            // Datos cliente
            agregarDatosCliente(doc, cliente, documento, tipo);

            // Tabla productos
            agregarTablaProductos(doc, documento, tipo);

            // Totales
            agregarTotales(doc, documento);

            // Pie
            doc.add(new Paragraph(" "));
            String pieTexto = tipo.equals("Factura") ?
                    "Gracias por su confianza." :
                    "Este presupuesto es válido durante 30 días.";
            Paragraph pie = new Paragraph(pieTexto, FONT_NORMAL);
            pie.setAlignment(Element.ALIGN_CENTER);
            doc.add(pie);

            doc.close();

            File temp = File.createTempFile(tipo.toLowerCase() + "_", ".pdf");
            temp.deleteOnExit();
            try (FileOutputStream fos = new FileOutputStream(temp)) {
                baos.writeTo(fos);
            }
            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ====================== UTILIDADES INTERNAS ==========================

    private static void mostrarPDF(File archivo) {
        try {
            if (archivo != null && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(archivo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void agregarCabeceraEmpresa(Document doc, String tipo) throws Exception {
        PdfPTable tablaCabecera = new PdfPTable(2);
        tablaCabecera.setWidthPercentage(100);
        tablaCabecera.setWidths(new float[]{75, 25}); // Más espacio para texto, menos para logo




        // 👉 Parte izquierda: datos sin recuadro
        PdfPTable subtabla = new PdfPTable(1);
        subtabla.setWidthPercentage(100);
        subtabla.addCell(getCeldaSinBorde("DJSOLUTIONS S.A.", FONT_NEGRITA));
        subtabla.addCell(getCeldaSinBorde("Calle Ejemplo 123", FONT_NORMAL));
        subtabla.addCell(getCeldaSinBorde("MADRID, España", FONT_NORMAL));
        subtabla.addCell(getCeldaSinBorde("djsolutionssa@gmail.com", FONT_NORMAL));
        subtabla.addCell(getCeldaSinBorde("+34 000 000 000", FONT_NORMAL));
        if (tipo.equals("Factura")) {
            subtabla.addCell(getCeldaSinBorde("CIF: B12345678", FONT_NORMAL));
        }

        PdfPCell celdaTexto = new PdfPCell(subtabla);
        celdaTexto.setBorder(Rectangle.NO_BORDER);
        tablaCabecera.addCell(celdaTexto);

        // 👉 Parte derecha: imagen/logo alineado arriba a la derecha
        try {
            Image logo = Image.getInstance("src/resources/logo.jpg");
            logo.scaleToFit(175, 175);
            logo.setAlignment(Image.ALIGN_RIGHT);

            PdfPCell celdaLogo = new PdfPCell();
            celdaLogo.addElement(logo);
            celdaLogo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaLogo.setVerticalAlignment(Element.ALIGN_TOP);
            celdaLogo.setBorder(Rectangle.NO_BORDER);
            tablaCabecera.addCell(celdaLogo);
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar el logo: " + e.getMessage());
            PdfPCell vacia = new PdfPCell(new Phrase(""));
            vacia.setBorder(Rectangle.NO_BORDER);
            tablaCabecera.addCell(vacia);
        }

        doc.add(tablaCabecera);
        doc.add(new Paragraph(" "));

        // 👉 Título centrado (Factura / Presupuesto)
        Paragraph titulo = new Paragraph(tipo, FONT_TITULO);
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);
        doc.add(new Paragraph(" "));
    }

    private static PdfPCell getCeldaSinBorde(String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBorder(Rectangle.NO_BORDER);
        return celda;
    }





    private static void agregarDatosCliente(Document doc, Cliente cliente, Object docObj, String tipo) throws DocumentException {
        PdfPTable datos = new PdfPTable(2);
        datos.setWidthPercentage(60);
        datos.setHorizontalAlignment(Element.ALIGN_LEFT);
        datos.addCell(getCelda("Para:", FONT_NEGRITA));
        datos.addCell(getCelda(cliente.getCampoNombre(), FONT_NORMAL));
        datos.addCell(getCelda("CIF:", FONT_NEGRITA));
        datos.addCell(getCelda(cliente.getcif(), FONT_NORMAL));
        datos.addCell(getCelda("Fecha:", FONT_NEGRITA));

        String fecha = (tipo.equals("Factura")) ?
                ((Factura) docObj).getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) :
                ((Presupuesto) docObj).getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        datos.addCell(getCelda(fecha, FONT_NORMAL));
        datos.addCell(getCelda(tipo + " ID:", FONT_NEGRITA));
        String id = (tipo.equals("Factura")) ?
                String.valueOf(((Factura) docObj).getIdFactura()) :
                String.valueOf(((Presupuesto) docObj).getId());
        datos.addCell(getCelda(id, FONT_NORMAL));
        doc.add(datos);
        doc.add(new Paragraph(" "));
    }

    private static void agregarTablaProductos(Document doc, Object docObj, String tipo) throws DocumentException {
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new int[]{1, 3, 2, 2, 2});
        tabla.addCell(getCeldaCabecera("ID"));
        tabla.addCell(getCeldaCabecera("Descripción"));
        tabla.addCell(getCeldaCabecera("Cantidad"));
        tabla.addCell(getCeldaCabecera("Precio Unitario"));
        tabla.addCell(getCeldaCabecera("Total"));

        if (tipo.equals("Factura")) {
            for (LineaFactura l : ((Factura) docObj).getLineas()) {
                tabla.addCell(getCelda(String.valueOf(l.getIdProducto()), FONT_NORMAL));
                tabla.addCell(getCelda(l.getNombreProducto(), FONT_NORMAL));
                tabla.addCell(getCelda(String.valueOf(l.getCantidad()), FONT_NORMAL));
                tabla.addCell(getCelda(String.format("%.2f €", l.getPrecioUnitario()), FONT_NORMAL));
                tabla.addCell(getCelda(String.format("%.2f €", l.getSubtotal()), FONT_NORMAL));
            }
        } else {
            for (LineaPresupuesto l : ((Presupuesto) docObj).getLineas()) {
                double subtotal = l.getCantidad() * l.getPrecioUnitario();
                tabla.addCell(getCelda(String.valueOf(l.getIdProducto()), FONT_NORMAL));
                tabla.addCell(getCelda(l.getNombreProducto(), FONT_NORMAL));
                tabla.addCell(getCelda(String.valueOf(l.getCantidad()), FONT_NORMAL));
                tabla.addCell(getCelda(String.format("%.2f €", l.getPrecioUnitario()), FONT_NORMAL));
                tabla.addCell(getCelda(String.format("%.2f €", subtotal), FONT_NORMAL));
            }
        }

        doc.add(tabla);
        doc.add(new Paragraph(" "));
    }

    private static void agregarTotales(Document doc, Object docObj) throws DocumentException {
        double subtotal = 0;
        if (docObj instanceof Factura) {
            subtotal = ((Factura) docObj).getLineas().stream().mapToDouble(LineaFactura::getSubtotal).sum();
        } else if (docObj instanceof Presupuesto) {
            subtotal = ((Presupuesto) docObj).getLineas().stream()
                    .mapToDouble(l -> l.getCantidad() * l.getPrecioUnitario())
                    .sum();
        }
        double iva = subtotal * 0.21;
        double total = subtotal + iva;

        PdfPTable totales = new PdfPTable(2);
        totales.setWidthPercentage(40);
        totales.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totales.addCell(getCelda("Subtotal", FONT_NEGRITA));
        totales.addCell(getCelda(String.format("%.2f €", subtotal), FONT_NORMAL));
        totales.addCell(getCelda("IVA (21%)", FONT_NEGRITA));
        totales.addCell(getCelda(String.format("%.2f €", iva), FONT_NORMAL));
        totales.addCell(getCelda("TOTAL", FONT_NEGRITA));
        totales.addCell(getCelda(String.format("%.2f €", total), FONT_NORMAL));
        doc.add(totales);
    }

    // Celdas reutilizables
    private static PdfPCell getCelda(String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBorder(Rectangle.BOX);
        return celda;
    }

    private static PdfPCell getCeldaCabecera(String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_NEGRITA));
        celda.setBackgroundColor(new BaseColor(230, 230, 230));
        return celda;
    }
}