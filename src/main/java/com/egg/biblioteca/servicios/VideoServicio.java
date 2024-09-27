package com.egg.biblioteca.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.egg.biblioteca.entidades.Video;
import com.egg.biblioteca.repositorios.VideoRepositorio;

import java.util.List;

@Service
public class VideoServicio {

    @Autowired
    private VideoRepositorio videoRepository;

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Video getVideoById(Long id) {
        return videoRepository.findById(id).orElse(null);
    }
}
