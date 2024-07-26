package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Proveedor;
import com.egg.biblioteca.servicios.ProveedorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST} )

public class ProveedorControladorRest {

    @Autowired
    private ProveedorServicio proveedorServicio;

    @RequestMapping(value = "/proveedorrest")
    public List<Proveedor> listar() {
        
        return proveedorServicio.listarProveedores();
    }

}

