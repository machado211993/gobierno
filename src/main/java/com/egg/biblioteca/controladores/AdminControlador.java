package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Obra;
import com.egg.biblioteca.entidades.Evento;
import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.servicios.ObraServicio;
import com.egg.biblioteca.servicios.EventoServicio;
import com.egg.biblioteca.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private EventoServicio eventoServicio;
    @Autowired
    private ObraServicio obraServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Evento> eventos = eventoServicio.listarEventos();
        modelo.addAttribute("eventos", eventos);
        List<Obra> obras = obraServicio.listarObras();
        modelo.addAttribute("obras", obras);
        Page<Evento> productosPage = eventoServicio.listarPaginacion(page, size);
        modelo.addAttribute("eventos", productosPage.getContent());
        modelo.addAttribute("pageable", productosPage);
        return "panel.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);

        return "usuario_list";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id) {
        usuarioServicio.cambiarRol(id);

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/dashboard/{idEvento}")
    public ResponseEntity<byte[]> imagenProducto(@PathVariable String idEvento) {

        Evento producto = eventoServicio.getOne(idEvento);

        byte[] imagen = producto.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG); // se va a recibir una imagen de tipo JPEG

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    
    @GetMapping("/dashboard/{idObra}")
    public ResponseEntity<byte[]> imagenOferta(@PathVariable String idObra) {

        Obra obra = obraServicio.getOne(idObra);

        byte[] imagen = obra.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG); // se va a recibir una imagen de tipo JPEG

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

}
