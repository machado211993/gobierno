package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Acto;
import com.egg.biblioteca.entidades.Evento;
import com.egg.biblioteca.entidades.Obra;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.EventoRepositorio;
import com.egg.biblioteca.servicios.ActoServicio;
import com.egg.biblioteca.servicios.EventoServicio;
import com.egg.biblioteca.servicios.ObraServicio;
import com.egg.biblioteca.util.reportes.EventoExporterPDF;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/evento")
public class EventoControlador {
    @Autowired
    private ObraServicio obraServicio;
    @Autowired
    private EventoServicio eventoServicio;
    @Autowired
    private ActoServicio actoServicio;

    @GetMapping("/registrar") // localhost:8080/producto/registrar
    public String registrar(ModelMap modelo) {
        return "evento_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombreEvento, ModelMap modelo,
            @RequestParam(required = false) MultipartFile archivo, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            eventoServicio.crearEvento(archivo, nombreEvento, nombreEvento);
            ;
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

            return "evento_form.html"; // volvemos a cargar el formulario. ///evento form
        }

    }

    // paginacion
    @GetMapping("/lista")
    public String listar(ModelMap modelo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Evento> productosPage = eventoServicio.listarPaginacion(page, size);
        modelo.addAttribute("eventos", productosPage.getContent());
        modelo.addAttribute("pageable", productosPage);

        return "evento_list";
    }

    // filtrado
    @GetMapping("/filtrar")
    public String listar(ModelMap modelo, @Param("palabraClave") String palabraClave) {
        List<Evento> eventos = eventoServicio.listAll(palabraClave);
        modelo.addAttribute("eventos", eventos);
        modelo.addAttribute("palabraClave", palabraClave);

        return "evento_list";
    }

    @GetMapping("/modificar/{idEvento}")
    public String modificar(@PathVariable String idEvento, ModelMap modelo) {

        modelo.put("evento", eventoServicio.getOne(idEvento));

        return "evento_modificar.html";
    }

    @PostMapping("/modificar/{idEvento}")
    public String modificar(@PathVariable String idEvento, MultipartFile archivo, String nombreEvento,
            ModelMap modelo) {
        try {

            eventoServicio.modificarEvento(archivo, idEvento, nombreEvento);

            return "redirect:../lista";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());

            return "evento_modificar.html";
        }

    }

    @GetMapping("/imagen/{idEvento}") // para devolver imagen como cartas
    public ResponseEntity<byte[]> imagenEvento(@PathVariable String idEvento) {

        Evento evento = eventoServicio.getOne(idEvento);

        byte[] imagen = evento.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG); // se va a recibir una imagen de tipo JPEG

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    // PARA ELIMINAR
    @GetMapping("/eliminar/{idEvento}")
    public String eliminar(@PathVariable String idEvento, ModelMap modelo) {

        modelo.put("evento", eventoServicio.getOne(idEvento));
        return "eliminar_evento.html";
    }

    // PARA ELIMINAR
    @PostMapping("/eliminado/{idEvento}")
    public String eliminado(@PathVariable String idEvento, ModelMap modelo) {

        eventoServicio.borrarPorId(idEvento);

        return "redirect:../lista";
    }

    // PARA REPORTES PDF DE EVENTOS
    @GetMapping("/exportarPDF")
    public void exportarListadoDeEventosEnPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Clientes_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);

        List<Evento> eventos = eventoServicio.listarEventos(); // cargo la lista
        EventoExporterPDF exporter = new EventoExporterPDF(eventos);
        exporter.exportar(response);
    }
}
