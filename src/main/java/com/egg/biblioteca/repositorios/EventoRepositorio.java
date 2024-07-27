package com.egg.biblioteca.repositorios;

import com.egg.biblioteca.entidades.Evento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepositorio extends JpaRepository<Evento, String> {

    @Query("SELECT e FROM Evento e WHERE" 
            + " CONCAT(e.idEvento, e.nombreEvento)"
            + " LIKE %?1%")
    public List<Evento> findAll(String palabraClave);

}

//FUNCIONALIDAD PARA APLICAR FILTROS DE EVENTOS 
