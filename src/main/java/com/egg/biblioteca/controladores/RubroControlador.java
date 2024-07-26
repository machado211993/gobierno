package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Rubro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.RubroServicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rubro")
public class RubroControlador {

    @Autowired
    private RubroServicio rubroServicio;

    @GetMapping("/registrar") // localhost:8080/rubro/registrar
    public String registrar() {
        return "rubro_form.html"; // rubro form
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) {

        try {
            rubroServicio.crearRubro(nombre);

            modelo.put("exito", "El rubro fue registrado correctamente!");
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            return "rubro_form.html";
        }

        return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Rubro> rubros = rubroServicio.listarRubros();

        modelo.addAttribute("rubros", rubros);

        return "rubro_list";
    }

    // FUNCIONALIDAD MODIFICAR RUBRO
    @GetMapping("/modificar/{idRubro}")
    public String modificar(@PathVariable String idRubro, ModelMap modelo) {
        modelo.put("rubro", rubroServicio.getOne(idRubro));

        return "rubro_modificar.html";
    }

    @PostMapping("/modificar/{idRubro}")
    public String modificar(@PathVariable String idRubro, String nombre, ModelMap modelo) {
        try {
            rubroServicio.modificarRubros(idRubro, nombre);

            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "rubro_modificar.html";
        }

    }

    // PARA ELIMINAR RUBRO
    @GetMapping("/eliminar/{idRubro}")
    public String eliminar(@PathVariable String idRubro, ModelMap modelo) {

        modelo.put("rubro", rubroServicio.getOne(idRubro));
        return "eliminar_rubro.html";
    }

    // PARA ELIMINAR RUBRO
    @PostMapping("/eliminado/{idRubro}")
    public String eliminado(@PathVariable String idRubro, ModelMap modelo) {

        rubroServicio.borrarPorId(idRubro);

        return "redirect:../lista";
    }

}
