package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Acto;
import com.egg.biblioteca.entidades.Evento;
import com.egg.biblioteca.entidades.Obra;
import com.egg.biblioteca.entidades.Aviso;
import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.ActoServicio;
import com.egg.biblioteca.servicios.AvisoServicio;
import com.egg.biblioteca.servicios.EventoServicio;
import com.egg.biblioteca.servicios.ObraServicio;
import com.egg.biblioteca.servicios.UsuarioServicio;
import com.egg.biblioteca.util.reportes.UsuarioExporterPDF;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private EventoServicio eventoServicio;
    @Autowired
    private ObraServicio obraServicio;
    @Autowired
    private ActoServicio actoServicio;
    @Autowired
    private AvisoServicio avisoServicio;

    @GetMapping("/registrar") // registro cliente formulario
    public String registrar() {
        return "registro.html";
    }

    @PostMapping("/registro") // cliente registrado
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
            String password2, ModelMap modelo, MultipartFile archivo) {

        try {
            usuarioServicio.registrar(archivo, nombre, email, password, password2);

            modelo.put("exito", "Usuario registrado correctamente!");
            return "index.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "registro.html";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o Contraseña invalidos!");
        }

        return "login.html";
    }

    // inicio
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        // LISTADO PARA RECORRER LAS CARTAS EN INICIO Y CAROUSEL
        List<Obra> obras = obraServicio.listarObras();
        modelo.addAttribute("obras", obras);
        List<Acto> actos = actoServicio.listarActos();
        modelo.addAttribute("actos", actos);
        List<Evento> eventos = eventoServicio.listarEventos();
        modelo.addAttribute("eventos", eventos);
        List<Aviso> avisos = avisoServicio.listarAvisos();
        modelo.addAttribute("avisos", avisos);

        return "inicio.html";
    }

    // funcionalidad para modificar cliente EN SESSION
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "usuario_modificar.html";
    }

    // funcionalidad para modificar cliente por id
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("usuario", usuarioServicio.getOne(id));

        return "usuario_modificar.html";
    }

    // funcionalidad para modificar cliente por id
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(@RequestParam MultipartFile archivo, @PathVariable String id, @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {

        try {
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2);

            modelo.put("exito", "Usuario actualizado correctamente!");

            return "inicio.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "usuario_modificar.html";
        }

    }

    // funcionalidad para devolver productoServicio lista y ofertaServicio lista y
    // usuarioServicio lista EN INDEX
    @GetMapping("/")
    public String listar(ModelMap modelo) {
       
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        List<Obra> obras = obraServicio.listarObras();
        modelo.addAttribute("obras", obras);
        List<Acto> actos = actoServicio.listarActos();
        modelo.addAttribute("actos", actos);
        List<Evento> eventos = eventoServicio.listarEventos();
        modelo.addAttribute("eventos", eventos);
        List<Aviso> avisos = avisoServicio.listarAvisos();
        modelo.addAttribute("avisos", avisos);

        return "index";

    }

    @GetMapping("/listarUsuarios")
    public String listar(ModelMap modelo, @Param("palabraClave") String palabraClave) {
        List<Usuario> usuarios = usuarioServicio.listAll(palabraClave);
        modelo.addAttribute("usuarios", usuarios);
        modelo.addAttribute("palabraClave", palabraClave);
        return "usuario_list";
    }

    // PARA ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo) { // variable de ruta

        modelo.put("usuario", usuarioServicio.getOne(id));
        return "eliminar_usuario.html";
    }

    // PARA ELIMINAR
    @PostMapping("/eliminado/{id}")
    public String eliminado(@PathVariable String id, ModelMap modelo) {

        usuarioServicio.borrarPorId(id);

        return "redirect:../listarUsuarios"; // retornar a listarUsuarios
    }

    @GetMapping("/exportarPDF")
    public void exportarListadoDeUsuariosEnPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Clientes_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);

        List<Usuario> usuarios = usuarioServicio.listarUsuarios(); // cargo la lista
        UsuarioExporterPDF exporter = new UsuarioExporterPDF(usuarios);
        exporter.exportar(response);

    }

}