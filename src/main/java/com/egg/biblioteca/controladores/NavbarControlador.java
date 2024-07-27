package com.egg.biblioteca.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class NavbarControlador {
    @GetMapping("/acerca_de")
    public String acerca() {
        return "acerca_de.html";
    }

    @GetMapping("/contactos")
    public String contactos() {
        return "contactos.html";

    }

    @GetMapping("/avisos_importantes")
    public String avisosImportantes() {
        return "avisos_importantes.html";
    }

    @GetMapping("/quienes_somos")
    public String quienesSomos() {
        return "quienes_somos.html";
    }

}
