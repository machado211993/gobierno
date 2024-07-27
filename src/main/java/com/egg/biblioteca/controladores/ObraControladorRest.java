
package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Obra;
import com.egg.biblioteca.servicios.ObraServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST} )

public class ObraControladorRest {

    @Autowired
    private ObraServicio obraServicio;

    @RequestMapping(value = "/obrarest")
    public List<Obra> listar() {
        
        return obraServicio.listarObras();
    }

}
