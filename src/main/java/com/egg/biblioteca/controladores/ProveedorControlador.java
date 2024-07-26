
package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Proveedor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.ProveedorServicio;
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
@RequestMapping("/proveedor")
public class ProveedorControlador {

    @Autowired
    private ProveedorServicio proveedorServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "proveedor_form.html"; // autor_form
    }

    // FUNCIONALIDAD REGISTRAR PROVEEDOR
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) {

        try {
            proveedorServicio.crearProveedor(nombre);

            modelo.put("exito", "El Proveedor fue registrado correctamente!");
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            return "proveedor_form.html";
        }

        return "index.html";
    }

    // FUNCIONALIDAD LISTAR PROVEEDOR
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Proveedor> proveedores = proveedorServicio.listarProveedores();

        modelo.addAttribute("proveedores", proveedores);

        return "proveedor_list.html";
    }

    // FUNCIONALIDAD MODIFICAR PROVEEDOR
    @GetMapping("/modificar/{idProveedor}")
    public String modificar(@PathVariable String idProveedor, ModelMap modelo) {
        modelo.put("proveedor", proveedorServicio.getOne(idProveedor));

        return "proveedor_modificar.html";
    }

    @PostMapping("/modificar/{idProveedor}")
    public String modificar(@PathVariable String idProveedor, String nombre, ModelMap modelo) {
        try {
            proveedorServicio.modificarProveedor(nombre, idProveedor);

            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "proveedor_modificar.html";
        }

    }

    // PARA ELIMINAR PROVEEDOR
    @GetMapping("/eliminar/{idProveedor}")
    public String eliminar(@PathVariable String idProveedor, ModelMap modelo) {

        modelo.put("proveedor", proveedorServicio.getOne(idProveedor));
        return "eliminar_proveedor.html";
    }

    // PARA ELIMINAR PROVEEDOR
    @PostMapping("/eliminado/{idProveedor}")
    public String eliminado(@PathVariable String idProveedor, ModelMap modelo) {

        proveedorServicio.borrarPorId(idProveedor);

        return "redirect:../lista";
    }
}
