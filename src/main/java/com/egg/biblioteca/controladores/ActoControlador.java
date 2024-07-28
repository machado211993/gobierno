package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Acto;
import com.egg.biblioteca.entidades.Evento;
import com.egg.biblioteca.entidades.Obra;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.ActoServicio;
import com.egg.biblioteca.servicios.EventoServicio;
import com.egg.biblioteca.servicios.ObraServicio;
import com.egg.biblioteca.util.reportes.ActoExporterPDF;
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
@RequestMapping("/acto")
public class ActoControlador {

    @Autowired
    private ObraServicio obraServicio;
    @Autowired
    private EventoServicio eventoServicio;
    @Autowired
    private ActoServicio actoServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) { // metodo registro formulario
        return "acto_form";

    }

    @PostMapping("/registro") // metodo registrado
    public String registro(@RequestParam(required = false) String nombreActo,
            @RequestParam MultipartFile archivo, ModelMap modelo) {
        try {
            actoServicio.crearActo(archivo, nombreActo);

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
        List<Acto> actos = actoServicio.listAll(palabraClave);
        modelo.addAttribute("actos", actos);
        modelo.addAttribute("palabraClave", palabraClave);
        return "acto_list";
    }

    @GetMapping("/modificar/{idActo}")
    public String modificar(@PathVariable String idActo, ModelMap modelo) {

        modelo.put("actos", actoServicio.getOne(idActo));

        return "acto_modificar.html";
    }

    @PostMapping("/modificar/{idActo}")
    public String modificar(@PathVariable String idActo, String nombreActo, MultipartFile archivo,
            ModelMap modelo, Date altaActo) {
        try {
            actoServicio.modificarActo(archivo, nombreActo, idActo);

            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "acto_modificar.html"; // acto modificar
        }

    }

    @GetMapping("/imagen/{idActo}") // para devolver imagen como cartas
    public ResponseEntity<byte[]> imagenActo(@PathVariable String idActo) {

        Acto acto = actoServicio.getOne(idActo);

        byte[] imagen = acto.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG); // se va a recibir una imagen de tipo JPEG

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    // PARA ELIMINAR
    @GetMapping("/eliminar/{idActo}")
    public String eliminar(@PathVariable String idActo, ModelMap modelo) {

        modelo.put("acto", actoServicio.getOne(idActo));
        return "eliminar_acto.html";
    }

    // PARA ELIMINAR
    @PostMapping("/eliminado/{idActo}")
    public String eliminado(@PathVariable String idActo, ModelMap modelo) {

        actoServicio.borrarPorId(idActo);

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

        List<Acto> actos = actoServicio.listarActos(); // cargo la lista
        ActoExporterPDF exporter = new ActoExporterPDF(actos);
        exporter.exportar(response);

    }

}
