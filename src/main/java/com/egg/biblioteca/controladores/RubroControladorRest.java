package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Rubro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.RubroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/rubros")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST} )

public class RubroControladorRest {
    @Autowired
    private RubroServicio rubroServicio;

    @PostMapping
    public ResponseEntity<?> crearRubro(@RequestBody String nombre) {
        try {
            rubroServicio.crearRubro(nombre);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (MiException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public List<Rubro> listarRubros() {
        return rubroServicio.listarRubros();
    }

    @GetMapping("/{idRubro}")
    public ResponseEntity<?> obtenerRubro(@PathVariable String idRubro) {
        try {
            Rubro rubro = rubroServicio.getOne(idRubro);
            return ResponseEntity.status(HttpStatus.OK).body(rubro);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rubro no encontrado");
        }
    }

    @PutMapping("/{idRubro}")
    public ResponseEntity<?> modificarRubro(@PathVariable String idRubro, @RequestBody String nombre) {
        try {
            rubroServicio.modificarRubros(idRubro, nombre);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MiException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idRubro}")
    public ResponseEntity<?> borrarRubro(@PathVariable String idRubro) {
        rubroServicio.borrarPorId(idRubro);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}