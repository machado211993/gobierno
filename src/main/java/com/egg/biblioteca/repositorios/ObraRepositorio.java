package com.egg.biblioteca.repositorios;

import com.egg.biblioteca.entidades.Obra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ObraRepositorio extends JpaRepository<Obra, String> {
    @Query("SELECT o FROM Obra o WHERE"
            + " CONCAT(o.idObra, o.nombreObra, o.altaObra)"
            + " LIKE %?1%")
    public List<Obra> findAll(String palabraClave);
}
