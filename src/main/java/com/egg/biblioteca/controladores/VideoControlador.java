package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Video;
import com.egg.biblioteca.servicios.VideoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/videos")
public class VideoControlador {

    @Autowired
    private VideoServicio videoService;

    @GetMapping("/{filename}")
    public String viewVideo(@PathVariable String filename, Model model) {
        model.addAttribute("videoUrl", "/videos/" + filename);
        return "video-view";
    }
}
