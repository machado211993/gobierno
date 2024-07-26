package com.egg.biblioteca.controladores;


import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST} )
@RequestMapping("/api")
public class PortalControladorRest {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @RequestMapping(value = "/usuariorest")
    public List<Usuario> listar() {
        
        return usuarioServicio.listarUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario getUserById(@PathVariable String id) {
        return usuarioServicio.getOne(id);
    }

   /*  @PostMapping
    public Usuario createUser(@RequestBody Usuario usuario) {
        return usuarioServicio.registrar(usuario);
    }*/

   /*  @PutMapping("/{id}")
    public Usuario updateUser(@PathVariable String id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        return usuarioServicio.actualizar(usuario);
    }*/

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        usuarioServicio.borrarPorId(id);
    }

}
