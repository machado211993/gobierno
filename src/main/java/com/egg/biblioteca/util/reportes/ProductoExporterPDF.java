package com.egg.biblioteca.util.reportes;

import com.egg.biblioteca.entidades.Producto;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

public class ProductoExporterPDF {

    private final List<Producto> listarProductos;

    public ProductoExporterPDF(List<Producto> listarProductos) { //inicializa el listado
        super();
        this.listarProductos = listarProductos;

    }

    private void escribirCabeceraDeLaTabla(PdfPTable tabla) { //crea las cabeceras de la tabla
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(Color.RED);
        celda.setPadding(5);

        Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.WHITE);

        celda.setPhrase(new Phrase("CODIGO", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("NOMBRE", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("PRECIO", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("ALTA", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("RUBRO", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("PROVEEDOR", fuente));
        tabla.addCell(celda);

    }

    private void escribirDatosDeLaTabla(PdfPTable tabla) { //trae los datos de la base de datos 
        for (Producto producto : listarProductos) {

            tabla.addCell(producto.getCodigo());
            tabla.addCell(producto.getNombre());

            tabla.addCell(String.valueOf(producto.getPrecio()));

            tabla.addCell(String.valueOf(producto.getAlta()));

            tabla.addCell(producto.getRubro().getNombre()); //como es una relacion se busca dentro de rubro

            tabla.addCell(producto.getProveedor().getNombre());//como es una relacion se busca dentro de proveedor

        }
    }

    /**
     *
     * @param response
     * @throws java.io.IOException
     */
    public void exportar(HttpServletResponse response) throws IOException {
        try (Document documento = new Document(PageSize.A4)) {
            PdfWriter.getInstance(documento, response.getOutputStream());
            documento.open();

            Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            fuente.setColor(Color.BLUE);
            fuente.setSize(18);

            Paragraph titulo = new Paragraph("Lista de Productos", fuente);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            documento.add(titulo);

            //FORMATO DE LA TABLA 
            PdfPTable tabla = new PdfPTable(6); //6 COLUMNAS
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(15);
            tabla.setWidths(new float[]{4f, 2.3f, 2.3f, 4f, 2.9f, 3.5f});
            tabla.setWidthPercentage(110);

            //llamar los metodos
            escribirCabeceraDeLaTabla(tabla);
            escribirDatosDeLaTabla(tabla);
            documento.add(tabla);
        }

    }

}
