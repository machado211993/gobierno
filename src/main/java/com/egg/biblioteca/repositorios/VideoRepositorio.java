package com.egg.biblioteca.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.biblioteca.entidades.Video;

@Repository
public interface VideoRepositorio extends JpaRepository<Video, Long> {
}