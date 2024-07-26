
package com.egg.biblioteca.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/controller")
public class Controlador {

    @GetMapping("/holamundo")
    @ResponseBody

    public String mostrarMensaje() {
        return "hola mundo en spring boot";
    }

}
