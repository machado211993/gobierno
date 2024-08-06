package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Acto;
import com.egg.biblioteca.entidades.Aviso;
import com.egg.biblioteca.entidades.Evento;
import com.egg.biblioteca.entidades.Obra;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.ActoServicio;
import com.egg.biblioteca.servicios.AvisoServicio;
import com.egg.biblioteca.servicios.EventoServicio;
import com.egg.biblioteca.servicios.ObraServicio;
import com.egg.biblioteca.util.reportes.AvisoExporterPDF;
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
@RequestMapping("/aviso")
public class AvisoControlador {

    @Autowired
    private ObraServicio obraServicio;
    @Autowired
    private EventoServicio eventoServicio;
    @Autowired
    private ActoServicio actoServicio;
    @Autowired
    private AvisoServicio avisoServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) { // metodo registro formulario
        return "aviso_form";

    }

    @PostMapping("/registro") // metodo registrado
    public String registro(@RequestParam(required = false) String nombreAviso,
            @RequestParam MultipartFile archivo, ModelMap modelo) {
        try {
            avisoServicio.crearAviso(archivo, nombreAviso);

            List<Aviso> avisos = avisoServicio.listarAvisos();
            modelo.addAttribute("avisos", avisos);

            modelo.put("exito", "el evento fue cargada correctamente");
            List<Obra> obras = obraServicio.listarObras();
            modelo.addAttribute("obras", obras);
            List<Acto> actos = actoServicio.listarActos();
            modelo.addAttribute("actos", actos);
            List<Evento> eventos = eventoServicio.listarEventos();
            modelo.addAttribute("eventos", eventos);
            return "index";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "acto_form";
        }

    }

    // @GetMapping("/lista")
    // public String listar(ModelMap modelo) {
    //
    // List<Obra> obras = obraServicio.listarObras();
    //
    // modelo.addAttribute("obras", obras);
    //
    // return "obra_list.html";
    // }
    // funcionalidad para busqueda personalizada de ofertas
    @GetMapping("/lista")
    public String listar(ModelMap modelo, @Param("palabraClave") String palabraClave) {
        List<Aviso> avisos = avisoServicio.listAll(palabraClave);
        modelo.addAttribute("avisos", avisos);
        modelo.addAttribute("palabraClave", palabraClave);
        return "aviso_list";
    }

    @GetMapping("/modificar/{idAviso}")
    public String modificar(@PathVariable String idAviso, ModelMap modelo) {

        modelo.put("avisos", avisoServicio.getOne(idAviso));

        return "aviso_modificar.html";
    }

    @PostMapping("/modificar/{idAviso}")
    public String modificar(@PathVariable String idAviso, String nombreAviso, MultipartFile archivo,
            ModelMap modelo, Date altaAviso) {
        try {
            avisoServicio.modificarAviso(archivo, nombreAviso, idAviso);

            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "aviso_modificar.html"; // aviso modificar
        }

    }

    @GetMapping("/imagen/{idAviso}") // para devolver imagen como cartas
    public ResponseEntity<byte[]> imagenAviso(@PathVariable String idAviso) {

        Aviso aviso = avisoServicio.getOne(idAviso);

        byte[] imagen = aviso.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG); // se va a recibir una imagen de tipo JPEG

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    // PARA ELIMINAR
    @GetMapping("/eliminar/{idAviso}")
    public String eliminar(@PathVariable String idAviso, ModelMap modelo) {

        modelo.put("aviso", avisoServicio.getOne(idAviso));
        return "eliminar_aviso.html";
    }

    // PARA ELIMINAR
    @PostMapping("/eliminado/{idAviso}")
    public String eliminado(@PathVariable String idAviso, ModelMap modelo) {

        avisoServicio.borrarPorId(idAviso);

        return "redirect:../lista";
    }

    @GetMapping("/exportarPDF")
    public void exportarListadoDeActoEnPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Clientes_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);

        List<Aviso> avisos = avisoServicio.listarAvisos(); // cargo la lista
        AvisoExporterPDF exporter = new AvisoExporterPDF(avisos);
        exporter.exportar(response);

    }

}
