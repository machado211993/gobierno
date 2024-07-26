package com.egg.biblioteca.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class NavbarControlador {
    @GetMapping("/acerca")
    public String acerca() {
        return "acerca_de.html";
    }

    @GetMapping("/contactos")
    public String contactos() {
        return "contactos.html";

    }

    @GetMapping("/avisos")
    public String avisosImportantes() {
        return "avisos.html";
    }

    @GetMapping("/quienes_somos")
    public String quienesSomos() {
        return "quienes_somos.html";
    }

}
