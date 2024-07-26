package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Oferta;
import com.egg.biblioteca.entidades.Producto;
import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.servicios.OfertaServicio;
import com.egg.biblioteca.servicios.ProductoServicio;
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
    private ProductoServicio productoServicio;
    @Autowired
    private OfertaServicio ofertaServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Producto> productos = productoServicio.listarProductos();
        modelo.addAttribute("productos", productos);
        List<Oferta> ofertas = ofertaServicio.listarOfertas();
        modelo.addAttribute("ofertas", ofertas);
        Page<Producto> productosPage = productoServicio.listarPaginacion(page, size);
        modelo.addAttribute("productos", productosPage.getContent());
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

    @GetMapping("/dashboard/{idProducto}")
    public ResponseEntity<byte[]> imagenProducto(@PathVariable String idProducto) {

        Producto producto = productoServicio.getOne(idProducto);

        byte[] imagen = producto.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG); // se va a recibir una imagen de tipo JPEG

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    
    @GetMapping("/dashboard/{idOferta}")
    public ResponseEntity<byte[]> imagenOferta(@PathVariable String idOferta) {

        Oferta oferta = ofertaServicio.getOne(idOferta);

        byte[] imagen = oferta.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG); // se va a recibir una imagen de tipo JPEG

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

}
