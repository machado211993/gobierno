package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Oferta;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.OfertaServicio;
import com.egg.biblioteca.util.reportes.OfertaExporterPDF;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/oferta")
public class OfertaControlador {

    @Autowired
    private OfertaServicio ofertaServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) { // metodo registro formulario
        return "oferta_form";

    }

    @PostMapping("/registro") // metodo registrado
    public String registro(@RequestParam(required = false) String nombreOferta, @RequestParam String precio,
            @RequestParam MultipartFile archivo, ModelMap modelo) {
        try {
            ofertaServicio.crearOferta(archivo, nombreOferta, precio);
            modelo.put("exito", "la oferta fue cargada correctamente");

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "oferta_form";
        }
        return "index";

    }

    // @GetMapping("/lista")
    // public String listar(ModelMap modelo) {
    //
    // List<Oferta> ofertas = ofertaServicio.listarOfertas();
    //
    // modelo.addAttribute("ofertas", ofertas);
    //
    // return "oferta_list.html";
    // }
    // funcionalidad para busqueda personalizada de ofertas
    @GetMapping("/lista")
    public String listar(ModelMap modelo, @Param("palabraClave") String palabraClave) {
        List<Oferta> ofertas = ofertaServicio.listAll(palabraClave);
        modelo.addAttribute("ofertas", ofertas);
        modelo.addAttribute("palabraClave", palabraClave);
        return "oferta_list";
    }

    @GetMapping("/modificar/{idOferta}")
    public String modificar(@PathVariable String idOferta, ModelMap modelo) {

        modelo.put("oferta", ofertaServicio.getOne(idOferta));

        return "oferta_modificar.html";
    }

    @PostMapping("/modificar/{idOferta}")
    public String modificar(@PathVariable String idOferta, String nombreOferta, String precio, MultipartFile archivo,
            ModelMap modelo, Date altaOferta) {
        try {
            ofertaServicio.modificarOferta(archivo, nombreOferta, precio, idOferta);

            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "oferta_modificar.html"; // oferta modificar
        }

    }

    @GetMapping("/imagen/{idOferta}") // para devolver imagen como cartas
    public ResponseEntity<byte[]> imagenOferta(@PathVariable String idOferta) {

        Oferta oferta = ofertaServicio.getOne(idOferta);

        byte[] imagen = oferta.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG); // se va a recibir una imagen de tipo JPEG

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    // PARA ELIMINAR
    @GetMapping("/eliminar/{idOferta}")
    public String eliminar(@PathVariable String idOferta, ModelMap modelo) {

        modelo.put("oferta", ofertaServicio.getOne(idOferta));
        return "eliminar_oferta.html";
    }

    // PARA ELIMINAR
    @PostMapping("/eliminado/{idOferta}")
    public String eliminado(@PathVariable String idOferta, ModelMap modelo) {

        ofertaServicio.borrarPorId(idOferta);

        return "redirect:../lista";
    }

    @GetMapping("/exportarPDF")
    public void exportarListadoDeOfertasEnPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Clientes_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);

        List<Oferta> ofertas = ofertaServicio.listarOfertas(); // cargo la lista
        OfertaExporterPDF exporter = new OfertaExporterPDF(ofertas);
        exporter.exportar(response);

    }

}
