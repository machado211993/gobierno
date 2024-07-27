package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Obra;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.ObraServicio;
import com.egg.biblioteca.util.reportes.ObraExporterPDF;
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
@RequestMapping("/obra")
public class ObraControlador {

    @Autowired
    private ObraServicio obraServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) { // metodo registro formulario
        return "obra_form";

    }

    @PostMapping("/registro") // metodo registrado
    public String registro(@RequestParam(required = false) String nombreObra,
            @RequestParam MultipartFile archivo, ModelMap modelo) {
        try {
            obraServicio.crearObra(archivo, nombreObra);
            modelo.put("exito", "la obra fue cargada correctamente");

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "obra_form";
        }
        return "index";

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
        List<Obra> obras = obraServicio.listAll(palabraClave);
        modelo.addAttribute("obras", obras);
        modelo.addAttribute("palabraClave", palabraClave);
        return "obra_list";
    }

    @GetMapping("/modificar/{idObra}")
    public String modificar(@PathVariable String idObra, ModelMap modelo) {

        modelo.put("obras", obraServicio.getOne(idObra));

        return "obra_modificar.html";
    }

    @PostMapping("/modificar/{idObra}")
    public String modificar(@PathVariable String idObra, String nombreObra, MultipartFile archivo,
            ModelMap modelo, Date altaObra) {
        try {
            obraServicio.modificarObra(archivo, nombreObra, idObra);

            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "obra_modificar.html"; // obra modificar
        }

    }

    @GetMapping("/imagen/{idObra}") // para devolver imagen como cartas
    public ResponseEntity<byte[]> imagenObra(@PathVariable String idObra) {

        Obra obra = obraServicio.getOne(idObra);

        byte[] imagen = obra.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG); // se va a recibir una imagen de tipo JPEG

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    // PARA ELIMINAR
    @GetMapping("/eliminar/{idObra}")
    public String eliminar(@PathVariable String idObra, ModelMap modelo) {

        modelo.put("obra", obraServicio.getOne(idObra));
        return "eliminar_obra.html";
    }

    // PARA ELIMINAR
    @PostMapping("/eliminado/{idObra}")
    public String eliminado(@PathVariable String idObra, ModelMap modelo) {

        obraServicio.borrarPorId(idObra);

        return "redirect:../lista";
    }

    @GetMapping("/exportarPDF")
    public void exportarListadoDeObrasEnPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Clientes_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);

        List<Obra> obras = obraServicio.listarObras(); // cargo la lista
        ObraExporterPDF exporter = new ObraExporterPDF(obras);
        exporter.exportar(response);

    }

}
